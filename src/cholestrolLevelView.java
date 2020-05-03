import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class cholestrolLevelView {

	private JFrame frame;
	private JTable table;
	private JTextField patientNameField;
	
	/**
	 * Launch the application.
	 */
	public static void cholestrolScreen() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					cholestrolLevelView window = new cholestrolLevelView();
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
	public cholestrolLevelView() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblCholestrolLevels = new JLabel("Cholestrol Levels");
		lblCholestrolLevels.setBounds(180, 11, 135, 14);
		frame.getContentPane().add(lblCholestrolLevels);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 40, 242, 196);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Name", "Cholestrol Level", "Date Issued"
			}
		));
		
		Object[] columns = {"Name", "Cholestrol Level", "Date Issued"};
		DefaultTableModel model = new DefaultTableModel();
		model.setColumnIdentifiers(columns);
		table.setModel(model);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int i = table.getSelectedRow();
				patientNameField.setText(model.getValueAt(i, 0).toString());
			}
		});
		
		table.getColumnModel().getColumn(1).setPreferredWidth(93);
		scrollPane.setViewportView(table);
		
		
		patientNameField = new JTextField();
		patientNameField.setBounds(287, 61, 124, 23);
		frame.getContentPane().add(patientNameField);
		patientNameField.setColumns(10);
		
		JButton btnAddPatient = new JButton("Add Patient");
		btnAddPatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] row = new String[3];
				row[0] = patientNameField.getText();
				model.addRow(row);
			}
		});
		btnAddPatient.setBounds(287, 92, 124, 23);
		frame.getContentPane().add(btnAddPatient);
		
		JButton btnRemovePatient = new JButton("Remove Patient");
		btnRemovePatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int i = table.getSelectedRow();
				if (i >= 0) {
					model.removeRow(i);
				}
				else {
					System.out.println("Delete Error");
				}
			}
		});
		btnRemovePatient.setBounds(287, 119, 124, 23);
		frame.getContentPane().add(btnRemovePatient);
	}
}
