package org.palladiosimulator.pcmtx.pcmtxviews.views;

import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.widgets.Display;
import org.palladiosimulator.pcmtx.Table;
import org.palladiosimulator.pcmtx.pcmtxviews.manager.DataRepositoryManager;
import org.palladiosimulator.pcmtx.pcmtxviews.ui.TablesUIProvider;
import org.palladiosimulator.pcmtx.pcmtxviews.util.Utils;

/**
 * This class represents a eclipse workbench view to modify {@link Table}s in a pcmtx model file.
 * 
 * @author Tobias Weiberg
 *
 */
public class TablesView extends PCMTXView {

    public static final String ID = "org.palladiosimulator.pcmtx.pcmtxviews.views.TablesView";

    /**
     * Creates a new Tables-{@code EditorView}.
     */
    public TablesView() {
        super(ID, DataRepositoryManager.getInstance(), new TablesUIProvider(DataRepositoryManager.getInstance()),
                PCMTX_FILE_EXTENSION);
    }

    @Override
    public int promptToSaveOnClose() {
        URI uri = URI.createPlatformResourceURI(this.getCurrentResource().getFullPath().toString(), true);
        return Utils.openSaveChangesDialog(Display.getCurrent().getActiveShell(), "Tables View, Databases View", uri);
    }
}
