package model;
import java.util.List;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

public abstract class FhirServer {
	/*
	 * Server abstract class to use as an interface for fetching data from the rest of the system
	 */
	private final String BASE_URL = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir";
	
	public abstract Patient getPatient(String patientIdentifier);
	public abstract List<Patient> getAllPractitionerPatients(String practitionerIdentifier);
	public abstract List<Observation> getPatientLatestObservations(String patientIdentifier, String observationCode, int amountToReturn);
}
