package gui;
public interface SingletonMonitorViewFactory {
	public HistoryMonitorView createHistoryView();
	public LatestMonitorView createLatestView();
}
