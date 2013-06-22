package com.cfm.waker.widget;

import com.cfm.waker.R;
import com.cfm.waker.util.DensityUtil;
import com.cfm.waker.view.SlideEvent;
import com.cfm.waker.widget.base.BaseSlideWidget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;

public class SlideSwitcher extends BaseSlideWidget {
	
	private Context context;
	
	private String[] contents;
	private String[] drawProcedure;
	
	private int drawY;
	private int space;
	
	private int position;
	
	private int centerX, centerY;
	
	private Paint paint;
	private TextPaint textPaint;
	private RectF background;
	private Rect textBound;
	
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
		
		this.context = context;
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlideSwitcher);
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setColor(0xFF000000);
		textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(ta.getDimension(R.styleable.SlideSwitcher_text_size, DensityUtil.dip2px(context, 16)));
		space = (int) textPaint.getTextSize();
		textPaint.setColor(ta.getColor(R.styleable.SlideSwitcher_text_color, 0xFFFFFFFF));
		
		ta.recycle();
		
		background = new RectF();
		textBound = new Rect();
		drawProcedure = new String[2];
	}
	
	public void setContent(String[] contents){
		this.contents = contents;
		
		drawProcedure[0] = contents[0];
		drawProcedure[1] = contents[1];
	}
	
	public void setSwitch(int position){
		this.position = position;
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		centerX = getMeasuredWidth() / 2;
		centerY = getMeasuredHeight() / 2;
		
		drawY = centerY - space / 2;
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
		
		int round = DensityUtil.dip2px(context, 3);
		background.top = getTop();
		background.left = getLeft();
		background.right = getMeasuredWidth();
		background.bottom = getMeasuredHeight();
		canvas.drawRoundRect(background, round, round, paint);
		
		if(null != drawProcedure && null != contents){
			textPaint.getTextBounds(drawProcedure[0], 0, drawProcedure[0].length(), textBound);
			int preHeight = textBound.height();
			canvas.drawText(drawProcedure[0], centerX - textBound.width() / 2, drawY + preHeight, textPaint);
			textPaint.getTextBounds(drawProcedure[1], 0, drawProcedure[1].length(), textBound);
			canvas.drawText(drawProcedure[1], centerX - textBound.width() / 2, drawY + preHeight + space + textBound.height(), textPaint);
		}
	}

}
