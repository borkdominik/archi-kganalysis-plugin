# EA Smells


## Chatty Service
```sql
MATCH (a)-[r]-(b) 
WHERE  a.ClassName contains 'Service' and b.ClassName contains 'Service' 
WITH b, count(r) as cnt WHERE cnt>4 MATCH (a1)-[r1]-(b) 
WHERE  a1.ClassName contains 'Service' and b.ClassName contains 'Service' 
RETURN a1, b, cnt
```

## Dense Structure

```sql
MATCH (p) RETURN CASE WHEN avg(apoc.node.degree(p)) > 1.75 THEN 1 ELSE 0 
END as result
```