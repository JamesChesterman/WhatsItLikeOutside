import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JOptionPane;

import javax.imageio.ImageIO;



public class WeatherCalculator {
	String urlStr;
	String tempStr;
	static String longitude;
	static String latitude;
	static String cityName;
	String APIKEY = "1f861231d1810675c98c1148cc84482e";
	String response;
	BufferedImage clearSky;
	BufferedImage clouds;
	BufferedImage rain;
	BufferedImage snow;
	BufferedImage thunderstorm;
	Dictionary<String, BufferedImage> dictionary;
	BufferedImage[] weatherImgList = new BufferedImage[5];
	String[] weatherStrList = {"clear sky", "clouds", "rain", "snow", "thunderstorm", "haze"};
	
	public static String GetLongitude() {
		return longitude;
	}
	public static String GetLatitude() {
		return latitude;
	}
	public static String GetCity() {
		return cityName;
	}
	
	public void DoHttpResponse() {
		try {
			//Make client, make request then get the response
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = HttpRequest.newBuilder()
					.uri(URI.create(urlStr))
					.build();
			response = client.send(request, BodyHandlers.ofString()).body();
			System.out.println(response);
		}catch(Exception e) {
			JOptionPane.showMessageDialog(null, "Error, try again");
		}
	}
	
	public String GetTemperature() {
		int tempIndex = response.indexOf("temp");
		//Don't include decimals yet, it messes up the subtraction operation (float rounding error)
		String tempStrKelvinInt = response.substring(tempIndex+6, tempIndex+9);
		
		String tempStrDecimals;
		if(response.charAt(tempIndex+11) == ',') {
			tempStrDecimals = response.substring(tempIndex+9, tempIndex+11);
		}else {
			tempStrDecimals = response.substring(tempIndex+9, tempIndex+12);
		}
		//To go from Kevlin to Celsius you subtract 273
		int temperature = Integer.valueOf(tempStrKelvinInt)-273;
		//Now appending two decimal places along with the degrees symbol and C
		char degrees = '\u00B0';
		tempStr = String.valueOf(temperature) + tempStrDecimals + degrees + "C";
		return tempStr;
	}
	
	public String GetWeather() {
		String weatherStr = null;
		
		int descIndex = response.indexOf("description");
		int nextCommaIndex = (response.substring(descIndex, response.length())).indexOf(',') + descIndex;
		
		for(int i =0;i<weatherStrList.length;i++) {
			if((response.substring(descIndex, nextCommaIndex)).indexOf(weatherStrList[i]) > -1){
				weatherStr = weatherStrList[i];
			}
		}
		return weatherStr;
	}
	
	
	
	public WeatherCalculator(String mode, String lat, String longi, String city) {
		// TODO Auto-generated constructor stub
		//urlStr = ("https://api.openweathermap.org/data/2.5/weather?q=London&appid=1f861231d1810675c98c1148cc84482e");
		latitude = lat;
		longitude = longi;
		cityName = city;
		if(mode == "latLong") {
			urlStr = "https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&appid="+APIKEY;
		}else {
			urlStr = "https://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid="+APIKEY;
		}
		
		response = "";
		
		
	}

}
