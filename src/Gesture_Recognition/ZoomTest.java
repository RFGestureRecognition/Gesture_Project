package Gesture_Recognition;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.impinj.octanesdk.AntennaConfigGroup;
import com.impinj.octanesdk.ImpinjReader;
import com.impinj.octanesdk.OctaneSdkException;
import com.impinj.octanesdk.ReaderMode;
import com.impinj.octanesdk.ReportConfig;
import com.impinj.octanesdk.ReportMode;
import com.impinj.octanesdk.Settings;

public class ZoomTest extends JFrame {

	// Reader and antennas
	ImpinjReader reader;
	AntennaConfigGroup antennas;

	// For image drawing and scaling
	ImagePanel imagePanel;
	JSpinner spinner;

	// Tag list
	TagListPanel tagListPanel;

	//
	Parameters par;

	ZoomTest() {
		par = Parameters.getInstance();

		initUI();

	}

	private void initUI() {
		// South Panel
		JLabel southPanel = new JLabel("Click start to read tags");

		// Center Panel
		imagePanel = new ImagePanel("./Resources/moon.jpg");
		
		
		
		// North Panel
		JPanel northPanel = new JPanel();
		SpinnerNumberModel model = new SpinnerNumberModel(par.getScale(), 0.1,
				2, par.getScaleUnit());
		spinner = new JSpinner(model);
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				float scale = ((Double) spinner.getValue()).floatValue();
				imagePanel.setScale(scale);
			}
		});
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

		
		northPanel.add(new JLabel("scale"));
		northPanel.add(spinner);
		northPanel.add(startButton);
		northPanel.add(stopButton);
		northPanel.add(closeButton);

		// East Panel
		tagListPanel = new TagListPanel();

		getContentPane().add(northPanel, "North");
		getContentPane().add(tagListPanel, "East");
		getContentPane().add(new JScrollPane(imagePanel), "Center");
		getContentPane().add(southPanel, "South");
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

			reader.setTagReportListener(new TagListener(spinner, tagListPanel));

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
