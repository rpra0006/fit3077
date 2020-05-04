import java.util.*;

public class PatientMonitor implements Subject {
	private Practitioner practitioner;
	private int secondsToUpdate = 10;
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	
	public PatientMonitor() {
		
	}
	
	public PatientMonitor(Practitioner practitioner) {
		this.practitioner = practitioner;
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
}