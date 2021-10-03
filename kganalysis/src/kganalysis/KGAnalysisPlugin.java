package kganalysis;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * Activator for the Knowledge Graph Analysis Plugin
 * 
 * @author philipp
 *
 */
public class KGAnalysisPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "kganalysis"; //$NON-NLS-1$

	// The shared instance
	public static KGAnalysisPlugin INSTANCE;
		
	public KGAnalysisPlugin() {
		INSTANCE = this;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		INSTANCE = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	*/
	public static KGAnalysisPlugin getDefault() {
		return INSTANCE;
	}

}
