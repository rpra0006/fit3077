package model;
public interface TimerNotifierSubject {
	public void attach(TimerNotifierObserver o);
	public void detach(TimerNotifierObserver o);
	public void notifyObservers();
}
