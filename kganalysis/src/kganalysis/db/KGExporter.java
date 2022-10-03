package kganalysis.db;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Color;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import com.archimatetool.editor.ui.ColorFactory;
import com.archimatetool.model.FolderType;
import com.archimatetool.model.IAccessRelationship;
import com.archimatetool.model.IActiveStructureElement;
import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimateModel;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IAssignmentRelationship;
import com.archimatetool.model.IAssociationRelationship;
import com.archimatetool.model.IBehaviorElement;
import com.archimatetool.model.ICompositionRelationship;
import com.archimatetool.model.IDependendencyRelationship;
import com.archimatetool.model.IDynamicRelationship;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IInfluenceRelationship;
import com.archimatetool.model.IMotivationElement;
import com.archimatetool.model.IOtherRelationship;
import com.archimatetool.model.IPassiveStructureElement;
import com.archimatetool.model.IRealizationRelationship;
import com.archimatetool.model.IServingRelationship;
import com.archimatetool.model.ISpecializationRelationship;
import com.archimatetool.model.IStructuralRelationship;
import com.archimatetool.model.ITriggeringRelationship;
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
		storeRelationships2(model.getFolder(FolderType.RELATIONS));
		
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
	
	private void storeRelationships2(IFolder folder) {
		List<IArchimateConcept> concepts = getConcepts(folder);
		if (concepts.isEmpty()) {
			return;
		}
		
		for (IArchimateConcept concept : concepts) {
			if (concept instanceof IArchimateRelationship) {
				String relId = concept.getId();
				String doc = concept.getDocumentation();
				String name = concept.eClass().getName();
				String type = getType(((IArchimateRelationship) concept));
				String sourceId = ((IArchimateRelationship) concept).getSource().getId();
				String targetId = ((IArchimateRelationship) concept).getTarget().getId();
				if (sourceId.isEmpty() || targetId.isEmpty()) {
					continue;
				}
				
				try (Transaction tx = graphDb.beginTx()) {
					tx.execute("MATCH (n {id:'" + sourceId + "'}), (m {id:'" + targetId + "'}) WITH n, m\n" +
							"CREATE (n)-[:RELATIONSHIP {id:'" + relId + "', type:'" + type + "', documentation:'" + doc + "', name:'" + name + "'}]->(m)");
					tx.commit();
				}
			}
		}
		
	}
	
	// Old method to store relationships by using the CSV export. TODO: Remove this
	/*
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
	*/

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
	
	private String getType(IArchimateRelationship relationship) {
		if (relationship instanceof IAccessRelationship)
			return "AccessRelationship";
		else if (relationship instanceof IAssignmentRelationship)
			return "AssignmentRelationship";
		else if (relationship instanceof IAssociationRelationship)
			return "AssociationRelationship";
		else if (relationship instanceof ICompositionRelationship)
			return "CompositionRelationship";
		else if (relationship instanceof IDependendencyRelationship)
			return "DependencyRelationship";
		else if (relationship instanceof IDynamicRelationship)
			return "DynamicRelationship";
		else if (relationship instanceof IInfluenceRelationship)
			return "InfluenceRelationship";
		else if (relationship instanceof IOtherRelationship)
			return "OtherRelationship";
		else if (relationship instanceof IRealizationRelationship)
			return "RealizationRelationship";
		else if (relationship instanceof IServingRelationship)
			return "ServingRelationship";
		else if (relationship instanceof ISpecializationRelationship)
			return "SpecializationRelationship";
		else if (relationship instanceof IStructuralRelationship)
			return "StructuralRelationship";
		else if (relationship instanceof ITriggeringRelationship)
			return "TriggeringRelationship";
		
		return "Other";
	}

}
