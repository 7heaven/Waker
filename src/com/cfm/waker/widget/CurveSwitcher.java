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

import com.cfm.waker.R;

public class CurveSwitcher extends Knob {
	
	private static final String TAG = CurveSwitcher.class.getSimpleName();
	
	private static final int VIEWMODE_UPPER = 0;
	private static final int VIEWMODE_LOWER = 1;
	
	private int viewMode;
	
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
		angleRange = ta.getInteger(R.styleable.CurveSwitcher_angleRange, -1);
		
		ta.recycle();
	}
	
	private void performToggle(){
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		
		switch(event.getAction() & MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if(!isInMovingMode){
				performToggle();
			}else{
				isInMovingMode = false;
			}
		}
		
		return super.onTouchEvent(event);
	}
	
	@Override
	public void onDraw(Canvas canvas){
		
        if(maxAngleRange != -1 && minAngleRange != -1){
        	paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
			canvas.drawArc(progressBound, minAngleRange - 90, angleMinus(drawDegree, minAngleRange), true, paint);
		}

	}
}
