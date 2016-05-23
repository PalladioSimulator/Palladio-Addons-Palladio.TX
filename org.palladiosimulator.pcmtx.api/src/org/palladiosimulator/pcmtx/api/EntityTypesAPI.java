package org.palladiosimulator.pcmtx.api;

import org.palladiosimulator.pcm.core.entity.EntityFactory;
import org.palladiosimulator.pcm.core.entity.ResourceProvidedRole;
import org.palladiosimulator.pcm.resourcetype.ResourceInterface;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcm.resourcetype.ResourceSignature;
import org.palladiosimulator.pcm.resourcetype.ResourcetypeFactory;
import org.palladiosimulator.pcmtx.EntityType;
import org.palladiosimulator.pcmtx.PcmtxFactory;

public class EntityTypesAPI {

	private static final String PROVIDED_ROLE_SUFFIX = "ProvidedRole";

	private static final String RESOURCE_INTERFACE_PREFIX = "I";

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
		return createEntityType(repository, name, new String[] { "SELECT", "UPDATE", "INSERT" }); // TODO
	}

	public static EntityType createEntityType(ResourceRepository repository, String name, String... operations) {
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
		for (String op : operations) {
			ResourceSignature signature = ResourcetypeFactory.eINSTANCE.createResourceSignature();
			signature.setResourceInterface__ResourceSignature(iface);
			signature.setEntityName(op);
			signature.setResourceServiceId(resourceServiceId++);
		}

		// let the entity type provide its resource interface
		ResourceProvidedRole providedRole = EntityFactory.eINSTANCE.createResourceProvidedRole();
		providedRole.setEntityName(name + PROVIDED_ROLE_SUFFIX);
		providedRole.setResourceInterfaceProvidingEntity__ResourceProvidedRole(entityType);
		providedRole.setProvidedResourceInterface__ResourceProvidedRole(iface);

		return entityType;
	}

}
