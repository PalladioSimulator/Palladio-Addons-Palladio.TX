package org.palladiosimulator.pcmtx.pcmtxviews.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Observable;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcm.resourcetype.ResourcetypeFactory;
import org.palladiosimulator.pcm.resourcetype.ResourcetypePackage;
import org.palladiosimulator.pcmtx.DataRepository;
import org.palladiosimulator.pcmtx.Database;
import org.palladiosimulator.pcmtx.PcmtxFactory;
import org.palladiosimulator.pcmtx.PcmtxPackage;
import org.palladiosimulator.pcmtx.Table;
import org.palladiosimulator.pcmtx.TransactionIsolation;
import org.palladiosimulator.pcmtx.pcmtxviews.resource.ResourceManager;
import org.palladiosimulator.pcmtx.pcmtxviews.resource.ResourceUpdate;
import org.palladiosimulator.pcmtx.pcmtxviews.resource.ResourceUpdate.UpdateKind;

/**
 * This class encapsulates the access to a {@link DataRepository} in a pcmtx model. It provides
 * methods to add {@link Table}s and {@link Database}s to and to remove {@link Table}s and
 * {@link Database}s from a pcmtx model. A {@link ResourceManager} provides access to the model
 * resource.
 * 
 * @author Tobias Weiberg
 *
 */
public final class DataRepositoryManager extends EObjectManager<DataRepository> {
    private DataRepository dataRepository;
    private URI resourceURI;
    private IResource resource;
    private ResourceRepositoryManager etypesManager;
    private static final DataRepositoryManager INSTANCE = new DataRepositoryManager();
    private final List<Database> availableDatabases;
    private List<ResourceRepository> loadedResourceRepositories;
    private ResourceRepository currentRepository;
    private static final ResourceRepository EMPTY_RESOURCE_REPOSITORY = ResourcetypeFactory.eINSTANCE.createResourceRepository();
    private static final EClassifier RESOURCE_REPOSITORY_CLASSIFIER = ResourcetypePackage.eINSTANCE
            .getEClassifier("ResourceRepository");
    private static final EClassifier DATA_REPOSITORY_CLASSIFIER = PcmtxPackage.eINSTANCE
            .getEClassifier("DataRepository");

    /**
     * Creates a new {@code DataRepositoriesManager}.
     */
    private DataRepositoryManager() {
        super();
        this.etypesManager = ResourceRepositoryManager.getInstance();
        this.availableDatabases = new ArrayList<>();
        this.loadedResourceRepositories = new ArrayList<>();
        this.etypesManager.addObserver(this);
    }

    /**
     * Returns the singleton instance of this class.
     * 
     * @return the singleton instance
     */
    public static DataRepositoryManager getInstance() {
        return INSTANCE;
    }

    /**
     * Returns the {@link DataRepository} of the pcmtx model.
     * 
     * @return the {@link DataRepository} of the pcmtx model
     */
    @Override
    public DataRepository getEObject() {
        return dataRepository;
    }

    /**
     * Creates a default {@link Table} and adds it to the {@link DataRepository}.
     */
    public void createAndAddEmptyTable() {
        Table table = PcmtxFactory.eINSTANCE.createTable();
        dataRepository.getTable().add(table);
    }

    /**
     * Removes the given {@link Table} from the {@link DataRepository}.
     * 
     * @param table
     *            the {@link Table} to remove
     */
    public void removeTable(Table table) {
        dataRepository.getTable().remove(table);
    }

    /**
     * Creates a default {@link Database} and adds it to the {@link DataRepository}.
     */
    public void createAndAddEmptyDatabase() {
        Database db = PcmtxFactory.eINSTANCE.createDatabase();
        db.setIsolation(TransactionIsolation.SERIALIZABLE);
        int index = dataRepository.getDatabases().size();
        availableDatabases.add(index, db);
        dataRepository.getDatabases().add(db);
    }

    /**
     * Removes the given {@link Database} from the {@link DataRepository}.
     * 
     * @param db
     *            the {@link Database} to remove
     */
    public void removeDatabase(Database db) {
        availableDatabases.remove(db);
        dataRepository.getDatabases().remove(db);
    }

    /**
     * Sets the {@link DataRepository} to the one contained in the given emf workspace resource.
     * Persists the old {@link DataRepository} to the model file if it contains unsaved changes.
     * 
     * @param newResource
     *            the emf workspace resource containing the new {@link DataRepository}
     * @throws CoreException
     *             if persisting of the old {@link DataRepository} failed.
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
        DataRepository newRepos = getResourceManager().getEObject(DataRepository.class);

        if (dataRepository != null) {
            dataRepository.eAdapters().clear();
        }
        dataRepository = newRepos;
        resourceURI = uri;
        resource = newResource;

        if (newRepos != null) {
            EcoreUtil.resolveAll(newRepos);
            updateLoadedResourceRepositories();
            updateAvailableDatabases();
            dataRepository.eAdapters().add(new DataRepositoryContentAdapter());
            setChanged();
            notifyObservers(newResource);
        } else {
            // handle selection of invalid resources
            getResourceManager().clear();
            setChanged();
            notifyObservers(null);
        }
    }

    /**
     * Reloads the current {@link DataRepository} to see modifications made by other parties.
     */
    @Override
    public void reloadEObject() {
        getResourceManager().setResource(resource);
        DataRepository newRepos = getResourceManager().getEObject(DataRepository.class);

        if (newRepos != null) {
            if (dataRepository != null) {
                dataRepository.eAdapters().clear();
            }

            dataRepository = (DataRepository) newRepos;
            EcoreUtil.resolveAll(newRepos);
            updateLoadedResourceRepositories();
            updateAvailableDatabases();
            dataRepository.eAdapters().add(new DataRepositoryContentAdapter());
            setChanged();
            notifyObservers(resource);
            setDirty(false);
        }
    }

    /**
     * Private Adapter class to notify when the DataRepository is modified.
     */
    private class DataRepositoryContentAdapter extends EContentAdapter {
        @Override
        public void notifyChanged(Notification notification) {
            if (notification.getEventType() != Notification.RESOLVE
                    && notification.getEventType() != Notification.REMOVING_ADAPTER
                    && (isDatabaseUpdate(notification) || isTablesUpdate(notification))) {
                setDirty(true);
                if (isDatabaseUpdate(notification)) {
                    updateAvailableDatabases();
                    setChanged();
                    notifyObservers(dataRepository);
                }
            }
            super.notifyChanged(notification);
        }
    }

    /**
     * Returns {@code true} if the given {@link Notification} is about an update of a {@link Table}
     * or an update of the {@link Table}s list in the {@link DataRepository}, {@code false}
     * otherwise.
     * 
     * @param notification
     *            the {@link Notification}
     * @return {@code true} if the given {@link Notification} is about an update of a {@link Table}
     *         else {@code false}
     */
    private boolean isTablesUpdate(Notification notification) {
        boolean fromTable = notification.getNotifier() instanceof Table;
        boolean fromDataRepository = notification.getNotifier() instanceof DataRepository;
        boolean isTableFeatureUpdate = PcmtxPackage.Literals.DATA_REPOSITORY__TABLE.equals(notification.getFeature());
        return fromTable || (fromDataRepository && isTableFeatureUpdate);
    }

    /**
     * Returns {@code true} if the given {@link Notification} is about an update of a
     * {@link Database} or an update of the {@link Database}s list in the @link DataRepository},
     * {@code false} otherwise.
     * 
     * @param notification
     *            the {@link Notification}
     * @return {@code true} if the given {@link Notification} is about an update of a
     *         {@link Database} else {@code false}
     */
    private boolean isDatabaseUpdate(Notification notification) {
        boolean fromDatabase = notification.getNotifier() instanceof Database;
        boolean fromDataRepository = notification.getNotifier() instanceof DataRepository;
        boolean isDatabasesFeatureUpdate = PcmtxPackage.Literals.DATA_REPOSITORY__DATABASES
                .equals(notification.getFeature());
        return fromDatabase || (fromDataRepository && isDatabasesFeatureUpdate);
    }

    /**
     * Persists the {@link DataRepository} to the model file.
     * 
     * @throws CoreException
     *             if persisting failed
     */
    @Override
    public void saveEObject() throws CoreException {
        if (isDirty()) {
            setDirty(false);
            try {
                getResourceManager().persistChangesToWorkspaceResource(dataRepository);
            } catch (CoreException e) {
                setDirty(true);
                throw e;
            } finally {
                this.setLastUpdate(null);
            }
        }
    }

    /**
     * Returns the list of available {@link Database}s contained in the currently managed
     * {@link DataRepository} or in one of the other loaded {@link DataRepository DataRepositories}.
     * 
     * @return the list of available {@link Database}s
     */
    public List<Database> getAvailableDatabases() {
        return this.availableDatabases;
    }

    /**
     * Notifies the {@code DataRepositoriesManager} about changes. Used by the
     * {@link ResourceManager} to notify about loaded resources which are containing further
     * {@link DataRepository DataRepositories} or {@link ResourceRepository ResourceRepositories}.
     * Also used by the {@link ResourceRepositoryManager} to notify about a new
     * {@link ResourceRepository} or about modifications in the current one.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o.equals(this.getResourceManager())) {
            handleResourceManagerUpdate(arg);
        } else if (o.equals(this.etypesManager)) {
            handleResourceRepositoryManagerUpdate(arg);
        }
    }

    /**
     * Performs necessary actions if an update of the {@link ResourceManager} containing the given
     * {@link Object} is received.
     * 
     * @param arg
     *            the update {@link Object}
     */
    private void handleResourceManagerUpdate(Object arg) {
        if (arg instanceof ResourceUpdate) {
            ResourceUpdate update = (ResourceUpdate) arg;

            if (update.getKind() == UpdateKind.MODIFIED) {

                if (update.isPcmtxResourceUpdate()) {
                    // resource containing DataRepository is loaded or modified
                    if (update.getURI().equals(resourceURI)) {
                        // resource containing the current DataRepository is modified by another
                        // party
                        this.setLastUpdate(new UpdateRunnable(this, resourceURI, "Tables View, Databases View", true));
                    }
                    updateAvailableDatabases();

                } else if (update.isResourcetypeResourceUpdate()) {
                    // resource containing ResourceRepository is loaded or modified
                    updateLoadedResourceRepositories();
                }
                setChanged();
                notifyObservers(update);

            } else if (update.getKind() == UpdateKind.DELETED) {

                if (update.isPcmtxResourceUpdate()) {
                    // resource containing DataRespository is deleted
                    if (update.getURI().equals(resourceURI)) {
                        // resource containing the current DataRepository is deleted
                        this.resourceURI = null;
                        this.setDirty(false);
                        setChanged();
                        notifyObservers(null);
                    }
                    updateAvailableDatabases();

                } else if (update.isResourcetypeResourceUpdate()) {
                    // resource containing ResourceRepository is deleted
                    updateLoadedResourceRepositories();
                }
                setChanged();
                notifyObservers(update);
            }
        }
    }

    /**
     * Performs necessary actions if an update of the {@link ResourceRepositoryManager} containing
     * the given {@link Object} is received.
     * 
     * @param arg
     *            the update {@link Object}
     */
    private void handleResourceRepositoryManagerUpdate(Object arg) {
        if (arg instanceof IResource) {
            // new resource is selected
            this.currentRepository = etypesManager.getEObject();
            this.updateLoadedResourceRepositories();
            setChanged();
            notifyObservers(currentRepository);

        } else if (arg instanceof ResourceRepository) {
            // ResourceRepository is modified
            setChanged();
            notifyObservers(currentRepository);

        } else if (arg == null) {
            // resource containing current ResourceRepository is deleted
            this.currentRepository = null;
            this.updateLoadedResourceRepositories();
            setChanged();
            // use EMPTY_RESOURCE_REPOSITORY instead of NULL to avoid the clearing of the UI
            notifyObservers(EMPTY_RESOURCE_REPOSITORY);
        }
    }

    /**
     * Retrieves all ResourceRepositories which are contained in the Resources the ResourceManager
     * currently knows about.
     */
    private void updateLoadedResourceRepositories() {
        this.loadedResourceRepositories.clear();
        Collection<ResourceRepository> c = EcoreUtil.getObjectsByType(getResourceManager().getResourceSetContents(),
                RESOURCE_REPOSITORY_CLASSIFIER);

        if (this.currentRepository != null) {
            this.loadedResourceRepositories.add(currentRepository);
        }

        boolean isCurrentRepos = false;
        for (ResourceRepository r : c) {
            isCurrentRepos = this.currentRepository != null
                    && r.eResource().getURI().equals(this.currentRepository.eResource().getURI());

            if (!isCurrentRepos) {
                this.loadedResourceRepositories.add(r);
            }
        }
    }

    /**
     * Retrieves all Databases which are contained in the Resources the ResourceManager currently
     * knows about.
     */
    private void updateAvailableDatabases() {
        if (this.dataRepository == null) {
            return;
        }
        this.availableDatabases.clear();
        Collection<DataRepository> c = EcoreUtil.getObjectsByType(getResourceManager().getResourceSetContents(),
                DATA_REPOSITORY_CLASSIFIER);
        this.availableDatabases.addAll(this.dataRepository.getDatabases());
        boolean isCurrentDataRepos = false;
        for (DataRepository dr : c) {
            isCurrentDataRepos = dr.eResource().getURI().equals(this.dataRepository.eResource().getURI());
            if (!isCurrentDataRepos) {
                this.availableDatabases.addAll(dr.getDatabases());
            }
        }
    }

    /**
     * Returns the list of loaded {@link ResourceRepository ResourceRepositories}.
     * 
     * @return the list of loaded {@link ResourceRepository ResourceRepositories}
     */
    public List<ResourceRepository> getLoadedResourceRepositories() {
        return this.loadedResourceRepositories;
    }

    /**
     * Loads a new {@link Resource} specified by the given {@link URI} to make its contained
     * {@link EObject}s available.
     * 
     * @param uri
     *            the {@link URI}
     */
    public void loadResource(URI uri) {
        getResourceManager().loadResourceToResourceSet(uri);
    }
}
