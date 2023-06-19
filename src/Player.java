import java.util.ArrayList;

public class Player extends Character {
	public final static int HORIZONTAL_SPEED = 12;
	public final static int VERTICAL_SPEED = 12;
	public final static int HEALTH = 3;
	private final static String[] FRAMES = new String[] {"player_0.png", "player_1.png"};
	private final static String[] SOUND_EFFECT_PATHS = new String[] {"player_shoot.wav", "player_hit.wav", null};

	public Player(int x, int y) {
		super(x, y, HORIZONTAL_SPEED, VERTICAL_SPEED, HEALTH, FRAMES, SOUND_EFFECT_PATHS);
	}
	
	@Override
	protected ArrayList<Projectile> shootProjectiles() {
		ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
		
		projectiles.add(new PlayerProjectile(this.getRectangle().x + this.getRectangle().width / 2, this.getRectangle().y));
		
		return projectiles;
	}
	
	public void setInvisible() {
		FRAMES[0] = "player_invisible_0.png";
		FRAMES[1] = "player_invisible_1.png";
		super.setGameObjectFrames(FRAMES);
	}
	
	public void setVisible() {
		FRAMES[0] = "player_0.png";
		FRAMES[1] = "player_1.png";
		super.setGameObjectFrames(FRAMES);
	}

	/**
	 * 	Updates the (x,y) location of the GameObject.
	 * 	Increases the x and y values according to speed.
	 * 	X values are between [0, SpaceInvaders.WIDTH - rectangle.width].
	 * 	Y values are between [0, SpaceInvaders.HEIGHT - getRectangle().height].
	 */
	@Override
	public void updatePosition() {
		if (!getStayStillX()) getRectangle().x = Math.max(0, Math.min(getRectangle().x + getHorizontalSpeed(), SpaceInvaders.WIDTH - getRectangle().width));
		if (!getStayStillY()) getRectangle().y = Math.max(0, Math.min(getRectangle().y + getVerticalSpeed(), SpaceInvaders.HEIGHT - getRectangle().height));
	}
}
