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


public class CyclicDependencySmell implements ISmell {
    
    private GraphDatabaseService graphdb;
    private final String LABEL = "Cyclic Dependency";
    private final String QUERY =  "MATCH (a)-[r1:RELATIONSHIP]->(b)-[r2:RELATIONSHIP]->(c)-[:RELATIONSHIP]->(a) RETURN a, b, c";
    private enum RelTypes implements RelationshipType {
        CYCLIC_DEPENDENCY
    }
    
    /*private final String QUERY =  "WITH collect(n) as nodes \n"
    	+ "CALL apoc.nodes.cycles(nodes, {relType: 'RELATIONSHIP'}) \n"
    	+ "YIELD path RETURN path";*/
    
    public CyclicDependencySmell() {
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
		Node c = (Node) row.get("c");
		
		a.createRelationshipTo(b, RelTypes.CYCLIC_DEPENDENCY);
		a.addLabel(Label.label("Smell"));
		
		String aName = (a).getProperty("name").toString();
		String aClass = (a).getProperty("class").toString();
		String bName = (b).getProperty("name").toString();
		String cName = (c).getProperty("name").toString();
		
		detected.add("'" + aName + "' (" + aClass + ") in cycle: '" + aName + " -> " + bName + " -> " + cName + " -> " +  aName + "'");
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