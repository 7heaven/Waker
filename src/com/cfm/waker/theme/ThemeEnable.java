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
	
	public void setThemeColor(int color);
	public int getThemeColor();
	
	public void setBackground(Drawable drawable);
	public Drawable getBackground();
	
	public void setThemeResources(String path);
	public String getThemeResources();
}
