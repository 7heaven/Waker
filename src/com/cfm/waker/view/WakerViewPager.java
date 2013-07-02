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
	
	/*
	private float dx,dy;
	
	private static final int MODE_EDGE_DRAG = 0;
	private static final int MODE_NORMAL = 1;
	private static final int MODE_DISPATCH = 2;
	
	private int mode = MODE_DISPATCH;
	 */

	public WakerViewPager(Context context){
		super(context);
	}
	
	public WakerViewPager(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN 
		    && event.getY() > getMeasuredHeight() / 2)
			return false;
		
		return super.onTouchEvent(event);
	}
	
	//when viewPager scroll to the end, the continually scroll from same direction will disable viewpager touch event
	/*
	@Override
	public boolean onTouchEvent(MotionEvent event){
		
		switch(event.getAction() & MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN:
			if(getCurrentItem() == 0 || getCurrentItem() == getAdapter().getCount() - 1){
			    super.onTouchEvent(event);
			    return false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			switch(mode){
			case MODE_DISPATCH:
				int hisX = (int) event.getX();
				int hisY = (int) event.getY();
				try{
					hisX = (int) event.getHistoricalX(event.getHistorySize() - 1);
					hisY = (int) event.getHistoricalY(event.getHistorySize() - 1);
				}catch(ArrayIndexOutOfBoundsException e){
					
				}catch(IllegalArgumentException e){
					
				}
				dx = event.getX() - hisX;
				dy = event.getY() - hisY;
				WLog.print(TAG, "MODE_NORMAL");
				if(Math.abs(dx) > Math.abs(dy)){
					WLog.print(TAG, getCurrentItem() + "");
					if(dx < 0 && getCurrentItem() == getAdapter().getCount() - 1){
						WLog.print(TAG, "last Item and right drag:" + dx);
						mode = MODE_EDGE_DRAG;
						return false;
					}
					if(dx > 0 && getCurrentItem() == 0){
						WLog.print(TAG, "first Item and left drag:" + dx);
						mode = MODE_EDGE_DRAG;
						return false;
					}
					mode = MODE_NORMAL;
				}
				break;
			case MODE_NORMAL:
				break;
			case MODE_EDGE_DRAG:
				WLog.print(TAG, "MODE_EDGE_DRAG");
				return false;
			}
			
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mode = MODE_DISPATCH;
			
			break;
		}
		
		return  super.onTouchEvent(event);
	}
	 */
}
