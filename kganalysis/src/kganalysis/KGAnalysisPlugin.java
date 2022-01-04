package kganalysis;

import java.io.File;
import java.io.IOException;
import java.net.URL;


import org.eclipse.core.runtime.FileLocator;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * KG Analysis Plugin Activator 
 * 
 * Extends AbstractUIPlugin for preferences, dialogs, images
 *
 */
public class KGAnalysisPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "kganalysis";
	
	public static KGAnalysisPlugin INSTANCE;
	
	private static File pluginFolder;

		
	public KGAnalysisPlugin() {
		INSTANCE = this;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		INSTANCE = null;
		super.stop(context);
	}

	public static KGAnalysisPlugin getDefault() {
		return INSTANCE;
	}

	public File getPluginFolder() {
        if(pluginFolder == null) {
            URL url = getBundle().getEntry("/"); 
            try {
                url = FileLocator.resolve(url);
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
            pluginFolder = new File(url.getPath());
        }
        return pluginFolder;
    }
    
    
    

}
