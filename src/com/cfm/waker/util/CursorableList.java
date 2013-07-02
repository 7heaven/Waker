/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.util;

import java.util.ArrayList;
import java.util.Collection;

public class CursorableList<E> extends ArrayList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2081417782380614153L;
	
	private int cursor;
	
	public CursorableList(){
		super();
	}
	
	public CursorableList(int capicity){
		super(capicity);
	}
	
	public CursorableList(Collection<? extends E> collection){
		super(collection);
	}
	
    public boolean moveToFirst(){
    	if(size() > 0){
    		cursor = 0;
    		return true;
    	}
    	return false;
    }
    
    public boolean moveToLast(){
    	if(size() > 0){
    		cursor = size() - 1;
    		return true;
    	}
    	
    	return false;
    }
    
    public boolean hasNext(){
    	return cursor < size() - 1;
    }
    
    public boolean moveToNext(){
    	if(hasNext()){
    		cursor++;
    		return true;
    	}
    	
    	return false;
    }

    public E get(){
    	return super.get(cursor);
    }
}
