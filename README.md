<h1 align="center">Archi KG Analysis Plugin </h1>
<p align="center">
  <img src="./images/logo.png" width="20%">
</p>

<br>

> [Archi](https://www.archimatetool.com/) plugin to visualize and analyze Enterprise Architecture (EA) models as **Knowledge Graphs**, including the detection of **EA Smells**.

<br>

## To Do

- Create list of available EA smells
  - import .json files
  - show list
    - browser (smells.html)
    - table? 


- Execute EA smells
  - Table with all 
  - Browser 
    - select smell to detect (more info in smells.html)
    - detect runs java function 
    - java function copies query to cypher textarea and runs query on embedded instance
    - show detected elements in table

- Browser
  - Add Toolbar
- index.html
  - Use neovis.js 2.0
  - Fix Styling

- Database
  - Initialize size, community
  - Use queries
- Use APOC core or full jar (not both)

### On-hold

- Fix ProgressMonitor

### Planned

- Import properties.csv



## Plugin Overview

**KGAnalysisPlugin** 


`extends AbstractUIPlugin` for:
- Preferences - support for storing, setting default preferences  
- Dialogs - store dialogs e.g. choices from wizards
- Images - image registry 




### Database

Elements stored as 
```json
{
  "identity": 46,
  "labels": [
    "elements"
  ],
  "properties": {
"name": "Accept",
"id": "572",
"class": "BusinessProcess",
"documentation": ""
  }
}
```


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
