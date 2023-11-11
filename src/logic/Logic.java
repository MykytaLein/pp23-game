package logic;

import java.awt.Color;
import java.util.HashMap;
import java.util.Random;

import gui.GameStatPanel;
import gui.MainFrame;
import gui.MenuPanel;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import testing.Testing;

public abstract class Logic {
	// All colors with their identifiers as keys in a HashMap
	private static final HashMap<Integer, Color> colors = new HashMap<Integer, Color>(
			Map.ofEntries(
					new AbstractMap.SimpleEntry<Integer, Color>(1, Color.decode("#e6194b")),
					new AbstractMap.SimpleEntry<Integer, Color>(2, Color.decode("#f58231")),
					new AbstractMap.SimpleEntry<Integer, Color>(3, Color.decode("#ffe119")),
					new AbstractMap.SimpleEntry<Integer, Color>(4, Color.decode("#bfef45")),
					new AbstractMap.SimpleEntry<Integer, Color>(5, Color.decode("#3cb44b")),
					new AbstractMap.SimpleEntry<Integer, Color>(6, Color.decode("#42d4f4")),
					new AbstractMap.SimpleEntry<Integer, Color>(7, Color.decode("#4363d8")),
					new AbstractMap.SimpleEntry<Integer, Color>(8, Color.decode("#911eb4")),
					new AbstractMap.SimpleEntry<Integer, Color>(9, Color.decode("#f032e6"))));

	// Variables from menu panel
	private static boolean playerGoesFirst = true;
	private static boolean firstMove = true;
	
	private static int noRows = 6;
	private static int noColumns = 6;
	private static int noColors = 5;
	
	private static Field[][] currentBoard;
	private static HashSet<Field> playerComponent = new HashSet<Field>();
	private static HashSet<Field> compComponent = new HashSet<Field>();
	
	// True if player can make move, false else
	private static boolean played;
	
	// Other instances
	private static GameStatPanel gameStatInst;
	private static MainFrame mainFrameInst;
	private static MenuPanel menuPanelInst;
	
	// Stack with last 4 moves
	private static SizedStack<Boolean> lastMoves = new SizedStack<Boolean>(4);
	
	/**
	 * Initializes board
	 * @return two-dimensional array of fields, where following is true:
	 * 	1. player's component in left down corner has different color then computer's component in right upper corner
	 * 	2. all the fields have neighbors (i.e. fields with the same edge) of different colors
	 * 	3. fields in the board use all the colors currently in game at least once 
	 */
	public static Field[][] initBoard() {
		// Initialize new two-dimensional array (board)
		Field[][] board = new Field[noRows][noColumns];
		// Colors that may not be used by initializing new field, as they are the new field's neighbor colors
		// Left and upper are enough, because the field is filled starting from the left uppermost edge 
		int exclusionColorLeft = 0;
		int exclusionColorTop[] = new int[noColumns];
		// Variable to remember computer' color
		int compColor = 0;
		// Define and initialize a list of the yet unused colors currently in game
		ArrayList<Integer> unusedColors = new ArrayList<Integer>();
		for(int i = 1; i <= Logic.noColors; i++) unusedColors.add(i);

		Random rnd = new Random();
		int color = 0;
		
		// Fill the board starting from the left uppermost field 
		for (int row = 0; row < noRows; row++) {
			for (int column = 0; column < noColumns; column++) {
				// If there are unused colors choose randomly one of them and delete it form unused colors list
				// All conditions for the board are met, because the colors in the list are unique
				if(!unusedColors.isEmpty()) {
					color = unusedColors.get(rnd.nextInt(unusedColors.size()));
					unusedColors.remove(Integer.valueOf(color));
					
				// If there are no unused colors choose randomly from all the colors currently in game
				// excluding colors of already initialized neighbors and if it is the field of player's
				// component also exclude computer's component's color (else exclude 0 as it is a-priori excluded)
				}else {
					boolean isPlayerField = (row == noRows - 1 && column == 0);
					color = Util.getRandomColWithExclusion(exclusionColorLeft, exclusionColorTop[column],
						isPlayerField ? compColor : 0); 					
				}

				// Remember the computer's component's color
				if(row == 0 && column == noColumns - 1) compColor = color;
				
				Field field = new Field(row, column, color);
				
				// Remember field's color for generating further fields
				exclusionColorLeft = color;
				exclusionColorTop[column] = color;
				board[row][column] = field;
			}
		}
		
		Logic.currentBoard = board;
		return board;
	}
	
	/**
	 * Returns player's/computer's component's current color
	 * @param player - if true returns color of player's component, else of computer's component
	 * @return player's/computer's component's current color
	 */
	public static int getPlayerColor(boolean player) {
		if(player) {
			return Logic.currentBoard[Logic.noRows - 1][0].getColor();
		}else {
			return Logic.currentBoard[0][Logic.noColumns - 1].getColor();
		}
	}
	
	/**
	 * Processes one move cycle including player's and computer's moves
	 * @param color - new color chosen by the player
	 */
	public static void move(int color) {
		if(Logic.validMove(color)) {
			Logic.updatePlayersComponent(color, true);
			if(Logic.currentBoard != null) {
				CompLogic compLogic = new CompLogic();
				compLogic.start();				
			}
		}
	}
	
	/**
	 * Checks if the player may move now choosing @param color as new color for its component
	 * @param color - color chosen by the player as a new color for its component
	 * @return true if move is valid, false else
	 */
	public static boolean validMove(int color) {
		if(!Logic.played) return false;
		if(color == Logic.getPlayerColor(true))  return false;
		if(color == Logic.getPlayerColor(false)) return false;
		return true;
	}
	
	/**
	 * Updates player's/computer's component
	 * @param color - new color for component
	 * @param player - if true update player's component, else update computer's component
	 */
	public static void updatePlayersComponent(int color, boolean player) {
		// After first move it's not the first move anymore
		if(Logic.firstMove) Logic.setFirstMove(false);

		HashSet<Field> comp;
		if(player) {
			comp = Logic.playerComponent;
		}else {
			comp = Logic.compComponent;
		}

		// Variable to check if the component has grown through the move or not
		int sizeBeforeMove = comp.size();
		// Create an array from initial game component, to avoid errors changing the component (adding new fields)
		Field[]compToArray = comp.toArray(new Field[comp.size()]);
		// Iterate through all the fields in component and if neighboring field's color corresponds to the component's new color
		// add the neighboring field to the game component
		for(int fieldIx = 0; fieldIx < compToArray.length; fieldIx++) {
			Field field = compToArray[fieldIx];
			// If field is not in the first row check its upper neighbor
			if(field.getRow() > 0) {
				Field fieldUp = Logic.currentBoard[field.getRow() - 1][field.getCol()];
				if(fieldUp.getColor() == color) comp.add(fieldUp);
			}
			// If field is not in the last row check its lower neighbor
			if(field.getRow() < Logic.noRows - 1) {
				Field fieldDown = Logic.currentBoard[field.getRow() + 1][field.getCol()];
				if(fieldDown.getColor() == color) comp.add(fieldDown);
			}
			// If field is not in the first column check its left neighbor
			if(field.getCol() > 0) {
				Field fieldLeft = Logic.currentBoard[field.getRow()][field.getCol() - 1];
				if(fieldLeft.getColor() == color) comp.add(fieldLeft);
			}
			// If field is not in the last column check its right neighbor
			if(field.getCol() < Logic.noColumns - 1) {
				Field fieldRight = Logic.currentBoard[field.getRow()][field.getCol() + 1];
				if(fieldRight.getColor() == color) comp.add(fieldRight);
			}
			field.setColor(color);
		}

		// Update game statistics
		Logic.gameStatInst.updatePlayerStats();
		
		// Check if the game component has grown and if no add true to stack, else add false
		if(player) {	
			boolean hasNotChangedSize = sizeBeforeMove == Logic.playerComponent.size();
			Logic.lastMoves.push(hasNotChangedSize);
		}else {
			boolean hasNotChangedSize = sizeBeforeMove == Logic.playerComponent.size();
			Logic.lastMoves.push(hasNotChangedSize);
		}
		
		// Check if the board is in end configuration and if yes end the game
		Testing test = new Testing(Logic.currentBoard);
		if (test.isEndConfig()) {
			Logic.menuPanelInst.changeSettingsButtonsState(true);
		}
		// Display colors in game to update choosable colors
		Logic.gameStatInst.displayColorsInGame();
	}
	
	/**
	 * Resets all the game variables and shows the result
	 */
	public static void resetGame() {
		String message = Logic.getGameResultMessage();
		
		Logic.mainFrameInst.displayBoard(false, message);
		Logic.setFirstMove(true);
		Logic.getLastMoves().clear();
		Logic.setPlayed(false);
		Logic.setCurrentBoard(null);
		// Clear Player's and Computer's components
		Logic.getPlayerComponent().clear();
		Logic.getCompComponent().clear();
	}

	/**
	 * Returns the game result message String 
	 */
	private static String getGameResultMessage(){
		int playerSize = Logic.playerComponent.size();
		int compSize = Logic.compComponent.size();

		if(playerSize + compSize != Logic.noColumns * Logic.noRows) return "GAME ENDED";
		if(playerSize > compSize) return "PLAYER WINS";
		if(playerSize < compSize) return "COMPUTER WINS";
		return "DRAW";
	}
	
	/**
	 * Handles play button click event
	 */
	public static void playButtonClicked() {
		Logic.played = !Logic.played;
		// System.out.println("played: " + Logic.played);
		// Computer makes first move
		if(!Logic.playerGoesFirst && Logic.firstMove) {
			CompLogic compLogic = new CompLogic();
			compLogic.start();
			Logic.firstMove = false;
		}
	}
	/**
	 * Initialize player's and computer's components, i.e., add lower left field to player's and upper right field to computer's components
	 */
	public static void initComponents() {
		Logic.playerComponent.add(Logic.currentBoard[Logic.noRows - 1][0]);
		Logic.compComponent.add(Logic.currentBoard[0][Logic.noColumns - 1]);
	}
	
	/*
	 * Getters and Setters
	 */
	public static HashMap<Integer, Color> getColors() {
		return colors;
	}

	public static boolean isPlayerGoesFirst() {
		return playerGoesFirst;
	}

	public static void setPlayerGoesFirst(boolean playerGoesFirst) {
		Logic.playerGoesFirst = playerGoesFirst;
	}

	public static boolean isFirstMove() {
		return firstMove;
	}

	public static void setFirstMove(boolean firstMove) {
		Logic.firstMove = firstMove;
	}

	public static int getNoRows() {
		return noRows;
	}

	public static void setNoRows(int noRows) {
		Logic.noRows = noRows;
	}

	public static int getNoColumns() {
		return noColumns;
	}

	public static void setNoColumns(int noColumns) {
		Logic.noColumns = noColumns;
	}

	public static int getNoColors() {
		return noColors;
	}

	public static void setNoColors(int noColors) {
		Logic.noColors = noColors;
	}

	public static Field[][] getCurrentBoard() {
		return currentBoard;
	}

	public static void setCurrentBoard(Field[][] currentBoard) {
		Logic.currentBoard = currentBoard;
	}

	public static HashSet<Field> getPlayerComponent() {
		return playerComponent;
	}

	public static void setPlayerComponent(HashSet<Field> playerComponent) {
		Logic.playerComponent = playerComponent;
	}

	public static HashSet<Field> getCompComponent() {
		return compComponent;
	}

	public static void setCompComponent(HashSet<Field> compComponent) {
		Logic.compComponent = compComponent;
	}

	public static boolean isPlayed() {
		return played;
	}

	public static void setPlayed(boolean played) {
		Logic.played = played;
	}

	public static GameStatPanel getGameStatInst() {
		return gameStatInst;
	}

	public static void setGameStatInst(GameStatPanel gameStatInst) {
		Logic.gameStatInst = gameStatInst;
	}

	public static MainFrame getMainFrameInst() {
		return mainFrameInst;
	}

	public static void setMainFrameInst(MainFrame mainFrameInst) {
		Logic.mainFrameInst = mainFrameInst;
	}

	public static MenuPanel getMenuPanelInst() {
		return menuPanelInst;
	}

	public static void setMenuPanelInst(MenuPanel menuPanelInst) {
		Logic.menuPanelInst = menuPanelInst;
	}

	public static SizedStack<Boolean> getLastMoves() {
		return lastMoves;
	}

	public static void setLastMoves(SizedStack<Boolean> lastMoves) {
		Logic.lastMoves = lastMoves;
	}
}
