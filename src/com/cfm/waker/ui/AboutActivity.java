/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.ui;

import com.cfm.waker.R;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.cfm.waker.ui.base.BaseSlidableActivity;

public class AboutActivity extends BaseSlidableActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		setIsHorizontallyOnly(true);
	}

	@Override
	protected Class<? extends Activity> getLeftActivityClass() {
		// TODO Auto-generated method stub
		return this.getClass();
	}

	@Override
	protected Class<? extends Activity> getRightActivityClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Drawable getLeftDrawable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Drawable getRightDrawable() {
		// TODO Auto-generated method stub
		return null;
	}

}
