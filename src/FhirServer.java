import java.util.ArrayList;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

public abstract class FhirServer {
	private final String BASE_URL = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir";
	
	public abstract Patient getPatient(String patientID);
	public abstract ArrayList<Patient> getAllPractitionerPatients(String practitionerID);
	public abstract Observation getPatientLatestObservation(String patientID, String observationCode);
}
