/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker;

import java.lang.reflect.Field;

import com.cfm.waker.dao.WakerPreferenceManager;
import com.cfm.waker.log.WLog;

import android.app.Application;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class WakerApplication extends Application{
	
	private WakerPreferenceManager preferenceManager;
	
	private boolean isDatabaseChanged;
	
	@Override
	public void onCreate(){
		super.onCreate();
		
		DisplayMetrics dm = new DisplayMetrics();
		((WindowManager) getBaseContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
		preferenceManager = WakerPreferenceManager.getInstance(getBaseContext());
		preferenceManager.setScreenDensity(dm.density);
		preferenceManager.setScreenResolution(dm.widthPixels, dm.heightPixels);
		
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		preferenceManager.setStatusBarHeight(sbar);
		
		if(preferenceManager.isFirstTimeBoot() == -1){
			preferenceManager.setIsFirstTimeBoot(1);
		}else if(preferenceManager.isFirstTimeBoot() == 1){
			preferenceManager.setIsFirstTimeBoot(0);
		}
		
		isDatabaseChanged = false;
	}

	public boolean is24(){
		return DateFormat.is24HourFormat(getBaseContext());
	}
	
	public boolean isDatabaseChanged(){
		return isDatabaseChanged;
	}
	
	public void setDatabaseChanged(boolean isDatabaseChanged){
		this.isDatabaseChanged = isDatabaseChanged;
	}
	
	public void notifyDatabaseChanged(){
		
	}
	
}
