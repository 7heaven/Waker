/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.widget;

import com.cfm.waker.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DialTimePicker extends View{
	
	private static final String TAG = "DialTimePicker";
	
	private int outerCircleRange;
	private int innerCircleRange;
	private int mediumCircleRange;
	
	private int thumbPressRange;
	
	private int outerPressRange;
	private int innerPressRange;
	
	private int circleWidth;
	
	private Point centerPoint;
	
	private boolean isDrawPressPoint;
	private boolean isCirclePressed;
	private Point drawPoint;
	
	private Integer increment;
	
	private Paint paint;
	
	private OnTimePickListener mOnTimePickListener;
	
	public interface OnTimePickListener{
		
		public void onStartPick();
		public void onPick(int value, int increment);
		public void onStopPick();
	}

	public DialTimePicker(Context context){
		this(context, null);
	}
	
	public DialTimePicker(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public DialTimePicker(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DialTimePicker);
		
		outerCircleRange = (int) ta.getDimension(R.styleable.DialTimePicker_outer_circle, 0) / 2;
		innerCircleRange = (int) ta.getDimension(R.styleable.DialTimePicker_inner_circle, 0) / 2;
		
		circleWidth = outerCircleRange - innerCircleRange;
		
		ta.recycle();
		
		thumbPressRange = 100;
		
		if(outerCircleRange != 0 && innerCircleRange != 0 && outerCircleRange > innerCircleRange){
			mediumCircleRange = (outerCircleRange + innerCircleRange) / 2;
			
			outerPressRange = mediumCircleRange + thumbPressRange / 2;
			innerPressRange = mediumCircleRange - thumbPressRange / 2;
			
		}
		
		isDrawPressPoint = false;
		isCirclePressed = false;
		
		centerPoint = new Point();
		drawPoint = new Point();
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}
	
	public void setOnTimePickListener(OnTimePickListener mOnTimePickListener){
		this.mOnTimePickListener = mOnTimePickListener;
	}
	
	public OnTimePickListener getOnTimePickListener(){
		return mOnTimePickListener;
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		centerPoint.x = getMeasuredWidth() / 2;
		centerPoint.y = getMeasuredHeight() / 2;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		final int action = event.getAction();
		
		switch(action){
		case MotionEvent.ACTION_DOWN:
			float x = event.getX() - centerPoint.x;
			float y = event.getY() - centerPoint.y;
			int pressedRange = (int) Math.sqrt(x * x + y * y);
			if(outerPressRange > pressedRange && pressedRange > innerPressRange){
				isDrawPressPoint = true;
				isCirclePressed = true;
				increment = null;
				if(null != mOnTimePickListener) mOnTimePickListener.onStartPick();
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if(isDrawPressPoint){
				getParent().requestDisallowInterceptTouchEvent(true);
				double dx = event.getX() - centerPoint.x;
				double dy = event.getY() - centerPoint.y;
				double degree = Math.atan2(dy, dx);
				drawPoint = centerRadiusPoint(centerPoint, degree, mediumCircleRange);
				
				int tDegree = (int) Math.toDegrees(degree);
				tDegree = tDegree < 0 ? 180 + (180 + tDegree) : tDegree;
				
				int incrementValue = 0;
				if(null == increment){
					increment = tDegree;
				}else{
					incrementValue = tDegree - increment;
					int positiveIncrement = Math.abs(incrementValue);
					if(positiveIncrement > 180)
						incrementValue = -(incrementValue / positiveIncrement) * (360 - positiveIncrement);
				}
				if(null != mOnTimePickListener) mOnTimePickListener.onPick(tDegree, incrementValue);
				increment = tDegree;
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			isDrawPressPoint = false;
			isCirclePressed = false;
			if(null != mOnTimePickListener) mOnTimePickListener.onStopPick();
			invalidate();
			break;
		}
		
		return isCirclePressed;
	}
	
	public void performDial(int angle){
		double realAngle = angle - 90;
		if(realAngle >= 180 && realAngle < 270){
			realAngle = angle - (360 + 90);
		}
		realAngle = Math.toRadians(realAngle);
		isDrawPressPoint = true;
		drawPoint = centerRadiusPoint(centerPoint, realAngle, mediumCircleRange);
		invalidate();
	}
	
	public void endPerformDial(){
		isDrawPressPoint = false;
		invalidate();
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		paint.setColor(0xFF24BCF6);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(circleWidth);
		canvas.drawCircle(centerPoint.x, centerPoint.y, mediumCircleRange, paint);
		
		if(isDrawPressPoint){
			paint.setColor(0xFFFFFFFF);
			paint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(drawPoint.x, drawPoint.y, circleWidth * 2, paint);
		}
		
	}
	
	private Point centerRadiusPoint(Point center, double angle, double radius){
		Point p = new Point();
		p.x = (int) (radius * Math.cos(angle)) + center.x;
		p.y = (int) (radius * Math.sin(angle)) + center.y;
		
		return p;
	}
}
