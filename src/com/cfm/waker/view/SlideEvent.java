/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.view;

public class SlideEvent {

	private float startX,startY;
	private float x,y;
	private float dx,dy;

	
	public static final int                  TOUCHMODE_IDLE = 0x00000000;
	public static final int                  TOUCHMODE_DOWN = 0x00000001;
	
	public static final int              TOUCHMODE_DRAGGING = 0x00000002;
	public static final int TOUCHMODE_DRAGGING_HORIZONTALLY = 0x00000012;
	public static final int   TOUCHMODE_DRAGGING_VERTICALLY = 0x00000022;
	
	public static final int        TOUCHMODE_DRAGGING_START = 0x00000003;
	public static final int      TOUCHMODE_HORIZONTAL_START = 0x00000013;
	public static final int        TOUCHMODE_VERTICAL_START = 0x00000023;
	
	public static final int  TOUCHMODE_DIRECTION_HORIZONTAL = 0x00000010;
	public static final int    TOUCHMODE_DIRECTION_VERTICAL = 0x00000020;
	
	//this mask is use to ignore direction when trying to detect a dragging action
	public static final int         TOUCHMODE_DRAGGING_MASK = 0x0000000F;
	//this mask is use to ignore a dragging action that whether it just started or dragging is on going when trying to detect dragging direction 
	public static final int        TOUCHMODE_DIRECTION_MASK = 0x000000F0;
	
	private int touchMode;
	private int previouslyMode;
	
	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}
	
	public void setStartX(float startX){
		this.startX = startX;
	}
	
	public float getStartX(){
		return startX;
	}
	
	public void setStartY(float startY){
		this.startY = startY;
	}
	
	public float getStartY(){
		return startY;
	}
	
	public float getX(){
		return x;
	}
	
	public void setX(float f){
		this.x = f;
	}
	
	public float getY(){
		return y;
	}
	
	public void setY(float y){
		this.y = y;
	}
	
	public int getAction(){
		return touchMode;
	}
	
	public int getPreviouslyAction(){
		return previouslyMode;
	}
	
	public void setAction(int touchMode){
		if(null != (Integer) touchMode) previouslyMode = this.touchMode;
		this.touchMode = touchMode;
	}
}
