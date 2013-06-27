/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.service;

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
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
