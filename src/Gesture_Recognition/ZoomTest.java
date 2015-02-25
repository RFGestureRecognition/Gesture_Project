package Gesture_Recognition;

import java.util.List;
import java.util.Scanner;

import com.impinj.octanesdk.AntennaConfigGroup;
import com.impinj.octanesdk.ImpinjReader;
import com.impinj.octanesdk.OctaneSdkException;
import com.impinj.octanesdk.ReaderMode;
import com.impinj.octanesdk.ReportConfig;
import com.impinj.octanesdk.ReportMode;
import com.impinj.octanesdk.Settings;
import com.impinj.octanesdk.Tag;
import com.impinj.octanesdk.TagReport;
import com.impinj.octanesdk.TagReportListener;

public class ZoomTest implements TagReportListener {
	ZoomTestFrame zFrame;
	ImpinjReader reader;
	AntennaConfigGroup antennas;
	double scale;
	ZoomTest() {
		zFrame = new ZoomTestFrame();
		zFrame.setVisible(true);
		scale=zFrame.defaultScale;
	}

	public void setReader() {

		try {
			reader = new ImpinjReader();
			String hostname = "169.254.1.1";
			System.out.println("Connecting");
			reader.connect(hostname);

			Settings settings = reader.queryDefaultSettings();

			ReportConfig report = settings.getReport();
			report.setIncludeAntennaPortNumber(true);
			report.setMode(ReportMode.Individual);
			report.setIncludePhaseAngle(true);
			report.setIncludePeakRssi(true);

			settings.setReaderMode(ReaderMode.AutoSetDenseReader);

			antennas = settings.getAntennas();
			antennas.disableAll();

			// set some special settings for antenna 1
			antennas.enableById(new short[] { 1 });
			antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
			antennas.getAntenna((short) 1).setIsMaxTxPower(false);
			antennas.getAntenna((short) 1).setTxPowerinDbm(20.0);
			antennas.getAntenna((short) 1).setRxSensitivityinDbm(-70);

			// set some special settings for antenna 2
			antennas.enableById(new short[] { 2 });
			antennas.getAntenna((short) 2).setIsMaxRxSensitivity(false);
			antennas.getAntenna((short) 2).setIsMaxTxPower(false);
			antennas.getAntenna((short) 2).setTxPowerinDbm(20.0);
			antennas.getAntenna((short) 2).setRxSensitivityinDbm(-70);

			reader.setTagReportListener(this);

			System.out.println("Applying Settings");
			reader.applySettings(settings);

			System.out.println("Starting");
			reader.start();

			System.out.println("Press Enter to exit.");
			Scanner s = new Scanner(System.in);
			s.nextLine();

			reader.stop();
			reader.disconnect();

		} catch (OctaneSdkException ex) {
			System.out.println(ex.getMessage());
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			ex.printStackTrace(System.out);
		}

	}

	@Override
	public void onTagReported(ImpinjReader reader, TagReport report) {
		List<Tag> tags = report.getTags();

		for (Tag t : tags) {
			if (t.isPeakRssiInDbmPresent()) {
				double temp = RSSItoScale(t.getPeakRssiInDbm());
				
				if (temp != scale ){
					zFrame.zoom.imagePanel.setScale(temp);
					scale = temp;
				}
				
			}
			/*
			 * System.out.print("EPC: " + t.getEpc().toString());
			 * 
			 * if (reader.getName() != null) { System.out.print(" Reader_name: "
			 * + reader.getName()); } else { System.out.print(" Reader_ip: " +
			 * reader.getAddress()); }
			 * 
			 * if (t.isAntennaPortNumberPresent()) {
			 * System.out.print(" antenna: " + t.getAntennaPortNumber()); }
			 * 
			 * if (t.isFirstSeenTimePresent()) { System.out.print(" first: " +
			 * t.getFirstSeenTime().ToString()); }
			 * 
			 * if (t.isLastSeenTimePresent()) { System.out.print(" last: " +
			 * t.getLastSeenTime().ToString()); }
			 * 
			 * if (t.isSeenCountPresent()) { System.out.print(" count: " +
			 * t.getTagSeenCount()); }
			 * 
			 * if (t.isRfDopplerFrequencyPresent()) {
			 * System.out.print(" doppler: " + t.getRfDopplerFrequency()); }
			 * 
			 * if (t.isPeakRssiInDbmPresent()) { System.out.print(" peak_rssi: "
			 * + t.getPeakRssiInDbm()); }
			 * 
			 * if (t.isRfPhaseAnglePresent()) { System.out.print(" phase: " +
			 * t.getPhaseAngleInRadians()); }
			 * 
			 * if (t.isChannelInMhzPresent()) { System.out.print(" chan_MHz: " +
			 * t.getChannelInMhz()); }
			 * 
			 * if (t.isFastIdPresent()) { System.out.print("\n     fast_id: " +
			 * t.getTid().toHexString());
			 * 
			 * System.out.print(" model: " +
			 * t.getModelDetails().getModelName());
			 * 
			 * System.out.print(" epcsize: " +
			 * t.getModelDetails().getEpcSizeBits());
			 * 
			 * System.out.print(" usermemsize: " +
			 * t.getModelDetails().getUserMemorySizeBits()); }
			 * 
			 * System.out.println("");
			 */

		}

	}

	private double RSSItoScale(double RSSI){
		System.out.println(RSSI*0.7/20+1.85);
		return RSSI*0.7/20+1.85;
	}
}
