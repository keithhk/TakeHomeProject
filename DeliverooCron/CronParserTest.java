import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Map;

import org.junit.Test;

public class CronParserTest {

	@Test
	public void testDeliverooTestCase() {
		CronParser cronParser = new CronParser();
		try {
			Map<String, String> map = cronParser.parseCron("*/15 0 1,15 * 1-5 /usr/bin/find");
			assertEquals(map.get("minute"), "0 15 30 45");
			assertEquals(map.get("hour"), "0");
			assertEquals(map.get("day of month"), "1 15");
			assertEquals(map.get("month"), "1 2 3 4 5 6 7 8 9 10 11 12");
			assertEquals(map.get("day of week"), "1 2 3 4 5");
			assertEquals(map.get("command"), "/usr/bin/find");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testWildCardOddDivisor() {
		CronParser cronParser = new CronParser();
		try {
			Map<String, String> map = cronParser.parseCron("*/7 0 1,15 * 1-5 /usr/bin/find");
			assertEquals(map.get("minute"), "0 7 14 21 28 35 42 49 56");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCommandContainStar() {
		CronParser cronParser = new CronParser();
		try {
			Map<String, String> map = cronParser.parseCron("*/59 2 1-31 * 1-5 /***");
			assertEquals(map.get("command"), "/***");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//--------Test Valid Range
	@Test
	public void testValidRangeMinute() {
		CronParser cronParser = new CronParser();
		try {
			assertThrows(IllegalArgumentException.class, () -> {cronParser.parseCron("0-61 0 1-31 * 1-5 /***"); } );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testValidRangeHour() {
		CronParser cronParser = new CronParser();
		try {
			assertThrows(IllegalArgumentException.class, () -> {cronParser.parseCron("*/59 1-25 1-31 * 1-5 /***"); } );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testValidRangeDayOfMonth() {
		CronParser cronParser = new CronParser();
		try {
			assertThrows(IllegalArgumentException.class, () -> {cronParser.parseCron("*/59 0 1-32 * 1-5 /***"); } );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testValidRangeMonth() {
		CronParser cronParser = new CronParser();
		try {
			assertThrows(IllegalArgumentException.class, () -> {cronParser.parseCron("*/59 2 1-31 1-13 1-5 /***"); } );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testValidRangeDayOfWeek() {
		CronParser cronParser = new CronParser();
		try {
			assertThrows(IllegalArgumentException.class, () -> {cronParser.parseCron("*/59 2 1-31 * 1-8 /***"); } );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//-------- End Test Valid Range
	
	//----------Test Wildcard
	@Test
	public void testWildCardMinute() {
		CronParser cronParser = new CronParser();
		try {
			Map<String, String> map = cronParser.parseCron("* 0 1,15 * 1-5 /usr/bin/find");
			StringBuilder sb = new StringBuilder();
			int i = 0;
			while(i < 60) {
				sb.append(String.valueOf(i) + " ");
				i++;
			}
			assertEquals(map.get("minute"), sb.toString().trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testWildCardHour() {
		CronParser cronParser = new CronParser();
		try {
			Map<String, String> map = cronParser.parseCron("*/15 * 1,15 * 1-5 /usr/bin/find");
			StringBuilder sb = new StringBuilder();
			int i = 0;
			while(i < 24) {
				sb.append(String.valueOf(i) + " ");
				i++;
			}
			assertEquals(map.get("hour"), sb.toString().trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testWildCardDayOfMonth() {
		CronParser cronParser = new CronParser();
		try {
			Map<String, String> map = cronParser.parseCron("*/15 0 * * 1-5 /usr/bin/find");
			StringBuilder sb = new StringBuilder();
			int i = 1;
			while(i < 32) {
				sb.append(String.valueOf(i) + " ");
				i++;
			}
			assertEquals(map.get("day of month"), sb.toString().trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testWildCardMonth() {
		CronParser cronParser = new CronParser();
		try {
			Map<String, String> map = cronParser.parseCron("*/15 0 1,15 * 1-5 /usr/bin/find");
			StringBuilder sb = new StringBuilder();
			int i = 1;
			while(i < 13) {
				sb.append(String.valueOf(i) + " ");
				i++;
			}
			assertEquals(map.get("month"), sb.toString().trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testWildCardDayOfWeek() {
		CronParser cronParser = new CronParser();
		try {
			Map<String, String> map = cronParser.parseCron("*/15 0 1,15 * * /usr/bin/find");
			StringBuilder sb = new StringBuilder();
			int i = 1;
			while(i < 8) {
				sb.append(String.valueOf(i) + " ");
				i++;
			}
			assertEquals(map.get("day of week"), sb.toString().trim());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//---------End Test Wildcard
	
	@Test
	public void testLessThanFiveTimeFields() {
		CronParser cronParser = new CronParser();
		try {
			assertThrows(IllegalArgumentException.class, () -> {cronParser.parseCron("*/59 1-31 * 1-5 /***"); } );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testEmptyInput() {
		CronParser cronParser = new CronParser();
		try {
			assertThrows(IllegalArgumentException.class, () -> {cronParser.parseCron(""); } );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testNullInput() {
		CronParser cronParser = new CronParser();
		try {
			assertThrows(IllegalArgumentException.class, () -> {cronParser.parseCron(null); } );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInputNotNumeric() {
		CronParser cronParser = new CronParser();
		try {
			assertThrows(IllegalArgumentException.class, () -> {cronParser.parseCron("a b c d e /***"); } );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}

