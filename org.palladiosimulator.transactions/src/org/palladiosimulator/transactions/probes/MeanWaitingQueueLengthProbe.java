package org.palladiosimulator.transactions.probes;

import org.palladiosimulator.pcmtx.Table;
import org.palladiosimulator.transactions.TransactionProbeConfiguration;
import org.palladiosimulator.transactions.ccsim.LockMode;
import org.palladiosimulator.transactions.ccsim.listener.LockingListener;

import edu.kit.ipd.sdq.eventsim.measurement.Measurement;
import edu.kit.ipd.sdq.eventsim.measurement.MeasuringPoint;
import edu.kit.ipd.sdq.eventsim.measurement.annotation.Probe;
import edu.kit.ipd.sdq.eventsim.measurement.probe.AbstractProbe;

@Probe(type = Table.class, property = "lock_waiting_mean")
public class MeanWaitingQueueLengthProbe extends AbstractProbe<Table, TransactionProbeConfiguration> {

    public MeanWaitingQueueLengthProbe(MeasuringPoint<Table> p, TransactionProbeConfiguration configuration) {
        super(p, configuration);

        Table table = p.getElement();
        configuration.getLockManager().addListener(table, new LockingListener() {

            @Override
            public void lockedRowsCountChanged(int count) {
                // nothing to do
            }

            @Override
            public void lockedRowsCountPerModeChanged(LockMode mode, int count) {
                // nothing to do
            }

            @Override
            public void meanWaitingQueueLengthChanged(double length) {
                // build measurement
                double simTime = configuration.getSimulationModel().getSimulationControl().getCurrentSimulationTime();
                Measurement<Table> m = new Measurement<>("LOCK_WAITING_MEAN", getMeasuringPoint(), null, length,
                        simTime);

                // store
                // cache.put(m);

                // notify
                measurementListener.forEach(l -> l.notify(m));
            }
        });
    }

}
