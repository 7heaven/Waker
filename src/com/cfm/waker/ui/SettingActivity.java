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
import android.widget.TextView;

import com.cfm.waker.dao.WakerDatabaseHelper;
import com.cfm.waker.entity.Alarm;
import com.cfm.waker.ui.base.BaseSlidableActivity;

public class SettingActivity extends BaseSlidableActivity {
	
	private TextView content;
	private List<Alarm> alarm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		content = (TextView) findViewById(R.id.content);
	}

	@Override
	public void onResume(){
		super.onResume();
		content.setText("");
		alarm = WakerDatabaseHelper.getInstance(this).getAlarms(true);
		if(null != alarm){
			int position = 0;
			do{
				content.append(alarm.get(position).getFormatedTime() + "\n");
			}while(++position < alarm.size());
		}
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

}
