import java.util.*;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

public abstract class PatientMonitor implements Subject {
	private int secondsToUpdate = 10;
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private ArrayList<Patient> patients = new ArrayList<Patient>();
	private Timer timer;
	
	public PatientMonitor() {
		
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
	
	public void startMonitor() {
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
		this.startMonitor();
	}
	
	//add patient to monitor to connect with CholestrolLevelView
	public void addPatient(Patient p) {
		Boolean exists = false;
		
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
	
	public void removePatientByName(String name) {
		for(Patient p: this.patients) {
			if(p.getName().get(0).getNameAsSingleString().compareTo(name) == 0) {
				this.patients.remove(p);
				return;
			}
		}
	}
	
	public ArrayList<Patient> getAllPatients(){
		return this.patients;
	}
	
	public abstract Map<Patient, Observation> getAllObservation();
	
	public void stopMonitor() {
		timer.cancel();
	}
}