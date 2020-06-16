import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class HistoryGraphView {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HistoryGraphView window = new HistoryGraphView();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public HistoryGraphView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 804, 468);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel historyGraphPanel = new JPanel();
		historyGraphPanel.setBounds(10, 11, 770, 409);
		frame.getContentPane().add(historyGraphPanel);
	}
}
