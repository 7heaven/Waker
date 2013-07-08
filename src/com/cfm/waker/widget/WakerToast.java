package com.cfm.waker.widget;

import com.cfm.waker.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class WakerToast{
	
	public static Toast makePositiveText(Context context, CharSequence text, int duration){
        Toast result = new Toast(context);
		
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = layoutInflater.inflate(R.layout.toast_layout, null);
		TextView tv = (TextView) v.findViewById(R.id.content);
		tv.setText(text);
		
		ImageView iv = (ImageView) v.findViewById(R.id.image);
		iv.setImageResource(R.drawable.icon_positive_toast);
		
		result.setView(v);
		result.setDuration(duration);
		
		return result;
	}
	
	public static Toast makeNegativeText(Context context, CharSequence text, int duration){
        Toast result = new Toast(context);
		
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = layoutInflater.inflate(R.layout.toast_layout, null);
		TextView tv = (TextView) v.findViewById(R.id.content);
		tv.setText(text);
		
		ImageView iv = (ImageView) v.findViewById(R.id.image);
		iv.setImageResource(R.drawable.icon_negative_toast);
		
		result.setView(v);
		result.setDuration(duration);
		
		return result;
	}

}
