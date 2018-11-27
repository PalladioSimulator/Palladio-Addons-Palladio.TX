package org.palladiosimulator.pcmtx.pcmtxviews.ui;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.edit.ui.action.LoadResourceAction;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import org.palladiosimulator.pcm.core.entity.EntityPackage;

import org.palladiosimulator.pcmtx.PcmtxPackage;
import org.palladiosimulator.pcmtx.Table;
import org.palladiosimulator.pcmtx.pcmtxviews.manager.DataRepositoryManager;
import org.palladiosimulator.pcmtx.pcmtxviews.resource.ResourceManager;

/**
 * This class provides the GUI elements for the {@link TablesView}.
 * 
 * @author Tobias Weiberg
 *
 */
public class TablesUIProvider extends AbstractUIProvider {

    private TableDetailsUI detailsUI;
    private Button loadButton;
    private final DataRepositoryManager drManager;

    /**
     * Creates a new {@code TablesUIProvider} with the given parameters.
     * 
     * @param drManager
     *            the {@link DataRepositoryManager} to access the pcmtx model resource
     */
    public TablesUIProvider(DataRepositoryManager drManager) {
        this.drManager = drManager;
    }

    @Override
    protected void createLabelComposite(Composite parent) {
        Label label = new Label(parent, SWT.NONE);
        label.setText("Tables:");
        label.setLayoutData(new GridData(SWT.BEGINNING, SWT.END, false, false));
        label.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        loadButton = new Button(parent, SWT.PUSH);
        loadButton.setText("Load Resource");
        loadButton.setLayoutData(new GridData(SWT.END, SWT.END, false, false));

        loadButton.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                LoadResourceAction.LoadResourceDialog loadResDialog = new LoadResourceAction.LoadResourceDialog(
                        parent.getShell());
                if (Dialog.OK == loadResDialog.open()) {
                    for (URI uri : loadResDialog.getURIs()) {
                        if (uri.fileExtension().endsWith("resourcetype") || uri.fileExtension().endsWith("pcmtx")) {
                            drManager.loadResource(uri);
                        }
                    }
                    detailsUI.refreshEntityTypesViewer();
                    detailsUI.refreshDatabaseViewer();
                }
            }

        });
    }

    @Override
    protected DetailsUI createDetailsUI(Composite parent) {
        this.detailsUI = new TableDetailsUI(parent, drManager, viewer);
        return detailsUI;
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
                EMFProperties.list(PcmtxPackage.Literals.DATA_REPOSITORY__TABLE).observe(drManager.getEObject()));

        viewer.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

    @Override
    public void updateUI() {
        if (!viewer.getList().isDisposed()) {
            viewer.setInput(
                    EMFProperties.list(PcmtxPackage.Literals.DATA_REPOSITORY__TABLE).observe(drManager.getEObject()));
            viewer.setSelection(StructuredSelection.EMPTY);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        loadButton.setEnabled(enabled);
    }

    @Override
    protected void addButtonSelected() {
        drManager.createAndAddEmptyTable();
        int lastIndex = viewer.getList().getItemCount() - 1;
        viewer.setSelection(new StructuredSelection(viewer.getElementAt(lastIndex)));
    }

    @Override
    protected void removeButtonSelected() {
        Object selection = ViewersObservables.observeSingleSelection(viewer).getValue();
        if (selection instanceof Table) {
            drManager.removeTable((Table) selection);
        }
    }

    @Override
    protected String getModelExtension() {
        return ResourceManager.PCMTX_FILE_EXTENSION;
    }
}
