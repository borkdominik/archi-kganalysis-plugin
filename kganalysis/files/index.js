var viz;
// reusable vis.js config
const defaultVisConfig = {
    nodes: {
        font: { size: 22, face: 'Tahoma', strokeWidth: 3, vadjust: -12 },
        labelHighlightBold: true,
        opacity: 0.8,
        shadow: { enabled: true, size: 20 },
        shape: "dot",
        widthConstraint: { maximum: 160 },
    },  
    edges: {
        arrows: { to: { enabled: true, scaleFactor: 1.5 } },
        arrowStrikethrough: false,
        color: { inherit: 'both', opacity: 0.5 },
        font: { align: 'top' },
        hoverWidth: 5.0,
        smooth: { type: "continuous", forceDirection: "none" },
        width: 3.0
    },
    layout: { improvedLayout: true },
    interaction: { 
        hover: true, 
        navigationButtons: true, 
        keyboard: true 
    },
    physics: {
        stabilization: { enabled: true, fit: true },
        solver: "forceAtlas2Based",
        forceAtlas2Based: {
            theta: 0.5,
            gravitationalConstant: -150,
            centralGravity: 0.015,
            springConstant: 0.08,
            springLength: 100,
            damping: 0.5,
            avoidOverlap: 0.8
        }
    }
};

// draws the initial graph
function draw() {
    var config = {
        containerId: "viz",
        neo4j: { serverUrl: "bolt://localhost:7687" },
        initialCypher: $("#cypher").val(),
        labels: {
            [NeoVis.NEOVIS_DEFAULT_CONFIG]: {
                label: "name",
                color: "color",
                value: $("#node-size input[type='radio']:checked").val(), 
                [NeoVis.NEOVIS_ADVANCED_CONFIG]: {
                    function: { 
                        title: (props) => NeoVis.objectToTitleHtml(props, ["name", "class", "layer", "aspect"]),
                    },
                }
            }
        },
        relationships: {
            "RELATIONSHIP": {
                [NeoVis.NEOVIS_ADVANCED_CONFIG]: {
                    function: {
                        title: (props) => NeoVis.objectToTitleHtml(props, ["name", "type"])
                    }
                }
            }  
        },
        visConfig: defaultVisConfig
    };

    viz = new NeoVis.default(config);
    viz.render();
}

function drawCommunites() {
    var config = {
        containerId: "viz",
        neo4j: { serverUrl: "bolt://localhost:7687" },
        initialCypher: $("#cypher").val(),

        labels: {
            [NeoVis.NEOVIS_DEFAULT_CONFIG]: {
                label: "name",
                group: $("#community-color input[type='radio']:checked").val(),
                value: $("#node-size input[type='radio']:checked").val(), 
                [NeoVis.NEOVIS_ADVANCED_CONFIG]: {
                    function: { 
                        title: (props) => NeoVis.objectToTitleHtml(props, ["name", "class", "layer", "aspect"]),
                    }
                }
            }
        },
        relationships: {
            "RELATIONSHIP": {
                [NeoVis.NEOVIS_ADVANCED_CONFIG]: {
                    function: {
                        title: (props) => NeoVis.objectToTitleHtml(props, ["name", "type"])
                    }
                }
            }  
        },
        visConfig: defaultVisConfig
    };

    viz = new NeoVis.default(config);
    viz.render();
}

function drawSmells() {
    var smellConfig = {
        containerId: "viz",
        neo4j: { serverUrl: "bolt://localhost:7687" },
        initialCypher: $("#cypher").val(),//"MATCH (n) OPTIONAL MATCH (n)-[r]->(m) RETURN n, r, m",
        labels: {
          [NeoVis.NEOVIS_DEFAULT_CONFIG]: {
            label: "name",
            color: "color",
            value: $("#node-size input[type='radio']:checked").val(), 
            [NeoVis.NEOVIS_ADVANCED_CONFIG]: {
              function: { 
                title: (props) => NeoVis.objectToTitleHtml(props, ["name", "class", "layer", "aspect"]),
              }
            }
          },
          "Smell": {
            value: $("#node-size input[type='radio']:checked").val(),
            [NeoVis.NEOVIS_ADVANCED_CONFIG]: {
                /*TODO: smell info - function: { 
                  title: (props) => NeoVis.objectToTitleHtml(props, ["name"])
                },*/
                static: {
                  borderWidth: 2,
                  color: '#F08080',
                }
              },
            },
          },
          relationships: {
            "RELATIONSHIP": {
            [NeoVis.NEOVIS_ADVANCED_CONFIG]: {
              function: { // same as label advance function
                title: NeoVis.objectToTitleHtml
              }
            }
          },
          "DUPLICATION": {
            [NeoVis.NEOVIS_ADVANCED_CONFIG]: {
              function: { // same as label advance function
                title: NeoVis.objectToTitleHtml
              },
              static: { 
                label: "DUPLICATION",
                dashes: true,
                color: 'red',
              }  
            }
          },
          "CYCLIC_DEPENDENCY": {
            [NeoVis.NEOVIS_ADVANCED_CONFIG]: {
              static: { 
                label: "CYCLIC_DEPENDENCY",
                dashes: true,
                color: 'red'
              }  
            }
          },
          "STRICT_LAYER_VIOLATION": {
            [NeoVis.NEOVIS_ADVANCED_CONFIG]: {
              static: { 
                label: "STRICT_LAYER_VIOLATION",
                dashes: true,
                color: 'red'
              }  
            }
          }
        },
        visConfig: defaultVisConfig
    };
    
    viz = new NeoVis.default(smellConfig);
    viz.render();
}

$(window).ready(function() {
    // Update EA Smells tab with data from smells.js
  smells.forEach(function(currentValue, index) {
    var $card = $("<div>", {class: "list-group-item d-flex justify-content-between align-items-center"});
    var $smellName = $("<h6>", {class: "ml-2", text: smells[index].name});
    var $button = $("<button>", {class: "btn btn-sm btn-warning mr-2", text: "Detect"});
      
    // event listener to update info once smell is clicked
    $button.click(function() {
      $("#cypher").val(smells[index].query);
      drawSmells();
      $("#smellName").text(smells[index].name);
      $("#smellDescription").text("Description: " + smells[index].description);
      $("#smellSolution").text("Solution: " + smells[index].solution);
    });

    $card.append($smellName, $button);
    $("#smells-list").append($card);
  });
  draw();
});

  // -- Show Graph --
  $("#getAll").click(async function () {
    $("#cypher").val("MATCH (n) OPTIONAL MATCH (n)-[r:RELATIONSHIP]->(m) RETURN n, r, m");
    viz.stabilize();
    draw();
  });

  // -- Show Smells --
  $("#smellDetection").click(async function () {
    $("#cypher").val("MATCH (n) OPTIONAL MATCH (n)-[r]->(m) RETURN n, r, m");
    viz.stabilize();
    drawSmells();
  });

  // -- Options --
  $("#save").click(async function () {
    $("#cypher").val("MATCH (n) OPTIONAL MATCH (n)-[r:RELATIONSHIP]->(m) RETURN n, r, m");
    viz.stabilize();
    if ($("#community-color input[type='radio']:checked").val() === 'color') {
      draw();
    } else {
      drawCommunites();
    }
  });

// -- Filters --
$("#apply").click(async function () {
    var query = "MATCH (n)  WHERE (";
    if ($("#layers-form input[type='checkbox']:checked").length === 0 || $("#aspect-form input[type='checkbox']:checked").length === 0) {
      return;
    }
    $("#layers-form input[type='checkbox']:checked").each(function(){
      query += "n.";
      query += ($(this).val());
      query += " OR ";
    });
    query = query.slice(0, query.length - 4);
    query += ") AND ("
    $("#aspect-form input[type='checkbox']:checked").each(function(){
      query += "n.";
      query += ($(this).val());
      query += " OR ";
    });
    query = query.slice(0, query.length - 4);
    query += ") OPTIONAL MATCH (n)-[r]->(m) RETURN n, r";
    $("#cypher").val(query);
    viz.stabilize();
    draw();
  });

  $("#reload").click(async function() {
    viz.stabilize();
    var cypher = $("#cypher").val();

    if (cypher.length > 3) {
     draw();
   } else {
    viz.reload();
  }
});

$("#stabilize").click(function() {
    viz.stabilize();
});