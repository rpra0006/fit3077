import java.util.ArrayList;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

public abstract class FhirServer {
	/*
	 * Server abstract class to use as an interface for fetching data from the rest of the system
	 */
	private final String BASE_URL = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir";
	
	public abstract Patient getPatient(String patientIdentifier);
	public abstract ArrayList<Patient> getAllPractitionerPatients(String practitionerIdentifier);
	public abstract Observation getPatientLatestObservation(String patientIdentifier, String observationCode);
}
