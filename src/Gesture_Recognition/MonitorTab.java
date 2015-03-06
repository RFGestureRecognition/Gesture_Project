package Gesture_Recognition;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JLabel;

import com.impinj.octanesdk.Tag;

public class MonitorTab extends Tab {
	ArrayList<JLabel> jLabelList;
	int antennaNum = 2;
	int paraNum = 4;
	TagListPanel tagListPanel;
	MonitorTab() {
		tagListPanel = TagListPanel.getInstance();
		ArrayList<String> paraNames = new ArrayList<String>();
		jLabelList = new ArrayList<JLabel>();

		for (int i = 0; i < antennaNum * paraNum; i++) {
			jLabelList.add(new JLabel("0"));
		}

		paraNames.add("Phase");
		paraNames.add("TX Freq");
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

	@Override
	void onTagReported(Tag t) {
		if (tagListPanel.getSelectedTag().equals(t.getEpc().toString())) {
			for (int j = 0; j < antennaNum; j++) {
				if ((t.getAntennaPortNumber() - 1) == j)
					jLabelList.get(j).setText(t.getPhaseAngleInRadians() + "");
			}
			for (int j = 0; j < antennaNum; j++) {
				if ((t.getAntennaPortNumber() - 1) == j)
					jLabelList.get(j + antennaNum).setText(
							t.getChannelInMhz() + "");
			}

			for (int j = 0; j < antennaNum; j++) {
				if ((t.getAntennaPortNumber() - 1) == j)
					jLabelList.get(j + 2 * antennaNum).setText(
							t.getPeakRssiInDbm() + "");
			}
			for (int j = 0; j < antennaNum; j++) {
				if ((t.getAntennaPortNumber() - 1) == j)
					jLabelList.get(j + 3 * antennaNum).setText(
							t.getRfDopplerFrequency() + "");
			}
		}
		
	}
}
