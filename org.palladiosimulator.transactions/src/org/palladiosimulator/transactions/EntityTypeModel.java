package org.palladiosimulator.transactions;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.palladiosimulator.pcm.core.entity.ResourceProvidedRole;
import org.palladiosimulator.pcm.resourcetype.ResourceInterface;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcm.resourcetype.ResourceType;
import org.palladiosimulator.pcmtx.EntityType;
import org.palladiosimulator.pcmtx.PcmtxPackage;
import org.palladiosimulator.transactions.launch.ConfigurationConstants;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.kit.ipd.sdq.eventsim.api.ISimulationConfiguration;

@Singleton
public class EntityTypeModel extends AbstractEcoreModel<ResourceRepository> {

    /**
     * maps {@link ResourceInterface}s (identified by their id) to entity types.
     */
    private Map<String, EntityType> interfaceToEntityTypeMap;

    @Inject
    public EntityTypeModel(ISimulationConfiguration configuration) {
        super(configuration, ConfigurationConstants.ENTITY_TYPES_REPOSITORY_FILE);

        interfaceToEntityTypeMap = buildInterfaceToEntityTypeMapping(root);
    }

    private Map<String, EntityType> buildInterfaceToEntityTypeMapping(ResourceRepository root) {
        Map<String, EntityType> mapping = new HashMap<>();
        for (EntityType type : getEntityTypesInternal(root)) {
            for (ResourceProvidedRole role : type.getResourceProvidedRoles__ResourceInterfaceProvidingEntity()) {
                ResourceInterface providedInterface = role.getProvidedResourceInterface__ResourceProvidedRole();
                mapping.put(providedInterface.getId(), type);
            }
        }
        return mapping;
    }

    private Collection<EntityType> getEntityTypesInternal(ResourceRepository root) {
        List<ResourceType> resourceTypes = root.getAvailableResourceTypes_ResourceRepository();
        Collection<EntityType> entityTypes = EcoreUtil.getObjectsByType(resourceTypes,
                PcmtxPackage.eINSTANCE.getEntityType());
        return entityTypes;
    }

    /**
     * @param iface
     * @return the {@link EntityType} that provides the specified interface
     */
    public EntityType getEntityType(ResourceInterface iface) {
        return interfaceToEntityTypeMap.get(iface.getId());
    }

    public Collection<EntityType> getEntityTypes() {
        return interfaceToEntityTypeMap.values();
    }

}
