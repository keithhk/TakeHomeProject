import java.util.LinkedHashMap;
import java.util.Map;

public class CronParser {

	//How to parse?
	//split string by space

	//How to store the output?

	//how to output the result?
	//how to handle different category minute, hour, day, month, dayOfWeek

	public static final int[][] validRanges = {{0, 59}, {0, 23}, {1, 31}, {1, 12}, {1,7}};
	public static final String[] categoryString = {"minute", "hour", "day of month", "month", "day of week", "command"};
	
	public String parseWildcard(String input, int category) {
		StringBuilder sb = new StringBuilder();
		int start = validRanges[category][0], end = validRanges[category][1];
		int divisor = 0;
		boolean containsDivisor = false;

		if(input.contains("/")) {
			containsDivisor = true;
			String[] tokens = input.split("/");
			if(checkValidRange(Integer.valueOf(tokens[1]), category)) {
				divisor = Integer.valueOf(tokens[1]);
			}
		}

		while(start <= end) {
			if(!containsDivisor) {
				sb.append(start + " ");
			}else if(start % divisor == 0){
				sb.append(start + " ");
			}
			start++;
		}
		return sb.toString();
	}

	public Map<String, String> parseCron(String input) throws Exception{

		if(input == null || input.trim().equals("")) {
			throw new IllegalArgumentException("input string cannot be empty or null!");
		}

		String[] tokens = input.split("\\s+");
		if(tokens.length != 6) {
			throw new IllegalArgumentException("input string format invalid. valid cron format:[minute] [hour] [dayOfMonth] [month] [week] [command]");
		}

		Map<String, String> result = new LinkedHashMap<>();

		for(int i=0; i<5; i++) {
			if(tokens[i].contains("*")) {
				result.put(categoryString[i], parseWildcard(tokens[i], i).trim());
			
			}else if(tokens[i].contains(",")) {
				String[] times = tokens[i].split(",");

				StringBuilder sb = new StringBuilder();
				for(String time : times) {
					if(checkValidRange(Integer.valueOf(time), i)) {
						sb.append(time + " ");
					}
				}
				result.put(categoryString[i], sb.toString().trim());

			}else if(tokens[i].contains("-")){
				String[] range = tokens[i].split("-");
				int start = Integer.valueOf(range[0]);
				int end = Integer.valueOf(range[1]);

				if(checkValidRange(start, i) && (checkValidRange(end, i))) {
					StringBuilder sb = new StringBuilder();
					while(start <= end) {
						sb.append(start + " ");
						start++;
					}
					result.put(categoryString[i], sb.toString().trim());
				}
			}else if(checkValidRange(Integer.parseInt(tokens[i]), i)){
				result.put(categoryString[i], String.valueOf(Integer.parseInt(tokens[i])).trim()); //Ensure token is numeric
			}
		}
		
		result.put(categoryString[5], tokens[5].trim());

		return result;
	}

	public boolean checkValidRange(int num, int category) {
		if(num >= validRanges[category][0] && num <= validRanges[category][1])
			return true;
		else
			throw new IllegalArgumentException(num + " does not falls within the valid " + categoryString[category] + " range of " + validRanges[category][0] + "-" + validRanges[category][1]);
	}

	public static void main(String[] args0) throws Exception{
		CronParser parser= new CronParser();

		Map<String, String> map = parser.parseCron(args0[0]);

		for(Map.Entry<String, String> entry: map.entrySet()) {
			System.out.printf("%-15s%s%n", entry.getKey(), entry.getValue());
		}
	}

}
