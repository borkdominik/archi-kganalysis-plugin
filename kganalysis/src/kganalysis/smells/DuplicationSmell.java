package kganalysis.smells;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import kganalysis.KGPlugin;


public class DuplicationSmell implements ISmell {

    private GraphDatabaseService graphdb;
    public final String LABEL = "Duplication";
    public final String QUERY =  "MATCH (a),(b) WHERE a<>b AND a.class = b.class AND apoc.text.jaroWinklerDistance(a.name, b.name)>0.9 "
    	+ "RETURN a, b, round(apoc.text.jaroWinklerDistance(a.name, b.name), 2) AS distance ORDER by distance DESC";
    private enum RelTypes implements RelationshipType {
        DUPLICATION
    }
    
    public DuplicationSmell() {
	this.graphdb = KGPlugin.INSTANCE.getGraphDb();
    }
    
    @Override
    public String[] detect() {
	List<String> detected = new ArrayList<String>();

	try (Transaction tx = graphdb.beginTx(); Result result = tx.execute(QUERY)) {
	    while (result.hasNext()) {
		Map<String, Object> row = result.next();
		Node a = (Node) row.get("a");
		Node b = (Node) row.get("b");
		double score = (double) row.get("distance");
		
		a.addLabel(Label.label("Smell"));
		
		Relationship duplication = a.createRelationshipTo(b, RelTypes.DUPLICATION);
		duplication.setProperty("weight", score);
		
		String aString = a.getProperty("name").toString();
		String bString = b.getProperty("name").toString();
		detected.add("'" + aString + "' and '" + bString + "' (" + score + ")");
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
