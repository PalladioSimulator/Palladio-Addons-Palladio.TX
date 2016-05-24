package org.palladiosimulator.pcmtx.api;

import java.util.List;

import org.palladiosimulator.pcm.core.entity.EntityFactory;
import org.palladiosimulator.pcm.core.entity.ResourceProvidedRole;
import org.palladiosimulator.pcm.resourcetype.ResourceInterface;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcm.resourcetype.ResourceSignature;
import org.palladiosimulator.pcm.resourcetype.ResourcetypeFactory;
import org.palladiosimulator.pcmtx.EntityType;
import org.palladiosimulator.pcmtx.PcmtxFactory;

public class EntityTypesAPI {

	public static final String PROVIDED_ROLE_SUFFIX = "ProvidedRole";

	public static final String RESOURCE_INTERFACE_PREFIX = "I";

	public static final EntityAccessType[] DEFAULT_ACCESS_TYPES = new EntityAccessType[] { EntityAccessType.READ,
			EntityAccessType.INSERT, EntityAccessType.UPDATE };

	public static ResourceRepository createEmptyRepository() {
		return ResourcetypeFactory.eINSTANCE.createResourceRepository();
	}

	/**
	 * Creates a new entity type in the specified entity type repository.
	 * 
	 * @param repository
	 *            the repository in which the entity type is to be created
	 */
	public static EntityType createEntityType(ResourceRepository repository, String name) {
		return createEntityType(repository, name, DEFAULT_ACCESS_TYPES);
	}

	public static EntityType createEntityType(ResourceRepository repository, String name,
			EntityAccessType... accessTypes) {
		// add entity type to the repository
		EntityType entityType = PcmtxFactory.eINSTANCE.createEntityType();
		entityType.setResourceRepository_ResourceType(repository);
		entityType.setEntityName(name);

		// by convention, each entity type provides exactly one resource interface
		ResourceInterface iface = ResourcetypeFactory.eINSTANCE.createResourceInterface();
		iface.setResourceRepository__ResourceInterface(repository);
		iface.setEntityName(RESOURCE_INTERFACE_PREFIX + name);

		// add resource signature to the interface
		int resourceServiceId = 1;
		for (EntityAccessType accessType : accessTypes) {
			ResourceSignature signature = ResourcetypeFactory.eINSTANCE.createResourceSignature();
			signature.setResourceInterface__ResourceSignature(iface);
			signature.setEntityName(accessType.getName());
			signature.setResourceServiceId(resourceServiceId++);
		}

		// let the entity type provide its resource interface
		ResourceProvidedRole providedRole = EntityFactory.eINSTANCE.createResourceProvidedRole();
		providedRole.setEntityName(name + PROVIDED_ROLE_SUFFIX);
		providedRole.setResourceInterfaceProvidingEntity__ResourceProvidedRole(entityType);
		providedRole.setProvidedResourceInterface__ResourceProvidedRole(iface);

		return entityType;
	}

	/**
	 * @return the resource interface provided by the specified entity type. By convention, each entity type provides
	 *         exactly one resource interface.
	 */
	public static ResourceInterface getProvidedInterface(EntityType entityType) {
		List<ResourceProvidedRole> roles = entityType.getResourceProvidedRoles__ResourceInterfaceProvidingEntity();
		if (roles.isEmpty()) {
			return null;
		}
		return roles.get(0).getProvidedResourceInterface__ResourceProvidedRole();
	}

}
