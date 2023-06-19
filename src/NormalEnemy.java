import java.util.ArrayList;

public class NormalEnemy extends Enemy {
	public final static int HORIZONTAL_SPEED = 2;
	public final static int VERTICAL_SPEED = 1;
	public final static int HEALTH = 10;
	public final static int SCORE_GAIN = 10;
	private final static String[] FRAMES = new String[] {"enemy0_0.png", "enemy0_1.png"};
	private final static String[] SOUND_EFFECT_PATHS = new String[] {"normalEnemy_shoot.wav", "enemy_hit.wav", "normalEnemy_death.wav"};

	public NormalEnemy(int x, int y) {
		super(x, y, HORIZONTAL_SPEED, VERTICAL_SPEED, HEALTH, SCORE_GAIN, true, FRAMES, SOUND_EFFECT_PATHS);
		setFramesRandomly();
	}
	
	/**
	 * 	Selects a random normal enemy image.
	 */
	public static void setFramesRandomly() {
		int randomIndex = (int) (Math.random() * 3);	// 0 or 1 or 2
		
		FRAMES[0] = String.format("enemy%d_0.png", randomIndex);
		FRAMES[1] = String.format("enemy%d_1.png", randomIndex);
	}
	
	@Override
	protected ArrayList<Projectile> shootProjectiles() {
		ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
		
		projectiles.add(new NormalEnemyProjectile(this.getRectangle().x + this.getRectangle().width / 2, this.getRectangle().y + this.getRectangle().height));
		
		return projectiles;
	}
}
