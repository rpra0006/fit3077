import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.hl7.fhir.r4.model.Patient;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class PatientListView implements Observer{

	private JFrame frame;
	private JTable table;
	private String pracId;
	private FhirServer server = new FhirApiAdapter();
	
	/**
	 * Create the application.
	 */
	public PatientListView(String s) {
		this.pracId = s;
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
				"Patient ID", "Patient Name"
			}
		) {
			Class[] columnTypes = new Class[] {
				Object.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(1).setPreferredWidth(85);
		scrollPane.setViewportView(table);
		
		//
		Object[] columns = {"Patient ID", "Patient Name"};
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		table.setModel(model);
		
		ArrayList<Patient> allPatients = server.getAllPractitionerPatients(pracId);
		for (Patient patient : allPatients) {
			String[] row = new String[2];
			row[0] = patient.getIdentifier().get(0).getValue();
			row[1] = patient.getName().get(0).getNameAsSingleString();
			model.addRow(row);
		}
		
		
		//
		JLabel lblPatientMonitor = new JLabel("Patient Monitor");
		lblPatientMonitor.setBounds(181, 147, 99, 14);
		frame.getContentPane().add(lblPatientMonitor);
		
		JButton btnCholestrolLevel = new JButton("Cholestrol Level");
		btnCholestrolLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CholestrolLevelView cholestrolView = new CholestrolLevelView();
				cholestrolView.cholestrolScreen();
			}
		});
		btnCholestrolLevel.setBounds(138, 172, 152, 23);
		frame.getContentPane().add(btnCholestrolLevel);
		this.frame.setVisible(true);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
