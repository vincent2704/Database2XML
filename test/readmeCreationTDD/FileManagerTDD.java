package readmeCreationTDD;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

/**
 * This test suite uses current time, so some assertions need to be changed if
 * they contain current day. Test cases which include hours and minutes don't
 * contain assert methods, displaying console text instead.
 * 
 * @author Jarek
 *
 */
@RunWith(JUnitPlatform.class)
class FileManagerTDD {

	private GregorianCalendar gc;
	private Map<String, String> mapContent;

	@BeforeEach
	void setUp() {
		mapContent = new HashMap<>();
		gc = new GregorianCalendar();
	}

	@Test
	void createContentStringforFileBasedOnCurrentDay() {
		int iYear = gc.get(Calendar.YEAR);
		String strMonth = "";
		int iMonth = gc.get(Calendar.MONTH) + 1;
		if (iMonth < 10) {
			strMonth = "0" + iMonth;
		} else {
			strMonth = "" + iMonth;
		}
		int iDay = gc.get(Calendar.DAY_OF_MONTH);
		String strDay = "";
		if (iDay < 10) {
			strDay = "0" + iDay;
		} else {
			strDay = "" + iDay;
		}
		assertEquals(2018, iYear);
		assertEquals("01", strMonth);
		assertEquals("19", strDay);

		String strCurrentDay = "Database converted on (YYYY-MM-DD): " + iYear + "/" + strMonth + "/" + strDay;
		assertEquals("Database converted on (YYYY-MM-DD): 2018/01/19", strCurrentDay);
	}

	@Disabled
	@Test
	void initializeContentMapForReadmeFile() {

	}

	@Disabled
	@Test
	void createReadmeFile(String content) {
		File f = new File("Readme.txt");
		try {
			FileUtils.writeStringToFile(f, content, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
