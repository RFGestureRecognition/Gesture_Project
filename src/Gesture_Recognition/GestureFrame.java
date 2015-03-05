package Gesture_Recognition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

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
	JTabbedPane tab;

	// For image drawing and scaling
	ImagePanel imagePanel;

	// Tag list
	TagListPanel tagListPanel;

	// State label
	JLabel stateLabel;
	// Parameters
	Parameters par;

	GestureFrame() {
		par = Parameters.getInstance();
		initUI();

	}

	private void initUI() {
		// South Panel
		stateLabel = new JLabel("Click start to read tags");

		// North Panel
		JPanel northPanel = new JPanel();

		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startReader(stateLabel);
			}
		});

		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopReader(stateLabel);
			}
		});

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopReader(stateLabel);
				System.exit(0);
			}
		});

		northPanel.add(startButton);
		northPanel.add(stopButton);
		northPanel.add(closeButton);

		// East Panel
		tagListPanel = new TagListPanel();

		// Center Panel (Taps)
		tab = new JTabbedPane();
		ZoomTab zoomTab = new ZoomTab(); // Tab1
		MoveTab moveTab = new MoveTab(); // Tab2
		MonitorTab monitorTab = new MonitorTab(); // Tab3

		tab.addTab("Zoom", zoomTab);
		tab.addTab("Move", moveTab);
		tab.addTab("Monitor", monitorTab);

		getContentPane().add(northPanel, "North");
		getContentPane().add(tagListPanel, "East");
		getContentPane().add(tab, "Center");
		getContentPane().add(stateLabel, "South");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1200, 900);
		setLocation(100, 50);
		setVisible(true);
	}

	public void startReader(JLabel stateLabel) {

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

			settings.setReaderMode(ReaderMode.AutoSetDenseReader);

			antennas = settings.getAntennas();
			antennas.disableAll();

			// set some special settings for antenna 1
			antennas.enableById(new short[] { 1 });
			antennas.getAntenna((short) 1).setIsMaxRxSensitivity(false);
			antennas.getAntenna((short) 1).setIsMaxTxPower(false);
			antennas.getAntenna((short) 1).setTxPowerinDbm(30.0);
			antennas.getAntenna((short) 1).setRxSensitivityinDbm(-70);

			// set some special settings for antenna 2
			antennas.enableById(new short[] { 2 });
			antennas.getAntenna((short) 2).setIsMaxRxSensitivity(false);
			antennas.getAntenna((short) 2).setIsMaxTxPower(false);
			antennas.getAntenna((short) 2).setTxPowerinDbm(30.0);
			antennas.getAntenna((short) 2).setRxSensitivityinDbm(-70);

			reader.setTagReportListener(new TagListener(tab,
					tagListPanel));

			reader.applySettings(settings);

			reader.start();
			stateLabel.setText("Connected");
		} catch (OctaneSdkException ex) {
			stateLabel.setText(ex.getMessage());
		} catch (Exception ex) {
			stateLabel.setText(ex.getMessage());
			ex.printStackTrace(System.out);
		}

	}

	public void stopReader(JLabel stateLabel) {
		try {
			reader.stop();
			reader.disconnect();
			stateLabel.setText("Disconnected");
		} catch (OctaneSdkException ex) {
			stateLabel.setText(ex.getMessage());
		} catch (Exception ex) {
			stateLabel.setText(ex.getMessage());
			ex.printStackTrace(System.out);
		}

	}

}
