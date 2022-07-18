package kganalysis.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Color;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.model.IActiveStructureElement;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IBehaviorElement;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IMotivationElement;
import com.archimatetool.model.IPassiveStructureElement;
import kganalysis.KGPlugin;


/**
 ** Transforms an ArchiMate model to a Knowledge Graph and stores it in the graph database.
 */
public class KGExporter {

	IArchimateModel model;
	GraphDatabaseService graphDb;

	public KGExporter() { 
		
	}
	
	public void export(IArchimateModel model) {
		this.model = model;
		this.graphDb = KGPlugin.INSTANCE.getGraphDb();
		
		// Use the model folders to retrieve and store the elements/relations
		model.getFolders().stream().forEach(e -> storeNodes(e));
		storeRelationships();
		
		// Run graph algorithms on all nodes
		KGDatabase db = KGPlugin.INSTANCE.getKGDatabase();
		db.setCentralities();
		db.setCommunities();
	}

	private void storeNodes(IFolder folder) {
		List<IArchimateConcept> concepts = getConcepts(folder);
		
		if (concepts.isEmpty()) {
			return;
		}
		
		// Create a new node and set the properties
		try (Transaction tx = graphDb.beginTx()) {
			for (IArchimateConcept concept : concepts) {
				if (concept instanceof IArchimateElement) {
					Node node = tx.createNode();
					node.setProperty("id", concept.getId());
					node.setProperty("class", concept.eClass().getName());
					node.setProperty("name", concept.getName());
					node.setProperty("documentation", concept.getDocumentation());
					node.setProperty("layer", folder.getType().toString());
					node.setProperty("aspect", getAspect((IArchimateElement) concept));
					Color color = ColorFactory.getDefaultFillColor(concept);
					node.setProperty("color", ColorFactory.convertColorToString(color));
				}
			}
			tx.commit();
		}
	}

	private void storeRelationships() {
		File folder = KGPlugin.KG_FOLDER;
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
	
	private String getAspect(IArchimateElement element) {
		if (element instanceof IActiveStructureElement)
			return "active structure";
		else if (element instanceof IBehaviorElement)
			return "behaviour";
		else if (element instanceof IPassiveStructureElement)
			return "passive structure";
		else if (element instanceof IMotivationElement)
			return "motivation";
		
		return "other";
	}

}
