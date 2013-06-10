/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.ui.base;

import com.cfm.waker.WakerApplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class BaseActivity extends Activity{

	protected WakerApplication mApplication;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		mApplication = (WakerApplication) getApplication();
	}
}
