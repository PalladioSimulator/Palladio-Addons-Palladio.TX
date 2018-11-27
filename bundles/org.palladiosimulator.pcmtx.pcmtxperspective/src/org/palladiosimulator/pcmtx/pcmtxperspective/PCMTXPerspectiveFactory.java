package org.palladiosimulator.pcmtx.pcmtxperspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

/**
 * This class represents the factory for the eclipse perspective.
 * 
 * @author Tobias Weiberg
 *
 */
public class PCMTXPerspectiveFactory implements IPerspectiveFactory {

    @Override
    public void createInitialLayout(IPageLayout layout) {
        layout.setEditorAreaVisible(false);
        layout.addView("org.eclipse.ui.navigator.ProjectExplorer", IPageLayout.LEFT, 0.2f, IPageLayout.ID_EDITOR_AREA);
        layout.addView("org.palladiosimulator.pcmtx.pcmtxviews.views.EntityTypesView", IPageLayout.TOP, 0.5f,
                IPageLayout.ID_EDITOR_AREA);
        layout.addView("org.palladiosimulator.pcmtx.pcmtxviews.views.TablesView", IPageLayout.LEFT, 0.5f,
                IPageLayout.ID_EDITOR_AREA);
        layout.addView("org.palladiosimulator.pcmtx.pcmtxviews.views.DatabasesView", IPageLayout.RIGHT, 0.5f,
                "org.palladiosimulator.pcmtx.pcmtxviews.views.EntityTypesView");
        layout.addShowViewShortcut("org.palladiosimulator.pcmtx.pcmtxviews.views.EntityTypesView");
        layout.addShowViewShortcut("org.palladiosimulator.pcmtx.pcmtxviews.views.TablesView");
        layout.addShowViewShortcut("org.palladiosimulator.pcmtx.pcmtxviews.views.DatabasesView");
    }

}
