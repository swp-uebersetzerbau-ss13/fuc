package swp_compiler_ss13.fuc.gui.ide;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

public class FucIdeAboutView extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					FucIdeAboutView frame = new FucIdeAboutView();
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
	public FucIdeAboutView() {
		this.setTitle("About FUC");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(100, 100, 450, 300);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(new BorderLayout(0, 0));
		this.setContentPane(this.contentPane);

		JTextPane txtpnTxt = new JTextPane();
		txtpnTxt.setEditable(false);
		txtpnTxt.setText("FUC - Freie Universität Berlin Compiler Group\n\nhttps://github.com/swp-uebersetzerbau-ss13/fuc\n\nDesigned and Developed by:\n\nMember\nMember\nMember\nMember\nMember");
		this.contentPane.add(txtpnTxt, BorderLayout.CENTER);

		JButton btnClose = new JButton("close");
		btnClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				FucIdeAboutView.this.dispose();
			}
		});
		this.contentPane.add(btnClose, BorderLayout.SOUTH);
	}

}
