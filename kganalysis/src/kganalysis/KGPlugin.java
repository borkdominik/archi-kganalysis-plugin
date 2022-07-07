package kganalysis;

import java.io.File;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.neo4j.graphdb.GraphDatabaseService;
import org.osgi.framework.BundleContext;
import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.ui.services.ViewManager;

import kganalysis.db.KGDatabase;
import kganalysis.db.KGExporter;
import kganalysis.views.ISmellsView;


/**
 * Activator for the KGAnalysis Plugin to control the life-cycle,
 * extends {@link AbstractUIPlugin} for preferences, dialogs, images
 */
public class KGPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "kganalysis";
	public static KGPlugin INSTANCE;
	public static File KG_FOLDER = new File(ArchiPlugin.INSTANCE.getUserDataFolder(), "kg-analysis");
	private KGDatabase kgDatabase;
	private KGExporter exporter;
	
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		kgDatabase = new KGDatabase();
		exporter = new KGExporter();
		KG_FOLDER.mkdirs();
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		INSTANCE = null;
		ViewManager.hideViewPart(ISmellsView.ID);
		super.stop(context);
	}
	
	public static KGPlugin getDefault() {
		return INSTANCE;
	}

	public KGDatabase getKGDatabase() {
		return kgDatabase;
	}
	
	public GraphDatabaseService getGraphDb() {
		return kgDatabase.getGraphDb();
	}
	
	public KGExporter getExporter() {
		return exporter;
	}
	
	
	public boolean isGraphDbStarted() {
		if (kgDatabase != null && kgDatabase.isStarted()) {
			return true;
		}
		return false;
	}
	
	
}
