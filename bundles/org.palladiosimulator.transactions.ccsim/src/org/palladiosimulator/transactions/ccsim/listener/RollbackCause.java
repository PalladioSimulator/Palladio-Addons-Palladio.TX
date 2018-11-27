package org.palladiosimulator.transactions.ccsim.listener;

public enum RollbackCause {

    DEADLOCK, TIMEOUT, UNSPECIFIED; 
    
    // TODO MVCC_CONCURRENT_WRITE
    
    // TODO JPA_OPTIMSITIC_LOCKING_EXCEPTION

}
