package org.palladiosimulator.pcmtx.pcmtxviews.views;

import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPartReference;

/**
 * This class represents a {@link IPartListener2} that notifies a {@link PCMTXView} when it is
 * activated.
 * 
 * @author Tobias Weiberg
 *
 */
public class ViewActiveListener implements IPartListener2 {
    private final PCMTXView view;

    /**
     * Creates a new {@code ViewActiveListener} that listens for the given {@link PCMTXView} to
     * become active.
     * 
     * @param view
     *            the {@link PCMTXView}
     */
    public ViewActiveListener(PCMTXView view) {
        this.view = view;
    }

    @Override
    public void partActivated(IWorkbenchPartReference partRef) {
        if (view.getViewID().equals(partRef.getId())) {
            view.notifyActive(false);
        }
    }

    @Override
    public void partBroughtToTop(IWorkbenchPartReference partRef) {
        // not needed
    }

    @Override
    public void partClosed(IWorkbenchPartReference partRef) {
        // not needed
    }

    @Override
    public void partDeactivated(IWorkbenchPartReference partRef) {
        // not needed
    }

    @Override
    public void partOpened(IWorkbenchPartReference partRef) {
        // not needed
    }

    @Override
    public void partHidden(IWorkbenchPartReference partRef) {
        // not needed
    }

    @Override
    public void partVisible(IWorkbenchPartReference partRef) {
        // not needed
    }

    @Override
    public void partInputChanged(IWorkbenchPartReference partRef) {
        // not needed
    }

}
