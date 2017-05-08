import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.spi.CharsetProvider;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;


public class embTextToImages{
	
	public void Encoder(String filesource, String filedestination){
		BufferedImage bufimg = getImage(filedestination);
		
		try
		{
			Embedded(bufimg, toBinary(IOMaster.readUTF8Text(filesource), 16)) ;
			
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}

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
	private void Embedded(BufferedImage img, String msg){
        
		
	}
	private BufferedImage getImage(String f)
	{
		BufferedImage 	image	= null;
		File 		file 	= new File(f);
		
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
	private byte[] get_byte_data(BufferedImage image)
	{
		WritableRaster raster   = image.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		return buffer.getData();
	}
	
	public static String toBinary(String str, int bits) {
	    String result = "";
	    String tmpStr;
	    int tmpInt;
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
	    return result;
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
