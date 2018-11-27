package org.palladiosimulator.transactions.ccsim;

import java.util.List;

import org.palladiosimulator.transactions.ccsim.listener.RollbackCause;
import org.palladiosimulator.transactions.ccsim.listener.TransactionManagerListener;

public interface TransactionManager {

    ITransaction begin();

    void commit(ITransaction tx);

    void rollback(ITransaction tx, RollbackCause cause);

    boolean queryOrExecute(ITransaction tx, List<TableAccess> tableAccesses);

    void addListener(TransactionManagerListener l);

    void removeListener(TransactionManagerListener l);

}
