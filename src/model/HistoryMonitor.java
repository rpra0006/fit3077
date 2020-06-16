package model;

public class HistoryMonitor extends PatientMonitor {
	/*
	 * Extend PatientMonitor to only get the historical monitors
	 */
	private final int MAX_OBSERVATIONS = 5;
	
	public HistoryMonitor(String observationCode) {
		super(observationCode);
		this.setObservationsNum(MAX_OBSERVATIONS);
	}

}
