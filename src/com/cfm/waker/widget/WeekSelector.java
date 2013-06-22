package com.cfm.waker.widget;

import com.cfm.waker.log.WLog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class WeekSelector extends View {
	
	private static final String TAG = "WeekSelector";
	
	private int weekSet;
	private int movementSet;
	
	private int width, height;
	private int blockWidth;
	
	private Paint paint;
	
	public WeekSelector(Context context){
		this(context, null);
	}
	
	public WeekSelector(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public WeekSelector(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(0xFFFFFFFF);
		
		weekSet = 0x1;
	}
	
	public void setWeekSet(int weekSet){
		this.weekSet = weekSet;
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		width = getMeasuredWidth();
		height = getMeasuredHeight();
		blockWidth = width / 7;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event){
		
		switch(event.getAction() & MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN:
			getParent().requestDisallowInterceptTouchEvent(true);
			return true;
		case MotionEvent.ACTION_MOVE:
			if(event.getY() >= 0 && event.getY() <= height){
				int tc = 6 - (int) Math.floor(event.getX() / blockWidth);
				movementSet = 0x1 << tc;
				WLog.print(TAG, Integer.toBinaryString(movementSet));
			}else{
				movementSet = 0x0;
			}
			
			invalidate();
			return true;
		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			movementSet = 0x0;
			if(event.getY() < 0 || event.getY() > height) return true;
			int count = (int) Math.floor(event.getX() / blockWidth);
			int c = 6 - count;
			int t = (weekSet >> c) & 0x1;
			t ^= 0x1;
			//yeah, I know it looks like a really messy piece of shit
			//but to me it's really some kind of bitwise operation skill test :p
			weekSet = (((weekSet >> (7 - count) << 1) | t) << c) | (weekSet & (0x7F >> (count + 1) << (32 - c) >>> (32 - c)));
			
			invalidate();
			
			break;
		}
		
		return super.onTouchEvent(event);
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		int i = 0;
		do{
			int left = i * blockWidth;
			if(((weekSet | movementSet) & (0x1 << (6 - i))) > 0){
				paint.setColor(0xFF000000);
			}else{
				paint.setColor(0xFFFFFFFF);
			}
			canvas.drawRect(left, 0, left + width, height, paint);
		}while(++i < 7);
	}
}
