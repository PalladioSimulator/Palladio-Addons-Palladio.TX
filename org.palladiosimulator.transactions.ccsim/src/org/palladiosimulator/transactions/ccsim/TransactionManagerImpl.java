package org.palladiosimulator.transactions.ccsim;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.palladiosimulator.transactions.ccsim.listener.RollbackCause;
import org.palladiosimulator.transactions.ccsim.listener.TransactionManagerListener;

public class TransactionManagerImpl implements TransactionManager {

    private LockManager lockManager;

    private List<TransactionManagerListener> listeners = new CopyOnWriteArrayList<>();

    public TransactionManagerImpl(LockManager lockManager) {
        this.lockManager = lockManager;
    }

    public LockManager getLockManager() {
        return lockManager;
    }

    @Override
    public ITransaction begin() {
        return new TransactionImpl();
    }

    @Override
    public boolean queryOrExecute(ITransaction tx, List<TableAccess> tableAccesses) {
        if (tableAccesses.isEmpty()) {
            // fail fast
            throw new RuntimeException("List of table accesses may not be null.");
        }

        boolean granted = false;
        for (TableAccess tableAccess : tableAccesses) {
            TableLockManager tableLockManager = lockManager.get(tableAccess.getTable());
            switch (tableAccess.getType()) {
            case "Read":
                granted = tableLockManager.lock(tx, tableAccess.getQuantity(), LockMode.SHARED);
                break;
            case "Update":
                granted = tableLockManager.lock(tx, tableAccess.getQuantity(), LockMode.EXCLUSIVE);
                break;
            case "Insert":
                // TODO
                granted = true;
                break;
            default:
                throw new RuntimeException("Unknown access type: " + tableAccess.getType());
            }

            // resolve deadlocks, if any
            new DeadlockDetector(this).resolveDeadlocks();

            // stop further requests if TX is marked for rollback (e.g. because TX has been chosen
            // as victim of deadlock resolution)
            if (tx.isRollbackOnly()) {
                return false;
            }
        }
        return granted;
    }

    @Override
    public void commit(ITransaction tx) {
        // LOG.info("Committing " + tx);
        releaseLocksHeldOrRequestedBy(tx);

        if (tx.isSuspended()) {
            // TODO log warning instead
            System.out.println("Warn: committing suspended TX");
//            throw new RuntimeException("suspended");
        }

        // notify listeners
        listeners.forEach(l -> l.commit(tx));
    }

    @Override
    public void rollback(ITransaction tx, RollbackCause cause) {
        System.out.println("Rolling back " + tx);
        // LOG.info("Rolling back " + tx);
        releaseLocksHeldOrRequestedBy(tx);

        // mark TX for rollback
        tx.setRollbackOnly();

        if (tx.isSuspended()) {
            tx.resume();
        }

        // notify listeners
        listeners.forEach(l -> l.rollback(tx, cause));
    }

    private void releaseLocksHeldOrRequestedBy(ITransaction tx) {
        tx.getGrantedLocks().forEach(lock -> {
            lockManager.release(lock);
        });
        tx.getPendingLocks().forEach(lock -> {
            lockManager.release(lock);
        });
        tx.clearLocks();
    }

    @Override
    public void addListener(TransactionManagerListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListener(TransactionManagerListener l) {
        listeners.remove(l);
    }

}
