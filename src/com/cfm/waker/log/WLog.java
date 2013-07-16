package com.cfm.waker.log;

import android.util.Log;

public class WLog {

	public static void print(Class<?> c, String content){
		Log.d(c.getSimpleName(), content);
	}
	
	public static void print(Object object, String content){
		Log.d(object.getClass().getSimpleName(), content);
	}
	
	public static void print(String tag, String content){
		Log.d(tag, content);
	}
}
