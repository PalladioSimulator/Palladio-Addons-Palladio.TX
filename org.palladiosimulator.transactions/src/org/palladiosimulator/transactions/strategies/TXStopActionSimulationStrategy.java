package org.palladiosimulator.transactions.strategies;

import java.util.function.Consumer;

import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.SeffPackage;
import org.palladiosimulator.pcm.seff.StopAction;
import org.palladiosimulator.transactions.EntityManager;
import org.palladiosimulator.transactions.TransactionRegistry;

import com.google.inject.Inject;

import edu.kit.ipd.sdq.eventsim.interpreter.DecoratingSimulationStrategy;
import edu.kit.ipd.sdq.eventsim.interpreter.SimulationStrategy;
import edu.kit.ipd.sdq.eventsim.interpreter.TraversalInstruction;
import edu.kit.ipd.sdq.eventsim.system.entities.Request;

public class TXStopActionSimulationStrategy implements DecoratingSimulationStrategy<AbstractAction, Request> {

    @Inject
    private TransactionRegistry transactionRegistry;

    @Inject
    private EntityManager entityManager;

    private SimulationStrategy<AbstractAction, Request> decorated;

    @Override
    public void decorate(SimulationStrategy<AbstractAction, Request> decorated) {
        this.decorated = decorated;
    }

    @Override
    public void simulate(AbstractAction action, Request request, Consumer<TraversalInstruction> onFinishCallback) {
        // TODO commit here?

        // if(isLastStopActionInSEFF(stop)) {
        // TransactionEntity tx = transactionRegistry.getTransaction(request);
        // entityManager.commit(tx);
        // // TODO read TX scope from model
        // tx.notifyLeftSystem();
        // }

        // delegate to decorated strategy
        decorated.simulate(action, request, onFinishCallback);
    }

    private boolean isLastStopActionInSEFF(StopAction action) {
        return action.getResourceDemandingBehaviour_AbstractAction().eClass()
                .equals(SeffPackage.eINSTANCE.getResourceDemandingSEFF());
    }

}
