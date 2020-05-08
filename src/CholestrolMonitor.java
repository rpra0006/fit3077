import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

public class CholestrolMonitor extends PatientMonitor {
	private final String CHOLESTROL_CODE = "2093-3";
	private FhirServer server = new FhirApiAdapter();
	
	public Map<String,Observation> getAllObservation() {
		Map<String,Observation> patientCholestrol = new HashMap<String,Observation>();
		for (Patient patient : this.getAllPatients()) {
			Observation cholestrolObservation = server.getPatientLatestObservation(patient.getIdentifier().get(0).getValue(), CHOLESTROL_CODE);
			String patientName = patient.getName().get(0).getNameAsSingleString();
			patientCholestrol.put(patientName, cholestrolObservation);
		}
		return patientCholestrol;
	}
}
