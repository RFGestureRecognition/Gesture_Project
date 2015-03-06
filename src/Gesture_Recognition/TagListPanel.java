package Gesture_Recognition;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.impinj.octanesdk.Tag;

public class TagListPanel extends JPanel {
	private static TagListPanel sigleton = new TagListPanel();
	private JLabel label;
	private JScrollPane pane;
	private JList<String> tagList;

	DefaultListModel<String> listModel;

	public static TagListPanel getInstance() {
		return sigleton;
	}

	private TagListPanel() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		listModel = new DefaultListModel<String>();

		tagList = new JList<String>(listModel);
		tagList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					label.setText(tagList.getSelectedValue().toString()
							+ " is selected");
				}
			}
		});
		JScrollPane pane = new JScrollPane();
		pane.getViewport().add(tagList);
		pane.setPreferredSize(new Dimension(250, 200));
		add(pane);

		label = new JLabel("Select the tag");
		;
		add(label, BorderLayout.SOUTH);
	}

	public void addTag(Tag t) {
		if (listModel.contains(t.getEpc().toString()) == false) {
			listModel.addElement(t.getEpc().toString());
		}
	}

	public String getSelectedTag() {
		return tagList.getSelectedValue().toString();
	}

}