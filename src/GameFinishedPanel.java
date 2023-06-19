import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Timer;


/**
 * 	The panel to show GAMEOVER or WIN screen.
 * 	Clicking on the text will change the program panel to WelcomePanel.
 * 	This panel also saves the score of the player.
 */
public class GameFinishedPanel extends SpaceInvadersPanel implements ActionListener {
	private static final long serialVersionUID = 7209069136838277954L;
	
	private static final int BACKGROUND_NEXT_FRAME_DELAY_MS = 500;
	private final static String[] BACKGROUND_IMAGE_FRAMES = new String[] {"background_gamefinished_0.png", "background_gamefinished_1.png"};
	private int backgroundFrameIndex;
	
	private final boolean SUCCESS;
	private JButton returnBackButton;
	
	private final Timer panelBackgroundTimer;

	public GameFinishedPanel(SpaceInvaders gameFrame, boolean success, String username, int score) {
		super(gameFrame);
		this.SUCCESS = success;
		backgroundFrameIndex = 0;
		
		// try to save user score
		try {
			HighScoresFileHandler.saveScore(username, score);
		} 
		catch (IOException e) {
			JOptionPane.showMessageDialog(getGameFrame(), e.getMessage());
		}
		
		// button creation
		createReturnBackButton();
		addActionListeners();
		
		// set panel timer
		panelBackgroundTimer = new Timer(BACKGROUND_NEXT_FRAME_DELAY_MS, this);
		panelBackgroundTimer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		backgroundFrameIndex = (backgroundFrameIndex + 1) % BACKGROUND_IMAGE_FRAMES.length;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Image backgroundImage = ImageFileHandler.getImage(BACKGROUND_IMAGE_FRAMES[backgroundFrameIndex]);
		g.drawImage(backgroundImage, 0, 0, null);
	}
	
	private void createReturnBackButton() {
		String buttonText = (SUCCESS) ? "YOU WON" : "GAME OVER";
		
		returnBackButton = new JButton(buttonText);
		
		returnBackButton.setOpaque(false);
		returnBackButton.setContentAreaFilled(false);
		returnBackButton.setBorderPainted(false);
		returnBackButton.setFocusPainted(false);
		returnBackButton.setForeground(Color.WHITE);
		returnBackButton.setFont(getFont().deriveFont(Font.BOLD, 70f));
		returnBackButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// vertical alignment of the button
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalGlue());
		add(Box.createVerticalGlue());
		add(Box.createVerticalGlue());
		add(Box.createVerticalGlue());
		add(returnBackButton, BorderLayout.CENTER);
		add(Box.createVerticalGlue());
	}
	
	private void addActionListeners() {
		returnBackButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panelBackgroundTimer.stop();
				
				// change the panel to WelcomePanel
				getGameFrame().setWelcomePanel();
			}
		});
	}
}
