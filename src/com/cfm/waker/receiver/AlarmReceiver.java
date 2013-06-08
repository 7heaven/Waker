package com.cfm.waker.receiver;

import com.cfm.waker.ui.ShakeActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "alarm!!", Toast.LENGTH_LONG).show();
		Intent mIntent = new Intent(context, ShakeActivity.class);
		context.startActivity(mIntent);
	}

}
