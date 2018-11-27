package org.palladiosimulator.pcmtx.pcmtxviews.views;

import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.widgets.Display;
import org.palladiosimulator.pcmtx.Database;
import org.palladiosimulator.pcmtx.pcmtxviews.manager.DataRepositoryManager;
import org.palladiosimulator.pcmtx.pcmtxviews.ui.DatabasesUIProvider;
import org.palladiosimulator.pcmtx.pcmtxviews.util.Utils;

/**
 * This class represents a eclipse workbench view to modify {@link Database}s in a pcmtx model file.
 * 
 * @author Tobias Weiberg
 *
 */
public class DatabasesView extends PCMTXView {

    public static final String ID = "org.palladiosimulator.pcmtx.pcmtxviews.views.DatabasesView";

    /**
     * The constructor.
     */
    public DatabasesView() {
        super(ID, DataRepositoryManager.getInstance(), new DatabasesUIProvider(DataRepositoryManager.getInstance()),
                PCMTX_FILE_EXTENSION);
    }

    @Override
    public int promptToSaveOnClose() {
        URI uri = URI.createPlatformResourceURI(this.getCurrentResource().getFullPath().toString(), true);
        return Utils.openSaveChangesDialog(Display.getCurrent().getActiveShell(), "Databases View, Tables View", uri);
    }
}
