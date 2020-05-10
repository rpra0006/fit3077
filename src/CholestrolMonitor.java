import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

// Get all observation of patient
public class CholestrolMonitor extends PatientMonitor {
	private final String CHOLESTROL_CODE = "2093-3";
	private FhirServer server = new FhirApiAdapter();
	
	public Map<Patient, Observation> getAllObservation() {
		Map<Patient,Observation> patientCholestrol = new HashMap<Patient,Observation>();
		ArrayList<Patient> patientList = this.getAllPatients();
		
		for (int i = 0; i < patientList.size(); i++) {
			Patient patient = patientList.get(i);
			Observation cholestrolObservation = server.getPatientLatestObservation(patient.getIdentifier().get(0).getValue(), CHOLESTROL_CODE);
			patientCholestrol.put(patient, cholestrolObservation);
		}
		return patientCholestrol;
	}
}
