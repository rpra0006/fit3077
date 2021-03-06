package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import javax.swing.border.EtchedBorder;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import model.PatientMonitor;

import javax.swing.BoxLayout;

public class CholesterolGraphView extends GraphView {
	/*
	 * Display cholestrol data from monitor in graphical view
	 */
	
	private JFrame frame;
	private DefaultCategoryDataset choldata;
	private JPanel graphPanel = new JPanel();


	/**
	 * Create the application.
	 */
	public CholesterolGraphView(PatientMonitor monitor) {
		super(monitor);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1054, 579);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		graphPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Cholestrol Level", TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLACK));
		graphPanel.setBounds(27, 11, 991, 481);
		frame.getContentPane().add(graphPanel);
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.X_AXIS));	// add graph on x axis
		
		frame.setVisible(true);
	}
	
	/**
	 * Update contents of the frame to display monitor
	 */
	public void update() {
		graphPanel.removeAll(); // clear graph panel
		
		choldata = new DefaultCategoryDataset();
		for (Map.Entry<Patient, List<Observation>> patientObservation : monitor.getAllPatientObservations().entrySet()){
			Patient patient = patientObservation.getKey();
			Observation observation = patientObservation.getValue().get(0);
			
			BigDecimal cholestrolLevel;
				
			cholestrolLevel = observation.getValueQuantity().getValue();
			choldata.setValue(cholestrolLevel, "Cholestrol Level", patient.getName().get(0).getNameAsSingleString());
		}
		
		JFreeChart jchart = ChartFactory.createBarChart("Patient Cholestrol Level", "Patient Name", "Cholestrol Value", choldata);
		//JFreeChart jchart = ChartFactory.createLineChart("Patient Cholestrol Level", "Patient Name", "Cholestrol Value", choldata);
		CategoryPlot plot = jchart.getCategoryPlot();
		plot.setRangeGridlinePaint(Color.black);
		
		ChartPanel chartPanel = new ChartPanel(jchart);
		
		graphPanel.add(chartPanel);	// add chart to graph panel
		graphPanel.updateUI(); // refresh graph panel
	}

	@Override
	public Boolean isRunning() {
		// TODO Auto-generated method stub
		return null;
	}
}
