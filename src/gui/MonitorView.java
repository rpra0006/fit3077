package gui;
import org.hl7.fhir.r4.model.Patient;

import model.PatientMonitor;

public abstract class MonitorView implements Observer {
	private PatientMonitor monitor;
	
	/*
	 * Display contents of data from subject
	 */
	public MonitorView(PatientMonitor monitor) {
		this.monitor = monitor;
		monitor.attach(this);
		initialize();
	}
	
	/**
	 * Adding a patient to monitor
	 * @param patientData (Patient object)
	 */
	public abstract void addPatientToMonitor(Patient patientData);
	
	/**
	 * Starting the screen
	 */
	public abstract void launchScreen();
	
	/**
	 * Check if screen is running
	 * @return Boolean
	 */
	public abstract Boolean isRunning();
	
	/**
	 * Update the contents of screen 
	 */
	public abstract void update();
	
	/**
	 * Initialize contents of view
	 */
	public abstract void initialize();

}
