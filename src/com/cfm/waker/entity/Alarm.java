/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.cfm.waker.R;

public class Alarm implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8799155296144926576L;

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
	
	private final Integer[] weekTransform = {0, 0x40, 0x20, 0x10, 0x8, 0x4, 0x2, 0x1};
	
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
	
	public Alarm(long TimeInMillis, boolean is24Format){
		this.is24Format = is24Format;
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(TimeInMillis);
		
		snoozeTime = 3000;
		enabled = true;
		vibrate = true;
		
		ringtone = "weather";
		
		week = 1;
		message = "wake up! you baster!";
	}
	
	public long getId(){
		return calendar.getTimeInMillis();
	}
	
	public void setId(long milliseconds){
		calendar.setTimeInMillis(milliseconds);
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
	
	public boolean isDaySet(int day){
		return (week & weekTransform[day]) > 0;
	}
	
	public int getNextDaySet(int day){
		int i = weekTransform[day] >> 1;
		do{
			if(isDaySet(day)) return getIndexOfWeek(i);
			
			if(i == 0x1){
				i = 0x40;
			}else{
				i >>= 1;
			}
			
		}while(i != day);
		
		return day;
	}
	
	private int getIndexOfWeek(int day){
		int i = 1;
		do{
			if(day == weekTransform[i]) return i;
		}while(++i < weekTransform.length);
		
		return 0;
	}
}
