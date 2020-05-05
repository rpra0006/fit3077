import java.util.*;

import org.hl7.fhir.r4.model.Patient;

public class PatientMonitor implements Subject {
	private int secondsToUpdate = 10;
	private String pracId;
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	private FhirServer server = new FhirApiAdapter();
	
	public PatientMonitor(String pracId) {
		this.pracId = pracId;
	}
	
	public ArrayList<Patient> getPatientList(){
		return server.getAllPractitionerPatients(this.pracId);
	}
	
	@Override
	public void attach(Observer o) {
		// TODO Auto-generated method stub
		observers.add(o);
	}

	@Override
	public void detach(Observer o) {
		// TODO Auto-generated method stub
		observers.remove(o);
	}

	@Override
	public void notifyObservers() {
		// TODO Auto-generated method stub
		for(Observer o: observers) {
			o.update();
		}
	}
	
	public void initialize() {
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				notifyObservers();
			}
		}, 0, secondsToUpdate * 1000);
	}
	
	public void setUpdateTime(int updateTime) {
		this.secondsToUpdate = updateTime;
	}
	
	//add patient to monitor connect with CholestrolLevelView
}