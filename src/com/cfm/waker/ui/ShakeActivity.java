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
import com.cfm.waker.ui.base.BaseActivity;

import android.content.Intent;
import android.os.Bundle;

public class ShakeActivity extends BaseActivity implements OnShakeListener,
                                                           OnStateChangeListener{
	
	private RiseView view;
	private ShakeDetector shakeDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shake);
		view = (RiseView) findViewById(R.id.shake_view);
		view.setColor(0xFF0099CC);
		view.setOnStateChangeListener(this);
		
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
