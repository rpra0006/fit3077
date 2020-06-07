package gui;

public class GraphViewFactory implements SingletonMonitorViewFactory {
	private HistoryMonitorView historyGraphView;
	private LatestMonitorView latestGraphView;

	@Override
	public HistoryMonitorView createHistoryView() {
		// TODO Auto-generated method stub
		if(historyGraphView == null) {
			historyGraphView = new HistoryGraphView();
		}
		return historyGraphView;
	}

	@Override
	public LatestMonitorView createLatestView() {
		// TODO Auto-generated method stub
		if(latestGraphView == null) {
			latestGraphView = new LatestGraphView();
		}
		return latestGraphView;
	}

}
