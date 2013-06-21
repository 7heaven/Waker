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
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.cfm.waker.dao.WakerPreferenceManager;
import com.cfm.waker.log.WLog;
import com.cfm.waker.ui.base.BaseSlidableActivity;
import com.cfm.waker.widget.ExactLinearLayout;

public class SettingActivity extends BaseSlidableActivity{
	
	private SeekBar seekBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		seekBar = (SeekBar) findViewById(R.id.volume);
		seekBar.setProgress(WakerPreferenceManager.getInstance(this).getGlobalAlarmVolume());
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				WakerPreferenceManager.getInstance(SettingActivity.this).setGlobalAlarmVolume(seekBar.getProgress());
			}
			
		});
		
		mOnSlideListener = new OnSlideListener(){
			int y;
			int measuredHeight = ((ExactLinearLayout) getContentView()).getChildAt(0).getMeasuredHeight();
			int screenHeight = WakerPreferenceManager.getInstance(SettingActivity.this).getScreenHeight();

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
				int dis = y - distance;
				
				if(dis <= 0){
					dis = 0;
				}
				
				if(dis + screenHeight > measuredHeight){
					dis = measuredHeight - screenHeight;
				}
				
				if(measuredHeight > screenHeight) getContentView().scrollTo(0, dis);
				
				WLog.print(TAG, ((ExactLinearLayout) getContentView()).getChildAt(0).getMeasuredHeight() + ":" + dis);
			}

			@Override
			public void onVerticallySlideReleased() {}
			
		};
	}
	
	@Override
	protected Class<? extends Activity> getLeftActivityClass() {
		// TODO Auto-generated method stub
		return this.getClass();
	}

	@Override
	protected Class<? extends Activity> getRightActivityClass() {
		// TODO Auto-generated method stub
		return AboutActivity.class;
	}

	@Override
	protected View getLeftView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected View getRightView() {
		// TODO Auto-generated method stub
		return null;
	}


}
