package org.palladiosimulator.transactions.ccsim;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import org.palladiosimulator.pcmtx.Table;
import org.palladiosimulator.transactions.ccsim.listener.LockingListener;

import edu.kit.ipd.sdq.randomutils.RandomVariable;
import edu.kit.ipd.sdq.randomutils.factories.TheoreticalDistribution;

public class TableLockManager {

    private RandomVariable<Integer> rowIdRandom;

    // private Map<Integer, LockMode> locks; // TODO map to Lock?

    private Table table;

    /** maps resource id to lock queue */
    private Map<Lock, LockQueue> lockTable;

    private List<LockingListener> lockingListeners = new CopyOnWriteArrayList<>();

    public TableLockManager(Table table) {
        this.table = table;

        if (table.getRows() < 1) {
            throw new RuntimeException("Tables are required to have at least 1 row, but table " + table.getEntityName()
                    + " has  " + table.getRows() + " rows.");
        }
        // TODO read distribution from model
        rowIdRandom = TheoreticalDistribution.zipf(table.getRows(), 1.0);

        lockTable = new HashMap<>();
    }

    public boolean lock(ITransaction tx, int quantity, LockMode type) {
        // if tx already holds some of the requested quantity, reduce the requested quantity
        // accordingly (TODO reflect in metamodel?)
        // TODO did not subtract granted resources, because if an exclusive lock is requested and a
        // shared lock is granted already, this results in a remaining quantity if 0, which is not
        // the expected behaviour
        int remainingQuantity = quantity; // - tx.grantedResourcesCount(table);

        // TODO consider mode / lock upgrades

        boolean allGranted = true;
        for (int i = 0; i < remainingQuantity; i++) {
            // TODO what if tx already holds that row?
            int rowId = rowIdRandom.next();
            boolean granted = lockRow(tx, rowId, type);
            if (!granted) {
                allGranted = false;
            }
        }

        notifyLockingListeners();

        return allGranted;
    }

    public void release(LockDescriptor lock) {
        // cannot release locks for tables managed by other table managers
        if (!lock.getLock().getTable().equals(table)) {
            throw new RuntimeException(
                    "Lock manager for table " + table + " cannot release locks for table " + lock.getLock().getTable());
        }

        LockQueue queue = lockTable.get(lock.getLock());

        // remove lock from corresponding active queue
        boolean removedActive = queue.removeActive(lock);

        // remove lock from corresponding waiting queue
        boolean removedWaiting = queue.removeWaiting(lock);

        if (!removedActive && !removedWaiting) {
            System.out.println("Warning: could not find " + lock + " to be removed.");
        }

        // grant waiting locks, if possible
        processWaiting(queue);

        // remove unused lock descriptors from lock table
        if (queue.isEmpty()) {
            lockTable.remove(lock.getLock());
        }

        // TODO call tx.revoke()??
        
        notifyLockingListeners();
    }

    private void processWaiting(LockQueue queue) {
        boolean granted = true;
        while (granted && queue.hasWaiting()) {
            LockMode requestedMode = queue.peekWaiting().getType(); // peek waiting queue head
            LockMode grantedMode = queue.getActiveMode();
            if (LockMode.isCompatible(requestedMode, grantedMode)) {
                // move waiting queue head to active queue
                LockDescriptor head = queue.pollWaiting();
                queue.addActive(head);

                // notify waiting ITransactions
                head.getTx().grant(head);
            } else {
                granted = false; // breaks loop
            }
        }
    }

    /**
     * 
     * @param tx
     * @param rowId
     * @param requestedLockType
     * @return {@code true}, if lock has been granted; {@code false}, else
     */
    protected boolean lockRow(ITransaction tx, int rowId, LockMode requestedLockType) {
        LockDescriptor lockRequest = new LockDescriptor(tx, new Lock(table, rowId), requestedLockType);

        // if requested row is not yet locked, acquire lock
        if (!isLocked(lockRequest.getLock())) {
            grantAndNotifyTX(lockRequest);
            return true;
        }

        // if TX already holds the requested lock or a stronger lock, we're done
        if (tx.holds(lockRequest) || tx.holdsAny(lockRequest.getStrongerLocks())) {
            return true;
        }

        // grant shared access, if request mode is compatible with active queue mode
        // TODO use compatibility table
        LockQueue queue = lockTable.get(lockRequest.getLock());
        if (queue.getActiveMode() == LockMode.SHARED && lockRequest.getType() == LockMode.SHARED) {
            grantAndNotifyTX(lockRequest);
            return true;
        }

        // try upgrading lock, if TX holds weaker lock than requested
        if (tx.holdsAny(lockRequest.getWeakerLocks())) {
            boolean immediateUpgrade;
            if (queue.getActiveCount() == 1) { // upgrade lock
                LockDescriptor weakerLock = queue.getActive().peek();
                queue.removeActive(weakerLock);
                weakerLock.getTx().revoke(weakerLock);
                grantAndNotifyTX(lockRequest);
                immediateUpgrade = true;
            } else {// don't upgrade
                boolean priority = true;
                waitAndNotifyTX(lockRequest, priority);
                immediateUpgrade = false;
            }

            // release/revoke weaker locks
            for (LockDescriptor lock : lockRequest.getWeakerLocks()) {
                if (tx.holds(lock)) {
                    tx.revoke(lock);
                    release(lock);
                }
            }
            return immediateUpgrade;
        }

        // requested row is locked by someone else, wait for lock
        waitAndNotifyTX(lockRequest, false);
        return false;
    }

    private void grantAndNotifyTX(LockDescriptor lockRequest) {
        LockQueue queue = getOrCreateLockQueue(lockRequest.getLock());
        queue.addActive(lockRequest);

        // notify tx
        lockRequest.getTx().grant(lockRequest);
    }

    private void notifyLockingListeners() {
        // number of locked rows per mode
        Map<LockMode, Integer> locksPerMode = new HashMap<>();
        for (LockQueue q : lockTable.values()) {
            int locks = locksPerMode.getOrDefault(q.getActiveMode(), 0);
            locksPerMode.put(q.getActiveMode(), ++locks);
        }

        // notify listeners about total lock count
        int lockCount = lockTable.size();
        lockingListeners.forEach(l -> l.lockedRowsCountChanged(lockCount));

        // notify listeners about lock count per lock mode
        for (Entry<LockMode, Integer> e : locksPerMode.entrySet()) {
            LockMode mode = e.getKey();
            Integer locks = e.getValue();
            lockingListeners.forEach(l -> l.lockedRowsCountPerModeChanged(mode, locks));
        }

        // mean waiting queue length per locked row
        int cumulatedWaitingQueueLength = 0;
        for (LockQueue queue : lockTable.values()) {
            cumulatedWaitingQueueLength += queue.getWaitingCount();
        }
        double meanWaitingQueueLength = (double) cumulatedWaitingQueueLength / lockCount;
        lockingListeners.forEach(l -> l.meanWaitingQueueLengthChanged(meanWaitingQueueLength));
    }

    private void waitAndNotifyTX(LockDescriptor lockRequest, boolean priority) {
        LockQueue queue = getOrCreateLockQueue(lockRequest.getLock());
        queue.addWaiting(lockRequest);

        // notify tx
        lockRequest.getTx().waitFor(lockRequest);
    }

    private LockQueue getOrCreateLockQueue(Lock lock) {
        if (!lockTable.containsKey(lock)) {
            lockTable.put(lock, new LockQueue());
        }
        return lockTable.get(lock);
    }

    private boolean isLocked(Lock lock) {
        return lockTable.containsKey(lock);
    }

    public Map<Lock, LockQueue> getLockTable() {
        return lockTable;
    }

    public void addLockingListener(LockingListener l) {
        lockingListeners.add(l);
    }

    public void removeLockingListener(LockingListener l) {
        lockingListeners.remove(l);
    }

}
