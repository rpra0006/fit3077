package model;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

// Get all observation of patient
public class LatestMonitor extends PatientMonitor {
	/*
	 * Extend the patientmonitor to get the cholestrol levels of a patient
	 */
	private final int MAX_OBSERVATIONS = 1;
	
	public LatestMonitor(String observationCode) {
		super(observationCode);
		this.setObservationsNum(MAX_OBSERVATIONS);
	}
	
}
