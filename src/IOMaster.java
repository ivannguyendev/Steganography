import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Arrays;

public class IOMaster {
	 private IOMaster (){}

	    public static byte[] readUTF8Text(String file) throws FileNotFoundException, UnsupportedEncodingException, IOException{
//	    	long startTime = new Date().getTime();
//	    	String sR = null, sNewLine;
	    	FileInputStream fi = new FileInputStream(file);
//	    	CharBuffer cbuf = null;
	    	
	    	FileChannel fc = fi.getChannel();
	    	
	    	byte[] byteArr = new byte[(int)IOMaster.sizefile(file)];
	    	Arrays.fill(byteArr, (byte)0);
	    	int posArr =0;
//	    	System.out.println((int)IOMaster.sizefile(file));
	    	
	    	ByteBuffer bbuf = ByteBuffer.allocateDirect(ConstantValue.sizebufferNIO);
	    	int nRead;
	    	while ( (nRead=fc.read( bbuf )) != -1 )
	    	{
	    		bbuf.flip();
	    		byte[] tmp = new byte[nRead];
	    		bbuf.get(tmp);
//	    		System.out.println("value tmp:" + new String(tmp,"UTF-8"));
//	    		System.out.println("value Arr:" + new String(byteArr, "utf-8"));
	    		System.arraycopy(tmp, 0, byteArr, posArr, nRead);
	    		posArr += nRead;
//	    	    cbuf = Charset.forName("utf-8").decode(bbuf);
//	    	    sR += String.valueOf(cbuf.array()) + "a";
//	    	    System.out.println("value buf:" + String.valueOf(cbuf.array()));
	    	    bbuf.clear( );  
	    	}
	    	fc.close();
	    	fi.close();
//	    	for(int i = 0; i < 100; i++)System.out.println("Byte:" + Integer.toBinaryString(byteArr[i]));
//	    	System.out.println("Charbuffer cbuf:" + String.valueOf(cbuf));
//	    	System.out.println("value sR:" + sR);
//	        long endTime = new Date().getTime();
//			System.out.println(endTime-startTime);
//	    	Viewtext b = new Viewtext(byteArr);
//	    	b.show();
	        return byteArr;
	    }

	    public static void writeUTF8Text(String file, byte[] text) throws FileNotFoundException, UnsupportedEncodingException, IOException{
	        writeUTF8Text(file, text, false);
	    }

	    public static void writeUTF8Text(String file, byte[] text, boolean append) throws FileNotFoundException, UnsupportedEncodingException, IOException{
	        FileOutputStream fo = new FileOutputStream(file, append);
	        FileChannel fc = fo.getChannel();
	       
	        ByteBuffer buf = ByteBuffer.allocateDirect(ConstantValue.sizebufferNIO);
	        buf.flip();
	        buf = ByteBuffer.wrap(text);
	        fc.write(buf);
	        fc.close();
	        fo.close();
	    }
	    public static long sizefile(String file){
	    	File f = new File(file);
	    	//System.out.println(f.length());
	    	return f.length();
	    }
	    public static String Stringbuffer(byte[] text) {
	        ByteBuffer buf = ByteBuffer.allocateDirect(ConstantValue.sizebufferNIO);
	        buf.flip();
	        buf = ByteBuffer.wrap(text);
	        CharBuffer cbuf = Charset.forName("UTF-8").decode(buf);
	        return new String(cbuf.array());
	    }
}
