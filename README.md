<h1 align="center">Archi KG Analaysis Plugin </h1>
<p align="center">
  <img src="./images/logo.png" width="20%">
</p>

<br>

> [Archi](https://www.archimatetool.com/) plugin to visualize and analyze Enterprise Architecture (EA) models as **Knowledge Graphs**, including the detection of **EA Smells**.


## Concept Draft

<img src="./images/kg-archi.png" width="100%">

### Knowledge Graph

<!-- TODO: Picture of Knowledge Graph View -->

Initiated through `View Knowledge Graph` action.

**Graph Visualization**

Archi model is automatically transformed and uploaded to a neo4j database (as .graphml). This requires to configure the database connection first (credentials/server/port). Neovis.js then visualizes the graph and adds additional graph algorithms.

**Graph Algorithms**

- Node size -> Centralities
- Color -> Community Detection
- Relationship Thickness -> Weight

### EA Smells (Additional View)

<!-- TODO: Picture of EA Smells View -->
  
Initiated through `Detect EA Smells` action.

Contains tabular list of detected EA smells. Individual, detected smells can be expanded:
- EA Smell Description
- Affected elements
- Action: Jump to smell in archi model/knowledge graph

### User Interface elements

<!-- TODO: Picture of Archi View (https://github.com/archimatetool/archi-modelrepository-plugin/wiki/Understand-the-Basics) -->

**Menu**

<img src="./images/kg-menu.png" width="70%">

"Knowledge Graph" menu entry:
- View Knowledge Graph
- View Knowledge Graph in external browser
- Detect EA Smells

**Toolbar** 

<img src="./images/kg-toolbar.png" width="70%">

- Knowledge Graph
- Detect EA Smells

**Views**

- Top-Right: Knowledge Graph View
- Bottom-Right EA Smells View

### Additional Features

Overview of additional features that will be included in the plugin (implementation in later stages).

**Export/Import**

New menu entry in `File -> Export`:
- Model to GraphML
- Model to RDF/OWL

**Preferences**

1. Plugin
   - Current Version
   - "Check for Update" button to update the plugin
2. Neo4j DB Configuration
   - username/password
   - neo4j db link (server:port)
   - database name
3. Smell Detection
   - Detect EA Smells when model gets saved automatically (implicit)
   - Detect EA Smells when executing respective Command (explicit)

**Graph Analysis**

Graph analysis/algorithm functionality (offered in the CM2KG platform).

Centralities:
- Degree
- Eigenvector
- Page Rank
- Article Rank
- Betweenes
- Approx. Betweenes
- Closeness

Community Detection:
- Louvain
- Modularity Optimization
- Label Propagation
- Local Clustering Coefficient


## Questions

> 3rd View (bottom-left)?  

"Database" View?

Shows active connection to database.  
If no connection: Configuration to connect to a neo4j db

> Prefered way to use CM2KG?

Import jar?

> What can be used from CM2KG

whole web interface?

---


<br>

## TODO

General:

- [x] Create Concept Draft
- [x] Add new icon
- [x] Analyse Archimate Extension options
  - [x] JavaSript in Views possible?
  - [x] Neo4j Integration
- [ ] Analyse CM2KG/eGEAA Platforms
  - [x] What can the platforms do
  - [ ] How does the Knowledge Graph look like
  - [ ] How to include platform in the plugin?

Implementation:

- [x] Toolbar entries
  - [x] Knowledge Graph
  - [x] Detect EA Smells
- [x] Menu entries
  - [x] View Knowledge Graph
  - [x] View Knowledge Graph in external browser
  - [x] Detect EA Smells
- [x] Knowledge Graph View (draft)
  - [x] Graph Visualization
- [x] Basic EA Smells View (draft)
  - [x] Table
- [ ] CM2KG 
  - [ ] Import/Connect CM2KG with plugin
  - [ ] Transform Archi model to GraphML
  - [ ] Visualize Neo4J DB

---
<br>

## Installing the plugin

Detailed instrcutions will follow, for now:

Export the package as a plugin in Eclipse and move the .jar file into the `dropins` folder of Archi. After restarting Archi, the plugin should be active. 

<br>

## Useful Resources

For useful notes see the `/notes` folder of the repository.

<!-- TODO: Link notes here for quick navigation -->

### Links

[ArchiMate Language Specification](https://pubs.opengroup.org/architecture/archimate31-doc/toc.html)  
[ArchiMate Concepts Overview](https://archimate.visual-paradigm.com/category/archimate-concepts/)  
[Archi User Guide](https://www.archimatetool.com/downloads/Archi%20User%20Guide.pdf)  
[Archi GitHub Repo](https://github.com/archimatetool/archi) ([Wiki](https://github.com/archimatetool/archi/wiki))

**Archi Plugins**

[ArchiContribs](http://archi-contribs.github.io/)  
[Specialization Plugin](https://github.com/archi-contribs/specialization-plugin) - Icons and labels can be replaced in Archi  
[Form Plugin](https://github.com/archi-contribs/form-plugin) - Allows to create forms to view and edit Archi models  
[Repository Plugin](https://github.com/archimatetool/archi-modelrepository-plugin) - Allows collaborative work on Archi (sharing and versioning)


**RCP**  
[RCP Page Eclipse Wiki](http://wiki.eclipse.org/Rich_Client_Platform)

### Publications
[Verification of ArchiMate Behavioral
Elements by Model Checking](https://link.springer.com/content/pdf/10.1007/978-3-319-24369-6_11.pdf) (Plugin for Archi)

<br>
