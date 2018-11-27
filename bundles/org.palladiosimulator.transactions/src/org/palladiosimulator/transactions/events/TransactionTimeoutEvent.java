package org.palladiosimulator.transactions.events;

import org.apache.log4j.Logger;
import org.palladiosimulator.transactions.EntityManager;
import org.palladiosimulator.transactions.ccsim.listener.RollbackCause;
import org.palladiosimulator.transactions.ccsim.listener.TransactionListener;
import org.palladiosimulator.transactions.entities.TransactionEntity;

import de.uka.ipd.sdq.simulation.abstractsimengine.AbstractSimEventDelegator;
import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;

public class TransactionTimeoutEvent extends AbstractSimEventDelegator<TransactionEntity>
        implements TransactionListener {

    private static Logger LOG = Logger.getLogger(TransactionTimeoutEvent.class);

    private EntityManager em;

    public TransactionTimeoutEvent(ISimulationModel model, EntityManager em) {
        super(model, "TransactionTimeoutEvent");
        this.em = em;
    }

    @Override
    public void schedule(TransactionEntity tx, double delay) {
        tx.addTransactionListener(this);
        super.schedule(tx, delay);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void eventRoutine(final TransactionEntity tx) {
        LOG.debug("Timeout occured for " + tx);
        em.rollback(tx, RollbackCause.TIMEOUT); // TODO enough?
    }

    @Override
    public void beforeResume() {
        // reset timeout by removing this event from the future event list
        this.removeEvent();
    }

}
