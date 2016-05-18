package org.palladiosimulator.pcmtx.pcmtxviews.ui;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.IViewerObservableValue;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.palladiosimulator.pcm.core.entity.EntityPackage;
import org.palladiosimulator.pcm.core.entity.ResourceProvidedRole;
import org.palladiosimulator.pcm.resourcetype.ResourceInterface;
import org.palladiosimulator.pcm.resourcetype.ResourceSignature;
import org.palladiosimulator.pcm.resourcetype.ResourcetypePackage;
import org.palladiosimulator.pcmtx.EntityType;
import org.palladiosimulator.pcmtx.pcmtxviews.util.Utils;

/**
 * This class represents a detail UI for a {@link EntityType}. It provides controls for modifying
 * and viewing attributes of the {@link EntityType}.
 * 
 * @author Tobias Weiberg
 *
 */
public class EntityTypeDetailsUI implements ISelectionChangedListener, DetailsUI {
    private final Composite detailsUI;

    private final DataBindingContext bindingContext;
    private ListViewer etypeSignaturesViewer;
    private Text nameText;
    private Viewer viewer;
    private ResourceInterface rInterface;
    private Button addButton;
    private Button renameButton;
    private Button removeButton;

    /**
     * Creates a new {@code EntityTypeDetailsUI} with the given parameters.
     * 
     * @param parent
     *            the parent {@link composite}
     * @param viewer
     *            the viewer the {@link EntityType} to view details for is selected
     */
    public EntityTypeDetailsUI(Composite parent, Viewer viewer) {
        this.viewer = viewer;

        detailsUI = new Composite(parent, SWT.NONE);
        Utils.colorCompositeBackground(detailsUI, SWT.COLOR_LIST_BACKGROUND);

        detailsUI.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        detailsUI.setLayout(new GridLayout(2, false));

        bindingContext = new DataBindingContext();

        createNamingPart();
        createAccessMethodsPart();

        viewer.addSelectionChangedListener(this);
    }

    /**
     * Creates the elements of the naming part of the detail UI.
     */
    private void createNamingPart() {
        Composite composite = new Composite(detailsUI, SWT.NONE);
        Utils.colorCompositeBackground(composite, SWT.COLOR_LIST_BACKGROUND);

        composite.setLayout(new GridLayout(2, false));
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        gridData.horizontalSpan = 2;
        composite.setLayoutData(gridData);

        final Label nameLabel = new Label(composite, SWT.NONE);
        nameLabel.setText("Name:");
        nameLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        nameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
        nameText.setEnabled(false);
        nameText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

        IObservableValue swtTarget = WidgetProperties.text(SWT.Modify).observe(nameText);
        IViewerObservableValue master = ViewerProperties.singleSelection().observe(viewer);
        IObservableValue modelSource = EMFProperties.value(EntityPackage.Literals.NAMED_ELEMENT__ENTITY_NAME)
                .observeDetail(master);
        bindingContext.bindValue(swtTarget, modelSource);
    }

    /**
     * Creates the elements of the part for provided access methods
     */
    private void createAccessMethodsPart() {
        final Label accessMethodsLabel = new Label(detailsUI, SWT.NONE);
        accessMethodsLabel.setText("Provided Access Methods:");
        accessMethodsLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        GridData gridData = new GridData(SWT.BEGINNING, SWT.END, false, false);
        gridData.horizontalSpan = 2;
        accessMethodsLabel.setLayoutData(gridData);

        createAndInitSignaturesList(detailsUI);

        createAndInitButtons(detailsUI);

        createAndRegisterListeners();
    }

    /**
     * Creates and initializes the signatures list.
     * 
     * @param parent
     *            the parent {@link Composite}
     */
    private void createAndInitSignaturesList(Composite parent) {
        etypeSignaturesViewer = new ListViewer(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);

        ObservableListContentProvider cpSignatures = new ObservableListContentProvider();
        IObservableMap mapSignatures = EMFProperties.value(EntityPackage.Literals.NAMED_ELEMENT__ENTITY_NAME)
                .observeDetail(cpSignatures.getKnownElements());

        etypeSignaturesViewer.setLabelProvider(new ObservableMapLabelProvider(mapSignatures));
        etypeSignaturesViewer.setContentProvider(cpSignatures);

        etypeSignaturesViewer.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

    /**
     * Creates and initializes the necessary buttons.
     * 
     * @param parent
     *            the parent {@link Composite}
     */
    private void createAndInitButtons(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        Utils.colorCompositeBackground(composite, SWT.COLOR_LIST_BACKGROUND);

        composite.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
        composite.setLayout(new GridLayout(1, false));

        addButton = new Button(composite, SWT.PUSH);
        addButton.setText("Add");
        addButton.setEnabled(false);
        addButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
        removeButton = new Button(composite, SWT.PUSH);
        removeButton.setText("Remove");
        removeButton.setEnabled(false);
        removeButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
        renameButton = new Button(composite, SWT.PUSH);
        renameButton.setText("Rename");
        renameButton.setEnabled(false);
        renameButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
    }

    /**
     * Creates and registers the necessary listeners for the buttons and the ListViewer.
     */
    private void createAndRegisterListeners() {
        SelectionAdapter buttonSelectionAdapter = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                IObservableValue selection = ViewersObservables.observeSingleSelection(etypeSignaturesViewer);
                ResourceSignature sig = null;
                Object obj = selection.getValue();
                if (obj instanceof ResourceSignature) {
                    sig = (ResourceSignature) obj;
                }

                if (e.widget == addButton && rInterface != null) {
                    new ResourceSignatureDialog(detailsUI.getShell(), rInterface, null).open();
                    addButton.setFocus();
                } else if (e.widget == renameButton && rInterface != null) {
                    new ResourceSignatureDialog(detailsUI.getShell(), rInterface, sig).open();
                    renameButton.setFocus();
                } else if (e.widget == removeButton && rInterface != null) {
                    rInterface.getResourceSignatures__ResourceInterface().remove(sig);
                }
            }
        };

        addButton.addSelectionListener(buttonSelectionAdapter);
        removeButton.addSelectionListener(buttonSelectionAdapter);
        renameButton.addSelectionListener(buttonSelectionAdapter);

        ISelectionChangedListener selectionChangedListener = new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                if (event.getSelection().isEmpty()) {
                    removeButton.setEnabled(false);
                    renameButton.setEnabled(false);
                } else {
                    removeButton.setEnabled(true);
                    renameButton.setEnabled(true);
                }
            }
        };

        etypeSignaturesViewer.addSelectionChangedListener(selectionChangedListener);
    }

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        Object selection = ViewersObservables.observeSingleSelection(viewer).getValue();
        if (selection instanceof EntityType) {
            EntityType etype = (EntityType) selection;
            ResourceProvidedRole role = etype.getResourceProvidedRoles__ResourceInterfaceProvidingEntity().get(0);
            rInterface = role.getProvidedResourceInterface__ResourceProvidedRole();
            etypeSignaturesViewer.setInput(EMFProperties
                    .list(ResourcetypePackage.Literals.RESOURCE_INTERFACE__RESOURCE_SIGNATURES_RESOURCE_INTERFACE)
                    .observe(rInterface));
            addButton.setEnabled(true);
            renameButton.setEnabled(false);
            removeButton.setEnabled(false);
            nameText.setEnabled(true);

        } else {
            etypeSignaturesViewer.setInput(null);
            addButton.setEnabled(false);
            renameButton.setEnabled(false);
            removeButton.setEnabled(false);
            nameText.setEnabled(false);
        }

        etypeSignaturesViewer.refresh();
    }

    @Override
    public void dispose() {
        viewer.removeSelectionChangedListener(this);
    }

}
