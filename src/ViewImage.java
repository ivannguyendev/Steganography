import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import java.awt.Component;

public class ViewImage extends JFrame {

	private JPanel contentPane;
	BufferedImage bufimg;
	BufferedImage buffkey;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewImage frame = new ViewImage();
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
	public ViewImage(){
		setType(Type.POPUP);
		Default();
	}
	public void Default() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setUndecorated(true);
		setBounds(100, 100, 545, 390);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path =  Browser.OpenB();
				if( path != null) {
					IOimages.setImage(bufimg,path);
					IOimages.setImage(buffkey,new File(path).getParentFile()+ "\\key.png");
					setVisible(false);
					dispose();
				}
			}
		});
		btnSave.setToolTipText("L\u01B0u l\u1EA1i k\u1EBFt qu\u1EA3");
		btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
		btnSave.setVerticalAlignment(SwingConstants.CENTER);
		panel.add(btnSave);
		
		JButton btnclose = new JButton("Close");
		btnclose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		btnclose.setToolTipText("H\u1EE7y k\u1EBFt qu\u1EA3");
		btnclose.setVerticalAlignment(SwingConstants.CENTER);
		btnclose.setAlignmentX(0.5f);
		panel.add(btnclose);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JLabel lblImg = new JLabel("");
		scrollPane.setViewportView(lblImg);
		lblImg.setIcon(new ImageIcon(bufimg));
	}
	public ViewImage(BufferedImage buff, BufferedImage key) {
		bufimg = buff;
		buffkey = key;
		Default();
	}
	
}
