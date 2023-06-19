import java.util.ArrayList;

public class WallEnemy extends Enemy {
	public final static int HORIZONTAL_SPEED = 0;
	public final static int VERTICAL_SPEED = 1;
	public final static int HEALTH = 30;
	public final static int SCORE_GAIN = 10;
	private final static String[] FRAMES = new String[] {"wallEnemy_0.png", "wallEnemy_1.png"};
	private final static String[] SOUND_EFFECT_PATHS = new String[] {null, "enemy_hit.wav", "wallEnemy_death.wav"};

	public WallEnemy(int x, int y) {
		super(x, y, HORIZONTAL_SPEED, VERTICAL_SPEED, HEALTH, SCORE_GAIN, false, FRAMES, SOUND_EFFECT_PATHS);
	}
	
	@Override
	protected ArrayList<Projectile> shootProjectiles() {
		ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
		return projectiles;
	}
}
