package com.jeffreypomeroy.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateUtil {
	
		static final int INTERNAL = 1;
		static final int SORT = 2;
		static final int YEAR = 3;
		static final int MONTHNO = 4;
		static final int DAYNO = 5;
		static final int WEEKDAYNO = 6;
		static final int HOUROFDAY = 7;
		static final int MINUTE = 8;
		static final int SECOND = 9;
		static final int SECONDOFDAY = 10;
		static final int STANDARD = 11;
		static final int HOUR = 12;

		static int dmy=1;
		
	public static String getTodayDate(){
		String todayDate;
		todayDate = new SimpleDateFormat("MM/dd/yyyy",Locale.US).format(Calendar.getInstance().getTime());
		return todayDate;
	}
	public static String getTodayDate(int iType){
		String todayDate=null;
		switch (iType){
		case SORT:
			todayDate = new SimpleDateFormat("yyyyMMdd",Locale.US).format(Calendar.getInstance().getTime());
			break;
		case YEAR:
			todayDate = new SimpleDateFormat("yyyy",Locale.US).format(Calendar.getInstance().getTime());
			break;
		case MONTHNO:
			todayDate = new SimpleDateFormat("MM",Locale.US).format(Calendar.getInstance().getTime());
			break;
		case DAYNO:
			todayDate = new SimpleDateFormat("dd",Locale.US).format(Calendar.getInstance().getTime());
			break;
		case STANDARD:
			todayDate = new SimpleDateFormat("MM/dd/yyyy",Locale.US).format(Calendar.getInstance().getTime());	
		}
		return todayDate;
	}
	public static String getTodayTime(){
		int todayHour, todayMinutes;
		String todayHourStr, todayMinutesStr, todayTimeStr;
	    Calendar rightNow = Calendar.getInstance();
	    todayHour=rightNow.get(Calendar.HOUR_OF_DAY);
	    todayMinutes=rightNow.get(Calendar.MINUTE);
	    todayHourStr=Integer.toString(todayHour);
	    todayMinutesStr=Integer.toString(todayMinutes);
	    if (todayHourStr.length()==1){todayHourStr="0"+todayHourStr;}
	    if (todayMinutesStr.length()==1){todayMinutesStr="0"+todayMinutesStr;}
	    todayTimeStr=todayHourStr+":"+todayMinutesStr;
	    return todayTimeStr;
	}
	public static String getTodayTime(int iType){
		String theTime = "init";
		int theHour, theMinute, theSecond;
		Calendar rightNow = Calendar.getInstance();
		switch (iType){
		case HOUR:
			theTime=Integer.toString(rightNow.get(Calendar.HOUR_OF_DAY));
			break;
		case MINUTE:
			theTime=Integer.toString(rightNow.get(Calendar.MINUTE));
			break;
		case SECOND:
			theTime=Integer.toString(rightNow.get(Calendar.SECOND));
			break;
		case SECONDOFDAY:
			theHour=rightNow.get(Calendar.HOUR_OF_DAY);
			theMinute=rightNow.get(Calendar.MINUTE);
			theSecond=rightNow.get(Calendar.SECOND);
			int workInt = (theHour * 60 * 60) + (theMinute * 60) + theSecond;
			theTime=Integer.toString(workInt);
			theTime="000"+theTime;
			theTime=theTime.substring(theTime.length()-5, theTime.length());
			break;
		case STANDARD:
			theTime = new SimpleDateFormat("kk:mm",Locale.US).format(Calendar.getInstance().getTime());	
			default:
				theTime="error";
		}
		dmy=1;
		return theTime;
	}
}
