import java.util.*;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

public abstract class PatientMonitor implements Subject, TimerNotifierObserver {
	/*
	 * Holds patient object list and notify observers for updates
	 */
	private SingletonTimerNotifier timerNotifier = SingletonTimerNotifier.getInstance();
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private ArrayList<Patient> patients = new ArrayList<Patient>();
	private Timer timer;
	private String observationCode;
	
	public PatientMonitor(String observationCode) {
		this.observationCode = observationCode;
		timerNotifier.attach(this);
	}
	
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
		System.out.println("Updated this monitor");
		for(Observer o: observers) {
			o.update();
		}
	}
	
	/**
	 * Start the monitor to call server for patient data at a fixed interval
	 */
	public void startTimer() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				notifyObservers();
			}
		}, 0, timerNotifier.getTime() * 1000);
	}
	
	/**
	 * Restart the monitor with new timer value
	 */
	private void restartTimer() {
		timer.cancel();
		this.startTimer();
	}
	
	/**
	 * Update the time interval
	 * @param updateTime (seconds int)
	 */
	public void setUpdateTime(int updateTime) {
		timerNotifier.setTime(updateTime);
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
	 * @return Map<Patient, List<Observation>>
	 */
	public abstract Map<Patient, List<Observation>> getAllPatientObservations();
	
	/**
	 * Stop monitor from receiving data from server
	 */
	public void stopMonitor() {
		timer.cancel();
	}
	
	public void updateTimer() {
		this.restartTimer();
	}
	
	public int getTime() {
		return this.timerNotifier.getTime();
	}
	
	public String getObservationCode() {
		return this.observationCode;
	}
}
