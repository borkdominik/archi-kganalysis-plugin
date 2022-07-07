package kganalysis.smells;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;


public class CyclicDependencySmell extends AbstractSmell {

	
	private final String LABEL = "Cyclic Dependency";
	private final String QUERY = "MATCH (a)-[r1:RELATIONSHIP]->(b)-[r2:RELATIONSHIP]->(c)-[:RELATIONSHIP]->(a) RETURN a, b, c";
	private enum RelTypes implements RelationshipType {
		CYCLIC_DEPENDENCY
	}
	

	public CyclicDependencySmell() {
		super();
	}
	
	
	@Override
	public String[] detect() {
		List<String> detected = new ArrayList<String>();
		
		try (Transaction tx = graphDb.beginTx(); Result result = tx.execute(QUERY)) {
			while (result.hasNext()) {
				Map<String, Object> row = result.next();
				Node a = (Node) row.get("a");
				Node b = (Node) row.get("b");
				Node c = (Node) row.get("c");

				a.createRelationshipTo(b, RelTypes.CYCLIC_DEPENDENCY);
				a.addLabel(Label.label("Smell"));

				String elementName = (a).getProperty("name").toString();
				String bName = (b).getProperty("name").toString();
				String cName = (c).getProperty("name").toString();
				
				detected.add("Element '"  + elementName + "' is part of a cycle. (" + elementName + " → " + bName + " → " + cName
						+ " → " + elementName + ")");
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