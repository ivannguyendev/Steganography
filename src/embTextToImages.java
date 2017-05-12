import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.swing.JOptionPane;

import com.sun.xml.internal.ws.commons.xmlutil.Converter;


public class embTextToImages{
	
	public void Encoder(String filesource, String filedestination){
		BufferedImage bufimg = IOimages.getImage(filedestination);
		String str = "";
		try
		{
			str = IOMaster.readUTF8Text(filesource);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
		bufimg = Embedded(bufimg, ConvertUTF8.toBinary(	str, ConstantValue.bitrate)) ;
		IOimages.setImage(bufimg, "C:\\Users\\ivannguyen.it\\Desktop\\test\\IMGtest.jpg");
	}
	public void Decoder(String filesource, String filedestination){
		String str = ConvertUTF8.tostring(Extract(IOimages.getImage(filesource)).substring(ConstantValue.bitrate),ConstantValue.bitrate);
		try
		{
			IOMaster.writeUTF8Text(filedestination, str);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
		
	}
	private BufferedImage Embedded(BufferedImage img, String msg){
			
		msg = ConvertUTF8.toBinary(Character.toString((char)((msg.length()+ConstantValue.bitrate))),ConstantValue.bitrate) + msg;
		for(int i = 3-(msg.length()%3); i !=0; i--) msg += "0";
		/*System.out.println(msg.length());
		System.out.println(Integer.parseInt(msg.substring(0, 16), 2));
		System.out.println(msg.substring(0, 16));*/
		char[] charArr = msg.toCharArray(); 
		int tmp = 0;
		for(int x = 0; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				int p = img.getRGB(x, y);

			    //get red
			    int r = (p>>16) & 0xff;

			    //get green
			    int g = (p>>8) & 0xff;

			    //get blue
			    int b = p & 0xff;

			    //embedded text to images
				r = (r & ConstantValue.emb_bit)|((int)charArr[tmp++]-48);
				g = (g & ConstantValue.emb_bit)|((int)charArr[tmp++]-48);
				b = (b & ConstantValue.emb_bit)|((int)charArr[tmp++]-48);
			   /* System.out.println(r + " " + g + " " + b);*/
			    //set the pixel value
			    img.setRGB(x, y, (new Color(r,g,b)).getRGB()); 
			    if(tmp >= charArr.length) return img;
			}
		}
		return img;
	}
	
	private String Extract(BufferedImage img){
		int tmp = 0, length = firstbit(img , ConstantValue.bitrate);
		System.out.println(length);
		String result ="";
		for(int x = 0; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				int p = img.getRGB(x, y);

			    //get red
			    int r = (p>>16) & 0xff;

			    //get green
			    int g = (p>>8) & 0xff;

			    //get blue
			    int b = p & 0xff;
			    //read LSB text of images
				result += (r & ConstantValue.exc_bit); 
				tmp++;	
				result += (g & ConstantValue.exc_bit);
				tmp++; 
				result += (b & ConstantValue.exc_bit);
				tmp++; 
				if(tmp >= length) return result; // End Point of function
			}
		}
		return result;
	}
	private int firstbit(BufferedImage img, int bits){
		bits --;
		int tmp = 0;
		String result = "";
		for(int x = 0; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				int p = img.getRGB(x, y);

			    //get red
			    int r = (p>>16) & 0xff;

			    //get green
			    int g = (p>>8) & 0xff;

			    //get blue
			    int b = p & 0xff;
			    //read LSB text of images
				result += (r & ConstantValue.exc_bit); 
				tmp++;	
				if(tmp > bits) return Integer.parseInt(result, 2); // End Point of function
				result += (g & ConstantValue.exc_bit);
				tmp++; 
				result += (b & ConstantValue.exc_bit);
				tmp++; 
			}
		}
		return -1;
	}
}
