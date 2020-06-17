package model;
import java.util.*;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

import gui.FhirObserver;

public abstract class PatientMonitor implements FhirSubject, TimerNotifierObserver {
	/*
	 * Holds patient object list and notify observers for updates
	 */
	private int observationsNum; // Number of observations returned on calling getAllPatientMonitors
	private FhirServer server = new FhirApiAdapter();
	private SingletonTimerNotifier timerNotifier = SingletonTimerNotifier.getInstance();
	private ArrayList<FhirObserver> observers = new ArrayList<FhirObserver>();
	private ArrayList<Patient> patients = new ArrayList<Patient>();
	private Timer timer;
	private String observationCode;
	
	public PatientMonitor(String observationCode) {
		this.observationCode = observationCode;
		timerNotifier.attach(this);
	}
	
	@Override
	public void attach(FhirObserver o) {
		observers.add(o);
	}

	@Override
	public void detach(FhirObserver o) {
		observers.remove(o);
	}

	@Override
	public void notifyObservers() {
		for(FhirObserver o: observers) {
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
				try {
					notifyObservers();
				}
				catch(ConcurrentModificationException e) {
					// if this occurs, let it through
					System.out.println("Concurrent modification error");
				}
			}
		}, 0, timerNotifier.getTime() * 1000);
	}
	
	/**
	 * Restart the monitor with new timer value
	 */
	private void restartTimer() {
		timer.cancel();
		this.startMonitor();
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
			notifyObservers();
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
	 * Get all observations of a patient
	 * @return Map<Patient, List<Observation>> (a map of patient object with their list of observations)
	 */
	public Map<Patient, List<Observation>> getAllPatientObservations() {
		Map<Patient, List<Observation>> allPatientObservations = new HashMap<Patient, List<Observation>>();
		ArrayList<Patient> patientList = this.getAllPatients();
		
		for (int i = 0; i < patientList.size(); i++) {
			Patient patient = patientList.get(i);
			// Get patient observation from server
			List<Observation> patientObservationList = server.getPatientLatestObservations(patient.getIdentifier().get(0).getValue(),
					this.getObservationCode(), this.getObservationsNum());
			allPatientObservations.put(patient, patientObservationList);
		}
		return allPatientObservations;
	}
	
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
	
	public int getObservationsNum() {
		return this.observationsNum;
	}
	
	public void setObservationsNum(int newObservationsNum) {
		this.observationsNum = newObservationsNum;
	}
}
