import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class patientListView {

	private JFrame frame;
	private JTable table;
	
	
	/**
	 * Launch the application.
	 */
	public static void patientScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					patientListView window = new patientListView();
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
	public patientListView() {
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
		
		JLabel patientList = new JLabel("Patient List");
		patientList.setBounds(188, 11, 81, 14);
		frame.getContentPane().add(patientList);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(76, 39, 305, 94);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Given Name", "Family Name"
			}
		) {
			Class[] columnTypes = new Class[] {
				Object.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		scrollPane.setViewportView(table);
		
		JLabel lblPatientMonitor = new JLabel("Patient Monitor");
		lblPatientMonitor.setBounds(181, 147, 99, 14);
		frame.getContentPane().add(lblPatientMonitor);
		
		JButton btnCholestrolLevel = new JButton("Cholestrol Level");
		btnCholestrolLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cholestrolLevelView cholestrolView = new cholestrolLevelView();
				cholestrolView.cholestrolScreen();
			}
		});
		btnCholestrolLevel.setBounds(50, 172, 152, 23);
		frame.getContentPane().add(btnCholestrolLevel);
	}
}
