# Meeting Notes

## 22-02-2022



## 17-02-2022

- **Transformation**
  - Create nodes with graph API (instead of load CSV) to set layer + aspect
  - Keep using CSV import for relationships
- **Visualization**
  - Set node colors to layer or aspect;
- **Report**
  - Shows list of detected elements (only string names so far)
- **Other**
  - Add confirm dialog to initialize command



## 09-02-2022

### High priority

- **General**
  - [X] Improve Progress Monitor
  - [ ] Documentation (ReadMe, screenshot, features, getting started, available smells)
- **Menubar**
  - [X] Start database and initialize Knowledge Graph
  - [ ] Stop Graph Database
  - [X] Reload with selected model
  - [X] **Open Knowledge Graph**
  - [X] **Open Knowledge Graph in external Browser**
    - [ ] Disable java functions (report), which cant be called externally -> load adapted index.html 
  - [X] **Open Smells Report**
- **Toolbar**
  - [ ] Add commands from menu
- **Visualization**
  - [X] Neovis 2.0
  - [X] New layout
  - [X] Fit content to view (no scrolling)
  - [X] General Tab (tools, options, cypher query)
  - [ ] EA Smells Tab (report, EA smells)
- **Report**
  - [X] TreeViewer
  - [ ] Smell Detection
  - [ ] Help Hint Provider (similar to Validation)
  - [ ] Select element in Diagram
- **Smells**
  - [X] Cyclic Dependency
  - [X] Dead Component
  - [X] Documentation
  - [X] Duplication

### Low priority

- Clean Up
  - Remove either apoc-core or apoc-full .jar (currently both in /lib) to decrease file size
  - General Code Clean Up
  - Add comments to code
- Knowledge Graph
  - Revise transformation (CM2KG instead of csv?)
  - support importing properties.csv
- Preferences
  - Support remote database
  - Set user folder
  - Knowledge Graph options



## 19-01-2022

### Current Progress

- [X] **ArchiMate Transformation** 
- [X] **Knowledge Graph UI**
  - [X] configurable Community/Score for Color/Size 
  - [X] Cypher Query
  - [x] EA Smell Detection 

### Next

- Show smells in Table
- Add more smells 
- Refresh/re-import Archi model
- Fix ProgressMonitor







## 14-12-2021

### Conference

Morgen Update

[Link to 1st Conference](https://ea-debts.org/news/first-student-conference-on-ea-debts-related-theses/)

[Link to 2nd Conference](https://ea-debts.org/news/2nd-ea-debts-student-conference/)


**Slides**

Structure

16:9 slides


### Plugin

**Database View** (bottom-left) unneccessary, since it makes the plugin more complex/harder to use

Embedded DB now has a **bolt connector** -> connect to e.g. `bolt://localhost:7687`  


**EA Smells**

[Catalogue/API](https://swc-public.pages.rwth-aachen.de/smells/ea-smells/ ) currently down

Listed some smells with corresponding cypher query in `EA_Smells.md` -> Realize a few of them in application for  the demo



**Transformation**


Steps:
1. Archi Model -> graphML (with CM2KG)
2. Import graphML into neo4j (with apoc.import)
3. Set node/relationship Labels (with apoc.create/apoc.refactor)




*Procedures* can now be easily registered:

```
registerProcedures(graphDb, ExportGraphML.class, GraphRefactoring.class, Create.class);
```

Procedures important for more advanced neo4j functions (import graphml, graph algorithms)







**User Interface**

<img src="../images/mockup.png" width="100%">










