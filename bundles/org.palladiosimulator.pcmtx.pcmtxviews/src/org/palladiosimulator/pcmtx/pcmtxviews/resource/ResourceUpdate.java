package org.palladiosimulator.pcmtx.pcmtxviews.resource;

import org.eclipse.emf.common.util.URI;

/**
 * This class represents an update of an {@link IResource}. It contains information about the kind
 * of the update and the affected {@link IResource}.
 * 
 * @author Tobias Weiberg
 *
 */
public class ResourceUpdate {

    /**
     * The kind of a {@code ResourceUpdate}. {@code MODIFIED} if the {@link IResource} is modified,
     * {@code DELETED} if the {@link IResource} is deleted.
     */
    public enum UpdateKind {
        MODIFIED, DELETED
    };

    private final URI uri;
    private final UpdateKind kind;

    /**
     * Creates a new {@code ResourceUpdate}. The given {@link URI} specifies the {@link iResource}
     * affected by this update and the given {@link UpdateKind} specifies the kind of the
     * {@code ResourceUpdate}.
     * 
     * @param uri
     *            the {@link URI} of the updated {@link IResource}
     * @param kind
     *            the {@link UpdateKind} of the {@code ResourceUpdate}
     */
    public ResourceUpdate(URI uri, UpdateKind kind) {
        this.uri = uri;
        this.kind = kind;
    }

    /**
     * Returns {@code true} if this {@code ResourceUpdate} affects a {@link IResource} containing a
     * Resourcetype model.
     * 
     * @return {@code true} if this {@code ResourceUpdate} affects a {@link IResource} containing a
     *         Resourcetype model, {@code false} otherwise.
     */
    public boolean isResourcetypeResourceUpdate() {
        return uri.toString().endsWith(ResourceManager.RESOURCETYPE_FILE_EXTENSION);
    }

    /**
     * Returns {@code true} if this {@code ResourceUpdate} affects a {@link IResource} containing a
     * Pcmtx model.
     * 
     * @return {@code true} if this {@code ResourceUpdate} affects a {@link IResource} containing a
     *         Pcmtx model, {@code false} otherwise.
     */
    public boolean isPcmtxResourceUpdate() {
        return uri.toString().endsWith(ResourceManager.PCMTX_FILE_EXTENSION);
    }

    /**
     * Returns the {@link UpdateKind} of this {@code ResourceUpdate}.
     * 
     * @return the {@link UpdateKind} of this {@code ResourceUpdate}
     * @see UpdateKind
     */
    public UpdateKind getKind() {
        return kind;
    }

    /**
     * Returns the {@link URI} of the {@link IResource} affected by this {@code ResourceUpdate}.
     * 
     * @return the {@link URI} of the {@link IResource} affected by this {@code ResourceUpdate}
     */
    public URI getURI() {
        return uri;
    }

}
