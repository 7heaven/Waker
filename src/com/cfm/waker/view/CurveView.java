package com.cfm.waker.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.cfm.waker.R;

public class CurveView extends View {

	private static final String TAG = "CurveView";
	
	public static final int   MODE_LEFT = 0x00001000;
	public static final int    MODE_TOP = 0x00000100;
	public static final int  MODE_RIGHT = 0x00000010;
	public static final int MODE_BOTTOM = 0x00000001;
	
	private int mode = MODE_TOP;
	
	private int width,height;
	
	private Paint paint;
	private Path path;
	
	private float offset;
	
	public CurveView(Context context){
		this(context, null);
	}
	
	public CurveView(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public CurveView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CurveView);
		paint.setColor(ta.getColor(R.styleable.CurveView_color, 0xFFEEEEEE));
		ta.recycle();
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		path = new Path();
	}
	
	public void setMode(int mode){
		this.mode = mode;
		invalidate();
	}
	
	public int getMode(){
		return mode;
	}
	
	public void setOffset(int offset){
		this.offset = offset;
		invalidate();
	}
	
	@Override
	public void onDraw(Canvas canvas){
		float bottomLeftX = 0;
        float bottomLeftY = height;
		float topLeftX = 0;
		float topLeftY = 0;
		float topRightX = width;
		float topRightY = 0;
		float bottomRightX = width;
		float bottomRightY = height;
		
		if((mode & MODE_LEFT) != 0){
			bottomLeftX = offset;
			topLeftX = offset;
		}
		if((mode & MODE_TOP) != 0){
			topLeftY = offset;
			topRightY = offset;
		}
	    if((mode & MODE_RIGHT) != 0){
	    	topRightX = width - offset;
	    	bottomRightX = width - offset;
	    }
	    if((mode & MODE_BOTTOM) != 0){
	    	bottomRightY = height - offset;
	    	bottomLeftY = height - offset;
	    }
	    
	    path.moveTo(bottomLeftX, bottomLeftY);
	    if(topLeftX != 0 ){
	    	path.quadTo(0, height / 2, topLeftX, topLeftY);
	    }else{
	    	path.lineTo(topLeftX, topLeftY);
	    }
	    if(topRightY != 0){
	    	path.quadTo(width / 2, 0, topRightX, topRightY);
	    }else{
	    	path.lineTo(topRightX, topRightY);
	    }
	    if(bottomRightX != width){
	    	path.quadTo(width, height / 2, bottomRightX, bottomRightY);
	    }else{
	    	path.lineTo(bottomRightX, bottomRightY);
	    }
	    if(bottomLeftY != height){
	    	path.quadTo(width / 2, height, bottomLeftX, bottomLeftY);
	    }else{
	    	path.lineTo(bottomLeftX, bottomLeftY);
	    }
	    
	    canvas.drawPath(path, paint);
	}
}
