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
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.cfm.waker.R;
import com.cfm.waker.dao.WakerDatabaseHelper;
import com.cfm.waker.entity.Alarm;
import com.cfm.waker.view.RowBlock;
import com.cfm.waker.view.SlideEvent;
import com.cfm.waker.widget.base.BaseSlideWidget;

public class AlarmClockBlock extends BaseSlideWidget {
	
	private static final String TAG = "AlarmClockBlock";
	
	private Context context;
	
	private Alarm alarm;
	
	public static final int MODE_NORMAL = 0;
	public static final int MODE_DELETE = 1;
	
	private int mode = MODE_NORMAL;
	
	private int width, height;
	private int centerX, centerY;
	private int radius;
	private float moveX, moveY;
	private int intrinsicHeight;
	
	private boolean enabled;
	private boolean trans = false;
	private int transN = 0;
	
	private Paint paint;
	private TextPaint textPaint;
	
	private int color;
	private int textColor;
	
	private boolean isInInitMovement;
	private float initMovementProcedure;
	
	private Rect bound;
	private Rect bound2;
	private RectF arcBound;
	
	private Handler handler;
	private MyRunnable runnable;
	private InitRunnable initRunnable;
	private DelRunnable delRunnable;
	
	private OnStateChangedListener onStateChangedListener;
	private OnPerformListener onPerformListener;
	
	public interface OnStateChangedListener{
		public void onStateChange(long id, boolean isEnabled);
	}
	
	public interface OnPerformListener{
		public void onInitFinish();
		public void onDelFinish();
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
		
		intrinsicHeight = context.getResources().getDimensionPixelOffset(R.dimen.alarmclockblock_height);
		
		enabled = false;
		isInInitMovement = false;
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AlarmClockBlock);
		if(null != ta.getString(R.styleable.AlarmClockBlock_font) && !isInEditMode()){
			Typeface typeface = Typeface.createFromAsset(context.getAssets(), ta.getString(R.styleable.AlarmClockBlock_font));
			textPaint.setTypeface(typeface);
		}
		
		color = ta.getColor(R.styleable.AlarmClockBlock_color, 0xFFFFFFFF);
		textColor = ta.getColor(R.styleable.AlarmClockBlock_textColor, 0xFFFFFFFF);
		
		ta.recycle();
		
		textPaint.setColor(textColor);
		
		bound = new Rect();
		bound2 = new Rect();
		
		handler = new Handler();
	}
	
	public void setOnStateChangedListener(OnStateChangedListener onStateChangedListener){
		this.onStateChangedListener = onStateChangedListener;
	}
	
	public OnStateChangedListener getOnStateChangedListener(){
		return onStateChangedListener;
	}
	
	public void setOnPerformListener(OnPerformListener onPerformListener){
		this.onPerformListener = onPerformListener;
	}
	
	public OnPerformListener getPerformListener(){
		return onPerformListener;
	}
	
	public void setAlarm(Calendar calendar, boolean is24Format){
		alarm = new Alarm(calendar, is24Format);
		enabled = alarm.isEnabled();
	}
	
	public void setAlarm(Alarm alarm){
		this.alarm = alarm;
		enabled = alarm.isEnabled();
	}
	
	public Alarm getAlarm(){
		return alarm;
	}
	
	@Override
	public boolean onSlideEvent(SlideEvent event){
		switch(event.getAction()){
		case SlideEvent.TOUCHMODE_DOWN:
			if(event.getStartY() > height) return false;
			break;
		case SlideEvent.TOUCHMODE_VERTICAL_START:
			getParent().requestDisallowInterceptTouchEvent(true);
			mode = MODE_NORMAL;
			break;
		case SlideEvent.TOUCHMODE_DRAGGING_VERTICALLY:
			moveX = centerX;
			moveY = centerY - event.getStartY() + event.getY();
			if(event.getDy() > 0 && event.getDy() < intrinsicHeight){
				invalidate();
			}else if(event.getDy() > intrinsicHeight){
				event.setAction(SlideEvent.TOUCHMODE_IDLE);
				enabled = !enabled;
				alarm.setEnabled(enabled);
				WakerDatabaseHelper.getInstance(context).updateAlarm(alarm.getId(), alarm);
				if(null != onStateChangedListener) onStateChangedListener.onStateChange(alarm.getId(), enabled);
				returnToOriginalSpot(event);
			}
			break;
		case SlideEvent.TOUCHMODE_IDLE:
			returnToOriginalSpot(event);
			break;
		}
		
		return true;
	}
	
	private void returnToOriginalSpot(SlideEvent event){
		runnable = new MyRunnable(event);
		
		handler.post(runnable);
		
	}
	
	public void prepareForInitMovement(){
		handler.removeCallbacks(initRunnable);
		handler.removeCallbacks(delRunnable);
		
		initMovementProcedure = 0F;
		isInInitMovement = true;
		arcBound = new RectF(moveX - radius, moveY - radius, moveX + radius, moveY + radius);
		
		invalidate();
	}
	
	public void performInitMovement(){
		if(isInInitMovement){
			initRunnable = new InitRunnable();
			handler.post(initRunnable);
		}
	}
	
	public void prepareForDelMovement(){
		handler.removeCallbacks(delRunnable);
		
		initMovementProcedure = 1F;
		isInInitMovement = true;
		arcBound = new RectF(moveX - radius, moveY - radius, moveX + radius, moveY + radius);
		
		invalidate();
	}
	
	public void performDelMovement(){
		if(isInInitMovement){
			delRunnable = new DelRunnable();
			handler.post(delRunnable);
		}
	}
	
	public void setMode(int mode){
		this.mode = mode;
	}
	
	public int getMode(){
		return mode;
	}
	
	@Override
	public boolean performClick(){
		if(mode == MODE_DELETE){
			handler.removeCallbacks(delRunnable);
			handler.removeCallbacks(initRunnable);
			((RowBlock) getParent().getParent()).performAlarmDelete(((RowBlock) getParent().getParent()).getItemPositionById(alarm.getId()));
			mode = MODE_NORMAL;
		}
		return super.performClick();
	}
	
	@Override
	public boolean performLongClick(){
		mode = MODE_DELETE;
		return super.performLongClick();
	}
	
	private class InitRunnable implements Runnable{
		
		public InitRunnable(){
			handler.removeCallbacks(delRunnable);
		}
		
		@Override
		public void run(){
			if(initMovementProcedure < 1F){
				initMovementProcedure += 0.1F;
				invalidate();
				
				handler.postDelayed(initRunnable, 20);
			}else{
				isInInitMovement = false;
				if(null != onPerformListener) onPerformListener.onInitFinish();
			}
		}
	}
	
	private class DelRunnable implements Runnable{
		
		public DelRunnable(){
			handler.removeCallbacks(initRunnable);
		}
		
		@Override
		public void run(){
			if(initMovementProcedure > 0F){
				initMovementProcedure -= 0.1F;
				invalidate();
				
				handler.postDelayed(delRunnable, 20);
			}else{
				if(null != onPerformListener) onPerformListener.onDelFinish();
			}
		}
	}
	
	private class MyRunnable implements Runnable{
		
		private SlideEvent event;
		
		public MyRunnable(SlideEvent event){
			this.event = event;
		}
		
		@Override
		public void run(){
			if(event.getAction() == SlideEvent.TOUCHMODE_IDLE && moveY != centerY){
				moveY += (centerY - moveY) * 0.3F;
				invalidate();
				
				handler.postDelayed(runnable, 20);
			}
		}
	}
	
	public void setColor(int color){
		this.color = color;
		invalidate();
	}
	
	public int getColor(){
		return color;
	}
	
	public void setToNormal(){
		initMovementProcedure = 0F;
		isInInitMovement = false;
		invalidate();
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
		
		if(mode == MODE_DELETE){
			if(trans){
				canvas.translate(--transN, 0);
				if(transN < -5) trans = false;
			}else{
				canvas.translate(++transN, 0);
				if(transN > 5) trans = true;
			}
		}
		
		paint.setColor(enabled ? color : 0xFF999999);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(context.getResources().getDimension(R.dimen.alarmclockblock_paint_stroke_width));
		if(isInInitMovement){
			canvas.drawArc(arcBound, 0, 360 * initMovementProcedure, false, paint);
		}else{
			canvas.drawCircle(moveX, moveY, radius, paint);
		}
		String text = alarm.getFormatedTime();
		if(null != text){
			textPaint.setTextSize(width * 0.25F);
			textPaint.getTextBounds(text, 0, text.length(), bound);
			
			int offset = 0;
			if(isInInitMovement){
				offset = bound.height() - (int) (bound.height() * initMovementProcedure);
				textPaint.setAlpha((int) (255 * initMovementProcedure));
			}else{
				textPaint.setAlpha(255);
			}
			
			canvas.drawText(text, moveX - bound.width() / 2, moveY + bound.height() / 2 - offset, textPaint);
			if(!alarm.is24Format()){
				text = alarm.getAmpm();
				textPaint.setTextSize(textPaint.getTextSize() / 2);
				textPaint.getTextBounds(text, 0, text.length(), bound2);
				
				canvas.drawText(text, moveX - bound2.width() / 2, moveY + bound.height() / 2 + bound2.height() + context.getResources().getDimension(R.dimen.alarmclockblock_text_bound_offset) - offset, textPaint);
			}
		}
	}
}
