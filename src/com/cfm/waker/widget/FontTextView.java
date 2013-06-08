package com.cfm.waker.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;
import android.util.AttributeSet;

public class FontTextView extends TextView {
	
	private static final String TAG = "FontTextView";

	public FontTextView(Context context){
		this(context, null);
	}
	
	public FontTextView(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public FontTextView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/swiss_ht.ttf");
		setTypeface(tf);
			
	}
}
