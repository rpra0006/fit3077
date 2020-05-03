//this is possibly the GUI part

public class View implements Observer {
	private String strToPrint = "No updates yet";
	private PatientMonitor monitor;
	
	public View(PatientMonitor monitor) {
		this.monitor = monitor;
		monitor.attach(this);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		System.out.println(strToPrint);
	}

}
