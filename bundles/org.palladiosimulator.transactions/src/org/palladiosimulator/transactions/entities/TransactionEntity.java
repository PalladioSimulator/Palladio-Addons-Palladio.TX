package org.palladiosimulator.transactions.entities;

import java.util.Set;

import org.apache.log4j.Logger;
import org.palladiosimulator.pcmtx.Table;
import org.palladiosimulator.transactions.ccsim.ITransaction;
import org.palladiosimulator.transactions.ccsim.LockDescriptor;
import org.palladiosimulator.transactions.ccsim.TransactionCallback;
import org.palladiosimulator.transactions.ccsim.TransactionImpl;
import org.palladiosimulator.transactions.ccsim.listener.TransactionListener;

import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.eventsim.entities.EventSimEntity;
import edu.kit.ipd.sdq.eventsim.system.entities.Request;

public class TransactionEntity extends EventSimEntity implements ITransaction {

    private static Logger LOG = Logger.getLogger(TransactionEntity.class);

    private Request request;

    private ITransaction delegate;

    /** the point in simulation time this transaction was suspended last */
    private double suspendedAt;

    private double waitingTimeTotal;

    public TransactionEntity(ISimulationModel simulationModel, Request request) {
        super(simulationModel, "Transaction");
        this.request = request;

        this.delegate = new TransactionImpl();
    }

    public Request getRequest() {
        return request;
    }

    @Override
    public void suspend(TransactionCallback resumeCallback) {
        // remember time of suspension
        double simTime = getModel().getSimulationControl().getCurrentSimulationTime();
        suspendedAt = simTime;

        delegate.suspend(resumeCallback);
    }

    @Override
    public void resume() {
        if (delegate.isSuspended()) {
            double simTime = getModel().getSimulationControl().getCurrentSimulationTime();
            double waitingTime = simTime - suspendedAt;
            waitingTimeTotal += waitingTime;
        }

        delegate.resume();
    }

    @Override
    public void grant(LockDescriptor lock) {
        // LOG.info("Granting lock to " + request + ": " + lock);
        delegate.grant(lock);
    }

    @Override
    public void revoke(LockDescriptor lock) {
        // LOG.info("Revoking lock held by " + request + ": " + lock);
        delegate.revoke(lock);
    }

    @Override
    public void waitFor(LockDescriptor lock) {
        delegate.waitFor(lock);
    }

    @Override
    public void clearLocks() {
        delegate.clearLocks();
    }

    @Override
    public boolean holdsAny(Set<LockDescriptor> locks) {
        return delegate.holdsAny(locks);
    }

    @Override
    public boolean holds(LockDescriptor lock) {
        return delegate.holds(lock);
    }

    @Override
    public Set<LockDescriptor> getGrantedLocks() {
        return delegate.getGrantedLocks();
    }

    @Override
    public Set<LockDescriptor> getPendingLocks() {
        return delegate.getPendingLocks();
    }

    @Override
    public int grantedResourcesCount(Table table) {
        return delegate.grantedResourcesCount(table);
    }

    public double getWaitingTimeTotal() {
        return waitingTimeTotal;
    }

    @Override
    public int getId() {
        return delegate.getId();
    }

    @Override
    public void setRollbackOnly() {
        delegate.setRollbackOnly();
    }

    @Override
    public boolean isRollbackOnly() {
        return delegate.isRollbackOnly();
    }

    @Override
    public boolean isSuspended() {
        return delegate.isSuspended();
    }

    @Override
    public void addTransactionListener(TransactionListener listener) {
        delegate.addTransactionListener(listener);
    }

    @Override
    public void removeTransactionListener(TransactionListener listener) {
        delegate.removeTransactionListener(listener);
    }

}
