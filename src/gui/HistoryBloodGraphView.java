package gui;

import java.awt.Color;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import model.PatientMonitor;
import javax.swing.JButton;
import javax.swing.JLabel;

public class HistoryBloodGraphView extends GraphView {
	
	private JFrame frame;
	private DefaultCategoryDataset bloodHistoryData;
	private JScrollPane graphPanel;
	
	public HistoryBloodGraphView(PatientMonitor monitor) {
		super(monitor);
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		frame = new JFrame();
		frame.setBounds(100, 100, 1054, 579);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		
		graphPanel = new JScrollPane();
		graphPanel.setBounds(25, 32, 987, 447);
		frame.getContentPane().add(graphPanel);
		
		JButton btnPreviousPatient = new JButton("Previous Patient");
		btnPreviousPatient.setBounds(267, 490, 131, 23);
		frame.getContentPane().add(btnPreviousPatient);
		
		JButton btnNextPatient = new JButton("Next Patient");
		btnNextPatient.setBounds(643, 490, 131, 23);
		frame.getContentPane().add(btnNextPatient);
		
		JLabel lblHistoricalGraph = new JLabel("Historical Graph");
		lblHistoricalGraph.setBounds(470, 11, 123, 14);
		frame.getContentPane().add(lblHistoricalGraph);
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.X_AXIS));
		
		frame.setVisible(true);

	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		graphPanel.removeAll();
		
		bloodHistoryData = new DefaultCategoryDataset();
		for (Map.Entry<Patient, List<Observation>> patientObservation : monitor.getAllPatientObservations().entrySet()){
			String[] row = new String[3];
			
			Patient patient = patientObservation.getKey();
			int numOfObservation = 0;
			
			for (Observation systolicObservation : patientObservation.getValue()) {
				BigDecimal systolicValues = systolicObservation.getComponent().get(1).getValueQuantity().getValue();
				bloodHistoryData.setValue(systolicValues, "Systolic Blood Levels", numOfObservation);
				numOfObservation++;
			}
			
			JFreeChart jchart = ChartFactory.createLineChart("Patient Systolic Blood Pressure", "Patient Name", "Systolic Blood Pressure Value", bloodHistoryData);
			CategoryPlot plot = jchart.getCategoryPlot();
			plot.setRangeGridlinePaint(Color.black);
			jchart.addSubtitle(new TextTitle(patient.getName().get(0).getNameAsSingleString())); // add patient name as table title
			
			ChartFrame chartfrm = new ChartFrame("Patient Systolic Blood Pressure History",jchart,true);
			
			ChartPanel chartPanel = new ChartPanel(jchart);
			
			graphPanel.add(chartPanel);
		}
		
		graphPanel.updateUI();
	}
}
