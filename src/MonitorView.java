import org.hl7.fhir.r4.model.Patient;

public abstract class MonitorView implements Observer {
	
	public abstract void addPatientToMonitor(Patient patientData);
	public abstract void launchScreen();
	public abstract Boolean isRunning();
	public abstract void update();

}
