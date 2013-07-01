/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.cfm.waker.adapter.AlarmListAdapter;
import com.cfm.waker.dao.WakerDatabaseHelper;
import com.cfm.waker.entity.Alarm;
import com.cfm.waker.log.WLog;
import com.cfm.waker.receiver.AlarmReceiver;
import com.cfm.waker.service.WakerService;
import com.cfm.waker.ui.SettingActivity;
import com.cfm.waker.ui.base.BaseSlidableActivity;
import com.cfm.waker.view.WakerViewPager;
import com.cfm.waker.widget.DebossFontText;
import com.cfm.waker.widget.DialTimePicker;
import com.cfm.waker.widget.DialTimePicker.OnTimePickListener;
import com.cfm.waker.widget.WeekSelector;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class MainActivity extends BaseSlidableActivity implements OnTimePickListener{
	
	private DebossFontText timeText;
	private DebossFontText amPm;
	private DialTimePicker dialTimePicker;
	private WeekSelector weekSelector;
	private RelativeLayout dialLayout;
	
	private TimeRunnable tRunnable;
	
	private ViewMoveRunnable vmRunnable;
	private ViewMoveRunnable weekRunnable;
	private ViewMoveRunnable mContentRunnable;
	private Handler handler;
	
	private Calendar calendar;
	private SimpleDateFormat dateFormat;
	
	private boolean pickingTime;
	
	private FrameLayout viewPagerLayout;
	private WakerViewPager viewPager;
	private ArrayList<Alarm> alarmList;
	private AlarmListAdapter alarmListAdapter;
	
	private int alarmCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dialTimePicker = (DialTimePicker) findViewById(R.id.time_pick);
		dialTimePicker.setOnTimePickListener(this);
		dialLayout = (RelativeLayout) findViewById(R.id.dial_layout);
		
		timeText = (DebossFontText) findViewById(R.id.time);
		amPm = (DebossFontText) findViewById(R.id.am_pm);
		
		weekSelector = (WeekSelector) findViewById(R.id.selector);
		
		tRunnable = new TimeRunnable();
		handler = new Handler();
		handler.post(tRunnable);
		
		if(mApplication.is24()){
			dateFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
			amPm.setVisibility(View.GONE);
		}else{
			dateFormat = new SimpleDateFormat("hh:mm", Locale.CHINA);
			amPm.setVisibility(View.VISIBLE);
		}
		
		
		pickingTime = false;
		
		viewPagerLayout = (FrameLayout) findViewById(R.id.viewpager_layout);
		
		viewPager = (WakerViewPager) findViewById(R.id.alarm_list);
		alarmList = new ArrayList<Alarm>();
		alarmListAdapter = new AlarmListAdapter(this, alarmList);
		viewPager.setAdapter(alarmListAdapter);
		
		alarmCount = 0;
		
		mOnSlideListener = new OnSlideListener(){
			int vy,dy;
			
			@Override
			public void onHorizontallySlidePressed(){}
			
			@Override
			public void onHorizontallySlide(int distance){}
			
			@Override
			public void onHorizontallySlideReleased(boolean isActionPerformed){}
			
			@Override
			public void onVerticallySlidePressed(){
				viewPagerLayout.setVisibility(View.VISIBLE);
				vy = viewPagerLayout.getScrollY();
				dy = dialLayout.getScrollY();
				handler.removeCallbacks(vmRunnable);
				handler.removeCallbacks(weekRunnable);
				handler.removeCallbacks(mContentRunnable);
			}
			
			@Override
			public void onVerticallySlide(int distance){
				if(dialTimePicker.getMode() != DialTimePicker.MODE_CONFIRM){
					int disV = vy - distance;
					int disD = dy - distance;
					
					if(disD <= -viewPagerLayout.getMeasuredHeight() / 2 ){
						disV = 0;
						disD = -viewPagerLayout.getMeasuredHeight() / 2;
					}
					
					if(disD >= 0){
						disV = viewPagerLayout.getMeasuredHeight() / 2;
						disD = 0;
					}
					
					viewPagerLayout.scrollTo(0, disV);
					dialLayout.scrollTo(0, disD);
				}
			}
			
			@Override
			public void onVerticallySlideReleased(){
				WLog.print(TAG, viewPagerLayout.getMeasuredHeight() + "");
				int yPosition = dialLayout.getScrollY();
				if(yPosition < -viewPagerLayout.getMeasuredHeight() / 4){
					contentMovement(0);
				}
				if(yPosition >= -viewPagerLayout.getMeasuredHeight() / 4 && yPosition < weekSelector.getMeasuredHeight() / 2){
					contentMovement(1);
				}
				
				getContentView().requestLayout();
			}
		};
		
		updateAlarmsByDatabase();
		
		contentMovement(1);
		
		Intent serviceIntent = new Intent(this, WakerService.class);
		startService(serviceIntent);
	}
	
	//time tick movement
	private class TimeRunnable implements Runnable{
		@Override
		public void run(){
		   
			if(!pickingTime){
				calendar = Calendar.getInstance();
				
				dialTimePicker.performDial(6 * calendar.get(Calendar.SECOND));
				timeText.setText(dateFormat.format(calendar.getTime()));
				if(!mApplication.is24()) amPm.setText(new SimpleDateFormat("a", Locale.CHINA).format(calendar.getTime()));
				
				handler.postDelayed(tRunnable, 500);
				
			}
			
		}
	}
	
	private class ViewMoveRunnable implements Runnable{
		private View view;
		private int destinationX, destinationY;
		private float moveX, moveY;
		private boolean hide;

		public ViewMoveRunnable(View view, int destinationX, int destinationY, boolean hide){
			this.view = view;
			moveX = view.getScrollX();
			moveY = view.getScrollY();
			this.destinationX = destinationX;
			this.destinationY = destinationY;
			this.hide = hide;
		}
		@Override
		public void run() {
			if((int) moveX != destinationX || (int) moveY != destinationY){
				if(view.getVisibility() == View.INVISIBLE) view.setVisibility(View.VISIBLE);
				moveX += (destinationX - moveX) * 0.51F;
				moveY += (destinationY - moveY) * 0.51F;
				view.scrollTo((int) moveX, (int) moveY);
				
				WLog.print(TAG, "runnable run");
				
				handler.postDelayed(this, 20);
			}else{
				if(hide){
					WLog.print(TAG, "runnable gone");
					view.setVisibility(View.INVISIBLE);
				}
			}
			
		}
		
	}
	
	private void contentMovement(int position){
		switch(position){
		case 0:
			vmRunnable = new ViewMoveRunnable(viewPagerLayout, 0, 0, false);
			weekRunnable = new ViewMoveRunnable(weekSelector, 0, -weekSelector.getMeasuredHeight(), true);
			mContentRunnable = new ViewMoveRunnable(dialLayout, 0, -viewPagerLayout.getMeasuredHeight() / 2, false);
			break;
		case 1:
			vmRunnable = new ViewMoveRunnable(viewPagerLayout, 0, viewPagerLayout.getMeasuredHeight() / 2, true);
			weekRunnable = new ViewMoveRunnable(weekSelector, 0, -weekSelector.getMeasuredHeight(), true);
			mContentRunnable = new ViewMoveRunnable(dialLayout, 0, 0, false);
			break;
		case 2:
			vmRunnable = new ViewMoveRunnable(viewPagerLayout, 0, viewPagerLayout.getMeasuredHeight() / 2, true);
			weekRunnable = new ViewMoveRunnable(weekSelector, 0, 0, false);
			mContentRunnable = new ViewMoveRunnable(dialLayout, 0, weekSelector.getMeasuredHeight(), false);
			break;
		}
		
		handler.post(vmRunnable);
		handler.post(weekRunnable);
		handler.post(mContentRunnable);
	}
	
	private Alarm addAlarm(Calendar calendar){
		Alarm alarm = new Alarm(calendar, mApplication.is24());
		alarm.setWeek(weekSelector.getWeekSet());
		
		WLog.print(TAG, "weekSelector" + Integer.toBinaryString(weekSelector.getWeekSet()) + "B");
		
		WakerDatabaseHelper.getInstance(this).insertAlarm(alarm);
		
		updateAlarmsByDatabase();
		
		return alarm;
	}
	
	private void updateAlarmsByDatabase(){
		List<Alarm> tmp_list = WakerDatabaseHelper.getInstance(this).getAlarms(mApplication.is24());

		
		addAlarmsIntoRow(tmp_list);
	}
	
	private void addAlarmsIntoRow(List<Alarm>  alarms){
		alarmList.clear();
		if(null != alarms){
			alarmList.addAll(alarms);
		}
		alarmListAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onStartPick(){
		pickingTime = true;
		calendar = Calendar.getInstance();
		timeText.setText(dateFormat.format(calendar.getTime()));
	}
	
	@Override
	public void onPick(int value, int increment){
		WLog.print("Activity", increment + "");
		calendar.setTimeInMillis(calendar.getTimeInMillis() + (increment * 10000));
		timeText.setText(dateFormat.format(calendar.getTime()));
		if(!mApplication.is24()) amPm.setText(new SimpleDateFormat("a", Locale.CHINA).format(calendar.getTime()));
		
	}
	
	@Override
	public void onStopPick(){
		dialTimePicker.setMode(DialTimePicker.MODE_CONFIRM);
		
		contentMovement(2);
	}
	
	
	@Override
	public void onCenterClick(){
        pickingTime = false;
		
		//to setup a alarm and when the alarm being triggered start a AlarmReceiver anyway
		//it's AlarmReceiver's job to decide whether start a new ShakeActivity to perform a alarm or not.
		Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
		
		calendar.set(Calendar.SECOND, 0);
		
		final boolean beforeTime = calendar.getTimeInMillis() <= System.currentTimeMillis();
		
		WLog.print(TAG, "beforeTime:" + beforeTime);
		
		
		
		intent.putExtra("com.cfm.waker.alarm_id", addAlarm(calendar).getId());
		//put a boolean into intent to tell AlarmReceiver to stop this alarm and set a new alarm for the next trigger day 
		//in case the trigger time of this alarm is before the currentTime
		intent.putExtra("com.cfm.waker.before_current_time", beforeTime);
		intent.putExtra("com.cfm.wakier.flag", alarmCount + 1);
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, ++alarmCount);
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
		
		contentMovement(1);
		dialTimePicker.setMode(DialTimePicker.MODE_PICK);
		
		handler.post(tRunnable);
	}

	@Override
	protected Class<? extends Activity> getLeftActivityClass() {
		return this.getClass();
	}

	@Override
	protected Class<? extends Activity> getRightActivityClass() {
		return SettingActivity.class;
	}
	
	@Override
	protected Drawable getLeftDrawable(){
		return getResources().getDrawable(R.drawable.icon_exit);
	}
	
	@Override
	protected Drawable getRightDrawable(){
		return getResources().getDrawable(R.drawable.icon_settings);
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}

}
