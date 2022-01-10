# General


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

# EA Smells

Concept (similar to Code Smells) which indicate possible Technical Debts.

[Knowledge Base for EA Smells](https://swc-public.pages.rwth-aachen.de/smells/ea-smells/)


## List

### Dead Component

Unused component / support future behaviour

``` 
MATCH (n)
WHERE not(n)-−()
return n
``` 


### Dense Structure

An EA repository has dense dependencies
without any particular structure.

```
MATCH (p)
RETURN CASE WHEN avg(apoc.node.degree(p)) > 1.75
    THEN 1 ELSE 0 END AS result
```

### Documentation

A lengthy documentation often points to
unnecessary complex structures.

```
MATCH (n)
WHERE size(n.documentation)>256
RETURN n
```


### Duplication

Two or more abstractions with highly
similar functionality exist.

```
MATCH (a), (b)
WHERE a<>b and a.ClassName = b.ClassName
    AND apoc.text.jaroWinklerDistance(
    a.Label, b.Label) > 0.8
RETURN a, b, apoc.text.jaroWinklerDistance(
    a.Label, b.Label) as similarNameScore
```


### Hub-like Modularization

This smell arises when an abstraction
has dependencies (both incoming and
outgoing) with a large number of
other abstractions, being a single point
of failure.


### Lazy Component

A component that is not doing enough
to pay for itself should be eliminated.
Those components often only pass
messages on to another.


### Message Chain

A number of services that rely
on each other, while providing
similar functionality.


### Shared Persistency

Different services access the same database.
In the worst case, different services access
the same entities of the same schema.




















kann diese propernutzen um die Nodes in z.B. verschiedene Größen oder Farben darzustellen


wobei ich es noch nicht hinbekommen hab die Nodes in neovis komplett zu customizen ()

Habs leider , wobei aber diverse Algorithmen  anwendbar sind und 




Jedoch kann man  anwenden um so z.B. score

