package org.palladiosimulator.pcmtx.pcmtxviews.ui;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.palladiosimulator.pcm.resourcetype.ResourceInterface;
import org.palladiosimulator.pcm.resourcetype.ResourceSignature;
import org.palladiosimulator.pcm.resourcetype.ResourcetypeFactory;

/**
 * This class represents a dialog for refactoring of {@link ResourceSignature}s and adding of
 * {@link ResourceSignature}s to the {@link ResourceInterface} an {@link EntityType} provides.
 * 
 * @author Tobias Weiberg
 *
 */
public class ResourceSignatureDialog extends TitleAreaDialog {
    private final ResourceInterface etypeInterface;
    private final ResourceSignature etypeSignature;
    private Text nameText;
    private final boolean newSignature;

    /**
     * Creates a new {@code ResourceSignatureDialog} with the given parameters.
     * 
     * @param parent
     *            the parent {@link Shell}
     * @param etypeInterface
     *            the {@link ResourceInterface} that provides the {@link ResourceInterface}
     * @param etypeSignature
     *            the {@link ResourceSignature} to refactor, can be {@code null} if a new
     *            {@link ResourceSignature} should be created
     */
    public ResourceSignatureDialog(Shell parent, ResourceInterface etypeInterface, ResourceSignature etypeSignature) {
        super(parent);
        this.etypeInterface = etypeInterface;
        this.newSignature = etypeSignature == null;
        if (newSignature) {
            this.etypeSignature = ResourcetypeFactory.eINSTANCE.createResourceSignature();
        } else {
            this.etypeSignature = etypeSignature;
        }
    }

    @Override
    public void create() {
        super.create();
        if (newSignature) {
            setTitle("Add Signature");
        } else {
            setTitle("Rename Signature");
        }

        setMessage("Enter the name for the signature.", IMessageProvider.INFORMATION);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        this.getShell().setMinimumSize(300, 200);

        Composite area = (Composite) super.createDialogArea(parent);
        Composite container = new Composite(area, SWT.NONE);

        container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        container.setLayout(new GridLayout(2, false));

        createNamingArea(container);
        return area;
    }

    @Override
    public boolean isHelpAvailable() {
        return false;
    }

    /**
     * Creates the naming area.
     * 
     * @param parent
     *            the parent {@link Composite}
     */
    private void createNamingArea(Composite parent) {
        final Label nameLabel = new Label(parent, SWT.NONE);
        nameLabel.setText("Name:");

        nameText = new Text(parent, SWT.BORDER);
        nameText.setText(etypeSignature.getEntityName());
        nameText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
    }

    @Override
    protected boolean isResizable() {
        return true;
    }

    @Override
    protected void okPressed() {
        this.etypeSignature.setEntityName(nameText.getText());
        if (newSignature) {
            this.etypeInterface.getResourceSignatures__ResourceInterface().add(etypeSignature);
        }
        super.okPressed();
    }
}
