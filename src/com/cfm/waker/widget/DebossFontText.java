/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.widget;

import com.cfm.waker.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("DrawAllocation")
public class DebossFontText extends View {
	
	private Typeface tf;
	private int textColor;
	private float textSize;
	private int colorOffsetBright;
	private int colorOffsetDark;
	
	private String text;
	
	private TextPaint paint;
	private Shader shader;
	
	private int centerY;
	private int height;
	private float offset;
	
	private boolean isMarginShow;
	
	private StaticLayout layout;

	public DebossFontText(Context context){
		this(context, null);
	}
	
	public DebossFontText(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public DebossFontText(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DebossFontText);
		
		String font = ta.getString(R.styleable.DebossFontText_font);
		if(null != font && !isInEditMode()){
			tf = Typeface.createFromAsset(context.getAssets(), font);
			paint.setTypeface(tf);
		}
		
		text = ta.getString(R.styleable.DebossFontText_text);
		
		textSize = ta.getDimension(R.styleable.DebossFontText_textSize, context.getResources().getDimension(R.dimen.debossfonttext_default_textsize));
		paint.setTextSize(textSize);
		textColor = ta.getColor(R.styleable.DebossFontText_textColor, 0xFF999999);
		
		int a = textColor >> 24;
		int r = textColor >> 16 & 0xFF;
		int g = textColor >> 8 & 0xFF;
		int b = textColor & 0xFF;
		
		int max = Math.max(r, Math.max(g, b));
		int min = Math.min(r, Math.min(g, b));
		
		int colorOffset = ta.getInt(R.styleable.DebossFontText_colorOffset, 75);
		
		int colorIntOffsetDark = colorOffset;
		int colorIntOffsetBright = colorOffset;
		
		if(max + colorIntOffsetBright >= 255) colorIntOffsetBright = 255 - max;
		if(min - colorIntOffsetDark <= 0) colorIntOffsetDark = min;
		
		colorOffsetBright = a << 24 | (r + colorIntOffsetBright) << 16 | (g + colorIntOffsetBright) << 8 | (b + colorIntOffsetBright);
		colorOffsetDark = a << 24 | (r - colorIntOffsetDark) << 16 | (g - colorIntOffsetDark) << 8 | (b - colorIntOffsetDark);
		
		offset = ta.getDimension(R.styleable.DebossFontText_textPaddingOffset, context.getResources().getDimension(R.dimen.debossfonttext_default_offset));
		
		ta.recycle();
		
		isMarginShow = true;
	}
	
	public void marginShow(boolean isShow){
		isMarginShow = isShow;
		invalidate();
	}
	
	public void setText(String text){
		this.text = text;
		
		requestLayout();
		invalidate();
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		doMeasureWork();
	}
	
	private void doMeasureWork(){
		if(null != text){
			int textWidth = (int) paint.measureText(text);
			this.setMeasuredDimension(textWidth, (int) (textSize * 1.2F + (offset * 2)));
			layout = new StaticLayout(text, 0, text.length(), paint, getMeasuredWidth(), Alignment.ALIGN_CENTER, 1.0F, 0.0F, true, TruncateAt.END, getMeasuredWidth());
			
		}
		
		height = getMeasuredHeight();
		centerY = height / 2;
		
		shader = new LinearGradient(0, 0, 0, height, new int[]{colorOffsetBright, colorOffsetDark}, null, Shader.TileMode.REPEAT);
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		if(isMarginShow){
			paint.setShader(null);
			paint.setColor(colorOffsetDark);
			canvas.translate(-offset, -offset);
			layout.draw(canvas);
			//canvas.drawText(text, 0, centerY + (textBound.height() / 2) - (offset * 2), paint);
			paint.setColor(colorOffsetBright);
			canvas.translate(offset, offset);
			layout.draw(canvas);
			//canvas.drawText(text, 0, centerY + (textBound.height() / 2), paint);
			paint.setColor(textColor);
			paint.setShader(shader);
			canvas.translate(-offset, -offset);
			layout.draw(canvas);
			//canvas.drawText(text, 0, centerY + (textBound.height() / 2) - offset, paint);
		}else{
			paint.setColor(textColor);
			paint.setShader(shader);
			layout.draw(canvas);
			//canvas.drawText(text, 0, centerY + (textBound.height() / 2) - offset, paint);
		}
		
		
		
	}
	
}
