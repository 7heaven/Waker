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
	
	private static final String SCREEN_DENSITY = "screen_density";
	private static final String SCREEN_WIDTH = "screen_width";
	private static final String SCREEN_HEIGHT = "screen_height";
	
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
	
}
