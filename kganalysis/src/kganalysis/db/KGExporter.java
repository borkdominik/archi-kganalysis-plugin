package kganalysis.db;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Color;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IActiveStructureElement;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IBehaviorElement;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IMotivationElement;
import com.archimatetool.model.IPassiveStructureElement;

import kganalysis.KGPlugin;

public class KGExporter {

    IArchimateModel model;
    GraphDatabaseService graphDb;
    HashMap<String, IArchimateElement> nodes;

    public KGExporter() {

    }

    public void export(IArchimateModel model) {
	this.model = model;
	this.graphDb = KGPlugin.INSTANCE.getGraphDb();
	nodes = new HashMap<>();

	exportElements(model.getFolder(FolderType.BUSINESS));
	exportElements(model.getFolder(FolderType.APPLICATION));
	exportElements(model.getFolder(FolderType.TECHNOLOGY));
	exportElements(model.getFolder(FolderType.STRATEGY));
	exportElements(model.getFolder(FolderType.IMPLEMENTATION_MIGRATION));
	exportElements(model.getFolder(FolderType.MOTIVATION));
	exportElements(model.getFolder(FolderType.OTHER));
	exportRelationships(KGPlugin.KG_FOLDER);
	
	KGDatabase db = KGPlugin.INSTANCE.getKGDatabase();
	// db.addNodeLabels();
	// db.addRelationshipLabels();
	db.setCentralities();
	db.setCommunities();
	KGPlugin.INSTANCE.setSmellProvider(new SmellDetectionProvider());
    }

    private void exportElements(IFolder folder) {
	List<IArchimateConcept> concepts = getConcepts(folder);
	if (concepts.isEmpty()) {
	    return;
	}
	String layer = folder.getType().toString();

	try (Transaction tx = graphDb.beginTx()) {
	    for (IArchimateConcept concept : concepts) {
		if (concept instanceof IArchimateElement) {
		    String id = concept.getId();
		    String className = concept.eClass().getName();
		    String name = concept.getName();
		    String documentation = concept.getDocumentation();
		    String aspect;
		    Color color = ColorFactory.getDefaultFillColor(concept);
		    if (concept instanceof IActiveStructureElement)
			aspect = "active structure";
		    else if (concept instanceof IBehaviorElement)
			aspect = "behaviour";
		    else if (concept instanceof IPassiveStructureElement)
			aspect = "passive structure";
		    else if (concept instanceof IMotivationElement)
			aspect = "motivation";
		    else
			aspect = "other";

		    Node node = tx.createNode();
		    
		    // Label.label("elements")
		    node.setProperty("id", id);
		    node.setProperty("class", className);
		    node.setProperty("name", name);
		    node.setProperty("documentation", documentation);
		    node.setProperty("layer", layer);
		    node.setProperty("aspect", aspect);
		    node.setProperty("color", ColorFactory.convertColorToString(color));
		    //nodes.put(id, (IArchimateElement) concept);
		}
	    }
	    tx.commit();
	}
    }

    private void exportRelationships(File folder) {
	File relationsFile = new File(folder.getAbsolutePath(), "relations.csv");
	String relationsString = relationsFile.getAbsolutePath().replaceAll(" ", "%20");
	String filePrefix = "file://";
	if (PlatformUtils.isWindows()) {
	    filePrefix = "file:///";
	}
		
	// TODO: Don't use CSV export?
	try (Transaction tx = graphDb.beginTx()) {
	    tx.execute("LOAD CSV WITH HEADERS FROM '" + filePrefix + relationsString + "' AS line\n"
		    + " MATCH (n {id:line.Source})\n" + " WITH n, line\n" + " MATCH (m {id:line.Target})\n"
		    + " WITH n, m, line\n"
		    + " CREATE (n)-[:RELATIONSHIP {id:line.ID, type:line.Type, documentation:line.Documentation, name:line.Name}]->(m)");
	    tx.commit();
	}

    }

    // Adapted from {@link CSVExporter} to return the concepts (elements) of a folder
    private List<IArchimateConcept> getConcepts(IFolder folder) {
	List<IArchimateConcept> concepts = new ArrayList<IArchimateConcept>();

	if (folder == null) {
	    return concepts;
	}

	for (EObject object : folder.getElements()) {
	    if (object instanceof IArchimateConcept) {
		concepts.add((IArchimateConcept) object);
	    }
	}
	// also add subfolders
	for (IFolder f : folder.getFolders()) {
	    concepts.addAll(getConcepts(f));
	}

	return concepts;
    }

    public HashMap<String, IArchimateElement> getNodes() {
	return nodes;
    }

}
