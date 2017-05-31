import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JOptionPane;


public class embTextToImages{
	
	public void Encoder(String filesource, String filedestination, String pass){
		BufferedImage bufimg = IOimages.getImage(filedestination);
		byte[] str = null;
		try
		{
			if((IOMaster.sizefile(filesource)*ConstantValue.bitrate) < (bufimg.getWidth()*bufimg.getHeight()*3*4/5)){
				str = IOMaster.readUTF8Text(filesource);
				byte[] hash = keypass.createhash(str.length, pass);
				bufimg = Embedded(bufimg, hash, str) ;
				BufferedImage key = keypass.create(hash);
				ViewImage b = new ViewImage(bufimg, key);
				b.isVisible();
				b.show();
				JOptionPane.showMessageDialog(null, "Successfully! ", "Success",JOptionPane.INFORMATION_MESSAGE);
			}else JOptionPane.showMessageDialog(null, "The File Audio is too short! ", "Alert",JOptionPane.ERROR_MESSAGE);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	public void Decoder(String filesource, String pass){
		BufferedImage img = IOimages.getImage(filesource);
		BufferedImage key = IOimages.getImage((new File(filesource).getParentFile() + "\\key.png"), true);
		byte[] byteArr = Extract(img, key, pass);
		try
		{
			Viewtext b = new Viewtext(byteArr);
			b.show();
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
		
	}
	private BufferedImage Embedded(BufferedImage img, byte[] hash, byte[] byteArr){
		byte[] binArr = new byte[byteArr.length*ConstantValue.bitrate];
		byte[] binfirst = new byte[24*8];
		int k = 0;
		int l = 0;
		int x = 0;
		
		k = 0;
		for(int i = 0; i < 24; i++){
		    for(int in = 0; in < 8; in++, k++){
				binfirst[k] = (byte)(((hash[i] >> (8-in-1))) & 1);
			}
		}
		k = 0;
//		Embbedded ever bit into bitLSB of byte pixel
		binfirst: {for( x = 0; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				int p = img.getRGB(x, y);
			    //get red
			    int r = (p>>16) & 0xff;
			    //get green
			    int g = (p>>8) & 0xff;
			    //get blue
			    int b = p & 0xff;
			    try{
			    	//embedded text to images
					r = (r & ConstantValue.emb_bit)| binfirst[k];
					k++;
					g = (g & ConstantValue.emb_bit)| binfirst[k];
					k++;
					b = (b & ConstantValue.emb_bit)| binfirst[k];
					k++;
					img.setRGB(x, y, (new Color(r,g,b)).getRGB());
			    }catch(Exception e){
				    img.setRGB(x, y, (new Color(r,g,b)).getRGB());
				    break binfirst;
//				    Endpoints: break all loops by block break <label>:{}
			    }   
			}
		}}
//		Convert ever byteArr to 16bit
		k = 0;
		for(int i = 0; i < byteArr.length; i++){
		    for(int in = 0; in < ConstantValue.bitrate; in++, k++){
				binArr[k] = (byte)(((byteArr[i] >> (ConstantValue.bitrate-in-1))) & 1);
//				System.out.print(pb[in]);
			}
		}
//		Embbedded ever bit into bitLSB of byte pixel
		k = 0;
		for( x++ ; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				int p = img.getRGB(x, y);
			    //get red
			    int r = (p>>16) & 0xff;
			    //get green
			    int g = (p>>8) & 0xff;
			    //get blue
			    int b = p & 0xff;
			    try{
			    	//embedded text to images
					r = (r & ConstantValue.emb_bit)| binArr[k];
					k++;
					g = (g & ConstantValue.emb_bit)| binArr[k];
					k++;
					b = (b & ConstantValue.emb_bit)| binArr[k];
					k++;
					img.setRGB(x, y, (new Color(r,g,b)).getRGB());
			    }catch(Exception e){
//			    	Endpoint loop
			    	/* System.out.println(r + " " + g + " " + b);*/
				    //set the pixel value
				    img.setRGB(x, y, (new Color(r,g,b)).getRGB()); 
				    return img;
			    }   
			}
		}
		return img;
	}
	
	private byte[] Extract(BufferedImage img,BufferedImage key, String pass){
//		Get ID key.png
	    byte[] keyArr = keypass.getbyte(key);
//		Get length Images
	    byte[] lengthIMG = new byte[24];
//		for pass + images = ID
	    byte[] tmp;
//	    for return result
		byte[] result = null;
		
//		Xác định chuỗi hash đã lưu trên ảnh, để xem ảnh đó có phải là ảnh phù hợp key.png hay ko?
		int l = 0, x, p = 0;
		byte[] IDimg = new byte[24*8];
Endpoint1:{for(x = 0; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				p = img.getRGB(x, y);
				try{
					 //get red
					IDimg[l] = (byte) ((p>>16) & ConstantValue.exc_bit);
					l++;
				    //get green
					IDimg[l] = (byte) ((p>>8) & ConstantValue.exc_bit);
					l++;
				    //get blue
					IDimg[l] = (byte) (p & ConstantValue.exc_bit);
					l++;
				}catch(Exception e){
					break Endpoint1;
				}
			}
		}}
		for (int i = 0, k = 0; i < 24; i++){
	    	for(int in = 0; in < 8; in++, k+=1)
	    	{
	    		lengthIMG[i] = (byte) ((lengthIMG[i]<<1) | IDimg[k]);
	    	}
	    }
		int length = keypass.getlength(lengthIMG);
		tmp = keypass.createhash(length, pass);
		
//		So sánh ID trong ảnh có phải là ID trong key.png
		if(!Arrays.equals(keyArr, tmp)){
			x+=2;
			length = new Random().nextInt((img.getHeight()*img.getWidth()*3)/ConstantValue.bitrate - (length -1))/16;
		}
		
		byte[] binArr = new byte[length*ConstantValue.bitrate];
		l = 0;
		result = new byte[length];
Endpoint:	for(x++; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				p = img.getRGB(x, y);
				try{
					 //get red
					binArr[l] = (byte) ((p>>16) & ConstantValue.exc_bit);
					l++;
				    //get green
					binArr[l] = (byte) ((p>>8) & ConstantValue.exc_bit);
					l++;
				    //get blue
					binArr[l] = (byte) (p & ConstantValue.exc_bit);
					l++;
				}catch(Exception e){
					break Endpoint;
				}
			}
		}
//		long startTime = new Date().getTime();
//		Need 14ms
		for(int i = 0, k = 0; i < length; i++, k+=16){
			result[i] = (byte) ((result[i]<<1) | binArr[k]);
			result[i] = (byte) ((result[i]<<1) | binArr[k+1]);
			result[i] = (byte) ((result[i]<<1) | binArr[k+2]);
			result[i] = (byte) ((result[i]<<1) | binArr[k+3]);
			result[i] = (byte) ((result[i]<<1) | binArr[k+4]);
			result[i] = (byte) ((result[i]<<1) | binArr[k+5]);
			result[i] = (byte) ((result[i]<<1) | binArr[k+6]);
			result[i] = (byte) ((result[i]<<1) | binArr[k+7]);
			result[i] = (byte) ((result[i]<<1) | binArr[k+8]);
			result[i] = (byte) ((result[i]<<1) | binArr[k+9]);
			result[i] = (byte) ((result[i]<<1) | binArr[k+10]);
			result[i] = (byte) ((result[i]<<1) | binArr[k+11]);
			result[i] = (byte) ((result[i]<<1) | binArr[k+12]);
			result[i] = (byte) ((result[i]<<1) | binArr[k+13]);
			result[i] = (byte) ((result[i]<<1) | binArr[k+14]);
			result[i] = (byte) ((result[i]<<1) | binArr[k+15]);
		}
//		need 24ms
//		 for (int i = 0, k = 0; i < length; i++){
//		    	for(int in = 0; in < ConstantValue.bitrate; in++, k+=1)
//		    	{
//		    		result[i] = (byte) ((result[i]<<1) | binArr[k]);
//		    	}
//		    }
//		 long endTime = new Date().getTime();
//		System.out.println("Time Convert bit to byte run: "+ (endTime-startTime));
		return result;
	}
}
