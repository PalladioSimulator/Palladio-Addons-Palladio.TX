package org.palladiosimulator.pcmtx.pcmtxviews.ui;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ObservableMapLabelProvider;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.palladiosimulator.pcm.core.entity.EntityPackage;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcmtx.Database;
import org.palladiosimulator.pcmtx.EntityType;
import org.palladiosimulator.pcmtx.PcmtxPackage;
import org.palladiosimulator.pcmtx.Table;
import org.palladiosimulator.pcmtx.pcmtxviews.util.Utils;
import org.palladiosimulator.pcmtx.pcmtxviews.manager.DataRepositoryManager;

/**
 * This class represents a detail UI for a {@link Table}. It provides controls for modifying and
 * viewing attributes of the {@link Table}.
 * 
 * @author Tobias Weiberg
 *
 */
public class TableDetailsUI implements ISelectionChangedListener, Observer, DetailsUI {
    private final Composite detailsUI;
    private final DataRepositoryManager drManager;
    private final DataBindingContext bindingContext;
    private ListViewer etypesInTableViewer;
    private TreeViewer availableEtypesTreeViewer;
    private Button addButton;
    private Button removeButton;
    private final Viewer viewer;
    private Text nameText;
    private Spinner rowsSpinner;
    private ComboViewer dbViewer;
    private ViewerFilter entityTypesNotInTableFilter;

    /**
     * Creates a new {@code TableDetailsUI} with the given parameters.
     * 
     * @param parent
     *            the parent {@link Composite}
     * @param drManager
     *            the {@link DataRepositoryManager} to find available {@link Database}s
     * @param viewer
     *            the viewer the {@link Table} to view details for is selected
     */
    public TableDetailsUI(Composite parent, DataRepositoryManager drManager, Viewer viewer) {
        this.drManager = drManager;
        this.viewer = viewer;

        detailsUI = new Composite(parent, SWT.NONE);
        Utils.colorCompositeBackground(detailsUI, SWT.COLOR_LIST_BACKGROUND);

        detailsUI.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        detailsUI.setLayout(new GridLayout(3, false));

        bindingContext = new DataBindingContext();

        createNamingPart();
        createEntityTypesMappingPart();
        createRowsPart();
        createDatabasePart();

        viewer.addSelectionChangedListener(this);
        this.drManager.addObserver(this);
    }

    /**
     * Creates the elements of the naming part of the detail UI.
     */
    private void createNamingPart() {
        Composite composite = new Composite(detailsUI, SWT.NONE);
        Utils.colorCompositeBackground(composite, SWT.COLOR_LIST_BACKGROUND);

        composite.setLayout(new GridLayout(2, false));
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        gridData.horizontalSpan = 3;
        composite.setLayoutData(gridData);

        final Label nameLabel = new Label(composite, SWT.NONE);
        nameLabel.setText("Name:");
        nameLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        nameText = new Text(composite, SWT.SINGLE | SWT.BORDER);
        nameText.setEnabled(false);
        nameText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

        IObservableValue swtTarget = WidgetProperties.text(SWT.Modify).observe(nameText);
        IObservableValue master = ViewersObservables.observeSingleSelection(viewer);
        IObservableValue modelSource = EMFProperties.value(EntityPackage.Literals.NAMED_ELEMENT__ENTITY_NAME)
                .observeDetail(master);
        bindingContext.bindValue(swtTarget, modelSource);
    }

    /**
     * Creates the elements of the part for mapping {@link EntityType}s to the {@link Table}.
     */
    private void createEntityTypesMappingPart() {

        createEntityTypesMappingLabels(detailsUI);

        createEtypesInTableListViewer(detailsUI);

        createEntityTypesMappingButtons(detailsUI);

        createTreeViewer(detailsUI);

        createAndRegisterViewerFilterAndListeners();

    }

    /**
     * Creates the labels for the {@link EntityType}s mapping part.
     * 
     * @param parent
     *            the parent {@link Composite}
     */
    private void createEntityTypesMappingLabels(Composite parent) {
        final Label etypesInTableLabel = new Label(parent, SWT.NONE);
        etypesInTableLabel.setText("EntityTypes in Table");
        etypesInTableLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.END, false, false));
        etypesInTableLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        new Label(parent, SWT.NONE); // spacing of one cell

        final Label etypesAvailableLabel = new Label(parent, SWT.NONE);
        etypesAvailableLabel.setText("Available EntityTypes");
        etypesAvailableLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.END, false, false));
        etypesAvailableLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
    }

    /**
     * Creates the buttons for the {@link EntityType}s mapping part.
     * 
     * @param parent
     *            the parent {@link Composite}
     */
    private void createEntityTypesMappingButtons(Composite parent) {
        final Composite buttonComposite = new Composite(parent, SWT.NONE);
        Utils.colorCompositeBackground(buttonComposite, SWT.COLOR_LIST_BACKGROUND);

        buttonComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));
        buttonComposite.setLayout(new GridLayout(1, false));

        addButton = new Button(buttonComposite, SWT.PUSH);
        addButton.setText("<<");
        addButton.setEnabled(false);
        addButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

        removeButton = new Button(buttonComposite, SWT.PUSH);
        removeButton.setText(">>");
        removeButton.setEnabled(false);
        removeButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

        createAndRegisterButtonListener();
    }

    /**
     * Creates ListViewer to view the {@link EntityType}s currently contained in the {@link Table}.
     * 
     * @param parent
     *            the parent {@link Composite}
     */
    private void createEtypesInTableListViewer(Composite parent) {
        etypesInTableViewer = new ListViewer(parent, SWT.BORDER | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);

        ObservableListContentProvider cp = new ObservableListContentProvider();
        IObservableMap map = EMFProperties.value(EntityPackage.Literals.NAMED_ELEMENT__ENTITY_NAME)
                .observeDetail(cp.getKnownElements());

        etypesInTableViewer.setLabelProvider(new ObservableMapLabelProvider(map));
        etypesInTableViewer.setContentProvider(cp);
        IObservableValue master = ViewersObservables.observeSingleSelection(viewer);
        etypesInTableViewer.setInput(EMFProperties.list(PcmtxPackage.Literals.TABLE__TYPES).observeDetail(master));

        etypesInTableViewer.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

    /**
     * Creates the TreeViewer to view available {@link EntityType}s.
     * 
     * @param parent
     *            the parent {@link Composite}
     */
    private void createTreeViewer(Composite parent) {
        availableEtypesTreeViewer = new AvailableEntityTypesTreeViewer(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL,
                drManager);

        availableEtypesTreeViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

    /**
     * Creates and registers filters and listeners for the viewers
     */
    private void createAndRegisterViewerFilterAndListeners() {
        entityTypesNotInTableFilter = new ViewerFilter() {

            @Override
            public boolean select(Viewer viewer, Object parentElement, Object element) {
                if (element instanceof EntityType) {
                    EntityType etype = (EntityType) element;
                    IObservableList list = (IObservableList) etypesInTableViewer.getInput();
                    EntityType etypeInTable;
                    for (Object o : list) {
                        if (o instanceof EntityType) {
                            etypeInTable = (EntityType) o;

                            if (etype.getId().equals(etypeInTable.getId())) {
                                if (!etypeInTable.getEntityName().equals(etype.getEntityName())) {
                                    // use opportunity to update the name of EntityTypes in the
                                    // Table
                                    etypeInTable.setEntityName(etype.getEntityName());
                                }
                                return false;
                            }
                        }
                    }
                    return true;
                } else if (element instanceof ResourceRepository) {
                    return true;
                }
                return false;
            }
        };
        availableEtypesTreeViewer.addFilter(entityTypesNotInTableFilter);

        ISelectionChangedListener selectionChangedListener = new ISelectionChangedListener() {
            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                Object selectedTable = ViewersObservables.observeSingleSelection(viewer).getValue();
                if (event.getSource() == etypesInTableViewer && selectedTable instanceof Table) {
                    if (event.getSelection().isEmpty()) {
                        removeButton.setEnabled(false);
                    } else {
                        removeButton.setEnabled(true);
                    }
                } else if (event.getSource() == availableEtypesTreeViewer && selectedTable instanceof Table) {
                    if (event.getSelection().isEmpty()) {
                        addButton.setEnabled(false);
                    } else {
                        addButton.setEnabled(true);
                    }
                }
            }
        };

        etypesInTableViewer.addSelectionChangedListener(selectionChangedListener);
        availableEtypesTreeViewer.addSelectionChangedListener(selectionChangedListener);
    }

    /**
     * Creates and registers necessary listeners for the buttons.
     */
    private void createAndRegisterButtonListener() {

        addButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Object selectedAvailableEtype = availableEtypesTreeViewer.getStructuredSelection().getFirstElement();
                Object selectedTable = ViewersObservables.observeSingleSelection(viewer).getValue();

                if (selectedAvailableEtype instanceof EntityType) {
                    EntityType etype = (EntityType) selectedAvailableEtype;
                    if (selectedTable instanceof Table) {
                        ((Table) selectedTable).getTypes().add(etype);
                    }
                    etypesInTableViewer.refresh();
                    availableEtypesTreeViewer.refresh();
                }
            }
        });

        removeButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                IObservableValue selection = ViewersObservables.observeSingleSelection(etypesInTableViewer);
                Object selectedTable = ViewersObservables.observeSingleSelection(viewer).getValue();
                EntityType etype = null;
                Object selectedEtypeInTable = selection.getValue();

                if (selectedEtypeInTable instanceof EntityType) {
                    etype = (EntityType) selectedEtypeInTable;

                    if (selectedTable instanceof Table) {
                        ((Table) selectedTable).getTypes().remove(etype);
                    }
                    etypesInTableViewer.refresh();
                    availableEtypesTreeViewer.refresh();
                }
            }
        });
    }

    /**
     * Creates the elements for selection of the number of rows.
     */
    private void createRowsPart() {
        Composite composite = new Composite(detailsUI, SWT.NONE);
        Utils.colorCompositeBackground(composite, SWT.COLOR_LIST_BACKGROUND);

        composite.setLayout(new GridLayout(2, false));
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        gridData.horizontalSpan = 3;
        composite.setLayoutData(gridData);

        final Label rowsLabel = new Label(composite, SWT.NONE);
        rowsLabel.setText("Rows:");
        rowsLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        rowsSpinner = new Spinner(composite, SWT.BORDER);
        rowsSpinner.setMinimum(0);
        rowsSpinner.setMaximum(Integer.MAX_VALUE);
        rowsSpinner.setEnabled(false);
        rowsSpinner.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

        IObservableValue swtTarget = WidgetProperties.selection().observe(rowsSpinner);
        IObservableValue master = ViewersObservables.observeSingleSelection(viewer);
        IObservableValue modelSource = EMFProperties.value(PcmtxPackage.Literals.TABLE__ROWS).observeDetail(master);

        bindingContext.bindValue(swtTarget, modelSource);
    }

    /**
     * Creates the elements for selection of the {@link Database}.
     */
    private void createDatabasePart() {
        Composite composite = new Composite(detailsUI, SWT.NONE);
        Utils.colorCompositeBackground(composite, SWT.COLOR_LIST_BACKGROUND);
        composite.setLayout(new GridLayout(2, false));
        GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        gridData.horizontalSpan = 3;
        composite.setLayoutData(gridData);

        final Label dbLabel = new Label(composite, SWT.NONE);
        dbLabel.setText("Database:");
        dbLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
        dbViewer = new ComboViewer(composite, SWT.DROP_DOWN);
        dbViewer.getCombo().setEnabled(false);

        ArrayContentProvider acp = new ArrayContentProvider();
        dbViewer.setLabelProvider(new DatabaseViewerLabelProvider());
        dbViewer.setContentProvider(acp);
        dbViewer.setInput(drManager.getAvailableDatabases());

        IObservableValue dbSelection = ViewersObservables.observeSingleSelection(dbViewer);
        IObservableValue master = ViewersObservables.observeSingleSelection(viewer);
        bindingContext.bindValue(dbSelection,
                EMFProperties.value(PcmtxPackage.Literals.TABLE__DATABASE).observeDetail(master));

        dbViewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

        // makes Combo read only but keeps a white background color
        // using of style constant SWT.READ_ONLY results in grey background color on windows systems
        dbViewer.getCombo().addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getSource().equals(dbViewer.getCombo())) {
                    e.doit = false;

                    // allow arrow key selection
                    if (e.keyCode == SWT.ARROW_UP || e.keyCode == SWT.ARROW_DOWN) {
                        e.doit = true;
                    }
                }

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // not needed
            }

        });

    }

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        this.availableEtypesTreeViewer.refresh();
        Object selectedTable = ViewersObservables.observeSingleSelection(viewer).getValue();
        if (selectedTable instanceof Table) {
            nameText.setEnabled(true);
            rowsSpinner.setEnabled(true);
            dbViewer.getCombo().setEnabled(true);

        } else {
            nameText.setEnabled(false);
            addButton.setEnabled(false);
            rowsSpinner.setEnabled(false);
            dbViewer.getCombo().setEnabled(false);
        }
        this.availableEtypesTreeViewer.setSelection(StructuredSelection.EMPTY);
        refreshDatabaseViewer();
        this.rowsSpinner.update();
    }

    /**
     * Refreshes the Viewer for available {@link Database}s.
     */
    public void refreshDatabaseViewer() {
        Object selected = ViewersObservables.observeSingleSelection(viewer).getValue();
        Database db = null;
        Table selectedTable = null;
        if (selected != null && selected instanceof Table) {
            selectedTable = (Table) selected;
            db = selectedTable.getDatabase();
        }
        dbViewer.refresh();

        // reset selection
        if (db != null) {
            dbViewer.setSelection(new StructuredSelection(db));
        }
    }

    /**
     * Refreshes the TreeViewer for available {@link EntityType}s.
     */
    public void refreshEntityTypesViewer() {
        this.etypesInTableViewer.refresh();
        this.availableEtypesTreeViewer.refresh();

        if (drManager.getLoadedResourceRepositories().size() > 0) {
            ITreeContentProvider contentProvider = (ITreeContentProvider) this.availableEtypesTreeViewer
                    .getContentProvider();
            ResourceRepository repos = drManager.getLoadedResourceRepositories().get(0);
            entityTypesNotInTableFilter.filter(availableEtypesTreeViewer, repos, contentProvider.getChildren(repos));
        }
    }

    /**
     * Label provider for {@link Database} ComboViewer.
     */
    private static class DatabaseViewerLabelProvider extends LabelProvider {
        @Override
        public String getText(Object element) {
            String dbRep = "";
            if (element instanceof Database) {
                Database db = (Database) element;
                dbRep += db.getEntityName();
                dbRep += " (Isolation: " + db.getIsolation() + ", ";
                dbRep += "Timeout: " + db.getTimeout() + ") ";
            }
            return dbRep;
        }
    }

    /**
     * Notifies this {@code TableDetailsUI} about changes in the list of available
     * {@link EntityType}s or in the list of available {@link Database}s.
     */
    @Override
    public void update(Observable o, Object arg) {
        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                if (o.equals(drManager)) {
                    refreshDatabaseViewer();
                    refreshEntityTypesViewer();
                }
            }
        });
    }

    /**
     * Disposes the {@code TableDetailsUI}.
     */
    public void dispose() {
        viewer.removeSelectionChangedListener(this);
        this.drManager.deleteObserver(this);
    }
}
