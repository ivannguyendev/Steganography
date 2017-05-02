import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import com.sun.javafx.scene.layout.region.Margins.Converter;

public class embTextToImages{
	public void Embedded(String filesource, String filedestination){
		BufferedImage bufimg = getImage(filedestination);
		Graphics g = bufimg.getGraphics();
		
		byte msg[] = getText(filedestination).getBytes();
		byte len[]   = Integer.toBinaryString(msg.length).getBytes();
		byte img[]  = get_byte_data(bufimg);
		
		try
		{
			encode_text(img, len,  0); //0 first positiong
			encode_text(img, msg, 32); //4 bytes of space for length: 4bytes*8bit = 32 bits
		}
		catch(Exception e)
		{
			JOptionPane.showMessageDialog(null, 
"Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	public void Encoder(String filesource, String filedestination){
		File fsrc = new File(filesource);
		BufferedImage fdes = getImage(filedestination);
		

	}
	public void Decoder(String filesource, String filedestination){
		File fsrc = new File(filesource);
		BufferedImage fdes = getImage(filedestination);
		

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
	private String getText(String path){
		steganography view = null;;
		String content = null;
		try {
			content = new String(java.nio.file.Files.readAllBytes(new File(path).toPath()));
			return content;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(view, "The File cannot be opened!", 
					"Error!", JOptionPane.INFORMATION_MESSAGE);
		}
		return content;
	}
	private byte[] get_byte_data(BufferedImage image)
	{
		WritableRaster raster   = image.getRaster();
		DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
		return buffer.getData();
	}
}
