package com.cfm.waker.widget;

import com.cfm.waker.R;
import com.cfm.waker.log.WLog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class WeekSelector extends View {
	
	private static final String TAG = "WeekSelector";
	
	private Context context;
	
	private int weekSet;
	private int movementSet;
	
	private int width, height;
	private int blockWidth;
	
	private Paint paint;
	
	private Rect textBound;
	
	public WeekSelector(Context context){
		this(context, null);
	}
	
	public WeekSelector(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public WeekSelector(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		this.context = context;
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(0xFFFFFFFF);
		paint.setTextSize(context.getResources().getDimension(R.dimen.weekselector_textsize));
		
		weekSet = 0x0;
		textBound = new Rect();
	}
	
	public void setWeekSet(int weekSet){
		this.weekSet = weekSet;
	}
	
	public int getWeekSet(){
		return weekSet;
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
		Drawable drawable;
		String text;
		String idString;
		String weekIdString = context.getString(R.string.week_0);
		paint.getTextBounds(weekIdString, 0, weekIdString.length(), textBound);
		int textHeight = textBound.height();
		do{
			idString = context.getPackageName() + ":drawable/weekselector_";
			weekIdString = context.getPackageName() + ":string/week_" + i;
			if(i == 0){
				idString += "left_";
			}else if(i == 6){
				idString += "right_";
			}else{
				idString += "middle_";
			}
			int left = i * blockWidth;
			if(((weekSet | movementSet) & (0x1 << (6 - i))) > 0){
				paint.setColor(0x99FFFFFF);
				idString += "pressed";
			}else{
				idString += "normal";
				paint.setColor(0xFFFFFFFF);
			}
			drawable = context.getResources().getDrawable(context.getResources().getIdentifier(idString, null, null));
			drawable.setBounds(left, 0, left + blockWidth, height);
			drawable.draw(canvas);
			text = context.getString(context.getResources().getIdentifier(weekIdString, null, null));
			paint.getTextBounds(text, 0, text.length(), textBound);
			WLog.print(TAG, text + ":" + textBound.width() + ":" + textBound.height() + ":" + height);
			canvas.drawText(text, left + blockWidth / 2 - textBound.width() / 2, height / 2 + textHeight / 2, paint);
		}while(++i < 7);
	}
}
