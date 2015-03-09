package Gesture_Recognition;

import java.awt.GridLayout;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JLabel;

import com.impinj.octanesdk.Tag;

public class MonitorTab extends Tab {
	ArrayList<JLabel> jLabelList;
	int antennaNum = 2;
	int paraNum = 4;
	TagListPanel tagListPanel;
	double phase1;
	double phase2;
	int reportNum = 0;
	FileWriter writer;
	MonitorTab() {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
			Date date = new Date();
			writer = new FileWriter("Results/"+dateFormat.format(date));
			writer.write("Num\tRSSI\tFrequency\tPhase\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		phase1 = 0;
		phase2 = 0;
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
			
			// file write
			reportNum++;
			try {
				writer.write(reportNum+"\t"+t.getPeakRssiInDbm()+"\t"+t.getChannelInMhz()+"\t"+t.getPhaseAngleInRadians()+"\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
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

	@Override
	void stop() {

		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
