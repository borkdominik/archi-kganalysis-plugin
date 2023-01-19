import NeoVis, { NeovisConfig, NEOVIS_ADVANCED_CONFIG } from 'neovis.js/dist/neovis';

export class NeoVisGraph {

    viz: NeoVis;
    //config: NeovisConfig;
    
    constructor() {
        const config: NeovisConfig = {
            containerId: "viz",
            neo4j: {
                serverUrl: "bolt://localhost:7687",
                serverUser: "neo4j",
                serverPassword: "password"
            },
            labels: {
                Movie: {
                    label: 'title',
                    value: "15"
                },
                Person: {
                    label: "name",
                    value: "10"
                }
            },
            relationships: {
                ACTED_IN: {
                    value: "3"
                }
            },
            initialCypher: "MATCH (n)-[r]->(m) RETURN n,r,m"
        }
        
        this.viz = new NeoVis(config);
        this.viz.render();
        console.log(this.viz);
    }
}