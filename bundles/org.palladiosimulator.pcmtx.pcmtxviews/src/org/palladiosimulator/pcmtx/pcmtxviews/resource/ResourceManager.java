package org.palladiosimulator.pcmtx.pcmtxviews.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.palladiosimulator.pcm.resourcetype.util.ResourcetypeResourceFactoryImpl;
import org.palladiosimulator.pcmtx.pcmtxviews.resource.ResourceUpdate.UpdateKind;

/**
 * This class encapsulates the access to emf model files of type pcmtx and resourcetype.
 * 
 * @param <T>
 *            The type of the {@link EObject} the main {@link Resource} contains.
 * 
 * @author Tobias Weiberg
 *
 */
public class ResourceManager<T extends EObject> extends Observable {

    private final ResourceSet resourceSet;
    private URI resourceURI;
    private IResource workspaceResource;
    public static final String PCMTX_FILE_EXTENSION = "pcmtx";
    public static final String RESOURCETYPE_FILE_EXTENSION = "resourcetype";

    // listens for resource modifications by other parties
    private IResourceChangeListener resourceListener = new IResourceChangeListener() {

        @Override
        public void resourceChanged(IResourceChangeEvent event) {
            if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
                try {
                    event.getDelta().accept(new ChangedVisitor());
                    event.getDelta().accept(new RemovedVisitor());
                } catch (CoreException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * Private class that notifies observers if a {@link Resource} contained in the
     * {@link ResourceSet} is modified by another party.
     */
    // notifies observers about modifications of loaded resources
    private class ChangedVisitor implements IResourceDeltaVisitor {

        @Override
        public boolean visit(IResourceDelta delta) throws CoreException {
            IResource res = delta.getResource();
            URI uri = URI.createPlatformResourceURI(res.getFullPath().toString(), true);
            if (delta.getKind() == IResourceDelta.CHANGED && modificationsRelevant(res)) {
                int indexOfChangedURI = indexOfURI(uri);
                if (indexOfChangedURI != -1) {
                    if (indexOfChangedURI == 0) {
                        // current main resource modified
                        resourceURI = null;
                    } else {
                        resourceSet.getResources().get(indexOfChangedURI).unload();
                    }
                    loadResourceToResourceSet(uri);
                }
            }
            return true; // visit the children
        }
    }

    /**
     * Private class that notifies observers if a {@link Resource} contained in the
     * {@link ResourceSet} is deleted.
     */
    // notifies observers about deleted resources
    private class RemovedVisitor implements IResourceDeltaVisitor {

        @Override
        public boolean visit(IResourceDelta delta) throws CoreException {
            IResource res = delta.getResource();
            URI uri = URI.createPlatformResourceURI(res.getFullPath().toString(), true);
            if (delta.getKind() == IResourceDelta.REMOVED && modificationsRelevant(res)) {
                int indexOfRemovedURI = indexOfURI(uri);
                if (indexOfRemovedURI != -1) {
                    if (indexOfRemovedURI == 0) {
                        // current main resource deleted
                        setResource(null);
                    } else {
                        resourceSet.getResources().remove(indexOfRemovedURI);
                    }
                    setChanged();
                    notifyObservers(new ResourceUpdate(uri, UpdateKind.DELETED));
                }
            }
            return true; // visit the children
        }
    }

    /**
     * Returns the index of the {@link Resource} with the given {@link URI} in the
     * {@link ResourceSet}.
     * 
     * @param uri
     *            the {@link URI}
     * @return the index of the {@link Resource} with the given {@link URI} in the
     *         {@link ResourceSet}, or {@code -1} if the {@link ResourceSet} does not contain a
     *         {@link Resource} identified by this {@link URI}
     */
    private int indexOfURI(URI uri) {
        List<Resource> resources = resourceSet.getResources();
        for (int i = 0; i < resources.size(); i++) {
            if (resources.get(i).getURI().equals(uri)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if modifications of the given {@link IResource} are necessary to notify about.
     * 
     * @param res
     *            the {@link IResource}
     * @return {@code true} if modifications of the {@link Resource} should be notified,
     *         {@code false} otherwise.
     */
    private boolean modificationsRelevant(IResource res) {
        return PCMTX_FILE_EXTENSION.equals(res.getFileExtension())
                || RESOURCETYPE_FILE_EXTENSION.equals(res.getFileExtension());
    }

    /**
     * Creates a new {@code ResourceManager}.
     */
    public ResourceManager() {
        resourceSet = new ResourceSetImpl();
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
                .put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(RESOURCETYPE_FILE_EXTENSION,
                new ResourcetypeResourceFactoryImpl());
        ResourcesPlugin.getWorkspace().addResourceChangeListener(resourceListener);
    }

    /**
     * Clears the {@link ResourceSet} and sets the main model resource to the one specified by the
     * given workspace {@link IResource resource}. {@code workspaceResource} may be {@code null}.
     * 
     * @param workspaceResource
     *            the workspace {@link IResource resource} that specifies the new main
     *            {@link Resource}.
     */
    public void setResource(IResource workspaceResource) {
        resourceSet.getResources().clear();
        this.workspaceResource = workspaceResource;
        if (workspaceResource != null) {
            this.resourceURI = URI.createPlatformResourceURI(workspaceResource.getFullPath().toString(), true);
        } else {
            this.resourceURI = null;
        }
    }

    /**
     * Loads a new {@link Resource} specified by the given {@link URI} to the {@link ResourceSet}.
     * Notifies observers afterwards.
     * 
     * @param uri
     *            the {@link URI} of the new {@link Resource}
     */
    public void loadResourceToResourceSet(URI uri) {
        if (uri.equals(resourceURI)) {
            return;
        }
        this.setChanged();
        resourceSet.getEObject(uri.appendFragment("/"), true);
        this.notifyObservers(new ResourceUpdate(uri, UpdateKind.MODIFIED));
    }

    /**
     * Persists the given {@link EObject} to the current model resource in the workspace.
     * 
     * @param eobj
     *            the {@link EObject} to persist
     * @throws CoreException
     *             if persisting failed.
     */
    public void persistChangesToWorkspaceResource(T eobj) throws CoreException {
        if (workspaceResource != null) {
            IWorkspace workspace = ResourcesPlugin.getWorkspace();
            workspace.run(new ApplyChangesToResourceWorkspaceRunnable(eobj), workspaceResource.getProject(),
                    IWorkspace.AVOID_UPDATE, null);
        }
    }

    /**
     * This private class represents an {@link IWorkspaceRunnable} that persists an {@link EObject}
     * to the current main model {@link Resource}.
     */
    private class ApplyChangesToResourceWorkspaceRunnable implements IWorkspaceRunnable {
        private T eobj;

        /**
         * Creates a new {@code ApplyChangesToResourceWorkspaceRunnable} to persist the given
         * {@link EObject} to the model {@link Resource}.
         * 
         * @param eobj
         *            the {@link EObject} to persist
         */
        public ApplyChangesToResourceWorkspaceRunnable(T eobj) {
            this.eobj = eobj;
        }

        @Override
        public void run(IProgressMonitor monitor) throws CoreException {
            try {
                saveContent(eobj);
                workspaceResource.refreshLocal(IResource.DEPTH_ZERO, null);
            } catch (IOException e) {
                throw new CoreException(new Status(IStatus.WARNING, "org.palladiosimulator.pcmtx.pcmtxviews",
                        "Persisting of the model content failed.", e));
            }
        }
    }

    /**
     * Persists the given {@link EObject} and its content to the model {@link Resource}.
     * 
     * @param eobj
     *            the {@link EObject}
     * @throws IOException
     *             if persisting failed
     */
    private void saveContent(T eobj) throws IOException {
        List<Resource> resources = resourceSet.getResources();
        resources.get(0).getContents().add(eobj);
        resources.get(0).save(Collections.EMPTY_MAP);
    }

    /**
     * Returns the first {@link EObject} of the main {@link Resource} of the {@link ResourceSet} or
     * {@code null} if the {@link ResourceSet} does not contain any {@link Resource}s or the main
     * {@link Resource} does not contain any {@link EObject}.
     * 
     * @param c
     *            a {@link Class} object to check if the current main resource contains an
     *            {@link EObject} of the specific type.
     * 
     * @return the first {@link EObject} of the main {@link Resource} or {@code null} if there are
     *         not any {@link Resource}s or {@link EObject}s.
     */
    public T getEObject(Class<T> c) {
        if (resourceURI == null) {
            return null;
        }
        EObject eobj = resourceSet.getEObject(resourceURI.appendFragment("/"), true);

        if (c.isInstance(eobj)) {
            return c.cast(eobj);
        }

        return null;
    }

    /**
     * Returns the {@link URI} of the main {@link Resource} in the {@link ResourceSet}.
     * 
     * @return the {@link URI} of the main {@link Resource} in the {@link ResourceSet}
     */
    public URI getResourceURI() {
        return resourceURI;
    }

    /**
     * Returns a list of the contents contained in the resources of the resource set.
     * 
     * @return a list of the contents contained in the resources of the resource set
     */
    public List<EObject> getResourceSetContents() {
        List<EObject> eobjects = new ArrayList<>();
        for (Resource resource : resourceSet.getResources()) {
            eobjects.addAll(resource.getContents());
        }
        return eobjects;
    }

    /**
     * Removes workspace listener.
     */
    public void removeListener() {
        ResourcesPlugin.getWorkspace().removeResourceChangeListener(resourceListener);
    }

    /**
     * Clears the state of the {@code ResourceManager}.
     */
    public void clear() {
        this.resourceURI = null;
    }

}
