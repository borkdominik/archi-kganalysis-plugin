package kganalysis.smells;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;


public class DeadComponentSmell extends AbstractSmell {

	private final String LABEL = "Dead Component";
	private final String QUERY = "MATCH (n) WHERE not (n)--() RETURN n";

	public DeadComponentSmell() {
		super();
	}

	@Override
	public String[] detect() {
		List<String> detected = new ArrayList<String>();

		try (Transaction tx = graphDb.beginTx(); Result result = tx.execute(QUERY)) {
			while (result.hasNext()) {
				Map<String, Object> row = result.next();
				Node n = (Node) row.get("n");
				n.addLabel(Label.label("Smell"));
				String element = n.getProperty("name").toString();
				detected.add("Element '" + element + "' is not connected to any other element.");
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
