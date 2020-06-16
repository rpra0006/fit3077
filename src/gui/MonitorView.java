package gui;
import java.awt.EventQueue;

import org.hl7.fhir.r4.model.Patient;

import model.PatientMonitor;

public abstract class MonitorView implements Observer {
	private PatientMonitor monitor;
	private Boolean isRunning = false;
	
	/*
	 * Display contents of data from subject
	 */
	public MonitorView(PatientMonitor monitor) {
		this.monitor = monitor;
		monitor.attach(this);
		initialize();
	}
	
	/**
	 * Starting the screen
	 */
	public void launchScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
					setRunning(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Check if screen is running
	 * @return Boolean
	 */
	public Boolean isRunning() {
		return this.isRunning;
	}
	
	
	/**
	 * Set status of screen running (true or false)
	 */
	public void setRunning(Boolean running) {
		this.isRunning = running;
	}
	
	
	/**
	 * Update the contents of screen 
	 */
	public abstract void update();
	
	/**
	 * Initialize contents of view
	 */
	public abstract void initialize();

}
