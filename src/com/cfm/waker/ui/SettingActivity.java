/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.ui;

import java.util.List;

import com.cfm.waker.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cfm.waker.dao.WakerDatabaseHelper;
import com.cfm.waker.entity.Alarm;
import com.cfm.waker.ui.base.BaseSlidableActivity;

public class SettingActivity extends BaseSlidableActivity implements OnClickListener{
	
	private List<Alarm> alarm;
	private Button removeButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		removeButton = (Button) findViewById(R.id.remove_database);
		removeButton.setOnClickListener(this);
	}
	
	@Override
	protected Class<? extends Activity> getLeftActivityClass() {
		// TODO Auto-generated method stub
		return this.getClass();
	}

	@Override
	protected Class<? extends Activity> getRightActivityClass() {
		// TODO Auto-generated method stub
		return AboutActivity.class;
	}

	@Override
	protected View getLeftView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected View getRightView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.remove_database:
			WakerDatabaseHelper.getInstance(this).deleteAllAlarms();
			mApplication.setDatabaseChanged(true);
			break;
		}
	}

}
