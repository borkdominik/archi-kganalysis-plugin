package kganalysis.smells;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import kganalysis.KGPlugin;

public class DocumentationSmell implements ISmell {
    
    private GraphDatabaseService graphdb;
    private final String LABEL = "Documentation";
    private final String QUERY =  "MATCH (n) WHERE size(n.documentation)>256 RETURN n";
    
    
    public DocumentationSmell() {
	this.graphdb = KGPlugin.INSTANCE.getGraphDb();
    }
    
    @Override
    public String[] detect() {
	List<String> detected = new ArrayList<String>();

	try (Transaction tx = graphdb.beginTx(); Result result = tx.execute(QUERY)) {
	    while (result.hasNext()) {
		Map<String, Object> row = result.next();
		Node n = (Node) row.get("n");
		n.addLabel(Label.label("Smell"));
		String nameString = (n).getProperty("name").toString();
		String docString = (n).getProperty("documentation").toString();
		detected.add("'" + nameString + "' with documentation length '" + docString.length() + "'");
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
