
public class NormalEnemyProjectile extends EnemyProjectile {
	public final static int WIDTH = 12;
	public final static int HEIGHT = 46;
	public final static int HORIZONTAL_SPEED = 0;
	public final static int VERTICAL_SPEED = 20;
	private final static String[] FRAMES = new String[] {"enemyProjectile_0.png", "enemyProjectile_1.png"};

	public NormalEnemyProjectile(int x, int y) {
		super(x, y, WIDTH, HEIGHT, HORIZONTAL_SPEED, VERTICAL_SPEED, NormalEnemy.VERTICAL_SPEED, FRAMES);
		this.goDown();
	}
}
