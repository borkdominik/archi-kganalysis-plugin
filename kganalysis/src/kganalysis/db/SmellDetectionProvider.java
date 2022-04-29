package kganalysis.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.viewers.ITreeContentProvider;

import kganalysis.KGAnalysisPlugin;
import kganalysis.smells.CyclicDependencySmell;
import kganalysis.smells.DeadComponentSmell;
import kganalysis.smells.DocumentationSmell;
import kganalysis.smells.DuplicationSmell;
import kganalysis.smells.ISmell;
import kganalysis.smells.StrictLayerViolationSmell;

public class SmellDetectionProvider implements ITreeContentProvider {

    private Map<String, String[]> contentMap = new HashMap<>();
    
    
    public SmellDetectionProvider() {
	if (KGAnalysisPlugin.INSTANCE.getExporter() != null) {
	    runSmellDetection();
	}
    }

    private void runSmellDetection() {
	List<ISmell> smells = new ArrayList<ISmell>();
	String[] elements;
	
	// add individual smell detectors
	smells.add(new CyclicDependencySmell());
	smells.add(new DeadComponentSmell());
	smells.add(new DocumentationSmell());
	smells.add(new DuplicationSmell());
	smells.add(new StrictLayerViolationSmell());
	
	// initialize the tree with smell names
	String[] smellNames = smells.stream()
		.map(ISmell::getLabel)
		.toArray(String[]::new);
	contentMap.put("smells", smellNames);
	
	// run the smell detection
	for (ISmell smell : smells) {
	    elements = smell.detect();
	    contentMap.put(smell.getLabel(), elements);
	}
    }
    
    @Override
    public Object[] getElements(Object inputElement) {
	return contentMap.get("smells");
    }

    @Override
    public Object[] getChildren(Object parentElement) {
	return contentMap.get(parentElement);
    }

    @Override
    public Object getParent(Object element) {
	return getParentOf(element);
    }

    @Override
    public boolean hasChildren(Object element) {
	return contentMap.containsKey(element);
    }

    private String getParentOf(Object element) {
	Set<String> keys = contentMap.keySet();
	for (String key : keys) {
	    String[] values = contentMap.get(key);
	    for (String val : values) {
		if (val.equals(element)) {
		    return key;
		}
	    }
	}
	return null;
    }
    
    /**
     * Loads all the smells with properties from the smells.json file using gson.
     * JSON provided by the EA Smells catalogue.
     * 
     * @see <a href="https://swc-public.pages.rwth-aachen.de/smells/ea-smells/">EA
     *      Smells catalogue</a>
     
    private void loadSmellsFromFile() {
	smells = new ArrayList<EASmell>();
	Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	try {
	    URL url = new URL("platform:/plugin/kganalysis/files/smells.json");
	    File smellsFile = new File(KGAnalysisPlugin.KG_FOLDER, "smells.json");
	    FileUtils.copyURLToFile(url, smellsFile);
	    JsonReader reader = new JsonReader(new FileReader(smellsFile));
	    smells = gson.fromJson(reader, new TypeToken<List<EASmell>>() {
	    }.getType());
	    reader.close();
	} catch (Exception e) {
	    e.printStackTrace();
	    Logger.logError(e.getMessage());
	}
    }
    */
    
    /*
    private String[] findDetectedElements(String query) {
	List<String> detected = new ArrayList<String>();

	GraphDatabaseService graphDb = KGAnalysisPlugin.INSTANCE.getGraphDb();
	try (Transaction tx = graphDb.beginTx(); Result result = tx.execute(query)) {
	    Iterator<Node> detectedNodes = result.columnAs("n");
	    detectedNodes.forEachRemaining(n -> {
		detected.add(n.getProperty("name").toString());
	    });
	    result.close();
	    tx.close();
	}
	return detected.toArray(String[]::new);
    }
    */
}
