import java.awt.Color;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class keypass {
	public static BufferedImage create(	byte[] hash ){
		BufferedImage image = new BufferedImage( 
                8, 8, BufferedImage.TYPE_INT_RGB );
		short r = 0, g = 0, b = 0, k = 0;
		for(int x = 0; x < 8; x++ )
		{
			r = (short) (hash[k] & 0xff);
			k++;
			g = (short) (hash[k] & 0xff);
			k++;
			b = (short) (hash[k] & 0xff);
			k++;
			image.setRGB(0, x, (new Color(r,g,b)).getRGB());
		}
		for(int x = 1; x < 8; x++ )
			for(int y = 0; y < 8; y++ ){
			Random a = new Random();
			image.setRGB(x, y, a.nextInt()); 
		}
		
		return image;
		
	}
	public static byte[] createhash(int length, String pass){
		MessageDigest hash = null;
		byte[] l  = new byte[24];
		try {
			hash = MessageDigest.getInstance("SHA-1");
//			l = ByteBuffer.allocateDirect(4).putInt(length).array();
			l[0] = (byte) (length >> 24);
			l[1] = (byte) (length >> 16);
			l[2] = (byte) (length >> 8);
			l[3] = (byte) (length );
			byte[] t = hash.digest(pass.getBytes(StandardCharsets.US_ASCII));
			System.arraycopy(t, 0, l, 4, 20);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return l;
	}
	public static int getlength(byte[] byteArr){
		byte[] b = new byte[4];
		b[0] = byteArr[0];
		b[1] = byteArr[1];
		b[2] = byteArr[2];
		b[3] = byteArr[3];
		return ByteBuffer.wrap(b).getInt();
	}
	public static byte[] getpass(byte[] byteArr){
		byte[] b = new byte[20];
		for(int i = 4; i < 24; i++){
		    b[i] = byteArr[i];
		}
		return b;
	}
	public static byte[] getbyte(BufferedImage img){
		byte[] byteArr = new byte[24];
		int p = 0, k = 0;
		for(int i = 0; i < 8; i++){
			p = img.getRGB(0, i);
			byteArr[k] = (byte) (p >> 16);
			k++;
			byteArr[k] = (byte) (p >> 8);
			k++;
			byteArr[k] = (byte) (p);
			k++;
		}
		return byteArr;
	}
}
