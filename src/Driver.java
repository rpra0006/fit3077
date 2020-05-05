import java.util.ArrayList;

import org.hl7.fhir.r4.model.Patient;

public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//FhirServer server = new FhirApiAdapter();
		//ArrayList<Patient> patients = server.getAllPractitionerPatients("500");
		//for(Patient p: patients) {
		//	System.out.println(p.getName().get(0).getNameAsSingleString());
		//}
		PatientMonitor monitor = new PatientMonitor();
		monitor.setUpdateTime(3);
		View view = new View(monitor);
	}
	
}
