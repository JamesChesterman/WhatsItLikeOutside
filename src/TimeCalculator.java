import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeCalculator {
	private boolean dayBool;
	
	public String GetCurrentTime(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat formattedStr = new SimpleDateFormat("HH:mm:ss");
		String timeStr = formattedStr.format(calendar.getTime());
		return timeStr;
	}
	//The argument timeStr is passed after the GetCurrentTime method is called - avoids having to call it multiple times
	public boolean DayTimeBool(String timeStr){
		String hour = String.valueOf(timeStr.charAt(0)) + String.valueOf(timeStr.charAt(1));
		if (Integer.valueOf(hour) >= 6 && Integer.valueOf(hour) < 18){
			dayBool = true;
			return true;
		}else{
			dayBool = false;
			return false;
		}
	}
	
	public float GetAngle(String timeStr){
		int noOfMins;
		float angle;
		//In the form HH:mm:ss
		//720 mins in a day, 180 degrees to cover, so each minute accounts for 0.25 degrees
		int hour = Integer.valueOf(String.valueOf(timeStr.charAt(0)) + String.valueOf(timeStr.charAt(1)));
		int mins = Integer.valueOf(String.valueOf(timeStr.charAt(3)) + String.valueOf(timeStr.charAt(4)));
		if(dayBool == true){
			noOfMins = ((hour - 6)*60) + mins;
			
		}else{
			if(hour >= 18){
				noOfMins = ((hour-18)*60) + mins;
			}else{
				noOfMins = ((hour+6)*60) + mins;
			}
		}
		angle = (float) (noOfMins * 0.25);
		return angle;
	}

	public TimeCalculator() {
		// TODO Auto-generated constructor stub
		dayBool = true;
	}

}
