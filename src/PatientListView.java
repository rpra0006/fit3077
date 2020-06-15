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
	/*
	 * Display all the patients of a practitioner in a table
	 */
	
	private JFrame frame;
	private JTable table;
	private String pracId;
	private FhirServer server = new FhirApiAdapter(); // Call server to get all patients
	private MonitorView cholesterolView = new LatestTableView();
	private MonitorView graphView = new LatestGraphView();
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
				"Patient ID", "Patient Name","Cholestrol Monitor", "Blood Pressure Monitor"
			}
		) {
			Class[] columnTypes = new Class[] {
				Object.class, String.class, Boolean.class, Boolean.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(1).setPreferredWidth(85);
		scrollPane.setViewportView(table);
		
		Object[] columns = {"Patient ID", "Patient Name","Cholestrol Monitor", "Blood Pressure Monitor"};
		DefaultTableModel model = new DefaultTableModel() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return String.class;
                    case 2:
                        return Boolean.class;
                    default:
                        return Boolean.class;
                }
		 	}
		};
		model.setColumnIdentifiers(columns);
		table.setModel(model);
		
		// Get all patients of practitioner
		ArrayList<Patient> allPatients = server.getAllPractitionerPatients(pracId);
		for (Patient patient : allPatients) {
			Object[] row = new Object[4];
			row[0] = patient.getIdentifier().get(0).getValue();
			row[1] = patient.getName().get(0).getNameAsSingleString();
			row[2] = false;
			row[3] = false;
			
			model.addRow(row);	// add to table (patient identifier, patient name)
		}
		
		JLabel lblPatientMonitor = new JLabel("Patient Monitor");
		lblPatientMonitor.setBounds(409, 337, 99, 14);
		frame.getContentPane().add(lblPatientMonitor);
		
		JButton btnCholestrolLevel = new JButton("Show Patient Table");
		btnCholestrolLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// only create single instance of cholestrol view
				if(!cholesterolView.isRunning()) {
					cholesterolView.launchScreen();
				}
			}
		});
		btnCholestrolLevel.setBounds(321, 434, 243, 23);
		frame.getContentPane().add(btnCholestrolLevel);
		
		JButton btnAddPatient = new JButton("Add Patient To Monitor");
		btnAddPatient.addActionListener(new ActionListener() {
			// Add patient to cholestrol monitor
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
					// Only add patient to monitor which has a cholestrol reading
					if(selectedPatientCholesterol == null) {
						JOptionPane.showMessageDialog(null, "Patient does not have cholesterol reading");
					}
					else {
						cholesterolView.addPatientToMonitor(selectedPatient);
						graphView.addPatientToMonitor(selectedPatient);
					}
				}
			}
		});
		btnAddPatient.setBounds(321, 400, 243, 23);
		frame.getContentPane().add(btnAddPatient);
		
		JButton btnShowPatientGraph = new JButton("Show Patient Graph");
		btnShowPatientGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graphView.launchScreen();
			}
		});
		btnShowPatientGraph.setBounds(321, 468, 243, 23);
		frame.getContentPane().add(btnShowPatientGraph);
		
		this.frame.setVisible(true);
	}
}
