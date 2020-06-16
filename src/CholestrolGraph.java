import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Map;
import java.awt.event.ActionEvent;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.BoxLayout;

public class CholestrolGraph extends MonitorView{
	
	private JFrame frame;
	private DefaultCategoryDataset choldata;
	private PatientMonitor patientMonitor = new CholestrolMonitor();
	private JPanel graphPanel;
	
	/**
	 * Launch the application.
	 */
	@Override
	public void launchScreen() {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the application.
	 */
	public CholestrolGraph() {
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1054, 579);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel graphPanel = new JPanel();
		graphPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Cholestrol Level", TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLACK));
		graphPanel.setBounds(27, 11, 991, 481);
		frame.getContentPane().add(graphPanel);
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.X_AXIS));
		
		patientMonitor.attach(this);
		patientMonitor.startMonitor();
		frame.setVisible(true);
	}
	
	public void update() {
		graphPanel.removeAll();
		
		DefaultCategoryDataset choldata = new DefaultCategoryDataset();
		
		for (Map.Entry<Patient, Observation> patientObservation : patientMonitor.getAllObservation().entrySet()){
			String[] row = new String[3];
			
			Patient patient = patientObservation.getKey();
			Observation observation = patientObservation.getValue();
			
			BigDecimal cholestrolLevel;
				
			cholestrolLevel = observation.getValueQuantity().getValue();
			
			choldata.setValue(cholestrolLevel, "Cholestrol Level", patient.getName().get(0).getNameAsSingleString());
		}
		
		JFreeChart jchart = ChartFactory.createBarChart("Patient Cholestrol Level", "Patient Name", "Cholestrol Value", choldata);
		CategoryPlot plot = jchart.getCategoryPlot();
		plot.setRangeGridlinePaint(Color.black);
		
		ChartFrame chartfrm = new ChartFrame("Patient Cholestrol Level",jchart,true);
		
		ChartPanel chartPanel = new ChartPanel(jchart);
		
		graphPanel.add(chartPanel);
		graphPanel.updateUI();
	}

	@Override
	public void addPatientToMonitor(Patient patientData) {
		// TODO Auto-generated method stub
		patientMonitor.addPatient(patientData); //Add to patientMonitor list
		update();
		
	}


	@Override
	public Boolean isRunning() {
		// TODO Auto-generated method stub
		return null;
	}
}
