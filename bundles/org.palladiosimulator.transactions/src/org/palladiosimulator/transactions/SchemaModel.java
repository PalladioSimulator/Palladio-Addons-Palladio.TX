package org.palladiosimulator.transactions;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.palladiosimulator.pcmtx.DataRepository;
import org.palladiosimulator.pcmtx.EntityType;
import org.palladiosimulator.pcmtx.Table;
import org.palladiosimulator.transactions.launch.ConfigurationConstants;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.kit.ipd.sdq.eventsim.api.ISimulationConfiguration;

@Singleton
public class SchemaModel extends AbstractEcoreModel<DataRepository> {

    private Map<String, List<Table>> entityTypeToTablesMap;

    @Inject
    public SchemaModel(ISimulationConfiguration configuration) {
        super(configuration, ConfigurationConstants.DATA_REPOSITORY_FILE);
        entityTypeToTablesMap = buildEntityTypeToTablesMapping(root);
    }

    // TODO test/debug
    private Map<String, List<Table>> buildEntityTypeToTablesMapping(DataRepository root) {
        Map<String, List<Table>> mapping = new HashMap<>();
        for (Table table : getTables()) {
            for (EntityType entityType : table.getTypes()) {
                if (!mapping.containsKey(entityType.getId())) {
                    mapping.put(entityType.getId(), new LinkedList<>());
                }
                List<Table> tables = mapping.get(entityType.getId());
                tables.add(table);
            }
        }
        return mapping;
    }

    // TODO better use Set
    public Collection<Table> getTables() {
        return root.getTable();
    }

    public Collection<Table> getTablesForEntityType(EntityType entityType) {
        return entityTypeToTablesMap.get(entityType.getId());
    }

}
