# archi-kganalysis-plugin

>[Archi](https://www.archimatetool.com/) plugin that uses Knowledge Graphs to detect Enterprise Architecture Smells.


## Archi Customizations

### Menubar

<img src="./images/menubar.png" width="50%">

### Toolbar

<img src="./images/toolbar.png" width="50%">


### Import/Export

<img src="./images/export.png" width="50%">


### Preferences

<img src="./images/preferences.png" width="70%">


---
<br>

## Notes

### ArchiMate

**ArchiMate Language** defines the concepts to model Enterprise Architectures.

ArchiMate Language Model (Structure):

<img src="./images/archi-language.png" width="50%">

**ArchiMate Framework** classifies the elements of the language structure into *layers* and *aspects*.

**Layers**
- *Motivation* - why?
- *Strategy* - how to get there?
- *Business* - structure and function of business organization (conceptual)
- *Application* - running apps, available data (logical)
- *Technology* - (logical & physical)
- *Implementation & Migration*

**Aspects**
- *Passive Structure* - structural elements (business actors, application componets)
- *Behaviour* - behaviour (processes, functions, events) performed by actors
- *Active Structure* - objects on which behavior is performed

ArchiMate Framework:

<img src="./images/archi-framework.png" width="60%">


**Relationships**

[Overview](https://archimate.visual-paradigm.com/archimate-notation-part-8-relationships/)

- *Structural* - e.g. aggregation (element consists of one or more other elements) or realization
- *Dependency* - e.g. serving (element provides functionality to other element)
- *Dynamic* - e.g. flow (transfer from one elemet to another)
- *Other* - e.g. specialization (kind of other element) or association, junction



<br>

### Archi

Archi uses ArchiMate language

Archi Concepts:
<img src="https://www.archimatetool.com/wp-content/uploads/2018/11/ArchiMate-3.0-Notation-Overview-ArchiMate-standard-default-color-scheme.png" width="90%">

<br>

### EA Smells

Concept (similar to Code Smells) which indicate possible Technical Debts.

[Knowledge Base for EA Smells](https://swc-public.pages.rwth-aachen.de/smells/ea-smells/)




<br>

### Eclipse RCP

Archi is developed based on the Eclipse Rich Client Platform (RCP).  

#### Plug-in manifest
Ties all the code and resources together. Split into 2 manifest files:
- `MANIFEST.MF`, contains OSGi configuration information (API and dependencies)
- `plugin.xml`, contains Eclipse specific extension mechanisms, *Extension-points* (interfaces  for plug-ins  to  contribute  functionality) and *Extensions* (functionality)

#### SWT and JFace

**Standard Widget Toolkit (SWT)** - Java based user interface library, provides lots of standard widgets, e.g., buttons and text field or custom widgets  
**JFace** - adds additional convenient functionality on top of SWT and makes the usage of SWT simpler (e.g., JFace builder API)

Key components of SWT:
- `Shell` - represents Window
- `Display` - manages event loops, fonts, colors (base for all SWT components)

[Available SWT widgets](https://www.eclipse.org/swt/widgets/)

<br>

### Links and Publications 

#### Links

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

#### Publications
[Verification of ArchiMate Behavioral
Elements by Model Checking](https://link.springer.com/content/pdf/10.1007/978-3-319-24369-6_11.pdf) (Plugin for Archi)

<br>