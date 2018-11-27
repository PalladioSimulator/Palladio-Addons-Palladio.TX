package org.palladiosimulator.pcmtx.pcmtxviews.ui;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.palladiosimulator.pcm.core.entity.EntityPackage;
import org.palladiosimulator.pcmtx.Database;
import org.palladiosimulator.pcmtx.PcmtxPackage;
import org.palladiosimulator.pcmtx.pcmtxviews.manager.DataRepositoryManager;
import org.palladiosimulator.pcmtx.pcmtxviews.resource.ResourceManager;

/**
 * This class provides the GUI elements for the {@link DatabasesView}.
 * 
 * @author Tobias Weiberg
 *
 */
public class DatabasesUIProvider extends AbstractUIProvider {

    private final DataRepositoryManager drManager;

    /**
     * Creates a new {@code DatabasesUIProvider} with the given {@link DataRepositoryManager}.
     * 
     * @param drManager
     *            the {@link DataRepositoryManager} to access the model resource
     */
    public DatabasesUIProvider(DataRepositoryManager drManager) {
        this.drManager = drManager;
    }

    @Override
    protected void createLabelComposite(Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        label.setText("Databases:");
        label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        GridData gridData = new GridData(SWT.BEGINNING, SWT.END, false, false);
        gridData.horizontalSpan = 2;
        label.setLayoutData(gridData);
    }

    @Override
    protected DetailsUI createDetailsUI(Composite parent) {
        return new DatabaseDetailsUI(parent, viewer);
    }

    @Override
    protected void createAndInitList(Composite parent) {
        viewer = new ListViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);

        ObservableListContentProvider cp = new ObservableListContentProvider();

        EAttribute nameAttribute = EntityPackage.Literals.NAMED_ELEMENT__ENTITY_NAME;
        IObservableMap map = EMFProperties.value(nameAttribute).observeDetail(cp.getKnownElements());
        viewer.setLabelProvider(new ObservableMapLabelProvider(map));

        viewer.setContentProvider(cp);
        viewer.setInput(
                EMFProperties.list(PcmtxPackage.Literals.DATA_REPOSITORY__DATABASES).observe(drManager.getEObject()));

        viewer.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

    @Override
    public void updateUI() {
        if (!viewer.getList().isDisposed()) {
            viewer.setInput(EMFProperties.list(PcmtxPackage.Literals.DATA_REPOSITORY__DATABASES)
                    .observe(drManager.getEObject()));
            viewer.setSelection(StructuredSelection.EMPTY);
        }
    }

    @Override
    protected void addButtonSelected() {
        drManager.createAndAddEmptyDatabase();
        int lastIndex = viewer.getList().getItemCount() - 1;
        viewer.setSelection(new StructuredSelection(viewer.getElementAt(lastIndex)));
    }

    @Override
    protected void removeButtonSelected() {
        IObservableValue dbSelection = ViewersObservables.observeSingleSelection(viewer);
        Object obj = dbSelection.getValue();
        if (obj instanceof Database) {
            drManager.removeDatabase((Database) obj);
        }
    }

    @Override
    protected String getModelExtension() {
        return ResourceManager.PCMTX_FILE_EXTENSION;
    }
}
