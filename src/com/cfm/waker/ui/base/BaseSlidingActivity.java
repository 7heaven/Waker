package com.cfm.waker.ui.base;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

import com.cfm.waker.WakerApplication;
import com.cfm.waker.dao.WakerPreferenceManager;
import com.cfm.waker.theme.ThemeManager;
import com.cfm.waker.ui.base.BaseSlidableActivity.OnSlideListener;
import com.cfm.waker.view.SlideEvent;

import com.cfm.waker.slidingmenu.SlidingActivity;

public class BaseSlidingActivity extends SlidingActivity {

	protected static final String TAG = "BaseSlidingActivity";

	protected WakerApplication mApplication;
	protected ThemeManager theme;
	
	protected int screenWidth;
	protected int screenHeight;
	
	private OnSlideListener onSlideListener;
	
	private int dx,dy;
	
	private SlideEvent slideEvent;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mApplication = (WakerApplication) getApplication();
		theme = ThemeManager.getInstance(this);
		
		screenWidth = WakerPreferenceManager.getInstance(this).getScreenWidth();
		screenHeight = WakerPreferenceManager.getInstance(this).getScreenHeight();
		
		slideEvent = new SlideEvent();
	}

	public void setOnSlideListener(OnSlideListener onSlideListener){
		this.onSlideListener = onSlideListener;
	}
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event){
		switch(event.getAction() & MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN:
			dx = (int) event.getX();
			dy = (int) event.getY();
			slideEvent.setAction(SlideEvent.TOUCHMODE_DOWN);
			break;
		case MotionEvent.ACTION_MOVE:
			dx = (int) (event.getX() - dx);
			dy = (int) (event.getY() - dy);
			
			switch(slideEvent.getAction() & SlideEvent.ACTION_DIRECTION_MASK){
			case SlideEvent.TOUCHMODE_DOWN:
				if(Math.abs(dx) < Math.abs(dy)){
					dy = (int) event.getY();
					if(null != onSlideListener) onSlideListener.onVerticallySlidePressed();
					slideEvent.setAction(SlideEvent.TOUCHMODE_DIRECTION_VERTICAL);
					return false;
				}
				break;
			case SlideEvent.TOUCHMODE_DIRECTION_VERTICAL:
				if(null != onSlideListener) onSlideListener.onVerticallySlide((int) (event.getY() - dy));
				return false;
			}
			
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if(null != onSlideListener) onSlideListener.onVerticallySlideReleased();
			slideEvent.setAction(SlideEvent.TOUCHMODE_IDLE);
			break;
		}
		
		return super.dispatchTouchEvent(event);
	}
}
