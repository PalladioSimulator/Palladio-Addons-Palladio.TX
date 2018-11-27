package org.palladiosimulator.transactions;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

import edu.kit.ipd.sdq.eventsim.api.ISimulationConfiguration;

public abstract class AbstractEcoreModel<M> {

    protected M root;

    public AbstractEcoreModel(ISimulationConfiguration configuration, String configurationAttribute) {
        root = loadModel(configuration, configurationAttribute);
    }

    protected M loadModel(ISimulationConfiguration configuration, String configurationAttribute) {
        String modelLocation = (String) configuration.getConfigurationMap().get(configurationAttribute);
        ResourceSet rs = new ResourceSetImpl();
        Resource resource = rs.getResource(URI.createURI(modelLocation), true);
        return (M) resource.getContents().get(0);
    }

    public M getRoot() {
        return root;
    }

}
