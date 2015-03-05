package Gesture_Recognition;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
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
	ImagePanel imagePanel, imagePanel2;
	JSpinner spinner, spinner2;

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
		final JLabel southPanel = new JLabel("Click start to read tags");

		
		
		
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

		
		//northPanel.add(spinner);
		northPanel.add(startButton);
		northPanel.add(stopButton);
		northPanel.add(closeButton);

		// East Panel
		tagListPanel = new TagListPanel();
		
		// Center Panel

		imagePanel = new ImagePanel("./Resources/moon.jpg");
		imagePanel2 = new ImagePanel("./Resources/moon.jpg");
		
		// Tab1
		SpinnerNumberModel model = new SpinnerNumberModel(par.getScale(), 0.1,
				2, par.getScaleUnit());
		spinner = new JSpinner(model);
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				float scale = ((Double) spinner.getValue()).floatValue();
				imagePanel.setScale(scale);
			}
		});
		
		JPanel Tab1Panel = new JPanel();
		Tab1Panel.setLayout(new BorderLayout());

		Tab1Panel.add("North", spinner);
		Tab1Panel.add("Center", imagePanel);
		
		// Tab2
		SpinnerNumberModel model2 = new SpinnerNumberModel(100,0,200,10);
		spinner2 = new JSpinner(model2);
		spinner2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				int location = (int) spinner2.getValue();
				imagePanel2.setLocation(location-50, 0);
			}
		});
		
		JPanel Tab2Panel = new JPanel();
		Tab2Panel.setLayout(new BorderLayout());
		Tab2Panel.add("North", spinner2);
		Tab2Panel.add("Center", imagePanel2);
		Tab2Panel.setBackground(Color.BLACK);
		
		JTabbedPane tab = new JTabbedPane();
		tab.addTab("Tab1", Tab1Panel);
		tab.addTab("Tab2", Tab2Panel);
		tab.addTab("Tab3", new JPanel());
		
		

		getContentPane().add(northPanel, "North");
		getContentPane().add(tagListPanel, "East");
		//getContentPane().add(new JScrollPane(imagePanel), "Center");
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
