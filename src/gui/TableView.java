package gui;

import javax.swing.table.DefaultTableCellRenderer;
import model.PatientMonitor;

/*
 * Abstract class of table view objects
 */
public abstract class TableView extends MonitorView {
	private DefaultTableCellRenderer cellRenderer;

	public TableView(PatientMonitor monitor) {
		super(monitor);
	}
	
	/**
	 * Return type of table cell renderer
	 * @return cellRenderer
	 */
	public DefaultTableCellRenderer getRenderer() {
		return this.cellRenderer;
	};
	
	/**
	 * Set renderer as a part of table view
	 * @param renderer
	 */
	public void setRenderer(DefaultTableCellRenderer renderer) {
		this.cellRenderer = renderer;
	}

}
