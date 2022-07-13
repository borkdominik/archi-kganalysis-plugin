This guide shows how to add new smells to the Tool.

Useful Resources:
- [Neo4j Cypher Queries for EA Smells](https://github.com/borkdominik/CM2KG/tree/main/at.ac.tuwien.big.msm.cmgba.graphml/smellQueries)
- [Java Implementation of various EA Smells](https://github.com/borkdominik/CM2KG/tree/main/EASmells_Salentin/src/main/java/de/example/smells)
- [EA Smell Catalogue](https://swc-public.pages.rwth-aachen.de/smells/ea-smells/)

## EA Smell Implementation

EA Smell: **Chatty Service**

*Query*
```sql
MATCH (a)-[r:RELATIONSHIP]-(b)
WHERE a.class contains 'Service' and b.class contains 'Service' and not r.type contains 'Composition'
WITH a, count(r) as cnt
WHERE cnt > 3
RETURN a, cnt
```

The following figure shows an example of a Chatty Service with the element `Payment Service` (BusinessService) connected to more than 3 other services. 

*Example*
<img src="./images/new-smell-model.png">

### Step 1 - Create a new Smell

Create a new Java Class for the EA Smell within the package `kganalysis.smells` and extend the class `AbstractSmell`.
The abstract class provides access to the graph database and requires implementing the methods `getLabel()` that returns the name of the smell (root element in the table) and `detect()` that returns the detected elements that are affected by the smell (child elements in the table). 

In the case of the _Chatty Service_ smell, we only add the `Smell` label to detected elements and do not require to create a new relationship. If the new Smell requires to create new relationships, see, e.g., the implementations for _Strict Layer Violation_ or _Cyclic Dependency_. 

```java
public class ChattyServiceSmell extends AbstractSmell {

	private final String LABEL = "Chatty Service";
	private final String QUERY = "MATCH (a)-[r:RELATIONSHIP]-(b)\n"
			+ "WHERE a.class contains 'Service' and b.class contains 'Service' and not r.type contains 'Composition'\n"
			+ "WITH a, count(r) as cnt\n"
			+ "WHERE cnt > 3\n"
			+ "RETURN a, cnt";
	
	@Override
	public String getLabel() {
		return LABEL;
	}

	@Override
	public String[] detect() {
		List<String> detected = new ArrayList<String>();
		
                // start a new transaction, execute the query and iterate over the result
		try (Transaction tx = graphDb.beginTx(); Result result = tx.execute(QUERY)) {
			while (result.hasNext()) {
                                Map<String, Object> row = result.next();
                                
                                // store the node and count from the result
				Node a = (Node) row.get("a");
				long count = (long) row.get("cnt");
				
                                // add the "Smell" label to the node
				a.addLabel(Label.label("Smell"));

                                // format the string to be shown in the table
				String elementName = (a).getProperty("name").toString();
				detected.add("Element '"  + elementName + "' is connected to " + count + " other services.");
			}
			tx.commit();
		}
		return detected.toArray(String[]::new);
	}

}
```

### Step 2 - Add the Smell to the SmellDetectionProvider

As a next step, add the smell to the `runSmellDetection()` method in the `SmellDetectionProvider` which is responsible for running the smell detection process. 


```java
private void runSmellDetection() {
		List<AbstractSmell> smells = new ArrayList<>();
		String[] elements;

		smells.add(new ChattyServiceSmell()); // --> Smell added here
		smells.add(new CyclicDependencySmell());
		smells.add(new DeadComponentSmell());
		smells.add(new DocumentationSmell());
		smells.add(new DuplicationSmell());
		smells.add(new StrictLayerViolationSmell());

		String[] smellNames = smells
				.stream()
				.map(AbstractSmell::getLabel)
				.toArray(String[]::new);
		contentMap.put("smells", smellNames);
		
                for (AbstractSmell smell : smells) {
			elements = smell.detect();
			contentMap.put(smell.getLabel(), elements);
		}
	}
```

### Step 3 - Adapt Visualization

As a final step, the smell has to be included in the graph visualization. Detected nodes with the `Smell` label (which we added in the step before) are automatically included in the visualization. However, it is still necessary to include information about the smell that shows up in the EA Smells tab. For this we add JSON data from the [EA Smells Catalog](https://swc-public.pages.rwth-aachen.de/smells/ea-smells/) to the `smells.js` file. To get the JSON data click on "Copy JSON file" for the smell in the catalog. An additional property `query` has to be added to the JSON data that contains the cypher query for visualizing the smell. In this case, we slightly adapt the previous query.

> If a relationship is created for the smell the `drawSmells()` method in `index.js` has to be adapted to show the relationship (not the case for the example).

```js
smells = [{
  "name": "Chatty Service",
  "description": "A high number of operations is required to complete one abstraction. Such operations are typically rather simple tasks that needlessly slow down an entire process.",
  "query": "MATCH (a)-[r:RELATIONSHIP]-(b) WHERE a.class contains 'Service' and b.class contains 'Service' and not r.type contains 'Composition' WITH a, count(r) as cnt WHERE cnt > 3 MATCH (a)-[r1:RELATIONSHIP]-(b1) WHERE a.class contains 'Service' and b1.class contains 'Service' RETURN a, r1, b1",
  "context": "Derived from: \"Chatty Service\"",
  "detection": "A chatty service may have many fine-grained operations/responsibilities. It is suspicious when a service relates to many other services and makes use of them, especially when the provided functionality is rather simple.",
  "consequences": "Maintenance becomes harder, like e.g. changing the order of invocations. Many interactions are required, which leads to overall higher time consumption. This can be especially harmful when synchronous tasks are chained together.",
  "cause": "",
  "solution": "Simple operations should not be made available to other elements. Instead, a more coarse-grained operation should be created to fulfill an abstraction.",
  "example": "",
...
```

**Done!** The smell is now fully added to the tool.

*Table*
<img src="./images/new-smell-table.png">

*Visualization*
<img src="./images/new-smell-visualization.png">





