package logic;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/*
 * Siehe Hinweise zu Umgang mit dem Repository auf Aufgabenblatt.  
 */

public class Field extends JPanel implements MouseListener{

	private int color;
	private int row;
	private int col;
	
	private boolean choosable;
	
	public Field() {
		
	}
	
	/**
	 * Default constructor 
	 * @param row - the row the field in
	 * @param col - the column the field in
	 * @param color - field's color
	 */
	public Field(int row, int col, int color) {
		this.row = row; 
		this.col = col; 
		this.color = color;  
		this.display();
	}
	
	/**
	 * Constructor for gameStatPanel to show the current color of player or computer
	 * @param player - true to create field with player's component's color, 
	 * 				   false to create one with computer's component's color
	 */
	public Field(boolean player) {
		this.color = Logic.getPlayerColor(player);
		this.display();
	}

	/**
	 * Displays field
	 */
	public void display() {
		this.addMouseListener(this);
		this.setBackground(Logic.getColors().get(this.color));
		this.setBorder(BorderFactory.createRaisedBevelBorder());
		this.setPreferredSize(this.getPreferredSize());
		this.setMinimumSize(new Dimension(30, 30));
	}

	/**
	 * MouseListener interface method to handle mouse click events,
	 * i.e. to enable user to choose a new color through clicking on field
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	    Field clickedField = (Field) e.getSource();
	    
	    Logic.move(clickedField.getColor());
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Getter and Setter
	 */	
	
	/**
	 * Keeps Field instances square
	 * Taken from https://stackoverflow.com/questions/16075022/making-a-jpanel-square
	 */
	@Override
    public Dimension getPreferredSize() {
		int noRows = Logic.getNoRows();
		int noColumns = Logic.getNoColumns();
        Dimension d = super.getPreferredSize();
        Container c = this.getParent();
        if (c != null) {
            d = c.getSize();
        } 
        int w = (int) (d.getWidth()/noColumns);
        int h = (int) (d.getHeight()/noRows);
        int s = (w < h ? w : h);
        s = (s < 25 ? 25 : s);
        return new Dimension(s, s);
    }
	
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
		this.setBackground(Logic.getColors().get(this.color));
		this.repaint();
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}
	
	public boolean isChoosable() {
		return choosable;
	}

	public void setChoosable(boolean isChoosable) {
		if(isChoosable) {
			this.setBorder(BorderFactory.createRaisedBevelBorder());
		}else {
			this.setBorder(BorderFactory.createLoweredBevelBorder());
		}
		this.choosable = isChoosable;
	}
}
