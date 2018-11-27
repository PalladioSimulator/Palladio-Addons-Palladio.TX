package org.palladiosimulator.pcmtx.pcmtxviews.manager;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.palladiosimulator.pcmtx.pcmtxviews.util.ErrorHandler;
import org.palladiosimulator.pcmtx.pcmtxviews.util.Utils;

/**
 * This class represents an update of an {@code IResource}.
 * 
 * @author Tobias Weiberg
 *
 */
public class UpdateRunnable implements Runnable {
    private final EObjectManager<?> manager;
    private boolean reload;
    private final URI uri;
    private final String viewsString;

    /**
     * Creates a new {@code UpdateRunnable} with the given parameters.
     * 
     * @param manager
     *            the {@link EObjectManager} that has to reload the {@link Resource} if necessary.
     * @param uri
     *            the {@link URI} of the updated {@link Resource}
     * @param viewsString
     *            a string representation of the views affected by the update
     * @param reload
     *            should be {@code true} if the {@link Resource} the views currently work on
     *            changes, {@code false} otherwise.
     */
    public UpdateRunnable(EObjectManager<?> manager, URI uri, String viewsString, boolean reload) {
        this.manager = manager;
        this.uri = uri;
        this.reload = reload;
        this.viewsString = viewsString;
    }

    @Override
    public void run() {
        if (manager.isDirty()) {
            Shell shell = Display.getCurrent().getActiveShell();

            if (reload) {
                if (Utils.openReloadDialog(shell, viewsString, uri)) {
                    if (reload) {
                        manager.reloadEObject();
                    }
                }
            } else {
                if (!Utils.openSaveDialog(shell, viewsString, uri)) {
                    try {
                        manager.saveEObject();
                    } catch (CoreException e) {
                        ErrorHandler.handleError(e);
                    }
                }
            }
        } else {
            if (reload) {
                manager.reloadEObject();
            }
        }

    }

    /**
     * Sets the {@code reload} flag to the given value. Specifies whether a reload dialog or a save
     * dialog is shown.
     * 
     * @param reload
     *            the new reload value
     */
    public void setReload(boolean reload) {
        this.reload = reload;
    }

}
