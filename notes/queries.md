Match all nodes:
```
MATCH (n)
RETURN n
```


Load ArchiMate Elements:
```
LOAD CSV WITH HEADERS FROM 'file:///elements.csv' AS line
CREATE (:elements {class:line.Type, name:line.Name, documentation:line.Documentation, id:line.ID })
```

Load ArchiMate Relations:
```
LOAD CSV WITH HEADERS FROM 'file:///relations.csv' AS line
MATCH (n {id:line.Source})
WITH n, line
MATCH (m {id:line.Target})
WITH n, m, line
CREATE (n)-[:relationships {id:line.ID, class:line.Type, documentation:line.Documentation, name:line.Name}]->(m)
```