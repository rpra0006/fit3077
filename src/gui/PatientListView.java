package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

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
	private MonitorView latestBloodTableView;
	private MonitorView historyBloodTableView = new HistoryBloodTableView(historyBloodPressureMonitor);
	private MonitorView historyBloodGraphView = new HistoryBloodGraphView(historyBloodPressureMonitor);
	private float systolicX;
	
	private List<Patient> allPatients;
	
	/**
	 * Create the application.
	 */
	public PatientListView(String s) {
		this.pracId = s;
		initialize();
	}
	
	private class MonitorCheckboxItemListener implements ItemListener {
		PatientMonitor monitor;
		
		public MonitorCheckboxItemListener(PatientMonitor monitor) {
			this.monitor = monitor;
		}

		@Override
		public void itemStateChanged(ItemEvent e) {
			// TODO Auto-generated method stub
			if(e.getStateChange() == ItemEvent.SELECTED) {
				this.monitor.startMonitor();
			}
			else if(e.getStateChange() == ItemEvent.DESELECTED){
				this.monitor.stopMonitor();
			}
		}
	}
	
	private class ViewButtonActionListener implements ActionListener {
		private MonitorView view;
		
		public ViewButtonActionListener(MonitorView view) {
			this.view = view;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			view.launchScreen();
		}
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
		
		systolicX = Float.parseFloat(JOptionPane.showInputDialog(null, "Enter maximum value for systolic reading (X):"));
		float diastolicY = Float.parseFloat(JOptionPane.showInputDialog(null, "Enter maximum value for diastolic reading (Y):"));
		latestBloodTableView = new LatestBloodTableView(latestBloodPressureMonitor, systolicX, diastolicY);
		
		JLabel lblPatientMonitor = new JLabel("Patient Monitor");
		lblPatientMonitor.setBounds(409, 337, 99, 14);
		frame.getContentPane().add(lblPatientMonitor);
		
		// Set button for showing cholesterol table monitor view
		JButton btnCholestrolLevel = new JButton("Show Patient Cholesterol Table");
		btnCholestrolLevel.addActionListener(new ViewButtonActionListener(latestCholesterolTableView));
		btnCholestrolLevel.setBounds(179, 369, 243, 23);
		frame.getContentPane().add(btnCholestrolLevel);
		
		// Set button for showing cholesterol graph monitor view
		JButton btnShowPatientGraph = new JButton("Show Patient Cholesterol Graph");
		btnShowPatientGraph.addActionListener(new ViewButtonActionListener(latestCholesterolGraphView));
		btnShowPatientGraph.setBounds(179, 403, 243, 23);
		frame.getContentPane().add(btnShowPatientGraph);
		
		// Set button for showing blood pressure table monitor view
		JButton btnShowBloodPressureLatest = new JButton("Show Patient Blood Pressure Table");
		btnShowBloodPressureLatest.addActionListener(new ViewButtonActionListener(latestBloodTableView));
		btnShowBloodPressureLatest.setBounds(179, 437, 243, 23);
		frame.getContentPane().add(btnShowBloodPressureLatest);
		
		// Set button for showing blood pressure history table monitor view
		JButton btnShowBloodPressureHistoryTable = new JButton("Show Blood Pressure History Table");
		btnShowBloodPressureHistoryTable.addActionListener(new ViewButtonActionListener(historyBloodTableView));
		btnShowBloodPressureHistoryTable.setBounds(179, 471, 243, 23);
		frame.getContentPane().add(btnShowBloodPressureHistoryTable);
		
		// Set button for showing blood pressure history graph monitor view
		JButton btnShowBloodPressureHistoryGraph = new JButton("Show Blood Pressure History Graph");
		btnShowBloodPressureHistoryGraph.addActionListener(new ViewButtonActionListener(historyBloodGraphView));
		btnShowBloodPressureHistoryGraph.setBounds(179, 505, 243, 23);
		frame.getContentPane().add(btnShowBloodPressureHistoryGraph);
		
		// Set checkbox for turning on/off cholesterol monitor
		JCheckBox toggleCholesterolMonitorCheckbox = new JCheckBox("Toggle Cholesterol Monitor");
		toggleCholesterolMonitorCheckbox.addItemListener(new MonitorCheckboxItemListener(latestCholesterolMonitor));
		toggleCholesterolMonitorCheckbox.setBounds(491, 382, 243, 23);
		frame.getContentPane().add(toggleCholesterolMonitorCheckbox);
		
		// Set checkbox for turning on/off blood pressure monitor
		JCheckBox chckbxToggleBloodPressure = new JCheckBox("Toggle Blood Pressure Monitor");
		chckbxToggleBloodPressure.addItemListener(new MonitorCheckboxItemListener(latestBloodPressureMonitor));
		chckbxToggleBloodPressure.setBounds(491, 413, 243, 23);
		frame.getContentPane().add(chckbxToggleBloodPressure);
		
		// Set checkbox for turning on/off history monitor
		JCheckBox toggleHistoryMonitor = new JCheckBox("Toggle History Monitor");
		toggleHistoryMonitor.addItemListener(new MonitorCheckboxItemListener(historyBloodPressureMonitor));
		toggleHistoryMonitor.setBounds(491, 464, 243, 23);
		frame.getContentPane().add(toggleHistoryMonitor);
		
		this.frame.setVisible(true);
		table.getModel().addTableModelListener(new CheckBoxModelListener());
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
            		Boolean hasBloodPressure = addPatientToBloodPressureMonitor(model.getValueAt(row, 0).toString(), row);
            		System.out.println(model.getValueAt(row, 0).toString() + "Add to Blood Pressure Table");
            		if(!hasBloodPressure) {
                		model.setValueAt(false, row, 3); // untick checkbox if no blood pressure
                	}
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
		
		Patient selectedPatient = allPatients.get(row);
		List<Observation> selectedPatientCholesterol = server.getPatientLatestObservations(patientIdentifier, CHOLESTEROL_CODE, 1);
		
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
	
	public Boolean addPatientToBloodPressureMonitor(String patientIdentifier, int row) {
		
		Patient selectedPatient = allPatients.get(row);
		List<Observation> selectedPatientBloodPressure = server.getPatientLatestObservations(patientIdentifier, BLOOD_PRESSURE_CODE, 1);
		
		// Only add patient to monitor which has both diastolic and systolic blood pressure reading
		if(selectedPatientBloodPressure == null) {
			JOptionPane.showMessageDialog(null, "Patient does not have blood pressure reading");
			return false;
		}
		else {
			latestBloodPressureMonitor.addPatient(selectedPatient);
			if (selectedPatientBloodPressure.get(0).getComponent().get(1).getValueQuantity().getValue().floatValue() > systolicX) {
				historyBloodPressureMonitor.addPatient(selectedPatient);
			}
			return true;
		}
	}
}
