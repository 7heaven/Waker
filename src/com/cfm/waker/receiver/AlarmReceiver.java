/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.receiver;

import java.util.Calendar;

import com.cfm.waker.dao.WakerDatabaseHelper;
import com.cfm.waker.entity.Alarm;
import com.cfm.waker.log.WLog;
import com.cfm.waker.ui.ShakeActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		final long alarmId = intent.getLongExtra("alarm_id", -1);
		final boolean isBeforeTime = intent.getBooleanExtra("before_time", false);
		int flag = intent.getIntExtra("flag", 0);
		Alarm alarm = WakerDatabaseHelper.getInstance(context).getAlarm(alarmId, DateFormat.is24HourFormat(context));
		if(null != alarm){
			if(alarm.isEnabled()){
				Intent myIntent = new Intent(context, AlarmReceiver.class);
				myIntent.putExtra("alarm_id", alarmId);
				myIntent.putExtra("before_time", false);
				myIntent.putExtra("flag", flag + 1);
				
				AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				PendingIntent pi = PendingIntent.getBroadcast(context, 0, myIntent, ++flag);
				alarmManager.set(AlarmManager.RTC_WAKEUP, getNextAlarmTime(alarm), pi);
				
				WLog.print("RECEIVER", isBeforeTime + ":" + getNextAlarmTime(alarm) + ":" + System.currentTimeMillis());
				if(!isBeforeTime){
					Toast.makeText(context, "alarm!!", Toast.LENGTH_LONG).show();
					Intent mIntent = new Intent(context, ShakeActivity.class);
					mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					
					context.startActivity(mIntent);
				}
			}
		}
		
	}
	
	//this function will generate the next trigger day of the alarm
	//it's not a very bright way to do that (>_<) still finding a better way
	private long getNextAlarmTime(Alarm alarm){
		Calendar calendar = Calendar.getInstance();
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		int count = alarm.getNextDaySet(dayOfWeek);
		
		if(count < dayOfWeek){
			count = 7 - (dayOfWeek - count);
		}else if(count > dayOfWeek){
			count = count - dayOfWeek;
		}else{
			count = 7;
		}

        return alarm.getCalendar().getTimeInMillis() + (86400000L * count);
	}

}
