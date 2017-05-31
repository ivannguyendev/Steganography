import java.awt.image.BufferedImage;
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
}