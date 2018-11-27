package org.palladiosimulator.transactions;

import org.osgi.framework.Bundle;
import org.palladiosimulator.pcmtx.Table;
import org.palladiosimulator.transactions.ccsim.TransactionManager;
import org.palladiosimulator.transactions.entities.TransactionEntity;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import de.uka.ipd.sdq.simulation.abstractsimengine.ISimulationModel;
import edu.kit.ipd.sdq.eventsim.api.ISimulationMiddleware;
import edu.kit.ipd.sdq.eventsim.api.events.IEventHandler.Registration;
import edu.kit.ipd.sdq.eventsim.api.events.SimulationPrepareEvent;
import edu.kit.ipd.sdq.eventsim.measurement.MeasurementFacade;
import edu.kit.ipd.sdq.eventsim.measurement.MeasurementStorage;
import edu.kit.ipd.sdq.eventsim.measurement.osgi.BundleProbeLocator;
import edu.kit.ipd.sdq.eventsim.measurement.probe.IProbe;
import edu.kit.ipd.sdq.eventsim.modules.SimulationModuleEntryPoint;

@Singleton
public class TransactionSimulationModel implements SimulationModuleEntryPoint {

    @Inject
    private SchemaModel schemaModel;

    @Inject
    private MeasurementStorage measurementStorage;

    @Inject
    private ISimulationModel simulationModel;

    @Inject
    private TransactionManagerWrapper transactionManager;

    @Inject
    public TransactionSimulationModel(ISimulationMiddleware middleware) {
        middleware.registerEventHandler(SimulationPrepareEvent.class, e -> {
            init();
            return Registration.UNREGISTER;
        });
    }

    private void init() {
        // setup measurement facade
        Bundle bundle = Activator.getContext().getBundle();
        MeasurementFacade<TransactionProbeConfiguration> measurementFacade = new MeasurementFacade<>(
                new TransactionProbeConfiguration(transactionManager, transactionManager.getLockManager(),
                        simulationModel),
                new BundleProbeLocator<>(bundle));

        // TODO read instrumentation description

        for (Table table : schemaModel.getTables()) {
            IProbe<?> p1 = measurementFacade.createProbe(table, "lock_count_total");
            p1.forEachMeasurement(m -> measurementStorage.put(m));

            IProbe<?> p2 = measurementFacade.createProbe(table, "lock_count_per_mode");
            p2.forEachMeasurement(m -> measurementStorage.put(m));

            IProbe<?> p3 = measurementFacade.createProbe(table, "lock_waiting_mean");
            p3.forEachMeasurement(m -> measurementStorage.put(m));
        }

        IProbe<?> p4 = measurementFacade.createProbe(transactionManager, "transaction_rollback");
        p4.forEachMeasurement(m -> measurementStorage.put(m));

        IProbe<?> p5 = measurementFacade.createProbe(transactionManager, "transaction_commit");
        p5.forEachMeasurement(m -> measurementStorage.put(m));

        measurementStorage.addIdExtractor(Table.class, c -> ((Table) c).getId());
        // TODO prefix with database name?
        measurementStorage.addNameExtractor(Table.class, c -> ((Table) c).getEntityName());

        // TODO adjust the following two lines once multiple transaction managers are allowed
        measurementStorage.addIdExtractor(TransactionManager.class, m -> "TransactionManager");
        measurementStorage.addNameExtractor(TransactionManager.class, m -> "TransactionManager");

        measurementStorage.addIdExtractor(TransactionEntity.class,
                m -> Integer.toString(((TransactionEntity) m).getId()));
        measurementStorage.addNameExtractor(TransactionEntity.class, m -> ((TransactionEntity) m).getName());
    }

}
