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

<p align="center">
  <img src="./images/plugin-screenshot.png" width="100%">
</p>

<!-- ABOUT -->
## About the Project


The **EAKG Toolkit** for [Archi](https://www.archimatetool.com/) allows enterprise architects to perform advanced analysis on ArchiMate models by using **Enterprise Architecture Knowledge Graphs (EAKGs)** for additional visualization and representation methods. 

**Features:**
- *Transformation* of ArchiMate models into EAKGs, stored in an embedded [Neo4j Graph Database](https://neo4j.com/developer/graph-platform/neo4j) and making use of the powerful [Cypher Query Language](https://neo4j.com/developer/cypher/).
- *Visualization* of the created EAKGs with [neovis.js](https://github.com/neo4j-contrib/neovis.js), including filters, options and execution of custom queries for changing the representation.
- *EA Smell Detection* for detecting commonly considered bad practices in EA models. Currently features 8 different smells from the [EA Smells Catalogue](https://swc-public.pages.rwth-aachen.de/smells/ea-smells/) which can be individually visualized.
- *Graph Analysis* for highlighting centrality and community measures in the EAKG through graph algorithms, such as page rank or degree.

**Publications:**
- The method for constructing model-based Enterprise Architecture Knowledge Graphs is proposed in this [EDOC'22 paper](./papers/EDOC2022-Model_based_Construction_of_EAKG-Web.pdf).

<!-- INSTALLING -->
## Installation

Copy the downloaded `.jar` file into the `/dropins` folder of Archi.

Location of the `/dropins` folder:
- Windows: `<home>/AppData/Roaming/Archi4/dropins`
- Mac: `<home>/Library/Application Support/Archi4/dropins`
- Linux: `<home>/.archi4/dropins`

Restart Archi for the plugin to become active.



<!-- Usage -->
## Usage

After installing the plug-in, the *Knowledge Graph* menu becomes available within Archi. For transforming an ArchiMate model, choose the `Create Knowledge Graph` action. Wait for the creation process to finish and see the other actions in the menu become available (can initially take up to 2 minutes).

The EAKG toolkit offers two additional views that can be toggled from the menu:
- *Knowledge Graph Visualization*
- *EA Smells Report*

Furthermore, the toolkit includes actions for reloading a new model and to stop the graph database.



<!-- CONTRIBUTING --->
## Contributing

Contributions to the project are highly welcome! You can find additional instructions regarding development in the [EAKG Wiki](https://github.com/borkdominik/archi-kganalysis-plugin/wiki).



<!-- LICENSE -->
## License

The project is distributed under the MIT License.