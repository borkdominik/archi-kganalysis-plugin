package kganalysis.db;


/**
 ** Cypher Queries to set centrality and community properties of nodes by running various
 ** graph algorithms (provided by the neo4j GDS library).
 */
public class CypherQueries {

	private static final String CENTRALITY_GRAPH = "kgCentrality";
	private static final String DEGREE_GRAPH = "kgDegree";
	private static final String COMMUNITY_GRAPH = "kgCommunity";

	public static final String DROP_CENTRALITY_GRAPH = "CALL gds.graph.drop('" + CENTRALITY_GRAPH + "', false);";

	public static final String PROJECT_CENTRALITY_GRAPH = "CALL gds.graph.project(\n"
		+ " '" + CENTRALITY_GRAPH + "',\n"
		+ " '*',\n"
		+ " { relType: { type: '*', orientation: 'NATURAL' }}\n"
		+ ");";

	public static final String DROP_DEGREE_GRAPH = "CALL gds.graph.drop('" + DEGREE_GRAPH + "', false);";

	public static final String PROJECT_DEGREE_GRAPH = "CALL gds.graph.project(\n"
		+ " '" + DEGREE_GRAPH + "',\n"
		+ " '*',\n"
		+ " { relType: { type: '*', orientation: 'REVERSE' }}\n"
		+ ");";

	public static final String DROP_COMMUNITY_GRAPH = "CALL gds.graph.drop('" + COMMUNITY_GRAPH + "', false);";

	public static final String PROJECT_COMMUNITY_GRAPH = "CALL gds.graph.project(\n"
		+ " '" + COMMUNITY_GRAPH + "',\n"
		+ " '*',\n"
		+ " { relType: { type: '*', orientation: 'UNDIRECTED' }}\n"
		+ ");";
	
	// Centrality
	public static final String PAGERANK = "CALL gds.pageRank.write('" + CENTRALITY_GRAPH + "', {\n"
		+ " dampingFactor: 0.85, maxIterations: 20, writeProperty: 'pageRank'\n"
		+ "});";

	public static final String BETWEENNESS = "CALL gds.betweenness.write('" + CENTRALITY_GRAPH + "', {\n"
		+ " writeProperty: 'betweenness'\n"
		+ "});";

	public static final String DEGREE = "CALL gds.degree.write('" + DEGREE_GRAPH + "', {\n"
		+ " writeProperty: 'degree'\n"
		+ "});";
	
	
	// Community
	public static final String LOUVAIN = "CALL gds.louvain.write('" + COMMUNITY_GRAPH + "', {\n"
		+ " includeIntermediateCommunities: false, writeProperty: 'louvain'\n"
		+ "});";

	public static final String LABEL_PROPAGATION = "CALL gds.labelPropagation.write('" + COMMUNITY_GRAPH + "', {\n"
		+ " writeProperty: 'labelPropagation'\n"
		+ "});";
	
	public static final String WCC = "CALL gds.wcc.write('" + COMMUNITY_GRAPH + "', {\n"
		+ " writeProperty: 'wcc'\n"
		+ "});";
	
}
