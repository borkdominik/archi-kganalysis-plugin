package kganalysis;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.archimatetool.editor.Logger;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;

public enum SmellsProvider {
	
	INSTANCE;
	
	private List<EASmell> smells;
	
	
	private SmellsProvider() {
		//smells = new ArrayList<EASmell>();
		Gson gson = new GsonBuilder()
		        .excludeFieldsWithoutExposeAnnotation()
		        .create();
		
		try {
			// TODO: load json file more efficiently
			URL url = new URL("platform:/plugin/kganalysis/files/smells.json");
			File smellsFile = new File(KGAnalysisPlugin.KG_FOLDER, "smells.json");
			FileUtils.copyURLToFile(url, smellsFile);
			smells = gson.fromJson(new FileReader(smellsFile), new TypeToken<List<EASmell>>() {}.getType());  

			
		} catch (Exception e) {
			e.printStackTrace();
			Logger.logError(e.getMessage());
		}
	}
	
	public List<EASmell> getSmells() {
		return smells;
	}
	
	/*
	private void loadSmellsFromJSON() {
		Gson gson = new GsonBuilder()
		        .excludeFieldsWithoutExposeAnnotation()
		        .create();
		try {
			Bundle bundle = Platform.getBundle("com.archimatetool.help");
			URL url = FileLocator.resolve(bundle.getEntry(""));
			
			
			URL smellsUrl = new URL("platform:/plugin/kganalysis/files/smells/");
			URL url = FileLocator.find(smellsUrl);
			url = FileLocator.resolve(url);
			File smellsDir = new File(url.getPath());
	
			File[] files = smellsDir.listFiles();
			
			if (files != null) {
				for(File file : files) {
					File temp = new File(KGAnalysisPlugin.KG_FOLDER, "temp.json");
					FileUtils.copyF
					EASmell smell = gson.fromJson(new FileReader(file), EASmell.class);
					smells.add(smell);		
				}
			}
			
			
			
			// File file = new File(KGAnalysisPlugin.KG_FOLDER, "temp.json");
			
			
			
			

			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	
}
