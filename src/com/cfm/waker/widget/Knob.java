package com.cfm.waker.widget;

import com.cfm.waker.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class Knob extends DialTimePicker{
	
	private Drawable backgroundDrawable;
	private Drawable dotDrawable;
	
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
		
		exactRangeRatio = 0.75F;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		
		backgroundDrawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
		backgroundDrawable.draw(canvas);
		
		float dotRange = backgroundRange * dotRangeRatio;
		int halfDotRange = (int) (dotRange / 2);
		canvas.drawCircle(drawPoint.x, drawPoint.y, dotRange * 0.45F, paint);
		dotDrawable.setBounds(drawPoint.x - halfDotRange, drawPoint.y - halfDotRange, drawPoint.x + halfDotRange, drawPoint.y + halfDotRange);
		dotDrawable.draw(canvas);
	}
}
