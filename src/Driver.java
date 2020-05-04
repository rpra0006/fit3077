
public class Driver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FhirServer server = new FhirApiAdapter();
		server.getPatient("1");
		
	}

}
