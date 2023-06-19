import java.awt.Image;
import java.awt.Rectangle;


/**
 * 	Parent class of each game object.
 */
public abstract class GameObject {
	private boolean stayStillX;
	private boolean stayStillY;
	private int horizontalSpeed;
	private int verticalSpeed;
	private Rectangle rectangle;
	private final Image[] gameObjectFrames;
	public static final int NUM_OF_GAME_OBJECT_FRAMES = 2;
	
	public GameObject(int x, int y, int width, int height, int horizontalSpeed, int verticalSpeed, String[] frames) {
		this.stayStillX = true;
		this.stayStillY = true;
		this.horizontalSpeed = horizontalSpeed;
		this.verticalSpeed = verticalSpeed;
		this.rectangle = new Rectangle(x, y, width, height);
		
		// gets the frames into Image list
		gameObjectFrames = new Image[NUM_OF_GAME_OBJECT_FRAMES];
		setGameObjectFrames(frames);
	}
	
	/**
	 * 	Sets the frame image list according to given String list.
	 */
	public void setGameObjectFrames(String[] frames) {
		for (int i = 0; i < NUM_OF_GAME_OBJECT_FRAMES; i++) {
			gameObjectFrames[i] = ImageFileHandler.getImage(frames[i]);
		}
	}
	
	/**
	 * 	Updates the (x,y) location of the GameObject.
	 * 	Increases the x and y values according to speed.
	 * 	X values are between [0, SpaceInvaders.WIDTH - rectangle.width].
	 * 	Y values are between [-rectangle.height, SpaceInvaders.HEIGHT].
	 */
	public void updatePosition() {
		if (!stayStillX) rectangle.x = Math.max(0, Math.min(rectangle.x + horizontalSpeed, SpaceInvaders.WIDTH - rectangle.width));
		if (!stayStillY) rectangle.y = Math.max(-rectangle.height, Math.min(rectangle.y + verticalSpeed, SpaceInvaders.HEIGHT));
	}
	
	/**
	 * 	Returns whether the GameObject is out of the screen or not.
	 */
	public boolean shouldRemoved() {
		return (passedTopBorder() || passedBottomBorder() || passedLeftBorder() || passedRightBorder());
	}
	
	public final boolean isAtTopBorder() {
		return (rectangle.y == 0);
	}
	
	public final boolean passedTopBorder() {
		return (rectangle.y == 0 - rectangle.height);
	}
	
	public final boolean isAtBottomBorder() {
		return (rectangle.y == SpaceInvaders.HEIGHT - rectangle.height);
	}
	
	public final boolean passedBottomBorder() {
		return (rectangle.y == SpaceInvaders.HEIGHT);
	}
	
	public final boolean isAtLeftBorder() {
		return (rectangle.x == 0);
	}
	
	public final boolean passedLeftBorder() {
		return (rectangle.x == 0 - rectangle.width);
	}
	
	public final boolean isAtRightBorder() {
		return (rectangle.x == SpaceInvaders.WIDTH - rectangle.width);
	}
	
	public final boolean passedRightBorder() {
		return (rectangle.x == SpaceInvaders.WIDTH);
	}
	
	public final void goUp() {
		stayStillY = false;
		verticalSpeed = -Math.abs(verticalSpeed);
	}
	
	public final void goDown() {
		stayStillY = false;
		verticalSpeed = Math.abs(verticalSpeed);
	}
	
	public final void goLeft() {
		stayStillX = false;
		horizontalSpeed = -Math.abs(horizontalSpeed);
	}
	
	public final void goRight() {
		stayStillX = false;
		horizontalSpeed = Math.abs(horizontalSpeed);
	}
	
	public final void setStayStill() {
		stayStillX = true;
		stayStillY = true;
	}
	
	public final void setStayStillX() {
		stayStillX = true;
	}
	
	public final boolean getStayStillX() {
		return stayStillX;
	}
	
	public final void setStayStillY() {
		stayStillY = true;
	}
	
	public final boolean getStayStillY() {
		return stayStillY;
	}
	
	public final int getHorizontalSpeed() {
		return horizontalSpeed;
	}
	
	public final int getVerticalSpeed() {
		return verticalSpeed;
	}
	
	public final Rectangle getRectangle() {
		return rectangle;
	}
	
	public final Image[] getGameObjectFrames() {
		return gameObjectFrames;
	}
}
