package kganalysis.smells;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;

public class SharedPersistencySmell extends AbstractSmell {
	private final String LABEL = "Shared Persistency Service";
//	private final String QUERY = "MATCH (a)-[r]-(b)\n"
//			+ "WHERE a.class='SystemSoftware' and (r.type="
//			+ "'AssociationRelationship' or r.type="
//			+ "'RealizationRelationship' or r.type="
//			+ "'AssignmentRelationship')\n"
//			+ "with a, count(r) as cnt\n"
//			+ "MATCH (a)-[r1]-(b1)\n"
//			+ "where cnt>1 and (r1.type="
//			+ "'AssociationRelationsip' or r1.type="
//			+ "'RealizationRelationship' or r1.type="
//			+ "'AssignmentRelationship')\n"
//			+ "return a,cnt";
	
	private final String QUERY = "MATCH (a)-[r]-(b) WHERE a.class='SystemSoftware' and (r.type='AssociationRelationship' or r.type='RealizationRelationship' or r.type='AssignmentRelationship')  with a, count(r) as cnt where cnt > 1 return a,cnt";
	
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
                                
                                // store the node and count from the result
				Node a = (Node) row.get("a");
				long count = (long) row.get("cnt");
				
                                // add the "Smell" label to the node
				a.addLabel(Label.label("Smell"));
                                // format the string to be shown in the table
				String elementName = (a).getProperty("name").toString();
				detected.add("Element '"  + elementName + "' is accessed by " + count + " other services.");
			}
			tx.commit();
		}
		return detected.toArray(String[]::new);
	}
}