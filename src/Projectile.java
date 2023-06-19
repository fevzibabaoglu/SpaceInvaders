
public abstract class Projectile extends GameObject {
	private int verticalSpeedOfShooter;
	private boolean ignoreVerticalSpeedOfShooter;

	public Projectile(int x, int y, int width, int height, int horizontalSpeed, int verticalSpeed, int verticalSpeedOfShooter, String[] frames) {
		super(x, y, width, height, horizontalSpeed, verticalSpeed, frames);
		this.verticalSpeedOfShooter = verticalSpeedOfShooter;
		this.ignoreVerticalSpeedOfShooter = false;
	}
	
	/**
	 * 	Updates the (x,y) location of the GameObject.
	 * 	Increases the x and y values according to speed.
	 * 	Also updates the y value if it does not ignore the vertical speed of the shooter.
	 * 	X values are between [-getRectangle().width, SpaceInvaders.WIDTH].
	 * 	Y values are between [-rectangle.height, SpaceInvaders.HEIGHT].
	 */
	@Override
	public void updatePosition() {
		if (!getStayStillX()) getRectangle().x = Math.max(-getRectangle().width, Math.min(getRectangle().x + getHorizontalSpeed(), SpaceInvaders.WIDTH));
		if (!getStayStillY()) getRectangle().y = Math.max(-getRectangle().height, Math.min(getRectangle().y + getVerticalSpeed(), SpaceInvaders.HEIGHT));
		
		// vertical move caused by the shooters vertical movement
		// by doing this, horizontal projectiles stay at the same y value as its shooter's y value
		if (!ignoreVerticalSpeedOfShooter) getRectangle().y = Math.max(-getRectangle().height, Math.min(getRectangle().y + verticalSpeedOfShooter, SpaceInvaders.HEIGHT));
	}
	
	/**
	 * 	The projectile does not ignore the vertical speed of the shooter by default.
	 * 	Call this to change it.
	 */
	public final void ignoreVerticalSpeedOfShooter() {
		this.ignoreVerticalSpeedOfShooter = true;
	}
}
