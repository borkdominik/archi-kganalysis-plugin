package kganalysis.smells;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;


public class DuplicationSmell extends AbstractSmell {

	public final String LABEL = "Duplication";
	public final String QUERY = "MATCH (a),(b) "
			+ "WHERE id(a) < id(b) AND a.class = b.class "
			+ "AND a.name IS NOT NULL AND b.name IS NOT NULL "
			+ "AND trim(a.name) <> '' AND trim(b.name) <> '' "
			+ "WITH a, b, apoc.text.jaroWinklerDistance(toLower(trim(a.name)), toLower(trim(b.name))) AS distance "
			+ "WHERE distance < 0.1 "
			+ "MERGE (a)-[dup:DUPLICATION]->(b) "
			+ "SET dup.weight = round(1.0 - distance, 2), a:Smell, b:Smell "
			+ "RETURN a, b, dup.weight AS similarity ORDER BY similarity DESC";

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
				double score = ((Number) row.get("similarity")).doubleValue();

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
