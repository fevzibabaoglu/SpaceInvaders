import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;


/**
 * 	Has the needed static methods to find absolute path of the files.
 */
public abstract class SpaceInvadersFileHandler {
	private final static URI FOLDER_PATH = getFolderPath();
	
	/**
	 * 	Returns the correct String representation of file path.
	 */
	public static String getFilePathString(String filename) {
		return getFilePath(filename).getPath();
	}
	
	/**
	 * 	Returns the absolute path of the file as URI.
	 */
	public static URI getFilePath(String filename) {
		return FOLDER_PATH.resolve(filename);
	}
	
	/**
	 * 	Returns the parent file's absolute path as URI. 
	 * 	(if jar, gets the jar's parent folder path; if bin/, gets the bin's parent folder path)
	 */
	private final static URI getFolderPath() {
		URI parentFilePath = null;
		
		try {
			parentFilePath = new File(SpaceInvadersFileHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().toURI();
		} 
		catch (URISyntaxException e) {
			System.err.printf("Exception occurred when getting folder path: %s\n", e.getMessage());
		}
		
		return parentFilePath;
	}
}
