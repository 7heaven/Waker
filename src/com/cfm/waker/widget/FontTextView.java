/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.widget;

import com.cfm.waker.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.widget.TextView;
import android.util.AttributeSet;

public class FontTextView extends TextView {
	
	private static final String TAG = "FontTextView";

	public FontTextView(Context context){
		this(context, null);
	}
	
	public FontTextView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public FontTextView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FontTextView);
		String tfString = ta.getString(R.styleable.FontTextView_font);
		if(null != tfString && !isInEditMode()){
			Typeface tf = Typeface.createFromAsset(context.getAssets(), tfString);
			setTypeface(tf);
		}
		
		ta.recycle();
			
	}
	
}
