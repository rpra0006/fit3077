import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;

public class pracIdView {

	private JFrame frame;
	private JTextField pracId;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					pracIdView window = new pracIdView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public pracIdView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblEnterPractitionerId = new JLabel("Enter Practitioner ID: ");
		lblEnterPractitionerId.setBounds(57, 98, 126, 33);
		frame.getContentPane().add(lblEnterPractitionerId);
		
		pracId = new JTextField();
		pracId.setBounds(183, 98, 141, 33);
		frame.getContentPane().add(pracId);
		pracId.setColumns(10);
		
		JButton pracIdbutton = new JButton("Enter");
		pracIdbutton.setBounds(163, 166, 109, 23);
		frame.getContentPane().add(pracIdbutton);
	}

}
