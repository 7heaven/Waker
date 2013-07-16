/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.dao;

import android.content.Context;
import android.content.SharedPreferences;

public class WakerPreferenceManager {

	private Context context;
	private static WakerPreferenceManager instance;
	
	private static final String PREFERENCE_NAME = "waker_preference";
	
	private static final String FIRST_TIME_BOOTUP = "first_time_bootup";
	
	//screen measurement
	private static final String SCREEN_DENSITY = "screen_density";
	private static final String SCREEN_WIDTH = "screen_width";
	private static final String SCREEN_HEIGHT = "screen_height";
	private static final String STATUSBAR_HEIGHT = "statusbar_height";
	
	//settings
	private static final String GLOBAL_SHAKE_LEVEL = "global_shakelevel";
	private static final String GLOBAL_SNOOZETIME = "global_snoozetime";
	private static final String GLOBAL_RINGTONE = "global_ringtone";
	private static final String GLOBAL_ALARM_VOLUME = "global_alarm_volume";
	
	private SharedPreferences wakerPreferences;
	
	public WakerPreferenceManager(Context context){
		this.context = context;
	}
	
	public static WakerPreferenceManager getInstance(Context context){
		if(null == instance){
			instance = new WakerPreferenceManager(context);
		}
		
		return instance;
	}
	
	private SharedPreferences getPreference(){
		if(null == wakerPreferences){
			wakerPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		}
		
		return wakerPreferences;
	}
	
	public void setIsFirstTimeBoot(int firstTime){
		getPreference().edit().putInt(FIRST_TIME_BOOTUP, firstTime);
	}
	
	public int isFirstTimeBoot(){
		return getPreference().getInt(FIRST_TIME_BOOTUP, -1);
	}
	
	public void setScreenDensity(float density){
		getPreference().edit().putFloat(SCREEN_DENSITY, density).commit();
	}
	
	public float getScreenDensity(){
		return getPreference().getFloat(SCREEN_DENSITY, 1F);
	}
	
	public void setScreenResolution(int width, int height){
		SharedPreferences.Editor editor = getPreference().edit();
		editor.putInt(SCREEN_WIDTH, width);
		editor.putInt(SCREEN_HEIGHT, height);
		
		editor.commit();
	}
	
	public int getScreenWidth(){
		return getPreference().getInt(SCREEN_WIDTH, 480);
	}
	
	public int getScreenHeight(){
		return getPreference().getInt(SCREEN_HEIGHT, 800);
	}
	
	public void setStatusBarHeight(int height){
		getPreference().edit().putInt(STATUSBAR_HEIGHT, height).commit();
	}
	
	public int getStatusBarHeight(){
		return getPreference().getInt(STATUSBAR_HEIGHT, 0);
	}
	
	public void setGlobalShakeLevel(int level){
		getPreference().edit().putInt(GLOBAL_SHAKE_LEVEL, level).commit();
	}
	
	public int getGlobalShakeLevel(){
		return getPreference().getInt(GLOBAL_SHAKE_LEVEL, 0);
	}
	
	public void setGlobalSnoozeTime(int snoozeTime){
	    getPreference().edit().putLong(GLOBAL_SNOOZETIME, snoozeTime).commit();
	}
	
	public long getGlobalSnoozeTime(){
		return getPreference().getLong(GLOBAL_SNOOZETIME, 0L);
	}
	
	public void setGlobalRingtone(String ringtone){
		getPreference().edit().putString(GLOBAL_RINGTONE, ringtone).commit();
	}
	
	public String getGlobalRingtone(){
		return getPreference().getString(GLOBAL_RINGTONE, "");
	}
	
	public void setGlobalAlarmVolume(float volume){
		getPreference().edit().putFloat(GLOBAL_ALARM_VOLUME, volume).commit();
	}
	
	public float getGlobalAlarmVolume(){
		return getPreference().getFloat(GLOBAL_ALARM_VOLUME, 1F);
	}
}
