import java.awt.Color;
import java.awt.image.BufferedImage;
import java.math.BigInteger;

import javax.swing.JOptionPane;


public class embTextToImages{
	
	public void Encoder(String filesource, String filedestination, String pass){
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
		bufimg = Embedded(bufimg, pass, str) ;
		ViewImage b = new ViewImage(bufimg);
		b.isVisible();
		b.show();
	}
	public void Decoder(String filesource, String pass){
		String str = Encryption.decode(pass, Extract(IOimages.getImage(filesource),pass).substring(ConstantValue.sizedocument));
		try
		{
			Viewtext b = new Viewtext(ConvertUTF8.tostring(str,ConstantValue.bitrate));
			b.show();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
		
	}
	private BufferedImage Embedded(BufferedImage img,String pass,String msg){
		//Convert Value type Long to binary String
		msg = Encryption.encode(pass, ConvertUTF8.toBinary(msg, ConstantValue.bitrate));
		String binlength = Long.toBinaryString((msg.length()+ ConstantValue.sizedocument));
		System.out.println(binlength);
		if(binlength.length() < ConstantValue.sizedocument){
			char[] t = new char[ConstantValue.sizedocument - binlength.length()];
			for(int i = 0; i < t.length; i++) t[i] = '0';
			binlength = String.valueOf(t) + binlength;
		}
		binlength = Encryption.encode(pass, binlength);
		// join BinaryString of length into msg
		msg =  binlength + msg;
		System.out.println(binlength);
		for(int i = 3-(msg.length()%3); i !=0; i--) msg += "0";
		System.out.println(msg.length());
		/*
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
	
	private String Extract(BufferedImage img, String pass){
		String binlength = firstbit(img , ConstantValue.sizedocument);
		System.out.println(binlength);
		binlength = Encryption.decode(pass,binlength);
		System.out.println(binlength);
		long tmp = 0, length = new BigInteger(binlength, 2).longValue();
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
				if(tmp >= length) return result; // End Point of function
				result += (g & ConstantValue.exc_bit);
				tmp++; 
				if(tmp >= length) return result; // End Point of function
				result += (b & ConstantValue.exc_bit);
				tmp++; 
				if(tmp >= length) return result; // End Point of function
			}
		}
		return result;
	}
	private String firstbit(BufferedImage img, int bits){
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
				if(tmp > bits) return result; // End Point of function
				result += (g & ConstantValue.exc_bit);
				tmp++;
				if(tmp > bits) return result; // End Point of function
				result += (b & ConstantValue.exc_bit);
				tmp++;
				if(tmp > bits) return result; // End Point of function
			}
		}
		return result;
	}
}
