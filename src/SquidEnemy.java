import java.util.ArrayList;

public class SquidEnemy extends Enemy {
	public final static int HORIZONTAL_SPEED = 0;
	public final static int VERTICAL_SPEED = 1;
	public final static int HEALTH = 10;
	public final static int SCORE_GAIN = 20;
	private final static String[] FRAMES = new String[] {"squid_0.png", "squid_1.png"};
	private final static String[] SOUND_EFFECT_PATHS = new String[] {"squidEnemy_shoot.wav", "enemy_hit.wav", "squidEnemy_death.wav"};

	public SquidEnemy(int x, int y) {
		super(x, y, HORIZONTAL_SPEED, VERTICAL_SPEED, HEALTH, SCORE_GAIN, true, FRAMES, SOUND_EFFECT_PATHS);
	}

	@Override
	protected ArrayList<Projectile> shootProjectiles() {
		ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
		
		Projectile leftProjectile = new SquidEnemyProjectile(this.getRectangle().x - SquidEnemyProjectile.WIDTH, this.getRectangle().y + this.getRectangle().height - SquidEnemyProjectile.HEIGHT);
		projectiles.add(leftProjectile);
		leftProjectile.goLeft();
		
		Projectile leftBottomProjectile = new SquidEnemyProjectile(this.getRectangle().x, this.getRectangle().y + this.getRectangle().height);
		projectiles.add(leftBottomProjectile);
		leftBottomProjectile.goLeft();
		leftBottomProjectile.goDown();
		
		Projectile rightBottomProjectile = new SquidEnemyProjectile(this.getRectangle().x + this.getRectangle().width - SquidEnemyProjectile.WIDTH, this.getRectangle().y + this.getRectangle().height);
		projectiles.add(rightBottomProjectile);
		rightBottomProjectile.goRight();
		rightBottomProjectile.goDown();
		
		Projectile rightProjectile = new SquidEnemyProjectile(this.getRectangle().x + this.getRectangle().width, this.getRectangle().y + this.getRectangle().height - SquidEnemyProjectile.HEIGHT);
		projectiles.add(rightProjectile);
		rightProjectile.goRight();
		
		return projectiles;
	}
}
