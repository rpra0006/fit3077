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
		return null;
	}

	@Override
	public LatestMonitorView createLatestView() {
		// TODO Auto-generated method stub
		return null;
	}

}
