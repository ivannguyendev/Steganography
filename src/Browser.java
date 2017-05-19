import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;


public class Browser{
	public static String OpenB(){
		JFrame frame = null;
		JFileChooser c = new JFileChooser();
	      // Demonstrate "Open" dialog:
	      int rVal = c.showOpenDialog(frame);
	      if (rVal == JFileChooser.APPROVE_OPTION) {
	       return c.getSelectedFile().getPath();
	      }
	      else return null;
	}
	public static String SaveB(){
		JFrame frame = null;
		JFileChooser c = new JFileChooser();
	      // Demonstrate "Open" dialog:
	      int rVal = c.showOpenDialog(frame);
	      if (rVal == JFileChooser.APPROVE_OPTION) {
	       return c.getSelectedFile().getPath();
	      }
	      else return null;
	}
	public static File Getfile(String str ){
		File file = new File(str);
		return file;
	}

}
