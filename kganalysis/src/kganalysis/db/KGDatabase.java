package kganalysis.db;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;

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
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.io.fs.FileUtils;
import org.neo4j.kernel.internal.GraphDatabaseAPI;

import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;
import static apoc.ApocConfig.APOC_IMPORT_FILE_ENABLED;
import static apoc.ApocConfig.apocConfig;


public class KGDatabase {
	
	// folder to store database
	public static Path KG_DATABASE_FOLDER = new File(ArchiPlugin.INSTANCE.getUserDataFolder(), "kg-db").toPath();
	
	// provides access and API to manage neo4j database
	private DatabaseManagementService managementService;
	
	// for transactions on the graph database
	private GraphDatabaseService graphDb;
	
	private boolean isStarted;

	
	public KGDatabase() {
		isStarted = false;
	}

	/**
	 * Create a new neo4j database with a bolt connector (bolt://localhost:7687) and
	 * register procedures
	 * 
	 * @throws IOException
	 */
	public void createDb() throws IOException {
		
		// clear db folder
		FileUtils.deleteDirectory(KG_DATABASE_FOLDER);
		Logger.logInfo("Database directory: " + KG_DATABASE_FOLDER);
		
		managementService = new DatabaseManagementServiceBuilder( KG_DATABASE_FOLDER )
				.setConfig(BoltConnector.enabled, true)
				.setConfig(BoltConnector.listen_address, new SocketAddress("localhost", 7687)) 
				.build();
        graphDb = managementService.database(DEFAULT_DATABASE_NAME);
        
        registerShutdownHook(managementService);
        
        apocConfig().setProperty(APOC_IMPORT_FILE_ENABLED, true);
        registerProcedures(graphDb, ExportGraphML.class, GraphRefactoring.class, Create.class, PageRankWriteProc.class,
        		BetweennessCentralityWriteProc.class, DegreeCentralityWriteProc.class, LabelPropagationWriteProc.class,
        		LouvainWriteProc.class);
        isStarted = true;
	}
	
	/**
	 * Load CSV-exported Archi Model into the database
	 * 
	 * @param folder containing the exported CSV files
	 */
	public void importCSVModel(File folder) {
		
		// TODO: Check if CSV files are present in folder
		// TODO: Load properties
		
		File elementsFile = new File(folder.getAbsolutePath(), "elements.csv");
		String elementsString = elementsFile.getAbsolutePath().replaceAll(" ", "%20");
		
		File relationsFile = new File(folder.getAbsolutePath(), "relations.csv");
		String relationsString = relationsFile.getAbsolutePath().replaceAll(" ", "%20");
		
		// Load elements into the db from the elements.csv File
		try (Transaction tx = graphDb.beginTx()) {
			tx.execute("LOAD CSV WITH HEADERS FROM 'file://" + elementsString + "' AS line\n"
					+ " CREATE (:elements {class:line.Type, name:line.Name, documentation:line.Documentation, id:line.ID })");
        	
			tx.commit();
		}
		
		// Load relations into the db from the relations.csv File
		try (Transaction tx = graphDb.beginTx()) {
			tx.execute("LOAD CSV WITH HEADERS FROM 'file://" + relationsString + "' AS line\n"
					+ " MATCH (n {id:line.Source})\n"
					+ " WITH n, line\n" 
					+ " MATCH (m {id:line.Target})\n"
					+ " WITH n, m, line\n"
					+ " CREATE (n)-[:relationships {id:line.ID, class:line.Type, documentation:line.Documentation, name:line.Name}]->(m)");
        	
			tx.commit();
		}
		
		addNodeLabels();
		setPageRank();
		addCommunity();
	}
	
	public void setPageRank() {
		try (Transaction tx = graphDb.beginTx()) {
			tx.execute("CALL gds.pageRank.write({\n"
					+ " nodeProjection: '*',\n"
					+ " relationshipProjection: { relType: { type: '*', orientation: 'NATURAL', properties: {} }},\n"
					+ " relationshipWeightProperty: null, dampingFactor: 0.85, maxIterations: 20, writeProperty: 'score'\n"
					+ "});");
        	
			tx.commit();
		}
	}
	
	public void setDegree() {
		try (Transaction tx = graphDb.beginTx()) {
			tx.execute("CALL gds.degree.write({\n"
					+ " nodeProjection: '*',\n"
					+ " relationshipProjection: { relType: { type: '*', orientation: 'REVERSE', properties: {} }},\n"
					+ " relationshipWeightProperty: null, writeProperty: 'score'\n"
					+ "});");
        	
			tx.commit();
		}
	}
	
	public void addCommunity() {
		try (Transaction tx = graphDb.beginTx()) {
			tx.execute("CALL gds.louvain.write({\n"
					+ " nodeProjection: '*',\n"
					+ " relationshipProjection: { relType: { type: '*', orientation: 'UNDIRECTED', properties: {} }},\n"
					+ " relationshipWeightProperty: null, includeIntermediateCommunities: false, seedProperty: '', writeProperty: 'community'\n"
					+ "});");
        	
			tx.commit();
		}
	}
	
	public void addNodeLabels() {

		try (Transaction tx = graphDb.beginTx()) {
			tx.execute("MATCH (n:elements)\n"
					+ " CALL apoc.create.addLabels(n, [ n.class ]) YIELD node\n"
					+ " RETURN node\n");
        	
			tx.commit();
		}
	}
	
	@Deprecated
	public void importModel() {
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
	
	/**
	 * Remove all elements from the database
	 */
	public void removeData() {
		try (Transaction tx = graphDb.beginTx()) {
			tx.execute("MATCH (n)\n"
					+ " DETACH DELETE n");
			tx.commit();
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

	public boolean isStarted() {
		return isStarted;
	}

}
