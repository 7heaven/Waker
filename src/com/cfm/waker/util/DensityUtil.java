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

	public static int dip2px(Context context, float dipValue) {
		final float scale = WakerPreferenceManager.getInstance(context).getScreenDensity();

		return (int) (dipValue * scale + 0.5F);
	}

	public static int px2dip(Context context, float pxValue) {
		final float scale = WakerPreferenceManager.getInstance(context).getScreenDensity();

		return (int) (pxValue / scale + 0.5F);
	}

}
