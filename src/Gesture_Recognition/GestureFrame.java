package Gesture_Recognition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import com.impinj.octanesdk.AntennaConfigGroup;
import com.impinj.octanesdk.ImpinjReader;
import com.impinj.octanesdk.OctaneSdkException;
import com.impinj.octanesdk.ReaderMode;
import com.impinj.octanesdk.ReportConfig;
import com.impinj.octanesdk.ReportMode;
import com.impinj.octanesdk.Settings;

public class GestureFrame extends JFrame {

	// Reader and antennas
	ImpinjReader reader;
	AntennaConfigGroup antennas;

	// Tap
	TabManager tabManager;

	// For image drawing and scaling
	ImagePanel imagePanel;

	// Tag list
	TagListPanel tagListPanel;

	// State label
	StatePanel statePanel;
	// Parameters
	Parameters par;

	GestureFrame() {
		par = Parameters.getInstance();
		initUI();

	}

	private void initUI() {
		// South Panel
		statePanel = StatePanel.getInstance();
		statePanel.setText("Click start to read tags");

		// East Panel
		tagListPanel = TagListPanel.getInstance();

		// North Panel
		JPanel northPanel = new JPanel();

		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startReader();
			}
		});

		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopReader();
			}
		});

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopReader();
				tabManager.stop();
				System.exit(0);
			}
		});

		northPanel.add(startButton);
		northPanel.add(stopButton);
		northPanel.add(closeButton);

		// Center Panel (Taps)
		tabManager = new TabManager();
		ZoomTab zoomTab = new ZoomTab(); // Tab1
		MoveTab moveTab = new MoveTab(); // Tab2
		MonitorTab monitorTab = new MonitorTab(); // Tab3
		GraphTab graphTab = new GraphTab(); // Tab3

		tabManager.addTab("Zoom", zoomTab);
		tabManager.addTab("Move", moveTab);
		tabManager.addTab("Monitor", monitorTab);
		tabManager.addTab("Graph", graphTab);

		getContentPane().add(northPanel, "North");
		getContentPane().add(tagListPanel, "East");
		getContentPane().add(statePanel, "South");
		getContentPane().add(tabManager, "Center");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 900);
		setLocation(100, 50);
		setVisible(true);
	}

	public void startReader() {
		try {
			reader = new ImpinjReader();
			String hostname = "169.254.1.1";
			reader.connect(hostname);

			Settings settings = reader.queryDefaultSettings();
			ReportConfig report = settings.getReport();
			report.setIncludeAntennaPortNumber(true);
			report.setMode(ReportMode.Individual);
			report.setIncludePhaseAngle(true);
			report.setIncludePeakRssi(true);
			report.setIncludeDopplerFrequency(true);
			report.setIncludeChannel(true);
			report.setIncludeLastSeenTime(true);
			// settings.setReaderMode(ReaderMode.AutoSetmDenseReader);
			settings.setReaderMode(ReaderMode.MaxThroughput);
			// settings.setTxFrequenciesInMhz(txFrequenciesInMhz)

			antennas = settings.getAntennas();
			antennas.disableAll();
			antennas.enableAll();

			// set some special settings for antenna 1
			antennas.enableById(new short[] { 1 });
			antennas.getAntenna((short) 1).setIsMaxRxSensitivity(true);
			antennas.getAntenna((short) 1).setIsMaxTxPower(true);
			antennas.getAntenna((short) 1).setTxPowerinDbm(30.0);
			// antennas.getAntenna((short) 1).setRxSensitivityinDbm(-70);

			// // set some special settings for antenna 2
			antennas.enableById(new short[] { 2 });
			antennas.getAntenna((short) 2).setIsMaxRxSensitivity(true);
			antennas.getAntenna((short) 2).setIsMaxTxPower(true);
			antennas.getAntenna((short) 2).setTxPowerinDbm(30.0);
			antennas.getAntenna((short) 2).setRxSensitivityinDbm(-70);

			reader.setTagReportListener(tabManager);

			reader.applySettings(settings);

			reader.start();
			statePanel.setText("Connected");

		} catch (OctaneSdkException ex) {
			statePanel.setText(ex.getMessage());
		} catch (Exception ex) {
			statePanel.setText(ex.getMessage());
			ex.printStackTrace(System.out);
		}

	}

	public void stopReader() {
		try {
			
			reader.stop();
			reader.disconnect();

			statePanel.setText("Disconnected");

		} catch (OctaneSdkException ex) {
			statePanel.setText(ex.getMessage());
		} catch (Exception ex) {
			statePanel.setText(ex.getMessage());
			ex.printStackTrace(System.out);
		}

	}

}
