/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.widget.base;

import com.cfm.waker.view.SlideEvent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * This abstract class BaseSlideWidget is single slide detectable widget that allow you to write a custom single slide widget more easily.
 * 
 * @author 7heaven
 *
 */
public abstract class BaseSlideWidget extends View{
	
	protected static final String TAG = "BaseSlideWidget";
	
	private SlideEvent mSlideEvent;
	private VelocityTracker velocityTracker = VelocityTracker.obtain();
	private int minVelocity;
	
	protected long t;
	
	protected boolean clickable;
	protected boolean longClickable;

	public BaseSlideWidget(Context context){
		this(context, null);
	}
	
	public BaseSlideWidget(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public BaseSlideWidget(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		mSlideEvent = new SlideEvent();
		ViewConfiguration config = ViewConfiguration.get(context);
		minVelocity = config.getScaledMinimumFlingVelocity();
		
		clickable = true;
		longClickable = false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		velocityTracker.addMovement(event);
		switch(event.getAction() & MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN:
			t = System.currentTimeMillis();
			
			mSlideEvent.setStartX(event.getX());
			mSlideEvent.setStartY(event.getY());
			
			mSlideEvent.setAction(SlideEvent.TOUCHMODE_DOWN);
			break;
		case MotionEvent.ACTION_MOVE:
			mSlideEvent.setX(event.getX());
			mSlideEvent.setY(event.getY());
			mSlideEvent.setDx(mSlideEvent.getX() - mSlideEvent.getStartX());
			mSlideEvent.setDy(mSlideEvent.getY() - mSlideEvent.getStartY());
			
			switch(mSlideEvent.getAction()){
			case SlideEvent.TOUCHMODE_DOWN:
				if(Math.abs(mSlideEvent.getDx()) >= Math.abs(mSlideEvent.getDy())){
					mSlideEvent.setAction(SlideEvent.TOUCHMODE_HORIZONTAL_START);
				}else{
					mSlideEvent.setAction(SlideEvent.TOUCHMODE_VERTICAL_START);
				}
				break;
			case SlideEvent.TOUCHMODE_HORIZONTAL_START:
				mSlideEvent.setAction(SlideEvent.TOUCHMODE_DRAGGING_HORIZONTALLY);
				break;
			case SlideEvent.TOUCHMODE_VERTICAL_START:
				mSlideEvent.setAction(SlideEvent.TOUCHMODE_DRAGGING_VERTICALLY);
				break;
			}
			
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			t = System.currentTimeMillis() - t;
			
			
			velocityTracker.computeCurrentVelocity(1000);
			mSlideEvent.setXVel(velocityTracker.getXVelocity());
			mSlideEvent.setYVel(velocityTracker.getYVelocity());
			
			if(mSlideEvent.getXVel() < minVelocity && mSlideEvent.getYVel() < minVelocity){
				if(t < 200 && clickable) performClick();
				if(t > 500 && longClickable) performLongClick();
			}
			
			mSlideEvent.setAction(SlideEvent.TOUCHMODE_IDLE);
			velocityTracker.clear();
			
			break;
		}
		
		return onSlideEvent(mSlideEvent);
	}
	
	public abstract boolean onSlideEvent(SlideEvent event);
}
