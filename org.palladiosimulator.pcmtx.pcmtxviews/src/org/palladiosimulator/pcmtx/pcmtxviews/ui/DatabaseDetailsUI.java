package org.palladiosimulator.pcmtx.pcmtxviews.ui;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.palladiosimulator.pcm.core.entity.EntityPackage;
import org.palladiosimulator.pcmtx.Database;
import org.palladiosimulator.pcmtx.PcmtxPackage;
import org.palladiosimulator.pcmtx.TransactionIsolation;
import org.palladiosimulator.pcmtx.pcmtxviews.util.Utils;

/**
 * This class represents a detail UI for a {@link Database}. It provides controls for modifying and
 * viewing attributes of a {@link Database}.
 * 
 * @author Tobias Weiberg
 *
 */
public class DatabaseDetailsUI implements ISelectionChangedListener, DetailsUI {
    private final Composite detailsUI;
    private final Viewer viewer;
    private final DataBindingContext bindingContext;
    private Text nameText;
    private Spinner timeoutSpinner;
    private ComboViewer tiViewer;

    /**
     * Creates a new {@code DatabaseDetailsUI} with the given parameters.
     * 
     * @param parent
     *            the parent {@link Composite}
     * @param viewer
     *            the {@link Viewer} in which the {@link Database} to view details for is selected
     */
    public DatabaseDetailsUI(Composite parent, Viewer viewer) {
        this.viewer = viewer;

        detailsUI = new Composite(parent, SWT.NONE);
        Utils.colorCompositeBackground(detailsUI, SWT.COLOR_LIST_BACKGROUND);

        detailsUI.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        detailsUI.setLayout(new GridLayout(1, false));

        bindingContext = new DataBindingContext();

        createNamingPart();
        createTimeoutPart();
        createTransactionIsolationPart();

        viewer.addSelectionChangedListener(this);
    }

    /**
     * Creates the elements of the naming part of the details UI.
     */
    private void createNamingPart() {
        final Label nameLabel = new Label(detailsUI, SWT.NONE);
        nameLabel.setText("Name:");
        nameLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        nameText = new Text(detailsUI, SWT.SINGLE | SWT.BORDER);
        nameText.setEnabled(false);

        IObservableValue swtTarget = WidgetProperties.text(SWT.Modify).observe(nameText);
        IObservableValue master = ViewersObservables.observeSingleSelection(viewer);
        IObservableValue modelSource = EMFProperties.value(EntityPackage.Literals.NAMED_ELEMENT__ENTITY_NAME)
                .observeDetail(master);
        bindingContext.bindValue(swtTarget, modelSource);

        nameText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
    }

    /**
     * Creates the elements for selection of the timeout value.
     */
    private void createTimeoutPart() {
        final Label timeoutLabel = new Label(detailsUI, SWT.NONE);
        timeoutLabel.setText("Timeout:");
        timeoutLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        timeoutSpinner = new Spinner(detailsUI, SWT.BORDER);
        timeoutSpinner.setMinimum(0);
        timeoutSpinner.setMaximum(Integer.MAX_VALUE);
        timeoutSpinner.setEnabled(false);

        IObservableValue swtTarget = WidgetProperties.selection().observe(timeoutSpinner);
        IObservableValue master = ViewersObservables.observeSingleSelection(viewer);
        IObservableValue modelSource = EMFProperties.value(PcmtxPackage.Literals.DATABASE__TIMEOUT)
                .observeDetail(master);
        bindingContext.bindValue(swtTarget, modelSource);

        timeoutSpinner.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
    }

    /**
     * Creates the elements for selection of the transaction isolation.
     */
    private void createTransactionIsolationPart() {
        final Label tiLabel = new Label(detailsUI, SWT.NONE);
        tiLabel.setText("Transaction Isolation:");
        tiLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        tiViewer = new ComboViewer(detailsUI, SWT.DROP_DOWN);
        tiViewer.getCombo().setEnabled(false);
        tiViewer.setContentProvider(new ArrayContentProvider());
        tiViewer.setInput(TransactionIsolation.VALUES);

        IObservableValue tiSelection = ViewersObservables.observeSingleSelection(tiViewer);
        IObservableValue master = ViewersObservables.observeSingleSelection(viewer);
        bindingContext.bindValue(tiSelection,
                EMFProperties.value(PcmtxPackage.Literals.DATABASE__ISOLATION).observeDetail(master));

        tiViewer.getCombo().setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
        tiViewer.getCombo().setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

        // makes Combo read only but keeps a white background color
        // using of style constant SWT.READ_ONLY results in grey background color on windows systems
        tiViewer.getCombo().addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getSource().equals(tiViewer.getCombo())) {
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

    /**
     * Enables the elements if a Database is selected in the viewer.
     */
    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        Object selection = ViewersObservables.observeSingleSelection(viewer).getValue();
        if (selection instanceof Database) {
            nameText.setEnabled(true);
            timeoutSpinner.setEnabled(true);
            tiViewer.getCombo().setEnabled(true);
        } else {
            nameText.setEnabled(false);
            timeoutSpinner.setEnabled(false);
            timeoutSpinner.setSelection(0);
            tiViewer.getCombo().setEnabled(false);
        }
        timeoutSpinner.update();
        tiViewer.refresh();
    }

    @Override
    public void dispose() {
        viewer.removeSelectionChangedListener(this);
    }
}
