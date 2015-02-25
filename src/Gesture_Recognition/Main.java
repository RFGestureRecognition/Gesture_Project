package Gesture_Recognition;

import java.util.Scanner;

import com.impinj.octanesdk.AntennaConfigGroup;
import com.impinj.octanesdk.ImpinjReader;
import com.impinj.octanesdk.OctaneSdkException;
import com.impinj.octanesdk.ReaderMode;
import com.impinj.octanesdk.ReportConfig;
import com.impinj.octanesdk.ReportMode;
import com.impinj.octanesdk.Settings;
import com.impinj.octanesdk.samples.helpers.TagReportListenerImplementation;
import com.impinj.octanesdk.samples.helpers.SampleProperties;

public class Main {
	public static void main(String[] args) {
		
		Saturn ex = new Saturn();
        ex.setVisible(true);
		
		/*
		try {
			//String hostname = System.getProperty(SampleProperties.hostname);
			String hostname = "169.254.1.1";
			if (hostname == null) {
				throw new Exception("Must specify the '"
						+ SampleProperties.hostname + "' property");
			}

			
			
			
			ImpinjReader reader = new ImpinjReader();

			System.out.println("Connecting");
			reader.connect(hostname);

			Settings settings = reader.queryDefaultSettings();

			ReportConfig report = settings.getReport();
			report.setIncludeAntennaPortNumber(true);
			report.setMode(ReportMode.Individual);
			report.setIncludePhaseAngle(true);
			report.setIncludePeakRssi(true);
		

			settings.setReaderMode(ReaderMode.AutoSetDenseReader);

			
			AntennaConfigGroup antennas = settings.getAntennas();
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
			
			
			reader.setTagReportListener(new TagReportListenerImplementation());

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
	
	*/
	}
}
