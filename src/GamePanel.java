import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.Timer;


/**
 *	The GAME!
 */
public class GamePanel extends SpaceInvadersPanel implements ActionListener {
	private static final long serialVersionUID = 3325065429037997331L;
	
	// game status
	private enum GameStatus {RUNNING, GAMEOVER, GAMESUCCESS}
	
	// game info
	private GameStatus gameStatus;
	private int gameLevel;			// game difficulty
	private int gameScore;			// game difficulty multiplies the score gain from enemies
	private int gamePercentage;
	private int gamePhase;
	
	// player character info
	private boolean playerInvisible;
	private boolean playerCanShoot;
	
	// gameObject image frame info
	private int gameObjectFrameIndex;
	
	// panel background info
	private int backgroundY;
	private Image backgroundImage;
	private Image nextBackgroundImage;
	
	// gameObject objects
	private Player player;
	private final List<Enemy> enemies;
	private final List<PlayerProjectile> playerProjectiles;
	private final List<EnemyProjectile> enemyProjectiles;
	
	// nested classes
	private TimerController timerController;
	private KeyAndMouseController keyAndMouseController;
	
	// other
	private long time;	// in order to calculate game FPS
	
	
	public GamePanel(SpaceInvaders gameFrame, int gameLevel) {
		super(gameFrame);
		
		// default game info
		gameStatus = GameStatus.RUNNING;
		this.gameLevel = gameLevel;
		gameScore = 0;
		gamePercentage = 0;
		gamePhase = 0;
		
		// default player character info
		playerInvisible = false;
		playerCanShoot = true;
		
		// default gameObject image frame info
		gameObjectFrameIndex = 0;
		
		// default panel background info
		backgroundY = 0;
		backgroundImage = ImageFileHandler.getImage("background0.png");
		nextBackgroundImage = ImageFileHandler.getImage("background1.png");
		
		// default gameObject objects
		player = new Player(SpaceInvaders.WIDTH / 2 - Player.WIDTH / 2, 3 * SpaceInvaders.HEIGHT / 4);
		enemies = new ArrayList<Enemy>();
		playerProjectiles = new ArrayList<PlayerProjectile>();
		enemyProjectiles = new ArrayList<EnemyProjectile>();
		
		// nested class objects
		timerController = new TimerController();
		keyAndMouseController = new KeyAndMouseController();
		
		// other settings
		Enemy.setSpeedMultiplier(gameLevel);
		timerController.startGameTimers();
		time = System.currentTimeMillis();
		
		// panel settings
		addKeyListener(keyAndMouseController.keyAdapter);
		addMouseListener(keyAndMouseController.mouseAdapter);
		addMouseMotionListener(keyAndMouseController.mouseAdapter);
	}
	
	/**
	 * 	The Game Loop.
	 * 	Invoked when TimerController.gameTimer fires.
	 */
	@Override
	public final void actionPerformed(ActionEvent e) {
		if (gameStatus == GameStatus.RUNNING) {
			keyAndMouseController.inputControl();
			updatePositions();
			repaint();
			checkCollisions();
			checkGameSuccess();
			checkAndSetGameFinished();
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// drawing the background
		int imageHeight = backgroundImage.getHeight(null);
		int nextImageHeight = nextBackgroundImage.getHeight(null);
		
		g.drawImage(backgroundImage, 0, backgroundY - imageHeight + SpaceInvaders.HEIGHT, null);
		g.drawImage(nextBackgroundImage, 0, backgroundY - imageHeight - nextImageHeight + SpaceInvaders.HEIGHT, null);
		
		// drawing a box between two different backgrounds
		if (backgroundImage != nextBackgroundImage) {
			g.setColor(Color.DARK_GRAY);
			g.fillRect(0, backgroundY - imageHeight + SpaceInvaders.HEIGHT - 10, SpaceInvaders.WIDTH, 20);
		}
		
		
		// drawing all of the GameObjects
		g.drawImage(player.getGameObjectFrames()[gameObjectFrameIndex], player.getRectangle().x, player.getRectangle().y, null);
		
		for (Enemy enemy : enemies) {
			g.drawImage(enemy.getGameObjectFrames()[gameObjectFrameIndex], enemy.getRectangle().x, enemy.getRectangle().y, null);
		}
		for (PlayerProjectile playerProjectile : playerProjectiles) {
			g.drawImage(playerProjectile.getGameObjectFrames()[gameObjectFrameIndex], playerProjectile.getRectangle().x, playerProjectile.getRectangle().y, null);
		}
		for (EnemyProjectile enemyProjectile : enemyProjectiles) {
			g.drawImage(enemyProjectile.getGameObjectFrames()[gameObjectFrameIndex], enemyProjectile.getRectangle().x, enemyProjectile.getRectangle().y, null);
		}
		
		
		// calculating FPS
		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - time;
		time = currentTime;


		// drawing information strings
		g.setColor(Color.RED);
		g.setFont(new Font("Ariel", Font.BOLD, 20));
		g.drawString(String.format("LEVEL %d - H: x%d", gameLevel, player.getHealth()), 10, 20);
		g.drawString(String.format("STAGE %d - %d%%", gamePhase, getGamePercentage()), 10, 45);
		g.drawString(String.format("%d", getGameScore()), SpaceInvaders.WIDTH / 2 - 10, 20);
		g.drawString(String.format("%d FPS", (int) 1000 / ((elapsedTime == 0) ? (TimerController.GAME_TIMER_DELAY_MS) : elapsedTime)), SpaceInvaders.WIDTH - 70, 20);
	}
	
	/**
	 * 	Updates all of the GameObjects.
	 */
	private void updatePositions() {
		player.updatePosition();
		
		Iterator<Enemy> enemyIterator = enemies.iterator();
		while (enemyIterator.hasNext()) {
			Enemy enemy = enemyIterator.next();
			enemy.updatePosition();
			
			if (enemy.shouldRemoved()) {
				enemyIterator.remove();
			} else if (enemy.isAtLeftBorder()) {
				enemy.goRight();
			} else if (enemy.isAtRightBorder()) {
				enemy.goLeft();
			}
		}
		
		Iterator<PlayerProjectile> playerProjectileIterator = playerProjectiles.iterator();
		while (playerProjectileIterator.hasNext()) {
			PlayerProjectile playerProjectile = playerProjectileIterator.next();
			playerProjectile.updatePosition();
			
			if (playerProjectile.shouldRemoved()) {
				playerProjectileIterator.remove();
			}
		}
		
		Iterator<EnemyProjectile> enemyProjectileIterator = enemyProjectiles.iterator();
		while (enemyProjectileIterator.hasNext()) {
			EnemyProjectile enemyProjectile = enemyProjectileIterator.next();
			enemyProjectile.updatePosition();
			
			if (enemyProjectile.shouldRemoved()) {
				enemyProjectileIterator.remove();
			}
		}
	}
	
	/**
	 * 	Checks GameObject collisions.
	 * 	Destroys objects if needed.
	 */
	private void checkCollisions() {
		// check for player-enemy collision
		for (Enemy enemy : enemies) {
			if (!playerInvisible && enemy.getRectangle().intersects(player.getRectangle())) {
				player.takeDamage();
				timerController.startPlayerInvisibilityTimer();
				
				if (player.isDestroyed()) {
					gameStatus = GameStatus.GAMEOVER;
					return;
				}
			}
		}
		
		// check playerProjectile-enemy collision
		Iterator<PlayerProjectile> playerProjectileIterator = playerProjectiles.iterator();
		while (playerProjectileIterator.hasNext()) {
			PlayerProjectile playerProjectile = playerProjectileIterator.next();

			Iterator<Enemy> enemyIterator = enemies.iterator();
			while (enemyIterator.hasNext()) {
				Enemy enemy = enemyIterator.next();
				
				if (playerProjectile.getRectangle().intersects(enemy.getRectangle())) {
					playerProjectileIterator.remove();
					enemy.takeDamage();
					addToGameScore(enemy.getScoreGain());
					
					if (enemy.isDestroyed()) {
						enemyIterator.remove();
					}
					
					// continue with the next PlayerProjectile
					break;
				}
			}
		}
		
		// check for enemyProjectile collisions
		Iterator<EnemyProjectile> enemyProjectileIterator = enemyProjectiles.iterator();
		while (enemyProjectileIterator.hasNext()) {
			EnemyProjectile enemyProjectile = enemyProjectileIterator.next();

			// check for player-enemyProjectile collision
			if (!playerInvisible && player.getRectangle().intersects(enemyProjectile.getRectangle())) {
				enemyProjectileIterator.remove();
				player.takeDamage();
				timerController.startPlayerInvisibilityTimer();
				
				if (player.isDestroyed()) {
					gameStatus = GameStatus.GAMEOVER;
					return;
				}
				
				// continue with the next EnemyProjectile
				continue;
			}
			
			// check for enemy-enemyProjectile collision
			Iterator<Enemy> enemyIterator = enemies.iterator();
			while (enemyIterator.hasNext()) {
				Enemy enemy = enemyIterator.next();
				
				if (enemyProjectile.getRectangle().intersects(enemy.getRectangle())) {
					enemyProjectileIterator.remove();
					
					// continue with the next EnemyProjectile
					break;
				}
			}
		}
	}
	
	/**
	 * 	GAMESUCCESS if gameLevel is 3 and gamePercentage is 100.
	 */
	private void checkGameSuccess() {
		if (gamePercentage == 100 && gameLevel == 3 && gameStatus == GameStatus.RUNNING) {
			gameStatus = GameStatus.GAMESUCCESS;
		}
	}
	
	/**
	 * 	Finish the game if GAMESUCCESS or GAMEOVER.
	 */
	private void checkAndSetGameFinished() {
		if (gameStatus != GameStatus.RUNNING) {
			timerController.stopGameTimers();
			
			// we add this to event thread in order to not get null pointer exception (it is thrown in paintComponent())
			SwingUtilities.invokeLater(() -> {
				player = null;
			});
			
			// clear the lists
			enemies.clear();
			playerProjectiles.clear();
			enemyProjectiles.clear();
			
			// change the panel to WelcomePanel
			getGameFrame().setGameFinishedPanel(gameStatus == GameStatus.GAMESUCCESS, gameScore);
		}
	}
	
	/**
	 * 	Increase gameScore accordingly.
	 */
	private void addToGameScore(int scoreGain) {
		gameScore += (scoreGain * gameLevel);
	}
	
	public final int getGameScore() {
		return gameScore;
	}
	
	public final int getGamePercentage() {
		return gamePercentage;
	}
	
	
	/**
	 * 	Controls all key and mouse inputs.
	 * 	The mouse has a priority. 
	 * 	Program will block all key inputs as long as the mouse is in the frame.
	 */
	private class KeyAndMouseController {
		private final List<Integer> UP_KEYS = Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_W);
		private final List<Integer> LEFT_KEYS = Arrays.asList(KeyEvent.VK_LEFT, KeyEvent.VK_A);
		private final List<Integer> RIGHT_KEYS = Arrays.asList(KeyEvent.VK_RIGHT, KeyEvent.VK_D);
		private final List<Integer> DOWN_KEYS = Arrays.asList(KeyEvent.VK_DOWN, KeyEvent.VK_S);
		private final List<Integer> SHOOT_KEYS = Arrays.asList(KeyEvent.VK_SPACE);
		private final List<Integer> SHOOT_BUTTONS = Arrays.asList(MouseEvent.BUTTON1);
		
		private final HashSet<Integer> keyPressedSet;
		private Point mouseLocation;
		private boolean mouseControl;
		
		private final KeyAdapter keyAdapter;		// KeyListener
		private final MouseAdapter mouseAdapter;	// MouseListener and MouseMotionListener
		
		private KeyAndMouseController() {
			keyPressedSet = new HashSet<Integer>();
			mouseLocation = new Point();
			mouseControl = false;
			
			keyAdapter = new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) {
					keyPressedSet.add(e.getKeyCode());
				}

				@Override
				public void keyReleased(KeyEvent e) {
					keyPressedSet.remove(e.getKeyCode());
				}
			};
			
			mouseAdapter = new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					keyPressedSet.add(e.getButton());
				}
				
				@Override
				public void mouseReleased(MouseEvent e) {
					keyPressedSet.remove(e.getButton());
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					mouseControl = true;
					if (gameStatus == GameStatus.RUNNING) player.setStayStill();
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					mouseControl = false;
					if (gameStatus == GameStatus.RUNNING) player.setStayStill();
				}
				
				@Override
				public void mouseDragged(MouseEvent e) {
					mouseLocation = e.getPoint();
				}
				
				@Override
				public void mouseMoved(MouseEvent e) {
					mouseLocation = e.getPoint();
				}
			};
		}
		
		private void inputControl() {
			if (mouseControl) mouseInputControl();
			else keyInputControl();
		}
		
		private boolean isKeyPressed(List<Integer> keyCodes) {
			for (Integer keyCode : keyCodes) {
				if (keyPressedSet.contains(keyCode)) {
					return true;
				}
			}
			
			return false;
		}
		
		private void keyInputControl() {
			
			// player fire
			if (isKeyPressed(SHOOT_KEYS) && playerCanShoot) {
				for (Projectile projectile : player.shoot()) {
					playerProjectiles.add((PlayerProjectile) projectile);
				}
				
				timerController.startPlayerShootTimer();
			}
			
			// vertical movement
			if (isKeyPressed(UP_KEYS)) {
				player.goUp();
			} 
			else if (isKeyPressed(DOWN_KEYS)) {
				player.goDown();
			} 
			else {
				player.setStayStillY();
			}

			// horizontal movement
			if (isKeyPressed(LEFT_KEYS)) {
				player.goLeft();
			} 
			else if (isKeyPressed(RIGHT_KEYS)) {
				player.goRight();
			} 
			else {
				player.setStayStillX();
			}
		}
		
		private void mouseInputControl() {
			
			// player fire
			if (isKeyPressed(SHOOT_BUTTONS) && playerCanShoot) {
				for (Projectile projectile : player.shoot()) {
					playerProjectiles.add((PlayerProjectile) projectile);
				}
				timerController.startPlayerShootTimer();
			}
			
			// vertical movement, moves the player if the mouse is above or below the player
			if (mouseLocation.y < player.getRectangle().getMinY()) {
				player.goUp();
			} 
			else if (mouseLocation.y > player.getRectangle().getMaxY()) {
				player.goDown();
			} 
			else {
				player.setStayStillY();
			}
			
			// horizontal movement, moves the player if the mouse is at left or right of the player
			if (mouseLocation.x < player.getRectangle().getMinX()) {
				player.goLeft();
			} 
			else if (mouseLocation.x > player.getRectangle().getMaxX()) {
				player.goRight();
			} 
			else {
				player.setStayStillX();
			}
		}
	}
	
	/**
	 * 	Controls the different enemy spawn methods.
	 * 	TimerController.enemySpawnTimer uses this class.
	 */
	private class EnemySpawner implements ActionListener {
		private final static int SONE_TIMER_DELAY_MS = 2000;	// SeriesOfNormalEnemies timer delay
		private final static int RSE_TIMER_DELAY_MS = 2000;		// RandomSquidEnemies timer delay
		
		private boolean selectAnother;
		private Timer spawnTimer;
		
		private EnemySpawner() {
			selectAnother = true;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (gamePhase != 0 && selectAnother) {
				
				int random = (int)(Math.random() * 10) + 1;	// 1-10
				
				// only spawn normal enemies
				if (gamePhase == 1) {
					if (random <= 3) {
						spawnRandomClusterOfWallEnemies();	// 30%
					}
					else {
						spawnSeriesOfNormalEnemies();		// 70%
					}
				}
				
				// spawn normal and wall enemies
				else if (gamePhase == 2) {
					if (random <= 2) {
						spawnRandomClusterOfWallEnemies();	// 20%
					} 
					else if (random <= 5) {
						spawnLineOfWallEnemies();			// 30%
					}
					else {
						spawnSeriesOfNormalEnemies();		// 50%
					}
				}
				
				// spawn squid and wall enemies
				else if (gamePhase == 3) {
					if (random <= 2) {
						spawnRandomClusterOfWallEnemies();	// 20%
					} 
					else if (random <= 5) {
						spawnLineOfWallEnemies();			// 30%
					}
					else {
						spawnRandomSquidEnemies();			// 50%
					}
				}
				
				// spawn all enemies
				else if (gamePhase == 4) {
					if (random <= 2) {
						spawnRandomClusterOfWallEnemies();	// 20%
					} 
					else if (random <= 4) {
						spawnLineOfWallEnemies();			// 20%
					}
					else if (random <= 7) {
						spawnRandomSquidEnemies();			// 30%
					}
					else {
						spawnSeriesOfNormalEnemies();		// 30%
					}
				}
				
			}
		}
		
		private void canSelectAnother() {
			selectAnother = true;
			spawnTimer.stop();
		}
		
		private void cannotSelectAnother() {
			selectAnother = false;
			spawnTimer.start();
		}
		
		/**
		 * 	Spawns 'ENEMY_COUNT' normal enemies repeatedly.
		 * 	Uses timer to regulate the spawn rate.
		 */
		private void spawnSeriesOfNormalEnemies() {
			final int ENEMY_COUNT = 5;
			final int spawnX = ((int)(Math.random() * 2) == 0) ? 1 : SpaceInvaders.WIDTH - 1 ;	// random value, 0 -> left corner, 1 -> right corner
			
			spawnTimer = new Timer(SONE_TIMER_DELAY_MS / gameLevel, new ActionListener() {
				private int loopCounter = 0;
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (loopCounter < ENEMY_COUNT) {
						
						Enemy newEnemy = new NormalEnemy(spawnX, 1);	// spawn location is either (1,1) or (SpaceInvaders.WIDTH-1,1)
						enemies.add(newEnemy);
						
						if (spawnX < SpaceInvaders.WIDTH) newEnemy.goRight();
						else newEnemy.goLeft();
						
						loopCounter++;
					}
					
					else {
						canSelectAnother();
					}
				}
			});
			
			cannotSelectAnother();
		}
		
		/**
		 * 	Spawns 'ENEMY_COUNT' wall enemies side by side.
		 */
		private void spawnRandomClusterOfWallEnemies() {
			final int ENEMY_COUNT = 3;
			final int spawnX = (int)(Math.random() * (SpaceInvaders.WIDTH - ENEMY_COUNT * WallEnemy.WIDTH - 2)) + 1;	// x location of first wall enemy
			
			for (int i = 0; i < ENEMY_COUNT; i++) {
				Enemy newEnemy = new WallEnemy(spawnX + i * WallEnemy.WIDTH, 1);
				enemies.add(newEnemy);
			}
		}
		
		/**
		 * 	Spawns a line of wall enemies.
		 */
		private void spawnLineOfWallEnemies() {
			final int ENEMY_COUNT = (SpaceInvaders.WIDTH - 2) / WallEnemy.WIDTH;
			final int spawnX = ((SpaceInvaders.WIDTH - ENEMY_COUNT * WallEnemy.WIDTH) / 2) + 1;	// x location of first wall enemy
			
			for (int i = 0; i < ENEMY_COUNT; i++) {
				Enemy newEnemy = new WallEnemy(spawnX + i * WallEnemy.WIDTH, 1);
				enemies.add(newEnemy);
			}
		}
		
		/**
		 * 	Spawn 'ENEMY_COUNT' squid enemies at random locations.
		 * 	Uses timer to regulate the spawn rate.
		 */
		private void spawnRandomSquidEnemies() {
			final int ENEMY_COUNT = 3;
			
			spawnTimer = new Timer(RSE_TIMER_DELAY_MS / gameLevel, new ActionListener() {
				private int loopCounter = 0;
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if (loopCounter < ENEMY_COUNT) {
						
						int spawnX = (int)(Math.random() * (SpaceInvaders.WIDTH - SquidEnemy.WIDTH - 2)) + 1;
						
						Enemy newEnemy = new SquidEnemy(spawnX, 1);
						enemies.add(newEnemy);
						
						loopCounter++;
					}
					
					else {
						canSelectAnother();
					}
				}
			});
			
			cannotSelectAnother();
		}
	}
	
	
	/**
	 * 	Controls all Timer objects.
	 * 	<p>
	 * 	As the game level increases; 
	 * 	game percentage increases slower, 
	 * 	enemies spawn and shoot more often, 
	 * 	and player invisibility time gets shorten.
	 */
	private class TimerController {
		private static final int GAME_TIMER_DELAY_MS = 20;
		private static final int GAME_PERCENTAGE_TIMER_DELAY_MS = 2000;
		private static final int GAME_OBJECT_NEXT_FRAME_DELAY_MS = 300;
		private static final int GAME_BACKGROUND_MOVE_DELAY_MS = 20;
		private static final int PLAYER_INVISIBILITY_DELAY_MS = 2000;
		private static final int PLAYER_PROJECTILE_TIMER_DELAY_MS = 150;
		private static final int ENEMY_SPAWN_TIMER_DELAY_MS = 3000;
		private static final int ENEMY_PROJECTILE_TIMER_DELAY_MS = 1500;
		
		private final Timer gameTimer;
		private final Timer gamePercentageTimer;
		private final Timer nextGameObjectFrameTimer;
		private final Timer moveGameBackgroundTimer;
		private final Timer playerInvisibilityTimer;
		private final Timer playerShootTimer;
		private final Timer enemySpawnTimer;
		private final Timer enemyShootTimer;
		
		private TimerController() {
			gameTimer = new Timer(GAME_TIMER_DELAY_MS, GamePanel.this);
			
			gamePercentageTimer = new Timer(GAME_PERCENTAGE_TIMER_DELAY_MS * gameLevel, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gamePercentage++;
					
					// set the game level
					if (gamePercentage == 100 && gameLevel != 3) {
						gamePercentage = 0;
						gameLevel++;
						Enemy.setSpeedMultiplier(gameLevel);
						player.gainHealth();
					}
					
					// set the game phase
					if (gamePercentage >= 75) {
						gamePhase = 4;
					} else if (gamePercentage >= 50) {
						gamePhase = 3;
					} else if (gamePercentage >= 20) {
						gamePhase = 2;
					} else if (gamePercentage >= ((double) 3 / gameLevel)) {
						gamePhase = 1;
					} else {
						gamePhase = 0;
					}
				}
			});
			
			nextGameObjectFrameTimer = new Timer(GAME_OBJECT_NEXT_FRAME_DELAY_MS, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					gameObjectFrameIndex = (gameObjectFrameIndex + 1) % GameObject.NUM_OF_GAME_OBJECT_FRAMES;
				}
			});
			
			moveGameBackgroundTimer = new Timer(GAME_BACKGROUND_MOVE_DELAY_MS / gameLevel, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					backgroundY++;
					int imageHeight = backgroundImage.getHeight(null);
					
					if (backgroundY >= imageHeight) {
						backgroundY = 0;
						backgroundImage = nextBackgroundImage;
						
						if (gamePhase == 4) {
							nextBackgroundImage = ImageFileHandler.getImage("background3.png");
						}
						else if (gamePhase == 3) {
							nextBackgroundImage = ImageFileHandler.getImage("background2.png");
						}
						else {
							nextBackgroundImage = ImageFileHandler.getImage("background1.png");
						}
					}
				}
			});
			
			playerInvisibilityTimer = new Timer(PLAYER_INVISIBILITY_DELAY_MS / gameLevel, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					playerInvisible = false;
					player.setVisible();
					playerInvisibilityTimer.stop();
				}
			});
			
			playerShootTimer = new Timer(PLAYER_PROJECTILE_TIMER_DELAY_MS, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					playerCanShoot = true;
					playerShootTimer.stop();
				}
			});
			
			enemySpawnTimer = new Timer(ENEMY_SPAWN_TIMER_DELAY_MS, new EnemySpawner());
			
			enemyShootTimer = new Timer(ENEMY_PROJECTILE_TIMER_DELAY_MS / gameLevel, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (!enemies.isEmpty()) {
						
						Enemy randomEnemy = enemies.get((int) (Math.random() * enemies.size()));
						
						for (Projectile projectile : randomEnemy.shoot()) {
							enemyProjectiles.add((EnemyProjectile) projectile);
						}
					}
				}
			});
		}
		
		private void startGameTimers() {
			gameTimer.start();
			gamePercentageTimer.start();
			nextGameObjectFrameTimer.start();
			moveGameBackgroundTimer.start();
			playerInvisibilityTimer.start();
			playerShootTimer.start();
			enemySpawnTimer.start();
			enemyShootTimer.start();
		}
		
		private void stopGameTimers() {
			gameTimer.stop();
			gamePercentageTimer.stop();
			nextGameObjectFrameTimer.stop();
			moveGameBackgroundTimer.stop();
			playerInvisibilityTimer.stop();
			playerShootTimer.stop();
			enemySpawnTimer.stop();
			enemyShootTimer.stop();
		}
		
		private void startPlayerInvisibilityTimer() {
			playerInvisible = true;
			player.setInvisible();
			playerInvisibilityTimer.start();
		}
		
		private void startPlayerShootTimer() {
			playerCanShoot = false;
			playerShootTimer.start();
		}
	}
}


