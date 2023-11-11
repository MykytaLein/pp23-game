package graph;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

import logic.Field;
import testing.Testing;

public class Dijkstra {
	private Graph graph;
	
	public Dijkstra(Field[][]board, Testing test, boolean forMinMoves) {
		this.graph = new Graph(board, test, forMinMoves);
	} 
	
	public Dijkstra(Graph graph) {
		this.graph = graph;
	}
	
	/**
	 * Returns the minimum number of moves to include a field with coordinates @param row, @param column to player's component, 
	 * assumed that only player moves, can choose computer's component color and may choose colors only sequentially and ascending
	 * @param row - row of the field to include
	 * @param col - column of the field to include
	 * @return the minimum number of moves to include a field with coordinates @param row, @param column to player's component
	 */
	public int getShortestPathToField(int row, int col) {
		Field field = this.graph.getBoard()[row][col];
		int result = Integer.MAX_VALUE - 1;
		HashSet<Node>sourceNodeAdjNodes = this.graph.getSourceNodeAdjNodes();
		
		// Try every of six colors as first color for the player's component
		for(int color = 1; color <= 6; color++) {
			Node sourceNode = this.graph.getSourceNode();
			
			// Initialize distances to source node adjacent nodes given the first color for player's component to choose
			for(Node sourceAdjNode: sourceNodeAdjNodes) {
				int distance = (sourceAdjNode.getColor() - color + 6) % 6 + 1;
				sourceNode.addDestination(sourceAdjNode, distance);
			}
			
			// Calculate shortest paths to all nodes from the player's component
			Graph newGraph = this.calculateShortestPathFromSource(this.graph, sourceNode);
			Node nodeWithField = newGraph.getNodeFieldIn(field);
			int distanceToTargetNode = nodeWithField.getDistance();
			// Update minimum number of moves to include the node with the field to include, if the new result is less the previous 
			if(distanceToTargetNode < result) {
				result = distanceToTargetNode;
			}
		}
		return result;
	}
	
	/**
	 * Calculates minimum distances from the @param source node to all other nodes in the @param graph
	 * @param graph - graph to calculate minimum distances 
	 * @param source - node to calculate minimum distances from
	 * @return Graph with nodes distances set to minimum distance from @param source node
	 */
	public Graph calculateShortestPathFromSource(Graph graph, Node source) {
	    source.setDistance(0);

	    HashSet<Node> settledNodes = new HashSet<>();
	    HashSet<Node> unsettledNodes = new HashSet<>();

	    unsettledNodes.add(source);

	    while (unsettledNodes.size() != 0) {
	        Node currentNode = this.getLowestDistanceNode(unsettledNodes);
	        unsettledNodes.remove(currentNode);
	        for (Entry <Node, Integer> adjacencyPair: 
	          currentNode.getAdjacentNodes().entrySet()) {
	            Node adjacentNode = adjacencyPair.getKey();
	            Integer edgeWeight = adjacencyPair.getValue();
	            if (!settledNodes.contains(adjacentNode)) {
	                this.calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
	                unsettledNodes.add(adjacentNode);
	            }
	        }
	        settledNodes.add(currentNode);
	    }
	    return graph;
	}
	
	/**
	 * Get the nearest node from the Set of not yet processed nodes
	 * @param unsettledNodes - Set of not yet processed nodes
	 * @return nearest node from the Set of not yet processed nodes
	 */
	private Node getLowestDistanceNode(Set < Node > unsettledNodes) {
	    Node lowestDistanceNode = null;
	    int lowestDistance = Integer.MAX_VALUE;
	    for (Node node: unsettledNodes) {
	        int nodeDistance = node.getDistance();
	        if (nodeDistance < lowestDistance) {
	            lowestDistance = nodeDistance;
	            lowestDistanceNode = node;
	        }
	    }
	    return lowestDistanceNode;
	}
	
	/**
	 * Calculates and updates the minimum distance from source node to @param evaluationNode
	 * @param evaluationNode - node to set distance from source node
	 * @param edgeWeight - distance between @param evaluationNode and @param currentNode
	 * @param currentNode - @param evaluationNode's neighboring node, visited before @param evaluationNode
	 */
	private void calculateMinimumDistance(Node evaluationNode, Integer edgeWeight, Node currentNode) {
	    Integer sourceDistance = currentNode.getDistance();
	    if (sourceDistance + edgeWeight < evaluationNode.getDistance()) {	    	
	        evaluationNode.setDistance((sourceDistance + edgeWeight));
	        LinkedList<Node> shortestPath = new LinkedList<>(currentNode.getShortestPath());
	        shortestPath.add(currentNode);
	        evaluationNode.setShortestPath(shortestPath);
	    }
	}
	
	/*
	 * Getters and setters
	 */

	public Graph getGraph() {
		return graph;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	
}
