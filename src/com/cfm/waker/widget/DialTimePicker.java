/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.widget;

import com.cfm.waker.R;
import com.cfm.waker.theme.ThemeEnable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DialTimePicker extends View implements ThemeEnable{
	
	private static final String TAG = "DialTimePicker";
	
	public static final int MODE_PICK = 0;
	public static final int MODE_CONFIRM = 1;
	
	private int mode = MODE_PICK;
	
	private int mediumCircleRange;
	
	private int thumbPressRange;
	
	private int outerPressRange;
	private int innerPressRange;
	
	private int circleWidth;
	
	private Point centerPoint;
	
	private Point drawPoint;
	
	private Drawable backgroundDrawable;
	private Drawable backgroundPressedDrawable;
	private Drawable seekerDrawable;
	private int backgroundRange;
	//private int arcDrawOffset;
	private int drawDegree;
	
	private Integer increment;
	
	private Paint paint;
	
	private RectF arcBound;
	
	private OnTimePickListener mOnTimePickListener;
	
	private boolean convert;
	
	private boolean isCenterPressed;
	private boolean isCirclePressed;
	private boolean isDrawPressPoint;
	private boolean isDrawCenterButtonPressed;
	
	public interface OnTimePickListener{
		
		//on circle pressed
		public void onStartPick();
		public void onPick(int value, int increment);
		public void onStopPick();
		
		//on center button pressed
		public void onCenterClick();
	}

	public DialTimePicker(Context context){
		this(context, null);
	}
	
	public DialTimePicker(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public DialTimePicker(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		circleWidth = (int) context.getResources().getDimension(R.dimen.dialtimepicker_default_circlewidth);
		
		thumbPressRange = (int) context.getResources().getDimension(R.dimen.dialtimepicker_default_thumbpressrange);
		
		centerPoint = new Point();
		drawPoint = new Point();
		
		backgroundDrawable = context.getResources().getDrawable(R.drawable.background_dialtimepicker);
		backgroundPressedDrawable =context.getResources().getDrawable(R.drawable.background_dialtimepicker_pressed);
		seekerDrawable = context.getResources().getDrawable(R.drawable.seeker_dialtimepicker);
		//arcDrawOffset = (int) context.getResources().getDimension(R.dimen.dialtimepicker_default_arcdrawoffset);
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DialTimePicker);
		paint.setColor(ta.getColor(R.styleable.DialTimePicker_color, 0xFF5CA4E5));
		ta.recycle();
		
		arcBound = new RectF();
		
		convert = false;
		
		isCenterPressed = false;
		isCirclePressed = false;
		isDrawPressPoint = false;
		isDrawCenterButtonPressed = false;
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
		
		backgroundRange = getMeasuredWidth() > getMeasuredHeight() ? getMeasuredHeight() : getMeasuredWidth();
		
		int arcWidth = (int) (backgroundRange * 0.7F) / 2;
		int arcHeight = (int) (backgroundRange * 0.7F) / 2;
		
		mediumCircleRange = (int) (backgroundRange * 0.675F) / 2;
		
		outerPressRange = mediumCircleRange + thumbPressRange / 2;
		innerPressRange = mediumCircleRange - thumbPressRange / 2;
		
		arcBound.top = centerPoint.y - arcHeight;
		arcBound.left = centerPoint.x - arcWidth;
		arcBound.right = centerPoint.x + arcWidth;
		arcBound.bottom = centerPoint.y + arcHeight;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		final int action = event.getAction() & MotionEvent.ACTION_MASK;
		
		switch(action){
		case MotionEvent.ACTION_DOWN:
			float x = event.getX() - centerPoint.x;
			float y = event.getY() - centerPoint.y;
			int pressedRange = (int) Math.sqrt(x * x + y * y);
			
			//calculate the pressedRange to make sure it's in the clickable circle
			switch(mode){
			case MODE_CONFIRM:
				if(pressedRange <= innerPressRange){
					isDrawCenterButtonPressed = true;
					isCenterPressed = true;
				}
			case MODE_PICK:
				if(outerPressRange > pressedRange){
					isDrawPressPoint = true;
					increment = null;
					invalidate();
					if(pressedRange > innerPressRange){
						isCirclePressed = true;
						if(null != mOnTimePickListener) mOnTimePickListener.onStartPick();
					}
				}
				break;
			}
			
			break;
		case MotionEvent.ACTION_MOVE:
			double dx = event.getX() - centerPoint.x;
			double dy = event.getY() - centerPoint.y;
			if(isDrawPressPoint){
				getParent().requestDisallowInterceptTouchEvent(true);
				if(isCirclePressed){
					double degree = Math.atan2(dy, dx);
					drawPoint = centerRadiusPoint(centerPoint, degree, mediumCircleRange);
					performDial(get360Angel(degree));
					
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
			}
			
			if(isCenterPressed){
				if(Math.sqrt(dx * dx + dy * dy) <= innerPressRange){
					isDrawCenterButtonPressed = true;
				}else{
					isDrawCenterButtonPressed = false;
				}
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if(null != mOnTimePickListener){
				mOnTimePickListener.onStopPick();
				if(isDrawCenterButtonPressed) mOnTimePickListener.onCenterClick();
			}
			
			if(mode == MODE_PICK) isDrawPressPoint = false;
			isDrawCenterButtonPressed = false;
			isCirclePressed = false;
			isCenterPressed = false;
			
			invalidate();
			
			break;
		}
		
		return isCirclePressed || isCenterPressed;
	}
	
	public void setMode(int mode){
		this.mode = mode;
	}
	
	public int getMode(){
		return mode;
	}
	
	//perform a dial action ever there's no touch event input
	/**
	 * angel in degrees
	 * @param angel
	 */
	public void performDial(int angel){
		
		int angelOffset = 89;
		
		if(drawDegree > 360 - angelOffset && angel < angelOffset || angel > 360 - angelOffset && drawDegree < angelOffset){
			convert = !convert;
		}
		
		drawDegree = angel;
		
		double realAngle = angel - 90;
		if(realAngle >= 180 && realAngle < 270){
			realAngle = angel - (360 + 90);
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
		
		//progressBar
		if(isDrawPressPoint){
			if(convert){
				canvas.drawArc(arcBound, drawDegree - 90, 360 - drawDegree, true, paint);
			}else{
				canvas.drawArc(arcBound, -90, drawDegree, true, paint);
			}
			
		}
		
		//background
		if(isDrawCenterButtonPressed){
			backgroundPressedDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
			backgroundPressedDrawable.draw(canvas);
		}else{
			backgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
			backgroundDrawable.draw(canvas);
		}
		
		//seeker
		if(isDrawPressPoint){
			
			canvas.drawCircle(drawPoint.x, drawPoint.y, circleWidth / 5, paint);
			
			seekerDrawable.setBounds(drawPoint.x - circleWidth / 2, drawPoint.y - circleWidth / 2, drawPoint.x + circleWidth / 2, drawPoint.y + circleWidth / 2);
			seekerDrawable.draw(canvas);
		}
	}
	
	private Point centerRadiusPoint(Point center, double angle, double radius){
		Point p = new Point();
		p.x = (int) (radius * Math.cos(angle)) + center.x;
		p.y = (int) (radius * Math.sin(angle)) + center.y;
		
		return p;
	}
	
	/**
	 * angel in radians
	 * @param angel
	 * @return
	 */
	private int get360Angel(double angel){
		angel = Math.toDegrees(angel);
		return (int) (angel <= -90 && angel >= -180 ? 450 + angel : angel + 90);
	}
	
	//for Theme management
	
	@Override
	public void setThemeColor(int color){
		paint.setColor(color);
	}

	@Override
	public void setThemeBackground(Drawable drawable) {}

	@Override
	public void setThemeResources(String path) {
		// TODO Auto-generated method stub
		
	}

}
