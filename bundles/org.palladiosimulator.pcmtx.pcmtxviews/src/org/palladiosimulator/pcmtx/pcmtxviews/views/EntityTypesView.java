package org.palladiosimulator.pcmtx.pcmtxviews.views;

import org.eclipse.emf.common.util.URI;
import org.eclipse.swt.widgets.Display;
import org.palladiosimulator.pcmtx.pcmtxviews.manager.ResourceRepositoryManager;
import org.palladiosimulator.pcmtx.pcmtxviews.ui.EntityTypesUIProvider;
import org.palladiosimulator.pcmtx.pcmtxviews.util.Utils;

/**
 * This class represents a eclipse workbench view to modify {@link EntityType}s in a resourcetype
 * model file.
 * 
 * @author Tobias Weiberg
 *
 */
public class EntityTypesView extends PCMTXView {

    public static final String ID = "org.palladiosimulator.pcmtx.pcmtxviews.views.EntityTypesView";

    /**
     * Creates a new EntityTypes-{@code EditorView}.
     */
    public EntityTypesView() {
        super(ID, ResourceRepositoryManager.getInstance(),
                new EntityTypesUIProvider(ResourceRepositoryManager.getInstance()), RESOURCETYPE_FILE_EXTENSION);
    }

    @Override
    public int promptToSaveOnClose() {
        URI uri = URI.createPlatformResourceURI(this.getCurrentResource().getFullPath().toString(), true);
        return Utils.openSaveChangesDialog(Display.getCurrent().getActiveShell(), "Entity Types View", uri);
    }
}