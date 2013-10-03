/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.view;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.cfm.waker.R;
import com.cfm.waker.widget.AlarmClockBlock;
import com.cfm.waker.widget.AlarmClockBlock.OnPerformListener;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * a RowBlock is a simple Class contain 4 AlarmClockBLock </br>
 * @author caifangmao8@gmail.com
 *
 */
public class RowBlock extends RelativeLayout {
	
	private static final String TAG = "RowBlock";
	
	private AlarmClockBlock alarm0;
	private AlarmClockBlock alarm1;
	private AlarmClockBlock alarm2;
	private AlarmClockBlock alarm3;
	
	private int[] colors;
	private HashMap<Long, Integer> alarmIds;
	
	private int visiblePosition = -1;
	
	private Handler handler;
	
	private RefreshToPrecessCallback refreshCallback;
	
	public interface RefreshToPrecessCallback{
		public void refresh(long alarmId);
	}
	
	private class AlarmInitRunnable implements Runnable{
		private AlarmClockBlock alarm;
		
		public AlarmInitRunnable(AlarmClockBlock alarm){
			this.alarm = alarm;
		}
		
		@Override
		public void run(){
			alarm.performInitMovement();
		}
	}

	public RowBlock(Context context){
		super(context);
		init(context);
	}
	
	public RowBlock(Context context, AttributeSet attrs){
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context){
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.viewpager_page_alarmblock, null, false);
		addView(view);
		
		handler = new Handler();
		
		alarm0 = (AlarmClockBlock) view.findViewById(R.id.alarm0);
		alarm1 = (AlarmClockBlock) view.findViewById(R.id.alarm1);
		alarm2 = (AlarmClockBlock) view.findViewById(R.id.alarm2);
		alarm3 = (AlarmClockBlock) view.findViewById(R.id.alarm3);
		
		colors = new int[]{alarm0.getColor(), alarm1.getColor(), alarm2.getColor(), alarm3.getColor()};
		alarmIds = new HashMap<Long, Integer>();
	}
	
	public void updateInfo(){
		alarmIds.put(alarm0.getAlarm().getId(), 0);
		alarmIds.put(alarm1.getAlarm().getId(), 1);
		alarmIds.put(alarm2.getAlarm().getId(), 2);
		alarmIds.put(alarm3.getAlarm().getId(), 3);
	}
	
	public int getItemPositionById(long id){
		return (Integer) alarmIds.get(id);
	}
	
	public AlarmClockBlock getAlarmBlock(int position){
		switch(position){
		case 0:
			return alarm0;
		case 1:
			return alarm1;
		case 2:
			return alarm2;
		case 3:
			return alarm3;
		}
		
		return null;
	}
	
	public void setAlarmVisible(int position){
		getAlarmBlock(position).setVisibility(View.VISIBLE);
		if(visiblePosition < position) visiblePosition = position;
	}
	
	public void performLastAlarmInit(){
		AlarmClockBlock lastAlarm = getAlarmBlock(visiblePosition);
		lastAlarm.prepareForInitMovement();
		setAlarmVisible(visiblePosition);
		lastAlarm.performInitMovement();
	}
	
	public void prepareForAlarmsInit(){
		getAlarmBlock(0).prepareForInitMovement();
		getAlarmBlock(1).prepareForInitMovement();
		getAlarmBlock(2).prepareForInitMovement();
		getAlarmBlock(3).prepareForInitMovement();
	}
	
	public void performAlarmsInit(){
		
		AlarmInitRunnable alarmInitRunnable0 = new AlarmInitRunnable(getAlarmBlock(0));
		AlarmInitRunnable alarmInitRunnable1 = new AlarmInitRunnable(getAlarmBlock(1));
		AlarmInitRunnable alarmInitRunnable2 = new AlarmInitRunnable(getAlarmBlock(2));
		AlarmInitRunnable alarmInitRunnable3 = new AlarmInitRunnable(getAlarmBlock(3));
		
		handler.post(alarmInitRunnable0);
		handler.postDelayed(alarmInitRunnable1, 150);
		handler.postDelayed(alarmInitRunnable2, 300);
		handler.postDelayed(alarmInitRunnable3, 450);
	}
	
	public void performAlarmDelete(final int position){
		final AlarmClockBlock alarm = getAlarmBlock(position);
		alarm.prepareForDelMovement();
		alarm.performDelMovement();
		alarm.setOnPerformListener(new OnPerformListener(){

			@Override
			public void onInitFinish() {}

			@Override
			public void onDelFinish() {
				if(null != refreshCallback) refreshCallback.refresh(alarm.getAlarm().getId());
				performRollback(position);
			}
		
		});
	}
	
	public void performRollback(int position){
		
	}
	
	public void setRefreshCallback(RefreshToPrecessCallback refreshCallback){
		this.refreshCallback = refreshCallback;
	}
	
	public RefreshToPrecessCallback getRefreshCallback(){
		return refreshCallback;
	}
	
	private int mixColor(int c0, int c1, float ratio){
		int r0 = c0 >> 16 & 0xFF;
		int g0 = c0 >> 8 & 0xFF;
		int b0 = c0 & 0xFF;
		
		int r1 = c1 >> 16 & 0xFF;
		int g1 = c1 >> 8 & 0xFF;
		int b1 = c1 & 0xFF;
		
		int rRange = (int) ((r1 - r0) * ratio);
		int gRange = (int) ((g1 - g0) * ratio);
		int bRange = (int) ((b1 - b0) * ratio);
		
		return (r0 + rRange) << 16 | (g0 + gRange) << 8 | b0 + bRange;
	}
}
