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
    public void registerThemeObject(ThemeEnable themeEnable){
    	themeObjectList.add(themeEnable);
    }
    
    //remove ThemeEnable object if theme managements of this object are no longer required;
    public void unregisterThemeObject(ThemeEnable themeEnable){
    	themeObjectList.remove(themeEnable);
    }
    
    public void setThemeColor(int color){
    	if(themeObjectList.size() > 0){
    		int i = 0;
    		do{
    			themeObjectList.get(i).setThemeColor(color);
    		}while(++i < themeObjectList.size());
    	}
    }
    
    public void setThemeBackground(Drawable background){
    	if(themeObjectList.size() > 0){
    		int i = 0;
    		do{
    			themeObjectList.get(i).setThemeBackground(background);
    		}while(++i < themeObjectList.size());
    	}
    }
    
    public void setThemeResources(String path){
    	if(themeObjectList.size() > 0){
    		int i = 0;
    		do{
    			themeObjectList.get(i).setThemeResources(path);
    		}while(++i < themeObjectList.size());
    	}
    }
}
