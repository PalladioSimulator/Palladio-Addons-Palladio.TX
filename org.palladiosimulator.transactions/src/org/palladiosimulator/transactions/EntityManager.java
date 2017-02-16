package org.palladiosimulator.transactions;

import java.util.ArrayList;
import java.util.List;

import org.palladiosimulator.pcm.resourcetype.ResourceInterface;
import org.palladiosimulator.pcm.seff.seff_performance.ResourceCall;
import org.palladiosimulator.pcmtx.EntityType;
import org.palladiosimulator.pcmtx.Table;
import org.palladiosimulator.transactions.ccsim.TableAccess;
import org.palladiosimulator.transactions.ccsim.listener.RollbackCause;
import org.palladiosimulator.transactions.entities.TransactionEntity;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.kit.ipd.sdq.eventsim.system.entities.Request;

@Singleton
public class EntityManager {

    @Inject
    private TransactionManagerWrapper transactionManager;

    @Inject
    private EntityTypeModel entityTypeModel;

    @Inject
    private SchemaModel schemaModel;

    public TransactionEntity begin(Request request) {
        return transactionManager.begin(request);
    }

    public void commit(TransactionEntity tx) {
        transactionManager.commit(tx);
    }

    public void rollback(TransactionEntity tx, RollbackCause cause) {
        transactionManager.rollback(tx, cause);
    }

    /**
     * 
     * @param tx
     * @param entityAccesses
     * @return {@code true}, if all entityAccesses have been granted; {@code false}, else
     */
    public boolean queryOrExecute(TransactionEntity tx, List<ResourceCall> entityAccesses) {
        // translate entity accesses to table accesses according to table mapping
        List<TableAccess> tableAccesses = translateToTableAccesses(tx, entityAccesses);

        // TODO defer insert/update until commit/flush to resemble JPA

        // delegate to transaction manager
        return transactionManager.queryOrExecute(tx, tableAccesses);
    }

    private List<TableAccess> translateToTableAccesses(TransactionEntity tx, List<ResourceCall> entityAccesses) {
        List<TableAccess> tableAccesses = new ArrayList<>();
        for (ResourceCall entityAccess : entityAccesses) {
            // calculate number of accessed rows from StoEx
            int evaluatedQuantity = tx.getRequest().getRequestState().getStoExContext()
                    .evaluate(entityAccess.getNumberOfCalls__ResourceCall().getSpecification(), Integer.class);

            String accessType = entityAccess.getSignature__ResourceCall().getEntityName();
            ResourceInterface resourceInterface = entityAccess.getSignature__ResourceCall()
                    .getResourceInterface__ResourceSignature();
            EntityType entityType = entityTypeModel.getEntityType(resourceInterface);

            for (Table table : schemaModel.getTablesForEntityType(entityType)) {
                tableAccesses.add(new TableAccess(table, evaluatedQuantity, accessType, entityAccess));
            }
        }
        return tableAccesses;
    }

}
