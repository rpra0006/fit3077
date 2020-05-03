import java.util.ArrayList;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.*;
import ca.uhn.fhir.rest.client.api.IGenericClient;

public class FhirApiAdapter extends FhirServer {
	private final String BASE_URL = "https://fhir.monash.edu/hapi-fhir-jpaserver/fhir";
	private IParser parser = FhirContext.forR4().newJsonParser().setPrettyPrint(true);
	private IGenericClient client = FhirContext.forR4().newRestfulGenericClient(BASE_URL);
	
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
		return null;
	}

	@Override
	public Observation getPatientLatestObservation(String patientID, String observationCode) {
		// TODO Auto-generated method stub
		return null;
	}

}