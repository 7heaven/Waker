/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.ui;

import com.cfm.waker.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.cfm.waker.dao.WakerPreferenceManager;
import com.cfm.waker.log.WLog;
import com.cfm.waker.ui.base.BaseSlidableActivity;
import com.cfm.waker.widget.DialPicker.OnTimePickListener;
import com.cfm.waker.widget.ExactLinearLayout;
import com.cfm.waker.widget.Knob;

public class SettingActivity extends BaseSlidableActivity{
	
	private Knob knob;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		knob = (Knob) findViewById(R.id.volume_picker);
		theme.registerThemeObject(knob);
		
		knob.setOnTimePickListener(new OnTimePickListener(){

			@Override
			public void onStartPick() {}

			@Override
			public void onPick(int value, int increment) {}

			@Override
			public void onStopPick() {
				WakerPreferenceManager.getInstance(SettingActivity.this).setGlobalAlarmVolume(knob.getValue());
			}

			@Override
			public void onCenterClick() {}
			
		});
		
		/*
		seekBar = (SeekBar) findViewById(R.id.volume);
		seekBar.setProgress((int) (WakerPreferenceManager.getInstance(this).getGlobalAlarmVolume() * seekBar.getMax()));
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				WakerPreferenceManager.getInstance(SettingActivity.this).setGlobalAlarmVolume(seekBar.getProgress() / seekBar.getMax());
			}
			
		});
		 */
		
		setOnSlideListener(new OnSlideListener(){
			int y;
			int screenHeight = WakerPreferenceManager.getInstance(SettingActivity.this).getScreenHeight() - 
					           WakerPreferenceManager.getInstance(SettingActivity.this).getStatusBarHeight();

			@Override
			public void onHorizontallySlidePressed() {}

			@Override
			public void onHorizontallySlide(int distance) {}

			@Override
			public void onHorizontallySlideReleased(boolean isActionPerformed) {}

			@Override
			public void onVerticallySlidePressed() {
				y = getContentView().getScrollY();
			}

			@Override
			public void onVerticallySlide(int distance) {
				int measuredHeight = ((ExactLinearLayout) getContentView()).getChildAt(0).getMeasuredHeight();
				int dis = y - distance;
				
				if(dis <= 0){
					dis = 0;
				}
				
				if(dis + screenHeight >= measuredHeight){
					dis = measuredHeight - screenHeight;
				}
				
				if(measuredHeight > screenHeight) getContentView().scrollTo(0, dis);
				
				WLog.print(TAG, ((ExactLinearLayout) getContentView()).getChildAt(0).getMeasuredHeight() + ":" + dis);
			}

			@Override
			public void onVerticallySlideReleased() {}
			
		});
	}
	
	@Override
	public void onResume(){
		super.onResume();
		knob.setValue(WakerPreferenceManager.getInstance(this).getGlobalAlarmVolume());
	}
	
	@Override
	protected Class<? extends Activity> getLeftActivityClass() {
		return this.getClass();
	}

	@Override
	protected Class<? extends Activity> getRightActivityClass() {
		return AboutActivity.class;
	}

	@Override
	protected Drawable getLeftDrawable() {
		return getResources().getDrawable(R.drawable.icon_mainpage);
	}

	@Override
	protected Drawable getRightDrawable() {
		return null;
	}


}
