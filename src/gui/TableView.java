package gui;

import javax.swing.table.DefaultTableCellRenderer;
import model.PatientMonitor;

public abstract class TableView extends MonitorView {
	private DefaultTableCellRenderer cellRenderer;

	public TableView(PatientMonitor monitor) {
		super(monitor);
	}
	
	public DefaultTableCellRenderer getRenderer() {
		return this.cellRenderer;
	};
	
	public void setRenderer(DefaultTableCellRenderer renderer) {
		this.cellRenderer = renderer;
	}

}
