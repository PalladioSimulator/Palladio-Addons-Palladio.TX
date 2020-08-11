package edu.kit.ipd.sdq.randomutils.internal;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.palladiosimulator.commons.stoex.api.StoExParser;

public class Activator extends Plugin {

    private static Activator instance;
    private ServiceReference<StoExParser> stoexParserServiceReference;
    private StoExParser stoexParser;

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        setInstance(this);

        stoexParserServiceReference = context.getServiceReference(StoExParser.class);
        stoexParser = context.getService(stoexParserServiceReference);
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        stoexParser = null;
        context.ungetService(stoexParserServiceReference);

        setInstance(null);
        super.stop(context);
    }

    public static Activator getInstance() {
        return instance;
    }

    private static void setInstance(Activator instance) {
        Activator.instance = instance;
    }

    public StoExParser getStoexParser() {
        return stoexParser;
    }
}
