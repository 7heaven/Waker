package com.cfm.waker.widget;

import com.cfm.waker.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class Knob extends DialPicker{
	
	private static final String TAG = "Knob";
	
	private Drawable backgroundDrawable;
	protected Drawable dotDrawable;
	
	protected int maxAngleRange;
	protected int minAngleRange;
	
	protected float dotRadiusRange;
	protected float dotRange;
	
	protected int offset;
	
	protected float dotRangeRatio = 0.055F;
	protected float dotRadiusRatio = 1.115F;
	
	protected int boundStrokeWidth;
	protected int progressStrokeWidth;

	protected RectF progressBound;
	
	public Knob(Context context){
		this(context, null);
	}
	
	public Knob(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public Knob(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		setToKnobMode(true);
		
		backgroundDrawable = context.getResources().getDrawable(R.drawable.background_knob);
		dotDrawable = context.getResources().getDrawable(R.drawable.dot_knob);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Knob);
		maxAngleRange = ta.getInt(R.styleable.Knob_angleRange_max, -1);
		minAngleRange = ta.getInt(R.styleable.Knob_angleRange_min, -1);
		ta.recycle();
		
		if(minAngleRange != 0) drawDegree = minAngleRange;
		
		exactRangeRatio = 0.65F;
		
		boundStrokeWidth = context.getResources().getDimensionPixelOffset(R.dimen.knob_bound_stroke_width);
		progressStrokeWidth = context.getResources().getDimensionPixelOffset(R.dimen.knob_progress_stroke_width);
		
		progressBound = new RectF();
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		dotRange = mediumCircleRange * dotRangeRatio;
		dotRadiusRange = mediumCircleRange * dotRadiusRatio;
		
		progressBound.left = centerPoint.x - dotRadiusRange;
		progressBound.top = centerPoint.y - dotRadiusRange;
		progressBound.right = centerPoint.x + dotRadiusRange;
		progressBound.bottom = centerPoint.y + dotRadiusRange;
		
	}
	
	@Override
	public void onDraw(Canvas canvas){
		
		//float dotRange = backgroundBound.width() * dotRangeRatio;
		
		if(maxAngleRange != -1 && minAngleRange != -1){
			
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeCap(Paint.Cap.ROUND);
			/*
			paint.setStrokeWidth(boundStrokeWidth);
			
			//paint.setStrokeWidth(dotRange * 0.4F);
			
			int range = backgroundBound.width() / 2;
			Point innerPoint = centerRadiusPoint(centerPoint, getRadiansForDraw(maxAngleRange), range);
			Point outerPoint = centerRadiusPoint(centerPoint, getRadiansForDraw(maxAngleRange), range * 0.92F);
			canvas.drawLine(innerPoint.x, innerPoint.y, outerPoint.x, outerPoint.y, paint);
			innerPoint = centerRadiusPoint(centerPoint, getRadiansForDraw(minAngleRange), range);
			outerPoint = centerRadiusPoint(centerPoint, getRadiansForDraw(minAngleRange), range * 0.92F);
			canvas.drawLine(innerPoint.x, innerPoint.y, outerPoint.x, outerPoint.y, paint);
			 */
			
			paint.setStrokeWidth(progressStrokeWidth);
			canvas.drawArc(progressBound, minAngleRange - 90, angleMinus(drawDegree, minAngleRange), false, paint);
		}
		
		paint.setStyle(Paint.Style.FILL);
		
		backgroundDrawable.setBounds(backgroundBound);
		backgroundDrawable.draw(canvas);
		
		
		/*
		int halfDotRange = (int) (dotRange / 2);
		canvas.drawCircle(drawPoint.x, drawPoint.y, dotRange * 0.45F, paint);
		dotDrawable.setBounds(drawPoint.x - halfDotRange, drawPoint.y - halfDotRange, drawPoint.x + halfDotRange, drawPoint.y + halfDotRange);
		dotDrawable.draw(canvas);
		 */
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		if((event.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_DOWN){
			double dx = event.getX() - centerPoint.x;
			double dy = event.getY() - centerPoint.y;
			
			int angle = getDegrees(Math.atan2(dy, dx));
			
			if(!isInRange(minAngleRange, maxAngleRange, angle)) return false;
			
			//offset = angleMinus(angle, drawDegree);
		}
		
		return super.onTouchEvent(event);
	}
	
	public float getValue(){
		float returnValue = 1F - (float) angleMinus(maxAngleRange, drawDegree) / (float) angleMinus(maxAngleRange,minAngleRange);
		return returnValue;
	}
	
	public void setValue(float value){
		int degree = (int) (angleMinus(maxAngleRange, minAngleRange) * value);
		
		//offset = 0;
		drawDegree = anglePlus(minAngleRange, degree);
		performDial(drawDegree);
	}
	
	@Override
	public void performDial(int angle){
		//int r = angleMinus(angle, offset);
		int r = angle;
		
		//to prevent drawDegree exceed Range
		if(maxAngleRange != -1 && minAngleRange != -1){
			//when move ACW & drawDegree exceed minAngleRange;
			if(angleMinus(drawDegree, r) < 179 && angleMinus(minAngleRange, r) < 179){
				offset = angleMinus(angle, drawDegree);
				drawDegree = minAngleRange;
			//when move CW & drawDegree exceed maxAngleRange;
			}else if(angleMinus(r, drawDegree) < 179 && angleMinus(r, maxAngleRange) < 179){
				offset = angleMinus(angle, drawDegree);
				drawDegree = maxAngleRange;
			}else{
				drawDegree = r;
			}
		}else{
		    drawDegree = r;
		}
		
		double realAngle = getRadiansForDraw(drawDegree);
		
		isDrawPressPoint = true;
		drawPoint = centerRadiusPoint(centerPoint, realAngle, dotRadiusRange);
		
		invalidate();
	}
}
