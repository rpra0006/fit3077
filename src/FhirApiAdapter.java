import java.util.ArrayList;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.*;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class FhirApiAdapter extends FhirServer {
	private final String BASE_URL = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/";
	private FhirContext ctx = FhirContext.forR4();
	private IParser parser = ctx.newJsonParser().setPrettyPrint(true);
	private IGenericClient client = ctx.newRestfulGenericClient(BASE_URL);
	
	@Override
	public Patient getPatient(String patientIdentifier) {
		final String PATIENT_SEARCH_URL = "Patient?identifier=https://github.com/synthetichealth/synthea|" + patientIdentifier;
		Bundle patientBundle = client.search().byUrl(BASE_URL + PATIENT_SEARCH_URL).returnBundle(Bundle.class).execute();
		Patient patient = (Patient) patientBundle.getEntry().get(0).getResource();
		return patient;
	}

	@Override
	public ArrayList<Patient> getAllPractitionerPatients(String practitionerID) {
		final String ENCOUNTER_SEARCH_URL = "Encounter?participant.identifier=http://hl7.org/fhir/sid/us-npi|" + practitionerID;
		ArrayList<Patient> patientList = new ArrayList<Patient>();
		
		Bundle allEncounters = client.search().byUrl(BASE_URL + ENCOUNTER_SEARCH_URL).returnBundle(Bundle.class).execute();
		
		for(BundleEntryComponent entry: allEncounters.getEntry()) {
			Encounter encounter = (Encounter) entry.getResource();
			String patientURL = encounter.getSubject().getReference();
			
			Patient patient = client.read().resource(Patient.class).withUrl(BASE_URL + patientURL).execute();
			patientList.add(patient);
		}
		return patientList; //contains duplicates 
	}

	@Override
	public Observation getPatientLatestObservation(String patientIdentifier, String observationCode) {
		final String OBSERVATION_SEARCH_URL = "Observation?patient.identifier=https://github.com/synthetichealth/synthea|" + patientIdentifier + 
				"&code=" + observationCode + "&_sort=-date"; // sort in descending order based on date
		
		Bundle allObservations = client.search().byUrl(BASE_URL + OBSERVATION_SEARCH_URL)
				.returnBundle(Bundle.class)
				.execute();
		
		// since bundle is sorted in descending order based on date,
		// first item in entry has to be latest observation
		Observation latestObservation = (Observation) allObservations.getEntry().get(0).getResource();
		return latestObservation;
	}
	
}