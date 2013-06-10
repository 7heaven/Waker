/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.entity;

public class Week {

	
    public static final int    MONDAY = 0x01;
    public static final int   TUESDAY = 0x02;
    public static final int WEDNESDAY = 0x04;
    public static final int  THURSDAY = 0x08;
    public static final int    FRIDAY = 0x10;
    public static final int  SATURDAY = 0x20;
    public static final int    SUNDAY = 0x40;
	
	
	private int days;
	
	public Week(int days){
		this.days = days;
	}

	public void setWeek(int days){
		this.days = days;
	}
	
	public int getWeek(){
		return days;
	}
	
	@Override
	public String toString() {
		return "Week [days=" + days + "]";
	}
	
	public boolean[] getDaysOfWeek(){
		return new boolean[]{isDaySet(MONDAY),
				             isDaySet(TUESDAY),
				             isDaySet(WEDNESDAY),
				             isDaySet(THURSDAY),
				             isDaySet(FRIDAY),
				             isDaySet(SATURDAY),
				             isDaySet(SUNDAY)};
	}
	
	public boolean isDaySet(int day){
		return (days & day) > 0;
	}
}