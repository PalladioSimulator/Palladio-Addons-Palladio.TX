package org.palladiosimulator.pcmtx.pcmtxviews.manager;

import java.util.Observable;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.palladiosimulator.pcm.core.entity.EntityFactory;
import org.palladiosimulator.pcm.core.entity.EntityPackage;
import org.palladiosimulator.pcm.core.entity.ResourceProvidedRole;
import org.palladiosimulator.pcm.resourcetype.ResourceInterface;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcm.resourcetype.ResourceSignature;
import org.palladiosimulator.pcm.resourcetype.ResourceType;
import org.palladiosimulator.pcm.resourcetype.ResourcetypeFactory;
import org.palladiosimulator.pcm.resourcetype.ResourcetypePackage;
import org.palladiosimulator.pcmtx.EntityType;
import org.palladiosimulator.pcmtx.PcmtxFactory;
import org.palladiosimulator.pcmtx.pcmtxviews.resource.ResourceManager;
import org.palladiosimulator.pcmtx.pcmtxviews.resource.ResourceUpdate;
import org.palladiosimulator.pcmtx.pcmtxviews.resource.ResourceUpdate.UpdateKind;

/**
 * This class encapsulates the access to a {@link ResourceRepository} in a resourcetype model. It
 * provides methods to add {@link EntityType}s to and to remove {@link EntityType}s from a
 * resourcetype model. A {@link ResourceManager} provides access to the model resource.
 * 
 * @author Tobias Weiberg
 *
 */
public final class ResourceRepositoryManager extends EObjectManager<ResourceRepository> {

    private ResourceRepository resourceRepository;
    private static final ResourceRepositoryManager INSTANCE = new ResourceRepositoryManager();
    private URI resourceURI;
    private IResource resource;

    /**
     * Creates a new {@code ResourceRepositoryManager}.
     */
    private ResourceRepositoryManager() {
        super();
    }

    /**
     * Returns the singleton instance of this class.
     * 
     * @return the singleton instance
     */
    public static final ResourceRepositoryManager getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the {@link ResourceRepository} of the resourcetype model.
     * 
     * @return the {@link ResourceRepository} of the resourcetype model
     */
    @Override
    public ResourceRepository getEObject() {
        return resourceRepository;
    }

    /**
     * Sets the {@link ResourceRepository} to the one contained in the given emf workspace resource.
     * Persists the old {@link ResourceRepository} to the model file if it contains unsaved changes.
     * 
     * @param newResource
     *            the emf workspace resource containing the new {@link ResourceRepository}
     * @throws CoreException
     *             is persisting of the old {@link ResourceRepository} failed.
     */
    @Override
    public void setEObject(IResource newResource) throws CoreException {
        URI uri = URI.createPlatformResourceURI(newResource.getFullPath().toString(), true);
        if (getResourceManager().getResourceURI() != null && getResourceManager().getResourceURI().equals(uri)) {
            return;
        }

        if (isDirty()) {
            this.saveEObject();
        }

        getResourceManager().setResource(newResource);
        ResourceRepository newRepos = getResourceManager().getEObject(ResourceRepository.class);
        this.resourceRepository = newRepos;
        this.resourceURI = uri;
        this.resource = newResource;
        if (newRepos != null) {
            addAdapters();
            this.setChanged();
            this.notifyObservers(newResource);
        } else {
            // handle selection of invalid resources
            getResourceManager().clear();
            this.setChanged();
            this.notifyObservers(null);
        }
    }

    /**
     * Reloads the current {@link ResourceRepository} to see changes made by other parties.
     */
    @Override
    public void reloadEObject() {
        getResourceManager().setResource(resource);
        ResourceRepository newRepos = getResourceManager().getEObject(ResourceRepository.class);

        if (newRepos != null) {
            this.resourceRepository = newRepos;
            addAdapters();
            this.setChanged();
            this.notifyObservers(resource);
            setDirty(false);
        }
    }

    /**
     * Creates a default {@link EntityType} and adds it to the {@link ResourceRepository}.
     */
    public void createAndAddEmptyEntityType() {
        EntityType etype = PcmtxFactory.eINSTANCE.createEntityType();
        ResourceInterface etypeInterface = ResourcetypeFactory.eINSTANCE.createResourceInterface();
        ResourceProvidedRole etypeRole = EntityFactory.eINSTANCE.createResourceProvidedRole();

        etype.getResourceProvidedRoles__ResourceInterfaceProvidingEntity().add(etypeRole);
        etypeRole.setProvidedResourceInterface__ResourceProvidedRole(etypeInterface);
        etypeInterface.setEntityName("I" + etype.getEntityName());
        etypeRole.setEntityName(etype.getEntityName() + "ProvidedRole");

        // add R/W access methods as default
        this.addDefaultRWSignatures(etypeInterface);

        this.addEntityType(etype);
        etype.eAdapters().add(new EntityTypeUpdateAdapter());
    }

    /**
     * Creates a {@code Read} and a {@code Write} {@link ResourceSignature} and adds it to the given
     * {@link ResourceInterface}.
     * 
     * @param eInterface
     *            the {@link ResourceInterface}
     */
    private void addDefaultRWSignatures(ResourceInterface eInterface) {
        ResourceSignature read = ResourcetypeFactory.eINSTANCE.createResourceSignature();
        read.setEntityName("Read");
        ResourceSignature write = ResourcetypeFactory.eINSTANCE.createResourceSignature();
        write.setEntityName("Write");
        eInterface.getResourceSignatures__ResourceInterface().add(read);
        eInterface.getResourceSignatures__ResourceInterface().add(write);

    }

    /**
     * Adds the given {@link EntityType} to the {@link ResourceRepository}.
     * 
     * @param etype
     *            the {@link EntityType}
     */
    private void addEntityType(EntityType etype) {
        resourceRepository.getAvailableResourceTypes_ResourceRepository().add(etype);

        ResourceInterface etypeInterface = etype.getResourceProvidedRoles__ResourceInterfaceProvidingEntity().get(0)
                .getProvidedResourceInterface__ResourceProvidedRole();
        resourceRepository.getResourceInterfaces__ResourceRepository().add(etypeInterface);
    }

    /**
     * Removes the given {@link EntityType} from the {@link ResourceRepository}.
     * 
     * @param etype
     *            the {@link EntityType} to remove
     */
    public void removeEntityType(EntityType etype) {
        resourceRepository.getAvailableResourceTypes_ResourceRepository().remove(etype);
        ResourceInterface etypeInterface = etype.getResourceProvidedRoles__ResourceInterfaceProvidingEntity().get(0)
                .getProvidedResourceInterface__ResourceProvidedRole();
        resourceRepository.getResourceInterfaces__ResourceRepository().remove(etypeInterface);
    }

    /**
     * Adds an EntityTypeUpdateAdapter to every EntityType in the ResourceRepository and adds a
     * PersistContentChangesAdapter to the ResourceRepository.
     */
    private void addAdapters() {
        resourceRepository.eAdapters().add(new ResourceRepositoryContentAdapter());

        for (ResourceType r : resourceRepository.getAvailableResourceTypes_ResourceRepository()) {
            if (r instanceof EntityType) {
                ((EntityType) r).eAdapters().add(new EntityTypeUpdateAdapter());
            }
        }
    }

    /**
     * Private adapter class for modifying the associated ResourceInterface and ResourceProvidedRole
     * if the name of the EntityType is modified.
     */
    private static class EntityTypeUpdateAdapter extends AdapterImpl {
        @Override
        public void notifyChanged(Notification notification) {
            if (notification.getFeature() != null
                    && notification.getFeature().equals(EntityPackage.Literals.NAMED_ELEMENT__ENTITY_NAME)) {
                EntityType etype = (EntityType) notification.getNotifier();
                ResourceProvidedRole role = etype.getResourceProvidedRoles__ResourceInterfaceProvidingEntity().get(0);
                ResourceInterface etypeInterface = role.getProvidedResourceInterface__ResourceProvidedRole();
                role.setEntityName(etype.getEntityName() + "ProvidedRole");
                etypeInterface.setEntityName("I" + etype.getEntityName());
            }
        }
    }

    /**
     * Private adapter class to notify when the ResourceRepository is modified.
     */
    private class ResourceRepositoryContentAdapter extends EContentAdapter {
        @Override
        public void notifyChanged(Notification notification) {
            if (isEntityTypeUpdate(notification) || isResourceInterfaceUpdate(notification)) {
                if (isEntityTypeUpdate(notification)) {
                    setChanged();
                    notifyObservers(resourceRepository);
                }
                setDirty(true);
            }
            super.notifyChanged(notification);
        }
    }

    /**
     * Checks if the given {@link Notification} is about an update of an {@link EntityType} or the
     * Types-{@link EReference} in the ResourceRepository.
     * 
     * @param notification
     *            the {@link Notification}
     * @return {@code true} if the given {@link Notification} is about an update of an
     *         {@link EntityType}, {@code false} otherwise.
     */
    private boolean isEntityTypeUpdate(Notification notification) {
        boolean fromEntityType = notification.getNotifier() instanceof EntityType;
        boolean isNameFeatureUpdate = EntityPackage.Literals.NAMED_ELEMENT__ENTITY_NAME
                .equals(notification.getFeature());
        boolean fromResourceRepository = notification.getNotifier() instanceof ResourceRepository;
        boolean isTypesFeatureUpdate = ResourcetypePackage.Literals.RESOURCE_REPOSITORY__AVAILABLE_RESOURCE_TYPES_RESOURCE_REPOSITORY
                .equals(notification.getFeature());

        return (fromEntityType && isNameFeatureUpdate) || (fromResourceRepository && isTypesFeatureUpdate);
    }

    /**
     * Checks if the given {@link Notification} is about an update of an {@link ResourceSignature}
     * or the ResourceSignatures-{@link EReference} in the ResourceRepository.
     * 
     * @param notification
     *            the {@link Notification}
     * @return {@code true} if the given {@link Notification} is about an update of an
     *         {@link ResourceSignature}, {@code false} otherwise.
     */
    private boolean isResourceInterfaceUpdate(Notification notification) {
        boolean fromResourceSignature = notification.getNotifier() instanceof ResourceInterface;
        boolean isNameFeatureUpdate = EntityPackage.Literals.NAMED_ELEMENT__ENTITY_NAME
                .equals(notification.getFeature());
        boolean fromResourceInterface = notification.getNotifier() instanceof ResourceInterface;
        boolean isResourceSignaturesFeatureUpdate = ResourcetypePackage.Literals.RESOURCE_INTERFACE__RESOURCE_SIGNATURES_RESOURCE_INTERFACE
                .equals(notification.getFeature());

        return (fromResourceSignature && isNameFeatureUpdate)
                || (fromResourceInterface && isResourceSignaturesFeatureUpdate);
    }

    /**
     * Persists the {@link ResourceRepository} to the model file.
     * 
     * @throws CoreException
     *             if persisting failed
     */
    @Override
    public void saveEObject() throws CoreException {
        if (isDirty()) {
            setDirty(false);
            try {
                getResourceManager().persistChangesToWorkspaceResource(this.resourceRepository);
            } catch (CoreException e) {
                setDirty(true);
                throw e;
            } finally {
                this.setLastUpdate(null);
            }
        }
    }

    /**
     * Used by the {@link ResourceManager} to notify about modifications of the current
     * {@link ResourceRepository} by other parties.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o.equals(this.getResourceManager()) && arg instanceof ResourceUpdate) {
            ResourceUpdate update = (ResourceUpdate) arg;

            if (update.isResourcetypeResourceUpdate() && update.getURI().equals(resourceURI)) {
                if (update.getKind() == UpdateKind.MODIFIED) {
                    // resource containing the current ResourceRepository is modified by another
                    // party
                    this.setLastUpdate(new UpdateRunnable(this, resourceURI, "Entity Types View", true));

                } else if (update.getKind() == UpdateKind.DELETED) {
                    // resource containing the current ResourceRepository is deleted
                    this.setDirty(false);
                    this.resourceURI = null;
                    setChanged();
                    notifyObservers(null);
                }
            }
        }
    }
}
