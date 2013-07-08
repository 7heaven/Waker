package com.cfm.waker.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class WakerTextView extends TextView{
	
	public WakerTextView(Context context){
		this(context, null);
	}
	
	public WakerTextView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public WakerTextView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}
}