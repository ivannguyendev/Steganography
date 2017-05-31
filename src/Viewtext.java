import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import java.awt.GridLayout;

public class Viewtext extends JFrame {

	private JPanel contentPane;
	private byte[] msg;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Viewtext frame = new Viewtext();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Viewtext(){
		Default();
	}
	public void Default() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setUndecorated(true);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JTextArea textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		scrollPane.setViewportView(textArea);
		long startTime = new Date().getTime();
//		need 28ms with 34.5kB by JTextArea
		textArea.append(IOMaster.Stringbuffer(msg));
		long endTime = new Date().getTime();
		System.out.println("Time dislay JTextArea run: "+ (endTime-startTime));
//		need 285ms with 34.5kB by JTextPanel
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton btnsave = new JButton("Save");
		panel.add(btnsave);
		
		JButton btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false); //you can't see me!
				dispose(); //Destroy the JFrame object
			}
		});
		panel.add(btnClose);
		btnsave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					IOMaster.writeUTF8Text(Browser.OpenB(), msg);
					JOptionPane.showMessageDialog(null, "Complete!", "Information",JOptionPane.INFORMATION_MESSAGE);
					setVisible(false); //you can't see me!
					dispose(); //Destroy the JFrame object
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					JOptionPane.showMessageDialog(null, "Target File cannot hold message!", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	public Viewtext(byte[] txt) {
		msg = txt;
		Default();
		
		
	}
}
