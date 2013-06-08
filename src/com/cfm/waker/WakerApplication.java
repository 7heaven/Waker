package com.cfm.waker;

import android.app.AlarmManager;
import android.app.Application;
import android.content.Context;
import android.text.format.DateFormat;

public class WakerApplication extends Application{
	
	AlarmManager mAlarmManager;
	
	private boolean is24Format;
	
	@Override
	public void onCreate(){
		super.onCreate();
		mAlarmManager = (AlarmManager) getBaseContext().getSystemService(Context.ALARM_SERVICE);
		
		is24Format = DateFormat.is24HourFormat(getBaseContext());
	}

	public boolean is24(){
		return is24Format;
	}
	
}
