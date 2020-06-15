package gui;
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

import model.FhirApiAdapter;
import model.FhirServer;
import model.HistoryMonitor;
import model.LatestMonitor;
import model.PatientMonitor;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.List;
import java.awt.event.ActionEvent;

public class PatientListView {
	/*
	 * Display all the patients of a practitioner in a table
	 */
	
	private JFrame frame;
	private JTable table;
	private String pracId;
	private FhirServer server = new FhirApiAdapter(); // Call server to get all patients
	
	private final String CHOLESTEROL_CODE = "2093-3";
	private final String BLOOD_PRESSURE_CODE = "55284-4";
	
	private PatientMonitor latestCholesterolMonitor = new LatestMonitor(CHOLESTEROL_CODE);
	private PatientMonitor latestBloodPressureMonitor = new LatestMonitor(BLOOD_PRESSURE_CODE);
	private PatientMonitor historyBloodPressureMonitor = new HistoryMonitor(BLOOD_PRESSURE_CODE);
	
	private MonitorView latestCholesterolTableView = new LatestCholesterolTableView(latestCholesterolMonitor);
	private MonitorView latestCholesterolGraphView;
	private MonitorView historyBloodTableView;
	private MonitorView historyBloodGraphView;
	
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
		patientList.setBounds(406, 33, 201, 63);
		frame.getContentPane().add(patientList);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(70, 85, 712, 241);
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
		
		Object[] columns = {"Patient ID", "Patient Name"};
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		table.setModel(model);
		
		// Get all patients of practitioner
		List<Patient> allPatients = server.getAllPractitionerPatients(pracId);
		for (Patient patient : allPatients) {
			String[] row = new String[2];
			row[0] = patient.getIdentifier().get(0).getValue();
			row[1] = patient.getName().get(0).getNameAsSingleString();
			model.addRow(row);	// add to table (patient identifier, patient name)
		}
		
		JLabel lblPatientMonitor = new JLabel("Patient Monitor");
		lblPatientMonitor.setBounds(409, 337, 99, 14);
		frame.getContentPane().add(lblPatientMonitor);
		
		JButton btnCholestrolLevel = new JButton("Show Patient Table");
		btnCholestrolLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// only create single instance of cholesterol view
				if(!latestCholesterolTableView.isRunning()) {
					latestCholesterolTableView.launchScreen();
				}
			}
		});
		btnCholestrolLevel.setBounds(361, 396, 167, 23);
		frame.getContentPane().add(btnCholestrolLevel);
		
		JButton btnAddPatient = new JButton("Add Patient To Monitor");
		btnAddPatient.addActionListener(new ActionListener() {
			// Add patient to monitor
			public void actionPerformed(ActionEvent e) {
				if(!latestCholesterolTableView.isRunning()) {
					return; // if view is not running, don't do anything
				}
				
				final String cholesterolCode = "2093-3";
				int row = table.getSelectedRow();
				
				if(row < 0) {
					JOptionPane.showMessageDialog(null, "Please select a patient to add");
					return;
				}
				
				// confirm existence of cholesterol
				Patient selectedPatient = allPatients.get(row);
				List<Observation> selectedPatientCholesterol = server.getPatientLatestObservations(selectedPatient.getIdentifier().get(0).getValue(), 
						cholesterolCode, 1);
				
				if(latestCholesterolTableView != null) {
					// Only add patient to monitor which has a cholestrol reading
					if(selectedPatientCholesterol == null) {
						JOptionPane.showMessageDialog(null, "Patient does not have cholesterol reading");
					}
					else {
						latestCholesterolTableView.addPatientToMonitor(selectedPatient);
					}
				}
			}
		});
		btnAddPatient.setBounds(361, 362, 167, 23);
		frame.getContentPane().add(btnAddPatient);
		
		this.frame.setVisible(true);
	}
}
