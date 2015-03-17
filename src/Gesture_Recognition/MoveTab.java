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
	static int maxReportNum = 300;

	boolean isCalibration = false;

	PhaseMap phaseMap;
	int k = 0;

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
				phaseMap = new PhaseMap();
			}
		});

		setLayout(new BorderLayout());
		add("North", spinner);
		add("Center", imagePanel);
		add("South", caliButton);
	}

	void endCali() {
		isCalibration = false;
		for (double i = 902.25; i <= 926.75; i = i + 0.5) {
			if (!phaseMap.containsKey(i)) {

			}
		}
		JOptionPane.showMessageDialog(new JFrame(), "Finished");
		statePanel.setText("The caliration is finished");
	}

	@Override
	void onTagReported(Tag t) {
		if (tagListPanel.getSelectedTag().equals(t.getEpc().toString())) {
			// calibration
			if (isCalibration) {
				phaseMap.put(t.getChannelInMhz(), t.getPhaseAngleInRadians());
				reportNum++;
				statePanel.setText(reportNum + "/" + maxReportNum);
				if (reportNum >= maxReportNum)
					endCali();
			}

			// move object
			else {
				double curPhase = t.getPhaseAngleInRadians();
				double phaseDiff = phaseMap.get(t.getChannelInMhz()) - curPhase;
				System.out.println(phaseDiff);
				spinner.setValue(phaseDiff * 50);
			}
		}
	}

	@Override
	void stop() {
		isCalibration = false;
		reportNum = 0;
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
