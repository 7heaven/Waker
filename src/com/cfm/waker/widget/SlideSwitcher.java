package com.cfm.waker.widget;

import com.cfm.waker.R;
import com.cfm.waker.util.DensityUtil;
import com.cfm.waker.view.SlideEvent;
import com.cfm.waker.widget.base.BaseSlideWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

public class SlideSwitcher extends BaseSlideWidget {
	
	private String[] contents;
	private String[] drawProcedure;
	
	private int space;
	
	private Paint paint;
	
	public interface OnSwitchListener{
		public void onSwitch(String content);
	}
	
	public SlideSwitcher(Context context){
		this(context, null);
	}
	
	public SlideSwitcher(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public SlideSwitcher(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlideSwitcher);
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(ta.getDimension(R.styleable.SlideSwitcher_text_size, DensityUtil.dip2px(context, 16)));
		paint.setColor(ta.getColor(R.styleable.SlideSwitcher_text_color, 0xFFFFFFFF));
		
		ta.recycle();
	}
	
	public void setContent(String[] contents){
		this.contents = contents;
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
		
		
	}

}
