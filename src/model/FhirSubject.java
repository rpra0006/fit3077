package model;
import main.FhirObserver;

public interface FhirSubject {
	/*
	 * Notify observers if there is a change in state of the subject
	 */
	
	/**
	 * Attach an observer object to itself
	 * @param o (Observer object)
	 */
	public void attach(FhirObserver o);
	
	/**
	 * Detach an observer object from itself
	 * @param o (Observer object)
	 */
	public void detach(FhirObserver o);
	
	/**
	 * Notify the observers for updates
	 */
	public void notifyObservers();
}
