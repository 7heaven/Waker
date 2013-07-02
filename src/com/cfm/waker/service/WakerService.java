/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.service;

import java.util.List;

import com.cfm.waker.WakerApplication;
import com.cfm.waker.dao.WakerDatabaseHelper;
import com.cfm.waker.entity.Alarm;
import com.cfm.waker.log.WLog;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WakerService extends Service {
	
	@Override
	public void onCreate(){
		super.onCreate();
		WLog.print("SERVICE", "service started");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		int returnValue = super.onStartCommand(intent, flags, startId);
		long currentTime = System.currentTimeMillis();
		
		List<Alarm> alarms = WakerDatabaseHelper.getInstance(this.getBaseContext()).getAlarms(((WakerApplication) getApplication()).is24());
		
		
		return returnValue;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
