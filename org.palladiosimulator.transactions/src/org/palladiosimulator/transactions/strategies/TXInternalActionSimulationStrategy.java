package org.palladiosimulator.transactions.strategies;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.palladiosimulator.mdsdprofiles.api.StereotypeAPI;
import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.InternalAction;
import org.palladiosimulator.pcm.seff.seff_performance.ResourceCall;
import org.palladiosimulator.transactions.EntityManager;
import org.palladiosimulator.transactions.TransactionRegistry;
import org.palladiosimulator.transactions.entities.TransactionEntity;
import org.palladiosimulator.transactions.events.TransactionTimeoutEvent;

import com.google.inject.Inject;

import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.eventsim.interpreter.DecoratingSimulationStrategy;
import edu.kit.ipd.sdq.eventsim.interpreter.SimulationStrategy;
import edu.kit.ipd.sdq.eventsim.interpreter.TraversalInstruction;
import edu.kit.ipd.sdq.eventsim.system.entities.Request;

public class TXInternalActionSimulationStrategy implements DecoratingSimulationStrategy<AbstractAction, Request> {

    private static Logger LOG = Logger.getLogger(TXInternalActionSimulationStrategy.class);

    @Inject
    private TransactionRegistry transactionRegistry;

    @Inject
    private EntityManager entityManager;

    @Inject
    private ISimulationModel model;

    private SimulationStrategy<AbstractAction, Request> decorated;

    @Override
    public void decorate(SimulationStrategy<AbstractAction, Request> decorated) {
        this.decorated = decorated;
    }

    @Override
    public void simulate(AbstractAction action, Request request, Consumer<TraversalInstruction> onFinishCallback) {
        // collect resource calls that are entity accesses according to their stereotype
        List<ResourceCall> resourceCalls = ((InternalAction) action).getResourceCall__Action();
        List<ResourceCall> entityAccesses = resourceCalls.stream().filter(call -> isEntityAccess(call))
                .collect(Collectors.toList());

        // lookup request's transaction
        TransactionEntity tx = transactionRegistry.getTransaction(request);
        if (tx == null) {
            // TODO open new TX and close directly after entity access?
            LOG.warn("Could not find open transaction for " + request);
        }

        if (entityAccesses.isEmpty()) {
            decorated.simulate(action, request, onFinishCallback);
            return;
        }

        boolean granted = entityManager.queryOrExecute(tx, entityAccesses);

        if (granted) {
            decorated.simulate(action, request, onFinishCallback);
            return;
        } else {
            // schedule timeout event
            TransactionTimeoutEvent timeoutEvent = new TransactionTimeoutEvent(model, entityManager);
            timeoutEvent.schedule(tx, 10000); // TODO set correct timeout

            // continue simulation on transaction resume
            tx.suspend(() -> {
                timeoutEvent.removeEvent();
                if (tx.isRollbackOnly()) {
                    request.leaveBehaviour();
                } else {
                    decorated.simulate(action, request, onFinishCallback);
                }
            });
        }

    }

    private boolean isEntityAccess(ResourceCall call) {
        return StereotypeAPI.isStereotypeApplied(call, "EntityAccess");
    }

}
