package com.cfm.waker.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cfm.waker.service.WakerService;

public class SetNextDayReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		
		context.startService(new Intent(context, WakerService.class));
	}
	
}