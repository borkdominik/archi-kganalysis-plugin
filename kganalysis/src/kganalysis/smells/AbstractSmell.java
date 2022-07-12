package kganalysis.smells;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import kganalysis.KGPlugin;


public abstract class AbstractSmell {
	
	protected GraphDatabaseService graphDb;
	
	public AbstractSmell() {
		this.graphDb = KGPlugin.INSTANCE.getGraphDb();
	}
	
	// returns the name of the smell
	public abstract String getLabel();
	
	// returns the detected elements as strings by running the smell query on the graphDb
    public abstract String[] detect();
    
    // returns the name of the node
    protected String getName(Node node) {
		String name = node.getProperty("name").toString();
		if (name != null) {
			return name;
		}
		return "";
	}
}
