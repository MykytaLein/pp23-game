package graph;

import java.util.HashSet;
import java.util.ArrayList;

import logic.Field;
import testing.Testing;

public class Graph {
	
	private HashSet<Node> nodes = new HashSet<Node>();
	private Node sourceNode;
	private HashSet<Node> sourceNodeAdjNodes = new HashSet<Node>();
	private Node compComponent;
	
	private int noRows;
	private int noColumns;
	private Field[][]board; 
	
	private Testing test;
	
	/**
	 * Default constructor, that builds a graph from two-dimensional array of fields (board)
	 * with neighboring fields of same color as nodes
	 * @param board - two-dimensional array of fields (board)
	 * @param test  - instance of Testing class                     
	 */
	public Graph(Field[][] board, Testing test, boolean forMinMoves) {
		this.test = test;
		this.board = board;
		this.noRows = test.initNoRows(this.board);
		this.noColumns = test.initNoColumns(this.board);
		
		for(int row = this.noRows - 1; row >= 0; row--) {
			for(int column = 0; column < this.noColumns; column++) {
				if(!this.isInGraph(board[row][column])) {
					HashSet<Field> nodeFields = test.getComponent(board, row, column);
					Node newNode = new Node(board[row][column].getColor(), nodeFields);
					if(column == 0 && row == this.noRows - 1) {
						this.sourceNode = newNode;
					}
					this.addNode(newNode);
				}
			}
		}
		this.compComponent = this.getNodeFieldIn(this.board[0][this.noColumns - 1]);
		this.initAdjustmentNodes(forMinMoves);
	}
	
	/**
	 * Checks if field is in one of the nodes in the graph
	 * @param field - field to look for
	 * @return true if @param field is in one of the node in the graph, false else
	 */
	public boolean isInGraph(Field field) {
		for(Node node: this.nodes) {
			if(node.getIncludedFields().contains(field)) return true;
		}
		return false;
	}
	
	/**
	 * Adds node to the graph
	 * @param newNode
	 */
	public void addNode(Node newNode) {
        nodes.add(newNode);
    }
	
	/**
	 * Initializes distances between all neighboring nodes
	 * @param forMinMoves - boolean true, if it is for one of the minMoves methods from Testing class, 
	 * 						i.e. if players has to choose colors sequentially and ascending 
	 */
	public void initAdjustmentNodes(boolean forMinMoves) {
		Node[] nodesToArray = this.nodes.toArray(new Node[this.nodes.size()]);
		for(int nodeIx = 0; nodeIx < nodesToArray.length; nodeIx++) {
			Node node = nodesToArray[nodeIx];
			this.updateAdjNodes(node, forMinMoves);
		}
	}
	
	/**
	 * Paints source node (i.e. player's game component)in @param color and adds neighboring nodes with the same color to it
	 * @param color - new color for the source node (i.e. player's component)
	 */
	public void extendSourceNode(int color) {
		this.sourceNode.setColor(color);
		Node[]sourceAdjNodesArr = this.sourceNodeAdjNodes.toArray(
				new Node[this.sourceNodeAdjNodes.size()]);
		
		for(int nodeIx = 0; nodeIx < sourceAdjNodesArr.length; nodeIx++) {
			Node node = sourceAdjNodesArr[nodeIx];

			if(node.getColor() == color) {
				for(Field field: node.getIncludedFields()) {
					this.sourceNode.getIncludedFields().add(field);
				}
				this.nodes.remove(node);
				this.sourceNodeAdjNodes.remove(node);
			}
		}
		this.updateAdjNodes(this.sourceNode, true);
	}
	
	/**
	 * Updates adjacent nodes of @param node
	 * @param node - node which adjacent node are to be updated
	 * @param forMinMoves - boolean true, if it is for one of the minMoves methods from Testing class, 
	 * 						i.e. if players has to choose colors sequentially and ascending, false else
	 */
	public void updateAdjNodes(Node node, boolean forMinMoves) {
		HashSet<Field> nodeFields = node.getIncludedFields();
		for(Field field: nodeFields) {
			// If field is not in the first row check upper neighbor
			if(field.getRow() > 0) {
				this.checkNeighbor(field.getRow() - 1, field.getCol(), node, nodeFields, forMinMoves);
			}
			// If field is not in the last row check lower neighbor
			if(field.getRow() < this.noRows - 1) {
				this.checkNeighbor(field.getRow() + 1, field.getCol(), node, nodeFields, forMinMoves);
			}
			// If field is not in the first column check left neighbor
			if(field.getCol() > 0) {
				this.checkNeighbor(field.getRow(), field.getCol() - 1, node, nodeFields, forMinMoves);
			}
			// If field is not in the last column check right neighbor
			if(field.getCol() < this.noColumns - 1) {
				this.checkNeighbor(field.getRow(), field.getCol() + 1, node, nodeFields, forMinMoves);
			}
		}
	}
	
	public void checkNeighbor(int row, int column, Node node, HashSet<Field>nodeFields, boolean forMinMoves) {
		Field neighborField = this.board[row][column];
		// If the neighboring field is not included in the node 
		if(!nodeFields.contains(neighborField)) {
			Node newAdjNode = this.getNodeFieldIn(neighborField);
			// If node is not the source node update its adjacent nodes attribute
			if(!(node == this.sourceNode)){
				if(!node.getAdjacentNodes().containsKey(newAdjNode)) {
					int distance = 1;
					if(forMinMoves) {
						distance = (newAdjNode.getColor() - node.getColor() + 6) % 6;
					}
					node.addDestination(newAdjNode, distance);							
				}
			// If node is the source node update graph attribute sourceNodeAdjNodes
			}else {
				this.sourceNodeAdjNodes.add(newAdjNode);
			}
		}
	}
	
	/**
	 * Checks if the source node has grown so much, that it includes all the fields in the board
	 * @return true if the source node has grown so much, that it includes all the fields in the board, 
	 * 		   false else
	 */
	public boolean sourceNodeIsBoard() {
		if(this.sourceNode.getIncludedFields().size() == this.noColumns * this.noRows) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns graph's node the @param field is included in
	 * @param field - field to look for
	 * @return node, in which @param field is in
	 */
	public Node getNodeFieldIn(Field field) {
		for(Node node: this.nodes) {
			HashSet<Field> fields = node.getIncludedFields();
			if(fields.contains(field)) {
				return node;
			}
		}
		return null;
	}
	
	/**
	 * For the toBoard method of Testing class. Returns list of nodes, that are to be included to the 
	 * player's/computer's game component to match another board that is basis for @param graphToCompare
	 * @param source - true if player's game component, false if computer's game component
	 * @param graphToCompare - graph that has anotherBoard parameter from toBoard method of Testing class as basis board
	 * @return list of nodes, that are to be included to the 
	 * 		   player's/computer's game component to match another board that is basis for @param graphToCompare
	 */
	public ArrayList<Node> getNodesToInclude(boolean source, Graph graphToCompare){
		ArrayList<Node> nodesToInclude = new ArrayList<Node>();
		Node initialNode = new Node();
		Node nodeToCompare = new Node();
		if(source) {
			initialNode = this.sourceNode;
			nodeToCompare = graphToCompare.getSourceNode();
		}else {
			initialNode = this.compComponent;	
			nodeToCompare = graphToCompare.getCompComponent();			
		}		
		
		// Iterate through all the nodes in the graph and check, if they are included in the node to compare 
		for(Node node: this.nodes) {
			if(node != initialNode) {
				if(this.firstNodeIsIncludedInSecond(node, nodeToCompare)) {
					nodesToInclude.add(node);
					// Iterate through adjacent nodes of the node included in node to compare and update distances to them
					for(Node adjNode: node.getAdjacentNodes().keySet()) {
						if(!this.firstNodeIsIncludedInSecond(adjNode, nodeToCompare)) {
							int distance = (int)(node.getDistance()) + 1;
							adjNode.setDistance(distance);
						}
					}
				}				
			}
		}
		return nodesToInclude;
	}
	
	/**
	 * Checks if @param firstNode is fully included in @param secondNode
	 * @param firstNode - node that has to be included in another node
	 * @param secondNode - node that includes another node
	 * @return true if @param firstNode is fully included in @param secondNode, false else
	 */
	public boolean firstNodeIsIncludedInSecond(Node firstNode, Node secondNode) {
		@SuppressWarnings("unchecked")
		HashSet<Field> firstNodeFields = (HashSet<Field>) firstNode.getIncludedFields().clone();
		Field[]firstNodeFieldsArr = firstNodeFields.toArray(new Field[firstNodeFields.size()]);
		for(int fieldIx = 0; fieldIx < firstNodeFieldsArr.length; fieldIx++) {
			Field fieldInFirstNode = firstNodeFieldsArr[fieldIx];
			for(Field fieldInSecondNode: secondNode.getIncludedFields()) {				
				// If field is included in second node delete it from first node fields copy
				if(fieldInFirstNode.getRow() == fieldInSecondNode.getRow() && 
						fieldInFirstNode.getCol() == fieldInSecondNode.getCol()) {
					firstNodeFields.remove(fieldInFirstNode);
				}
			}
		}
		if(firstNodeFields.isEmpty()) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Checks if @param firstNode is equal to @param seondNode i.e. if they are both included in each other
	 * @param firstNode 
	 * @param secondNode
	 * @return true if @param firstNode is equal to @param seondNode, false else
	 */
	public boolean nodesAreEqual(Node firstNode, Node secondNode) {
		if(this.firstNodeIsIncludedInSecond(firstNode, secondNode) &&
				this.firstNodeIsIncludedInSecond(secondNode, firstNode)) {
			return true;
		}
		return false;
	}
	
	/*
	 * Getters and Setters
	 */
	public HashSet<Node> getNodes() {
		return nodes;
	}

	public void setNodes(HashSet<Node> nodes) {
		this.nodes = nodes;
	}

	public Node getSourceNode() {
		return sourceNode;
	}

	public void setSourceNode(Node sourceNode) {
		this.sourceNode = sourceNode;
	}

	public Node getCompComponent() {
		return compComponent;
	}

	public void setCompComponent(Node compComponent) {
		this.compComponent = compComponent;
	}

	public HashSet<Node> getSourceNodeAdjNodes() {
		return sourceNodeAdjNodes;
	}

	public void setSourceNodeAdjNodes(HashSet<Node> sourceNodeAdjNodes) {
		this.sourceNodeAdjNodes = sourceNodeAdjNodes;
	}

	public int getNoRows() {
		return noRows;
	}

	public void setNoRows(int noRows) {
		this.noRows = noRows;
	}

	public int getNoColumns() {
		return noColumns;
	}

	public void setNoColumns(int noColumns) {
		this.noColumns = noColumns;
	}

	public Field[][] getBoard() {
		return board;
	}

	public void setBoard(Field[][] board) {
		this.board = board;
	}

	public Testing getTest() {
		return test;
	}

	public void setTest(Testing test) {
		this.test = test;
	}
}