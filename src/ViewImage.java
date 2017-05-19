import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.eclipse.swt.internal.win32.BROWSEINFO;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.CardLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.SwingConstants;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import net.miginfocom.swing.MigLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.Component;
import javax.swing.JSplitPane;
import java.awt.Window.Type;

public class ViewImage extends JFrame {

	private JPanel contentPane;
	BufferedImage bufimg;
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
		setUndecorated(true);
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
	public ViewImage(BufferedImage buff) {
		bufimg = buff;
		Default();
	}
	
}
