import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class SpaceInvaders extends JFrame {
	private static final long serialVersionUID = -505354336368644509L;
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;
	
	private JMenuBar menuBar;
	private JMenu fileMenu;
	private JMenu helpMenu;
	private JMenuItem registerItem;
	private JMenuItem playGameItem;
	private JMenuItem highScoreItem;
	private JMenuItem quitItem;
	private JMenuItem aboutItem;
	
	private SpaceInvadersPanel currentPanel;
	private String username;	// username is needed to be able to play the game
	private int gameLevel;		// there are no way to change this in GUI, it will increase when player gets to 100% until level 3
	
	
	public SpaceInvaders() {
		// default values
		setWelcomePanel();
		gameLevel = 1;
		
		// menu bar creation
		createMenuBar();
		addActionListenersToMenuItems();
		
		// frame settings
		setTitle("Space Invaders");
		setIconImage(ImageFileHandler.getImage("background_welcomepanel.png"));
		setJMenuBar(menuBar);
		setContentPane(currentPanel);
		pack();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * 	Main Method!
	 */
	public static void main(String[] args) {
		new SpaceInvaders();
	}
	
	private void createMenuBar() {
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		registerItem = new JMenuItem("Register");
		playGameItem = new JMenuItem("Play Game");
		highScoreItem = new JMenuItem("High Score");
		quitItem = new JMenuItem("Quit");
		helpMenu = new JMenu("Help");
		aboutItem = new JMenuItem("About");
		
		menuBar.add(fileMenu);
		fileMenu.add(registerItem);
		fileMenu.add(playGameItem);
		fileMenu.add(highScoreItem);
		fileMenu.add(quitItem);
		menuBar.add(helpMenu);
		helpMenu.add(aboutItem);
	}
	
	private void addActionListenersToMenuItems() {
		registerItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// this button is not usable when not in WelcomePanel
				if (!(currentPanel instanceof WelcomePanel)) {
					return;
				}
				
				RegistrationPanel registerPanel = new RegistrationPanel();
				
				int result = JOptionPane.showOptionDialog(SpaceInvaders.this, registerPanel, "Register", 
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
				
				if (result == JOptionPane.OK_OPTION) {
					String usernameInput = registerPanel.getUsername();
					String passwordInput = registerPanel.getPassword();
					String outputMessage = "";
					
					try {
						UserCredentialsFileHandler.saveUserCredentials(usernameInput, passwordInput);
						outputMessage = "You have successfully registered.";
					} 
					catch (UserCredentialsFileHandler.UserCredentialsException err) {
						outputMessage = err.getMessage();
					} 
					catch (IOException err) {
						outputMessage = String.format("An exception occurred when dealing with credentials file: %s", err.getMessage());
					}
					
					JOptionPane.showMessageDialog(SpaceInvaders.this, outputMessage);
				}
			}
		});
		
		playGameItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// this button is not usable when not in WelcomePanel
				if (!(currentPanel instanceof WelcomePanel)) {
					return;
				}
				
				RegistrationPanel loginPanel = new RegistrationPanel();
				
				int result = JOptionPane.showOptionDialog(SpaceInvaders.this, loginPanel, "Login", 
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null);
				
				if (result == JOptionPane.OK_OPTION) {
					String usernameInput = loginPanel.getUsername();
					String passwordInput = loginPanel.getPassword();
					String outputMessage = "";
					
					try {
						if (UserCredentialsFileHandler.checkUserCredentials(usernameInput, passwordInput)) {
							username = usernameInput;
							outputMessage = "You have successfully logged in.";
						}
						else {
							outputMessage = "The given credentials are not correct!";
						}
					} 
					catch (IOException err) {
						outputMessage = String.format("An exception occurred when dealing with data files: %s", err.getMessage());
					}
					
					JOptionPane.showMessageDialog(SpaceInvaders.this, outputMessage);
				}
			}
		});
		
		highScoreItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				// if in welcome panel, go to HighScorePanel
				if (currentPanel instanceof WelcomePanel) {
					setHighScorePanel();
				}
			
				// if in HighScorePanel, return back
				else if (currentPanel instanceof HighScoresPanel) {
					setWelcomePanel();
				}
			}
		});
		
		quitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WindowEvent closeEvent = new WindowEvent(SpaceInvaders.this, WindowEvent.WINDOW_CLOSING);
                Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(closeEvent);
			}
		});
		
		aboutItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(SpaceInvaders.this, 
						"Name: Fevzi Babaoğlu\n"
						+ "Student Number: 20210702020\n"
						+ "Email: fevzi.babaoglu@std.yeditepe.edu.tr\n",
						"Developer Information",
						JOptionPane.INFORMATION_MESSAGE);
				
			}
		});
	}
	
	/**
	 * 	Change the frame panel to given panel.
	 */
	private void changePanel(SpaceInvadersPanel newPanel) {
		currentPanel = newPanel;
		setContentPane(currentPanel);
		revalidate();
		currentPanel.requestFocusInWindow();
	}
	
	public void setWelcomePanel() {
		changePanel(new WelcomePanel(this));
	}
	
	public void setGamePanel() {
		changePanel(new GamePanel(this, gameLevel));
	}
	
	public void setGameFinishedPanel(boolean success, int score) {
		changePanel(new GameFinishedPanel(this, success, username, score));
	}
	
	public void setHighScorePanel() {
		changePanel(new HighScoresPanel(this));
	}
	
	public boolean isUserLoggedIn() {
		return (username != null);
	}
	
	
	/**
	 * 	A simple pop-up panel for login and register.
	 */
	private class RegistrationPanel extends JPanel {
		private static final long serialVersionUID = -4087168642873398183L;
		
		private final JTextField usernameField;
		private final JPasswordField passwordField;
		
		private RegistrationPanel() {
			super(new GridBagLayout());
			GridBagConstraints gbc = new GridBagConstraints();
			
			usernameField = new JTextField(12);
			passwordField = new JPasswordField(12);
			
			JLabel messageLabel = new JLabel();
			messageLabel.setText("<html>Please type your username and password.<br>"
					+ String.format("Minimum character limit is %d.<br>", UserCredentialsFileHandler.MIN_LENGTH)
					+ String.format("Maximum character limit is %d.<br>", UserCredentialsFileHandler.MAX_LENGTH)
					+ String.format("Do not use the sequence \"%s\".</html>", UserCredentialsFileHandler.TOKEN_SPLIT_DELIM));
			
			
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.gridwidth = 2;
			gbc.insets = new Insets(5, 0, 5, 0);
			add(messageLabel, gbc);
			
			gbc.gridx = 0;
			gbc.gridy = 1;
			gbc.gridwidth = 1;
			add(new JLabel("Username: "), gbc);
			
			gbc.gridx = 1;
			add(usernameField, gbc);
			
			gbc.gridx = 0;
			gbc.gridy = 2;
			add(new JLabel("Password: "), gbc);
			
			gbc.gridx = 1;
			add(passwordField, gbc);
		}
		
		private String getUsername() {
			return usernameField.getText();
		}
		
		private String getPassword() {
			return new String(passwordField.getPassword());
		}
	}
}
