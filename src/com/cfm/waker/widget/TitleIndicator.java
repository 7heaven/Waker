package com.cfm.waker.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.cfm.waker.R;
import com.cfm.waker.theme.ThemeEnable;

public class TitleIndicator extends TextView implements ThemeEnable{
	
	private static final String TAG = "TitleIndicator";
	
	private String title;
	private Paint paint;
	private Rect bound;
	private int margin;
	
	private Point centerPoint;
	private int width,height;

	public TitleIndicator(Context context){
		this(context, null);
	}
	
	public TitleIndicator(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public TitleIndicator(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bound = new Rect();
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleIndicator);
		title = ta.getString(R.styleable.TitleIndicator_text);
		paint.setColor(ta.getColor(R.styleable.TitleIndicator_color, context.getResources().getColor(R.color.global_hint_color)));
		paint.setTextSize(ta.getDimension(R.styleable.TitleIndicator_textSize, 0));
		ta.recycle();
		
		margin = (int) (paint.getTextSize() / 2);
		
		centerPoint = new Point();
	}
	
	public void setTitle(String title){
		this.title = title;
		requestLayout();
		invalidate();
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		width = getMeasuredWidth();
		height = getMeasuredHeight();
		
		if(null != title){
			paint.getTextBounds(title, 0, title.length(), bound);
			
			if(width < bound.width()){
				width = bound.width();
				
				setMeasuredDimension(width, height);
			}
			
			if(height < bound.height()){
				height = bound.height();
				
				setMeasuredDimension(width, height);
			}
		}
		
		centerPoint.x = width / 2;
		centerPoint.y = height / 2;
	}
	
	@Override
	public void onDraw(Canvas canvas){
		if(null != title){
			canvas.drawLine(0, centerPoint.y, centerPoint.x - bound.width() / 2 - margin, centerPoint.y, paint);
			canvas.drawLine(centerPoint.x + bound.width() / 2 + margin, centerPoint.y, width, centerPoint.y, paint);
			canvas.drawText(title, centerPoint.x - bound.width() / 2, centerPoint.y + bound.height() / 2, paint);
		}
	}

	@Override
	public void setThemeColor(int color) {
		paint.setColor(color);
		invalidate();
	}

	@Override
	public void setThemeBackground(Drawable drawable) {}

	@Override
	public void setThemeResources(String path) {}
}
