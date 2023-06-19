
public class PlayerProjectile extends Projectile {
	public final static int WIDTH = 12;
	public final static int HEIGHT = 46;
	public final static int HORIZONTAL_SPEED = 0;
	public final static int VERTICAL_SPEED = 20;
	private final static String[] FRAMES = new String[] {"playerProjectile_0.png", "playerProjectile_0.png"};

	public PlayerProjectile(int x, int y) {
		super(x, y, WIDTH, HEIGHT, HORIZONTAL_SPEED, VERTICAL_SPEED, Player.VERTICAL_SPEED, FRAMES);
		this.goUp();
		this.ignoreVerticalSpeedOfShooter();
	}
}
