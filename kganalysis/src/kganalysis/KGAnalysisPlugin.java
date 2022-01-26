package kganalysis;

import java.io.File;
import java.io.IOException;
import java.net.URL;


import org.eclipse.core.runtime.FileLocator;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.archimatetool.editor.ArchiPlugin;

import kganalysis.db.KGDatabase;


/**
 * Activator for the KGAnalysis Plugin controlling the life-cycle,
 * extends AbstractUIPlugin for preferences, dialogs, images
 */
public class KGAnalysisPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "kganalysis";
	public static KGAnalysisPlugin INSTANCE;
	public static File KG_FOLDER = new File(ArchiPlugin.INSTANCE.getUserDataFolder(), "kg-analysis");

	private KGDatabase kgDatabase;
	private static File pluginFolder;

		
	public KGAnalysisPlugin() {
		INSTANCE = this;
	}
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		setKGDatabase(new KGDatabase());
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

	public KGDatabase getKGDatabase() {
		return kgDatabase;
	}

	public void setKGDatabase(KGDatabase kgDatabase) {
		this.kgDatabase = kgDatabase;
	}

    
    
    

}
