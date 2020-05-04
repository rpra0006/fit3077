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
	public Patient getPatient(String patientID) {
		// TODO Auto-generated method stub
		Patient patient = client.read().resource(Patient.class).withId(patientID).execute();
		System.out.println(parser.encodeResourceToString(patient));
		return null;
	}

	@Override
	public ArrayList<Patient> getAllPractitionerPatients(String practitionerID) {
		// TODO Auto-generated method stub
		ArrayList<Patient> patientList = new ArrayList<Patient>();
		
		
		Bundle encounters = client.search()
				.forResource(Encounter.class)
				.where(Encounter.PRACTITIONER.hasId(practitionerID))
				.returnBundle(Bundle.class).execute();
		
		System.out.println(parser.encodeResourceToString(encounters));
		
		for(BundleEntryComponent entry: encounters.getEntry()) {
			Encounter encounter = (Encounter) entry.getResource();
			String patientURL = encounter.getSubject().getReference();
			System.out.println(patientURL);
			
			Patient patient = client.read().resource(Patient.class).withUrl(BASE_URL + patientURL).execute();
			patientList.add(patient);
		}
		
		//REMOVE DUPLICATES
		return patientList;
	}

	@Override
	public Observation getPatientLatestObservation(String patientID, String observationCode) {
		// TODO Auto-generated method stub
		return null;
	}

}