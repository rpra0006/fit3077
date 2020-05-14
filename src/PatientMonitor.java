import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
	
	public String getPatientBdate(String name) {
		for(Patient p: this.patients) {
			if(p.getName().get(0).getNameAsSingleString().compareTo(name) == 0) {
				DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");  
				String strDate = dateFormat.format(p.getBirthDate());  
				return strDate; 
			}
		}
		return "Not found";
	}
	
	public String getPatientGender(String name) {
		for(Patient p: this.patients) {
			if(p.getName().get(0).getNameAsSingleString().compareTo(name) == 0) {
				return p.getGender().getDisplay(); 
			}
		}
		return "Not found";
	}
	
	public String getPatientAddress(String name) {
		for(Patient p: this.patients) {
			if(p.getName().get(0).getNameAsSingleString().compareTo(name) == 0) {
				String address = p.getAddressFirstRep().getLine().toString();
				return address;
			}
		}
		return "Not found";
	}
	
	public String getPatientAddressInfo(String name) {
		for(Patient p: this.patients) {
			if(p.getName().get(0).getNameAsSingleString().compareTo(name) == 0) {
				String city = p.getAddressFirstRep().getCity();
				String state = p.getAddressFirstRep().getState();
				String country = p.getAddressFirstRep().getCountry();
				String s = String.format("%s %s %s", city, state, country);
				return s;
			}
		}
		return "Not found";
	}
	
	
	public abstract Map<Patient, Observation> getAllObservation();
}