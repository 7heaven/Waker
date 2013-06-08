package com.cfm.waker.entity;

public class TimeEntity {
	
	public static final int FORMAT_H_M_S = 0;
	public static final int FORMAT_H_M = 1;
	
	private int format;
	
	private int hour;
	private int minute;
	private int second;
	
	public int time;
	
	public TimeEntity(int format, int hour, int minute, int second){
		this.format = format;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}
	
	public TimeEntity(int hour, int minute, int second){
		format = FORMAT_H_M;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
	}
	
	public int getHour(){
		return hour;
	}

    public int getMinute(){
    	return minute;
    }
    
    public int getSecond(){
    	return second;
    }
	
	public void increaseHour(int increment){
		increment = (increment > 24 || increment < -24) ? increment % 24 : increment;
		if(hour + increment >= 24){
			hour = hour + increment - 24;
		}else if(hour + increment < 0){
			hour = 24 + (hour + increment);
		}else{
			hour += increment;
		}
	}
	
	public void increaseMinute(int increment){
		int multiply = 1;
		if(increment > 60 || increment < -60){
			multiply = (int) Math.floor((increment + minute) / 60);
			increment %= 60;
		}
		if(minute + increment >= 60){
			minute = minute + increment - 60;
			increaseHour(multiply);
		}else if(minute + increment < 0){
			minute = 60 + (minute + increment);
			increaseHour(-multiply);
		}else{
			minute += increment;
		}
	}
	
	public void increaseSecond(int increment){
		int multiply = 1;
		if(increment > 60 || increment < -60){
		    multiply = (int) Math.floor((increment + second) / 60);
			increment %= 60;
		}
		if(second + increment >= 60){
			second = second + increment - 60;
			increaseMinute(multiply);
		}else if(second + increment < 0){
			second = 60 + (second + increment);
			increaseMinute(-multiply);
		}else{
			second += increment;
		}
	}
	
	public String getFormatedTime(){
		String h;
		String m;
		String s;
		
		switch(format){
		case FORMAT_H_M_S:
			h = To2Digit(hour);
			m = To2Digit(minute);
			s = To2Digit(second);
			
			return timeFormat(h, m, s);
		case FORMAT_H_M:
			h = To2Digit(hour);
			m = To2Digit(minute);
			
			return timeFormat(h, m);
		}
		return null;
	}
	
	private String To2Digit(int time){
		return time < 10 ? "0" + time : "" + time;
	}
	
	private String timeFormat(String... time){
		int i = 0;
		String content = "";
		do{
			if(i < time.length - 1){
				content = content + time[i] + ":";
			}else{
				content = content + time[i];
			}
		}while(++i < time.length);
		
		return content;
	}
}
