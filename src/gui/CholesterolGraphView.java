package gui;
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
import java.awt.event.ActionEvent;

import org.hl7.fhir.r4.model.Patient;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;

import model.PatientMonitor;

import javax.swing.BoxLayout;

public class CholesterolGraphView extends GraphView {
	
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	@Override
	public void launchScreen() {
		// TODO Auto-generated method stub
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the application.
	 */
	public CholesterolGraphView(PatientMonitor monitor) {
		super(monitor);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1054, 579);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel graphPanel = new JPanel();
		graphPanel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Cholestrol Level", TitledBorder.CENTER, TitledBorder.TOP, null, Color.BLACK));
		graphPanel.setBounds(27, 11, 991, 481);
		frame.getContentPane().add(graphPanel);
		graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.X_AXIS));
		
		JButton btnShowBar = new JButton("Show Bar Graph");
		btnShowBar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultCategoryDataset choldata = new DefaultCategoryDataset();
				choldata.setValue(101.1, "Cholestrol Level", "patient1");
				choldata.setValue(75, "Cholestrol Level", "patient2");
				choldata.setValue(150.23, "Cholestrol Level", "patient3");
				
				//JFreeChart jchart = ChartFactory.createBarChart("Patient Cholestrol Level", "Patient Name", "Cholestrol Value", choldata);
				JFreeChart jchart = ChartFactory.createLineChart("Patient Cholestrol Level", "Patient Name", "Cholestrol Value", choldata);
				
				CategoryPlot plot = jchart.getCategoryPlot();
				plot.setRangeGridlinePaint(Color.black);
				
				ChartFrame chartfrm = new ChartFrame("Patient Cholestrol Level",jchart,true);
				//chartfrm.setVisible(true);
				//chartfrm.setSize(500,400);
				
				ChartPanel chartPanel = new ChartPanel(jchart);
				
				graphPanel.removeAll();
				graphPanel.add(chartPanel);
				graphPanel.updateUI();
				
			}
		});
		btnShowBar.setBounds(439, 503, 157, 23);
		frame.getContentPane().add(btnShowBar);
	}
	
	public void update() {
		return;
	}

	@Override
	public void addPatientToMonitor(Patient patientData) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Boolean isRunning() {
		// TODO Auto-generated method stub
		return null;
	}
}
