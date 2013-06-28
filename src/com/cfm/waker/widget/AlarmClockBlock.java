/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.widget;

import java.lang.Runnable;
import java.util.Calendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.cfm.waker.R;
import com.cfm.waker.dao.WakerDatabaseHelper;
import com.cfm.waker.entity.Alarm;
import com.cfm.waker.util.DensityUtil;
import com.cfm.waker.view.SlideEvent;
import com.cfm.waker.widget.base.BaseSlideWidget;

public class AlarmClockBlock extends BaseSlideWidget {
	
	private static final String TAG = "AlarmClockBlock";
	
	private Context context;
	
	private Alarm alarm;
	
	private int width, height;
	private int centerX, centerY;
	private int radius;
	private float moveX, moveY;
	
	private boolean enabled;
	
	private Paint paint;
	private TextPaint textPaint;
	
	private int color;
	
	private Rect bound;
	private Rect bound2;
	
	private Handler handler;
	private MyRunnable runnable;
	
	private OnStateChangeListener onStateChangeListener;
	
	public interface OnStateChangeListener{
		public void onStateChanged(long id, boolean isEnabled);
	}
	
	public AlarmClockBlock(Context context){
		this(context, null);
	}
	
	public AlarmClockBlock(Context context, AttributeSet attrs){
		this(context, attrs, 0);
	}
	
	public AlarmClockBlock(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		
		this.context = context;
		
		enabled = false;
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AlarmClockBlock);
		if(null != ta.getString(R.styleable.AlarmClockBlock_font) && !isInEditMode()){
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), ta.getString(R.styleable.AlarmClockBlock_font));
			textPaint.setTypeface(typeface);
		}
		
		color = ta.getColor(R.styleable.AlarmClockBlock_color, 0xFFFFFFFF);
		
		ta.recycle();
		
		textPaint.setColor(0xFFFFFFFF);
		
		bound = new Rect();
		bound2 = new Rect();
		
	}
	
	public void setAlarm(Calendar calendar, boolean is24Format){
		alarm = new Alarm(calendar, is24Format);
		enabled = alarm.isEnabled();
	}
	
	public void setAlarm(Alarm alarm){
		this.alarm = alarm;
		enabled = alarm.isEnabled();
	}
	
	@Override
	public boolean onSlideEvent(SlideEvent event){
		switch(event.getAction()){
		case SlideEvent.TOUCHMODE_DOWN:
			if(event.getStartY() > height) return false;
			break;
		case SlideEvent.TOUCHMODE_VERTICAL_START:
			getParent().requestDisallowInterceptTouchEvent(true);
			break;
		case SlideEvent.TOUCHMODE_DRAGGING_VERTICALLY:
			moveX = centerX;
			moveY = centerY - event.getStartY() + event.getY();
			if(event.getDy() > 0 && event.getDy() < DensityUtil.dip2px(context, 80)){
				invalidate();
			}else if(event.getDy() > DensityUtil.dip2px(context, 80)){
				event.setAction(SlideEvent.TOUCHMODE_IDLE);
				enabled = !enabled;
				alarm.setEnabled(enabled);
				WakerDatabaseHelper.getInstance(context).updateAlarm(alarm.getId(), alarm);
				if(null != onStateChangeListener) onStateChangeListener.onStateChanged(alarm.getId(), enabled);
				returnToOriginalSpot(event);
			}
			break;
		case SlideEvent.TOUCHMODE_IDLE:
			returnToOriginalSpot(event);
			break;
		}
		
		return true;
	}
	
	/*
	@Override
	public boolean onTouchEvent(MotionEvent event){
		switch(event.getAction() & MotionEvent.ACTION_MASK){
		case MotionEvent.ACTION_DOWN:
			Log.d(TAG,"MOTIONEVENT_DOWN");
			dx = centerX - event.getX();
			dy = centerY - event.getY();
			
		    if(event.getY() < height){
		    	touchMode = TOUCHMODE_DOWN;
			    return true;
		    }
			
		case MotionEvent.ACTION_MOVE:
			Log.d(TAG,"MOTIONEVENT_MOVE" + moveY);
			switch(touchMode){
			case TOUCHMODE_IDLE:
				break;
			case TOUCHMODE_DOWN:
				if(Math.abs(centerX - dx - event.getX()) < Math.abs(centerY - dy - event.getY())){
					touchMode = TOUCHMODE_DRAGGING;
					getParent().requestDisallowInterceptTouchEvent(true);
				}
				break;
			case TOUCHMODE_DRAGGING:
				moveX = centerX;
				moveY = dy + event.getY();
				if(moveY > centerY && moveY <= centerY + DensityUtil.dip2px(context, 80)){
					invalidate();
				}else if(moveY > centerY + DensityUtil.dip2px(context, 80)){
					touchMode = TOUCHMODE_IDLE;
					enabled = !enabled;
					alarm.setEnabled(enabled);
					WakerDatabaseHelper.getInstance(context).updateAlarm(alarm.getId(), alarm);
					if(null != onStateChangeListener) onStateChangeListener.onStateChanged(alarm.getId(), enabled);
					returnToOriginalSpot();
				}
				
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			Log.d(TAG,"MOTIONEVENT_UP & MOTIONEVENT_CANCEL");
			touchMode = TOUCHMODE_IDLE;
			returnToOriginalSpot();
			break;
		}
		
		return true;
	}
	 */
	
	private void returnToOriginalSpot(SlideEvent event){
		handler = new Handler();
		runnable = new MyRunnable(event);
		
		handler.post(runnable);
		
	}
	
	private class MyRunnable implements Runnable{
		
		private SlideEvent event;
		
		public MyRunnable(SlideEvent event){
			this.event = event;
		}
		
		@Override
		public void run(){
			if(event.getAction() == SlideEvent.TOUCHMODE_IDLE && moveY != centerY){
				moveY += (centerY - moveY) * 0.6F;
				invalidate();
				
				handler.postDelayed(runnable, 20);
			}
		}
	}
	
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		width = getMeasuredWidth();
		height = getMeasuredHeight();
		
		centerX = width / 2;
		centerY = height / 2;
		
		radius = (int) (height * 0.4F);
	    
		//expand height to double size of the original height so it can leave room for slide action
	    super.setMeasuredDimension(width, height * 2);
	}
	
	@Override
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		moveX = moveX == 0 ? centerX : moveX;
		moveY = moveY == 0 ? centerY : moveY;
		
		/*
		paint.setColor(0xFF060612);
		paint.setStyle(Paint.Style.FILL);
		bound.top = 0;
		bound.left = 0;
		bound.right = width;
		bound.bottom = height;
		canvas.drawRect(bound, paint);
		//for background
		paint.setColor(0xFF363745);
		bound.top = (int) (moveY - centerY);
		bound.left = (int) (moveX - centerX);
		bound.right = (int) (moveX + centerX);
		bound.bottom = (int) (moveY + centerY);
		canvas.drawRect(bound, paint);
		*/
		
		paint.setColor(enabled ? color : 0xFF999999);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(DensityUtil.dip2px(context, 2));
		canvas.drawCircle(moveX, moveY, radius, paint);
		String text = alarm.getFormatedTime();
		if(null != text){
			textPaint.setTextSize(width * 0.25F);
			textPaint.getTextBounds(text, 0, text.length(), bound);
			
			canvas.drawText(text, moveX - bound.width() / 2, moveY + bound.height() / 2, textPaint);
			if(!alarm.is24Format()){
				text = alarm.getAmpm();
				textPaint.setTextSize(textPaint.getTextSize() / 2);
				textPaint.getTextBounds(text, 0, text.length(), bound2);
				
				canvas.drawText(text, moveX - bound2.width() / 2, moveY + bound.height() / 2 + bound2.height() + DensityUtil.dip2px(context, 5), textPaint);
			}
		}
	}
}
