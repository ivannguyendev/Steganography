import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import javax.swing.text.View;

import java.awt.Color;

public class ViewAudio extends JFrame {

	private JPanel contentPane;
	AudioInputStream audio;
	Clip clip;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ViewAudio frame = new ViewAudio();
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
	public ViewAudio(){
		Default();
	}
	public void Default() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setUndecorated(true);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton button = new JButton("Save");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IOaudio.setAudio(audio,"testAudio.wav");
			}
		});
		button.setVerticalAlignment(SwingConstants.CENTER);
		button.setToolTipText("L\u01B0u l\u1EA1i k\u1EBFt qu\u1EA3");
		button.setAlignmentX(0.5f);
		panel.add(button);
		
		JButton button_1 = new JButton("Close");
		button_1.setVerticalAlignment(SwingConstants.CENTER);
		button_1.setToolTipText("H\u1EE7y k\u1EBFt qu\u1EA3");
		button_1.setAlignmentX(0.5f);
		panel.add(button_1);
		
		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_2.add(panel_1, BorderLayout.SOUTH);
		
		JLabel lbltime = new JLabel("00:00");
		panel_1.add(lbltime);
		
		JProgressBar progressBar = new JProgressBar();
		panel_1.add(progressBar);
		
		JButton btnplay = new JButton("Play");
		btnplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					clip = AudioSystem.getClip();
					clip.open(audio);
					clip.start();
				} catch (LineUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		panel_1.add(btnplay);
		
		JButton btnStop = new JButton("Stop");
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clip.stop();
			}
		});
		panel_1.add(btnStop);
	}
	public ViewAudio(AudioInputStream a){
		audio =a;
		Default();
	}
}
