smells = [{
  "name": "Cyclic Dependency",
  "aliases": [
  ""
  ],
  "query": "MATCH (a)-[r1:CYCLIC_DEPENDENCY]->(b)-[r2:CYCLIC_DEPENDENCY]->(c)-[r3:CYCLIC_DEPENDENCY]->(a) RETURN a, r1, r2, r3, b,c",
  "description": "This smell arises when two or more abstractions depend on each other directly or indirectly (creating a tight coupling between the abstractions). A cyclic chain of calls between abstractions exists.",
  "context": "Derived from: \"Cyclic Dependency\"<br /> A special form of Cyclic Dependency is sometimes exhibited with specializations. A subtype has an obvious dependency on its supertype. However, when the supertype also depends on the subtype (for instance, by having an explicit reference to the subtype), it results in a cyclic dependency.",
  "detection": "Abstractions depend on each other in a cyclic interaction pattern, e.g. A depends on B, B depends on C, and C depends back on A. So check all reachable elements for the original element to detect cycles.",
  "consequences": "Abstractions involved in a cyclic dependency can be harder to maintain or reuse, as they are more strongly coupled.",
  "cause": "Hard to visualize indirect dependencies.",
  "solution": "Resolve the cycles by e.g. relocating functionality (merging) or use an intermediary element.",
  "example": "A depends on B, B on C and C on A",
  "sources": [],
  "tags": [
  "soa",
  "microservices",
  "business",
  "application",
  "technology",
  "The Couplers",
  "between elements",
  "automatic detection"
  ],
  "relatedItems": [
  {
    "relation": "relates",
    "name": "Chatty Service"
  },
  {
    "relation": "relates",
    "name": "Nanoservices"
  },
  {
    "relation": "relates",
    "name": "Message Chain"
  }
  ],
  "relatedAntiPatterns": [
  {
    "relation": "relates",
    "name": "Chatty Service",
    "description": "A high number of operations is required to complete one abstraction. Such operations are typically rather simple tasks that needlessly slow down an entire process."
  },
  {
    "relation": "relates",
    "name": "Message Chain",
    "description": "A chain of service calls and messages fulfills common functionality."
  },
  {
    "relation": "relates",
    "name": "Nanoservices",
    "description": "A service is too fine-grained so that its communication and maintenance efforts outweigh its utility. Such services often require several other coupled services to complete an abstraction."
  }
  ],
  "evidence": 0
},
{
  "name": "Dead Component",
  "aliases": [
  "Boat Anchor"
  ],
  "query": "MATCH (n) WHERE not (n)-[:RELATIONSHIP]-() RETURN n",
  "description": "A component is no longer used (usually because it is now obsolete). Or you have generic or abstract elements that are not actually needed today. Such components often exists to support future behavior, which may or may not be necessary in the future.",
  "context": "Derived from: \"Dead Code\"<br /> \"I don't know what that component is used for; it existed before I got here.\" If you are working on a framework, it is eminently reasonable to create functionality not used in the framework itself, as long as the functionality is needed by the users.",
  "detection": "Search for isolated elements: The element respectively its cluster has no incoming or outgoing relations to other components (structural relations can be neglected).",
  "consequences": "Unused elements increase the complexity which harms the readability and maintainability.",
  "cause": "When requirements have changed or corrections have been made, nobody had time to clean up the old structure. Sometimes components are created \"just in case\" to support anticipated future features that never get implemented.",
  "solution": "Delete unused elements. Redistribute the functionality and aggregate components.",
  "example": "A policy or programmatic relationship may require the purchase and usage of a particular piece of hardware or software. The consequences for managers and developers are that significant effort may have to be devoted to making the product work. After a significant investment of time and resources, the staff realizes that the product is useless in the current context, and abandons it for another approach. Eventually, the Boat Anchor is set aside and gathers dust in some corner (if it's hardware).",
  "sources": [],
  "tags": [
  "business",
  "application",
  "technology",
  "The Dispensables",
  "within elements",
  "automatic detection"
  ],
  "relatedItems": [
  {
    "relation": "follows",
    "name": "Lazy Component"
  },
  {
    "relation": "follows",
    "name": "Vendor Lock-In"
  }
  ],
  "evidence": 0,
  "relatedAntiPatterns": [
  {
    "relation": "follows",
    "name": "Lazy Component",
    "description": "A component that is not doing enough to pay for itself should be eliminated. Those components have limited responsibilities. That way, they clutter design, creating unnecessary abstractions. Although delegation is good, and one of the key fundamental features of abstractions, too much of a good thing can lead to objects that add no value, simply passing messages on to another component."
  },
  {
    "relation": "follows",
    "name": "Vendor Lock-In",
    "description": "The usage of diverse proprietary technology leads to many potentially conflicting standards and additional complexity in a system. Often combined with a Wolf Ticket, which is a product that claims openness and conformance to standards that have no enforceable meaning."
  }
  ]
},
{
  "name": "Documentation",
  "aliases": [
  "Comments"
  ],
  "query": "MATCH (n) WHERE size(n.documentation)>256 RETURN n",
  "description": "When you feel like writing a long and complex comment on aspects and the use of one element, try to refactor so that the comment becomes superfluous. Often documentations are even poorly written or completely unnecessary.",
  "context": "Derived from: \"Comments\"<br /> Explanations can sometimes be useful: When explaining why something is being implemented in a particular way. When explaining complex structures/processes (when all other methods for simplification have been tried and come up short). Note that this smell is called \"Documentation\" because it is compliant to the ArchiMate-Notation. Maybe it should be renamed in the future with a more accurate name, because it can be confused with a holistic documentation.",
  "detection": "Search for quite long documentation belonging to one complex element or comments explaining obvious things.",
  "consequences": "",
  "cause": "Comments are usually created with the best of intentions, when something is not intuitive or obvious. In such cases, explanations are like a deodorant masking the smell of fishy abstractions that could be improved.",
  "solution": "Refactor elements to make them self-explanatory.",
  "example": "",
  "sources": [],
  "tags": [
  "business",
  "application",
  "technology",
  "within elements",
  "The Dispensables",
  "automatic detection"
  ],
  "relatedItems": [
  {
    "relation": "relates",
    "name": "Architecture by Implication"
  }
  ],
  "relatedAntiPatterns": [
  {
    "relation": "relates",
    "name": "Architecture by Implication",
    "description": "This antipattern is characterized by the lack of architecture specifications for a system under development. Usually, the architects responsible for the project have experience with previous system construction, and therefore assume that documentation is unnecessary."
  }
  ],
  "evidence": 0
},
{
  "name": "Duplication",
  "aliases": [
    ""
  ],
  "query": "MATCH (a)-[r:DUPLICATION]-(b) RETURN a, r",
  "description": "Two or more abstractions with highly similar functionality exist.",
  "context": "Derived from: \"Duplicated Code\"<br /> Don't Repeat Yourself! While Microservices usually follow a \"share nothing\" philosophy, reuse is a common theme in SOA. Service normalization and consolidation are therefore frequent activities in an SOA to reduce the level of duplication. In Microservices on the other, you consciously allow duplication to exist up to a certain degree, because it reduces coupling and dependencies in your system.",
  "detection": "Services or operations with similar or identical names and/or message parameters may exist in your system. If the ratio of similar words to the total number of words in the names exceeds 0.75 the smell is detected.",
  "consequences": "Duplicated functionality reduces the level of reuse and may confuse service consumers that cannot identify the service or operation they need. Moreover, if this common functionality changes, it needs to change in all duplicated locations, therefore causing higher maintenance efforts. A culture of \"build and expose\" may form where the enterprise becomes a mess of services all attempting to do the same thing.",
  "cause": "Duplication usually occurs when multiple architects are working on different parts of the same architecture at the same time. Since they're working on different tasks, they may be unaware their colleague has already defined similar components that could be repurposed for their own needs. There's also more subtle duplication, when specific parts look different but actually perform the same job. This kind of duplication can be hard to find and fix. Sometimes duplication is purposeful. When rushing to meet deadlines and the existing architecture is \"almost right\" for the job, novice architects may not be able to resist the temptation of copying and pasting the relevant elements. And in some cases, the developer is simply too lazy to de-clutter.",
  "solution": "Consolidate the duplicate functionality into a single service. If necessary, raise the level of abstraction. Be aware though that this increases coupling in the system and may make the consolidated service a bottleneck or single point of failure.",
  "example": "",
  "sources": [],
  "tags": [
    "soa",
    "business",
    "application",
    "technology",
    "ambiguity",
    "The Dispensables",
    "within elements",
    "automatic detection"
  ]
},
{
  "name": "Strict Layer Violation",
  "query": "MATCH (a)-[r:STRICT_LAYER_VIOLATION]-(b) WHERE a.layer contains 'business' and b.layer contains 'technology' RETURN a, r, b",
  "description": " In this scenario, one cannot reliably prevent that strict layers are violated. It can always happen that a layer skips the one directly beneath it and accesses a layer further below instead.",
  "context": "Derived from: \"Strict Layers Violation\"<br /> This violation very often occurs in the real world, although the model itself defined the layers and processes precisely.",
  "detection": "There is an explicit and direct relation between Business and Technology Layer elements.",
  "consequences": "Alterability is affected. The number of a layer's potential clients will increase, and the dependency between layers will grow.",
  "cause": "Layers must be built based on conventions.",
  "solution": "No solution available.",
  "example": "",
  "sources": [],
  "tags": [
    "business",
    "application",
    "technology",
    "between elements",
    "automatic detection"
  ]
}];