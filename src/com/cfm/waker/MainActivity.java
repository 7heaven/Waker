/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.cfm.waker.adapter.AlarmListAdapter;
import com.cfm.waker.dao.WakerDatabaseHelper;
import com.cfm.waker.entity.Alarm;
import com.cfm.waker.receiver.AlarmReceiver;
import com.cfm.waker.ui.SettingActivity;
import com.cfm.waker.ui.base.BaseSlidableActivity;
import com.cfm.waker.widget.DialTimePicker;
import com.cfm.waker.widget.DialTimePicker.OnTimePickListener;
import com.cfm.waker.widget.FontTextView;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

public class MainActivity extends BaseSlidableActivity implements OnTimePickListener{
	
	private FontTextView timeText;
	private FontTextView amPm;
	private DialTimePicker dialTimePicker;
	
	private TimeRunnable tRunnable;
	private Handler timeHandler;
	
	private ViewMoveRunnable vmRunnable;
	private Handler vmHandler;
	
	private Calendar calendar;
	private SimpleDateFormat dateFormat;
	
	private boolean pickingTime;
	
	private ViewPager viewPager;
	private ArrayList<Alarm> alarmList;
	private AlarmListAdapter alarmListAdapter;
	
	private int alarmCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dialTimePicker = (DialTimePicker) findViewById(R.id.time_pick);
		dialTimePicker.setOnTimePickListener(this);
		
		timeText = (FontTextView) findViewById(R.id.time);
		amPm = (FontTextView) findViewById(R.id.am_pm);
		
		tRunnable = new TimeRunnable();
		timeHandler = new Handler();
		timeHandler.post(tRunnable);
		
		vmHandler = new Handler();
		
		if(mApplication.is24()){
			dateFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
			amPm.setVisibility(View.GONE);
		}else{
			dateFormat = new SimpleDateFormat("hh:mm", Locale.CHINA);
			amPm.setVisibility(View.VISIBLE);
		}
		
		
		pickingTime = false;
		
		viewPager = (ViewPager) findViewById(R.id.alarm_list);
		alarmList = new ArrayList<Alarm>();
		alarmListAdapter = new AlarmListAdapter(this, alarmList);
		viewPager.setAdapter(alarmListAdapter);
		
		alarmCount = 0;
		
		mOnSlideListener = new OnSlideListener(){
			int y;
			
			@Override
			public void onHorizontallySlidePressed(){viewPagerShow(false);}
			
			@Override
			public void onHorizontallySlide(int distance){}
			
			@Override
			public void onHorizontallySlideReleased(){}
			
			@Override
			public void onVerticallySlidePressed(){
				y = viewPager.getScrollY();
				vmHandler.removeCallbacks(vmRunnable);
				viewPager.setVisibility(View.VISIBLE);
				if(viewPager.getCurrentItem() != 0) viewPager.setCurrentItem(0, false);
			}
			
			@Override
			public void onVerticallySlide(int distance){
				int dis = y - distance;
				
				if(dis <= 0 ){
					dis = 0;
				}
				
				if(dis >= viewPager.getMeasuredHeight()){
					dis = viewPager.getMeasuredHeight();
				}
				
				viewPager.scrollTo(0, dis);
			}
			
			@Override
			public void onVerticallySlideReleased(){
				Log.d(TAG, viewPager.getMeasuredHeight() + "");
				if(viewPager.getScrollY() > viewPager.getMeasuredHeight() / 4){
					
					viewPagerShow(false);
				}else{
					viewPagerShow(true);
				}
			}
		};
		
		updateAlarmsByDatabase();
	}
	
	//time tick movement
	private class TimeRunnable implements Runnable{
		@Override
		public void run(){
		   
			if(!pickingTime){
				calendar = Calendar.getInstance();
				
				dialTimePicker.performDial(6 * calendar.get(Calendar.SECOND));
				timeText.setText(dateFormat.format(calendar.getTime()));
				if(!mApplication.is24()) amPm.setText(calendar.get(Calendar.AM_PM) == Calendar.AM ? getString(R.string.am) : getString(R.string.pm));
				
				timeHandler.postDelayed(tRunnable, 500);
				
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
				
				Log.d(TAG, "runnable run");
				
				vmHandler.postDelayed(vmRunnable, 20);
			}else{
				if(hide){
					Log.d(TAG, "runnable gone");
					view.setVisibility(View.INVISIBLE);
					hide = false;
				}
			}
			
		}
		
	}
	
	private void viewPagerShow(boolean isShow){
		if(isShow){
			vmRunnable = new ViewMoveRunnable(viewPager, 0, 0, false);
		}else{
			vmRunnable = new ViewMoveRunnable(viewPager, 0, viewPager.getMeasuredHeight() / 2, true);
		}
		
		vmHandler.post(vmRunnable);
	}
	
	private Alarm addAlarm(Calendar calendar){
		Alarm alarm = new Alarm(calendar, mApplication.is24());
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
		if(null != alarms) alarmList.addAll(alarms);
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
		Log.d("Activity", increment + "");
		calendar.setTimeInMillis(calendar.getTimeInMillis() + (increment * 10000));
		timeText.setText(dateFormat.format(calendar.getTime()));
		if(!mApplication.is24()) amPm.setText(getString(calendar.get(Calendar.AM_PM) == Calendar.AM ? R.string.am : R.string.pm));
		
	}
	
	@Override
	public void onStopPick(){
		pickingTime = false;
		
		Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
		
		boolean beforeTime = calendar.getTimeInMillis() <= System.currentTimeMillis();
		
		Log.d(TAG, beforeTime + "");
		
		calendar.set(Calendar.SECOND, 0);
		
		intent.putExtra("alarm_id", addAlarm(calendar).getId());
		intent.putExtra("before_time", beforeTime);
		intent.putExtra("flag", alarmCount + 1);
		
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, ++alarmCount);
		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
		
		timeHandler.post(tRunnable);
	}

	@Override
	protected Class<? extends Activity> getLeftActivityClass() {
		// TODO Auto-generated method stub
		return this.getClass();
	}

	@Override
	protected Class<? extends Activity> getRightActivityClass() {
		// TODO Auto-generated method stub
		return SettingActivity.class;
	}
	
	@Override
	protected View getLeftView(){
		return null;
	}
	
	@Override
	protected View getRightView(){
		return null;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(mApplication.isDatabaseChanged()){
			updateAlarmsByDatabase();
			mApplication.setDatabaseChanged(false);
		}
	}

}
