/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker;

import com.cfm.waker.dao.WakerPreferenceManager;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class WakerApplication extends Application{
	
	private AlarmManager mAlarmManager;
	
	private boolean isDatabaseChanged;
	
	@Override
	public void onCreate(){
		super.onCreate();
		mAlarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
		
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		WakerPreferenceManager.getInstance(getBaseContext()).setScreenDensity(dm.density);
		
		isDatabaseChanged = false;
	}

	public boolean is24(){
		return DateFormat.is24HourFormat(getBaseContext());
	}
	
	public boolean isDatabaseChanged(){
		return isDatabaseChanged;
	}
	
	public void setDatabaseChanged(boolean changed){
		isDatabaseChanged = changed;
	}
}
