package org.palladiosimulator.pcmtx.pcmtxviews.ui;

import org.eclipse.swt.custom.ScrolledComposite;

/**
 * This interface should be implemented by classes that create an user interface.
 * 
 * @author Tobias Weiberg
 *
 */
public interface UIProvider extends Disposable {

    /**
     * Updates the UI.
     */
    public void updateUI();

    /**
     * Clears and disables all UI elements.
     */
    public void clearUI();

    /**
     * Sets the enabled state of the UI to the given state.
     * 
     * @param enabled
     *            the new enabled state
     */
    public void setEnabled(boolean enabled);

    /**
     * Creates the UI with the given parent {@link ScrolledComposite}.
     * 
     * @param sc
     *            the parent {@link ScrolledComposite}
     */
    public void createUI(ScrolledComposite sc);

    /**
     * Sets the focus to an UI element of the provided UI.
     */
    public void setFocus();
}
