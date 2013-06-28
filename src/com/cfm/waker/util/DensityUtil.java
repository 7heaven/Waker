/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.util;

import com.cfm.waker.dao.WakerPreferenceManager;

import android.content.Context;

public class DensityUtil {

	public static float dip2px(Context context, float dipValue) {
		final float scale = WakerPreferenceManager.getInstance(context).getScreenDensity();

		return dipValue * scale;
	}

	public static float px2dip(Context context, float pxValue) {
		final float scale = WakerPreferenceManager.getInstance(context).getScreenDensity();

		return pxValue / scale;
	}

}
