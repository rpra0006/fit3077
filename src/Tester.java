
import java.util.List;

import org.hl7.fhir.r4.model.Observation;

import model.FhirApiAdapter;
import model.FhirServer;

// TEMP CLASS DELETE WHEN SUBMIT
public class Tester {
	
	public static void main(String[] args) {
		FhirServer server = new FhirApiAdapter();
		List<Observation> observation = server.getPatientLatestObservations("f8ec94e5-1eea-439d-8aac-f67593b6e9a9", "55284-4", 1);
		System.out.println(observation.get(0).getComponent().get(0).getValueQuantity().getValue()); // diastolic blood pressure
		System.out.println(observation.get(0).getComponent().get(1).getValueQuantity().getValue()); // systolic blood pressure
	}
	
}
