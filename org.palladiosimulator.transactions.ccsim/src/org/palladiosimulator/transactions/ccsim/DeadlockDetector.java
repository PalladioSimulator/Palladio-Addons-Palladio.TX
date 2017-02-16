package org.palladiosimulator.transactions.ccsim;

import java.util.Set;

import org.palladiosimulator.transactions.ccsim.listener.RollbackCause;

public class DeadlockDetector {

    private TransactionManagerImpl transactionManager;
    
    public DeadlockDetector(TransactionManagerImpl transactionManager) {
        this.transactionManager = transactionManager;
    }
    
    private WaitForGraph buildWaitForGraph() {
        WaitForGraph g = new WaitForGraph();
        for (TableLockManager tlm : transactionManager.getLockManager().getTableLockManagers()) {
            for (LockQueue queue : tlm.getLockTable().values()) {
                g.add(queue);
            }
        }
        return g;
    }

    public void resolveDeadlocks() {
        WaitForGraph wfg = buildWaitForGraph();
        Set<ITransaction> deadlocked = wfg.findCycles();
        while (deadlocked.size() > 0) {
            System.out.println("DEADLOCK! Number of nodes involved: " + deadlocked.size());
            System.out.println(wfg.toDOT());
            System.out.println("-----------------------------");
            // rollback first TX
            // TODO implement more elaborate victim selection?
            ITransaction victim = deadlocked.iterator().next();
            transactionManager.rollback(victim, RollbackCause.DEADLOCK);

            // System.out.println("<<<<<<<<<<<<");
            // System.out.println(buildWaitForGraph().toDOT());
            // System.out.println(">>>>>>>>>>>>");
            //
            // // TODO remove
            // if (buildWaitForGraph().findCycles().size() > 0) {
            // throw new RuntimeException("There is still a deadlock, should not happen");
            // // detectDeadlocks();
            // }

            // more deadlocks to resolve?
            wfg = buildWaitForGraph();
            deadlocked = wfg.findCycles();
        }
    }
    
}
