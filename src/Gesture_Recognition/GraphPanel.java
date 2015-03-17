package Gesture_Recognition;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GraphPanel extends JPanel {
   private static final int MAX_SCORE = 20;
   private static final int PREF_W = 1000;
   private static final int PREF_H = 1000;
   private static final int BORDER_GAP = 30;
   private static final Color GRAPH_POINT_COLOR = new Color(150, 50, 50, 180);
   private static final int GRAPH_POINT_WIDTH = 12;
   private static final int Y_HATCH_CNT = 10;
   private List<Integer> scores;
   public static List<Point> point;
   
   int count;

   public GraphPanel(){
		point = new ArrayList<Point>();
		// point.add(new Point(3,5));
		GraphPanel mainPanel = new GraphPanel(point);

		JFrame frame = new JFrame("DrawGraph");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(mainPanel);
		frame.pack();
		frame.setLocationByPlatform(true);
		
		count = 0;
		//frame.setVisible(true);
   }
   public GraphPanel(List<Point> point){
	   this.point = point;
   }

   @Override
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
//	   g.clearRect(0, 0, getWidth(), getHeight());
//      Graphics2D g2 = (Graphics2D)g;
      Image img = createImage(getWidth(), getHeight());
      Graphics2D g2 = (Graphics2D)img.getGraphics();
      g.drawImage(img, 0, 0, this);
      
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      // create x and y axes 
      g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, BORDER_GAP, BORDER_GAP);
      g2.drawLine(BORDER_GAP, getHeight() - BORDER_GAP, getWidth() - BORDER_GAP, getHeight() - BORDER_GAP);

      // create hatch marks for y axis. 
      for (int i = 0; i < Y_HATCH_CNT; i++) {
         int x0 = BORDER_GAP;
         int x1 = GRAPH_POINT_WIDTH + BORDER_GAP;
         int y0 = getHeight() - (((i + 1) * (getHeight() - BORDER_GAP * 2)) / Y_HATCH_CNT + BORDER_GAP);
         int y1 = y0;
         g2.drawLine(x0, y0, x1, y1);
      }

      // and for x axis
      for (int i = 0; i < point.size(); i++) {
         int x0 = (i + 1) * (getWidth() - BORDER_GAP * 2) / 100 + BORDER_GAP;
         int x1 = x0;
         int y0 = getHeight() - BORDER_GAP;
         int y1 = y0 - GRAPH_POINT_WIDTH;
         g2.drawLine(x0, y0, x1, y1);
      }

      Stroke oldStroke = g2.getStroke();
      g2.setStroke(oldStroke);      
      g2.setColor(GRAPH_POINT_COLOR);
      for (int i = 0;i<point.size(); i++) {
         int x = point.get(i).x;
         int y = point.get(i).y;
         int ovalW = GRAPH_POINT_WIDTH;
         int ovalH = GRAPH_POINT_WIDTH;
         g2.fillOval(x, y, ovalW, ovalH);
      }
      g.drawImage(img, 0, 0, this);
   }

   @Override
   public Dimension getPreferredSize() {
      return new Dimension(PREF_W, PREF_H);
   }
   
   void Func(int x, int y){
	   point.add(new Point(x,y));
	   count++;
	   if(count>10){
		   repaint();
		   //paintComponent(this.getGraphics());
		   count = 0;
	   }
   }
   
   void Clear(){
	   point.clear();
   }

   static void createAndShowGui() {
	  
      List<Integer> scores = new ArrayList<Integer>();
      Random random = new Random();
      int maxDataPoints = 16;
      int maxScore = 20;
      for (int i = 0; i < maxDataPoints ; i++) {
         scores.add(random.nextInt(maxScore));
      }
      point = new ArrayList<Point>();
      //point.add(new Point(3,5));
      GraphPanel mainPanel = new GraphPanel(point);

      JFrame frame = new JFrame("DrawGraph");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(mainPanel);
      frame.pack();
      frame.setLocationByPlatform(true);
      frame.setVisible(true);
   }

//   public static void main(String[] args) {
//      SwingUtilities.invokeLater(new Runnable() {
//         public void run() {
//            createAndShowGui();
//         }
//      });
//   }
}