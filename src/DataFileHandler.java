import java.net.URI;


/**
 *	Updates the parent classes static methods as needed.
 * 	Has the needed static methods to find absolute path of the data files.
 */
public abstract class DataFileHandler extends SpaceInvadersFileHandler {
	private final static String FOLDER_RELATIVE_PATH = "resources/Data/";
	
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
		return SpaceInvadersFileHandler.getFilePath(FOLDER_RELATIVE_PATH + filename);
	}
}
