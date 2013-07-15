package com.cfm.waker.widget;

import com.cfm.waker.view.SlideEvent;
import com.cfm.waker.widget.base.BaseSlideWidget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

public class WakerSeekBar extends BaseSlideWidget{
	
	private static final String TAG = "WakerSeekBar";
	
	private Rect miniRange;
	
	public WakerSeekBar(Context context){
		this(context, null);
	}

	public WakerSeekBar(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public WakerSeekBar(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onSlideEvent(SlideEvent event) {
		
		switch(event.getAction() & SlideEvent.ACTION_DRAGGING_MASK){
		
		}
		
		return false;
	}
}
