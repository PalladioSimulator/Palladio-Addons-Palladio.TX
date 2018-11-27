package org.palladiosimulator.pcmtx.pcmtxviews.ui;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.palladiosimulator.pcm.core.entity.EntityPackage;
import org.palladiosimulator.pcm.resourcetype.ResourcetypePackage;
import org.palladiosimulator.pcmtx.EntityType;
import org.palladiosimulator.pcmtx.pcmtxviews.manager.ResourceRepositoryManager;
import org.palladiosimulator.pcmtx.pcmtxviews.resource.ResourceManager;

/**
 * This class provides the GUI elements for the {@link EntityTypesView}.
 * 
 * @author Tobias Weiberg
 *
 */
public class EntityTypesUIProvider extends AbstractUIProvider {

    private final ResourceRepositoryManager rrManager;

    /**
     * Creates a new {@code EntityTypesUIProvider} with the given {@link ResourceRepositoryManager}.
     * 
     * @param rrManager
     *            the {@link ResourceRepositoryManager} to access the model resource
     */
    public EntityTypesUIProvider(ResourceRepositoryManager rrManager) {
        this.rrManager = rrManager;
    }

    @Override
    protected void createLabelComposite(Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        label.setText("Entity Types:");
        label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        GridData gridData = new GridData(SWT.BEGINNING, SWT.END, false, false);
        gridData.horizontalSpan = 2;
        label.setLayoutData(gridData);
    }

    @Override
    protected DetailsUI createDetailsUI(Composite parent) {
        return new EntityTypeDetailsUI(parent, viewer);
    }

    @Override
    protected void createAndInitList(Composite parent) {
        viewer = new ListViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

        ObservableListContentProvider cp = new ObservableListContentProvider();
        IObservableMap map = EMFProperties.value(EntityPackage.Literals.NAMED_ELEMENT__ENTITY_NAME)
                .observeDetail(cp.getKnownElements());

        viewer.setLabelProvider(new ObservableMapLabelProvider(map));

        viewer.setContentProvider(cp);

        if (rrManager.getEObject() != null) {
            viewer.setInput(EMFProperties
                    .list(ResourcetypePackage.Literals.RESOURCE_REPOSITORY__AVAILABLE_RESOURCE_TYPES_RESOURCE_REPOSITORY)
                    .observe(rrManager.getEObject()));
        }

        viewer.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

        // filtering of resource types which are not EntityTypes
        viewer.addFilter(new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                return element instanceof EntityType;
            }

        });
    }

    @Override
    public void updateUI() {
        if (!viewer.getList().isDisposed()) {
            viewer.setInput(EMFProperties
                    .list(ResourcetypePackage.Literals.RESOURCE_REPOSITORY__AVAILABLE_RESOURCE_TYPES_RESOURCE_REPOSITORY)
                    .observe(rrManager.getEObject()));
            viewer.setSelection(StructuredSelection.EMPTY);
        }
    }

    @Override
    protected void addButtonSelected() {
        rrManager.createAndAddEmptyEntityType();
        int lastIndex = viewer.getList().getItemCount() - 1;
        viewer.setSelection(new StructuredSelection(viewer.getElementAt(lastIndex)));
    }

    @Override
    protected void removeButtonSelected() {
        IObservableValue selection = ViewersObservables.observeSingleSelection(viewer);
        Object obj = selection.getValue();
        if (obj instanceof EntityType) {
            rrManager.removeEntityType((EntityType) obj);
        }
    }

    @Override
    protected String getModelExtension() {
        return ResourceManager.RESOURCETYPE_FILE_EXTENSION;
    }
}
