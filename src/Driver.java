import java.util.ArrayList;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;

public class Driver {

	public static void main(String[] args) {
		FhirServer server = new FhirApiAdapter();
		IParser parser = FhirContext.forR4().newJsonParser().setPrettyPrint(true);
		
		
		ArrayList<Patient> patients = server.getAllPractitionerPatients("500");
		for(Patient p: patients) {
			System.out.println(p.getName().get(0).getNameAsSingleString());
		}
		/*
		PatientMonitor monitor = new PatientMonitor();
		monitor.setUpdateTime(3);
		View view = new View(monitor);
		*/
		
		//Observation o = server.getPatientLatestObservation("68b568d4-eed6-4610-acfb-4e05c7399429", "2093-3");
		//System.out.println(parser.encodeResourceToString(o));
	}
	
}
