# archi-kganalysis-plugin

>[Archi](https://www.archimatetool.com/) plugin that uses Knowledge Graphs to detect Enterprise Architecture Smells.

## Notes

### Archi and ArchiMate
[ArchiMate Language Specification](https://pubs.opengroup.org/architecture/archimate31-doc/toc.html)  
[Archi User Guide](https://www.archimatetool.com/downloads/Archi%20User%20Guide.pdf)  
[Archi GitHub Repo](https://github.com/archimatetool/archi) ([Wiki](https://github.com/archimatetool/archi/wiki))

**Archi Plugins**  
[ArchiContribs](http://archi-contribs.github.io/)  
[Specialization Plugin](https://github.com/archi-contribs/specialization-plugin) - Icons and labels can be replaced in Archi  
[Form Plugin](https://github.com/archi-contribs/form-plugin) - Allows to create forms to view and edit Archi models  
[Repository Plugin](https://github.com/archimatetool/archi-modelrepository-plugin) - Allows collaborative work on Archi (sharing and versioning)

Archi Concepts and Colors:
<img src="https://www.archimatetool.com/wp-content/uploads/2018/11/ArchiMate-3.0-Notation-Overview-ArchiMate-standard-default-color-scheme.png">

### EA Smells

[Knowledge Base for EA Smells](https://swc-public.pages.rwth-aachen.de/smells/ea-smells/)


### Papers
[Verification of ArchiMate Behavioral
Elements by Model Checking](https://link.springer.com/content/pdf/10.1007/978-3-319-24369-6_11.pdf) (Plugin for Archi)

### Eclipse RCP

Archi is developed based on the Eclipse Rich Client Platform (RCP).  
➜ [RCP Page containing Ressources](http://wiki.eclipse.org/Rich_Client_Platform)

Eclipse application consists of individual software components, called *plug-ins* (e.g. new menu entries, toolbar entries). Eclipse RCP application typically uses the same base components as the Eclipse IDE and also add application-specific components on top. Core Components of the Eclipse Platform:
- **OSGi**, specification for modular Java applications
- **Equinox**, implementation of the OSGi specification, used by Eclipse 
- **SWT**,  standard  user  interface  component  library  used  by Eclipse (+ JFace)
- **Workbench**, provides framework for the application, responsible for all other UI components

<img src="https://www.vogella.com/tutorials/EclipseRCP/img/architecture20.png" width="60%">
</center>

Important configuration files:
- `MANIFEST.MF`, contains OSGi configuration information (API and dependencies)
- `plugin.xml`, contains Eclipse specific extension mechanisms, *Extension-points* (interfaces  for plug-ins  to  contribute  functionality) and *Extensions* (functionality)