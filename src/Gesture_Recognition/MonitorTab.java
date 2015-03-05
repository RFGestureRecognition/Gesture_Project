package Gesture_Recognition;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class MonitorTab extends JPanel {
	ArrayList<JLabel> jLabelList;
	int antennaNum = 2;
	int paraNum = 4;

	MonitorTab() {

		ArrayList<String> paraNames = new ArrayList<String>();
		jLabelList = new ArrayList<JLabel>();

		for (int i = 0; i < antennaNum * paraNum; i++) {
			jLabelList.add(new JLabel("0"));
		}

		paraNames.add("Phase");
		paraNames.add("Phase Diff");
		paraNames.add("RSSI");
		paraNames.add("DopplerFreq");

		setLayout(new GridLayout(0, antennaNum + 1));
		add(new JLabel(" "));

		for (int i = 0; i < antennaNum; i++)
			add(new JLabel("Monitor" + (i + 1)));

		for (int i = 0; i < paraNum; i++) {
			add(new JLabel(paraNames.get(i)));

			for (int j = 0; j < antennaNum; j++) {
				add(jLabelList.get(i * antennaNum + j));
			}
		}

	}
}
