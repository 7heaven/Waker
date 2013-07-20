/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class WakerViewPager extends ViewPager {
	
	private static final String TAG = "WakerViewPager";

	public WakerViewPager(Context context){
		super(context);
	}
	
	public WakerViewPager(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN && event.getY() > getMeasuredHeight() / 2) 
			return false;

        return super.onTouchEvent(event);
	}
}
