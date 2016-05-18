package org.palladiosimulator.pcmtx.pcmtxviews.manager;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.widgets.Display;
import org.palladiosimulator.pcmtx.pcmtxviews.resource.ResourceManager;

/**
 * This abstract class represents a manager for an {@link EObject}.
 * 
 * @param <T>
 *            The type of the {@link EObject} this {@code EObjectManager} manages.
 * 
 * @author Tobias Weiberg
 *
 */
public abstract class EObjectManager<T extends EObject> extends Observable implements Observer {

    private boolean dirty;
    private UpdateRunnable lastUpdate;
    private final ResourceManager<T> resourceManager;

    /**
     * Creates a new {@code EObjectManager}.
     */
    protected EObjectManager() {
        resourceManager = new ResourceManager<T>();
        resourceManager.addObserver(this);
    }

    /**
     * Sets the {@link EObject} to the one contained in the given {@link IResource}.
     * 
     * @param resource
     *            the {@link IResource} that contains the new {@link EObject}
     * @throws CoreException
     *             if the operation failed.
     */
    public abstract void setEObject(IResource resource) throws CoreException;

    /**
     * Returns the {@link EObject} this {@code EObjectManager} manages.
     * 
     * @return the {@link EObject} this {@code EObjectManager} manages.
     */
    public abstract T getEObject();

    /**
     * Reloads the {@link EObject}.
     */
    public abstract void reloadEObject();

    /**
     * Persists the current EObject to a model file.
     * 
     * @throws CoreException
     *             if persisting failed.
     */
    public abstract void saveEObject() throws CoreException;

    /**
     * Returns the {@link ResourceManager} this {@code EObjectManager} uses to access the model
     * resource.
     * 
     * @return the {@link ResourceManager} this {@code EObjectManager} uses
     */
    public ResourceManager<T> getResourceManager() {
        return this.resourceManager;
    }

    /**
     * Clears the state of the {@code EObjectManager}.
     */
    public void clear() {
        setDirty(false);
        resourceManager.clear();
        setChanged();
        notifyObservers(null);
    }

    /**
     * Returns the {@code dirty state} of this {@code EObjectManager}.
     * 
     * @return the {@code dirty state} of this object
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * Sets the {@code dirty state} of this {@code EObjectManager}. The {@code dirty state} should
     * be {@code true} if the {@link EObject} contains unsaved modifications, {@code false}
     * otherwise.
     * 
     * @param dirty
     *            the new {@code dirty state}
     */
    public void setDirty(boolean dirty) {
        if (this.dirty != dirty) {
            this.dirty = dirty;
            setChanged();
            this.notifyObservers(dirty);
        }
    }

    /**
     * Runs the last recognized update that modified the {@link EObject}.
     * 
     * @param eObjectChanges
     *            {@code true} if the {@link EObject} will change afterwards, {@code false}
     *            otherwise
     */
    public void runLastUpdate(boolean eObjectChanges) {
        if (lastUpdate != null) {
            UpdateRunnable update = lastUpdate;
            lastUpdate = null; // setting of the last update to null necessary before executing
            update.setReload(!eObjectChanges);
            Display.getDefault().syncExec(update);
        }
    }

    /**
     * Sets the last recognized update that modified the {@link EObject} to the given one.
     * 
     * @param update
     *            the new last recognized update
     */
    public void setLastUpdate(UpdateRunnable update) {
        this.lastUpdate = update;
    }

}
