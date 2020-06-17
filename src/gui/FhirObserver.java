package gui;

public interface FhirObserver {
	/*
	 * Observer class to receive and display data
	 */
	
	/**
	 * Update itself(view) when it receives and update
	 */
	public void update();
}