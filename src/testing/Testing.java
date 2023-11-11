package testing;

import java.util.HashSet;

import graph.Dijkstra;
import graph.FullBrute;
import graph.Compare;
import logic.Field;

/*
 * Siehe Hinweise auf dem Aufgabenblatt. 
 */
/**
 * Class for testing
 * For every method it is assumed that the game takes place with six colors
 * @author Mykyta
 *
 */
public class Testing {

	private final int noRows;
	private final int noColumns;
	private Field[][] board;

	/**
	 * Default constructor
	 * @param initBoard - two dimensional array of fields, i.e., the board
	 */
	public Testing(Field[][] initBoard) {
		this.board = initBoard;
		
		this.noRows = this.initNoRows(this.board);
		this.noColumns = this.initNoColumns(this.board);
	}

	/**
	 * Checks if the board meets conditions for an initial board
	 * @return true if the board meets conditions for an initial board, false else
	 */
	public boolean isStartklar() {
		HashSet<Integer> unusedColors = new HashSet<Integer>();
		if(this.board[this.noRows - 1][0].getColor() == this.board[0][this.noColumns - 1].getColor()) return false;

		for(int color = 1; color <= 6; color++) unusedColors.add(color);
		
		for (int row = 0; row < this.noRows; row++) {
			for (int column = 0; column < this.noColumns; column++) {
				Field field = this.board[row][column];
				int color = field.getColor();
				unusedColors.remove(color);
				if(field.getRow() > 0) {
					Field fieldUp = this.board[field.getRow() - 1][field.getCol()];
					int colorUp = fieldUp.getColor();
					if(color == colorUp) return false;
				}
				if(field.getRow() < this.noRows - 1) {
					Field fieldDown = this.board[field.getRow() + 1][field.getCol()];
					int colorDown = fieldDown.getColor();
					if(color == colorDown) return false;
				}
				if(field.getCol() > 0) {
					Field fieldLeft = this.board[field.getRow()][field.getCol() - 1];
					int colorLeft = fieldLeft.getColor();
					if(color == colorLeft) return false;
				}
				if(field.getCol() < this.noColumns - 1) {
					Field fieldRight = this.board[field.getRow()][field.getCol() + 1];
					int colorRight = fieldRight.getColor();
					if(color == colorRight) return false;
				}
			}
		}

		if(!unusedColors.isEmpty()) return false;
		
		return true;
	}

	/**
	 * Checks if the board is in an end configuration 
	 * @return true if the board is in an end configuration, false else
	 */
	public boolean isEndConfig() {
		int playerSize = this.getComponent(this.board, this.noRows - 1, 0).size();
		int compSize = this.getComponent(this.board, 0, this.noColumns - 1).size();
	
		int boardSize = this.noColumns * this.noRows;
	
		if(playerSize + compSize == boardSize) return true;

		return false;
	}
	
	/**
	 * Returns a related game component (i.e. fields with the same color as field in @param initialRow @param initialColumn) as HashSet of fields
	 * @param board - two-dimensional array (board) with fields
	 * @param initialRow - row of initial field
	 * @param initialColumn - column of initial field
	 * @return related game component (i.e. fields with the same color as field in @param initialRow @param initialColumn) as HashSet of fields
	 */
	public HashSet<Field> getComponent(Field[][]board, int initialRow, int initialColumn){
		HashSet<Field> playerComponent = new HashSet<Field>();
		
		playerComponent.add(board[initialRow][initialColumn]);		
		
		int size = 0;
		while(size != playerComponent.size()) {
			Field[]compToArray = playerComponent.toArray(new Field[playerComponent.size()]);
			size = playerComponent.size();
			for(int fieldIx = 0; fieldIx < compToArray.length; fieldIx++) {
				Field field = compToArray[fieldIx];
				int color = field.getColor();
				if(field.getRow() > 0) {
					Field fieldUp = board[field.getRow() - 1][field.getCol()];
					// Even if the field is looked at twice it will be uniquely added to the result hashset
					if(fieldUp.getColor() == color) playerComponent.add(fieldUp);
				}
				if(field.getRow() < this.noRows - 1) {
					Field fieldDown = board[field.getRow() + 1][field.getCol()];
					// Even if the field is looked at twice it will be uniquely added to the result hashset
					if(fieldDown.getColor() == color) playerComponent.add(fieldDown);
				}
				if(field.getCol() > 0) {
					Field fieldLeft = board[field.getRow()][field.getCol() - 1];
					// Even if the field is looked at twice it will be uniquely added to the result hashset
					if(fieldLeft.getColor() == color) playerComponent.add(fieldLeft);
				}
				if(field.getCol() < this.noColumns - 1) {
					Field fieldRight = board[field.getRow()][field.getCol() + 1];
					// Even if the field is looked at twice it will be uniquely added to the result hashset
					if(fieldRight.getColor() == color) playerComponent.add(fieldRight);
				}
			}
		}
		return playerComponent;
	}
	
	/**
	 * Returns number of columns in @param board
	 * @param board - two-dimensional array (board) with fields
	 * @return number of columns in @param board
	 */
	public int initNoColumns(Field[][]board) {
		int maxColumn = 0;
		for (Field field: board[0]) {
			if(field.getCol() > maxColumn) {
				maxColumn = field.getCol();
			}
		}
		return maxColumn + 1;
	}

	/**
	 * Returns number of rows in @param board
	 * @param board - two-dimensional array (board) with fields
	 * @return number of rows in @param board
	 */
	public int initNoRows(Field[][]board) {
		int maxRow = 0;
		for (int i = 0; i < board.length; i++) {
			if(board[i][0].getRow() > maxRow) {
				maxRow = board[i][0].getRow();
			}
		}
		return maxRow + 1;
	}
	
	/**
	 * Returns player's/computer's component's current color
	 * @param player - if true returns color of player's component, else of computer's component
	 * @return player's/computer's component's current color
	 */
	public int getPlayerColor(boolean player, Field[][]board) {
		if(player) {
			return board[this.noRows - 1][0].getColor();
		}else {
			return board[0][this.noColumns - 1].getColor();
		}
	}
	
	/**
	 * Checks if the current board(i.e. this.board) is transformable into @param anotherBoard
	 * in @param moves number of moves
	 * @param anotherBoard - two-dimensional array (board) to compare
	 * @param moves - number of moves
	 * @return true if the current board(i.e. this.board) is transformable into @param anotherBoard
	 * 		   in @param moves number of moves, false else
	 */
	public boolean toBoard(Field[][] anotherBoard, int moves) {
		if(this.initNoColumns(anotherBoard) != this.noColumns||
				this.initNoRows(anotherBoard) != this.noRows) {
			return false;
		}
		Compare compare = new Compare(this.board, anotherBoard, moves, this);
		return compare.compareBoards();
	}

	/**
	 * Returns minimum number of moves needed to include the field in @param row and @param column
	 * to players component, assuming that:
	 * 	1. only player makes moves
	 * 	2. only first color chosen may vary
	 * 	3. player may choose colors only ascending and sequentially, i.e. if he starts with 1 it should 
	 * 	   choose 2 as next color and so on up to 6 and then again 1 and so on
	 * @param row - row of field to include
	 * @param col - column of field to include
	 * @return minimum number of moves needed to include the field in @param row and @param column
	 * 		   in players component
	 */
	public int minMoves(int row, int col) { 
		if(row >= this.noRows || col >= this.noColumns) {
			return Integer.MAX_VALUE - 1;
		}
		Dijkstra dijkstra = new Dijkstra(this.board, this, true);
		return dijkstra.getShortestPathToField(row, col);
	}

	/**
	 * Returns the minimum number of moves needed to paint the whole board (i.e. this.board) in one color, assuming that: 
	 * 	1. only player makes moves
	 * 	2. only first color chosen may vary
	 * 	3. player may choose colors only ascending and sequentially, i.e. if he starts with 1 it should 
	 * 	   choose 2 as next color and so on up to 6 and then again 1 and so on	 * 
	 * @return minimum number of moves needed to paint the whole board (i.e. this.board) in one color
	 */
	public int minMovesFull() {
		FullBrute full = new FullBrute(this.board, this);
		return full.getMinMovesToPaintBoard();
	} 
	
	/*
	 * Getters and Setters
	 */

	public int getNoRows() {
		return noRows;
	}

	public int getNoColumns() {
		return noColumns;
	}
	
	public Field[][] getBoard() {
		return board;
	}

	public void setBoard(Field[][] board) {
		this.board = board;
	}
}
