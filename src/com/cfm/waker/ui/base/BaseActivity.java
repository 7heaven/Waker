/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.ui.base;

import com.cfm.waker.WakerApplication;
import com.cfm.waker.dao.WakerPreferenceManager;
import com.cfm.waker.theme.ThemeManager;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

public class BaseActivity extends Activity{
	
	protected static final String TAG = "BaseActivity";

	protected WakerApplication mApplication;
	protected ThemeManager theme;
	
	protected int screenWidth;
	protected int screenHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		getWindow().getDecorView().setBackgroundDrawable(null);
		
		mApplication = (WakerApplication) getApplication();
		theme = ThemeManager.getInstance(this);
		
		screenWidth = WakerPreferenceManager.getInstance(this).getScreenWidth();
		screenHeight = WakerPreferenceManager.getInstance(this).getScreenHeight();
	}
	
}
