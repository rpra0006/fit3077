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
	private FhirServer server = new FhirApiAdapter();
	
	public LatestMonitor(String observationCode) {
		super(observationCode);
	}
	
	/**
	 * Get all observations of a patient
	 * @return Map<Patient, List<Observation>> (a map of patient object with their list of observations)
	 */
	public Map<Patient, List<Observation>> getAllPatientObservations() {
		Map<Patient, List<Observation>> allPatientObservations = new HashMap<Patient, List<Observation>>();
		ArrayList<Patient> patientList = this.getAllPatients();
		
		for (int i = 0; i < patientList.size(); i++) {
			ArrayList<Observation> patientObservationList = new ArrayList<Observation>();
			Patient patient = patientList.get(i);
			// Get patient observation from server
			Observation observation = server.getPatientLatestObservation(patient.getIdentifier().get(0).getValue(), this.getObservationCode());
			patientObservationList.add(observation); 
			allPatientObservations.put(patient, patientObservationList);
		}
		return allPatientObservations;
	}
}
