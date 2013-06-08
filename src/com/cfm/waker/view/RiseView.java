package com.cfm.waker.view;

import com.cfm.waker.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;
import android.util.AttributeSet;
import android.util.Log;

public class RiseView extends View {
	
	private static final String TAG = "RiseView";
	
	private RectF rect;
	private Paint paint;
	
	private int riseRange;
	private int fallRange;
	
	private OnStateChangeListener mOnStateChangeListener;
	
	public interface OnStateChangeListener{
		public void onExceedTop();
		public void onUnderBottom();
	}

	public RiseView(Context context){
		this(context, null);
	}
	
	public RiseView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public RiseView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RiseView);
		
		riseRange = (int) ta.getDimension(R.styleable.RiseView_rise_range, -1);
		fallRange = (int) ta.getDimension(R.styleable.RiseView_fall_range, -1);
		
		ta.recycle();
		
		rect = new RectF();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}
	
	public void setOnStateChangeListener(OnStateChangeListener mOnStateChangeListener){
		this.mOnStateChangeListener = mOnStateChangeListener;
	}
	
	public OnStateChangeListener getOnStateChangeListener(){
		return mOnStateChangeListener;
	}
	
	@Override
	public void onLayout(boolean changed, int left, int top, int right, int bottom){
		super.onLayout(changed, left, top, right, bottom);
		rect.left = getLeft();
		rect.top = getBottom();
		rect.right = getRight();
		rect.bottom = getBottom();
		Log.d(TAG, rect.toString());
	}
	
	public void setColor(int color){
		paint.setColor(color);
	}
	
	public void rise(){
		if(riseRange == -1)
			throw new NullPointerException("in order to use 'rise()' function, you shall define 'rise_range' tag in xml file");
		
		rise(riseRange);
	}
	
	public void fall(){
		if(fallRange == -1)
			throw new NullPointerException("in order to use 'fall()' function, you shall define 'fall_range' tag in xml file");
		
		fall(fallRange);
	}
	
	public void rise(int distance){
		if(rect.top > getTop()){
			rect.top -= distance;
		}else{
			mOnStateChangeListener.onExceedTop();
		}
		invalidate();
	}
	
	public void fall(int distance){
		if(rect.top < getBottom()){
			rect.top += distance;
		}else{
			mOnStateChangeListener.onUnderBottom();
		}
		invalidate();
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		canvas.drawRect(rect, paint);
	}
}
