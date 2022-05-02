package kganalysis.smells;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import kganalysis.KGPlugin;

public class StrictLayerViolationSmell implements ISmell {
    
    private final String QUERY =  "MATCH (a)-[r:RELATIONSHIP]-(b) WHERE a.layer contains 'business' and b.layer contains 'technology' RETURN a, b";
    GraphDatabaseService graphDb; 
    public final String LABEL = "Strict Layer Violation";
    
    private enum RelTypes implements RelationshipType
    {
        STRICT_LAYER_VIOLATION
    }
    
    public StrictLayerViolationSmell() {
	this.graphDb = KGPlugin.INSTANCE.getGraphDb();
    }
    
    @Override
    public String[] detect() {
	List<String> detected = new ArrayList<String>();
	
	try (Transaction tx = graphDb.beginTx(); Result result = tx.execute(QUERY)) {
	    while( result.hasNext()) {
		Map<String, Object> row = result.next();
		Node a = (Node) row.get("a");
		Node b = (Node) row.get("b");
		
		a.createRelationshipTo(b, RelTypes.STRICT_LAYER_VIOLATION);
		a.addLabel(Label.label("Smell"));
		
		String aString = (a).getProperty("name").toString();
		String bString = (b).getProperty("name").toString();
		detected.add("'" + aString + "' (business) " + " - '" + bString + "' (technology)");
	    }
	    tx.commit();
	}
	return detected.toArray(String[]::new);
    }


    @Override
    public String getLabel() {
	return LABEL;
    }
    
     
}
