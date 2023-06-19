import java.awt.Image;
import java.net.MalformedURLException;

import javax.swing.ImageIcon;


/**
 * 	Helps to find and open image files.
 */
public class ImageFileHandler extends SpaceInvadersFileHandler {
	private final static String FOLDER_RELATIVE_PATH = "resources/Images/";
	
	// prevent instantiation
	private ImageFileHandler() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
	
	/**
	 * 	Returns the Image with the given filename.
	 */
	public static Image getImage(String imageFileName) {
		String relativePath = String.format("%s%s", FOLDER_RELATIVE_PATH, imageFileName);
		Image image = null;
		
		try {
			image = new ImageIcon(getFilePath(relativePath).toURL()).getImage();
		} 
		catch (MalformedURLException e) {
			System.err.printf("Exception occurred while opening image file (%s): %s\n", imageFileName, e.getMessage());
		}
		
		return image;
	}
}