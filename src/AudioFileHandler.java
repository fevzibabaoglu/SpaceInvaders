import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


/**
 * 	Helps to find and open audio files.
 */
public class AudioFileHandler extends SpaceInvadersFileHandler {
	private final static String FOLDER_RELATIVE_PATH = "resources/Sounds/";
	
	// prevent instantiation
	private AudioFileHandler() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
	
	/**
	 * 	Returns the InputStream of the 'audioFileName' file.
	 */
	public static InputStream getInputStream(String audioFileName) {
		String relativePath = String.format("%s%s", FOLDER_RELATIVE_PATH, audioFileName);
		InputStream inputStream = null;
		
		try {
			inputStream = new FileInputStream(getFilePathString(relativePath));
		} 
		catch (FileNotFoundException e) {
			System.err.printf("Exception occurred while opening audio file input stream (%s): %s\n", audioFileName, e.getMessage());
		}
		
		return inputStream;
	}
}
