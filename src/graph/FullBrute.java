package graph;

import logic.Field;
import testing.Testing;

public class FullBrute {
	
	private Graph graph;
	
	private Testing test;

	public FullBrute(Field[][]board, Testing test) {
		this.test = test;
		this.graph = new Graph(board, test, true);
	}
	
	/**
	 * Returns minimum number of moves needed to paint the board completely in one color, i.e. include all the fields in player's component,
	 * assumed that only player moves, can choose computer's component color and may choose colors only sequentially and ascending 
	 * @return minimum number of moves needed to paint the board completely in one color, i.e. include all the fields in player's component
	 */
	public int getMinMovesToPaintBoard() {
		Integer minMoves = Integer.MAX_VALUE - 1;
		if(this.graph.sourceNodeIsBoard()) {
			return 0;
		}
		
		// Try every of six colors as first color for the player's component
		for(int tryColor = 0; tryColor < 6; tryColor++) {
			Graph newGraph = new Graph(this.test.getBoard(), this.test, true);
			
			int move = 0;
			
			// Choose colors sequentially and ascending until the whole board is included in the player's component
			for(int color = tryColor; !newGraph.sourceNodeIsBoard(); color++, move++) {
				newGraph.extendSourceNode(color%6 + 1);
			}
			// Update minimum number of moves needed to paint the board completely in one color, i.e. include all the fields in player's component
			if(move < minMoves) {
				minMoves = move;
			}
		}
		return minMoves;
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

	public Testing getTest() {
		return test;
	}

	public void setTest(Testing test) {
		this.test = test;
	}

}
