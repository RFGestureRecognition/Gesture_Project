package Gesture_Recognition;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.impinj.octanesdk.Tag;

public class ZoomTab extends Tab  {
	ImagePanel imagePanel;
	JSpinner spinner;
	Parameters par;
	TagListPanel tagListPanel;

	ZoomTab() {
		tagListPanel = TagListPanel.getInstance();
		par = Parameters.getInstance();
		imagePanel = new ImagePanel("./Resources/moon.jpg");
		SpinnerNumberModel model = new SpinnerNumberModel(par.getScale(), 0.1,
				2, par.getScaleUnit());

		spinner = new JSpinner(model);
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				float scale = ((Double) spinner.getValue()).floatValue();
				imagePanel.setScale(scale);
			}
		});

		setLayout(new BorderLayout());
		add("North", spinner);
		add("Center", imagePanel);
		setBackground(Color.BLACK);

	}

	@Override
	void onTagReported(Tag t) {
		if (tagListPanel.getSelectedTag().equals(t.getEpc().toString())) {

			if (t.isPeakRssiInDbmPresent()) {
				double curScale = RSSItoScale(t.getPeakRssiInDbm());
				if (Double.compare(curScale, par.getTempScale()) != 0) {
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
		
	}

	private double RSSItoScale(double RSSI) {
		double scale = Math.sqrt(RSSI * 0.0315 + 1.585);
		double temp = Math.round(scale * 100d) / 100d;
		if (temp < 0.05)
			temp = 0.05;
		return Math.round(scale * 100d) / 100d;
	}
}
