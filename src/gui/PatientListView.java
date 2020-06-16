package gui;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
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
import javax.swing.JCheckBox;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
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
	private MonitorView latestCholesterolGraphView = new CholesterolGraphView(latestCholesterolMonitor);
	private MonitorView historyBloodTableView;
	private MonitorView historyBloodGraphView;
	
	private int systolicX;
	private int diastolicY;
	
	private List<Patient> allPatients;
	
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
		frame.setBounds(100, 100, 879, 623);
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
		allPatients = server.getAllPractitionerPatients(pracId);
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
		
		JButton btnCholestrolLevel = new JButton("Show Patient Cholesterol Table");
		btnCholestrolLevel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				latestCholesterolTableView.launchScreen();
			}
		});
		btnCholestrolLevel.setBounds(167, 396, 243, 23);
		frame.getContentPane().add(btnCholestrolLevel);
		
		/*
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
		btnAddPatient.setBounds(321, 362, 243, 23);
		frame.getContentPane().add(btnAddPatient);
		*/
		
		JButton btnShowPatientGraph = new JButton("Show Patient Cholesterol Graph");
		btnShowPatientGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				latestCholesterolGraphView.launchScreen();
			}
		});
		btnShowPatientGraph.setBounds(460, 396, 243, 23);
		frame.getContentPane().add(btnShowPatientGraph);
		
		JButton btnShowPatientBlood = new JButton("Show Patient Blood Pressure Table");
		btnShowPatientBlood.setBounds(167, 468, 243, 23);
		frame.getContentPane().add(btnShowPatientBlood);
		
		JButton btnShowBloodPressure = new JButton("Show Blood Pressure History Table");
		btnShowBloodPressure.setBounds(167, 434, 243, 23);
		frame.getContentPane().add(btnShowBloodPressure);
		
		JButton btnShowBloodPressure_1 = new JButton("Show Blood Pressure History Graph");
		btnShowBloodPressure_1.setBounds(460, 434, 243, 23);
		frame.getContentPane().add(btnShowBloodPressure_1);
		
		JCheckBox toggleCholesterolMonitorCheckbox = new JCheckBox("Toggle Cholesterol Monitor");
		toggleCholesterolMonitorCheckbox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					latestCholesterolMonitor.startMonitor();
				}
				else {
					latestCholesterolMonitor.stopMonitor();
				}
			}
		});
		toggleCholesterolMonitorCheckbox.setBounds(167, 501, 243, 23);
		frame.getContentPane().add(toggleCholesterolMonitorCheckbox);
		
		this.frame.setVisible(true);
		table.getModel().addTableModelListener(new CheckBoxModelListener());
		systolicX = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter maximum value for systolic reading (X):"));
		diastolicY = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter maximum value for diastolic reading (Y):"));
	}
	
	private class CheckBoxModelListener implements TableModelListener {
		
		@Override
		public void tableChanged(TableModelEvent e) {
			// TODO Auto-generated method stub
			int row = e.getFirstRow();
            int column = e.getColumn();
            TableModel model = (TableModel) e.getSource();
            Boolean checked = (Boolean) model.getValueAt(row, column);
            
            if (column == 2) {	// Cholestrol Column
                if (checked) {
                	Boolean hasCholesterol = addPatientToCholesterolMonitor(model.getValueAt(row, 0).toString(), row);
                	System.out.println(model.getValueAt(row, 0).toString() + "Add to Cholestrol Table");
                	if(!hasCholesterol) {
                		model.setValueAt(false, row, 2); // untick checkbox if no cholesterol
                	}
                } else {
                	latestCholesterolMonitor.removePatientByName(model.getValueAt(row, 1).toString());
                }
            }
            else {  // Blood Pressure Column
            	if (checked) {
            		System.out.println(model.getValueAt(row, 0).toString() + "Add to Blood Pressure Table");
                } else {
                	latestBloodPressureMonitor.removePatientByName(model.getValueAt(row, 1).toString());
                }
            }
		}
    }
	
	public Boolean addPatientToCholesterolMonitor(String patientIdentifier, int row) {
		
		/*
		if(!latestCholesterolTableView.isRunning()) {
			System.out.println("exit.");
			return; // if view is not running, don't do anything
		}
		*/
		
		final String cholesterolCode = "2093-3";
		
		Patient selectedPatient = allPatients.get(row);
		List<Observation> selectedPatientCholesterol = server.getPatientLatestObservations(patientIdentifier, cholesterolCode, 1);
		
		// Only add patient to monitor which has a cholestrol reading
		if(selectedPatientCholesterol == null) {
			JOptionPane.showMessageDialog(null, "Patient does not have cholesterol reading");
			return false;
		}
		else {
			latestCholesterolMonitor.addPatient(selectedPatient);
			return true;
		}
	}
	
	public void addPatientToBloodPressureMonitor(String patientIdentifier, int row) {
		return;
	}
}
