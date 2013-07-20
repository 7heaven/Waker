package com.cfm.waker.view;

import com.cfm.waker.theme.ThemeEnable;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class ThemeView extends View implements ThemeEnable{

	public ThemeView(Context context){
		this(context, null);
	}
	
	public ThemeView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public ThemeView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}

	@Override
	public void setThemeColor(int color) {
		setBackgroundColor(color);
	}

	@Override
	public void setThemeBackground(Drawable drawable) {
		setBackgroundDrawable(drawable);
	}

	@Override
	public void setThemeResources(String path) {}
}
