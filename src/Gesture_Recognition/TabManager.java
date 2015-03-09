package Gesture_Recognition;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.impinj.octanesdk.ImpinjReader;
import com.impinj.octanesdk.Tag;
import com.impinj.octanesdk.TagReport;
import com.impinj.octanesdk.TagReportListener;

public class TabManager extends JTabbedPane implements TagReportListener {
	TagListPanel tagListPanel;

	TabManager() {
		super();
		tagListPanel = TagListPanel.getInstance();
	}

	@Override
	public void onTagReported(ImpinjReader reader, TagReport report) {
		// TODO Auto-generated method stub
		List<Tag> tags = report.getTags();

		for (Tag t : tags) {
			tagListPanel.addTag(t);
			getSelectedTab().onTagReported(t);

		}

	}

	Tab getSelectedTab() {
		return (Tab) getSelectedComponent();
	}
	
	void stop(){
		for(int i=0; i<getComponentCount(); i++)
			((Tab) getComponentAt(i)).stop();
	}
}

abstract class Tab extends JPanel {
	abstract void onTagReported(Tag t);
	abstract void stop();
}