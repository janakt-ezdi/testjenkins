package com.ezdi.cac.util;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.ezdi.component.logger.EzdiLogManager;
import com.ezdi.component.logger.EzdiLogger;

public final class DateUtils
{
	private static EzdiLogger LOGGER = EzdiLogManager.getLogger(DateUtils.class);

	private static final String UTC_DATE_TIME_ZONE_ID = "Etc/UTC";

	private DateUtils()
	{
	}

	public static Date convertHL7DateTimeStringToUTCDateTime(String sourceDateTimeString, String sourceDateTimeZoneId) throws Exception
	{
		LOGGER.debug("Inside convertHL7DateTimeStringToUTCDateTime.");
		Date utcDateTime = null;
		String sourceDateTimePattern = getHL7DateTimePattern(sourceDateTimeString);
		String targetDateTimeZoneId = UTC_DATE_TIME_ZONE_ID;
		DateTime targetDateTime = convertDateTime(sourceDateTimeString, sourceDateTimeZoneId, sourceDateTimePattern, targetDateTimeZoneId);
		if (targetDateTime != null)
		{
			utcDateTime = targetDateTime.toDate();
		}
		LOGGER.debug("Exiting from convertHL7DateTimeStringToUTCDateTime.");
		return utcDateTime;
	}

	public static DateTime convertDateTime(String sourceDateTimeString, String sourceDateTimeZoneId, String sourceDateTimePattern,
			String targetDateTimeZoneId)
	{
		LOGGER.debug("Inside convertDateTime.");
		DateTime targetDateTime = null;
		if (sourceDateTimeZoneId != null && !sourceDateTimeZoneId.isEmpty() && sourceDateTimePattern != null && !sourceDateTimePattern.isEmpty())
		{
			DateTimeZone sourceDateTimeZone = DateTimeZone.forID(sourceDateTimeZoneId);
			DateTimeFormatter sourceDateTimeFormatter = DateTimeFormat.forPattern(sourceDateTimePattern);
			DateTime sourceDateTime = getDateTime(sourceDateTimeString, sourceDateTimeZone, sourceDateTimeFormatter);
			DateTimeZone targetDateTimeZone = null;
			if (sourceDateTime != null && targetDateTimeZoneId != null && !targetDateTimeZoneId.isEmpty())
			{
				targetDateTimeZone = DateTimeZone.forID(targetDateTimeZoneId);
				if (targetDateTimeZone != null)
				{
					targetDateTime = sourceDateTime.toDateTime(targetDateTimeZone);
				}
			}
		}
		LOGGER.debug("Exiting from convertDateTime.");
		return targetDateTime;
	}

	public static String getHL7DateTimePattern(String dateTimeString) throws Exception
	{
		LOGGER.debug("Inside getHL7DateTimePattern.");
		String dateTimePattern = null;
		if (dateTimeString != null && !dateTimeString.isEmpty())
		{
			if (dateTimeString.length() == 8)
			{
				dateTimePattern = "yyyyMMdd";
			} else if (dateTimeString.length() == 10)
			{
				dateTimePattern = "yyyyMMddHH";
			} else if (dateTimeString.length() == 12)
			{
				dateTimePattern = "yyyyMMddHHmm";
			} else if (dateTimeString.length() == 14)
			{
				dateTimePattern = "yyyyMMddHHmmss";
			} else if (dateTimeString.length() == 16)
			{
				dateTimePattern = "yyyyMMddHHmmssSS";
			} else
			{
				throw new Exception("Bed date time string");
			}
		}
		LOGGER.debug("Exiting from getHL7DateTimePattern.");
		return dateTimePattern;
	}

	public static DateTime getDateTime(String dateTimeString, DateTimeZone dateTimeZone, DateTimeFormatter dateTimeFormatter)
	{
		LOGGER.debug("Inside getDateTime.");
		DateTime dateTime = null;
		if (dateTimeFormatter != null && dateTimeZone != null)
		{
			DateTimeFormatter dateTimeFormatterWithTimeZone = dateTimeFormatter.withZone(dateTimeZone);
			if (dateTimeFormatterWithTimeZone != null && dateTimeString != null && !dateTimeString.isEmpty())
			{
				dateTime = dateTimeFormatterWithTimeZone.parseDateTime(dateTimeString);
			}
		}
		LOGGER.debug("Exiting from getDateTime.");
		return dateTime;
	}

	public static Date getUTCDateTime()
	{
		LOGGER.debug("Inside getUTCDateTime.");
		DateTime dateTime = DateTime.now(DateTimeZone.UTC);
		Date utcDateTime = dateTime.toDate();
		LOGGER.debug("Exiting from getUTCDateTime.");
		return utcDateTime;
	}
}
