package com.cfm.waker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.cfm.waker.R;
import com.cfm.waker.log.WLog;

public class CurveSwitcher extends Knob {
	
	private static final String TAG = CurveSwitcher.class.getSimpleName();
	
	private static final int VIEWMODE_UPPER = 0;
	private static final int VIEWMODE_LOWER = 1;
	
	private int viewMode;
	
	private int minFlingVelocity;
	
	private boolean checked = false;
	private Drawable drawableMask;
	private int angleRange;

	public CurveSwitcher(Context context){
		this(context, null);
	}
	
	public CurveSwitcher(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public CurveSwitcher(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CurveSwitcher);
		
		viewMode = ta.getInteger(R.styleable.CurveSwitcher_viewMode, 0);
		
		checked = ta.getBoolean(R.styleable.CurveSwitcher_checked, false);
		drawableMask = ta.getDrawable(R.styleable.CurveSwitcher_drawableMask);
		angleRange = ta.getInteger(R.styleable.CurveSwitcher_angleRange, 40);
		
		dotDrawable = context.getResources().getDrawable(R.drawable.seeker_dialpicker);
		
		if(viewMode == VIEWMODE_UPPER){
			minAngleRange = 360 - angleRange / 2;
			maxAngleRange = angleRange / 2;
		}else{
			minAngleRange = 180 - angleRange / 2;
			maxAngleRange = 180 + angleRange / 2;
		}
		
		ta.recycle();
		
		dotRangeRatio = 0.35F;
		
		ViewConfiguration config = ViewConfiguration.get(context);
		minFlingVelocity = config.getScaledMinimumFlingVelocity();
	}
	
	private void performToggle(){
		
		if(Math.abs(xVelocity) > minFlingVelocity){
			checked = xVelocity > 0;
		}else{
			checked = !checked;
		}
		performSwitch();
		
	}
	
	private void performSwitch(){
	    if(checked){
	    	setValue(1);
	    }else{
	    	setValue(0);
	    }
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		
		switch(event.getAction() & MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			performToggle();
		}
		
		return super.onTouchEvent(event);
	}
	
	@Override
	public void onDraw(Canvas canvas){
		
        if(maxAngleRange != -1 && minAngleRange != -1){
        	paint.setStyle(Paint.Style.STROKE);
        	paint.setStrokeCap(Paint.Cap.ROUND);
        	paint.setStrokeWidth(progressStrokeWidth);
        	//paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
			if(viewMode == VIEWMODE_UPPER){
				canvas.drawArc(progressBound, minAngleRange - 90, angleMinus(drawDegree, minAngleRange), false, paint);
			}else{
				canvas.drawArc(progressBound, drawDegree - 90, angleMinus(maxAngleRange, drawDegree), false, paint);
			}
			
			paint.setStyle(Paint.Style.FILL);
			int halfDotRange = (int) (dotRange / 2);
			canvas.drawCircle(drawPoint.x, drawPoint.y, dotRange * 0.35F, paint);
			dotDrawable.setBounds(drawPoint.x - halfDotRange, drawPoint.y - halfDotRange, drawPoint.x + halfDotRange, drawPoint.y + halfDotRange);
			dotDrawable.draw(canvas);
		}

	}
}
