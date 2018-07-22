package com.mytutor.persistence;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateAndTimeUtils {
	
	private DateAndTimeUtils()
	{
		
	}
	
	private static SimpleDateFormat sdf = new SimpleDateFormat();
	private static final String  DATE_PATTERN_1 = "yyyy-MM-dd";
	private static final String DATE_TIME_PATTERN_1 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	static {
		String timeZoneID = System.getenv("TIMEZONE");

		if (timeZoneID == null)
			timeZoneID = "GMT";
		if (timeZoneID.equals("USA"))
			sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		else if (timeZoneID.equals("GMT"))
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	static String getTodaysDate() {
		sdf.applyPattern(DATE_PATTERN_1);
		return sdf.format(new Date());
	}

	public static String getTodaysDateAndTime() {
		sdf.applyPattern(DATE_TIME_PATTERN_1);
		return sdf.format(new Date());
	}
	
	

	
	
	

}
