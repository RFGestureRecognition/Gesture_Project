package Gesture_Recognition;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;

import com.impinj.octanesdk.ImpinjReader;
import com.impinj.octanesdk.Tag;
import com.impinj.octanesdk.TagReport;
import com.impinj.octanesdk.TagReportListener;

public class TagListener implements TagReportListener {
	JSpinner spinner;
	TagListPanel tagListPanel;
	Parameters par;
	JTabbedPane tab;
	ArrayList<JLabel> monitorList;
	int antennaNum;
	int paraNum;

	TagListener(JTabbedPane tab, TagListPanel tagListPanel) {
		this.tagListPanel = tagListPanel;
		par = Parameters.getInstance();
		this.tab = tab;
		spinner = ((ZoomTab) tab.getComponentAt(0)).spinner;
		monitorList = ((MonitorTab) tab.getComponentAt(2)).jLabelList;
		antennaNum = ((MonitorTab) tab.getComponentAt(2)).antennaNum;
		paraNum = ((MonitorTab) tab.getComponentAt(2)).paraNum;
	}

	@Override
	public void onTagReported(ImpinjReader reader, TagReport report) {
		List<Tag> tags = report.getTags();

		for (Tag t : tags) {
			tagListPanel.addTag(t);
			switch (tab.getSelectedIndex()) {
			case 0:
				zoomTabListener(t);
				break;
			case 1:
				moveTablListener(t);
				break;
			case 2:
				monitorTabListener(t);
				break;

			}

		}

	}

	private void moveTablListener(Tag t) {

	}

	private void monitorTabListener(Tag t) {
		if (tagListPanel.getSelectedTag().equals(t.getEpc().toString())) {
			for (int j = 0; j < antennaNum; j++) {
				if ((t.getAntennaPortNumber() - 1) == j)
					monitorList.get(j).setText(t.getPhaseAngleInRadians() + "");
				monitorList.get(2).setText(Double.parseDouble(monitorList.get(0).getText())-Double.parseDouble(monitorList.get(1).getText())+"" );
			}
			
			for (int j = 0; j < antennaNum; j++) {
				if ((t.getAntennaPortNumber() - 1) == j)
					monitorList.get(j + 2 * antennaNum).setText(
							t.getPeakRssiInDbm() + "");
			}
			for (int j = 0; j < antennaNum; j++) {
				if ((t.getAntennaPortNumber() - 1) == j)
					monitorList.get(j + 3 * antennaNum).setText(
							t.getRfDopplerFrequency()+"");
			}
		}
	}

	private void zoomTabListener(Tag t) {

		if (tagListPanel.getSelectedTag().equals(t.getEpc().toString())) {

			if (t.isPeakRssiInDbmPresent()) {
				double curScale = RSSItoScale(t.getPeakRssiInDbm());
				if (Double.compare(curScale, par.getTempScale()) != 0) {
					System.out.println(curScale + " : " + par.getTempScale()
							+ " : " + par.getCounter());
					par.setTempScale(curScale);

				} else {
					par.addCounter(1);
					if (par.getCounter() > 3) {
						spinner.setValue(par.getTempScale());
						par.setScale(curScale);
						par.setCounterZero();
					}
				}

			}
		}
	}

	private void specReport(Tag t, ImpinjReader reader) {
		System.out.print("EPC: " + t.getEpc().toString());

		if (reader.getName() != null) {
			System.out.print(" Reader_name: " + reader.getName());
		} else {
			System.out.print(" Reader_ip: " + reader.getAddress());
		}

		if (t.isAntennaPortNumberPresent()) {
			System.out.print(" antenna: " + t.getAntennaPortNumber());
		}

		if (t.isFirstSeenTimePresent()) {
			System.out.print(" first: " + t.getFirstSeenTime().ToString());
		}

		if (t.isLastSeenTimePresent()) {
			System.out.print(" last: " + t.getLastSeenTime().ToString());
		}

		if (t.isSeenCountPresent()) {
			System.out.print(" count: " + t.getTagSeenCount());
		}

		if (t.isRfDopplerFrequencyPresent()) {
			System.out.print(" doppler: " + t.getRfDopplerFrequency());
		}

		if (t.isPeakRssiInDbmPresent()) {
			System.out.print(" peak_rssi: " + t.getPeakRssiInDbm());
		}

		if (t.isRfPhaseAnglePresent()) {
			System.out.print(" phase: " + t.getPhaseAngleInRadians());
		}

		if (t.isChannelInMhzPresent()) {
			System.out.print(" chan_MHz: " + t.getChannelInMhz());
		}

		if (t.isFastIdPresent()) {
			System.out.print("\n     fast_id: " + t.getTid().toHexString());

			System.out.print(" model: " + t.getModelDetails().getModelName());

			System.out.print(" epcsize: "
					+ t.getModelDetails().getEpcSizeBits());

			System.out.print(" usermemsize: "
					+ t.getModelDetails().getUserMemorySizeBits());
		}

		System.out.println("");
	}

	private double RSSItoScale(double RSSI) {
		double scale = Math.sqrt(RSSI * 0.0315 + 1.585);
		double temp = Math.round(scale * 100d) / 100d;
		if (temp < 0.05)
			temp = 0.05;
		return Math.round(scale * 100d) / 100d;
	}
}
