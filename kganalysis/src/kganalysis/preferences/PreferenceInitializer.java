package kganalysis.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import kganalysis.KGPlugin;


/**
 ** Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer implements IPreferenceConstants {

	public void initializeDefaultPreferences() {
		IPreferenceStore store = KGPlugin.INSTANCE.getPreferenceStore();
		store.setDefault(PREF_DB_FOLDER, KGPlugin.KG_FOLDER.getAbsolutePath());
		store.setDefault(P_EXTERNAL_MODE, false);
		store.setDefault(P_SERVER_URL, "bolt://localhost:7687");
		store.setDefault(P_SERVER_USERNAME, "neo4j");
		store.setDefault(P_SERVER_PASSWORD, "neo4j");
	}

}
