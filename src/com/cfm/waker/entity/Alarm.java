package com.cfm.waker.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.cfm.waker.R;

public class Alarm {
	
	public static class Columns{
        public static final String             ID = "id";
        public static final String           HOUR = "hour";
        public static final String         MINUTE = "minute";
        public static final String    SNOOZE_TIME = "snooze_time";
        public static final String        ENABLED = "enabled";
        public static final String        VIBRATE = "vibrate";
        public static final String       RINGTONE = "ringtone";
        public static final String   DAYS_OF_WEEK = "days_of_week";
        public static final String        MESSAGE = "message";
	}
	
	private Calendar calendar;
	private boolean is24Format;
	
	private int snoozeTime;
	
	private boolean enabled;
	private boolean vibrate;
	
	private String ringtone;
	
	private int week;
	private String message;
	
	public Alarm(Calendar calendar, boolean is24Format){
		this.is24Format = is24Format;
		this.calendar = calendar;
		
		snoozeTime = 300000;
		enabled = true;
		vibrate = true;
		
		ringtone = "weather";
		
		week = 1;
		message = "wake up! you baster!";
	}
	
	public int getHour(){
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	public void setHour(int hour){
		calendar.set(Calendar.HOUR_OF_DAY, hour);
	}
	
	public int getMinute(){
		return calendar.get(Calendar.MINUTE);
	}
	
	public void setMinute(int minute){
		calendar.set(Calendar.MINUTE, minute);
	}
	
	public int getSnoozeTime() {
		return snoozeTime;
	}
	public void setSnoozeTime(int snoozeTime) {
		this.snoozeTime = snoozeTime;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isVibrate() {
		return vibrate;
	}
	public void setVibrate(boolean vibrate) {
		this.vibrate = vibrate;
	}
	public String getRingtone() {
		return ringtone;
	}
	public void setRingtone(String ringtone) {
		this.ringtone = ringtone;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "Alarm [snoozeTime=" + snoozeTime + ", enabled=" 
	            + enabled + ", vibrate=" + vibrate
				+ ", ringtone=" + ringtone + ", week=" + week + ", message="
				+ message + "]";
	}
	
	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	public boolean is24Format() {
		return is24Format;
	}

	public void setIs24Format(boolean is24Format) {
		this.is24Format = is24Format;
	}
	
	public int getAmpmRes(){
		if(is24Format) throw new IllegalStateException("can not use the function when it's 24 Format.");
		
		return calendar.get(Calendar.AM_PM) == Calendar.AM ? R.string.am : R.string.pm;
	}

	public String getFormatedTime(){
		SimpleDateFormat dateFormat = new SimpleDateFormat("", Locale.CHINA);
		if(is24Format){
			dateFormat.applyPattern("HH:mm");
		}else{
			dateFormat.applyPattern("hh:mm");
		}
		
		return dateFormat.format(calendar.getTime());
	}
	
}
