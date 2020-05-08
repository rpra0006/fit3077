import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CholestrolLevelView implements Observer {

	private JFrame frame;
	private JTable table;
	private JTextField patientNameField;
	private PatientMonitor patientMonitor = new CholestrolMonitor();
	private DefaultTableModel model;
	private JTextField txtSetTimerInterval;
	
	/**
	 * Launch the application.
	 */
	public void cholestrolScreen() {
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
		frame.setBounds(100, 100, 712, 492);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
				patientNameField.setText(model.getValueAt(i, 0).toString());
			}
		});
		
		table.getColumnModel().getColumn(1).setPreferredWidth(93);
		scrollPane.setViewportView(table);
		
		
		patientNameField = new JTextField();
		patientNameField.setBounds(516, 139, 160, 23);
		frame.getContentPane().add(patientNameField);
		patientNameField.setColumns(10);
		
		JButton btnRemovePatient = new JButton("Remove Patient");
		btnRemovePatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				if (i >= 0) {
					model.removeRow(i);
					patientMonitor.removePatient(i);
				}
				else {
					System.out.println("Delete Error");
				}
			}
		});
		btnRemovePatient.setBounds(538, 174, 124, 23);
		frame.getContentPane().add(btnRemovePatient);
		
		txtSetTimerInterval = new JTextField();
		txtSetTimerInterval.setBounds(516, 223, 160, 23);
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
		btnSetTimer.setBounds(538, 257, 124, 23);
		frame.getContentPane().add(btnSetTimer);
		
		this.patientMonitor.attach(this);
	}
	
	public void addPatientToMonitor(Patient patientData) {
		//Get patient data and update table
		//String patientId = patientData.getIdentifier().get(0).getValue();
		//String patientName = patientData.getName().get(0).getNameAsSingleString();
		
		// get cholestrol level and date for the first time
		//String[] row = new String[3];
		//row[0] = patientName;
		
		//model.addRow(row);
		patientMonitor.addPatient(patientData); //Add to patientMonitor list
	}
	
	private void setPatientDataTimer(int timer) {
		patientMonitor.setUpdateTime(timer);
	}
	
	public void update() {
		//list update
		System.out.println("updated");
		model.setRowCount(0);
		
		for (Map.Entry<String, Observation> patientObservation : patientMonitor.getAllObservation().entrySet()){
			String[] row = new String[3];
			row[0] = patientObservation.getKey();
			
			Observation observation = patientObservation.getValue();
			String cholestrolLevel;
			String dateIssued;
			
			if(observation == null) {
				cholestrolLevel = "No data";
			}
			else{
				cholestrolLevel = observation.getValueQuantity().getValue() + observation.getValueQuantity().getUnit();
				dateIssued = observation.getIssued().toString();
				row[2] = dateIssued;
			}
			row[1] = cholestrolLevel;
			
			model.addRow(row);
		}
		
	}
}
