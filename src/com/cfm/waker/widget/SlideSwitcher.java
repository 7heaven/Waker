package com.cfm.waker.widget;

import com.cfm.waker.view.SlideEvent;
import com.cfm.waker.widget.base.BaseSlideWidget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class SlideSwitcher extends BaseSlideWidget {
	
	private String no = "0";
	
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public SlideSwitcher(Context context){
		this(context, null);
	}
	
	public SlideSwitcher(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public SlideSwitcher(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onSlideEvent(SlideEvent event) {
		switch(event.getAction()){
		case SlideEvent.TOUCHMODE_DOWN:
			return true;
		case SlideEvent.TOUCHMODE_DRAGGING_HORIZONTALLY:
			break;
		case SlideEvent.TOUCHMODE_DRAGGING_VERTICALLY:
			getParent().requestDisallowInterceptTouchEvent(true);
			no = String.valueOf(event.getDx());
			invalidate();
			return true;
		case SlideEvent.TOUCHMODE_IDLE:
			break;
		}
		
	    return false;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		paint.setColor(0xFF000000);
		canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);
		paint.setColor(0xFFFFFFFF);
		canvas.drawText(no, 0, getMeasuredHeight(), paint);
		
	}

}
