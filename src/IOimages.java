import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

public class IOimages {
	public static BufferedImage getImage(String path)
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
				"Image could not be Read!","Error",JOptionPane.ERROR_MESSAGE);
		}
		return image;
	}
	public static void setImage(BufferedImage img, String path)
	{
		File 		file 	= new File(path);
		
		try
		{
			ImageIO.write(img, "png", file);
		}
		catch(Exception ex)
		{
			JOptionPane.showMessageDialog(null, 
				"Image could not be Write!","Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	public static BufferedImage createRGBImage(byte[] bytes, int width, int height) {
	    DataBufferByte buffer = new DataBufferByte(bytes, bytes.length);
	    ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8, 8, 8}, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    return new BufferedImage(cm, Raster.createInterleavedRaster(buffer, width, height, width * 3, 3, new int[]{0, 1, 2}, null), false, null);
	}
}