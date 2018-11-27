package org.palladiosimulator.transactions.ccsim;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import org.palladiosimulator.pcmtx.Table;
import org.palladiosimulator.transactions.ccsim.listener.TransactionListener;

public class TransactionImpl implements ITransaction {

    private static final AtomicInteger idGenerator = new AtomicInteger();

    private TransactionCallback resumeCallback;

    // TODO isolation level

    private Set<LockDescriptor> grantedLocks;

    private Set<LockDescriptor> pendingLocks;

    private boolean suspended;

    private int id;

    private boolean rollbackOnly;
    
    private List<TransactionListener> listeners = new CopyOnWriteArrayList<>();
    
    public TransactionImpl() {
        grantedLocks = new HashSet<>();
        pendingLocks = new HashSet<>();
        this.id = idGenerator.getAndIncrement();
    }

    @Override
    public void suspend(TransactionCallback callback) {
        // TODO notify listeners?
        this.resumeCallback = callback;
        suspended = true;
    }

    public void resume() {
        listeners.forEach(l -> l.beforeResume());
        if (suspended && resumeCallback != null) {
            suspended = false;
            resumeCallback.resume();
        }
    }

    @Override
    public boolean holdsAny(Set<LockDescriptor> locks) {
        for (LockDescriptor l : locks) {
            if (grantedLocks.contains(l)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean holds(LockDescriptor lock) {
        // return ownedRows.containsKey(table) && ownedRows.get(table).contains(rowId);
        return grantedLocks.contains(lock);
    }

    @Override
    public void grant(LockDescriptor lock) {
        pendingLocks.remove(lock);
        grantedLocks.add(lock);

        // resume once all locks have been granted
        // TODO better resume from "outside"?
        if (pendingLocks.isEmpty()) {
            resume();
        }
    }

    @Override
    public int grantedResourcesCount(Table table) {
        int count = 0;
        // TODO perhaps improve computational complexity
        for (LockDescriptor lock : grantedLocks) {
            if (lock.getLock().getTable().getId().equals(table.getId())) {
                count++;
            }
        }
        return count;
    }

    @Override
    public void waitFor(LockDescriptor lock) {
        pendingLocks.add(lock);
    }

    @Override
    public Set<LockDescriptor> getGrantedLocks() {
        return grantedLocks;
    }

    @Override
    public Set<LockDescriptor> getPendingLocks() {
        return pendingLocks;
    }

    @Override
    public void clearLocks() {
        grantedLocks.clear();
        pendingLocks.clear();
    }

    @Override
    public void revoke(LockDescriptor lock) {
        grantedLocks.remove(lock);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public boolean isRollbackOnly() {
        return rollbackOnly;
    }

    @Override
    public void setRollbackOnly() {
        rollbackOnly = true;
    }

    @Override
    public boolean isSuspended() {
        return suspended;
    }

    @Override
    public void addTransactionListener(TransactionListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeTransactionListener(TransactionListener listener) {
        this.listeners.remove(listener);
    }

}
