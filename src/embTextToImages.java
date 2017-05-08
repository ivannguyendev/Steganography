import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class embTextToImages{
	
	public void Encoder(String filesource, String filedestination){
		BufferedImage bufimg = getImage(filedestination);
		String str = "";
		try
		{
			str = IOMaster.readUTF8Text(filesource);
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
		bufimg = Embedded(bufimg, toBinary(str, 16)) ;
		setImage(bufimg, "IMGtest.jpg");
	}
	public void Decoder(String filesource, String filedestination){
		try {
			IOMaster.writeUTF8Text("decode.txt", tostring(IOMaster.readUTF8Text(filesource),16));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private BufferedImage Embedded(BufferedImage img, String msg){
		for(int i = 3-(msg.length()%3); i !=0; i--) msg += "0";
		char[] charArr = msg.toCharArray(); 
		int tmp = 0;
		for(int x = 0; x < img.getWidth(); x++){
			for(int y = 0; y < img.getHeight(); y++){
				int p = img.getRGB(x, y);
				
			    //get alpha
			    int a = (p>>24) & 0xff;

			    //get red
			    int r = (p>>16) & 0xff;

			    //get green
			    int g = (p>>8) & 0xff;

			    //get blue
			    int b = p & 0xff;
			    //embedded text to images
				r = (r & 0xfe)|((int)charArr[tmp++]-48);
				g = (g & 0xfe)|((int)charArr[tmp++]-48);
				b = (b & 0xfe)|((int)charArr[tmp++]-48);
			     
			    //set the pixel value
			    p = (a<<24) | (r<<16) | (g<<8) | b;
			    img.setRGB(x, y, p); 
			    if(tmp >= charArr.length) return img;
			}
		}
		return img;
	}
	private BufferedImage getImage(String path)
	{
		BufferedImage 	image	= null;
		File 		file 	= new File(path);
		
		try
		{
			image = ImageIO.read(file);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, 
				"Image could not be read!","Error",JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}
	private void setImage(BufferedImage img, String path)
	{
		File 		file 	= new File(path);
		
		try
		{
			ImageIO.write(img, "jpg", file);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, 
				"Image could not be read!","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	/*private byte[] get_byte_data(BufferedImage image)
	{
		WritableRaster raster   = image.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		return buffer.getData();
	}*/
	
	public static String toBinary(String str, int bits) {
	    String result = "";
	    String tmpStr;
	    int tmpInt;
	    str = (char)str.length() + str;
	    System.out.println(str);
	    char[] messChar = str.toCharArray();

	    for (int i = 0; i < messChar.length; i++) {
	        tmpStr = Integer.toBinaryString(messChar[i]);
	        tmpInt = tmpStr.length();
	        if(tmpInt != bits) {
	            tmpInt = bits - tmpInt;
	            if (tmpInt == bits) {
	                result += tmpStr;
	            } else if (tmpInt > 0) {
	                for (int j = 0; j < tmpInt; j++) {
	                    result += "0";
	                }
	                result += tmpStr;
	            } 
	        } else {
	            result += tmpStr;
	        }        
	    } 
	    
	    return (Integer.toBinaryString(str.length())) + result;
	}
	public static String tostring(String str, int bits) {
		String result = "";
	    for (int i = 0; i < str.length()/bits; i++) {
	        int a = Integer.parseInt(str.substring(bits*i,(i+1)*bits),2);
	        result += (char)(a);
	    }
	    return result;
	}
}
