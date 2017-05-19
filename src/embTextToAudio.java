import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.swing.JOptionPane;


public class embTextToAudio {
	byte[] audiobyte;
	public void Encoder(String filesource, String filedestination, String pass) throws IOException{
		AudioInputStream audiofile = IOaudio.getAudio(filedestination);
		String str = "";
		try
		{
			str = IOMaster.readUTF8Text(filesource);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
		audiofile = Embedded(audiofile,Encryption.encode(pass, ConvertUTF8.toBinary(str, ConstantValue.bitrate)));
		IOaudio.setAudio(audiofile,"testAudio.wav");
		JOptionPane.showMessageDialog(null, "Successfully! ", "Success",JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void Decoder(String filesource, String filedestination, String pass)
	{
		AudioInputStream audiofile = IOaudio.getAudio(filesource);
		try
		{
			String str = Extract(audiofile);
			String str1 = Encryption.decode(pass,str);
		    IOMaster.writeUTF8Text(filedestination, ConvertUTF8.tostring(str1,ConstantValue.bitrate));
			JOptionPane.showMessageDialog(null, "Successfully!", "Success",JOptionPane.INFORMATION_MESSAGE);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private byte[] readbyte(AudioInputStream audio)
	{
		long frame = audio.getFrameLength();
		int bytesPerFrame = audio.getFormat().getFrameSize();
		int bytesread = (int)frame * bytesPerFrame;
		audiobyte = new byte[bytesread];
		try {
			audio.read(audiobyte);
		   
		} catch (Exception ex) { 
		              // Handle the error...
		    	System.out.println("Audio file error:" + ex);
		}
		return audiobyte;
	}
	
	
	private AudioInputStream Embedded(AudioInputStream audio, String msg) throws IOException{
		int k = 0;
	    int i = 1; //start of plaintext in audioBytes
	    int pt;
	    char pb; 
	    char[] charArr = msg.toCharArray(); 
		readbyte(audio);
		//encode the length of the plaintext
		pt = msg.length();
		if(pt<(audiobyte.length - 32))
		{
		    for (int j=1; j<=32; j++) {
		    	if ((pt & 0x80000000)!=0) // MSB of pt is '1'
		    		audiobyte[i] = (byte)(audiobyte[i] | 0x01);
		    	else if ((audiobyte[i] & 0x01)!=0){ //MSB of pt '0' and lsb of audio '1'
		    		audiobyte[i] = (byte)(audiobyte[i] >>> 1);
		    		audiobyte[i] = (byte)(audiobyte[i] << 1);
		    	}//if
		    	pt = (int)(pt << 1);
		    	i++;
		    }
		    //nhung text vao audio
			while(k<msg.length()){
				pb = charArr[k];
				if (pb == '1')// MSB of pb is '1'
					audiobyte[i] = (byte)(audiobyte[i] | 0x01);
				else if ((audiobyte[i] & 0x01) !=0 ){ //MSB of pt '0' and lsb of audio '1'
					audiobyte[i] = (byte)(audiobyte[i] >>> 1);
					audiobyte[i] = (byte)(audiobyte[i] << 1);
				}//if
				i++;
				k++;
			}
			ByteArrayInputStream byteIS = new ByteArrayInputStream(audiobyte);
		    audio = new AudioInputStream(byteIS, audio.getFormat(), audio.getFrameLength());
			return audio;
		}
		else
		{
			JOptionPane.showMessageDialog(null, "The audio file is too short for the message to fit!", "Error",JOptionPane.ERROR_MESSAGE);
			return null;
		}
	}
	
	private String Extract(AudioInputStream audio){
		String str = "";
	    int length = 0;
	    int k = 1;
	    readbyte(audio);
	    length = length & 0x00000000;
	    for (int j=1; j<=32; j++){
	    	length = length << 1;
			if ((audiobyte[k] & 0x01)!=0){
				length = length | 0x00000001;
			}
			k++;
	    }// for j
	    for (int i=k; i <= length; i++){
	    	if ((audiobyte[i] & 0x01)!=0){
	    		str += "1";
	    	}
	    	else {
	    		str += "0";
	    	}
	    }
	    return str;
	}
	
}
