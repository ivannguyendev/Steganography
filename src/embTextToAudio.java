import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Date;

import javax.sound.sampled.AudioInputStream;
import javax.swing.JOptionPane;

import oracle.jrockit.jfr.events.Bits;


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
				
//				System.out.println(IOMaster.sizefile(filesource)*ConstantValue.bitrate);
//				System.out.println(str.length());
				audiofile = Embedded(audiofile,byteArr);
				IOaudio.setAudio(audiofile,Browser.OpenB());
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
		try
		{
			byte[] byteArr = Extract(audiofile);
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
		try {
			audio.read(audiobyte);
		   
		} catch (Exception ex) { 
		              // Handle the error...
		    	System.out.println("Audio file error:" + ex);
		}
		return audiobyte;
	}
	
	
	private AudioInputStream Embedded(AudioInputStream audio,byte[] byteArr) throws IOException{
		int k = 0;
	    int i = 1; //start of plaintext in audioBytes
	    int pt;
	    byte[] pb = new byte[ConstantValue.bitrate]; 
		readbyte(audio);
//		encode the length of the plaintext
//		for(int t = 0; t < 100; t++)System.out.println("Byte:" + byteArr[t]);
		pt = byteArr.length;
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
		    
			while(k < byteArr.length){
				//Convert byte to 16bit
				for(int in = 0; in < ConstantValue.bitrate; in++){
					pb[in] = (byte)(((byteArr[k] >> (ConstantValue.bitrate-in-1))) & 1);
//					System.out.print(pb[in]);
				}
//				System.out.println();
//				System.out.println("bit pb: " + new String(pb));
//				System.out.println("bit byteArr[i]: " + Integer.toBinaryString(byteArr[k]));
				for(int in = 0; in < ConstantValue.bitrate ; in++, i+=16){
//					System.out.println("clr: "+Integer.toBinaryString(audiobyte[i]));
					if (pb[in] == 1)// MSB of pb is '1'
						audiobyte[i] = (byte)(audiobyte[i]|1);
					else audiobyte[i] = (byte)(audiobyte[i]&0xfe);
					//if
//					System.out.println("emb: "+audiobyte[i]);
				}
				k++;
			}
//			for(int t = 0; t < 100; t++) System.out.println(Integer.toBinaryString(audiobyte[t]));
			ByteArrayInputStream byteIS = new ByteArrayInputStream(audiobyte);
		    audio = new AudioInputStream(byteIS, audio.getFormat(), audio.getFrameLength());
			return audio;
	}
	
	private byte[] Extract(AudioInputStream audio){
	    int length = 0;
	    int k = 1;
	    readbyte(audio);
	    length = length & 0x00000000;
//	    for(int t = 0; t < 100; t++) System.out.println( Integer.toBinaryString(audiobyte[t]));
	    for (int j=1; j<=32; j++){
	    	length = length << 1;
			if ((audiobyte[k] & 0x01)!=0){
				length = length | 0x01;
			} 
			k++;
	    }// for j
	    //length = length/ConstantValue.bitrate;
	    byte[] byteArr = new byte[length];
	    long startTime = new Date().getTime();
	    
	    for (int i = 0; i < length; i++){
	    	for(int in = 0; in < ConstantValue.bitrate; in++, k++)
	    	{
	    		byteArr[i] = (byte) ((byteArr[i]<<1) | (audiobyte[k]&1));
//	    		System.out.println(audiobyte[k]);
//	    		System.out.println((audiobyte[k]&0x01));
	    	}
//	    	System.out.println();
//	    	System.out.println(Integer.toBinaryString(byteArr[i]));
	    }
	    long endTime = new Date().getTime();
		System.out.println("Time run: "+ (endTime-startTime));
//	    for(int t = 0; t < 100; t++){
//	    	System.out.println( Integer.toBinaryString((audiobyte[k] & 0x01)));
//	    	System.out.println( Integer.toBinaryString(bitArr[t]));
//	    }
	    
//	    System.out.println();
//	    for(int t = 0; t < 200; t++)System.out.print(Integer.toBinaryString(bitArr[t]&0x01));
	    return byteArr;
	}
	
}
