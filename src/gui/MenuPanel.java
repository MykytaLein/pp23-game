package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import javax.swing.SwingConstants;
import javax.swing.JSlider;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JOptionPane;

import logic.CompLogic;
import logic.Logic;

public class MenuPanel extends JPanel implements ActionListener, ChangeListener{
	// Help String
	private String helpString = "To generate new board press Start/Stop button.\n"
					+ "To hide it and stop the game press it one more time.\n"
					+ "\n"
					+ "To start the game press Play/Pause button. Press it\n"
					+ "one more time to pause the game.\n"
					+ "\n"
					+ "To choose new color press the corresponding field\n"
					+ "on the board, on game stats panel or the number\n"
					+ "on the keyboard, that corresponds to the color\n"
					+ "number displayed above the board.";
	// Sliders
	private JSlider colorNumberSlider = new JSlider(JSlider.HORIZONTAL, 4, 9, 5);
	private JSlider columnNumberSlider = new JSlider(JSlider.HORIZONTAL, 3, 10, 6);
	private JSlider rowNumberSlider = new JSlider(JSlider.HORIZONTAL, 3, 10, 6);
	
    // Start/Stop and Play/Pause buttons
	private JButton playButton = new JButton("Play");
	private JButton startButton = new JButton("Start");
	
	// First turn buttons
    private JToggleButton playerButton = new JToggleButton("Player");
    private JToggleButton compButton = new JToggleButton("Computer");
    
    // Computer algorithm depth slider
	private JSlider depthSlider = new JSlider(JSlider.HORIZONTAL, 1, 7, 5);
    
    // Other component instances
    private GameStatPanel gameStatPaneInst;
	private MainFrame mainFrameInst;
	
	public MenuPanel(GameStatPanel gameStatPaneInst, MainFrame mainFrameInst) {
		// Set other component instances
		this.gameStatPaneInst= gameStatPaneInst;
		this.mainFrameInst = mainFrameInst;
		
		// Initialize and set layout
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbl.columnWidths = new int[]{200};
		gbl.columnWeights = new double[]{1};
		gbl.rowHeights = new int[]{30, 10, 60, 10, 60, 10, 60, 70, 70, 10, 50, 10, 50, 60};
		gbl.rowWeights = new double[]{0.05, 
				0.0167, 0.1, 0.0167, 0.1, 0.0167, 0.1,
				0.1167, 0.1167,
				0.0167, 0.0833, 
				0.0167, 0.0833, 0,1};
		this.setLayout(gbl);
        
        gbc.gridx = gbc.gridy = 0;
        gbc.gridwidth = gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.PAGE_START;
        
     	// Help button
		JButton helpButton = new JButton("Help");
        helpButton.setVisible(true);        
        helpButton.setActionCommand("HelpButton");
        helpButton.addActionListener(this);
		this.add(helpButton, gbc);
		
		// Colors, columns and rows sliders
		this.colorNumberSlider.setName("Color");
		this.columnNumberSlider.setName("Column");
		this.rowNumberSlider.setName("Row");

		this.colorNumberSlider.addChangeListener(this);
		this.columnNumberSlider.addChangeListener(this);
		this.rowNumberSlider.addChangeListener(this);
		
		this.colorNumberSlider.setMajorTickSpacing(1);
		this.columnNumberSlider.setMajorTickSpacing(1);
		this.rowNumberSlider.setMajorTickSpacing(1);
		
		this.colorNumberSlider.setPaintTicks(true);
		this.colorNumberSlider.setPaintLabels(true);
		this.columnNumberSlider.setPaintTicks(true);
		this.columnNumberSlider.setPaintLabels(true);
		this.rowNumberSlider.setPaintTicks(true);
		this.rowNumberSlider.setPaintLabels(true);
		
		Font fontForSliders = new Font("SansSerif", Font.PLAIN, 10);
		
		this.colorNumberSlider.setFont(fontForSliders);
		this.columnNumberSlider.setFont(fontForSliders);
		this.rowNumberSlider.setFont(fontForSliders);
		
		JLabel colorNumberLabel = new JLabel("Number of colors:");
		colorNumberLabel.setFont(fontForSliders);
		
		JLabel columnNumberLabel = new JLabel("Number of columns:");
		columnNumberLabel.setFont(fontForSliders);
		
		JLabel rowNumberLabel = new JLabel("Number of rows:");
		rowNumberLabel.setFont(fontForSliders);
		
		gbc.gridy = 1;
		this.add(colorNumberLabel, gbc);
		gbc.gridy = 2;
		this.add(this.colorNumberSlider, gbc);
		
		gbc.gridy = 3;
		this.add(columnNumberLabel, gbc);
		gbc.gridy = 4;
		this.add(this.columnNumberSlider, gbc);
		
		gbc.gridy = 5;
		this.add(rowNumberLabel, gbc);
		gbc.gridy = 6;
		this.add(this.rowNumberSlider, gbc);
		
	    
	    // Start button
 		this.startButton.setActionCommand("StartButton");
 		this.startButton.addActionListener(this);
 		
 		gbc.gridy = 7;
 		this.add(this.startButton, gbc);
 		
 		// Play/Pause button
        this.playButton.setActionCommand("PlayButton");
        this.playButton.addActionListener(this);
        this.playButton.setEnabled(false);
        
        gbc.gridy = 8;     
 		this.add(this.playButton, gbc);
 		
 		// First turn setting       
        JLabel turnLabel = new JLabel("FIRST TURN");
        turnLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        this.playerButton.setSelected(true);
        this.playerButton.setActionCommand("PlayerButton");
        this.playerButton.addActionListener(this);
		
        this.compButton.setActionCommand("CompButton");
        this.compButton.addActionListener(this);
        
        ButtonGroup turnButtonGroup = new ButtonGroup();
	    turnButtonGroup.add(this.playerButton);
	    turnButtonGroup.add(this.compButton);
	    
	    JPanel turnButtonPane = new JPanel(new GridLayout(1, 2));
	    turnButtonPane.add(this.playerButton);
	    turnButtonPane.add(this.compButton);
	    
	    gbc.gridy = 9;
	    this.add(turnLabel, gbc);
	    
	    gbc.gridy = 10;
	    this.add(turnButtonPane, gbc);
	    
	    // Depth setting
        JLabel strategyLabel = new JLabel("COMPUTER STRATEGY");
        strategyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        gbc.gridy = 11;
	    this.add(strategyLabel, gbc);

		this.depthSlider.setName("Depth");
		this.depthSlider.addChangeListener(this);
		this.depthSlider.setMajorTickSpacing(1);
		this.depthSlider.setPaintTicks(true);
		this.depthSlider.setPaintLabels(true);

		this.depthSlider.setFont(fontForSliders);
		
		JLabel depthLabel = new JLabel("Depth of best move search algorithm:");
		depthLabel.setFont(fontForSliders);
		
		gbc.gridy = 12;
		this.add(depthLabel, gbc);
		gbc.gridy = 13;
		this.add(this.depthSlider, gbc); 
		
        // Size and visibility variables of the menu panel
		this.setPreferredSize(new Dimension(203,616));
		this.setVisible(true);
	}
	
	/**
	 * ActionListener interface method to handle button clicks
	 */
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();

		switch(action){
			case "PlayButton":
				// Play or pause the game
				this.changeSettingsButtonsState(false);
				this.gameStatPaneInst.displayColorsInGame();
				break;
			case "HelpButton":
				// Show help
				JOptionPane.showMessageDialog(null, this.helpString, "Help", JOptionPane.PLAIN_MESSAGE);
				break;
			case "StartButton":
				// Start or stop the game
				this.changeSettingsButtonsState(true);
				break;
			case "PlayerButton":
				// Set player to make the first turn
				Logic.setPlayerGoesFirst(true);
				break;
			case "CompButton":
				// Set computer to make the first turn
				Logic.setPlayerGoesFirst(false);
				break;
			default:
				System.out.println("WHATTA HECK, WE DON'T HAVE ANY OTHER BUTTONS");
		}
	}
	
	/**
	 * Auxiliary method to handle Start/Stop and Play/Pause buttons click events
	 * @param startButton - boolean true if Start/StopButton, false if Play/Pause button
	 */
	public void changeSettingsButtonsState(boolean startButton) {
		// If start button
		if (startButton) {
			// If depth slider and/or turn buttons are disabled enable them 
			if(!this.playerButton.isEnabled()) {
				this.playerButton.setEnabled(true);
				this.compButton.setEnabled(true);
				this.depthSlider.setEnabled(true);
			}
			// If colors, columns and rows sliders are en-/disabled, change start and play buttons states
			// and dis-/enable spinners
			if(this.colorNumberSlider.isEnabled()) {
				this.startButton.setText("Stop");
				this.mainFrameInst.displayBoard(true, "");
				// Disable spinners
				this.colorNumberSlider.setEnabled(false);
				this.columnNumberSlider.setEnabled(false);
				this.rowNumberSlider.setEnabled(false);
				// Enable Play button
				this.playButton.setEnabled(true);
				// Initialize Player's and Computer's components
				Logic.initComponents();
			}else {
				Logic.resetGame();
				this.startButton.setText("Start");
				// Enable spinners
				this.colorNumberSlider.setEnabled(true);
				this.columnNumberSlider.setEnabled(true);
				this.rowNumberSlider.setEnabled(true);
				// Disable Play button
				this.playButton.setText("Play");
				this.playButton.setEnabled(false);
				// Reset the timer
				this.gameStatPaneInst.resetTimer();
			}
			this.gameStatPaneInst.updatePlayerStats();

		// If play button
		}else {
			// Set focus on the board for KeyListener to function,
			// i.e. to enable player to choose a color with keyboard
			this.mainFrameInst.getDisplayPane().setFocusable(true);
			this.mainFrameInst.getDisplayPane().requestFocusInWindow();
			// Game logic function on play button click
			Logic.playButtonClicked();
			// If turn and strategy buttons are enabled
			if(this.depthSlider.isEnabled()) {
				this.playButton.setText("Pause");
				this.gameStatPaneInst.getTimer().start();
				
				this.playerButton.setEnabled(false);
				this.compButton.setEnabled(false);
				
				this.depthSlider.setEnabled(false);
			// If turn and strategy buttons are disabled
			}else {
				this.playButton.setText("Play");
				this.gameStatPaneInst.getTimer().stop();
				
				this.depthSlider.setEnabled(true);
				
				if(Logic.isFirstMove()) {
					this.playerButton.setEnabled(true);
					this.compButton.setEnabled(true);
				}
			}
		}
	}
	
	/**
	 * ChangeListener interface method to listen to sliders change and update game variables 
	 */
	public void stateChanged(ChangeEvent e) {
		// Get the slider, which value was changed
        JSlider slider = (JSlider)(e.getSource());
		int value = (int)slider.getValue();

        // Check which slider it was and update corresponding game variable    
		switch (slider.getName()){
			case "Color":
				Logic.setNoColors(value);
				// Display colors currently in game
				this.gameStatPaneInst.displayColorsInGame();
				break;
			case "Column":
        		Logic.setNoColumns(value);
				break;
			case "Row":
				Logic.setNoRows(value);
				break;
			case "Depth":
				CompLogic.setDepth(value);
				break;
			default:
				System.out.println("never");
		}
    }

	/*
	 * Getters and Setters
	 */
	public String getHelpString() {
		return this.helpString;
	}

	public void setHelpString(String helpString) {
		this.helpString = helpString;
	}

	public JSlider getColorNumberSlider() {
		return colorNumberSlider;
	}

	public void setColorNumberSlider(JSlider colorNumberSlider) {
		this.colorNumberSlider = colorNumberSlider;
	}

	public JSlider getColumnNumberSlider() {
		return columnNumberSlider;
	}

	public void setColumnNumberSlider(JSlider columnNumberSlider) {
		this.columnNumberSlider = columnNumberSlider;
	}

	public JSlider getRowNumberSlider() {
		return rowNumberSlider;
	}

	public void setRowNumberSlider(JSlider rowNumberSlider) {
		this.rowNumberSlider = rowNumberSlider;
	}
	
	public JButton getPlayButton() {
		return playButton;
	}

	public void setPlayButton(JButton playButton) {
		this.playButton = playButton;
	}

	public JButton getStartButton() {
		return startButton;
	}

	public void setStartButton(JButton startButton) {
		this.startButton = startButton;
	}

	public JToggleButton getPlayerButton() {
		return playerButton;
	}

	public void setPlayerButton(JToggleButton playerButton) {
		this.playerButton = playerButton;
	}

	public JToggleButton getCompButton() {
		return compButton;
	}

	public void setCompButton(JToggleButton compButton) {
		this.compButton = compButton;
	}

	public JSlider getDepthSlider() {
		return this.depthSlider;
	}

	public void setDepthSlider(JSlider depthSlider) {
		this.depthSlider = depthSlider;
	}	

	public GameStatPanel getGameStatPaneInst() {
		return gameStatPaneInst;
	}

	public void setGameStatPaneInst(GameStatPanel gameStatPaneInst) {
		this.gameStatPaneInst = gameStatPaneInst;
	}

	public MainFrame getMainFrameInst() {
		return mainFrameInst;
	}

	public void setMainFrameInst(MainFrame mainFrameInst) {
		this.mainFrameInst = mainFrameInst;
	}
}