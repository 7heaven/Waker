/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.theme;

import android.graphics.drawable.Drawable;

/**
 * implement this interface to enable Theme change;
 *
 */
public interface ThemeEnable {
	
	//main Theme color
	public void setThemeColor(int color);
	
	//main Theme background
	public void setThemeBackground(Drawable drawable);
	
	//set other resources if requires
	public void setThemeResources(String path);
}
