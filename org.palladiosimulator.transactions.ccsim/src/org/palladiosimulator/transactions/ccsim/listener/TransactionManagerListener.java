package org.palladiosimulator.transactions.ccsim.listener;

import org.palladiosimulator.transactions.ccsim.ITransaction;

public interface TransactionManagerListener {

    public void commit(ITransaction tx);
    
    public void rollback(ITransaction tx, RollbackCause cause);
    
}
