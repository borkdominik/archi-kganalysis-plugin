package kganalysis.smells;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class ChattyServiceSmell extends AbstractSmell {

	private final String LABEL = "Chatty Service";
	private final String QUERY = "MATCH (a)-[r:RELATIONSHIP]-(b)\n"
			+ "WHERE a.class contains 'Service' and b.class contains 'Service' and not r.type contains 'Composition'\n"
			+ "WITH a, count(r) as cnt\n"
			+ "WHERE cnt > 3\n"
			+ "RETURN a, cnt";
	
	@Override
	public String getLabel() {
		return LABEL;
	}

	@Override
	public String[] detect() {
		List<String> detected = new ArrayList<String>();
		
		try (Transaction tx = graphDb.beginTx(); Result result = tx.execute(QUERY)) {
			while (result.hasNext()) {
				Map<String, Object> row = result.next();
				Node a = (Node) row.get("a");
				long count = (long) row.get("cnt");
				
				a.addLabel(Label.label("Smell"));
				
				detected.add("Element '"  + getName(a) + "' is connected to " + count + " other services.");
			}
			tx.commit();
		}
		return detected.toArray(String[]::new);
	}

}
