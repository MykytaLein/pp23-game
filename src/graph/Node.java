package graph;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;

import logic.Field;
public class Node {
	// Node's color
 	private int color;
 	
 	// Fields included in the node
 	private HashSet<Field> includedFields = new HashSet<Field>();
	   
 	// List of the nodes included in the shortest path from the source node to the node
    private LinkedList<Node> shortestPath = new LinkedList<Node>();
    
    // Distance from source node
    private Integer distance = 500;
    
    private HashMap<Node, Integer> adjacentNodes = new HashMap<Node, Integer>();
    
    // Number of fields included in the node, used to sort a list of node to included in player's/computer's component
    private int size = 0;
    
    public Node() {
    	
    }
 
    /*
     * Constructors
     */
    public Node(int color) {
        this.color = color;
    }

	public Node(int color, HashSet<Field> includedFields) {
		super();
		this.color = color;
		this.includedFields = includedFields;
		this.size = includedFields.size();
	}

	public Node(int color, HashMap<Node, Integer> adjacentNodes) {
		this.color = color;
		this.adjacentNodes = adjacentNodes;
	}
    
	/**
	 * Adds an adjacent node with distance to it
	 * @param destination - adjacent node
	 * @param distance - distance to adjacent node
	 */
    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }
	
    /**
     * Returns node's maximum row
     * @return node's maximum row
     */
	public int getMaxRow() {
		int maxRow = 0;
		for(Field field: this.includedFields) {
			if(field.getRow() > maxRow) {
				maxRow = field.getRow();
			}
		}
		return maxRow;
	}
	
	/**
     * Returns node's maximum column
     * @return node's maximum column
     */
	public int getMaxColumn() {
		int maxColumn = 0;
		for(Field field: this.includedFields) {
			if(field.getCol() > maxColumn) {
				maxColumn = field.getCol();
			}
		}
		return maxColumn;
	}
	
	/*
	 * Getters and setters
	 */

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public HashSet<Field> getIncludedFields() {
		this.size = this.includedFields.size();
		return includedFields;
	}

	public void setIncludedFields(HashSet<Field> includedFields) {
		this.size = includedFields.size();
		this.includedFields = includedFields;
	}

	public LinkedList<Node> getShortestPath() {
		return shortestPath;
	}

	public void setShortestPath(LinkedList<Node> shortestPath) {
		this.shortestPath = shortestPath;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public HashMap<Node, Integer> getAdjacentNodes() {
		return adjacentNodes;
	}

	public void setAdjacentNodes(HashMap<Node, Integer> adjacentNodes) {
		this.adjacentNodes = adjacentNodes;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
