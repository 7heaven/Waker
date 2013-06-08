package com.cfm.waker.view;

import com.cfm.waker.R;
import com.cfm.waker.widget.AlarmClockBlock;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class RowBlock extends LinearLayout {
	
	private AlarmClockBlock alarm0;
	private AlarmClockBlock alarm1;
	private AlarmClockBlock alarm2;
	private AlarmClockBlock alarm3;

	public RowBlock(Context context){
		super(context);
		init(context);
	}
	
	public RowBlock(Context context, AttributeSet attrs){
		super(context, attrs);
		init(context);
	}
	
	private void init(Context context){
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.viewpager_page_alarmblock, null, false);
		addView(view);
		
		alarm0 = (AlarmClockBlock) view.findViewById(R.id.alarm0);
		alarm1 = (AlarmClockBlock) view.findViewById(R.id.alarm1);
		alarm2 = (AlarmClockBlock) view.findViewById(R.id.alarm2);
		alarm3 = (AlarmClockBlock) view.findViewById(R.id.alarm3);
	}
	
	public AlarmClockBlock getAlarmBlock(int position){
		switch(position){
		case 0:
			return alarm0;
		case 1:
			return alarm1;
		case 2:
			return alarm2;
		case 3:
			return alarm3;
		}
		
		return null;
	}
	
}
