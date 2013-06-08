package com.cfm.waker.ui.base;

import java.lang.Runnable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseSlidableActivity extends BaseActivity {
	
	protected static final String TAG = "BaseSlidableActivity";
	
	private View mContent;
	
	private View leftIcon;
	private View rightIcon;
	
	private int dx, dy;
	private int moveX = 0;
  //private int moveY = 0;
	private int distance;
	
	private static final int TOUCHMODE_IDLE = 0;
	private static final int TOUCHMODE_DOWN = 1;
	private static final int TOUCHMODE_DRAGGING_HORIZONTALLY = 2;
	private static final int TOUCHMODE_DRAGGING_VERTICALLY = 3;
	
	private int touchMode;
	
	protected OnVerticallySlideListener mOnVerticallySlideListener;
	
	private MyRunnable runnable;
	private Handler handler;
	
	protected abstract Class<? extends Activity> getLeftActivityClass();
	protected abstract Class<? extends Activity> getRightActivityClass();
	
	protected abstract View getLeftView();
	protected abstract View getRightView();
	
	public interface OnVerticallySlideListener{
		public void onVerticallySlidePressed();
		public void onVerticallySlide(int distance);
		public void onVerticallySlideReleased();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		handler = new Handler();
	}
	
	@Override
	public void setContentView(View view){
		setContentView(view, new ViewGroup.LayoutParams(-1, -1));
	}
	
	@Override
	public void setContentView(int resId){
		setContentView(getLayoutInflater().inflate(resId, null));
	}
	
	@Override
	public void setContentView(View view, ViewGroup.LayoutParams params){
		super.setContentView(view, params);
		mContent = view;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		distance = mContent.getMeasuredWidth() / 5;
		switch(event.getAction() & MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN:
			dx = (int) event.getX();
			dy = (int) event.getY();
			touchMode = TOUCHMODE_DOWN;
			break;
		case MotionEvent.ACTION_MOVE:
			Log.d(TAG, "move");
			switch(touchMode){
			case TOUCHMODE_IDLE:
				break;
			case TOUCHMODE_DOWN:
				if(Math.abs(event.getX() - dx) > Math.abs(event.getY() - dy)){
					touchMode = TOUCHMODE_DRAGGING_HORIZONTALLY;
					//leftIcon = getLeftView();
					//rightIcon = getRightView();
					//addContentView(leftIcon, null);
					//addContentView(rightIcon, null);
				}else{
					touchMode = TOUCHMODE_DRAGGING_VERTICALLY;
					if(null != mOnVerticallySlideListener) mOnVerticallySlideListener.onVerticallySlidePressed();
				}
				break;
			case TOUCHMODE_DRAGGING_HORIZONTALLY:
				moveX = (int) (dx - event.getX());
				if(moveX < distance && moveX > -distance) mContent.scrollTo(moveX, 0);
				if(moveX > distance){
					if(null != getRightActivityClass()){
						if(this.getClass() == getRightActivityClass()){
							finish();
						}else{
							Intent intent = new Intent(this, getRightActivityClass());
							startActivity(intent);
							touchMode = TOUCHMODE_IDLE;
						}
						moveX = 0;
					}
					
					return true;
				}
				
				if(moveX < -distance){
					if(null != getLeftActivityClass()){
						if(this.getClass() == getLeftActivityClass()){
		                    finish();
						}else{
							Intent intent = new Intent(this, getLeftActivityClass());
							startActivity(intent);
							touchMode = TOUCHMODE_IDLE;
						}
						moveX = 0;
					}
					
					return true;
				}
				break;
			case TOUCHMODE_DRAGGING_VERTICALLY:
				if(null != mOnVerticallySlideListener) mOnVerticallySlideListener.onVerticallySlide((int) (event.getY() - dy));
				break;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			//向右滑动出界  && 左边Activity为空 || 向左滑动出界  && 右边Activity为空 || 滑动未超出界限
			if((null == getLeftActivityClass() && moveX < 0) || (null == getRightActivityClass() && moveX > 0) || (moveX < distance && moveX > -distance)){
				backToOriginalSpot();
			}else{
				stopContentMovement();
			}
			
			moveX = 0;
			
			if(touchMode == TOUCHMODE_DRAGGING_VERTICALLY && null != mOnVerticallySlideListener) mOnVerticallySlideListener.onVerticallySlideReleased();
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
		handler.removeCallbacks(runnable);
	}
	
	private void backToOriginalSpot(){
		runnable = new MyRunnable(mContent.getScrollX(), 0);
		handler.post(runnable);
	}
	
	private class MyRunnable implements Runnable{
		private float moveX, moveY;
		
		public MyRunnable(int moveX, int moveY){
			this.moveX = moveX;
			this.moveY = moveY;
		}
		@Override
		public void run(){
			Log.d(TAG, "x:" + moveX + ",y:" + moveY);
			if((int) moveX != 0 || (int) moveY != 0){
				moveX += (0 - moveX) * 0.6F;
				moveY += (0 - moveY) * 0.6F;
				
				mContent.scrollTo((int) moveX, (int) moveY);
				handler.postDelayed(runnable, 20);
				
			}
		}
	}
}
