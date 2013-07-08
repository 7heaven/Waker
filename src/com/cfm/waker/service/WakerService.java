/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.cfm.waker.WakerApplication;
import com.cfm.waker.dao.WakerDatabaseHelper;
import com.cfm.waker.entity.Alarm;
import com.cfm.waker.log.WLog;
import com.cfm.waker.receiver.SetNextDayReceiver;
import com.cfm.waker.ui.ShakeActivity;
import com.cfm.waker.util.CursorableList;
import com.cfm.waker.util.Constants;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class WakerService extends Service {
	
	private Context context;
	
	private int alarmId;
	
	private AlarmManager alarmManager;
	
	private LocalBinder localBinder;
	
	public class LocalBinder extends Binder{
		public WakerService getService(){
			return WakerService.this;
		}
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		WLog.print("SERVICE", "service started");
		context = getBaseContext();
		
		alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		localBinder = new LocalBinder();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		WLog.print("SERVICE", "onStartCommand");
		
        alarmId = 0;
		
		Intent nextDaySetIntent = new Intent(context, SetNextDayReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, nextDaySetIntent, alarmId);
		alarmManager.set(AlarmManager.RTC_WAKEUP, getNextZero(), pendingIntent);
		WLog.print("SERVICE", new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA).format(new Date(getNextZero())));
		
		setAlarms();
		
		flags = START_STICKY;
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void setAlarms(){
		if(WakerDatabaseHelper.getInstance(context).getDBCount() > 0){
			CursorableList<Alarm> alarms = new CursorableList<Alarm>();
			alarms.addAll(WakerDatabaseHelper.getInstance(context).getAlarms(((WakerApplication) getApplication()).is24()));
			if(alarms.moveToFirst()){
				do{
					setAlarm(alarms.get());
				}while(alarms.moveToNext());
			}
		}
	}
	
	public void setAlarm(Alarm alarm){
		alarmId++;
		
		long currentTime = System.currentTimeMillis();
		long time = alarm.getCalendar().getTimeInMillis();
		if(currentTime < time && alarm.isEnabled() && isDaySet(alarm)){
			Intent alarmIntent = new Intent(this, ShakeActivity.class);
			alarmIntent.putExtra(Constants.ALARM_ID, alarm.getId());
			alarmIntent.putExtra(Constants.ALARM_SNOOZE_COUNT, 0);
			alarmIntent.putExtra(Constants.ALARM_FLAG_COUNT, alarmId);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, alarmIntent, alarmId);
			alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
		}
	}
	
	private boolean isDaySet(Alarm alarm){
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		
		return alarm.isDaySet(dayOfWeek);
	}

	private long getNextZero(){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		
		calendar.setTimeInMillis(calendar.getTimeInMillis() + 86400000L);
		
		return calendar.getTimeInMillis();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return localBinder;
	}

	@Override
	public void onDestroy(){
		super.onDestroy();
		Intent intent = new Intent(this, WakerService.class);
		context.startService(intent);
	}

}
