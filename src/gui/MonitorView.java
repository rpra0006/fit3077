package gui;
import org.hl7.fhir.r4.model.Patient;

public abstract class MonitorView implements Observer {
	/*
	 * Display contents of data from subject
	 */
	
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

}
