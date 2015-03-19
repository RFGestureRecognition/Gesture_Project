package Gesture_Recognition;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.impinj.octanesdk.Tag;

public class MoveTab extends Tab {

	ImagePanel imagePanel;
	JSpinner spinner;
	Parameters par;

	StatePanel statePanel; // State label
	TagListPanel tagListPanel; // Tag list
	int reportNum = 0;
	static int maxReportNum = 500;

	boolean isCalibration = false;
	AntennaArray antennaArray;
	int k = 0;

	double x, y;
	double phaseDiff = 0;
	double distance = 0;

	MoveTab() {
		statePanel = StatePanel.getInstance();
		tagListPanel = TagListPanel.getInstance();
		par = Parameters.getInstance();
		imagePanel = new ImagePanel("./Resources/moon.jpg");

		SpinnerNumberModel model = new SpinnerNumberModel(0, -400, 400,
				par.getMoveSacle());
		spinner = new JSpinner(model);
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				float scale = ((Double) spinner.getValue()).floatValue();
				imagePanel.setImageLocation(scale, 0);
			}
		});

		Button caliButton = new Button("Calibration");
		caliButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(new JFrame(),
								"Please wait until the calibration is finished\n Click OK to proceed");
				if (isCalibration)
					stop();
				reportNum = 0;
				isCalibration = true;

				antennaArray = new AntennaArray(2); // input is the number of
													// antennas+1
			}
		});

		setLayout(new BorderLayout());
		add("North", spinner);
		add("Center", imagePanel);
		add("South", caliButton);
	}

	void endCali() {
		isCalibration = false;
		JOptionPane.showMessageDialog(new JFrame(), "Finished");
		statePanel.setText("The caliration is finished");
	}

	@Override
	void onTagReported(Tag t) {
		if (tagListPanel.getSelectedTag().equals(t.getEpc().toString())) {
			if (t.isPeakRssiInDbmPresent()) {
				// calibration
				if (isCalibration) {
					antennaArray.getPhaseMap(t.getAntennaPortNumber()).put(
							t.getChannelInMhz(), t.getPhaseAngleInRadians());

					reportNum++;

					statePanel.setText(reportNum + "/" + maxReportNum);

					if (reportNum >= maxReportNum)
						endCali();
				}

				// move object
				else {
					double curPhaseDiff = t.getPhaseAngleInRadians()
							- antennaArray
									.getPhaseMap(t.getAntennaPortNumber()).get(
											t.getChannelInMhz());
					if (curPhaseDiff > Math.PI) {
						curPhaseDiff -= 2 * Math.PI;
					} else if (curPhaseDiff < (Math.PI * -1)) {
						curPhaseDiff += 2 * Math.PI;
					}

					double deltaPhase = curPhaseDiff - phaseDiff;
					System.out.println(deltaPhase);

					if (deltaPhase > Math.PI) {
						deltaPhase -= 2 * Math.PI;
					} else if (deltaPhase < (Math.PI * -1)) {
						deltaPhase += 2 * Math.PI;
					}

					phaseDiff = curPhaseDiff;
					distance = distance + deltaPhase * 75.0
							/ t.getChannelInMhz() / Math.PI;

					if (t.getAntennaPortNumber() == 1)
						x = distance * 1000;
					else if (t.getAntennaPortNumber() == 2)
						y = deltaPhase * 100;

					imagePanel.setImageLocation(x, y);
					// spinner.setValue(phaseDiff*100);

					statePanel.setText("Distance: " + distance + "(m)");

				}
			}
		}
	}

	@Override
	void stop() {
		isCalibration = false;
		reportNum = 0;
	}

}

class AntennaArray {
	PhaseMap[] phaseMap;

	AntennaArray(int antennaNum) {
		phaseMap = new PhaseMap[antennaNum];
		for (int i = 0; i < antennaNum; i++)
			phaseMap[i] = new PhaseMap();
	}

	PhaseMap getPhaseMap(int antennaNum) {
		return phaseMap[antennaNum];
	}
}

class PhaseMap extends HashMap {
	PhaseMap() {
		super();
	}

	void put(double freq, double phase) {
		if (super.containsKey(freq)) {
			double temp = (phase + (double) super.get(freq)) / 2;
			super.put(freq, temp);
		} else {
			super.put(freq, phase);
		}
	}

	double get(double key) {
		return (double) super.get(key);
	}
}
