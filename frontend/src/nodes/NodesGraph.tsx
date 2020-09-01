import React, {memo} from "react";
import Graph from "react-graph-vis";

const graph = {
    nodes: [
        {id: 1, label: "Node 1", title: "node 1 tootip text"},
        {id: 2, label: "Node 2", title: "node 2 tootip text"},
        {id: 3, label: "Node 3", title: "node 3 tootip text"},
        {id: 4, label: "Node 4", title: "node 4 tootip text"},
        {id: 5, label: "Node 5", title: "node 5 tootip text"}
    ],
    edges: [
        {from: 1, to: 2},
        {from: 1, to: 3},
        {from: 2, to: 4},
        {from: 2, to: 5}
    ]
};
export const NodesGraph = memo<any>(({data}) => {
    console.log(data);
    console.log(graph);
    const options = {
        layout: {
            // improvedLayout: true
            // hierarchical: false
        },
        nodes: {
            shape: 'box',
            // widthConstraint: 50,
            color: "#cccccc",
            margin: 5,
            font: {
                size: 16
            }
        },
        edges: {
            color: "#fff",
            width: 2,
            arrows: {
                from: false,
                to: false
            },
            smooth: {
                type: "continuous",
                forceDirection: "none",
                roundness: 0.5,
            }
        },
        autoResize: true,
        height: "400px",
        physics: {
            solver: "hierarchicalRepulsion",
            hierarchicalRepulsion: {
                centralGravity: 0,
            },
        },
        interaction: {
            dragNodes: false,
            hover: true,
            zoomView: false,
        },
    };

    const events = {
        select: function (event) {
            var {nodes, edges} = event;
        },
        hoverNode: function (event) {
            console.log("event", event)
        }
    };
    return (
        <Graph
            graph={data}
            options={options}
            events={events}
        />
    )
})