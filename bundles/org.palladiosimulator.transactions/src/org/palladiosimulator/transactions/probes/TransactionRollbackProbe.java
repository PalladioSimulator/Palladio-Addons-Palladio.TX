package org.palladiosimulator.transactions.probes;

import org.palladiosimulator.transactions.TransactionProbeConfiguration;
import org.palladiosimulator.transactions.ccsim.ITransaction;
import org.palladiosimulator.transactions.ccsim.TransactionManager;
import org.palladiosimulator.transactions.ccsim.listener.RollbackCause;
import org.palladiosimulator.transactions.ccsim.listener.TransactionManagerListener;

import edu.kit.ipd.sdq.eventsim.measurement.Measurement;
import edu.kit.ipd.sdq.eventsim.measurement.MeasuringPoint;
import edu.kit.ipd.sdq.eventsim.measurement.Metadata;
import edu.kit.ipd.sdq.eventsim.measurement.annotation.Probe;
import edu.kit.ipd.sdq.eventsim.measurement.probe.AbstractProbe;

@Probe(type = TransactionManager.class, property = "transaction_rollback")
public class TransactionRollbackProbe extends AbstractProbe<TransactionManager, TransactionProbeConfiguration> {

    public TransactionRollbackProbe(MeasuringPoint<TransactionManager> p, TransactionProbeConfiguration configuration) {
        super(p, configuration);

        configuration.getTransactionManager().addListener(new TransactionManagerListener() {

            @Override
            public void rollback(ITransaction tx, RollbackCause cause) {
                // build measurement
                double simTime = configuration.getSimulationModel().getSimulationControl().getCurrentSimulationTime();
                Measurement<TransactionManager> m = new Measurement<>("TRANSACTION_ROLLBACK", getMeasuringPoint(), tx,
                        1, simTime, new Metadata("rollback.cause", cause.name()));

                // store
                // cache.put(m);

                // notify
                measurementListener.forEach(l -> l.notify(m));
            }

            @Override
            public void commit(ITransaction tx) {
                // nothing to do
            }

        });
    }

}
