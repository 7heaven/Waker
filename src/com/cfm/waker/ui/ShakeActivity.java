/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.ui;

import com.cfm.waker.R;
import com.cfm.waker.view.RiseView;
import com.cfm.waker.view.RiseView.OnStateChangeListener;
import com.cfm.waker.view.ShakeDetector;
import com.cfm.waker.view.ShakeDetector.OnShakeListener;
import com.cfm.waker.dao.WakerDatabaseHelper;
import com.cfm.waker.dao.WakerPreferenceManager;
import com.cfm.waker.entity.Alarm;
import com.cfm.waker.log.WLog;
import com.cfm.waker.ui.base.BaseActivity;
import com.cfm.waker.util.Constants;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ShakeActivity extends BaseActivity implements OnShakeListener,
                                                           OnStateChangeListener,
                                                           OnClickListener{
	
	private RiseView view;
	private TextView content;
	private Button button;
	
	private Alarm alarm;
	private int snoozeCount;
	private int flagCount;
	
	private int sampleId;
	private String musicPath;
	private int streamId;
	
	private SoundPool soundPool;
	
	private ShakeDetector shakeDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shake);
		view = (RiseView) findViewById(R.id.shake_view);
		view.setColor(0xFF0099CC);
		view.setOnStateChangeListener(this);
		
		content = (TextView) findViewById(R.id.content);
		button = (Button) findViewById(R.id.snooze);
		button.setOnClickListener(this);
		
		alarm = WakerDatabaseHelper.getInstance(this).getAlarm(getIntent().getLongExtra(Constants.ALARM_ID, 0), mApplication.is24());
		snoozeCount = getIntent().getIntExtra(Constants.ALARM_SNOOZE_COUNT, 0);
		flagCount = getIntent().getIntExtra(Constants.ALARM_FLAG_COUNT, 0);
		
		content.setText(alarm.getHour() + ":" + alarm.getMinute() + Integer.toBinaryString(alarm.getWeek()) + "B");
		
		soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
		soundPool.load(this, R.raw.weico_loaded, 1);
		
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener(){

			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
				float volume = WakerPreferenceManager.getInstance(ShakeActivity.this).getGlobalAlarmVolume();
				streamId = soundPool.play(sampleId, volume, volume, 0, -1, 1);
				WLog.print("S", streamId + "");
			}
			
		});
		
		shakeDetector = new ShakeDetector(this);
		shakeDetector.registerOnShakeListener(this);
		//shakeDetector.start();
	}
	
	@Override
	public void onStop(){
		super.onStop();
		soundPool.stop(streamId);
	}

	@Override
	public void onShake(float speed) {
		view.rise();
	}

	@Override
	public void onStable() {
		view.fall();
	}

	@Override
	public void onUnderBottom() { }

	@Override
	public void onExceedTop() {
		Intent intent = new Intent(ShakeActivity.this, AfterWakeUpActivity.class);
		startActivity(intent);
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK){
			return true;
		}
		
		return false;
	}

	@Override
	public void onClick(View v) {
		if(snoozeCount < 3){
			snoozeCount++;
			Intent alarmIntent = new Intent(this, ShakeActivity.class);
			alarmIntent.putExtra(Constants.ALARM_ID, alarm.getId());
			alarmIntent.putExtra(Constants.ALARM_SNOOZE_COUNT, snoozeCount);
			PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, alarmIntent, flagCount * 10 + snoozeCount);
			AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
			alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + alarm.getSnoozeTime(), pendingIntent);
			
			ShakeActivity.this.finish();
		}
	}

}
