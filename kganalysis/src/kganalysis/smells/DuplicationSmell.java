package kganalysis.smells;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;


public class DuplicationSmell extends AbstractSmell {

	public final String LABEL = "Duplication";
	public final String QUERY = "MATCH (a),(b) WHERE a<>b AND a.class = b.class AND apoc.text.jaroWinklerDistance(a.name, b.name)>0.9 "
			+ "RETURN a, b, round(apoc.text.jaroWinklerDistance(a.name, b.name), 2) AS distance ORDER by distance DESC";

	private enum RelTypes implements RelationshipType {
		DUPLICATION
	}

	public DuplicationSmell() {
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
				double score = (double) row.get("distance");
				
				Relationship duplication = a.createRelationshipTo(b, RelTypes.DUPLICATION);
				duplication.setProperty("weight", score);

				detected.add("'" + getName(a) + "' and '" + getName(b) + "' (" + score + ")");
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
