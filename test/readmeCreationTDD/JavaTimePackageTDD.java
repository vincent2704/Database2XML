package readmeCreationTDD;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

/**
 * Methods based on tests provided in this test suite are going to replace
 * Calendar class methods. Current time tests have System.out.println() methods
 * instead of assertions.
 * 
 * @author Jarek
 *
 */
@RunWith(JUnitPlatform.class)
class JavaTimePackageTDD {

	LocalDateTime ldtCurrent;
	LocalDateTime ldtCustom;

	@BeforeEach
	void setUp() {
		ldtCurrent = LocalDateTime.now();
	}

	@Test
	void showsCurrentYearMonthDayBasedOnLocalTimeZone() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		assertEquals("2018/02/01", ldtCurrent.format(format));
	}

	@Test
	void showsCustomYearMonthDayBasedOnLocalTimeZone() {
		ldtCustom = LocalDateTime.of(2022, 4, 27, 0, 0);
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		assertEquals("2022/04/27", ldtCustom.format(format));
	}

	@Test
	void showsCurrentHourBasedOnLocalTimeZone() {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("hh:mm");
		System.out.println(ldtCurrent.format(f));
	}

	@Test
	void showsCurrentDayAndHourBasedOnLocalTimeZone() {
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy/MM/dd hh:mm");
		System.out.println(ldtCurrent.format(f));
	}

	@Test
	void createCustomDateTimeFormatterWithBuilder() {
		DateTimeFormatterBuilder dtfb = new DateTimeFormatterBuilder();
		DateTimeFormatter f1 = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		dtfb = dtfb.append(f1);
		dtfb.appendLiteral(", at time of ");
		DateTimeFormatter f2 = DateTimeFormatter.ofPattern("hh:mm");
		dtfb = dtfb.append(f2);
		DateTimeFormatter f3 = dtfb.toFormatter();
		System.out.println(ldtCurrent.format(f3));
	}

	@Test
	void createCustomDateTimeFormatterWithBuilderIncludingLocalTimeZone() {
		DateTimeFormatterBuilder dtfb = new DateTimeFormatterBuilder();
		DateTimeFormatter day = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm");
		dtfb = dtfb.append(day);
		dtfb.appendLiteral(" ");
		dtfb = dtfb.append(time);
		DateTimeFormatter f = dtfb.toFormatter();		
		System.out.println(ldtCurrent.format(f));
	}

}
