package org.palladiosimulator.pcmtx.pcmtxviews.views;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.ISaveablePart2;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.part.ViewPart;
import org.palladiosimulator.pcm.core.entity.EntityPackage;
import org.palladiosimulator.pcm.resourcetype.ResourcetypePackage;
import org.palladiosimulator.pcmtx.PcmtxPackage;
import org.palladiosimulator.pcmtx.pcmtxviews.manager.EObjectManager;
import org.palladiosimulator.pcmtx.pcmtxviews.ui.UIProvider;
import org.palladiosimulator.pcmtx.pcmtxviews.util.ErrorHandler;

/**
 * This abstract class represents a pcmtx view.
 * 
 * @author Tobias Weiberg
 *
 */
public abstract class PCMTXView extends ViewPart implements ISaveablePart2, Observer {

    private boolean dirty = false;
    private EObjectManager<?> manager;
    private UIProvider uiProvider;
    private ISelectionListener selectedResourceListener;
    private IPartListener2 viewActiveListener;
    public static final String PCMTX_FILE_EXTENSION = "pcmtx";
    public static final String RESOURCETYPE_FILE_EXTENSION = "resourcetype";
    private final String viewID;
    private String defaultPartName;
    private boolean disposed;
    private final String fileExtension;
    private IResource resource;

    /**
     * Creates a new {@code PCMTXView} with the given parameters.
     * 
     * @param viewID
     *            the ID of this view
     * @param manager
     *            the {@link EObjectManager} this view uses to access models
     * @param uiProvider
     *            the {@link UIProvider} this view uses to create the user inferface
     * @param fileExtension
     *            the file extension of model files this view provides access to
     */
    protected PCMTXView(String viewID, EObjectManager<?> manager, UIProvider uiProvider, String fileExtension) {
        // necessary to register the packages
        PcmtxPackage.eINSTANCE.eClass();
        ResourcetypePackage.eINSTANCE.eClass();
        EntityPackage.eINSTANCE.eClass();

        this.viewID = viewID;
        this.manager = manager;
        this.manager.addObserver(this);
        this.uiProvider = uiProvider;
        this.fileExtension = fileExtension;
        this.disposed = false;
        this.selectedResourceListener = new ResourceSelectionListener(this, fileExtension);
        this.viewActiveListener = new ViewActiveListener(this);
    }

    /**
     * Returns the ID of this {@code PCMTXView}.
     * 
     * @return the ID of this {@code PCMTXView}
     */
    public String getViewID() {
        return viewID;
    }

    /**
     * Returns the {@link IResource} currently modified by this view.
     * 
     * @return the {@link IResource} currently modified by this view
     */
    public IResource getCurrentResource() {
        return resource;
    }

    /**
     * Sets the {@link IResource} that contains the model to work with.
     * 
     * @param resource
     *            the {@link IResource} that contains the model
     */
    public void setResource(IResource resource) {
        try {
            manager.setEObject(resource);
            this.resource = resource;
        } catch (CoreException e) {
            ErrorHandler.handleError(e);
        }
    }

    /**
     * Returns the {@link EObjectManager} the view uses for model modification.
     * 
     * @return the {@link EObjectManager}
     */
    protected EObjectManager<?> getEObjectManager() {
        return manager;
    }

    @Override
    public void createPartControl(Composite parent) {
        disposed = false;
        ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

        uiProvider.createUI(sc);

        getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(selectedResourceListener);
        ISelection selection = getSite().getWorkbenchWindow().getSelectionService().getSelection();

        if (selection instanceof IStructuredSelection) {
            IStructuredSelection sel = (IStructuredSelection) selection;
            if (sel.getFirstElement() instanceof IResource) {
                IResource res = (IResource) sel.getFirstElement();
                if (fileExtension.equals(res.getFileExtension())) {
                    setResource(res);
                    uiProvider.updateUI();
                    uiProvider.setEnabled(true);
                }
            }
        }

        getSite().getPage().addPartListener(viewActiveListener);

        this.defaultPartName = getPartName();
    }

    @Override
    public void setFocus() {
        uiProvider.setFocus();
    }

    @Override
    public void dispose() {
        disposed = true;
        getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(selectedResourceListener);
        getSite().getPage().removePartListener(viewActiveListener);
        manager.deleteObserver(this);
        manager.getResourceManager().removeListener();
        uiProvider.dispose();
        super.dispose();
        manager.clear();
    }

    /**
     * Sets the {@code dirty} flag to the given value.
     * 
     * @param dirty
     *            the new dirty state
     */
    protected void setDirty(boolean dirty) {
        if (isDirty() && !dirty) {
            this.setPartName(this.getPartName().substring(1));
        } else if (!isDirty() && dirty) {
            this.setPartName("*" + this.getPartName());
        }
        this.dirty = dirty;
        firePropertyChange(PROP_DIRTY);
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
        boolean failed = false;
        setDirty(false);
        try {
            manager.saveEObject();
        } catch (CoreException e) {
            ErrorHandler.handleError(e);
            failed = true;
        }
        if (failed && !disposed) {
            setDirty(true);
        }
    }

    @Override
    public void doSaveAs() {
        // not allowed
    }

    @Override
    public boolean isDirty() {
        return dirty;
    }

    @Override
    public boolean isSaveAsAllowed() {
        return false;
    }

    @Override
    public boolean isSaveOnCloseNeeded() {
        return true;
    }

    /**
     * Notifies this {@code PCMTXView} that it is active.
     * 
     * @param newResourceSelected
     *            {@code true} if a new {@link IResource} is selected, {@code false} otherwise
     */
    public void notifyActive(boolean newResourceSelected) {
        this.manager.runLastUpdate(newResourceSelected);
    }

    /**
     * Used to notify the {@code PCMTXView} about changes in the dirty state of the
     * {@link EObjectManager} and selection or deletion of model resources.
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o.equals(this.getEObjectManager())) {
            if (arg instanceof Boolean) {
                // EObject modified
                setDirty((Boolean) arg);
            } else if (arg instanceof IResource) {
                // new resource selected
                IResource res = (IResource) arg;
                setPartName(res.getFullPath().toString());
                uiProvider.updateUI();
                uiProvider.setEnabled(true);
            } else if (arg == null) {
                // resource is deleted
                setPartName(defaultPartName);
                uiProvider.clearUI();
            }
        }
    }
}
