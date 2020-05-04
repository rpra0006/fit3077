import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PracIdView {

	private JFrame frmFit;
	private JTextField pracId;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					PracIdView window = new PracIdView();
					window.frmFit.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public PracIdView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFit = new JFrame();
		frmFit.setTitle("FIT3077");
		frmFit.setBounds(100, 100, 450, 300);
		frmFit.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmFit.getContentPane().setLayout(null);
		
		JLabel lblEnterPractitionerId = new JLabel("Enter Practitioner ID ");
		lblEnterPractitionerId.setBounds(163, 72, 126, 33);
		frmFit.getContentPane().add(lblEnterPractitionerId);
		
		pracId = new JTextField();
		pracId.setBounds(148, 99, 141, 33);
		frmFit.getContentPane().add(pracId);
		pracId.setColumns(10);
		
		JButton pracIdbutton = new JButton("Enter");
		pracIdbutton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = pracId.getText();
				PatientListView patientList = new PatientListView(s);
				//patientList.patientScreen();
				frmFit.dispose();
			}
		});
		pracIdbutton.setBounds(163, 143, 109, 23);
		frmFit.getContentPane().add(pracIdbutton);
	}

}
