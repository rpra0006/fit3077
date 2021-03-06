package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

import model.PatientMonitor;

public class LatestBloodTableView extends TableView {
	/* Display the blood pressure levels of patients and highlight the ones above given x and y values
	 * in a table
	 */
	private JFrame frame;
	private JTable table;
	private JTextField patientNameField;
	private JTextField patientBirthDateField;
	private JTextField patientGenderField;
	private JTextField patientAddressField;
	
	private DefaultTableModel model;
	private JTextField txtSetTimerInterval;
	private JTextField addressInfoField;
	private Boolean isRunning = false;
	private float systolicX;
	private float diastolicY;
	
	private class SystolicBloodCellRenderer extends DefaultTableCellRenderer {
		/*
		 * Set custom cell color to table cell
		 */
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
			Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			final String nullString = "No data";
			String systolicData = (String) value;
			
			c.setForeground(null); //set foreground to null (black) first to prevent lingering highlighting
			
			// return if patient does not have systolic reading
			if(systolicData != nullString) {
				String[] systolicComponents = systolicData.split(" ");
				float systolicValue = Float.parseFloat(systolicComponents[0]);
				
				if(systolicValue > systolicX) {
					c.setForeground(Color.MAGENTA);
				}
			}
			
			return c;
		}
	}
	
	private class DiastolicBloodCellRenderer extends DefaultTableCellRenderer{
		/*
		 * Set custom cell color to table cell
		 */
		private static final long serialVersionUID = 1L;
		
		@Override
		public Component getTableCellRendererComponent(JTable table,Object value,boolean isSelected,boolean hasFocus,int row,int column) {
			Component c = super.getTableCellRendererComponent(table,value,isSelected,hasFocus,row,column);
			final String nullString = "No data";
			String diastolicData = (String) value;
			
			c.setForeground(null); //set foreground to null (black) first to prevent lingering highlighting
			
			// return if patient does not have diastolic reading
			if(diastolicData != nullString) {
				String[] diastolicComponents = diastolicData.split(" ");
				float diastolicValue = Float.parseFloat(diastolicComponents[0]);
				if(diastolicValue > diastolicY) {
					c.setForeground(Color.MAGENTA);
				}
			}
			return c;
		}
	}

	/**
	 * Create the application.
	 */
	public LatestBloodTableView(PatientMonitor monitor, float systolicX, float diastolicY) {
		super(monitor);
		this.systolicX = systolicX;
		this.diastolicY = diastolicY;	
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 1044, 614);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblCholestrolLevels = new JLabel("Blood Pressure Readings");
		lblCholestrolLevels.setBounds(375, 11, 135, 14);
		frame.getContentPane().add(lblCholestrolLevels);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 769, 530);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Name", "Systolic Blood Pressure", "Diastolic Blood Pressure", "Date Issued"
			}
		));
		
		Object[] columns = {"Name", "Systolic Blood Pressure", "Diastolic Blood Pressure", "Date Issued"};
		model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		table.setModel(model);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// Event for mouse click on table, to display patient data
				int i = table.getSelectedRow();
				String patientTableName = model.getValueAt(i, 0).toString();
				ArrayList<Patient> patientList = monitor.getAllPatients();
				
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
			}
		});
		
		table.getColumnModel().getColumn(1).setPreferredWidth(93);
		scrollPane.setViewportView(table);
		
		patientNameField = new JTextField();
		patientNameField.setText("Name");
		patientNameField.setBounds(789, 33, 207, 23);
		frame.getContentPane().add(patientNameField);
		patientNameField.setColumns(10);
		
		txtSetTimerInterval = new JTextField();
		txtSetTimerInterval.setText("Enter time in seconds...");
		txtSetTimerInterval.setBounds(789, 305, 207, 23);
		frame.getContentPane().add(txtSetTimerInterval);
		txtSetTimerInterval.setColumns(10);
		
		JButton btnSetTimer = new JButton("Set Timer");
		btnSetTimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Change timer interval to update data
				int newUpdateTime = Integer.parseInt(txtSetTimerInterval.getText());
				monitor.setUpdateTime(newUpdateTime);
			}
		});
		btnSetTimer.setBounds(809, 339, 173, 23);
		frame.getContentPane().add(btnSetTimer);
		
		patientBirthDateField = new JTextField();
		patientBirthDateField.setText("Birth Date");
		patientBirthDateField.setBounds(789, 67, 207, 23);
		frame.getContentPane().add(patientBirthDateField);
		
		patientGenderField = new JTextField();
		patientGenderField.setText("Gender");
		patientGenderField.setBounds(789, 101, 207, 23);
		frame.getContentPane().add(patientGenderField);
		
		patientAddressField = new JTextField();
		patientAddressField.setText("Address");
		patientAddressField.setBounds(789, 135, 207, 23);
		frame.getContentPane().add(patientAddressField);
		
		addressInfoField = new JTextField();
		addressInfoField.setText("Address Information");
		addressInfoField.setBounds(789, 169, 207, 23);
		frame.getContentPane().add(addressInfoField);
		
		MonitorView instance = this;
		frame.addWindowListener(new WindowAdapter() {
			// Notify if cholestrol monitor is closed
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Monitor closing");
				monitor.detach(instance);
				isRunning = false;
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				System.out.println("Monitor closed");
				monitor.detach(instance);
				isRunning = false;
			}
		});
		frame.setVisible(true);
	}
	
	
	/**
	 * Update table data
	 */
	public void update() {
		//list update
		model.setRowCount(0);
		
		for (Map.Entry<Patient, List<Observation>> patientObservation : monitor.getAllPatientObservations().entrySet()){
			String[] row = new String[4];
			
			Patient patient = patientObservation.getKey();
			Observation observation = patientObservation.getValue().get(0);
			
			String systolicLevel;
			String diastolicLevel;
			String dateIssued;

			// Get systolic level and diastolic level of patient blood pressure
			systolicLevel = observation.getComponent().get(1).getValueQuantity().getValue().floatValue() + " " + observation.getComponent().get(1).getValueQuantity().getUnit();
			diastolicLevel = observation.getComponent().get(0).getValueQuantity().getValue().floatValue() + " " + observation.getComponent().get(1).getValueQuantity().getUnit(); // diastolic blood pressure
			dateIssued = observation.getIssued().toString();
			
			row[0] = patient.getName().get(0).getNameAsSingleString();
			row[1] = systolicLevel;
			row[2] = diastolicLevel;
			row[3] = dateIssued;
			
			model.addRow(row);
		}
		
		setAverageHighlighting();
	}
	
	/**
	 * Return state of monitor, running or not running
	 * @param none
	 * @return Boolean
	 */
	public Boolean isRunning() {
		return this.isRunning;
	}
	
	/**
	 * Update table cell color, magenta if above set value and black if below
	 * @return void
	 */
	private void setAverageHighlighting() {
		this.table.getColumnModel().getColumn(1).setCellRenderer(new SystolicBloodCellRenderer());
		this.table.getColumnModel().getColumn(2).setCellRenderer(new DiastolicBloodCellRenderer());
	}

}
