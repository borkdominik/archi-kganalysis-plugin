<div align="center">
  <a href="https://github.com/borkdominik/archi-kganalysis-plugin">
    <img src="./images/logo.png" alt="Logo" width="80" height="80">
  </a>
  <h3 align="center">EAKG Toolkit for Archi</h3>
  <p align="center">
    Plugin for <a href="https://www.archimatetool.com/">Archi</a> to visualize and analyze Enterprise Architecture models through Knowledges Graphs
    <br><br>
    <a href="#about-the-project">About the Project</a> •
    <a href="#installation">Installation</a> •
    <a href="#usage">Usage</a> •
    <a href="#contributing">Contributing</a> •
    <a href="#license">License</a>
    <br><br>
  </p>
</div>



<!-- ABOUT -->
## About the Project

<p align="center">
  <img src="./images/plugin-screenshot.png" width="100%">
</p>

The EAKG toolkit allows enterprise architects to make use of advanced visualization and analysis on ArchiMate models through an **Enterprise Architecture Knowledge Graph (EAKG)**. Main features of the plug-in include:

- Transformation of an ArchiMate model to a Knowledge Graphs that is stored in an embedded [Neo4j Graph Database](https://neo4j.com/developer/graph-platform/neo4j). 
- Visualization of the Knowledge Graph with additional filters and options for different representations
- Detection of [EA Smells](https://swc-public.pages.rwth-aachen.de/smells/ea-smells/) through powerful [Cypher Queries](https://neo4j.com/developer/cypher/) which can be individually visualized.
- Graph analysis with centrality and community algorithms, e.g., Page Rank, Degree, or Louvain. 


<!-- INSTALLING -->
## Installation

Copy the downloaded .jar file into the `/dropins` folder of Archi.

Location of the `/dropins` folder:
- Windows: `<home>/AppData/Roaming/Archi4/dropins`
- Mac: `<home>/Library/Application Support/Archi4/dropins`
- Linux: `<home>/.archi4/dropins`

The plug-in automatically becomes active upon restarting Archi.



<!-- Usage -->
## Usage

Once the plug-in is installed the *Knowledge Graph* menu becomes available within Archi. Choose the *Create Knowledge Graph* action to transform an existing Archi model to a Knowledge Graph, stored in an embedded neo4j graph database. The process might take a while (up to 2 minutes, subsequent uses are faster) and once finished, the progress dialog disappears and the other actions from the menu become active. 

The EAKG toolkit currently offers two additional views that can be toggled from the menu:
- *Knowledge Graph Visualization*
- *EA Smells Report*

Furthermore, the toolkit includes actions to reload a new model and to stop the graph database.



<!-- CONTRIBUTING --->
## Contributing

Contributions to the project are highly welcome! Please see our [Contribution Guidelines](https://github.com/borkdominik/archi-kganalysis-plugin/wiki) for details on how to contribute to the project. Additional instructions regarding development are available in the [Wiki](https://github.com/borkdominik/archi-kganalysis-plugin/wiki).



<!-- LICENSE -->
## License

The project is distributed under the MIT License.