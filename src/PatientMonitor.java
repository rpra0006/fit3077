import java.util.*;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

public abstract class PatientMonitor implements Subject {
	private int secondsToUpdate = 10;
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private ArrayList<Patient> patients = new ArrayList<Patient>();
	private Timer timer;
	
	public PatientMonitor() {
		this.startNotification();
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
		for(Observer o: observers) {
			o.update();
		}
	}
	
	private void startNotification() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				notifyObservers();
			}
		}, 0, secondsToUpdate * 1000);
	}
	
	public void setUpdateTime(int updateTime) {
		this.secondsToUpdate = updateTime;
		timer.cancel();
		this.startNotification();
	}
	
	//add patient to monitor to connect with CholestrolLevelView
	public void addPatient(Patient p) {
		this.patients.add(p);
	}
	
	public void removePatient(int patientIndex) {
		this.patients.remove(patientIndex);
	}
	
	public ArrayList<Patient> getAllPatients(){
		return this.patients;
	}
	
	public abstract Map<String,Observation> getAllObservation();
}