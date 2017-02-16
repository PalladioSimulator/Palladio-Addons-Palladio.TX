package org.palladiosimulator.transactions.strategies;

import java.util.function.Consumer;

import org.palladiosimulator.pcm.seff.AbstractAction;
import org.palladiosimulator.pcm.seff.SeffPackage;
import org.palladiosimulator.pcm.seff.StartAction;
import org.palladiosimulator.transactions.TransactionManagerWrapper;
import org.palladiosimulator.transactions.TransactionRegistry;
import org.palladiosimulator.transactions.entities.TransactionEntity;

import com.google.inject.Inject;

import edu.kit.ipd.sdq.eventsim.entities.IEntityListener;
import edu.kit.ipd.sdq.eventsim.interpreter.DecoratingSimulationStrategy;
import edu.kit.ipd.sdq.eventsim.interpreter.SimulationStrategy;
import edu.kit.ipd.sdq.eventsim.interpreter.TraversalInstruction;
import edu.kit.ipd.sdq.eventsim.system.entities.Request;

public class TXStartActionSimulationStrategy implements DecoratingSimulationStrategy<AbstractAction, Request> {

    @Inject
    private TransactionManagerWrapper transactionManager;

    @Inject
    private TransactionRegistry transactionRegistry;

    private SimulationStrategy<AbstractAction, Request> decorated;

    @Override
    public void decorate(SimulationStrategy<AbstractAction, Request> decorated) {
        this.decorated = decorated;
    }

    @Override
    public void simulate(AbstractAction action, Request request, Consumer<TraversalInstruction> onFinishCallback) {
        // TODO read TX scope from model

        // TODO test/debug
        if (isFirstStartActionInSEFF((StartAction) action)) {
            // TODO read tx scope
            if (transactionRegistry.getTransaction(request) == null) {
                TransactionEntity tx = transactionManager.begin(request);
                transactionRegistry.addTransaction(tx, request);

                request.addEntityListener(new IEntityListener() {
                    @Override
                    public void leftSystem() {
                        // TODO use entity manager to commit?
                        transactionManager.commit(tx);
                    }

                    @Override
                    public void enteredSystem() {
                        // nothing to do
                    }
                });
            }
        }

        // delegate to decorated strategy
        decorated.simulate(action, request, onFinishCallback);
    }

    private boolean isFirstStartActionInSEFF(StartAction action) {
        return action.getResourceDemandingBehaviour_AbstractAction().eClass()
                .equals(SeffPackage.eINSTANCE.getResourceDemandingSEFF());
    }

}
