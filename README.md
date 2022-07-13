<h1 align="center">Archi KG Analysis Plugin </h1>
<p align="center">
  <img src="./images/logo.png" width="20%">
</p>

<br>

> [Archi](https://www.archimatetool.com/) plugin to visualize and analyze Enterprise Architecture (EA) models as **Knowledge Graphs**, including the detection of **EA Smells**.


<p align="center">
  <img src="./images/plugin-screenshot.png" width="100%">
</p>


## Features

- Backed by an embedded [neo4j graph database](https://neo4j.com/developer/graph-platform/neo4j) to gain further insights about EA models in Archi
- Transformation of ArchiMate models into a Knowledge Graph  
- EA Smell Detection including a complete report of the detected smells and information from the [EA Smell Catalogue](https://swc-public.pages.rwth-aachen.de/smells/ea-smells/).
- Visualization of the Knowledge Graph and the EA Smell Queries with [neovis.js](https://github.com/neo4j-contrib/neovis.js/). 
- Run arbitrary Cypher Queries on the Graph or customize the visualization by using different graph algorithms



## Getting Started


### Installing the plugin

Copy the downloaded .jar file into the `/dropins` folder of Archi.

Location of the `/dropins` folder:
- Windows: `<home>/AppData/Roaming/Archi4/dropins`
- Mac: `<home>/Library/Application Support/Archi4/dropins`
- Linux: `<home>/.archi4/dropins`

The plugin automatically becomes active upon restarting Archi.

### Using the plugin

From the menu choose the action: `Start database and initialize Knowledge Graph`. The process takes a while (~1-2 minutes) and once finished, the progress dialog disappears and the other actions from the menu become active.

Additional Hints: 
- Progress Dialog currently freezes the UI and does not indicate progress correctly.
- If the database is not started and the Smells Report view is open (e.g. from a previous session) the view shows an error message. Start the database and the view can be properly opened again. 
- Preference options are not implemented yet.


