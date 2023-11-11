package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import logic.Logic;

public class MainFrame extends JFrame {
	
	private int noRows;
	private int noColumns;
	private int noColors;
	
	private GridBagLayout gbl = new GridBagLayout();
	private GridBagConstraints gbc = new GridBagConstraints();
	
	private DisplayPanel displayPane;
	private MenuPanel menuPane;
	private JLabel resultLabel;
	
	public MainFrame() {	
		// Define look and feel via UIManager
		UIManager.put("Button.background", Color.WHITE);
		UIManager.put("ToggleButton.background", Color.WHITE);
		
		// Initialize and set layout
		this.gbl.columnWidths = new int[]{450, 150};
		this.gbl.columnWeights = new double[]{1, 0};
		this.gbl.rowHeights = new int[]{150, 450};
		this.gbl.rowWeights = new double[]{0, 1};
		this.setLayout(this.gbl);
        
		// Panel with game statistics
        GameStatPanel gameStatPane = new GameStatPanel();
        this.gbc.gridx = this.gbc.gridy = 0;
		this.gbc.gridwidth = this.gbc.gridheight = 1;
		this.gbc.fill = GridBagConstraints.BOTH;
		this.gbc.insets = new Insets(5, 0, 5, 0);
        this.add(gameStatPane, this.gbc);
        
        // Menu panel
		this.menuPane = new MenuPanel(gameStatPane, this);
		this.gbc.gridy = 0;
		this.gbc.gridx = 1;
		this.gbc.gridheight = 2;
		this.add(this.menuPane, this.gbc);
				
		// Set important window features
		this.setPreferredSize(new Dimension(616, 640));
		this.setMinimumSize(new Dimension(616, 640));
        this.setLocationRelativeTo(null);
        this.setResizable(true);
		this.setVisible(true);
        this.setDefaultCloseOperation(3);

		this.pack();
		
		// Set current instances in Logic class
		Logic.setGameStatInst(gameStatPane);
		Logic.setMainFrameInst(this);
		Logic.setMenuPanelInst(this.menuPane);
	}
	
	/**
	 * Shows and hides board
	 * @param display - if true show board, else hide
	 * @param message - String with result of the game
	 */
	public void displayBoard(boolean display, String message) {
		// Show board
		if(display) {
			// If the result was shown delete it
			if(this.resultLabel != null) {
				this.remove(this.resultLabel);
				this.resultLabel = null; 
			}
			
			// Initialize and add DisplayPanel with board to the layout
			this.displayPane = new DisplayPanel();
			
	        this.gbc.gridy = 1;
	        this.gbc.gridx = 0;
	        this.gbc.fill = GridBagConstraints.BOTH;
	        this.add(this.displayPane, this.gbc);
	        
	        // Set window's focus to the board for KeyListener to function
			this.displayPane.setFocusable(true);
			this.displayPane.requestFocusInWindow();
	    
		// Hide board and show result   
		}else {
			this.remove(this.displayPane);
			
			this.gbc.gridy = 1;
	        this.gbc.gridx = 0;
	        this.gbc.fill = GridBagConstraints.BOTH;
			
	        this.resultLabel = new JLabel(message);
	        this.resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
	        this.resultLabel.setFont(new Font("SansSerif", Font.PLAIN, 30));
	        this.add(this.resultLabel, this.gbc);
		}
		this.revalidate();
		this.repaint();
	}

	/*
	 * Getters and Setters
	 */
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

	public int getNoColors() {
		return noColors;
	}

	public void setNoColors(int noColors) {
		this.noColors = noColors;
	}

	public GridBagLayout getGbl() {
		return gbl;
	}

	public void setGbl(GridBagLayout gbl) {
		this.gbl = gbl;
	}

	public GridBagConstraints getGbc() {
		return gbc;
	}

	public void setGbc(GridBagConstraints gbc) {
		this.gbc = gbc;
	}

	public DisplayPanel getDisplayPane() {
		return displayPane;
	}

	public void setDisplayPane(DisplayPanel displayPane) {
		this.displayPane = displayPane;
	}

	public MenuPanel getMenuPane() {
		return menuPane;
	}

	public void setMenuPane(MenuPanel menuPane) {
		this.menuPane = menuPane;
	}

	public JLabel getResultLabel() {
		return resultLabel;
	}

	public void setResultLabel(JLabel resultLabel) {
		this.resultLabel = resultLabel;
	}
}