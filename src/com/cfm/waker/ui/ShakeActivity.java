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

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.widget.TextView;

public class ShakeActivity extends BaseActivity implements OnShakeListener,
                                                           OnStateChangeListener{
	
	private RiseView view;
	private TextView content;
	private Alarm alarm;
	private int snoozeCount;
	
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
		alarm = WakerDatabaseHelper.getInstance(this).getAlarm(getIntent().getLongExtra("com.cfm.waker.alarm_id", 0), mApplication.is24());
		snoozeCount = getIntent().getIntExtra("com.cfm.waker.snooze_count", 0);
		sampleId = getIntent().getIntExtra("com.cfm.waker.ringtone", 0);
		musicPath = getIntent().getStringExtra("com.cfm.waker.ringtone_path");
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
	public void onUnderBottom() {
		
	}

	@Override
	public void onExceedTop() {
		Intent intent = new Intent(ShakeActivity.this, AfterWakeUpActivity.class);
		startActivity(intent);
		finish();
	}

}
