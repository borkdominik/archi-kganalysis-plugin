package kganalysis.db;


public class CypherQueries {
    
    // --- Centrality -- 
    
    public static final String PAGERANK = "CALL gds.pageRank.write({\n" + " nodeProjection: '*',\n"
	    + " relationshipProjection: { relType: { type: '*', orientation: 'NATURAL', properties: {} }},\n"
	    + " relationshipWeightProperty: null, dampingFactor: 0.85, maxIterations: 20, writeProperty: 'pageRank'\n"
	    + "});";

    public static final String BETWEENNESS = "CALL gds.betweenness.write({\n" + " nodeProjection: '*',\n"
	    + " relationshipProjection: { relType: { type: '*', orientation: 'NATURAL', properties: {} }},\n"
	    + " writeProperty: 'betweenness'\n" + "});";

    public static final String DEGREE = "CALL gds.degree.write({\n" + " nodeProjection: '*',\n"
	    + " relationshipProjection: { relType: { type: '*', orientation: 'REVERSE', properties: {} }},\n"
	    + " relationshipWeightProperty: null, writeProperty: 'degree'\n" + "});";
    
    // --- Community ---
    
    public static final String LOUVAIN = "CALL gds.louvain.write({\n" + " nodeProjection: '*',\n"
	    + " relationshipProjection: { relType: { type: '*', orientation: 'UNDIRECTED', properties: {} }},\n"
	    + " relationshipWeightProperty: null, includeIntermediateCommunities: false, seedProperty: '', writeProperty: 'louvain'\n"
	    + "});";

    public static final String LABEL_PROPAGATION = "CALL gds.labelPropagation.write({\n" + " nodeProjection: '*',\n"
	    + " relationshipProjection: { relType: { type: '*', orientation: 'UNDIRECTED', properties: {} }},\n"
	    + " relationshipWeightProperty: null, writeProperty: 'labelPropagation'\n" + "});";
    
    public static final String WCC = "CALL gds.wcc.write({\n" + " nodeProjection: '*',\n"
	    + " relationshipProjection: { relType: { type: '*', orientation: 'UNDIRECTED', properties: {} }},\n"
	    + " writeProperty: 'wcc'\n" + "});";
    
}
