import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MainSurface extends JPanel implements ActionListener{
	private BufferedImage sunImage;
	private BufferedImage moonImage;
	private BufferedImage backgroundDay;
	private BufferedImage backgroundNight;
	private Color dayColour;
	private Color nightColour;
	private float sunMoonX;
	private float sunMoonY;
	private int sunMoonW;
	private int sunMoonH;
	private float backgroundX;
	private float backgroundY;
	private Timer timer;
	private int delay = 25;
	private TimeCalculator timeCalculator;
	private WeatherCalculator weatherCalculator;
	private String tempStr;
	private BufferedImage movingImgInUse;
	private BufferedImage backgroundInUse;
	private BufferedImage weatherImg;
	private int weatherImgW = 60;
	private Color colourInUse;
	private float oldAngle;
	private float widthScale;
	private float heightScale;
	private BufferedImage clearSky, clouds, rain, snow, thunderstorm;
	private String weatherStr;
	Dictionary<String, BufferedImage> dictionary;
	private Point point;
	private BufferedImage settingsImg, settingsPressedImg, settingsButtonInUse;
	private int settingsStartW, settingsStartH;
	private int settingsX, settingsY, settingsW, settingsH;
	private MainSurface mainSurface;
	
	private void DoDrawing(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		
		g2d.scale(widthScale, heightScale);
		
		//Draws the time
		g2d.setFont(new Font("Lucida Sans", Font.BOLD, 36));
		g2d.setColor(Color.WHITE);
		String timeStr = timeCalculator.GetCurrentTime();
		g2d.drawString(timeStr, (Main.GetWidth()/2-100), Main.GetHeight()/2);
		g2d.drawString(tempStr, (Main.GetWidth()/2-100), (Main.GetHeight()/2)+36);
		g2d.drawString("Weather: " + weatherStr, (Main.GetWidth()/2-150), (Main.GetHeight()/2)+72);
		
		if(timeCalculator.DayTimeBool(timeStr) == true){
			movingImgInUse = sunImage;
			backgroundInUse = backgroundDay;
			colourInUse = dayColour;
		}else{
			movingImgInUse = moonImage;
			backgroundInUse = backgroundNight;
			colourInUse = nightColour;
		}
		float angle;
		angle = timeCalculator.GetAngle(timeStr);
		//Makes it so these calculations only happen when the angle has changed (makes the program more efficient)
		if(String.valueOf(angle).equals(String.valueOf(oldAngle)) == false){
			oldAngle = angle;
			int hypotenuse = 350;
			//Using trig with the angle calculated and a fixed distance from centre (hypotenuse) to get co-ords
			//If angle == 90 then an error will occur, so just put it straight above the middle
			if(angle ==90){
				sunMoonX = 400;
				sunMoonY = 400-hypotenuse;
			}else if(angle < 90){
				//Math.cos and Math.sin take the angle in radians
				sunMoonX = (float) (400-(hypotenuse * Math.cos(Math.toRadians(angle))));
				sunMoonY = (float) (400-(hypotenuse * Math.sin(Math.toRadians(angle))));
			}else{
				//cos between 90 and 180 degrees is negative, so you are adding a negative value (so overall subtracting it)
				sunMoonX = (float)(400 - (hypotenuse * Math.cos(Math.toRadians(angle))));
				sunMoonY = (float)(400 - (hypotenuse * Math.sin(Math.toRadians(angle))));
			}
			
		}
		
		//drawImage takes the co-ordinates as the top left of the image, but my coordinates that i've calculated are for the centre of the image, so minus width/2 and height/2
		g2d.drawImage(movingImgInUse, (int)sunMoonX - (sunMoonW/2), (int)sunMoonY - (sunMoonH/2), sunMoonW, sunMoonH, null);
		g2d.drawImage(weatherImg, 400-weatherImgW, 200, weatherImgW, weatherImgW, null);
		g2d.drawImage(backgroundInUse, (int)backgroundX, (int)backgroundY, 800, 200, null);
		g2d.drawImage(settingsButtonInUse, settingsX, settingsY, settingsW, settingsH, null);
		setBackground(colourInUse);
		
		
		
	}
	
	public void WindowResized(float newWidthScale, float newHeightScale){
		widthScale = newWidthScale;
		heightScale = newHeightScale;
		settingsW = (int)(settingsStartW * newWidthScale);
		settingsH = (int)(settingsStartH * newHeightScale);
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		DoDrawing(g);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stuff
		//This gets cursor location on overall screen, then subtracts the coordinates of the window on the screen so it's like the coordinates on the window
		
		//Checks to see if mouse hovering over settings button
		point = getMousePosition();
		
		try {
			if((point.x/widthScale < settingsW + settingsX) && (point.y/heightScale < settingsY + settingsH)) {
				//Settings button entered
				settingsButtonInUse = settingsPressedImg;
			}else {
				settingsButtonInUse = settingsImg;
			}
		}catch(Exception ex) {
			
		}
		
		repaint();
	}
	
	public void RefreshWeather(String mode, String lati, String longi, String city) {
		weatherCalculator = new WeatherCalculator(mode, lati, longi, city);
		weatherCalculator.DoHttpResponse();
		tempStr = weatherCalculator.GetTemperature();
		weatherStr = weatherCalculator.GetWeather();

		weatherImg = dictionary.get(weatherStr);
		if(weatherImg == null) {
			weatherImg = clearSky;
		}
		
	}
	
	public MainSurface() {
		//So at the start the oldangle will not equal the angle calculated
		oldAngle = -10;
		sunMoonX=50;
		sunMoonY=350;
		sunMoonW = 100;
		sunMoonH = 100;
		
		backgroundX = 0;
		backgroundY = 400;
		
		dayColour = new Color(137, 207, 240);
		nightColour = new Color(42, 10, 51);
		
		timer = new Timer(delay, this);
		timer.start();
		
		timeCalculator = new TimeCalculator();
		timeCalculator.GetCurrentTime();
		
		weatherCalculator = new WeatherCalculator("latLong", "53.35", "-2.733333", "");
		weatherCalculator.DoHttpResponse();
		tempStr = weatherCalculator.GetTemperature();
		weatherStr = weatherCalculator.GetWeather();
		
		mainSurface = this;
		
		widthScale = 1;
		heightScale = 1;
		
		settingsX = 0;
		settingsY = 0;
		//System.out.println(settingsStartW);
		settingsStartW = 40;
		settingsStartH = 40;
		settingsW = settingsStartW;
		settingsH = settingsStartH;
		addMouseListener(mouseListener);
		settingsButtonInUse = settingsImg;
		
		try {
			sunImage = ImageIO.read(MainSurface.class.getResource("Images/sun1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			moonImage = ImageIO.read(MainSurface.class.getResource("Images/moon.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
		try{
			backgroundDay = ImageIO.read(MainSurface.class.getResource("Images/BackgroundDay.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
		try{
			backgroundNight = ImageIO.read(MainSurface.class.getResource("Images/BackgroundNight.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
		try {
			clearSky = ImageIO.read(WeatherCalculator.class.getResource("Images/ClearSky.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			clouds = ImageIO.read(WeatherCalculator.class.getResource("Images/Clouds.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			rain = ImageIO.read(WeatherCalculator.class.getResource("Images/Rain.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			snow = ImageIO.read(WeatherCalculator.class.getResource("Images/Snow.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			thunderstorm = ImageIO.read(WeatherCalculator.class.getResource("Images/Thunderstorm.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			settingsImg = ImageIO.read(WeatherCalculator.class.getResource("Images/settingsButton.png"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		try {
			settingsPressedImg = ImageIO.read(WeatherCalculator.class.getResource("Images/settingsPressed.png"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		//Allows you to match the string to the BufferedImage
		dictionary = new Hashtable<>();
		dictionary.put("clear sky", clearSky);
		dictionary.put("clouds", clouds);
		dictionary.put("rain", rain);
		dictionary.put("snow", snow);
		dictionary.put("thunderstorm", thunderstorm);
		
		weatherImg = dictionary.get(weatherStr);
		if(weatherImg == null) {
			weatherImg = clearSky;
		}
		
	}
	
	private MouseListener mouseListener = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			point = e.getPoint();
			if((point.x/widthScale < settingsW + settingsX) && (point.y/heightScale < settingsY + settingsH)) {
				//Settings button pressed
				//MAKE NEW JFRAME USING NEW CLASS
				OptionsMenu optionsMenu = new OptionsMenu("latLong", mainSurface);
			}
		}
	};

	

}
