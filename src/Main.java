import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main {
	private static int WIDTH = 800;
	private static int HEIGHT = 600;
	JFrame frame;
	
	public static int GetWidth(){
		return WIDTH;
	}
	public static int GetHeight(){
		return HEIGHT;
	}


	public Main() {
		// TODO Auto-generated constructor stub
		//This is the JFrame that contains the graphics window
		frame = new JFrame();
		//DONT PUT SETLAYOUT
		frame.setTitle("What's It Like Outside?");
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setResizable(true);
		
		//Need it to appear in the centre of the screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Point middle = new Point(screenSize.width / 2, screenSize.height / 2);
		//Don't add width to the middle.x so that entire window fits after the centre of the screen is reached
		Point newLocation = new Point(middle.x - (WIDTH/2), middle.y - (HEIGHT / 2));
		frame.setLocation(newLocation);
		
		MainSurface mainSurface = new MainSurface();
		frame.add(mainSurface);
		
	    frame.setVisible(true);
	   
	    frame.addComponentListener(new ComponentListener(){
			@Override
			public void componentHidden(ComponentEvent e) {}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentResized(ComponentEvent e) {
				//WHEN WINDOW IS RESIZED
        		Dimension screenSize = frame.getBounds().getSize();
        		float newWidth =  (float)screenSize.getWidth();
        		float newHeight = (float)screenSize.getHeight();
        		//Using WIDTH and HEIGHT as they were the original values for width and height
        		mainSurface.WindowResized((float)newWidth/WIDTH, (float)newHeight/HEIGHT);
			}
			@Override
			public void componentShown(ComponentEvent e) {}
	    });
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Main main = new Main();
	}

}
