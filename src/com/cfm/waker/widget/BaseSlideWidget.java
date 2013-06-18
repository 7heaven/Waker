package com.cfm.waker.widget;

import com.cfm.waker.view.SlideEvent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public abstract class BaseSlideWidget extends View{
	
	protected static final String TAG = "BaseSlideWidget";
	
	private SlideEvent mSlideEvent;

	public BaseSlideWidget(Context context){
		this(context, null);
	}
	
	public BaseSlideWidget(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public BaseSlideWidget(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		mSlideEvent = new SlideEvent();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		switch(event.getAction() & MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN:
			mSlideEvent.setStartX(event.getX());
			mSlideEvent.setStartY(event.getY());
			
			mSlideEvent.setAction(SlideEvent.TOUCHMODE_DOWN);
			break;
		case MotionEvent.ACTION_MOVE:
			mSlideEvent.setX(event.getX());
			mSlideEvent.setY(event.getY());
			mSlideEvent.setDx(mSlideEvent.getX() - mSlideEvent.getStartX());
			mSlideEvent.setDy(mSlideEvent.getY() - mSlideEvent.getStartY());
			if(mSlideEvent.getAction() == SlideEvent.TOUCHMODE_DOWN){
				if(Math.abs(mSlideEvent.getDx()) > Math.abs(mSlideEvent.getDy())){
					mSlideEvent.setAction(SlideEvent.TOUCHMODE_DRAGGING_HORIZONTALLY);
				}else{
					mSlideEvent.setAction(SlideEvent.TOUCHMODE_DRAGGING_VERTICALLY);
				}
			}
			
			break;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mSlideEvent.setAction(SlideEvent.TOUCHMODE_IDLE);
			break;
		}
		
		return onSlideEvent(mSlideEvent);
	}
	
	public abstract boolean onSlideEvent(SlideEvent event);
}
