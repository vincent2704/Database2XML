package functions;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Class designed to support time printing functions for FileManager's
 * Readme.txt file creation. All methods are declared protected since there's no
 * sense in enabling them for the other packages.
 * 
 * This class is going to be deprecated in the future as java.time package will
 * be implemented.
 * 
 * @author Jarek
 *
 */
public class CalendarManager {

	private GregorianCalendar gc;

	protected CalendarManager() {
		gc = new GregorianCalendar();
	}

	protected String getCurrentDay() {
		String strCurrentDay;
		int iYear = gc.get(Calendar.YEAR);
		int iMonth = gc.get(Calendar.MONTH) + 1;
		int iDay = gc.get(Calendar.DAY_OF_MONTH);
		String strMonth, strDay;
		strMonth = iMonth < 10 ? "0" + iMonth : "" + iMonth;
		strDay = iDay < 10 ? "0" + iDay : "" + iDay;
		strCurrentDay = "Day (YYYY-MM-DD): " + iYear + "/" + strMonth + "/" + strDay;

		return strCurrentDay;
	}

	protected String getCurrentTime() {
		String strCurrentTime;
		int iHour = gc.get(Calendar.HOUR_OF_DAY);
		int iMinutes = gc.get(Calendar.MINUTE);
		String strHour = "" + iHour;
		strHour = iHour < 10 ? "0" + iHour : strHour;
		String strMinutes = "" + iMinutes;
		strMinutes = iMinutes < 10 ? "0" + iMinutes : strMinutes;
		strCurrentTime = strHour + ":" + strMinutes;

		return strCurrentTime;
	}

	protected String getCurrentTimeWithSeconds() {
		int iHour = gc.get(Calendar.HOUR_OF_DAY);
		int iMinutes = gc.get(Calendar.MINUTE);
		String strHour = "" + iHour;
		strHour = iHour < 10 ? "0" + iHour : strHour;
		String strMinutes = "" + iMinutes;
		strMinutes = iMinutes < 10 ? "0" + iMinutes : strMinutes;
		int iSeconds = gc.get(Calendar.SECOND);
		String strSeconds = "" + iSeconds;
		strSeconds = iSeconds < 10 ? "0" + iSeconds : "" + iSeconds;

		return "Current hour: " + strHour + ":" + strMinutes + " :" + strSeconds + "sec.";
	}

}
