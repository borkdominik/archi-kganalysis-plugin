package kganalysis.db;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.archimatetool.editor.ArchiPlugin;
import com.archimatetool.editor.Logger;

import apoc.create.Create;
import apoc.export.graphml.ExportGraphML;
import apoc.refactor.GraphRefactoring;
import kganalysis.EASmell;

import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.api.procedure.GlobalProcedures;
import org.neo4j.configuration.connectors.BoltConnector;
import org.neo4j.configuration.helpers.SocketAddress;
import org.neo4j.dbms.api.DatabaseManagementService;
import org.neo4j.dbms.api.DatabaseManagementServiceBuilder;
import org.neo4j.exceptions.KernelException;
import org.neo4j.gds.betweenness.BetweennessCentralityWriteProc;
import org.neo4j.gds.degree.DegreeCentralityWriteProc;
import org.neo4j.gds.labelpropagation.LabelPropagationWriteProc;
import org.neo4j.gds.louvain.LouvainWriteProc;
import org.neo4j.gds.pagerank.PageRankWriteProc;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Result;
import org.neo4j.io.fs.FileUtils;
import org.neo4j.kernel.internal.GraphDatabaseAPI;

import static java.util.Arrays.asList;
import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;
import static apoc.ApocConfig.APOC_IMPORT_FILE_ENABLED;
import static apoc.ApocConfig.apocConfig;
public class EmbeddedNeo4j {

	public static Path KG_FOLDER = new File(ArchiPlugin.INSTANCE.getUserDataFolder(), "kg-db").toPath();
	
	// provides access and API to manage neo4j database
	private DatabaseManagementService managementService;
	
	// for transactions on the graph database
	private GraphDatabaseService graphDb;

	
	public EmbeddedNeo4j() {
	
	}


	public void createDb() throws IOException {
		
		// clear existing folder
		FileUtils.deleteDirectory(KG_FOLDER);
		Logger.logInfo("Database directory: " + KG_FOLDER);
		
		// create new database with bolt connector (bolt://localhost:7687)
		managementService = new DatabaseManagementServiceBuilder( KG_FOLDER )
				.setConfig(BoltConnector.enabled, true)
				.setConfig(BoltConnector.listen_address, new SocketAddress("localhost", 7687)) 
				.build();
        graphDb = managementService.database(DEFAULT_DATABASE_NAME);
        
        registerShutdownHook(managementService);
        
        // Register Procedures
        apocConfig().setProperty(APOC_IMPORT_FILE_ENABLED, true);
        registerProcedures(graphDb, ExportGraphML.class, GraphRefactoring.class, Create.class, PageRankWriteProc.class,
        		BetweennessCentralityWriteProc.class, DegreeCentralityWriteProc.class, LabelPropagationWriteProc.class,
        		LouvainWriteProc.class);
	}

	
	public static void registerProcedures(GraphDatabaseService db, Class<?>... procedures) {
        GlobalProcedures globalProcedures = ((GraphDatabaseAPI) db).getDependencyResolver().resolveDependency(GlobalProcedures.class);
        for (Class<?> procedure : procedures) {
            try {
                globalProcedures.registerProcedure(procedure, true);
                globalProcedures.registerFunction(procedure, true);
                globalProcedures.registerAggregationFunction(procedure, true);
            } catch (KernelException e) {
                throw new RuntimeException("while registering " + procedure, e);
            }
        }
    }
	
	public void importCSVModel(File folder) {
		
		File elementsFile = new File(folder.getAbsolutePath(), "elements.csv");
		String elementsString = elementsFile.getAbsolutePath().replaceAll(" ", "%20");
		
		File relationsFile = new File(folder.getAbsolutePath(), "relations.csv");
		String relationsString = relationsFile.getAbsolutePath().replaceAll(" ", "%20");
		
		//String elementsFile = "file:///" + folder.getAbsolutePath() + "/elements.csv";
		//elementsFile = elementsFile.replaceAll(" ", "%20");
		//String relationsFile = "file:///" + folder.getPath() + "/relations.csv";
		//relationsFile = relationsFile.replaceAll(" ", "%20");
		
		// Load Elements from elements.csv
		try (Transaction tx = graphDb.beginTx()) {
			tx.execute( 
					"LOAD CSV WITH HEADERS FROM 'file://" + elementsString + "' AS line\n"
					+ " CREATE (:elements {class:line.Type, name:line.Name, documentation:line.Documentation, id:line.ID })"
					);
        	
        	tx.commit();
		}
		
		
		// Load Relations from relations.csv
		try (Transaction tx = graphDb.beginTx()) {
			tx.execute( 
					"LOAD CSV WITH HEADERS FROM 'file://" + relationsString + "' AS line\n"
					+ " MATCH (n {id:line.Source})\n"
					+ " WITH n, line\n" 
					+ " MATCH (m {id:line.Target})\n"
					+ " WITH n, m, line\n"
					+ " CREATE (n)-[:relationships {id:line.ID, class:line.Type, documentation:line.Documentation, name:line.Name}]->(m)"
					);
        	
			tx.commit();
		}
	}
	
	
	public void importModel() {
		// https://raw.githubusercontent.com/borkdominik/eGEAA/master/webapp/export/827b68d9-cff5-47d6-98d7-869c379bfe86.graphml
		// https://raw.githubusercontent.com/borkdominik/CM2KG/main/webapp/export/d8bcd4f1-8252-4fcb-bc27-810bd5c616b0.graphml
		// CYCLIC_DEPENDENCY, DENSE_STRUCTURE, MESSAGE_CHAIN
		try (Transaction tx = graphDb.beginTx()) {
			Result result = tx.execute( 
        			"CALL apoc.import.graphml('https://raw.githubusercontent.com/borkdominik/CM2KG/main/Experiments/EMF/Archi/ManyModels/repo-github-archimate/models/OpenGroup Format/transformed/11 domain events_transformed.graphml', {})");
        	
        	result.close();
		}
		try (Transaction tx = graphDb.beginTx()) {
			Result result = tx.execute( 
        			"MATCH (n)\r\n"
                			+ "CALL apoc.create.addLabels( id(n), [ n.ClassName ] )\r\n"
                			+ "YIELD node\r\n"
                			+ "RETURN node");
        	
        	result.close();
		}
		try (Transaction tx = graphDb.beginTx()) {
			Result result = tx.execute( 
					"MATCH (f)-[rel]->(b)\r\n"
							+ "CALL apoc.refactor.setType(rel, rel.Label)\r\n"
							+ "YIELD input, output\r\n"
							+ "RETURN input, output");
        	
        	result.close();
		}
	
	}
	
	
	public ArrayList<EASmell> cyclicDependency() {
		String name = "Cyclic Dependency";
		String description = " ";
		ArrayList<EASmell> detected = new ArrayList<>();
				
		try (Transaction tx = graphDb.beginTx(); 
			Result result = tx.execute("MATCH (a)-[r1]->(b)-[r2]->(c)-[]->(a) return DISTINCT a" ))
		{
			Iterator<Node> n_column = result.columnAs( "n" );
			n_column.forEachRemaining( node -> detected.add(new EASmell(name, description, node.getProperty( "Label" ).toString())));
			/*List<String> columns = result.columns();
			for(String object : columns) {
				detected.add(new EASmell(name, description, object));
			}*/
		}
		
		return detected;
	}
	
	// Old method trying to import a CSV
	public void importArchiModel() {
		// create elements
        try (Transaction tx = graphDb.beginTx()) {
        	
        	Result result = tx.execute( 
        			"LOAD CSV WITH HEADERS FROM 'file:///Users/philipp/Documents/workspace/kganalysis/files/elements.csv' AS line\n"
        			+ " CREATE (:Element {type:node.Type, name:node.Name, id:node.ID }) \n");
        	result.close();

        }
        
        try (Transaction tx = graphDb.beginTx()) {
        	Result result = tx.execute(
        			"LOAD CSV WITH HEADERS FROM 'file:///Users/philipp/Documents/workspace/kganalysis/files/relations.csv' AS r \n"
        			+ "MATCH (n {id:r.Source})\n"
        			+ " WITH n, r\n"
        			+ " MATCH (m {id:r.Target})\n"
        			+ " WITH n, m, r\n"
        			+ " CREATE (n)-[:Relationships {id:r.ID, type:r.Type, name:r.Name}]->(m)\n");
        	result.close();
        }
	}
	
	/**
	 * Delete all nodes and relationships 
	 */
	public void removeData() {
		try (Transaction tx = graphDb.beginTx()) {
			Result result = tx.execute( 
					"MATCH (n) \r\n"
							+ "DETACH DELETE n \r\n");
        	
        	result.close();
		}
	}

	public void shutDown() {
		managementService.shutdown();
	}

	private static void registerShutdownHook(final DatabaseManagementService managementService) {
		// Registers a shutdown hook for the Neo4j instance
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				managementService.shutdown();
			}
		});
	}



}
