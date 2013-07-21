/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.theme;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ThemeManager{
	
	private Context context;
	private static ThemeManager instance;
	
	private int currentColor = -1;
	private Drawable currentBackground;
	private String currentResourcesPath;
	
	private ArrayList<ThemeEnable> themeObjectList;
	
    public ThemeManager(Context context){
    	this.context = context;
    	themeObjectList = new ArrayList<ThemeEnable>();
    }
    
    public static ThemeManager getInstance(Context context){
    	if(null == instance){
    		instance = new ThemeManager(context);
    	}
    	
    	return instance;
    }
    
    //register ThemeEnable object so that the ThemeManager can manage theme of the ThemeEnable object
    public void registerThemeObject(ThemeEnable... themeEnable){
    	int i = 0;
    	do{
    		
    		themeObjectList.add(themeEnable[i]);
    		setCurrent(themeEnable[i]);
    		
    	}while(++i < themeEnable.length);
    }
    
    //remove ThemeEnable object if theme managements of this object are no longer required;
    public void unregisterThemeObject(ThemeEnable... themeEnable){
    	int i = 0;
    	do{
    		themeObjectList.remove(themeEnable[i]);
    	}while(++i < themeEnable.length);
    }
    
    public void setThemeColor(int color){
    	currentColor = color;
    	if(themeObjectList.size() > 0){
    		int i = 0;
    		do{
    			themeObjectList.get(i).setThemeColor(color);
    		}while(++i < themeObjectList.size());
    	}
    }
    
    public void setThemeBackground(Drawable background){
    	currentBackground = background;
    	if(themeObjectList.size() > 0){
    		int i = 0;
    		do{
    			themeObjectList.get(i).setThemeBackground(background);
    		}while(++i < themeObjectList.size());
    	}
    }
    
    public void setThemeResources(String path){
    	currentResourcesPath = path;
    	if(themeObjectList.size() > 0){
    		int i = 0;
    		do{
    			themeObjectList.get(i).setThemeResources(path);
    		}while(++i < themeObjectList.size());
    	}
    }
    
    public void setCurrent(ThemeEnable themeEnable){
    	if(currentColor != -1) themeEnable.setThemeColor(currentColor);
    	if(null != currentBackground) themeEnable.setThemeBackground(currentBackground);
    	if(null != currentResourcesPath) themeEnable.setThemeResources(currentResourcesPath);
    }
    
    public void setCurrent(){
    	if(currentColor != -1) setThemeColor(currentColor);
    	if(null != currentBackground) setThemeBackground(currentBackground);
    	if(null != currentResourcesPath) setThemeResources(currentResourcesPath);
    }
}
