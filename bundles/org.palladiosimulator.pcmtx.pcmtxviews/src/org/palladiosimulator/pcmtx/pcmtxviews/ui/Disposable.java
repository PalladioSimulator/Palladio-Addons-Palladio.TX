package org.palladiosimulator.pcmtx.pcmtxviews.ui;

/**
 * This interface should be implemented by UI classes.
 * 
 * @author Tobias Weiberg
 *
 */
public interface Disposable {

    /**
     * Disposes the {@code Disposable}. Can be used to dispose SWT objects and to remove listeners.
     */
    public void dispose();

}
