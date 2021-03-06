/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.receiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

@Deprecated
public class AlarmReceiver extends BroadcastReceiver{
	
	private Calendar calendar;

	@Override
	public void onReceive(Context context, Intent intent) {
		calendar = Calendar.getInstance();
		final long alarmId = intent.getLongExtra("com.cfm.waker.alarm_id", -1);
		final boolean isBeforeTime = intent.getBooleanExtra("com.cfm.waker.before_current_time", false);
		int flag = intent.getIntExtra("com.cfm.waker.flag", 0);
		Alarm alarm = WakerDatabaseHelper.getInstance(context).getAlarm(alarmId, DateFormat.is24HourFormat(context));
		if(null != alarm){
			if(alarm.isEnabled() && alarm.isDaySet(calendar.get(Calendar.DAY_OF_WEEK))){
				Intent myIntent = new Intent(context, AlarmReceiver.class);
				myIntent.putExtra("com.cfm.waker.alarm_id", alarmId);
				myIntent.putExtra("com.cfm.waker.before_current_time", false);
				myIntent.putExtra("com.cfm.waker.flag", flag + 1);
				
				AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				PendingIntent pi = PendingIntent.getBroadcast(context, 0, myIntent, ++flag);
				alarmManager.set(AlarmManager.RTC_WAKEUP, getNextAlarmTime(alarm), pi);
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(getNextAlarmTime(alarm));
				
				WLog.print("RECEIVER", isBeforeTime + ":" + new SimpleDateFormat("yyyy-MM-dd HH:mm EEEE", Locale.CHINA).format(calendar.getTime()) + ":" + System.currentTimeMillis());
				if(!isBeforeTime){
					Toast.makeText(context, "alarm!!", Toast.LENGTH_LONG).show();
					Intent mIntent = new Intent(context, ShakeActivity.class);
					mIntent.putExtra("com.cfm.waker.alarm_id", alarmId);
					mIntent.putExtra("com.cfm.waker.snooze_count", 0);
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
