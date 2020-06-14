package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.exceptions.FhirClientConnectionException;

public class FhirApiAdapter extends FhirServer {
	private final String BASE_URL = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir/";
	private FhirContext ctx = FhirContext.forR4();
	private IGenericClient client;
	
	public FhirApiAdapter() {
		// change socket timeout values
		final int MAX_SOCKET_TIMEOUT = 60;
		final int S_TO_MS = 1000;
		
		ctx.getRestfulClientFactory().setSocketTimeout(MAX_SOCKET_TIMEOUT * S_TO_MS);
		client = ctx.newRestfulGenericClient(BASE_URL);
	}
	
	/**
	 * Gets a single Patient instance, given a patient identifier
	 */
	@Override
	public Patient getPatient(String patientIdentifier) {
		final String PATIENT_SEARCH_URL = "Patient?identifier=https://github.com/synthetichealth/synthea|" + patientIdentifier;
		Bundle patientBundle = client.search().byUrl(BASE_URL + PATIENT_SEARCH_URL).returnBundle(Bundle.class).execute();
		Patient patient = (Patient) patientBundle.getEntry().get(0).getResource();
		return patient;
	}
	
	/**
	 * Gets a list of patients that a given practitioner has treated
	 */
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
			
			hasNextPage = hasNextLink(allEncounters);
			if(hasNextPage) {
				nextURL = allEncounters.getLink(Bundle.LINK_NEXT).getUrl();
			}
		}
	
		return removeDuplicates(patientList); 
	}
	
	
	/**
	 * Remove duplicate patient entries
	 */
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
	
	
	/**
	 * Gets a list of observations (according to observationCode), given a patient's identifier
	 * If the actual number of observations returned from server is less than the parameter amountToReturn,
	 * returns the actual number of observations, that is returned from server
	 */
	@Override
	public List<Observation> getPatientLatestObservations(String patientIdentifier, String observationCode, int amountToReturn) {
		final String OBSERVATION_SEARCH_URL = "Observation?patient.identifier=https://github.com/synthetichealth/synthea|" + patientIdentifier + 
				"&code=" + observationCode + "&_sort=-date"; // sort in descending order based on date
		
		List<Observation> observationList = new ArrayList<Observation>();
		Bundle allObservations = client.search().byUrl(BASE_URL + OBSERVATION_SEARCH_URL)
				.returnBundle(Bundle.class)
				.execute();
		int amountReturned = allObservations.getEntry().size();
		
		if(amountReturned == 0) {
			return null;
		}
		else if(amountReturned < amountToReturn) {
			amountToReturn = amountReturned;
		}
		
		// since bundle is sorted in descending order based on date,
		// first item in entry has to be latest observation
		for(int i = 0; i < amountToReturn; i++) {
			Observation observation = (Observation) allObservations.getEntry().get(i).getResource();
			observationList.add(observation);
		}
		return observationList;
	}
	
	private Boolean hasNextLink(Bundle bundle) {
		return bundle.getLink(Bundle.LINK_NEXT) != null;
	}
	
}