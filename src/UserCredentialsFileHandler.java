import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * 	Manages the user credentials file.
 */
public class UserCredentialsFileHandler extends DataFileHandler {
	private static final String USER_CREDENTIALS_FILE_PATH = getFilePathString("user_credentials.txt");
	public static final String TOKEN_SPLIT_DELIM = " ";
	private static final int TOKEN_COUNT = 2; 				// tokens are username and password
	private static final int MAX_NUM_OF_USERS = 20;			// max number of users that can be registered
	public static final int MIN_LENGTH = 5;					// min length of username or password
	public static final int MAX_LENGTH = 12;				// max length of username or password
	
	// prevent instantiation
	private UserCredentialsFileHandler() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
	
	/**
	 * 	Create user credentials file.
	 */
	private static boolean createUserCredentialsFile() throws IOException {
		boolean created = false;
		
		File file = new File(USER_CREDENTIALS_FILE_PATH);
		file.getParentFile().mkdirs();
		created = file.createNewFile();
		
		return created;
	}
	
	/**
	 * 	Checks for any existing user with the same username.
	 * 	Creates the file if it does not exists.
	 * 	Checks for any contradiction with the given static settings.
	 * 	Saves the credentials if there were no problem.
	 * 	<p>
	 * 	Exceptions should be handled.
	 */
	public static void saveUserCredentials(String username, String password) throws UserCredentialsException, IOException {
		
		// check for any matching username in the file
		try (BufferedReader reader = new BufferedReader(new FileReader(USER_CREDENTIALS_FILE_PATH))) {
			int lineCount = 0;
			String line;
			
			while ((line = reader.readLine()) != null) {
				String[] credentials = line.split(TOKEN_SPLIT_DELIM);
				lineCount++;
				
				if (username.equals(credentials[0])) {
					throw new UserCredentialsException("The selected username already exists. Please choose a different username.");
				}
			}
			
			if (lineCount >= MAX_NUM_OF_USERS) {
				throw new UserCredentialsException("Maximum number of users created. You cannot register any more users.");
			}
		} 
		// if file does not exist, create it
		catch (FileNotFoundException e) {
			if (!createUserCredentialsFile()) {
				throw new IOException("Unable to create the credentials file.");
			}
		}
		
		// write credentials to the file if there are no contradictions with the settings.
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_CREDENTIALS_FILE_PATH, true))) {
			String credentials = String.format("%s%s%s\n", username, TOKEN_SPLIT_DELIM, password);
			
			if (username.length() < MIN_LENGTH || password.length() < MIN_LENGTH) {
				throw new UserCredentialsException("Username or password is too short.");
			}
			if (username.length() > MAX_LENGTH || password.length() > MAX_LENGTH) {
				throw new UserCredentialsException("Username or password exceeds the character limit.");
			}
			if (credentials.split(TOKEN_SPLIT_DELIM).length != TOKEN_COUNT) {
				throw new UserCredentialsException(String.format("The token count is not correct. "
						+ "Be sure not to use the sequence \"%s\" in your username and password.", TOKEN_SPLIT_DELIM));
			}
			
			writer.write(credentials);
		}
	}
	
	/**
	 * 	Checks whether the given username and password are correct or not.
	 */
	public static boolean checkUserCredentials(String username, String password) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(USER_CREDENTIALS_FILE_PATH))) {
			String line;
			
			while ((line = reader.readLine()) != null) {
				String[] credentials = line.split(TOKEN_SPLIT_DELIM);

				if (username.equals(credentials[0]) && password.equals(credentials[1])) {
					return true;
				}
			}
		} 
		
		return false;
	}
	
	/**
	 * 	Custom Exception to help with user credentials handling.
	 * 	It is used as a message sender.
	 */
	public static class UserCredentialsException extends Exception {
		private static final long serialVersionUID = -5756412324094923685L;

		public UserCredentialsException(String errorMessage) {
			super(errorMessage);
		}
	}
}
