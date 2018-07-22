package com.mytutor.persistence;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateAndTimeUtils {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat();
	public static String DATE_PATTERN_1 = "yyyy-MM-dd";
	public static String DATE_TIME_PATTERN_1 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

	static {
		String timeZoneID = System.getenv("TIMEZONE");

		if (timeZoneID == null)
			timeZoneID = "GMT";
		if (timeZoneID.equals("USA"))
			sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		else if (timeZoneID.equals("GMT"))
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public static String getTodaysDate() {
		sdf.applyPattern(DATE_PATTERN_1);
		return sdf.format(new Date());
	}

	public static String getTodaysDateAndTime() {
		sdf.applyPattern(DATE_TIME_PATTERN_1);
		return sdf.format(new Date());
	}
	
	public static String getYesterdaysDate() {
		sdf.applyPattern(DATE_PATTERN_1);
		return sdf.format(getYesterday());
	}

	public static String getYesterdaysDateAndTime() {
		sdf.applyPattern(DATE_TIME_PATTERN_1);
		return sdf.format(getYesterday());
	}
	
	private static Date getYesterday()
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1); 
		Date yesterday = cal.getTime();
		return yesterday;
	}
	
	public static String getSingularOrPluralWeek(int weeks)
	{
		String weekString = null;
		if(weeks>0)
		{
			weekString = (weeks>1)?"weeks":"week";
		}
		return weekString;
	}
	
	public static String getSingularOrPluralDay(int days)
	{
		String dayString = null;
		if(days>0)
		{
			dayString = (days>1)?"days":"day";
		}
		return dayString;
	}

}
