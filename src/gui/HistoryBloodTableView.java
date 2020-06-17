package gui;

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
import javax.swing.table.DefaultTableModel;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

import model.PatientMonitor;

public class HistoryBloodTableView extends TableView {
	
	private JFrame frame;
	private JTable table;
	private DefaultTableModel model;
	private Boolean isRunning = false;
	
	public HistoryBloodTableView(PatientMonitor monitor) {
		super(monitor);
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		frame = new JFrame();
		frame.setBounds(100, 100, 804, 614);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblCholestrolLevels = new JLabel("Patient Systolic Blood Pressure Historical Table");
		lblCholestrolLevels.setBounds(280, 11, 274, 14);
		frame.getContentPane().add(lblCholestrolLevels);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 770, 530);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Patient Systolic Blood Pressure Historical Data"
			}
		));
		
		Object[] columns = {"Patient Systolic Blood Pressure Historical Data"};
		model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		table.setModel(model);
		table.getColumnModel().getColumn(1).setPreferredWidth(93);
		scrollPane.setViewportView(table);
		
		frame.addWindowListener(new WindowAdapter() {
			// Notify if cholestrol monitor is closed
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Monitor closing");
				monitor.stopMonitor();
				isRunning = false;
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				System.out.println("Monitor closed");
				monitor.stopMonitor();
				isRunning = false;
			}
		});
		monitor.startMonitor();
		frame.setVisible(true);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		model.setRowCount(0);
		
		for (Map.Entry<Patient, List<Observation>> patientObservation : monitor.getAllPatientObservations().entrySet()){
			
			String[] row = new String[1];
			
			Patient patient = patientObservation.getKey();
			List<Observation> observation = patientObservation.getValue();
			
			String patientSystolicString = "";
			
			for (Observation systolicValue : observation) {
				patientSystolicString += systolicValue.getComponent().get(1).getValueQuantity().getValue() + " (" 
						+ systolicValue.getIssued().toString() + ") ,";
			}
			
			row[0] = patient.getName().get(0).getNameAsSingleString() + ": " +  patientSystolicString;

			model.addRow(row);
		}
	}
	
	/**
	 * Return state of monitor, running or not running
	 * @param none
	 * @return Boolean
	 */
	public Boolean isRunning() {
		return this.isRunning;
	}
	
}
