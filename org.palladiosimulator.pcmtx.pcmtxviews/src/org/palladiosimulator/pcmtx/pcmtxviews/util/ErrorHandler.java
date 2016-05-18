package org.palladiosimulator.pcmtx.pcmtxviews.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This class represents a handler for errors.
 * 
 * @author Tobias Weiberg
 */
public class ErrorHandler {

    /**
     * Opens an {@link ErrorDialog} that shows some information about the given {@link Throwable}.
     * 
     * @param t
     *            the {@link Throwable} representing the error.
     */
    public static void handleError(Throwable t) {
        List<Status> childStatuses = new ArrayList<>();
        StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();

        for (StackTraceElement stackTrace : stackTraces) {
            Status status = new Status(IStatus.ERROR, "org.palladiosimulator.pcmtx.pcmtxviews", stackTrace.toString());
            childStatuses.add(status);
        }

        MultiStatus ms = new MultiStatus("org.palladiosimulator.pcmtx.pcmtxviews", IStatus.ERROR,
                childStatuses.toArray(new Status[] {}), t.getMessage(), t);

        Shell shell = Display.getCurrent().getActiveShell();

        String title;
        switch (ms.getSeverity()) {
        case IStatus.WARNING:
            title = "Warning";
            break;
        case IStatus.INFO:
            title = "Information";
            break;
        default:
            title = "Error";
        }

        ErrorDialog.openError(shell, title, ms.getMessage(), ms);
    }

    /**
     * Opens an {@link MessageDialog} that displays the given message.
     * 
     * @param message
     *            the message to display.
     */
    public static void showInformationDialog(String message) {
        MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "Information", message);
    }

}
