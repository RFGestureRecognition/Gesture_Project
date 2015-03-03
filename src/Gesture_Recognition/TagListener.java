package Gesture_Recognition;

import java.util.List;

import javax.swing.JSpinner;

import com.impinj.octanesdk.ImpinjReader;
import com.impinj.octanesdk.Tag;
import com.impinj.octanesdk.TagReport;
import com.impinj.octanesdk.TagReportListener;

public class TagListener implements TagReportListener{
	JSpinner spinner;
	TagListPanel tagListPanel;
	Parameters par;
	
	
	TagListener(JSpinner spinner, TagListPanel tagListPanel){
		this.spinner = spinner;
		this.tagListPanel = tagListPanel;
		par = Parameters.getInstance();
	}
	
	@Override
	public void onTagReported(ImpinjReader reader, TagReport report) {
		List<Tag> tags = report.getTags();
		System.out.println("reported");
		for (Tag t : tags) {
			tagListPanel.addTag(t);

			if (tagListPanel.getSelectedTag().equals(t.getEpc().toString())) {
				// phaseZoom(t);
				RSSIZoom(t);
				// specReport(t);
			}
		}

	}

	private void phaseZoom(Tag t) {

	}

	private void RSSIZoom(Tag t) {
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
