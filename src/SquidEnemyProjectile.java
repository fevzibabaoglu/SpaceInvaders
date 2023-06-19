
public class SquidEnemyProjectile  extends EnemyProjectile {
	public final static int WIDTH = 40;
	public final static int HEIGHT = 40;
	public final static int HORIZONTAL_SPEED = 5;
	public final static int VERTICAL_SPEED = 5;
	private final static String[] FRAMES = new String[] {"squidProjectile_0.png", "squidProjectile_1.png"};

	public SquidEnemyProjectile(int x, int y) {
		super(x, y, WIDTH, HEIGHT, HORIZONTAL_SPEED, VERTICAL_SPEED, SquidEnemy.VERTICAL_SPEED, FRAMES);
	}
}
