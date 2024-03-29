package kganalysis.smells;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;


public class SharedPersistencySmell extends AbstractSmell {
	private final String LABEL = "Shared Persistency";
	private final String QUERY = "MATCH (a)-[r:RELATIONSHIP]-(b) WHERE a.class='SystemSoftware' and (r.type='AssociationRelationship' or r.type='RealizationRelationship' or r.type='AssignmentRelationship')  with a, count(r) as cnt WHERE cnt > 1 return a, cnt";

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
				a.addLabel(Label.label("Smell"));
				long count = (long) row.get("cnt");
				
				detected.add("Element '" + getName(a) + "' is a shared persistency between " + count + " elements.");
			}
			tx.commit();
		}
		return detected.toArray(String[]::new);
	}
}