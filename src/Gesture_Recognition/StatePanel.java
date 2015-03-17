package Gesture_Recognition;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatePanel extends JPanel {
	private static StatePanel singleTon = new StatePanel();
	private JLabel label;

	StatePanel() {
		label = new JLabel();
		label.setHorizontalAlignment(JLabel.LEFT);
		add(label);
	}

	public static StatePanel getInstance() {

		return singleTon;
	}

	public void setText(String txt) {
		label.setText(txt);
	}

}
