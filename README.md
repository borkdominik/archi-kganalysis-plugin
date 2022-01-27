<h1 align="center">Archi KG Analysis Plugin </h1>
<p align="center">
  <img src="./images/logo.png" width="20%">
</p>

<br>

> [Archi](https://www.archimatetool.com/) plugin to visualize and analyze Enterprise Architecture (EA) models as **Knowledge Graphs**, including the detection of **EA Smells**.

<br>

-- Screenshot/GIF --

## Overview

**Features:**
- Transform ArchiMate models to Knowledge Graphs
- Detect EA Smells
- Visualization 


## Getting Started


### Installing the plugin


Download the plugin `.jar` file and copy it into the `dropins` folder in the user directory of Archi. After a restart of Archi the plugin becomes active.

### Using the plugin



## EA Smells



General info about EA Smells....

List of smells:
- Cyclic Dependency
- Dead Componentt


## TODO

### Release

High priority tasks to finish before the release:

- *General*
  - [X] Improve ProgressMonitor
  - [ ] Documentation
    - [ ] ReadMe (screenshot, features, getting started, supported smells, ...)
- *Menubar*
  - [X] **Initialize** 
    - 1 - Export archi model as CSV
    - 2 - Copy index.html
    - 3 - Start neo4j database
    - 4 - Load CSV to create nodes and relationships
    - 5 - Set properties (score, community)
  - [X] **Open Visualization**
  - [X] **Open Visualiation in external Browser**
    - [ ] Disable java functions (graph algorithms), as they cant be called externally -> load adapted index.html 
  - [ ] **Open EA Smells Report**
- *Toolbar*
  - [ ] Add commmands with icons
- *Visualization*
  - [ ] UI
    - [X] Decrease font-size
    - [X] Fit content to view (no scrolling)
    - [ ] Revise layout
- *Report*
  - [X] Load smells from JSON file and fill table
    - [ ] Add more smells
    - [ ] Include cypher query
  - [ ] Smell Detection
    - [ ] Run all smell queries and set respective *detected* variable to true/false 
    - [ ] Show affected elements 


### On-hold

Lower priority tasks to implement at a later stage:

- Decrease plugin .jar file size
  - Remove either apoc-core or apoc-full .jar (currently both in /lib)
- Visualization
  - Use neovis 2.0
- Knowledge Graph
  - support importing properties.csv
- Preferences
  - Support remote database
  - Set user folder
  - Knowledge Graph options

<br>

