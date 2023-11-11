package gui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import logic.*;

public class DisplayPanel extends JPanel implements KeyListener{
	
	private Field[][]board;
	private int noRows;
	private int noColumns;
	
	public DisplayPanel() {
		// Get number of rows and columns from Logic class
		this.noRows = Logic.getNoRows();
		this.noColumns = Logic.getNoColumns();
		
		// Initialize layout and layout constraints
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		// Set layout of the Display Panel
		this.setLayout(layout);
		this.setSize(new Dimension(400, 400));
		
		// Initialize board
		this.board = Logic.initBoard();

		// Iterate through the board and add every field to the layout
		for (int row = 0; row < this.noRows; row++) {
			for (int column = 0; column < this.noColumns; column++) {
				// Set x-position to column and y-position to row
				gbc.gridx = column;
				gbc.gridy = row;
				Field field = this.board[row][column];
				this.add(field, gbc);
			}
		}
		
		// Add key listener to enable player to choose new color with keyboard
		this.addKeyListener(this);
	}

	/**
	 * KeyListener interface method to handle key event, 
	 * i.e. to enable player to choose next color with keyboard
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// Character typed by the user
        char color = e.getKeyChar();
		
		// If the character is not a number exit the function
		if(!Character.isDigit(color)) return;
		// If the character is not in the allowed range (0 < color <= number of colors in game) exit the function
		if(Character.getNumericValue(color) == 0) return;
		if(Character.getNumericValue(color) > Logic.getNoColors()) return;

		Logic.move(Character.getNumericValue(color));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
	}
	
	/*
	 * Getters and Setters
	 */

	public Field[][] getBoard() {
		return board;
	}

	public void setBoard(Field[][] board) {
		this.board = board;
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
}
