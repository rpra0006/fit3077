
// TEMP CLASS DELETE WHEN SUBMIT
public class Tester {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PatientMonitor firstMonitor = new CholestrolMonitor();
		PatientMonitor secondMonitor = new CholestrolMonitor();
		PatientMonitor thirdMonitor = new CholestrolMonitor();
		
		firstMonitor.startTimer();
		secondMonitor.startTimer();
		thirdMonitor.startTimer();
		
		System.out.println("First monitor time: " + firstMonitor.getTime());
		System.out.println("Second monitor time: " + secondMonitor.getTime());
		
		firstMonitor.setUpdateTime(5);
		
		System.out.println("Updated first monitor time: " + firstMonitor.getTime());
		System.out.println("Updated second monitor time: " + secondMonitor.getTime());
		System.out.println("Third monitor time: " + thirdMonitor.getTime());
	}

}
