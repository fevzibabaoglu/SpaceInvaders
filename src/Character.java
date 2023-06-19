import java.util.ArrayList;

public abstract class Character extends GameObject {
	public final static int WIDTH = 80;
	public final static int HEIGHT = 80;
	
	private final String[] SOUND_EFFECTS;	// index 0->shoot, 1->hit, 2->death
	private int health;
	
	public Character(int x, int y, int horizontalSpeed, int verticalSpeed, int health, String[] frames, String[] soundEffects) {
		super(x, y, WIDTH, HEIGHT, horizontalSpeed, verticalSpeed, frames);
		this.SOUND_EFFECTS = soundEffects;
		this.health = health;
	}
	
	/**
	 * 	This method should not be called in anywhere other than inside of the shoot() method.
	 * 	Use shoot() instead.
	 */
	protected abstract ArrayList<Projectile> shootProjectiles();
	
	/**
	 * 	Returns the fired projectile objects as a list.
	 */
	public final ArrayList<Projectile> shoot() {
		playShootSound();
		return shootProjectiles();
	};
	
	private final void playShootSound() {
		playSound(0);
	}
	
	private final void playHitSound() {
		playSound(1);
	}
	
	private final void playDeathSound() {
		playSound(2);
	}
	
	private void playSound(int index) {
		// Exceptions are intended by the developer. Any character can have a null list, a list with null list element or less indexes than needed.
		
		try {
			if (SOUND_EFFECTS[index] != null) {
				
				// creates a thread to play sound effects.
				Thread playSoundThread = new Thread(new AudioPlayer(SOUND_EFFECTS[index]));
				playSoundThread.start();
			}
		}
		catch (NullPointerException e) {}
		catch (ArrayIndexOutOfBoundsException e) {}
	}
	
	public final int getHealth() {
		return health;
	}
	
	public final void gainHealth() {
		health++;
	}
	
	public void takeDamage() {
		health--;
		playHitSound();
	}
	
	/**
	 * 	This method is intended to called once per character. 
	 * 	Any character should be remover after this method returns true.
	 */
	public final boolean isDestroyed() {
		boolean isDestroyed = health <= 0;
		
		if (isDestroyed) playDeathSound();
		
		return (isDestroyed);
	}
}
