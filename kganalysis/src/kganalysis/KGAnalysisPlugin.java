package kganalysis;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import org.eclipse.core.runtime.Path;

import org.eclipse.core.runtime.FileLocator;
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
	
	private static File fPluginFolder;
		
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

	
	public static KGAnalysisPlugin getDefault() {
		return INSTANCE;
	}
	
	
    public File getPluginFolder() {
        if(fPluginFolder == null) {
            URL url = getBundle().getEntry("/"); //$NON-NLS-1$
            try {
                url = FileLocator.resolve(url);
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
            fPluginFolder = new File(url.getPath());
        }
        
        return fPluginFolder;
    }
    
    public File getTemplatesFolder() {
    	URL url = FileLocator.find(getBundle(), new Path("$nl$/templates"), null); //$NON-NLS-1$
    	try {
            url = FileLocator.resolve(url);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        return new File(url.getPath()); 
    }
    

}
