import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.swing.JOptionPane;


public class embTextToAudio {
	byte[] audiobyte;
	
	public void Encoder(String filesource, String filedestination, String pass) throws IOException{
		AudioInputStream audiofile = IOaudio.getAudio(filedestination);
		byte[] byteArr;
		try
		{
			long bytesread = audiofile.getFrameLength();
			int bytesPerFrame = audiofile.getFormat().getFrameSize();

			if((IOMaster.sizefile(filesource)*ConstantValue.bitrate) < (bytesread*bytesPerFrame*4/5)){
				byteArr = IOMaster.readUTF8Text(filesource);
				byte[] hash = keypass.createhash(byteArr.length, pass);
//				System.out.println(IOMaster.sizefile(filesource)*ConstantValue.bitrate);
//				System.out.println(str.length());
				audiofile = Embedded(audiofile, hash, byteArr);
				BufferedImage key = keypass.create(hash);
				String path = Browser.OpenB();
				IOaudio.setAudio(audiofile, path);
				IOimages.setImage(key, new File(path).getParentFile()+ "\\key.png");
				JOptionPane.showMessageDialog(null, "Successfully! ", "Success",JOptionPane.INFORMATION_MESSAGE);
			}else JOptionPane.showMessageDialog(null, "The File Audio is too short! ", "Alert",JOptionPane.ERROR_MESSAGE);
			//
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}

	}
	
	public void Decoder(String filesource, String pass)
	{
		AudioInputStream audiofile = IOaudio.getAudio(filesource);
		BufferedImage key = IOimages.getImage(new File(filesource).getParentFile() + "\\key.png");
		try
		{
			byte[] byteArr = Extract(audiofile, key, pass );
			//str = Encryption.decode(pass,str);
		    Viewtext b = new Viewtext(byteArr);
			b.show();
			JOptionPane.showMessageDialog(null, "Successfully!", "Success",JOptionPane.INFORMATION_MESSAGE);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private byte[] readbyte(AudioInputStream audio)
	{
		long bytesread = audio.getFrameLength();
		int bytesPerFrame = audio.getFormat().getFrameSize();
		audiobyte = new byte[(int)bytesread * bytesPerFrame];
		System.out.println((int)bytesread * bytesPerFrame);
		try {
			audio.read(audiobyte);
		   
		} catch (Exception ex) { 
		              // Handle the error...
		    	System.out.println("Audio file error:" + ex);
		}
		return audiobyte;
	}
	
	
	private AudioInputStream Embedded(AudioInputStream audio, byte[] hash, byte[] byteArr) throws IOException{
		int k = 0, sample = audio.getFormat().getChannels()*audio.getFormat().getFrameSize()*audio.getFormat().getSampleSizeInBits()/8;
	    int i = 0; //start of plaintext in audioBytes
	    byte[] pb = new byte[ConstantValue.bitrate]; 
		readbyte(audio);
//		encode the length of the plaintext
//		for(int t = 0; t < 100; t++)System.out.println("Byte:" + byteArr[t]);
//		Embedded ID into audio
		for(; k < 24; k++){
		    for(int in = 0; in < 8; in++, i++){
				audiobyte[i] = (byte) ((audiobyte[i]&ConstantValue.emb_bit)|(((hash[k] >> (8-in-1))) & 1));
			}
		}
//		nhung text vao audio
		k = 0;
		while(k < byteArr.length){
			//Convert byte to 16bit
			for(int in = 0; in < ConstantValue.bitrate; in++){
				pb[in] = (byte)(((byteArr[k] >> (ConstantValue.bitrate-in-1))) & 1);
//				System.out.print(pb[in]);
			}
//			System.out.println();
//			System.out.println("bit pb: " + new String(pb));
//			System.out.println("bit byteArr[i]: " + Integer.toBinaryString(byteArr[k]));
			for(int in = 0; in < ConstantValue.bitrate ; in++, i+=1){
//				System.out.println("clr: "+Integer.toBinaryString(audiobyte[i]));
				if (pb[in] == 1)// MSB of pb is '1'
					audiobyte[i] = (byte)(audiobyte[i]|1);
				else audiobyte[i] = (byte)(audiobyte[i]&0xfe);
				//if
//				System.out.println("emb: "+audiobyte[i]);
			}
			k++;
		}
//		for(int t = 0; t < 100; t++) System.out.println(Integer.toBinaryString(audiobyte[t]));
		ByteArrayInputStream byteIS = new ByteArrayInputStream(audiobyte);
	    audio = new AudioInputStream(byteIS, audio.getFormat(), audio.getFrameLength());
		return audio;
	}
	
	private byte[] Extract(AudioInputStream audio, BufferedImage key, String pass){
	    int sample = audio.getFormat().getChannels()*audio.getFormat().getFrameSize()*audio.getFormat().getSampleSizeInBits()/8;
//		Get ID key.png
	    byte[] keyArr = keypass.getbyte(key);
//		Get length Images
	    byte[] lengthIMG = new byte[24];
//		for pass + images = ID
	    byte[] tmp;
//	    for return result
		byte[] result = null;
		int k = 0;
		
		readbyte(audio);
//		Read byte ID in Audio		
		for (int i = 0; i < 24; i++){
	    	for(int in = 0; in < 8; in++, k+=1)
	    	{
	    		lengthIMG[i] = (byte) ((lengthIMG[i]<<1) | (audiobyte[k]&ConstantValue.exc_bit));
	    	}
	    }
		int length = keypass.getlength(lengthIMG);
		tmp = keypass.createhash(length, pass);

//		So sánh ID trong audio có phải là ID trong key.png
		if(!Arrays.equals(keyArr, tmp)){
			k+=3;
			length += new Random().nextInt(audiobyte.length/ConstantValue.bitrate-length-1);
		}
		result = new byte[length];
//		long startTime = new Date().getTime();
//		Need 14ms with 34.5kB
		for(int i = 0; i < length; i++, k+=16){
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k]&ConstantValue.exc_bit));
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k+1]&ConstantValue.exc_bit));
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k+2]&ConstantValue.exc_bit));
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k+3]&ConstantValue.exc_bit));
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k+4]&ConstantValue.exc_bit));
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k+5]&ConstantValue.exc_bit));
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k+6]&ConstantValue.exc_bit));
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k+7]&ConstantValue.exc_bit));
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k+8]&ConstantValue.exc_bit));
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k+9]&ConstantValue.exc_bit));
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k+10]&ConstantValue.exc_bit));
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k+11]&ConstantValue.exc_bit));
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k+12]&ConstantValue.exc_bit));
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k+13]&ConstantValue.exc_bit));
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k+14]&ConstantValue.exc_bit));
			result[i] = (byte) ((result[i]<<1) | (audiobyte[k+15]&ConstantValue.exc_bit));
		}
//		need 24ms with 34.5kB
//		 for (int i = 0, k = 0; i < length; i++){
//		    	for(int in = 0; in < ConstantValue.bitrate; in++, k+=1)
//		    	{
//		    		result[i] = (byte) ((result[i]<<1) | binArr[k]);
//		    	}
//		    }
//		 long endTime = new Date().getTime();
//		System.out.println("Time Convert bit to byte run: "+ (endTime-startTime));
		
//	    for(int t = 0; t < 100; t++){
//	    	System.out.println( Integer.toBinaryString((audiobyte[k] & 0x01)));
//	    	System.out.println( Integer.toBinaryString(bitArr[t]));
//	    }
	    
//	    System.out.println();
//	    for(int t = 0; t < 200; t++)System.out.print(Integer.toBinaryString(bitArr[t]&0x01));
	    return result;
	}
	
}
