import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.JTextPane;
import javax.swing.JFormattedTextField;

public class CholestrolLevelView extends MonitorView {

	private JFrame frame;
	private JTable table;
	private JTextField patientNameField;
	private JTextField patientBirthDateField;
	private JTextField patientGenderField;
	private JTextField patientAddressField;
	private PatientMonitor patientMonitor = new CholestrolMonitor();
	private DefaultTableModel model;
	private JTextField txtSetTimerInterval;
	private JTextField addressInfoField;
	
	class CholesterolCellRenderer extends DefaultTableCellRenderer {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		float averageChol;
		
		public CholesterolCellRenderer(float averageChol) {
			this.averageChol = averageChol;
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
			Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			final String nullString = "No data";
			String cholesterolData = (String) value;
			
			c.setForeground(null); //set foreground to null (black) first to prevent lingering highlighting
			
			// return if patient does not have cholesterol
			if(cholesterolData != nullString) {
				String[] cholesterolComponents = cholesterolData.split(" ");
				float cholesterolValue = Float.parseFloat(cholesterolComponents[0]);
				
				if(cholesterolValue > averageChol) {
					c.setForeground(Color.RED);
				}
			}
			
			return c;
		}
	}
	
	/**
	 * Launch the application.
	 */
	public void launchScreen() {
		CholestrolLevelView window = this;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//CholestrolLevelView window = new CholestrolLevelView();
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
	public CholestrolLevelView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 764, 492);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblCholestrolLevels = new JLabel("Cholestrol Levels");
		lblCholestrolLevels.setBounds(180, 11, 135, 14);
		frame.getContentPane().add(lblCholestrolLevels);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 470, 408);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Name", "Cholestrol Level", "Date Issued"
			}
		));
		
		Object[] columns = {"Name", "Cholestrol Level", "Date Issued"};
		model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		table.setModel(model);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = table.getSelectedRow();
				String patientTableName = model.getValueAt(i, 0).toString();
				ArrayList<Patient> patientList = patientMonitor.getAllPatients();
				
				Patient selectedPatient = null;
				
				for(Patient p: patientList) {
					if(p.getName().get(0).getNameAsSingleString().equals(patientTableName)){
						selectedPatient = p;
						break;
					}
				}
				
				patientNameField.setText(patientTableName);
				patientBirthDateField.setText(new SimpleDateFormat("dd MMM yyyy").format(selectedPatient.getBirthDate()));
				patientGenderField.setText(selectedPatient.getGender().getDisplay());
				patientAddressField.setText(selectedPatient.getAddressFirstRep().getLine().toString());
				
				String patientCity = selectedPatient.getAddressFirstRep().getCity();
				String patientState = selectedPatient.getAddressFirstRep().getState();
				String patientCountry = selectedPatient.getAddressFirstRep().getCountry();
				String patientFullAddress = String.format("%s %s %s", patientCity, patientState, patientCountry);
				addressInfoField.setText(patientFullAddress);
				
				/* possible code for alert
				String displayString = "";
				
				displayString += "Name: " + patientTableName + "\n";
				displayString += "Date of Birth: " + new SimpleDateFormat("dd MMM yyyy").format(selectedPatient.getBirthDate()) + "\n";
				displayString += "Gender: " + selectedPatient.getGender().getDisplay() + "\n";
				displayString += "Address Line: " + selectedPatient.getAddressFirstRep().getLine().toString() + "\n";
				
				String patientCity = selectedPatient.getAddressFirstRep().getCity();
				String patientState = selectedPatient.getAddressFirstRep().getState();
				String patientCountry = selectedPatient.getAddressFirstRep().getCountry();
				String patientFullAddress = String.format("%s %s %s", patientCity, patientState, patientCountry);
				displayString += "Full Address: " + patientFullAddress;
				
				JOptionPane.showMessageDialog(null, displayString);
				 */
			}
		});
		
		table.getColumnModel().getColumn(1).setPreferredWidth(93);
		scrollPane.setViewportView(table);
		
		patientNameField = new JTextField();
		patientNameField.setText("Name");
		patientNameField.setBounds(516, 33, 207, 23);
		frame.getContentPane().add(patientNameField);
		patientNameField.setColumns(10);
		
		JButton btnRemovePatient = new JButton("Remove Patient");
		btnRemovePatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				String patientName = model.getValueAt(i, 0).toString();
				
				if (i >= 0) {
					model.removeRow(i);
					patientMonitor.removePatientByName(patientName);
					update();
				}
				else {
					System.out.println("Delete Error");
				}
			}
		});
		btnRemovePatient.setBounds(560, 211, 124, 23);
		frame.getContentPane().add(btnRemovePatient);
		
		txtSetTimerInterval = new JTextField();
		txtSetTimerInterval.setText("Enter time in seconds...");
		txtSetTimerInterval.setBounds(516, 332, 207, 23);
		frame.getContentPane().add(txtSetTimerInterval);
		txtSetTimerInterval.setColumns(10);
		
		JButton btnSetTimer = new JButton("Set Timer");
		btnSetTimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Change timer interval to update data
				int second = Integer.parseInt(txtSetTimerInterval.getText());
				setPatientDataTimer(second);
			}
		});
		btnSetTimer.setBounds(560, 366, 124, 23);
		frame.getContentPane().add(btnSetTimer);
		
		patientBirthDateField = new JTextField();
		patientBirthDateField.setText("Birth Date");
		patientBirthDateField.setBounds(516, 67, 207, 23);
		frame.getContentPane().add(patientBirthDateField);
		
		patientGenderField = new JTextField();
		patientGenderField.setText("Gender");
		patientGenderField.setBounds(516, 105, 207, 23);
		frame.getContentPane().add(patientGenderField);
		
		patientAddressField = new JTextField();
		patientAddressField.setText("Address");
		patientAddressField.setBounds(516, 143, 207, 23);
		frame.getContentPane().add(patientAddressField);
		
		addressInfoField = new JTextField();
		addressInfoField.setText("Address Information");
		addressInfoField.setBounds(516, 177, 207, 23);
		frame.getContentPane().add(addressInfoField);
		
		this.patientMonitor.attach(this);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Monitor closing");
				patientMonitor.stopMonitor();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				System.out.println("Monitor closed");
				patientMonitor.stopMonitor();
			}
		});
	}
	
	public void addPatientToMonitor(Patient patientData) {
		patientMonitor.addPatient(patientData); //Add to patientMonitor list
		update();
	}
	
	private void setPatientDataTimer(int timer) {
		patientMonitor.setUpdateTime(timer);
	}
	
	public void update() {
		//list update
		model.setRowCount(0);
		float totalCholesterol = 0;
		int patientCholCount = 0;
		
		for (Map.Entry<Patient, Observation> patientObservation : patientMonitor.getAllObservation().entrySet()){
			String[] row = new String[3];
			
			Patient patient = patientObservation.getKey();
			Observation observation = patientObservation.getValue();
			
			String cholestrolLevel;
			String dateIssued;
			
			// update cholesterol and count for average
			totalCholesterol += observation.getValueQuantity().getValue().floatValue();
			patientCholCount += 1;
				
			cholestrolLevel = observation.getValueQuantity().getValue() + " " +  observation.getValueQuantity().getUnit();
			dateIssued = observation.getIssued().toString();
			
			row[0] = patient.getName().get(0).getNameAsSingleString();
			row[1] = cholestrolLevel;
			row[2] = dateIssued;
			
			model.addRow(row);
		}
		
		float averageCholesterol = totalCholesterol / patientCholCount;
		setAverageHighlighting(averageCholesterol);
	}
	
	private void setAverageHighlighting(float averageCholesterol) {
		this.table.getColumnModel().getColumn(1).setCellRenderer(new CholesterolCellRenderer(averageCholesterol));
	}
	
}
