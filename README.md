<h1 align="center">Archi KG Analysis Plugin </h1>
<p align="center">
  <img src="./images/logo.png" width="20%">
</p>

<br>

> [Archi](https://www.archimatetool.com/) plugin to visualize and analyze Enterprise Architecture (EA) models as **Knowledge Graphs**, including the detection of **EA Smells**.

<br>


-- Screenshot/GIF --


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

## EA Smells



General info about EA Smells....

List of smells:
- Cyclic Dependency
- Dead Componentt


# TODO

## Release

High priority tasks to finish before the release:

- **General**
  - *PogressMonitor*
    - [X] Show progress
  - *Documentation*
    - [ ] ReadMe (screenshot, features, getting started)
    - [ ] Wiki (feature overview, ea smells list)
- **Menubar**
  - [X] Initialize
    - 1 - Export archi model as CSV
    - 2 - Copy index.html
    - 3 - Start neo4j database
    - 4 - Load CSV to create nodes and relationships
    - 5 - Set properties (score, community)
  - [ ] Start Graph Database
  - [ ] Stop Graph Database
  - [ ] Load active model
  - [X] **Open Visualization**
  - [X] **Open Visualiation in external Browser**
    - [ ] Disable java functions (report), which cant be called externally -> load adapted index.html 
  - [ ] **Open EA Smells Report**
- **Toolbar**
  - [ ] Add commands with icons
- **Visualization**
  - [X] Neovis 2.0
  - *UI*
    - [X] Changes (new layout, fix font-size)
    - [X] Fit content to view (no scrolling)
    - [X] General Tab (tools, options, cypher query)
    - [ ] EA Smells Tab (report, EA smells)
- *Report*
  - [ ] Smell Detection
    - [ ] Command in menubar, view, browser
    - [ ] Report -> fill table with detected elements 
    - [ ] More smells


### On-hold

Lower priority tasks to implement at a later stage:

- Clean Up
  - Remove either apoc-core or apoc-full .jar (currently both in /lib) to decrease file size
  - General Code Clean Up
  - Code comments
- Knowledge Graph
  - support importing properties.csv
- Preferences
  - Support remote database
  - Set user folder
  - Knowledge Graph options

<br>

