import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;

public class PatientListView {

	private JFrame frame;
	private JTable table;
	private String pracId;
	private FhirServer server = new FhirApiAdapter();
	private MonitorView cholesterolView = null;
	
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
		frame.setBounds(100, 100, 879, 571);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel patientList = new JLabel("Patient List");
		patientList.setBounds(422, 32, 201, 63);
		frame.getContentPane().add(patientList);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(135, 85, 647, 241);
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
		
		JLabel lblPatientMonitor = new JLabel("Patient Monitor");
		lblPatientMonitor.setBounds(409, 337, 99, 14);
		frame.getContentPane().add(lblPatientMonitor);
		
		JButton btnCholestrolLevel = new JButton("Cholestrol Level");
		btnCholestrolLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// only create single instance of cholestrol view
				if(cholesterolView == null) {
					cholesterolView = new CholestrolLevelView();
				}
				cholesterolView.launchScreen();
			}
		});
		btnCholestrolLevel.setBounds(361, 392, 167, 23);
		frame.getContentPane().add(btnCholestrolLevel);
		
		JButton btnAddPatient = new JButton("Add Patient To Monitor");
		btnAddPatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				final String cholesterolCode = "2093-3";
				int row = table.getSelectedRow();
				
				if(row < 0) {
					JOptionPane.showMessageDialog(null, "Please select a patient to add");
					return;
				}
				
				Patient selectedPatient = allPatients.get(row);
				Observation selectedPatientCholesterol = server.getPatientLatestObservation(selectedPatient.getIdentifier().get(0).getValue(), cholesterolCode);
				
				if(cholesterolView != null) {
					if(selectedPatientCholesterol == null) {
						JOptionPane.showMessageDialog(null, "Patient does not have cholesterol reading");
					}
					else {
						cholesterolView.addPatientToMonitor(selectedPatient);
					}
				}
			}
		});
		btnAddPatient.setBounds(361, 362, 167, 23);
		frame.getContentPane().add(btnAddPatient);
		
		this.frame.setVisible(true);
	}
}
