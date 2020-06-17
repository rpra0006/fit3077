package gui;

import javax.swing.JOptionPane;

import org.hl7.fhir.r4.model.Patient;
import model.PatientMonitor;

public class LatestBloodTableView extends TableView {
	private int systolicX;
	private int diastolicY;

	public LatestBloodTableView(PatientMonitor monitor) {
		super(monitor);
	}

	@Override
	public void launchScreen() {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean isRunning() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		systolicX = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter maximum value for systolic reading (X):"));
		diastolicY = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter maximum value for diastolic reading (Y):"));
	}

}
