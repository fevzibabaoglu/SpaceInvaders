import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HighScoresPanel extends SpaceInvadersPanel {
	private static final long serialVersionUID = 1124290446139314608L;
	
	public HighScoresPanel(SpaceInvaders gameFrame) {
		super(gameFrame);
		setBackground(Color.BLACK);
		setContent();
	}
	
	private void setContent() {
		setLayout(new GridLayout(HighScoresFileHandler.MAX_NUM_OF_USER_SCORES + 1, 1));
		
		// get the high scores
		List<Map.Entry<String, Integer>> highScores = HighScoresFileHandler.getHighScores();
				
		// set the title label
		DataLabel titleLabel = new DataLabel("HIGH SCORES", SwingConstants.CENTER, 60f);
		add(titleLabel);
		
		// set the score panels
		for (int i = 0; i < HighScoresFileHandler.MAX_NUM_OF_USER_SCORES; i++) {
			String username = (i < highScores.size()) ? highScores.get(i).getKey() : "";
			String score = (i < highScores.size()) ? Integer.toString(highScores.get(i).getValue()) : "";
			
			ScorePanel panel = new ScorePanel(username, score);
            add(panel);
        }
	}
	
	
	/**
	 * 	Simple panel that contains two DataLabels to show username and score.
	 */
	private static class ScorePanel extends JPanel {
		private static final long serialVersionUID = 2986690734773516886L;

		private ScorePanel(String username, String score) {
			setBackground(Color.BLACK);
			setLayout(new GridLayout(1, 2, 40, 0));
			
			add(new DataLabel(username, SwingConstants.RIGHT, 40f));
			add(new DataLabel(score, SwingConstants.LEFT, 40f));
		}
	}
	
	/**
	 * 	Shows the given text.
	 */
	private static class DataLabel extends JLabel {
		private static final long serialVersionUID = -1782706673130947095L;

		private DataLabel(String text, int horizontalAlignment, float size) {
			super(text);
			
			setHorizontalAlignment(horizontalAlignment);
			setVerticalAlignment(SwingConstants.CENTER);
			
			setFont(getFont().deriveFont(Font.BOLD, size));
			setForeground(Color.WHITE);
		}
	}
}
