package Gesture_Recognition;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.impinj.octanesdk.ImpinjReader;
import com.impinj.octanesdk.Tag;
import com.impinj.octanesdk.TagReport;
import com.impinj.octanesdk.TagReportListener;

public class ZoomTestFrame extends JFrame {

	ImagePanel panel;
	ImageZoom zoom;
	double defaultScale = 0.1;
	double defaultScaleUnit = 0.1;
	
	public ZoomTestFrame() {

		initUI();
	}

	private void initUI() {
		
		panel = new ImagePanel("./Resources/moon.jpg", defaultScale);
		zoom = new ImageZoom(panel, defaultScaleUnit);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(zoom.getUIPanel(), "North");
		getContentPane().add(new JScrollPane(panel));
		setSize(1000, 900);
		setLocation(100, 50);
		setVisible(true);

	}

	public double getScale(){
		return panel.scale;
	}
}

class ImagePanel extends JPanel {
	BufferedImage image;
	double scale;

	public ImagePanel(String filepath, double scale) {
		loadImage(filepath);
		this. scale = scale;
		setBackground(Color.black);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		int w = getWidth();
		int h = getHeight();
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		double x = (w - scale * imageWidth) / 2;
		double y = (h - scale * imageHeight) / 2;
		AffineTransform at = AffineTransform.getTranslateInstance(x, y);
		at.scale(scale, scale);
		g2.drawRenderedImage(image, at);
	}

	/**
	 * For the scroll pane.
	 */
	public Dimension getPreferredSize() {
		int w = (int) (scale * image.getWidth());
		int h = (int) (scale * image.getHeight());
		return new Dimension(w, h);
	}

	public void setScale(double s) {
		scale = s;
		revalidate(); // update the scroll pane
		repaint();
	}

	private void loadImage(String filepath) {

		try {
			image = ImageIO.read(new File(filepath));
		} catch (MalformedURLException mue) {
			System.out.println("URL trouble: " + mue.getMessage());
		} catch (IOException ioe) {
			System.out.println("read trouble: " + ioe.getMessage());
		}
	}
}

class ImageZoom {

	double scaleUnit = 0.2;

	ImagePanel imagePanel;

	public ImageZoom(ImagePanel ip, double scaleUnit) {
		imagePanel = ip;
		this.scaleUnit = scaleUnit;
	}

	public JPanel getUIPanel() {
		SpinnerNumberModel model = new SpinnerNumberModel(imagePanel.scale,
				0.1, 2, scaleUnit);
		final JSpinner spinner = new JSpinner(model);
		spinner.setPreferredSize(new Dimension(45,
				spinner.getPreferredSize().height));
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				float scale = ((Double) spinner.getValue()).floatValue();
				imagePanel.setScale(scale);
			}
		});
		JPanel panel = new JPanel();
		panel.add(new JLabel("scale"));
		panel.add(spinner);
		return panel;
	}
}
