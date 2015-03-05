package Gesture_Recognition;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
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

	// Tap list
	ZoomTab zoomTab;
	MoveTab moveTab;

	// For image drawing and scaling
	ImagePanel tap2ImagePanel;
	JSpinner spinner2;

	// Tag list
	TagListPanel tagListPanel;

	// State label
	JLabel southPanel;
	// Parameters
	Parameters par;

	GestureFrame() {
		par = Parameters.getInstance();
		initUI();

	}

	private void initUI() {
		// South Panel
		southPanel = new JLabel("Click start to read tags");

		// North Panel
		JPanel northPanel = new JPanel();

		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startReader(southPanel);
			}
		});

		JButton stopButton = new JButton("Stop");
		stopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopReader(southPanel);
			}
		});

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopReader(southPanel);
				System.exit(0);
			}
		});

		// northPanel.add(spinner);
		northPanel.add(startButton);
		northPanel.add(stopButton);
		northPanel.add(closeButton);

		// East Panel
		tagListPanel = new TagListPanel();

		// Center Panel (Taps)
		JTabbedPane tab = new JTabbedPane();
		zoomTab = new ZoomTab(); // Tab1
		moveTab = new MoveTab(); // Tab2

		tab.addTab("Tab1", zoomTab);
		tab.addTab("Tab2", moveTab);
		tab.addTab("Tab3", new JPanel());

		getContentPane().add(northPanel, "North");
		getContentPane().add(tagListPanel, "East");
		getContentPane().add(tab, "Center");
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

			reader.setTagReportListener(new TagListener(zoomTab.spinner,
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
