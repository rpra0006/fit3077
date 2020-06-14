package model;

// Get all observation of patient
public class LatestMonitor extends PatientMonitor {
	/*
	 * Extend PatientMonitor to only get the latest monitors
	 */
	private final int MAX_OBSERVATIONS = 1;
	
	public LatestMonitor(String observationCode) {
		super(observationCode);
		this.setObservationsNum(MAX_OBSERVATIONS);
	}
}
