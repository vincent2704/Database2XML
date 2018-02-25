package readmeCreationTDD;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;;

/**
 * This test suite uses current time, so some assertions need to be changed when
 * they contain current day. Test cases which include hours and minutes don't
 * contain assert methods, displaying console text instead.
 * 
 * Class using this test is going to be deprecated in the future and be replaced
 * by java.time package methods.
 * 
 * @author Jarek
 *
 */
@RunWith(JUnitPlatform.class)
class CalendarTDD {

	GregorianCalendar gc;

	@BeforeEach
	void setup() {
		gc = new GregorianCalendar();
	}

	@Test
	void showsYearWithDefaultConstructor() {
		int iYear = gc.get(Calendar.YEAR);
		assertEquals(2018, iYear);
	}

	@Test
	void showsYearWithCustomConstructor() {
		GregorianCalendar gc = new GregorianCalendar(2033, 4, 27);
		int iYear = gc.get(Calendar.YEAR);
		assertEquals(2033, iYear);
	}

	void showsMonthWithCustromConstructor1() {
		GregorianCalendar gc = new GregorianCalendar(2033, Calendar.JANUARY, 27);
		assertEquals(1, gc.get(2));
	}

	void showsMonthWithCustromConstructor2() {
		GregorianCalendar gc = new GregorianCalendar(2033, Calendar.FEBRUARY, 27);
		assertEquals(2, gc.get(2));
	}

	void showsMonthWithCustromConstructor3() {
		GregorianCalendar gc = new GregorianCalendar(2033, Calendar.OCTOBER, 27);
		assertEquals(10, gc.get(2));
	}

	void showsMonthWithCustromConstructor4() {
		GregorianCalendar gc = new GregorianCalendar(2033, Calendar.DECEMBER, 27);
		assertEquals(12, gc.get(2));
	}

	@Test
	void showMonthWithDefaultConstructorAsInt() {
		int iMonth = gc.get(Calendar.MONTH) + 1;
		assertEquals(1, iMonth);
	}

	@Test
	void showYearWithMonthAndDay() {
		int year = gc.get(Calendar.YEAR);
		int month = gc.get(Calendar.MONTH) + 1;
		int day = gc.get(Calendar.DAY_OF_MONTH);

		assertEquals(2018, year);
		assertEquals(1, month);
		assertEquals(25, day);
	}

	@Test
	void showCurrentDayInProperFormat() {
		int iMonth = gc.get(Calendar.MONTH) + 1;
		int iDay = gc.get(Calendar.DAY_OF_MONTH);
		String strMonth, strDay;
		strMonth = iMonth < 10 ? "0" + iMonth : "" + iMonth;
		strDay = iDay < 10 ? "0" + iDay : "" + iDay;

		assertEquals("01", strMonth);
		assertEquals("25", strDay);
	}

	@Test
	void showMonthInTwoDigits() {
		String strMonth = "";
		int iMonth = gc.get(Calendar.MONTH) + 1;
		if (iMonth < 10) {
			strMonth = "0" + iMonth;
		} else {
			strMonth = "" + iMonth;
		}
		assertEquals("01", strMonth);
	}

	@Test
	void showCurrentHourAndMinutes() {
		int iHour = gc.get(Calendar.HOUR_OF_DAY);
		int iMinutes = gc.get(Calendar.MINUTE);
		String strHour = "" + iHour;
		strHour = iHour < 10 ? "0" + iHour : strHour;
		String strMinutes = "" + iMinutes;
		strMinutes = iMinutes < 10 ? "0" + iMinutes : strMinutes;

		System.out.println("Current hour: " + strHour + ":" + strMinutes);
	}

	@Test
	void showCurrentHourAndMinutesAndSeconds() {
		int iHour = gc.get(Calendar.HOUR_OF_DAY);
		int iMinutes = gc.get(Calendar.MINUTE);
		String strHour = "" + iHour;
		strHour = iHour < 10 ? "0" + iHour : strHour;
		String strMinutes = "" + iMinutes;
		strMinutes = iMinutes < 10 ? "0" + iMinutes : strMinutes;
		int iSeconds = gc.get(Calendar.SECOND);
		String strSeconds = "" + iSeconds;
		strSeconds = iSeconds < 10 ? "0" + iSeconds : "" + iSeconds;

		System.out.println("Current hour: " + strHour + ":" + strMinutes + " :" + strSeconds + "sec.");
	}

}
