package graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import logic.Field;
import testing.Testing;

public class Compare {
	// Initial graph
	private Graph firstGraph;
	// Graph to compare the initial graph to
	private Graph secondGraph;
	
	// Number of moves player has
	private int movesPlayer;
	// Number of moves computer has
	private int movesComp;
	
	/**
	 * Default constructor, that builds graphs from boards parameters and sets number of moves for player 
	 * and computer based on @param moves. It is assumed that player goes first
	 * @param firstBoard - initial board
	 * @param secondBoard - board to compare initial board to
	 * @param moves - number of moves for player and computer together
	 * @param test - instance of the test class, used for Graph constructor
	 */
	public Compare(Field[][]firstBoard, Field[][]secondBoard, int moves, Testing test) {
		this.firstGraph = new Graph(firstBoard, test, false);
		// Set all the distances from source node (i.e. player's game component) in initial graph to 1, as here player can choose any colors
		for(Node sourceAdjNode: this.firstGraph.getSourceNodeAdjNodes()) {
			int distance = 1;
			this.firstGraph.getSourceNode().addDestination(sourceAdjNode, distance);
		}
		this.secondGraph = new Graph(secondBoard, test, false);
		// Set all the distances from source node (i.e. player's game component) in graph to compare to to 1, as here player can choose any colors
		for(Node sourceAdjNode: this.secondGraph.getSourceNodeAdjNodes()) {
			int distance = 1;
			this.secondGraph.getSourceNode().addDestination(sourceAdjNode, distance);
		}
		// Player moves first, so if the number of moves is odd add the spare move to number of moves for player
		this.movesPlayer = moves / 2 + moves % 2;
		this.movesComp = moves / 2;
	}
	
	/**
	 * Checks if initial board (graph) can be transformed into a board (graph) to compare in given number of moves
	 * @return true, if initial board (graph) can be transformed into a board (graph) to compare in given number of moves, false else
	 */
	public boolean compareBoards() {
		// If configuration of board's to compare violates the rule of the game that player's and computer's component must have 
		// different colors return false
		if(this.secondGraph.getSourceNode().getColor() == this.secondGraph.getCompComponent().getColor()) {
			return false;
		}
		
		// Player's and computer's component included fields
		HashSet<Field> sourceFieldsFirst = this.firstGraph.getSourceNode().getIncludedFields();
		HashSet<Field> sourceFieldsSecond = this.secondGraph.getSourceNode().getIncludedFields();
		HashSet<Field> compFieldsFirst = this.firstGraph.getCompComponent().getIncludedFields();
		HashSet<Field> compFieldsSecond = this.secondGraph.getCompComponent().getIncludedFields();
		
		// Initial board player's component upper most row and left most column
		int maxRowSource = this.firstGraph.getSourceNode().getMaxRow();
		int maxColumnSource = this.firstGraph.getSourceNode().getMaxColumn();
		
		// Check if there is a field in initial board player's component that is not included in board to compare 
		// player's component and if yes return false, because no fields can be excluded from component
		for(int row = 0; row < maxRowSource; row++) {
			for(int column = 0; column < maxColumnSource; column++) {
				if(sourceFieldsFirst.contains(this.firstGraph.getBoard()[row][column]) &&
						!sourceFieldsSecond.contains(this.secondGraph.getBoard()[row][column])) {
					return false;
				}
			}
		}
		
		// Initial board computer's component upper most row and left most column
		int maxRowComp = this.firstGraph.getCompComponent().getMaxRow();
		int maxColumnComp = this.firstGraph.getCompComponent().getMaxColumn();
		
		// Check if there is a field in initial board computer's component that is not included in board to compare 
		// computer's component and if yes return false, because no fields can be excluded from component
		for(int row = 0; row < maxRowComp; row++) {
			for(int column = 0; column < maxColumnComp; column++) {
				if(compFieldsFirst.contains(this.firstGraph.getBoard()[row][column]) &&
						!compFieldsSecond.contains(this.secondGraph.getBoard()[row][column])) {
					return false;
				}
			}
		}

		// Calculate the shortest paths from player's component node to all other nodes in the initial graph
		Dijkstra dijkstra = new Dijkstra(this.firstGraph);
		
		this.firstGraph = dijkstra.calculateShortestPathFromSource(this.firstGraph, this.firstGraph.getSourceNode());
		HashSet<Integer> adjColorsSource = new HashSet<>();
		ArrayList<Node> nodesToIncludePlayer = this.firstGraph.getNodesToInclude(true, this.secondGraph);
		
		// Sort List of node to include in player's component ascending based on distances and nodes' sizes
		nodesToIncludePlayer.sort(Comparator.comparing(Node::getDistance).thenComparing(Node::getSize));
		if(nodesToIncludePlayer.size() > 0) {
			for(Node node: nodesToIncludePlayer) {
				// If distance to one of the nodes to included is more, than moves for player return false
				if(node.getDistance() > this.movesPlayer) {
					return false;
				}
				// If any of node to be included adjacent nodes that is not to be included to player's component has the color
				// that was used to get to the node to include, that means that the adjacent node should also be the part of the 
				// player's component and if it is not return false
				for(Node adjNode: node.getAdjacentNodes().keySet()) {
					if(!nodesToIncludePlayer.contains(adjNode)) {
						adjColorsSource.add(adjNode.getColor());
						if(adjColorsSource.contains(node.getColor())) {
//								System.out.println("node " + node.getMaxRow() + " " + node.getMaxColumn());
//								System.out.println("adjColorsSource "+adjColorsSource);
//								System.out.println("node.getUsedColorsToGetToNode() "+node.getUsedColorsToGetToNode());
							return false;
						}
					}
				}
	
			}
			
			// Get the moves needed to included all the nodes that are to be included to the player's component
			int movesPlayer = this.movesToIncludeNodes(nodesToIncludePlayer);
			// If the farthest node to included doesn't have the same color as player's component from board to compare 
			// that means that one more move is needed to transform the player's component from initial board to player's 
			// component from board to compare
			if(nodesToIncludePlayer.get(nodesToIncludePlayer.size() - 1).getColor() != this.secondGraph.getSourceNode().getColor()) {
				movesPlayer++;
			}
			// If more moves are needed to include all the fields in player's component then moves for player return false
			if(movesPlayer > this.movesPlayer) {
				return false;
			}
		}

		// Calculate the shortest paths from computer's component node to all other nodes in the initial graph
		this.firstGraph = dijkstra.calculateShortestPathFromSource(this.firstGraph, this.firstGraph.getCompComponent());
		ArrayList<Node> nodesToIncludeComp = this.firstGraph.getNodesToInclude(false, this.secondGraph);

		// Sort List of node to include in computer's component ascending based on distances and nodes' sizes
		nodesToIncludeComp.sort(Comparator.comparing(Node::getDistance).thenComparing(Node::getSize));
		if(nodesToIncludeComp.size() > 0) {
			for(Node node: nodesToIncludeComp) {
				// If distance to one of the nodes to included is more, than moves for computer return false
				if(node.getDistance() > this.movesComp) {
					return false;
				}
				// If any of node to be included adjacent nodes that is not to be included to computer's component has the color
				// that was used to get to the node to include, that means that the adjacent node should also be the part of the 
				// computer's component and if it is not return false	
				for(Node adjNode: node.getAdjacentNodes().keySet()) {
					for(Node adjNodeToCompare: node.getAdjacentNodes().keySet()) {
						if(adjNode.getColor() == adjNodeToCompare.getColor()
								&& (nodesToIncludeComp.contains(adjNode) && !nodesToIncludeComp.contains(adjNodeToCompare)
										&&(adjNode.getDistance() == adjNodeToCompare.getDistance()))) {
							return false;
						}
					}
				}
			}

			// Get the moves needed to included all the nodes that are to be included to the computer's component
			int movesComp = this.movesToIncludeNodes(nodesToIncludeComp);
			// If the farthest node to included doesn't have the same color as computer's component from board to compare 
			// that means that one more move is needed to transform the computer's component from initial board to computer's 
			// component from board to compare
			if(nodesToIncludeComp.get(nodesToIncludeComp.size() - 1).getColor() != this.secondGraph.getCompComponent().getColor()) {
				movesPlayer++;
			}
			// If more moves are needed to include all the fields in computer's component then moves for computer return false
			if(movesComp > this.movesComp) {
				return false;
			}
		}
		
		Field[][]boardFirstGraph = this.firstGraph.getBoard();
		Field[][]boardSecondGraph = this.secondGraph.getBoard();
		
		// Iterate through all the fields in board to compare, that are not in player's or computer's component
		// and check, if their color is identical to their color in the initial board and if not return false
		for(int row = 0; row < this.secondGraph.getNoRows(); row++) {
			for(int column = 0; column < this.secondGraph.getNoColumns(); column++) {
				Field field = boardSecondGraph[row][column];
				Node nodeFieldIn = this.secondGraph.getNodeFieldIn(field);
				if(nodeFieldIn != this.secondGraph.getSourceNode() &&
						nodeFieldIn != this.secondGraph.getCompComponent()) {
					if(field.getColor() != boardFirstGraph[row][column].getColor()) {
//						System.out.println("row " + row + " col " + column);
//						System.out.println("field.getColor() "+field.getColor());
//						System.out.println("boardFirstGraph[row][column].getColor() " + boardFirstGraph[row][column].getColor());
						return false;
					}
				}
			}
		}
		
		// Check if there were nodes that were not completely included in either of the components and if yes return false
		// because the nodes i.e. neighboring fields with the same color cannot be included separately
		int fieldsToIncludePlayer = this.firstGraph.getSourceNode().getIncludedFields().size();
		int fieldsToIncludeComp = this.firstGraph.getCompComponent().getIncludedFields().size();
		
		for(Node nodePlayer: nodesToIncludePlayer) {
			fieldsToIncludePlayer += nodePlayer.getIncludedFields().size();
		}
		
		for(Node nodeComp: nodesToIncludeComp) {
			fieldsToIncludeComp += nodeComp.getIncludedFields().size();
		}
		
		if(fieldsToIncludePlayer < this.secondGraph.getSourceNode().getIncludedFields().size() ||
				fieldsToIncludeComp < this.secondGraph.getCompComponent().getIncludedFields().size()) {
			return false; 
		}
		
		return true;
	}
	
	/**
	 * Calculates the number of moves needed to include all the nodes from @param nodesToInclude in the player's/computer's component
	 * @param nodesToInclude - nodes to be included with distances set to minimum distance from player's/computer's component and sorted
	 * 						   ascending based on distance and nodes' size
	 * @return the number of moves needed to include all the nodes from @param nodesToInclude in the player's/computer's component
	 */
	public int movesToIncludeNodes(ArrayList<Node> nodesToInclude) {
		int moves = 0;
		HashSet<Integer> ixToIgnore = new HashSet<>();
		
		for(int nodeIx = 0; nodeIx < nodesToInclude.size(); nodeIx++) {	
			if(!ixToIgnore.contains(nodeIx)) {
				Node node = nodesToInclude.get(nodeIx);
				for(int nodeToCompareIx = nodeIx + 1; nodeToCompareIx < nodesToInclude.size(); nodeToCompareIx++) {
					Node nodeToCompare = nodesToInclude.get(nodeToCompareIx);
					if(node.getColor() == nodeToCompare.getColor()) {
						ixToIgnore.add(nodeToCompareIx);
					}
				}
				moves++;
			}			
		}
		return moves;
	}
	
	/*
	 * Getters and setters
	 */
	public Graph getFirstGraph() {
		return firstGraph;
	}
	public void setFirstGraph(Graph firstGraph) {
		this.firstGraph = firstGraph;
	}
	public Graph getSecondGraph() {
		return secondGraph;
	}
	public void setSecondGraph(Graph secondGraph) {
		this.secondGraph = secondGraph;
	}

	public int getMovesPlayer() {
		return movesPlayer;
	}

	public void setMovesPlayer(int movesPlayer) {
		this.movesPlayer = movesPlayer;
	}

	public int getMovesComp() {
		return movesComp;
	}

	public void setMovesComp(int movesComp) {
		this.movesComp = movesComp;
	}
}
