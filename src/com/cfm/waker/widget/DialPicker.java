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
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;

public class DialPicker extends View implements ThemeEnable{
	
	private static final String TAG = "DialPicker";
	
	public static final int MODE_PICK = 0;
	public static final int MODE_CONFIRM = 1;
	
	private int mode = MODE_PICK;
	
	private VelocityTracker velocityTracker = VelocityTracker.obtain();
	protected float xVelocity;
	
	protected int mediumCircleRange;
	protected float exactRangeRatio = 0.675F;
	
	protected int thumbPressRange;
	
	private int outerPressRange;
	private int innerPressRange;
	
	private int circleWidth;
	
	protected Point centerPoint;
	
	protected Point drawPoint;
	
	private Drawable backgroundDrawable;
	private Drawable backgroundPressedDrawable;
	private Drawable seekerDrawable;
	//private int arcDrawOffset;
	protected int drawDegree = 0;
	
	private Integer increment;
	
	protected Paint paint;
	
	private RectF arcBound;
	protected Rect backgroundBound;
	
	private OnPickListener mOnTimePickListener;
	
	private boolean convert;
	private boolean isKnobMode;
	
	protected boolean isCenterPressed;
	protected boolean isCirclePressed;
	protected boolean isDrawPressPoint;
	protected boolean isDrawCenterButtonPressed;
	
	public interface OnPickListener{
		
		//on circle pressed
		public void onStartPick();
		public void onPick(int value, int increment);
		public void onStopPick();
		
		//on center button pressed
		public void onCenterClick();
	}

	public DialPicker(Context context){
		this(context, null);
	}
	
	public DialPicker(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public DialPicker(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		circleWidth = (int) context.getResources().getDimension(R.dimen.dialpicker_default_circlewidth);
		
		thumbPressRange = (int) context.getResources().getDimension(R.dimen.dialpicker_default_thumbpressrange);
		
		centerPoint = new Point();
		drawPoint = new Point();
		
		backgroundDrawable = context.getResources().getDrawable(R.drawable.background_dialpicker);
		backgroundPressedDrawable =context.getResources().getDrawable(R.drawable.background_dialpicker_pressed);
		seekerDrawable = context.getResources().getDrawable(R.drawable.seeker_dialpicker);
		//arcDrawOffset = (int) context.getResources().getDimension(R.dimen.dialtimepicker_default_arcdrawoffset);
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DialPicker);
		
		paint.setColor(ta.getColor(R.styleable.DialPicker_color, 0xFF5CA4E5));
		
		ta.recycle();
		
		arcBound = new RectF();
		backgroundBound = new Rect();
		
		convert = false;
		isKnobMode = false;
		
		isCenterPressed = false;
		isCirclePressed = false;
		isDrawPressPoint = false;
		isDrawCenterButtonPressed = false;
		
	}
	
	public void setOnPickListener(OnPickListener mOnTimePickListener){
		this.mOnTimePickListener = mOnTimePickListener;
	}
	
	public OnPickListener getOnPickListener(){
		return mOnTimePickListener;
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		centerPoint.x = getMeasuredWidth() / 2;
		centerPoint.y = getMeasuredHeight() / 2;
		
		int backgroundRange = getMeasuredWidth() > getMeasuredHeight() ? getMeasuredHeight() : getMeasuredWidth();
		int halfBackgroundRange = backgroundRange / 2;
		
		backgroundBound.top = centerPoint.y - halfBackgroundRange;
		backgroundBound.left = centerPoint.x - halfBackgroundRange;
		backgroundBound.right = centerPoint.x + halfBackgroundRange;
		backgroundBound.bottom = centerPoint.y + halfBackgroundRange;
		
		int arcRange = (int) (backgroundRange * 0.7F) / 2;
		
		mediumCircleRange = (int) (backgroundRange * exactRangeRatio) / 2;
		
		outerPressRange = mediumCircleRange + thumbPressRange / 2;
		setToKnobMode(isKnobMode);
		
		arcBound.top = centerPoint.y - arcRange;
		arcBound.left = centerPoint.x - arcRange;
		arcBound.right = centerPoint.x + arcRange;
		arcBound.bottom = centerPoint.y + arcRange;
		
		performDial(drawDegree);
	}
	
	public void setToKnobMode(boolean isKnobMode){
		this.isKnobMode = isKnobMode;
		if(isKnobMode){
			innerPressRange = mediumCircleRange - thumbPressRange / 2;
		}else{
			innerPressRange = mediumCircleRange - thumbPressRange / 2;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		velocityTracker.addMovement(event);
		
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
						performDial(getDegrees(Math.atan2(y, x)));
					}
				}
				break;
			}
			
			break;
		case MotionEvent.ACTION_MOVE:
			float dx = event.getX() - centerPoint.x;
			float dy = event.getY() - centerPoint.y;
			if(isDrawPressPoint){
				getParent().requestDisallowInterceptTouchEvent(true);
				if(isCirclePressed){
					int tDegree = getDegrees(Math.atan2(dy, dx));
					performDial(tDegree);
					
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
			
			velocityTracker.computeCurrentVelocity(1000);
			xVelocity = velocityTracker.getXVelocity();
			
			velocityTracker.clear();
			
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
	
	
	//perform a dial action even there's no touch event input
	/**
	 * angle in degrees
	 * @param angle
	 */
	public void performDial(int angle){
		
		int angleOffset = 89;
		
		if(drawDegree > 360 - angleOffset && angle < angleOffset || angle > 360 - angleOffset && drawDegree < angleOffset){
			convert = !convert;
		}
		
		drawDegree = angle;
		
		double realAngle = getRadiansForDraw(angle);
		
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
			backgroundPressedDrawable.setBounds(backgroundBound);
			backgroundPressedDrawable.draw(canvas);
		}else{
			backgroundDrawable.setBounds(backgroundBound);
			backgroundDrawable.draw(canvas);
		}
		
		//seeker
		if(isDrawPressPoint){
			
			canvas.drawCircle(drawPoint.x, drawPoint.y, circleWidth / 5, paint);
			
			seekerDrawable.setBounds(drawPoint.x - circleWidth / 2, drawPoint.y - circleWidth / 2, drawPoint.x + circleWidth / 2, drawPoint.y + circleWidth / 2);
			seekerDrawable.draw(canvas);
		}
	}
	
	protected Point centerRadiusPoint(Point center, double angle, double radius){
		Point p = new Point();
		p.x = (int) (radius * Math.cos(angle)) + center.x;
		p.y = (int) (radius * Math.sin(angle)) + center.y;
		
		return p;
	}
	
	protected int distance(Point point1, Point point2){
		int dx = point2.x - point1.x;
		int dy = point2.y - point1.y;
		
		return (int) Math.sqrt(dx * dx + dy * dy);
	}
	
	/**
	 * angle in radians
	 * @param angel
	 * @return
	 */
	protected int getDegrees(double angle){
		angle = Math.toDegrees(angle);
		return (int) (angle <= -90 && angle >= -180 ? 450 + angle : angle + 90);
	}
	
	protected double getRadiansForDraw(int angle){
		if(angle >= 0 && angle <= 270){
			angle -= 90;
		}else{
			angle -= 450;
		}
		
		return Math.toRadians(angle);
	}
	
	protected int angleMinus(int left, int right){
		if(left - right < 0){
			return 360 + (left - right);
		}else{
			return left - right;
		}
	}
	
	protected int anglePlus(int left, int right){
		if(left + right >= 360){
			return (left + right) - 360;
		}else{
			return left + right;
		}
	}
	
	protected boolean isInRange(int start, int end, int des){
		
		if(start > end){
			return ((des <= end && des >=0) || (des >= start && des <= 360));
		}else{
			return des >=start && des <= end;
		}
		
	}
	
	//for Theme management
	
	@Override
	public void setThemeColor(int color){
		paint.setColor(color);
		invalidate();
	}

	@Override
	public void setThemeBackground(Drawable drawable) {}

	@Override
	public void setThemeResources(String path) {}

}
