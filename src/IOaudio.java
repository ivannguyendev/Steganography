import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.swing.JOptionPane;



public class IOaudio {
	public static AudioInputStream getAudio(String f){
		AudioInputStream audio = null;
		File file = new File(f);
		try
		{
			
			audio = AudioSystem.getAudioInputStream(file);
		
				
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, 
				"Audio could not be read!","Error",JOptionPane.ERROR_MESSAGE);
		}finally {
//			try {
//				audio.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		}
		return audio;
	}
	
	public static void setAudio(AudioInputStream audio, String path)
	{
		File file = new File(path);
		try{
			AudioSystem.write(audio, AudioFileFormat.Type.WAVE, file);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, 
				"Audio could not be set!","Error",JOptionPane.ERROR_MESSAGE);
		}finally {
			try {
				audio.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
