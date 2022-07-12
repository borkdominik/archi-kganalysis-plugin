package kganalysis.smells;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class MessageChainSmell extends AbstractSmell {

	public final String LABEL = "Message Chain";
	public final String QUERY = "MATCH (a)-[:RELATIONSHIP]->(b)-[:RELATIONSHIP]->(c)-[:RELATIONSHIP]->(d)-[:RELATIONSHIP]->(e)\n"
			+ "WHERE a.class contains 'Service' \n"
			+ "and b.class contains 'Service' \n"
			+ "and c.class contains 'Service' \n"
			+ "and d.class contains 'Service' \n"
			+ "and e.class contains 'Service' \n"
			+ "return a, b, c, d, e";
	
	
	@Override
	public String getLabel() {
		return LABEL;
	}

	@Override
	public String[] detect() {
		List<String> detected = new ArrayList<String>();

		// start a new transaction, execute the query and iterate over the result
		try (Transaction tx = graphDb.beginTx(); Result result = tx.execute(QUERY)) {
			while (result.hasNext()) {
				Map<String, Object> row = result.next();
				Node a = (Node) row.get("a");
				Node b = (Node) row.get("b");
				Node c = (Node) row.get("c");
				Node d = (Node) row.get("d");
				Node e = (Node) row.get("e");
				
				a.addLabel(Label.label("Smell"));
				
				detected.add("Element '" + getName(a) + "' is part of a message chain: " + getName(a)
						+ " → " + getName(b) + " → " + getName(c) + " → " + getName(d) + " → " + getName(e));
			}
			tx.commit();
		}
		return detected.toArray(String[]::new);
	}

}
