package org.palladiosimulator.pcmtx.pcmtxviews.views;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This class represents an {@link ISelectionListener} that listens for selection of resources with
 * a specific file extension.
 * 
 * @author Tobias Weiberg
 *
 */
public class ResourceSelectionListener implements ISelectionListener {

    private final PCMTXView view;
    private final String fileExtension;
    private IResource lastResource;

    /**
     * Creates a new {@code ResourceSelectionListener} with the given Parameters.
     * 
     * @param view
     *            the {@link PCMTXView} that needs to be updated when a new resource is selected
     * @param fileExtension
     *            the file extension of resources
     */
    public ResourceSelectionListener(PCMTXView view, String fileExtension) {
        this.fileExtension = fileExtension;
        this.view = view;
    }

    @Override
    public void selectionChanged(IWorkbenchPart part, ISelection selection) {
        if (selection instanceof IStructuredSelection) {
            IStructuredSelection sel = (IStructuredSelection) selection;
            if (sel.getFirstElement() instanceof IResource) {
                IResource resource = (IResource) sel.getFirstElement();
                if (fileExtension.equals(resource.getFileExtension())) {
                    view.notifyActive(!resource.equals(lastResource));
                    lastResource = resource;
                    view.setResource(resource);
                }
            }
        }

    }

}
