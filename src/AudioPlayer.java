import java.io.BufferedInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;


/**
 *	Runnable class that plays the given audio.
 */
public class AudioPlayer implements Runnable {
	private final String audioFileName;
	
	public AudioPlayer(String audioFileName) {
		this.audioFileName = audioFileName;
	}
	
	@Override
	public void run() {
		playSound(audioFileName);
	}
	
	private void playSound(String filename) {
		try {
			// create the needed variables
			BufferedInputStream bufferedInputStream = new BufferedInputStream(AudioFileHandler.getInputStream(filename));  
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(bufferedInputStream);
			AudioFormat audioFormat = audioInputStream.getFormat();
			SourceDataLine sourceDataLine = AudioSystem.getSourceDataLine(audioFormat);
			
			// open and start audio output
			sourceDataLine.open(audioFormat);
			sourceDataLine.start();
			
			byte[] buffer = new byte[4096];
			int bytesRead;
			
			// read and write audio data
			while ((bytesRead = audioInputStream.read(buffer)) != -1) {
				sourceDataLine.write(buffer, 0, bytesRead);
			}
			
			// close data line
			sourceDataLine.drain();
			sourceDataLine.stop();
			sourceDataLine.close();
			
			// close stream
			audioInputStream.close();
		}
		catch (Exception e) {
			System.err.printf("Exception occurred while playing the sound (%s): %s\n", filename, e.getMessage());
		}
		
	}
}
