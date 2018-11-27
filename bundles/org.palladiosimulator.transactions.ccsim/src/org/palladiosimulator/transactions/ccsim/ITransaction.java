package org.palladiosimulator.transactions.ccsim;

import java.util.Set;

import org.palladiosimulator.pcmtx.Table;
import org.palladiosimulator.transactions.ccsim.listener.TransactionListener;

public interface ITransaction {

    int getId();
    
    void suspend(TransactionCallback resumeCallback);
    
    void resume();
    
    void grant(LockDescriptor lock);

    void revoke(LockDescriptor lock);
    
    void waitFor(LockDescriptor lock);
    
    void clearLocks();
    
    boolean holdsAny(Set<LockDescriptor> locks);

    boolean holds(LockDescriptor lock);
    
    int grantedResourcesCount(Table table);

    Set<LockDescriptor> getGrantedLocks();

    Set<LockDescriptor> getPendingLocks();
    
    boolean isRollbackOnly();

    void setRollbackOnly();

    boolean isSuspended();
    
    void addTransactionListener(TransactionListener listener);
    
    void removeTransactionListener(TransactionListener listener);

}
