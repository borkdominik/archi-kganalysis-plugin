package kganalysis.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import kganalysis.KGAnalysisPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer 
	implements IPreferenceConstants {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = KGAnalysisPlugin.INSTANCE.getPreferenceStore();
		store.setDefault(P_EXTERNAL_MODE, false);
		store.setDefault(P_SERVER_URL, "bolt://localhost:7687");
		store.setDefault(P_SERVER_USERNAME, "neo4j");
		store.setDefault(P_SERVER_PASSWORD, "neo4j");
	}

}
