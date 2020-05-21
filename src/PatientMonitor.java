import java.util.*;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

public abstract class PatientMonitor implements Subject {
	/*
	 * Holds patient object list and notify observers for updates
	 */
	private int secondsToUpdate = 10;
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private ArrayList<Patient> patients = new ArrayList<Patient>();
	private Timer timer;
	
	
	@Override
	public void attach(Observer o) {
		observers.add(o);
	}

	@Override
	public void detach(Observer o) {
		observers.remove(o);
	}

	@Override
	public void notifyObservers() {
		for(Observer o: observers) {
			o.update();
		}
	}
	
	/**
	 * Start the monitor to call server for patient data at a fixed interval
	 */
	public void startMonitor() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				notifyObservers();
			}
		}, 0, secondsToUpdate * 1000);
	}
	
	/**
	 * Update the time interval
	 * @param updateTime (seconds int)
	 */
	public void setUpdateTime(int updateTime) {
		this.secondsToUpdate = updateTime;
		timer.cancel();
		this.startMonitor();
	}
	
	/**
	 * Add patient to monitor to connect with CholestrolLevelView
	 * @param p (Patient object)
	 */
	public void addPatient(Patient p) {
		Boolean exists = false;
		
		// Only add patients to monitor that is not currently being monitored
		for(Patient existingPatient: this.patients) {
			String existingPatientId = existingPatient.getId();
			String currentPatientId = p.getId();
			
			if(existingPatientId.equals(currentPatientId)) {
				exists = true;
				break;
			}
		}
		
		if(!exists) {
			this.patients.add(p);
		}
		System.out.println(this.patients.size());
	}
	
	/**
	 * Remove patient from monitor
	 * @param name (Patient name string)
	 */
	public void removePatientByName(String name) {
		for(Patient p: this.patients) {
			// Find patient name in patient list
			if(p.getName().get(0).getNameAsSingleString().compareTo(name) == 0) {
				this.patients.remove(p);
				return;
			}
		}
	}
	
	/**
	 * Get all patients of practitioner
	 * @return ArrayList<Patient>
	 */
	public ArrayList<Patient> getAllPatients(){
		return this.patients;
	}
	
	/**
	 * Get all observation of patient
	 * @return Map<Patient, Observation>
	 */
	public abstract Map<Patient, Observation> getAllObservation();
	
	/**
	 * Stop monitor from recieving data from server
	 */
	public void stopMonitor() {
		timer.cancel();
	}
}