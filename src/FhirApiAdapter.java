import java.util.ArrayList;
import java.util.HashSet;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.*;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;

public class FhirApiAdapter extends FhirServer {
	private final String BASE_URL = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/";
	private FhirContext ctx = FhirContext.forR4();
	private IParser parser = ctx.newJsonParser().setPrettyPrint(true);
	private IGenericClient client;
	
	public FhirApiAdapter() {
		// change socket timeout values
		final int MAX_SOCKET_TIMEOUT = 60;
		final int S_TO_MS = 1000;
		
		ctx.getRestfulClientFactory().setSocketTimeout(MAX_SOCKET_TIMEOUT * S_TO_MS);
		client = ctx.newRestfulGenericClient(BASE_URL);
	}
	
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
		
		Boolean hasNextPage = true;
		String nextURL = BASE_URL + ENCOUNTER_SEARCH_URL;
		
		while(hasNextPage) {
			Bundle allEncounters;
			try {
				allEncounters = client.search().byUrl(nextURL).returnBundle(Bundle.class).execute();
			}
			catch(FhirClientConnectionException fe) {
				System.out.println(fe);
				System.out.println("Connection failed. Please restart");
				break;
			}
			
			for(BundleEntryComponent entry: allEncounters.getEntry()) {
				Encounter encounter = (Encounter) entry.getResource();
				String patientURL = encounter.getSubject().getReference();
				
				Patient patient = client.read().resource(Patient.class).withUrl(BASE_URL + patientURL).execute();
				patientList.add(patient);
			}
			
			hasNextPage = this.hasNextLink(allEncounters);
			if(hasNextPage) {
				nextURL = allEncounters.getLink(Bundle.LINK_NEXT).getUrl();
			}
		}
	
		return removeDuplicates(patientList); //contains duplicates 
	}
	
	private ArrayList<Patient> removeDuplicates(ArrayList<Patient> patientList) {
		ArrayList<Patient> noDuplicatesList = new ArrayList<Patient>();
		HashSet<String> availableIdentifiers = new HashSet<String>();
		
		for(int i = 0; i < patientList.size(); i++) {
			Patient patient = patientList.get(i);
			String patientIdentifier = patient.getIdentifier().get(0).getValue();
			
			if(!availableIdentifiers.contains(patientIdentifier)) {
				availableIdentifiers.add(patientIdentifier);
				noDuplicatesList.add(patient);
			}
		}
		return noDuplicatesList;
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
	
	private Boolean hasNextLink(Bundle bundle) {
		if(bundle.getLink(Bundle.LINK_NEXT) == null) {
			return false;
		}
		return true;
	}
	
}