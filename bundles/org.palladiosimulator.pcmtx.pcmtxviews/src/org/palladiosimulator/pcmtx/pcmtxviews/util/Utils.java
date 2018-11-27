package org.palladiosimulator.pcmtx.pcmtxviews.util;

import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISaveablePart2;

/**
 * Utility class.
 * 
 * @author Tobias Weiberg
 */
public class Utils {

    /**
     * Sets the {@link Font} of the given {@link Composite}. The new {@link Font} has the same name
     * as the {@link Font} used before. The style and the height is set to the given values.
     * 
     * Uses {@link LocalResourceManager} to dispose the {@link Font} if it is no longer needed.
     * 
     * @param composite
     *            the {@link Composite} the {@link Font} is set for
     * @param height
     *            the height of the {@link Font}
     * @param style
     *            the swt style of the {@link Font}
     */
    public static void setFont(Composite composite, int height, int style) {
        Font font = composite.getFont();
        FontDescriptor descriptor = FontDescriptor.createFrom(font).setHeight(14).setStyle(SWT.BOLD);

        ResourceManager resManager = new LocalResourceManager(JFaceResources.getResources(), composite);
        composite.setFont(resManager.createFont(descriptor));
    }

    /**
     * Colors the background of the given {@link Composite} with the {@link Color} specified by the
     * given id.
     * 
     * @param c
     *            the {@link Composite} to color
     * @param id
     *            the color constant
     */
    public static void colorCompositeBackground(Composite c, int id) {
        Display display = Display.getCurrent();
        c.setBackground(display.getSystemColor(id));
    }

    /**
     * Shows a reload dialog asking the user whether a modied resource that is also modified by
     * another party should be reloaded or not.
     * 
     * @param shell
     *            the parent {@link Shell}
     * @param views
     *            a string representation of the affected views
     * @param resourceURI
     *            the {@link URI} of the resource
     * @return {@code true} if the user presses the Yes button, false otherwise
     */
    public static boolean openReloadDialog(Shell shell, String views, URI resourceURI) {
        return MessageDialog.openQuestion(shell, "Warning",
                resourceURI + "\n\n" + "This model has been modified by another party. Do you want to"
                        + " reload it and lose the changes made in the views:   " + views + " ?");
    }

    /**
     * Shows a save dialog asking the user whether a modified resource that is also modified by
     * another party should be replaced or not.
     * 
     * @param shell
     *            the parent {@link Shell}
     * @param views
     *            a string representation of the affected views
     * @param resourceURI
     *            the {@link URI} of the resource
     * @return {@code true} if the user presses the Yes button, false otherwise
     */
    public static boolean openSaveDialog(Shell shell, String views, URI resourceURI) {
        return MessageDialog.openQuestion(shell, "Warning",
                resourceURI + "\n\n" + "This model has been modified by another party. Do you want to"
                        + " overwrite it with the version of this model made in the views:   " + views + " ?");
    }

    /**
     * Shows a save dialog asking the user whether the changes made by the views represented by the
     * given representation should be saved or not.
     * 
     * @param shell
     *            the parent {@link Shell}
     * @param views
     *            a string representation of the affected views
     * @param resourceURI
     *            the {@link URI} of the resource
     * @return a status code representing the users choice.
     * @see {@link ISaveablePart2#YES}, {@link ISaveablePart2#NO}, {@link ISaveablePart2#CANCEL}
     */
    public static int openSaveChangesDialog(Shell shell, String views, URI resourceURI) {
        String[] answer = new String[3];
        answer[ISaveablePart2.YES] = "Yes";
        answer[ISaveablePart2.NO] = "No";
        answer[ISaveablePart2.CANCEL] = "Cancel";

        MessageDialog dialog = new MessageDialog(shell, "Warning", null,
                resourceURI + "\n\n" + "This model has been modified by one of the views: " + views
                        + ". Do you want to save it? Otherwise unsaved modifications made in the named views are discarded!",
                MessageDialog.QUESTION, answer, 2);
        return dialog.open();
    }
}
