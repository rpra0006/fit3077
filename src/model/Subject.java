package model;
import gui.Observer;

public interface Subject {
	/*
	 * Notify observers if there is a change in state of the subject
	 */
	
	/**
	 * Attach an observer object to itself
	 * @param o (Observer object)
	 */
	public void attach(Observer o);
	
	/**
	 * Detach an observer object from itself
	 * @param o (Observer object)
	 */
	public void detach(Observer o);
	
	/**
	 * Notify the observers for updates
	 */
	public void notifyObservers();
}
