package gui;

import java.text.SimpleDateFormat;
import java.util.Date;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.Timer;

import logic.*;

public class GameStatPanel extends JPanel implements ActionListener, ComponentListener {
	// Timer variables
	private Timer timer = new Timer(100, this);
	private long timerValue;
	private SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
	private JLabel timerGui = new JLabel();
	
	// Layout constraints
	private GridBagConstraints gbc = new GridBagConstraints();
	
	// Game statistics panels to be update after each move
	private JPanel playerStats;
	private JPanel compStats;

	public GameStatPanel() {
		// Initialize layout for game statistic component
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 150, 100, 150 };
		gbl.columnWeights = new double[] { 0.375, 0.25, 0.375 };
		gbl.rowHeights = new int[] { 20, 50, 80 };
		this.setLayout(gbl);

		// Constraints for the layout
		this.gbc.insets = new Insets(10, 10, 10, 10);
		this.gbc.gridx = this.gbc.gridy = 0;
		this.gbc.gridwidth = this.gbc.gridheight = 1;
		this.gbc.fill = GridBagConstraints.BOTH;
		this.gbc.anchor = GridBagConstraints.WEST;

		// Headers for statistics
		JLabel player = new JLabel("Player stats:");
		player.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel timer = new JLabel("Timer:");
		timer.setHorizontalAlignment(SwingConstants.CENTER);
		JLabel computer = new JLabel("Computer stats:");
		computer.setHorizontalAlignment(SwingConstants.CENTER);

		this.add(player, this.gbc, 0);
		this.gbc.gridx = 1;
		this.add(timer, this.gbc, 1);
		this.gbc.gridx = 2;
		this.add(computer, this.gbc, 2);

		// Player statistics
		this.playerStats = new JPanel(new GridLayout(1, 2));
		Field playerCurrentColor = new Field();

		JLabel playerCurrentScore = new JLabel("0");
		playerCurrentScore.setHorizontalAlignment(SwingConstants.CENTER);
		this.playerStats.add(playerCurrentColor, 0);
		this.playerStats.add(playerCurrentScore, 1);

		// Computer statistics
		this.compStats = new JPanel(new GridLayout(1, 2));
		Field compCurrentColor = new Field();
		
		JLabel compCurrentScore = new JLabel("0");
		compCurrentScore.setHorizontalAlignment(SwingConstants.CENTER);
		this.compStats.add(compCurrentScore, 0);
		this.compStats.add(compCurrentColor, 1);

		// Initialize timer						
		this.timerGui.setText(this.sdf.format(new Date(0)));
		this.timerGui.setHorizontalAlignment(SwingConstants.CENTER);
		
		// Add player statistics to layout
		this.gbc.gridy = 1;
		this.gbc.gridx = 0;
		this.gbc.fill = GridBagConstraints.NONE;		
		this.gbc.anchor = GridBagConstraints.CENTER;
		this.add(this.playerStats, this.gbc, 3);
		
		// Add timer to layout
		this.gbc.gridx = 1;
		this.gbc.fill = GridBagConstraints.BOTH;
		this.add(this.timerGui, this.gbc, 4);

		// Add computer statistics to layout
		this.gbc.gridx = 2;
		this.gbc.fill = GridBagConstraints.NONE;
		this.add(this.compStats, this.gbc, 5);

		this.displayColorsInGame();
		this.addComponentListener(this);
	}
	
	/**
	 * Updates player's and computer's statistics
	 */
	public void updatePlayerStats() {
		// Declare player's and computer's color Fields
		// that show their respective current colors
		Field playerColorField;
		Field compColorField;

		// Initialize Labels with empty scores
		JLabel playerScore = new JLabel("0");
		JLabel compScore = new JLabel("0");
		playerScore.setHorizontalAlignment(SwingConstants.CENTER);
		compScore.setHorizontalAlignment(SwingConstants.CENTER);

		// Check if board was already initialized 
		if (Logic.getCurrentBoard() != null) {
			// If yes
			// Initialize color Fields with respective current colors
			playerColorField = new Field(true);
			compColorField = new Field(false);

			// Get the current sizes of both player's and computer's components
			playerScore.setText(Integer.toString(Logic.getPlayerComponent().size()));
			compScore.setText(Integer.toString(Logic.getCompComponent().size()));
		} else {
			// If no
			// Initialize empty new Fields as player's and computer's colors
			playerColorField = new Field();
			compColorField = new Field();
		}

		// Replace the elements in the Layout with new initialized color Fields and score Labels
		this.playerStats.remove(0);
		this.playerStats.add(playerColorField, 0);

		this.playerStats.remove(1);
		this.playerStats.add(playerScore, 1);

		this.compStats.remove(0);
		this.compStats.add(compScore, 0);

		this.compStats.remove(1);
		this.compStats.add(compColorField, 1);

		this.revalidate();
		this.repaint();
	}
	
	
	/**
	 * Displays colors currently in game
	 */
	public void displayColorsInGame() {
		// Get number of colors currently in game
		int noOfColors = Logic.getNoColors();

		// Initialize Panel and its layout, where the colors will be shown
		JPanel colorPane = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		// Initialize side distance between colors dynamically based on width of the GameStatPanel
		int sideInsets = (this.getSize().width / (5*noOfColors));
		
		// Initialize layout constraints
		gbc.insets = new Insets(0, sideInsets, 0, sideInsets);
		gbc.gridx = gbc.gridy = 0;
		gbc.gridwidth = gbc.gridheight = 1;
		
		// Iterate through the colors from first to number of colors,
		// instantiate a Field with corresponding color and add it
		// and its number to the color panel
		for (int color = 1; color <= noOfColors; color++) {
			Field colorField = new Field(0, 0, color);

			colorField.setChoosable(true);
			if(Logic.getCurrentBoard() != null){
				// Check if the color is already a color of player's 
				// or computer's component and if yes display it as 
				// not choosable (lowered border), otherwise as choosable
				// (raised border)
				if (color == Logic.getPlayerColor(true) ||
					color == Logic.getPlayerColor(false)) {
						colorField.setChoosable(false);
				}
			}
			JLabel colorNumber = new JLabel(Integer.toString(color)); 
			gbc.gridy = 0;
			colorPane.add(colorNumber, gbc);
			gbc.gridy = 1;
			colorPane.add(colorField, gbc);
			gbc.gridx = color;
		}

		// Check if the color pane was already initialized and added to layout
		// and if yes delete it
		if (this.getComponents().length >= 7) this.remove(6);
		
		// Add the color panel to the layout
		this.gbc.gridy = 2;
		this.gbc.gridx = 0;
		this.gbc.gridwidth = 3;
		this.gbc.fill = GridBagConstraints.NONE;
		this.gbc.anchor = GridBagConstraints.CENTER;
		this.add(colorPane, this.gbc, 6);
		this.revalidate();
		this.repaint();
	}
	
	/**
	 * Resets timer
	 */
	public void resetTimer() {
		this.timer.stop();
		this.timerValue = 0;
		this.timerGui.setText(this.sdf.format(this.timerValue));
	}

	/**
	 * ActionListener interface method
	 * Updates timer value every 100 milliseconds 
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		this.timerValue += 1;
		Date newTimerValue = new Date();
		newTimerValue.setTime(this.timerValue * 100);

		this.timerGui.setText(this.sdf.format(newTimerValue));
		this.repaint();
	}

	/**
	 * ComponentListener interface method
	 * Initializes color panel  on component resize event
	 */
	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		this.displayColorsInGame();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Getters and Setters
	 */

	public Timer getTimer() {
		return timer;
	}

	public void setTimer(Timer timer) {
		this.timer = timer;
	}

	public long getTimerValue() {
		return timerValue;
	}

	public void setTimerValue(long timerValue) {
		this.timerValue = timerValue;
	}

	public SimpleDateFormat getSdf() {
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf) {
		this.sdf = sdf;
	}

	public JLabel getTimerGui() {
		return timerGui;
	}

	public void setTimerGui(JLabel timerGui) {
		this.timerGui = timerGui;
	}

	public GridBagConstraints getGbc() {
		return gbc;
	}

	public void setGbc(GridBagConstraints gbc) {
		this.gbc = gbc;
	}

	public JPanel getPlayerStats() {
		return playerStats;
	}

	public void setPlayerStats(JPanel playerStats) {
		this.playerStats = playerStats;
	}

	public JPanel getCompStats() {
		return compStats;
	}

	public void setCompStats(JPanel compStats) {
		this.compStats = compStats;
	}

}
