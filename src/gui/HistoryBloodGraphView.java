package gui;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import model.PatientMonitor;
import javax.swing.JLabel;

public class HistoryBloodGraphView extends GraphView {
	/*
	 * Display patient systolic history in graph view
	 */
	
	private JFrame frame;
	private JPanel graphPanel;
	
	
	/**
	 * Create the application.
	 */
	public HistoryBloodGraphView(PatientMonitor monitor) {
		super(monitor);
	}

	/**
	 * Initialize contents of frame
	 */
	@Override
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1054, 579);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		graphPanel = new JPanel();
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));
		
		JScrollPane scrollablePane = new JScrollPane(graphPanel);
		scrollablePane.setBounds(25, 32, 987, 447);
		
		frame.getContentPane().add(scrollablePane);
		
		JLabel lblHistoricalGraph = new JLabel("Historical Graph");
		lblHistoricalGraph.setBounds(470, 11, 123, 14);
		frame.getContentPane().add(lblHistoricalGraph);
		
		frame.setVisible(true);
	}
	
	/**
	 * Update contents of panel to display historical data in graph
	 */
	@Override
	public void update() {
		// TODO Auto-generated method stub
		graphPanel.removeAll();
		
		// Get all observations in monitor
		for (Map.Entry<Patient, List<Observation>> patientObservation : monitor.getAllPatientObservations().entrySet()){
			DefaultCategoryDataset bloodHistoryData = new DefaultCategoryDataset();
			Patient patient = patientObservation.getKey();
			int observationCount = 1;
			
			// Get all observation from observation list for plotting
			for (Observation systolicObservation : patientObservation.getValue()) {
				BigDecimal systolicValues = systolicObservation.getComponent().get(1).getValueQuantity().getValue();
				bloodHistoryData.setValue(systolicValues, "Systolic Blood Levels", observationCount);
				observationCount++;
			}
			
			JFreeChart jchart = ChartFactory.createLineChart("Patient Systolic Blood Pressure", "Patient Name", "Systolic Blood Pressure Value", bloodHistoryData);
			CategoryPlot plot = jchart.getCategoryPlot();
			plot.setRangeGridlinePaint(Color.black);
			jchart.addSubtitle(new TextTitle(patient.getName().get(0).getNameAsSingleString())); // add patient name as table title
			
			ChartPanel chartPanel = new ChartPanel(jchart);
			
			graphPanel.add(chartPanel); // add graph to panel
		}
		
		graphPanel.updateUI(); // update panel page
	}
}
