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

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {
	BufferedImage image;
	Parameters par;

	public ImagePanel(String filepath) {
		par = Parameters.getInstance();
		loadImage(filepath);
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
		double x = (w - par.getScale() * imageWidth) / 2;
		double y = (h - par.getScale() * imageHeight) / 2;
		AffineTransform at = AffineTransform.getTranslateInstance(x, y);
		at.scale(par.getScale(), par.getScale());
		g2.drawRenderedImage(image, at);
	}

	/**
	 * For the scroll pane.
	 */
	public Dimension getPreferredSize() {
		int w = (int) (par.getScale() * image.getWidth());
		int h = (int) (par.getScale() * image.getHeight());
		return new Dimension(w, h);
	}

	public void setScale(double s) {
		par.setScale(s);
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

