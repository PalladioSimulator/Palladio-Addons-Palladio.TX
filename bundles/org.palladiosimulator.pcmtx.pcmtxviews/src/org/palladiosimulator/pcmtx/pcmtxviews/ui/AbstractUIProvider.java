package org.palladiosimulator.pcmtx.pcmtxviews.ui;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.palladiosimulator.pcmtx.pcmtxviews.util.Utils;

/**
 * This abstract class provides basic UIProvider functionality.
 * 
 * @author Tobias Weiberg
 *
 */
public abstract class AbstractUIProvider implements UIProvider {

    protected ListViewer viewer;
    private Button addButton;
    private Button removeButton;
    private DetailsUI detailsUI;
    private ScrolledComposite viewComposite;
    private Composite mainComposite;
    private Composite firstStepsComposite;
    private boolean enabled;

    @Override
    public void createUI(ScrolledComposite parent) {
        enabled = false;
        this.viewComposite = parent;
        mainComposite = new Composite(viewComposite, SWT.NONE);
        mainComposite.setLayout(new GridLayout(2, false));
        Utils.colorCompositeBackground(mainComposite, SWT.COLOR_LIST_BACKGROUND);

        createLabelComposite(mainComposite);

        createListComposite(mainComposite);

        detailsUI = createDetailsUI(mainComposite);

        createFirstStepsComposite(viewComposite);

        parent.setContent(firstStepsComposite);
        parent.setExpandHorizontal(true);
        parent.setExpandVertical(true);
        parent.setMinSize(firstStepsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

    }

    /**
     * Creates the elements of the label area.
     * 
     * @param parent
     *            the parent {@link Composite}
     */
    protected abstract void createLabelComposite(Composite parent);

    /**
     * Creates the elements of the list area.
     * 
     * @param parent
     *            the parent {@link Composite}
     */
    private void createListComposite(Composite parent) {
        Composite tableComposite = new Composite(parent, SWT.NONE);
        Utils.colorCompositeBackground(tableComposite, SWT.COLOR_LIST_BACKGROUND);

        tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        tableComposite.setLayout(new GridLayout(1, false));

        createAndInitList(tableComposite);

        createAndInitButtons(tableComposite);

        addListeners();
    }

    /**
     * Creates the {@link DetailsUI} and returns it.
     * 
     * @param parent
     *            the parent {@link Composite}
     * @return the created {@link DetailsUI}
     */
    protected abstract DetailsUI createDetailsUI(Composite parent);

    /**
     * Creates a {@link Composite} with information about first steps that is shown if no model
     * resource is selected.
     * 
     * @param parent
     *            the parent {@link Composite}
     */
    private void createFirstStepsComposite(Composite parent) {
        firstStepsComposite = new Composite(parent, SWT.NONE);
        firstStepsComposite.setLayout(new GridLayout(1, false));
        Utils.colorCompositeBackground(firstStepsComposite, SWT.COLOR_LIST_BACKGROUND);

        String modelName = Character.toUpperCase(getModelExtension().charAt(0)) + getModelExtension().substring(1);

        final CLabel label = new CLabel(firstStepsComposite, SWT.WRAP | SWT.LEFT);
        String text = "Currently, no valid " + getModelExtension() + " resource is selected. \n"
                + "To start working on " + modelName + " models,\n" + "select an already existing "
                + getModelExtension() + " resource in the Project Explorer or\n"
                + "create a new one by following the steps provided below: \n"
                + "    1. Right click on a Palladio project in the Project Explorer \n"
                + "    2. Choose New -> Other...\n" 
                + "    3. Search for " + getModelExtension() + " and select " + modelName + " model\n" 
                + "    4. Follow the steps of the EMF Creation Wizard";

        label.setText(text);
        label.setImage(Display.getCurrent().getSystemImage(SWT.ICON_INFORMATION));
        GridData gridData = new GridData(SWT.CENTER, SWT.CENTER, true, true);
        label.setLayoutData(gridData);

        Utils.setFont(label, 14, SWT.BOLD);

        Utils.colorCompositeBackground(label, SWT.COLOR_LIST_BACKGROUND);
    }

    /**
     * Creates and initializes the {@link ListViewer}.
     * 
     * @param parent
     *            the parent {@link Composite}
     */
    protected abstract void createAndInitList(Composite parent);

    @Override
    public abstract void updateUI();

    @Override
    public void clearUI() {
        Display.getDefault().syncExec(new Runnable() {
            public void run() {
                viewer.setInput(null);
                setEnabled(false);
            }
        });
    }

    /**
     * Creates and initializes the necessary buttons.
     * 
     * @param parent
     *            the parent {@link Composite}
     */
    private void createAndInitButtons(Composite parent) {
        Composite buttonComposite = new Composite(parent, SWT.NONE);
        Utils.colorCompositeBackground(buttonComposite, SWT.COLOR_LIST_BACKGROUND);

        buttonComposite.setLayout(new GridLayout(2, true));

        addButton = new Button(buttonComposite, SWT.PUSH);
        addButton.setText("Add");
        addButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
        addButton.setEnabled(false);

        removeButton = new Button(buttonComposite, SWT.PUSH);
        removeButton.setText("Remove");
        removeButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
        removeButton.setEnabled(false);

    }

    /**
     * Adds button and viewer listeners.
     */
    private void addListeners() {
        viewer.addSelectionChangedListener(new ISelectionChangedListener() {

            @Override
            public void selectionChanged(SelectionChangedEvent event) {
                if (event.getSelection().isEmpty()) {
                    removeButton.setEnabled(false);
                } else {
                    removeButton.setEnabled(true);
                }
            }

        });

        SelectionAdapter buttonSelectionAdapter = new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                if (e.widget == addButton) {
                    addButtonSelected();
                } else if (e.widget == removeButton) {
                    removeButtonSelected();
                }
            }
        };

        addButton.addSelectionListener(buttonSelectionAdapter);
        removeButton.addSelectionListener(buttonSelectionAdapter);
    }

    /**
     * This method is called when the add button is selected.
     */
    protected abstract void addButtonSelected();

    /**
     * This method is called when the remove button is selected.
     */
    protected abstract void removeButtonSelected();

    @Override
    public void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        if (enabled) {
            viewComposite.setContent(mainComposite);
            viewComposite.setExpandHorizontal(true);
            viewComposite.setExpandVertical(true);
            viewComposite.setMinSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));

        } else {
            viewComposite.setContent(firstStepsComposite);
            viewComposite.setExpandHorizontal(true);
            viewComposite.setExpandVertical(true);
            viewComposite.setMinSize(firstStepsComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        }
        this.enabled = enabled;
        addButton.setEnabled(enabled);
    }

    @Override
    public void setFocus() {
        firstStepsComposite.setFocus();
    }

    @Override
    public void dispose() {
        detailsUI.dispose();

    }

    /**
     * Returns a string representation of the extension of model files that can be modified by the
     * elements in the provided UI
     * 
     * @return the extension of model files that can be modified by the provided UI
     */
    protected abstract String getModelExtension();

}
