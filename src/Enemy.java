
public abstract class Enemy extends Character {
	private static int speedMultiplier = 1;
	private final int scoreGain;
	private final boolean canShoot;

	public Enemy(int x, int y, int horizontalSpeed, int verticalSpeed, int health, int scoreGain, boolean canShoot, String[] frames, String[] soundEffects) {
		super(x, y, horizontalSpeed * speedMultiplier, verticalSpeed * speedMultiplier, health, frames, soundEffects);
		this.scoreGain = scoreGain;
		this.canShoot = canShoot;
		
		goDown();
	}
	
	/**
	 * 	Multiplies the speed of enemies.
	 * 	Use this to increase speed of enemies after game level increases.
	 */
	public static void setSpeedMultiplier(int multiplier) {
		speedMultiplier = multiplier;
	}
	
	public int getScoreGain() {
		return scoreGain;
	}

	public boolean canShoot() {
		return canShoot;
	}
}
