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
import com.cfm.waker.ui.base.BaseActivity;

import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.TextView;

public class ShakeActivity extends BaseActivity implements OnShakeListener,
                                                           OnStateChangeListener{
	
	private RiseView view;
	private TextView content;
	private Alarm alarm;
	private int snoozeCount;
	
	private SoundPool soundPool;
	private int hit;
	
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
		content.setText(alarm.getHour() + ":" + alarm.getMinute() + Integer.toBinaryString(alarm.getWeek()) + "B");
		
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
		hit = soundPool.load(this, R.raw.weico_loaded, 1);
		new Thread(){
			@Override
			public void run(){
				try {
					float volume = WakerPreferenceManager.getInstance(ShakeActivity.this).getGlobalAlarmVolume();
					Thread.sleep(100);
					soundPool.play(hit, volume, volume, 0, 0, 1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}.start();
		
		shakeDetector = new ShakeDetector(this);
		shakeDetector.registerOnShakeListener(this);
		shakeDetector.start();
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
