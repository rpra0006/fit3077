import java.util.ArrayList;

public class SingletonTimerNotifier implements TimerNotifierSubject {
	private static final SingletonTimerNotifier INSTANCE = new SingletonTimerNotifier();
	private final int DEFAULT_UPDATE_TIME = 10;
	
	private ArrayList<TimerNotifierObserver> observers = new ArrayList<TimerNotifierObserver>();
	private int timerSeconds = DEFAULT_UPDATE_TIME;
	
	public static SingletonTimerNotifier getInstance() {
		return INSTANCE;
	}
	
	private SingletonTimerNotifier() {
	}

	public void attach(TimerNotifierObserver o) {
		// TODO Auto-generated method stub
		observers.add(o);
	}

	public void detach(TimerNotifierObserver o) {
		// TODO Auto-generated method stub
		observers.remove(o);
	}

	public void notifyObservers() {
		// TODO Auto-generated method stub
		for(TimerNotifierObserver observer: observers) {
			observer.updateTimer();
		}
		
	}
	
	public void setTime(int seconds) {
		this.timerSeconds = seconds;
		this.notifyObservers();
	}
	
	public int getTime() {
		return this.timerSeconds;
	}

}
