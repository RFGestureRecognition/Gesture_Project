package Gesture_Recognition;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MoveTab extends JPanel{

	ImagePanel imagePanel;
	JSpinner spinner;
	Parameters par;
	
	MoveTab(){
		par = Parameters.getInstance();
		imagePanel = new ImagePanel("./Resources/moon.jpg");
		
		SpinnerNumberModel model = new SpinnerNumberModel(0, -400, 400, par.getMoveSacle());
		spinner = new JSpinner(model);
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				float location = ((Double) spinner.getValue()).floatValue();
				imagePanel.setImageLocation((int) location, 0);
			}
		});
		
		setLayout(new BorderLayout());

		add("North", spinner);
		add("Center", imagePanel);
	}
	

}
