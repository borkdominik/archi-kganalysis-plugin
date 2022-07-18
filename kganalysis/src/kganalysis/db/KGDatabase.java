package kganalysis.db;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import com.archimatetool.editor.ArchiPlugin;
import apoc.create.Create;
import apoc.nodes.Nodes;
import apoc.refactor.GraphRefactoring;
import apoc.text.Strings;
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
import org.neo4j.gds.wcc.WccWriteProc;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.io.fs.FileUtils;
import org.neo4j.kernel.internal.GraphDatabaseAPI;
import static org.neo4j.configuration.GraphDatabaseSettings.DEFAULT_DATABASE_NAME;
import static apoc.ApocConfig.APOC_IMPORT_FILE_ENABLED;
import static apoc.ApocConfig.apocConfig;


public class KGDatabase {
	
	// TODO: Set folder in Wizard
	public static Path KG_DATABASE_FOLDER = new File(ArchiPlugin.INSTANCE.getUserDataFolder(), "kg-db").toPath();
	private DatabaseManagementService managementService;
	private GraphDatabaseService graphDb;
	private boolean isStarted;

	public KGDatabase() {
		isStarted = false;
	}

	/**
	 ** Creates a new neo4j database with a bolt connector and registers procedures
	 ** 
	 ** TODO: Fix Exception Handling
	 */
	public void createDb() throws IOException {
		// clear db folder to avoid inconsistencies
		FileUtils.deleteDirectory(KG_DATABASE_FOLDER);
		
		// create the new database
		managementService = new DatabaseManagementServiceBuilder(KG_DATABASE_FOLDER)
				.setConfig(BoltConnector.enabled, true)
				.setConfig(BoltConnector.listen_address, new SocketAddress("localhost", 7687))
				.build();
		graphDb = managementService.database(DEFAULT_DATABASE_NAME);
		registerShutdownHook(managementService);

		apocConfig().setProperty(APOC_IMPORT_FILE_ENABLED, true);
		registerProcedures(graphDb, PageRankWriteProc.class, BetweennessCentralityWriteProc.class,
				DegreeCentralityWriteProc.class, LouvainWriteProc.class, LabelPropagationWriteProc.class,
				WccWriteProc.class, GraphRefactoring.class, Create.class, Strings.class, Nodes.class);
		isStarted = true;
	}
	
	/**
	 ** Runs various centrality graph algorithms { @see CypherQueries } and sets 'pageRank', 'betweenness', and 
	 ** 'degree' properties for each node. Used for the size of nodes in the visualization.
	 */
	public void setCentralities() {
		try (Transaction tx = graphDb.beginTx()) {
			tx.execute(CypherQueries.PAGERANK);
			tx.execute(CypherQueries.BETWEENNESS);
			tx.execute(CypherQueries.DEGREE);
			tx.commit();
		}
	}
	
	/**
	 ** Runs various community graph algorithms { @see CypherQueries } and sets 'louvain', 'labelPropagation', and 
	 ** 'wcc' properties for each node. Used for the color of nodes in the visualization.
	 */
	public void setCommunities() {
		try (Transaction tx = graphDb.beginTx()) {
			tx.execute(CypherQueries.LOUVAIN);
			tx.execute(CypherQueries.LABEL_PROPAGATION);
			tx.execute(CypherQueries.WCC);
			tx.commit();
		}
	}

	/**
	 ** Removes all nodes and their relationships from the database
	 */
	public void removeData() {
		try (Transaction tx = graphDb.beginTx()) {
			tx.execute("MATCH (n) DETACH DELETE n");
			tx.commit();
		}
	}

	/**
	 ** Manually shut down the database.
	 */
	public void shutDown() {
		managementService.shutdown();
		isStarted = false;
	}

	// Registers a shutdown hook to automatically shut down the database
	private static void registerShutdownHook(final DatabaseManagementService managementService) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				managementService.shutdown();
			}
		});
	}

	public static void registerProcedures(GraphDatabaseService db, Class<?>... procedures) {
		GlobalProcedures globalProcedures = ((GraphDatabaseAPI) db).getDependencyResolver()
				.resolveDependency(GlobalProcedures.class);
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

	public GraphDatabaseService getGraphDb() {
		return this.graphDb;
	}

	public boolean isStarted() {
		return isStarted;
	}

}
