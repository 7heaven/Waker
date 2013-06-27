package com.cfm.waker.receiver;

import com.cfm.waker.log.WLog;
import com.cfm.waker.service.WakerService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		WLog.print("BootReceiver", "starting a service");
		Intent mBootIntent = new Intent(context, WakerService.class);
		context.startService(mBootIntent);
	}

}
