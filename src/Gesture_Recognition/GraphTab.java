package Gesture_Recognition;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.impinj.octanesdk.Tag;

public class GraphTab extends Tab {

	GraphPanel graphPanel;
	Parameters par;
	TagListPanel tagListPanel;
	Button clearButton;
	
	GraphTab(){
		tagListPanel = TagListPanel.getInstance();
		par = Parameters.getInstance();
		graphPanel = new GraphPanel();
		clearButton = new Button("Clear");
		clearButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				graphPanel.Clear();				
			}
		});
		
		setLayout(new BorderLayout());

		add("Center", graphPanel);
		add("South", clearButton);
		
	}

	@Override
	void onTagReported(Tag t) {
		// TODO Auto-generated method stub
		if (tagListPanel.getSelectedTag().equals(t.getEpc().toString())) {

			if (t.isPeakRssiInDbmPresent()) {				
				graphPanel.Func((int)(t.getChannelInMhz()-902.25)*20+30, (int)(t.getPhaseAngleInRadians()*100), t.getAntennaPortNumber());
			}
		}
	}

	@Override
	void stop() {
		
	}
	

}
