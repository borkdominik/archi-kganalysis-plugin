package kganalysis.smells;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;


public class StrictLayerViolationSmell extends AbstractSmell {

	private final String LABEL = "Strict Layer Violation";
	private final String QUERY = "MATCH (a)-[r:RELATIONSHIP]-(b) WHERE a.layer contains 'business' and b.layer contains 'technology' RETURN a, b";

	private enum RelTypes implements RelationshipType {
		STRICT_LAYER_VIOLATION
	}

	public StrictLayerViolationSmell() {
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
				a.addLabel(Label.label("Smell"));
				a.createRelationshipTo(b, RelTypes.STRICT_LAYER_VIOLATION);
				
				detected.add("'" + getName(a) + "' (business) " + " - '" + getName(b) + "' (technology)");
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
