import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * 	Manages the high score file.
 */
public class HighScoresFileHandler extends DataFileHandler {
	private static final String HIGH_SCORES_FILE_PATH = getFilePathString("high_scores.txt");
	private static final String TEMP_FILE_PATH = getFilePathString("high_scores_TEMP.txt");
	private static final String TOKEN_SPLIT_DELIM = " ";
	public static final int MAX_NUM_OF_USER_SCORES = 8;
	
	// prevent instantiation
	private HighScoresFileHandler() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
	
	/**
	 * 	Create original or temp high scores file.
	 */
	private static boolean createHighScoresFile(boolean tempFile) throws IOException {
		final String filePath = (tempFile) ? TEMP_FILE_PATH : HIGH_SCORES_FILE_PATH;
		boolean created = false;
		
		File file = new File(filePath);
		file.getParentFile().mkdirs();
		created = file.createNewFile();
		
		return created;
	}
	
	/**
	 * 	Reads the high score file. 
	 * 	Keeps the old score of the user, if there is.
	 * 	Creates a temp file which is the copy of the original file except the data of the user.
	 * 	Deletes the original file and renames temp file to original file.
	 * 	Appends the highest score of the user, which is max(oldScore, newScore), to the high score file.
	 */
	public static void saveScore(String username, int score) throws IOException {
		int oldScore = 0;
		
		// if high scores file does not exist, create it
		if (!(new File(HIGH_SCORES_FILE_PATH)).exists()) {
			if (!createHighScoresFile(false)) {
				throw new IOException("Unable to create the high scores file.");
			}
		}
		
		// else; remove the line containing 'username' if there is, and save its old score
		else {
			
			// create a temp file
			if (!createHighScoresFile(true)) {
				throw new IOException("Unable to create the high scores temp file.");
			}
			
			File originalFile = new File(HIGH_SCORES_FILE_PATH);
	        File tempFile = new File(TEMP_FILE_PATH);
			
	        // rewrite the original file to temp file without the line containing username
			try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
				 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
				
				String line;
				while ((line = reader.readLine()) != null) {
					String[] tokens = line.split(TOKEN_SPLIT_DELIM);
					
					// if it is 'username's line, save the old score
					if (username.equals(tokens[0])) {
						oldScore = Integer.parseInt(tokens[1]);
					}
					
					// else, write the line to temp file
					else {
						writer.write(line);
						writer.newLine();
					}
				}
				
				// close and save the files
				reader.close();
				writer.close();
			
				// delete the original file
				if (originalFile.delete()) {
					// rename the temp file
					if (!tempFile.renameTo(originalFile)) {
						throw new IOException("Unable to rename the temp file.");
					}
				}
				else {
					throw new IOException("Unable to delete high scores file. (This had to be done in order to rename temp file.)");
				}
			}
		}
		
		
		// append username and score to high scores file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORES_FILE_PATH, true))) {
			writer.write(String.format("%s%s%d\n", username, TOKEN_SPLIT_DELIM, Math.max(score, oldScore)));
		}
	}
	
	/**
	 *  Returns the highest 'MAX_NUM_OF_USER_SCORES' scores as a sorted entry list.
	 *  If there are less than 'MAX_NUM_OF_USER_SCORES' scores, it returns all.
	 *  If exception occurs, it returns empty list.
	 */
	public static ArrayList<Map.Entry<String, Integer>> getHighScores() {
		HashMap<String, Integer> highScores = new HashMap<String, Integer>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(HIGH_SCORES_FILE_PATH))) {
			String line;
			
			while ((line = reader.readLine()) != null) {
				String[] credentials = line.split(TOKEN_SPLIT_DELIM);
				highScores.put(credentials[0], Integer.parseInt(credentials[1]));
			}
		}
		catch (IOException e) {
			return new ArrayList<Map.Entry<String, Integer>>();
		} 
		
		// convert map to list
		List<Map.Entry<String, Integer>> highScoresEntryList = new ArrayList<>(highScores.entrySet());
		
		// Map.Entry::getValue is method reference (extracts the value of entry)
		// use getValue() function as a method for comparison
		// sort in reverse order to get descending list
		Collections.sort(highScoresEntryList, Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()));
		
		// get the highest 'MAX_NUM_OF_USER_SCORES' scores
		List<Map.Entry<String, Integer>> highestScoresEntryList = (highScoresEntryList.size() > MAX_NUM_OF_USER_SCORES) 
				? highScoresEntryList.subList(0, MAX_NUM_OF_USER_SCORES) : highScoresEntryList;
		
		return (ArrayList<Entry<String, Integer>>) highestScoresEntryList;
	}
}
