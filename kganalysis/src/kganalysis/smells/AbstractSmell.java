package kganalysis.smells;

import org.neo4j.graphdb.GraphDatabaseService;
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
}
