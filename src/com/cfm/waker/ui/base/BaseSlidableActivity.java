/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.ui.base;

import java.lang.Runnable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * this base Activity enable < ? extends BaseSlidableActivity > to detect and response a slide movement
 * @author caifangmao8@gmail.com
 *
 */
public abstract class BaseSlidableActivity extends BaseActivity {
	
	protected static final String TAG = "BaseSlidableActivity";
	
	private View mContent;
	
  //private View leftIcon;
  //private View rightIcon;
	
	private int dx, dy;
	private int moveX = 0;
  //private int moveY = 0;
	private float distance;
	private float overScrollDistance;
	
	private static final int TOUCHMODE_IDLE = 0;
	private static final int TOUCHMODE_DOWN = 1;
	private static final int TOUCHMODE_DRAGGING_HORIZONTALLY = 2;
	private static final int TOUCHMODE_DRAGGING_VERTICALLY = 3;
	
	private int touchMode;
	
	private boolean horizontallyScrollOnly = false;
	
	protected OnSlideListener mOnSlideListener;
	
	private MyRunnable mRunnable;
	private Handler mHandler;
	
	/**
	 * Must Override this class to return a left enter view(Activity) for a right Fling movement </br></br>
	 * 
	 * Any class< ? extends Activity > return itself will trigger finish()  </br></br>
	 * 
	 * You can return null when you don't want any action for a right Fling movement
	 * @return Class<? extends Activity>
	 */
	protected abstract Class<? extends Activity> getLeftActivityClass();
	
	/**
	 * Must Override this class to return a left enter view(Activity) for a left Fling movement </br></br>
	 * 
	 * Any class< ? extends Activity > return itself will trigger finish()  </br></br>
	 * 
	 * You can return null when you don't want any action for a left Fling movement
	 * @return Class<? extends Activity>
	 */
	protected abstract Class<? extends Activity> getRightActivityClass();
	
	/**
	 * return a View for right Fling hint
	 * @return
	 */
	protected abstract View getLeftView();
	
	/**
	 * return a View for left Fling hint
	 * @return
	 */
	protected abstract View getRightView();
	
	/**
	 * this Listener provide a interface for customize slide action
	 * @author 7heaven
	 *
	 */
	public interface OnSlideListener{
		public void onHorizontallySlidePressed();
		public void onHorizontallySlide(int distance);
		public void onHorizontallySlideReleased(boolean isActionPerformed);
		public void onVerticallySlidePressed();
		public void onVerticallySlide(int distance);
		public void onVerticallySlideReleased();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mHandler = new Handler();
		touchMode = TOUCHMODE_IDLE;
	}
	
	@Override
	public void setContentView(View view){
		setContentView(view, new ViewGroup.LayoutParams(-1, -1));
	}
	
	@Override
	public void setContentView(int resId){
		setContentView(getLayoutInflater().inflate(resId, null));
	}
	
	//Override setContentView(View, ViewGroup.LayoutParams) to get main content of this Activity
	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params){
		super.setContentView(view, params);
		mContent = view;
	}
	
	public View getContentView(){
		return mContent;
	}
	
	public void setIsHorizontallyOnly(boolean arg){
		horizontallyScrollOnly = arg;
	}
	
	//all those reduplicated and seems unnecessary codes are wrote to prevent TouchEvent being intercept by child view if any child view have it's own onTouchEvent
	//this is a temporally solution for the situation posted previously
	@Override
	public boolean onTouchEvent(MotionEvent event){
		distance = mContent.getMeasuredWidth() * 0.3F;
		overScrollDistance = mContent.getMeasuredWidth() * 0.6F;
		switch(event.getAction() & MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN:
			dx = (int) event.getX();
			dy = (int) event.getY();
			touchMode = TOUCHMODE_DOWN;
			break;
		case MotionEvent.ACTION_MOVE:
			switch(touchMode){
			case TOUCHMODE_IDLE:
				touchMode = TOUCHMODE_DOWN;
				if(null == (Integer) dx && null == (Integer) dy){
					dx = (int) event.getHistoricalX(event.getHistorySize() - 1);
					dy = (int) event.getHistoricalY(event.getHistorySize() - 1);
				}
				break;
			case TOUCHMODE_DOWN:
				if(horizontallyScrollOnly){
					touchMode = TOUCHMODE_DRAGGING_HORIZONTALLY;
				}else{
					if(Math.abs(event.getX() - dx) > Math.abs(event.getY() - dy)){
						dx = (int) event.getX();
						dy = (int) event.getY();
						touchMode = TOUCHMODE_DRAGGING_HORIZONTALLY;
						if(null != mOnSlideListener) mOnSlideListener.onHorizontallySlidePressed();
					}else{
						dx = (int) event.getX();
						dy = (int) event.getY();
						touchMode = TOUCHMODE_DRAGGING_VERTICALLY;
						if(null != mOnSlideListener) mOnSlideListener.onVerticallySlidePressed();
					}
				}
				break;
			case TOUCHMODE_DRAGGING_HORIZONTALLY:
				int scrollY = mContent.getScrollY();
				moveX = (int) (dx - event.getX());
				//Temporally solution for overScroll bounce
				float diff = moveX / overScrollDistance;
				
				if(null != mOnSlideListener) mOnSlideListener.onHorizontallySlide(-moveX);
				if(moveX < distance && moveX > -distance){
					//Temporally solution for overScroll bounce
					if(moveX < 0){
						moveX = (int) (moveX + moveX * diff);
					}
					if(moveX > 0){
						moveX = (int) (moveX - moveX * diff);
					}
					
					mContent.scrollTo(moveX, scrollY);
				}
				if(moveX >= distance){
					if(null != getRightActivityClass()){
						if(this.getClass() == getRightActivityClass()){
							finish();
						}else{
							Intent intent = new Intent(this, getRightActivityClass());
							startActivity(intent);
						}
						touchMode = TOUCHMODE_IDLE;
						moveX = 0;
					}
					
					return true;
				}
				
				if(moveX <= -distance){
					if(null != getLeftActivityClass()){
						if(this.getClass() == getLeftActivityClass()){
		                    finish();
						}else{
							Intent intent = new Intent(this, getLeftActivityClass());
							startActivity(intent);
						}
						touchMode = TOUCHMODE_IDLE;
						moveX = 0;
					}
					
					return true;
				}
				break;
			case TOUCHMODE_DRAGGING_VERTICALLY:
				if(null != mOnSlideListener) mOnSlideListener.onVerticallySlide((int) (event.getY() - dy));
				break;
			}
			
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			boolean isPerformed;
			if(touchMode != TOUCHMODE_IDLE){
				isPerformed = false;
				backToOriginalSpot();
			}else{
				isPerformed = true;
			}
			
			moveX = 0;
			
			if(null != mOnSlideListener){
				if(touchMode == TOUCHMODE_DRAGGING_VERTICALLY){
					mOnSlideListener.onVerticallySlideReleased();
				}else if(touchMode == TOUCHMODE_DRAGGING_HORIZONTALLY){
					mOnSlideListener.onHorizontallySlideReleased(isPerformed);
				}
			}
			
			touchMode = TOUCHMODE_IDLE;
			break;
		}
		
		return super.onTouchEvent(event);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		mContent.scrollTo(0, 0);
	}
	
	private void stopContentMovement(){
		mHandler.removeCallbacks(mRunnable);
	}
	
	private void backToOriginalSpot(){
		mRunnable = new MyRunnable(mContent.getScrollX(), mContent.getScrollY());
		mHandler.post(mRunnable);
	}
	
	private class MyRunnable implements Runnable{
		private float moveX, moveY;
		
		public MyRunnable(int moveX, int moveY){
			this.moveX = moveX;
			this.moveY = moveY;
		}
		@Override
		public void run(){
			if((int) moveX != 0 || (int) moveY != 0){
				moveX += (0 - moveX) * 0.6F;
				//moveY += (0 - moveY) * 0.6F;
				
				mContent.scrollTo((int) moveX, (int) moveY);
				mHandler.postDelayed(mRunnable, 20);
				
			}
		}
	}
}
