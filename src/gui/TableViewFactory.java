package gui;

public class TableViewFactory implements SingletonMonitorViewFactory {
	private HistoryMonitorView historyView;
	private LatestMonitorView latestView;
	
	public HistoryMonitorView createHistoryView() {
		// TODO Auto-generated method stub
		if(historyView == null) {
			historyView = new HistoryTableView();
		}
		return historyView;
	}

	@Override
	public LatestMonitorView createLatestView() {
		// TODO Auto-generated method stub
		if(latestView == null) {
			latestView = new LatestTableView();
		}
		return latestView;
	}

}
