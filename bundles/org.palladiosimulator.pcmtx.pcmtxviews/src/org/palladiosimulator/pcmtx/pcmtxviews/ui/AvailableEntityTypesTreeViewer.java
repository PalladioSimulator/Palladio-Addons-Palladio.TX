package org.palladiosimulator.pcmtx.pcmtxviews.ui;

import org.eclipse.emf.databinding.EMFProperties;
import org.eclipse.emf.databinding.IEMFListProperty;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.palladiosimulator.pcm.resourcetype.ResourceRepository;
import org.palladiosimulator.pcm.resourcetype.ResourcetypePackage;
import org.palladiosimulator.pcmtx.EntityType;
import org.palladiosimulator.pcmtx.pcmtxviews.manager.DataRepositoryManager;

/**
 * This class represents a {@link TreeViewer} viewing all available {@link EntityType}s in a set of
 * {@link Resource}s.
 * 
 * @author Tobias Weiberg
 *
 */
public class AvailableEntityTypesTreeViewer extends TreeViewer {
    private final DataRepositoryManager drManager;

    /**
     * Creates a new {@code AvailableEntityTypesTreeViewer} with the given parent-{@link Composite}
     * and the given style. The given {@link DataRepositoryManager} is used for retrieving all
     * available {@link EntityType}s contained in loaded {@link Resource}s.
     * 
     * @param parent
     *            the parent {@link Composite}
     * @param style
     *            the swt style
     * @param drManager
     *            the {@link DataRepositoryManager}
     */
    public AvailableEntityTypesTreeViewer(Composite parent, int style, DataRepositoryManager drManager) {
        super(parent, style);
        this.drManager = drManager;
        this.setContentProvider(new TreeContentProvider());

        this.setLabelProvider(new TreeLabelProvider());
        this.setInput(drManager);
    }

    /**
     * TreeContentProvider class for the TreeViewer.
     */
    private class TreeContentProvider implements ITreeContentProvider {

        @Override
        public void dispose() {
            // nothing to do here
        }

        @Override
        public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
            // nothing to do here
        }

        @Override
        public Object[] getElements(Object inputElement) {
            return getChildren(inputElement);
        }

        @Override
        public Object[] getChildren(Object parentElement) {
            if (parentElement instanceof DataRepositoryManager) {
                DataRepositoryManager manager = (DataRepositoryManager) parentElement;
                return manager.getLoadedResourceRepositories().toArray();
            } else if (parentElement instanceof ResourceRepository) {
                IEMFListProperty list = EMFProperties.list(
                        ResourcetypePackage.Literals.RESOURCE_REPOSITORY__AVAILABLE_RESOURCE_TYPES_RESOURCE_REPOSITORY);

                return (list.observe((ResourceRepository) parentElement)).toArray();
            }
            return null;
        }

        @Override
        public Object getParent(Object element) {
            if (element instanceof EntityType) {
                return ((EntityType) element).getResourceRepository_ResourceType();
            } else if (element instanceof ResourceRepository) {
                return drManager;
            }
            return null;
        }

        @Override
        public boolean hasChildren(Object element) {
            if (element instanceof DataRepositoryManager) {
                DataRepositoryManager manager = (DataRepositoryManager) element;
                return manager.getLoadedResourceRepositories().size() > 0;
            } else if (element instanceof ResourceRepository) {
                ResourceRepository repos = (ResourceRepository) element;
                return repos.getAvailableResourceTypes_ResourceRepository().size() > 0;
            }
            return false;
        }

    }

    /**
     * LabelProvider for the TreeViewer elements. Uses the entity name for {@link EntityType}s and
     * trimmed {@link Resource}-{@link URI}s for {@link ResourceRepository ResourceRepositories}
     */
    private static class TreeLabelProvider extends LabelProvider {
        @Override
        public String getText(Object element) {
            if (element instanceof ResourceRepository) {
                ResourceRepository repos = (ResourceRepository) element;
                String uriString = repos.eResource().getURI().toString();
                int index = uriString.lastIndexOf("/");
                int extensionIndex = uriString.lastIndexOf(".resourcetype");
                return uriString.substring(index + 1, extensionIndex);

            } else if (element instanceof EntityType) {
                return ((EntityType) element).getEntityName();
            }

            return null;
        }
    }
}
