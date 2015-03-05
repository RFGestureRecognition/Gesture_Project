package Gesture_Recognition;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ZoomTab extends JPanel {
	ImagePanel imagePanel;
	JSpinner spinner;
	Parameters par;
	ZoomTab() {
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

}
