import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;


/**
 * 	Parent class of every important panel used in this program.
 */
public abstract class SpaceInvadersPanel extends JPanel {
	private static final long serialVersionUID = -8771062886438766977L;
	
	private final SpaceInvaders gameFrame;	// panels keep a reference to the SpaceInvaders frame in order to change to a different panel

	public SpaceInvadersPanel(SpaceInvaders gameFrame) {
		this.gameFrame = gameFrame;
		
		setBackground(Color.DARK_GRAY);
		setFocusable(true);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(SpaceInvaders.WIDTH, SpaceInvaders.HEIGHT);
	}

	/**
	 * 	Returns the SpaceInvaders frame.
	 */
	public SpaceInvaders getGameFrame() {
		return gameFrame;
	}
}
