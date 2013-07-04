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

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class BaseActivity extends Activity{
	
	protected static final String TAG = "BaseActivity";

	protected WakerApplication mApplication;
	
	protected int screenWidth;
	protected int screenHeight;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mApplication = (WakerApplication) getApplication();
		
		screenWidth = WakerPreferenceManager.getInstance(this).getScreenWidth();
		screenHeight = WakerPreferenceManager.getInstance(this).getScreenHeight();
	}
}
