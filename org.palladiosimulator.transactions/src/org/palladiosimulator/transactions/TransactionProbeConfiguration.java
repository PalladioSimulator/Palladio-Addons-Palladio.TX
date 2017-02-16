package org.palladiosimulator.transactions;

import org.palladiosimulator.transactions.ccsim.LockManager;
import org.palladiosimulator.transactions.ccsim.TransactionManager;

import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.eventsim.measurement.ProbeConfiguration;

public class TransactionProbeConfiguration implements ProbeConfiguration {

    private TransactionManager transactionManager;
    
    private LockManager lockManager;
    
    private ISimulationModel simulationModel;
    
    public TransactionProbeConfiguration(TransactionManager transactionManager, LockManager lockManager, ISimulationModel simulationModel) {
        this.transactionManager = transactionManager;
        this.lockManager = lockManager;
        this.simulationModel = simulationModel;
    }
    
    public LockManager getLockManager() {
        return lockManager;
    }
    
    public ISimulationModel getSimulationModel() {
        return simulationModel;
    }
    
    public TransactionManager getTransactionManager() {
        return transactionManager;
    }
    
}
