import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;


/**
 * 	The first panel that comes when program starts.
 * 	The user must log in in order to play the game.
 */
public class WelcomePanel extends SpaceInvadersPanel {
	private static final long serialVersionUID = -3005242197451536464L;
	
	private static final String BACKGROUND_IMAGE_FRAME = "background_welcomepanel.png";
	private JButton startGameButton;
	
	public WelcomePanel(SpaceInvaders gameFrame) {
		super(gameFrame);
		
		createStartGameButton();
		addActionListeners();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Image backgroundImage = ImageFileHandler.getImage(BACKGROUND_IMAGE_FRAME);
		g.drawImage(backgroundImage, 0, 0, null);
	}
	
	private void createStartGameButton() {
		startGameButton = new JButton("START GAME");
		
		// button settings
		startGameButton.setOpaque(false);
		startGameButton.setContentAreaFilled(false);
		startGameButton.setBorderPainted(false);
		startGameButton.setFocusPainted(false);
		startGameButton.setForeground(Color.WHITE);
		startGameButton.setFont(getFont().deriveFont(Font.BOLD, 70f));
		startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// vertical alignment
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(Box.createVerticalGlue());
		add(startGameButton, BorderLayout.CENTER);
		add(Box.createVerticalGlue());
	}
	
	private void addActionListeners() {
		startGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// The user must log in in order to play the game.
				if (getGameFrame().isUserLoggedIn()) {
					getGameFrame().setGamePanel();
				}
				else {
					JOptionPane.showMessageDialog(getGameFrame(), "You need to login before starting game!");
				}
			}
		});
	}
}
