package org.palladiosimulator.transactions;

import java.util.HashMap;
import java.util.Map;

import org.palladiosimulator.transactions.entities.TransactionEntity;

import com.google.inject.Singleton;

import edu.kit.ipd.sdq.eventsim.entities.IEntityListener;
import edu.kit.ipd.sdq.eventsim.system.entities.Request;

@Singleton
public class TransactionRegistry {

    private Map<Request, TransactionEntity> requestToTransactionMap;

    public TransactionRegistry() {
        requestToTransactionMap = new HashMap<>();
    }

    public void addTransaction(TransactionEntity transaction, Request request) {
        requestToTransactionMap.put(request, transaction);

        // remove transaction from registry, once finished
        transaction.addEntityListener(new IEntityListener() {
            @Override
            public void leftSystem() {
                requestToTransactionMap.remove(transaction);
            }

            @Override
            public void enteredSystem() {
                // not relevant here
            }
        });
    }

    public TransactionEntity getTransaction(Request request) {
        return requestToTransactionMap.get(request);
    }

}
