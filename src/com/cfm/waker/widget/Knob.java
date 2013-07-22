package com.cfm.waker.widget;

import com.cfm.waker.R;
import com.cfm.waker.log.WLog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class Knob extends DialPicker{
	
	private static final String TAG = "Knob";
	
	private Drawable backgroundDrawable;
	private Drawable dotDrawable;
	
	private int maxDegreeRange;
	private int minDegreeRange;
	
	private int offset;
	
	private float dotRangeRatio = 0.05F;

	public Knob(Context context){
		this(context, null);
	}
	
	public Knob(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public Knob(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		setToKnotMode(true);
		
		backgroundDrawable = context.getResources().getDrawable(R.drawable.background_knob);
		dotDrawable = context.getResources().getDrawable(R.drawable.dot_knob);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Knob);
		maxDegreeRange = ta.getInt(R.styleable.Knob_degreeRange_max, -1);
		minDegreeRange = ta.getInt(R.styleable.Knob_degreeRange_min, -1);
		ta.recycle();
		
		if(minDegreeRange != 0) drawDegree = minDegreeRange;
		
		exactRangeRatio = 0.65F;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		
		float dotRange = backgroundBound.width() * dotRangeRatio;
		
		if(maxDegreeRange != -1 && minDegreeRange != -1){
			
			paint.setStyle(Paint.Style.STROKE);
			//paint.setStrokeWidth(dotRange * 0.4F);
			
			int range = backgroundBound.width() / 2;
			Point innerPoint = centerRadiusPoint(centerPoint, getRadiansForDraw(maxDegreeRange), range);
			Point outerPoint = centerRadiusPoint(centerPoint, getRadiansForDraw(maxDegreeRange), range * 0.92F);
			canvas.drawLine(innerPoint.x, innerPoint.y, outerPoint.x, outerPoint.y, paint);
			innerPoint = centerRadiusPoint(centerPoint, getRadiansForDraw(minDegreeRange), range);
			outerPoint = centerRadiusPoint(centerPoint, getRadiansForDraw(minDegreeRange), range * 0.92F);
			canvas.drawLine(innerPoint.x, innerPoint.y, outerPoint.x, outerPoint.y, paint);
		}
		
		paint.setStyle(Paint.Style.FILL);
		
		backgroundDrawable.setBounds(backgroundBound);
		backgroundDrawable.draw(canvas);
		
		
		int halfDotRange = (int) (dotRange / 2);
		canvas.drawCircle(drawPoint.x, drawPoint.y, dotRange * 0.45F, paint);
		dotDrawable.setBounds(drawPoint.x - halfDotRange, drawPoint.y - halfDotRange, drawPoint.x + halfDotRange, drawPoint.y + halfDotRange);
		dotDrawable.draw(canvas);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN){
			double dx = event.getX() - centerPoint.x;
			double dy = event.getY() - centerPoint.y;
			offset = angleMinus(getDegrees(Math.atan2(dy, dx)), drawDegree);
		}
		
		return super.onTouchEvent(event);
	}
	
	public float getValue(){
		float returnValue = 1F - (float) angleMinus(maxDegreeRange, drawDegree) / (float) angleMinus(maxDegreeRange,minDegreeRange);
		return returnValue;
	}
	
	public void setValue(float value){
		int degree = (int) (angleMinus(maxDegreeRange, minDegreeRange) * value);
		
		offset = 0;
		drawDegree = anglePlus(minDegreeRange, degree);
		performDial(drawDegree);
	}
	
	@Override
	public void performDial(int angle){
		int r = angleMinus(angle, offset);
		
		//to prevent drawDegree exceed Range
		if(maxDegreeRange != -1 && minDegreeRange != -1){
			//when move ACW & drawDegree exceed minDegreeRange;
			if(angleMinus(drawDegree, r) < 179 && angleMinus(minDegreeRange, r) < 179){
				offset = angleMinus(angle, drawDegree);
				drawDegree = minDegreeRange;
			//when move CW & drawDegree exceed maxDegreeRange;
			}else if(angleMinus(r, drawDegree) < 179 && angleMinus(r, maxDegreeRange) < 179){
				offset = angleMinus(angle, drawDegree);
				drawDegree = maxDegreeRange;
			}else{
				drawDegree = r;
			}
		}else{
		    drawDegree = r;
		}
		
		double realAngle = getRadiansForDraw(drawDegree);
		
		isDrawPressPoint = true;
		drawPoint = centerRadiusPoint(centerPoint, realAngle, mediumCircleRange * 1.1F);
		
		invalidate();
	}
}
