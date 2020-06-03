import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

// Get all observation of patient
public class CholestrolMonitor extends PatientMonitor {
	/*
	 * Extend the patientmonitor to get the cholestrol levels of a patient
	 */
	private final String CHOLESTROL_CODE = "2093-3";
	private FhirServer server = new FhirApiAdapter();
	
	public CholestrolMonitor() {
		super();
	}
	
	/**
	 * Get all cholestrol observation of a patient
	 * @return Map<Patient, Observation> (a map of patient object with their observations)
	 */
	public Map<Patient, Observation> getAllObservation() {
		Map<Patient,Observation> patientCholestrol = new HashMap<Patient,Observation>();
		ArrayList<Patient> patientList = this.getAllPatients();
		
		for (int i = 0; i < patientList.size(); i++) {
			Patient patient = patientList.get(i);
			// Get patient cholestrol level observation from server
			Observation cholestrolObservation = server.getPatientLatestObservation(patient.getIdentifier().get(0).getValue(), CHOLESTROL_CODE);
			patientCholestrol.put(patient, cholestrolObservation);
		}
		return patientCholestrol;
	}
}
