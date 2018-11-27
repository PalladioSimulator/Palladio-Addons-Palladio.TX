package org.palladiosimulator.transactions;

import java.util.List;

import org.apache.log4j.Logger;
import org.palladiosimulator.transactions.ccsim.ITransaction;
import org.palladiosimulator.transactions.ccsim.LockManager;
import org.palladiosimulator.transactions.ccsim.TableAccess;
import org.palladiosimulator.transactions.ccsim.TransactionManager;
import org.palladiosimulator.transactions.ccsim.TransactionManagerImpl;
import org.palladiosimulator.transactions.ccsim.listener.RollbackCause;
import org.palladiosimulator.transactions.ccsim.listener.TransactionManagerListener;
import org.palladiosimulator.transactions.entities.TransactionEntity;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.eventsim.system.entities.Request;

@Singleton
public class TransactionManagerWrapper implements TransactionManager {

    private static Logger LOG = Logger.getLogger(TransactionManagerWrapper.class);

    private TransactionManagerImpl delegate;

    private ISimulationModel model;

    @Inject
    public TransactionManagerWrapper(SchemaModel schemaModel, ISimulationModel simulationModel) {
        this.model = simulationModel;
        this.delegate = new TransactionManagerImpl(new LockManager(schemaModel.getTables())); // TODO
    }

    public LockManager getLockManager() {
        return delegate.getLockManager();
    }

    public TransactionEntity begin(Request request) {
        // TODO get ITransaction from superclass by calling begin()?
        return new TransactionEntity(model, request);
    }

    @Override
    public boolean queryOrExecute(ITransaction tx, List<TableAccess> tableAccesses) {
        return delegate.queryOrExecute(tx, tableAccesses);
    }

    @Override
    public void commit(ITransaction tx) {
        delegate.commit(tx);
    }

    @Override
    public void rollback(ITransaction tx, RollbackCause cause) {
        delegate.rollback(tx, cause);
    }

    public String toString() {
        return delegate.toString();
    }

    @Override
    public ITransaction begin() {
        throw new UnsupportedOperationException("Use signature begin(Request) instead");
    }

    @Override
    public void addListener(TransactionManagerListener l) {
        delegate.addListener(l);
    }

    @Override
    public void removeListener(TransactionManagerListener l) {
        delegate.removeListener(l);
    }

}
