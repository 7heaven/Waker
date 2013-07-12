/*
 * Waker project 2013
 * 
 * folks studio
 * 
 * by caifangmao8@gmail.com
 */
package com.cfm.waker.view;

import java.util.ArrayList;

import com.cfm.waker.log.WLog;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector implements SensorEventListener {
	
	private static final String TAG = "ShakeDetector";
	
	private long mLastUpdateTime;
	
	private float mLastX, mLastY, mLastZ;
	private Context mContext;
	private SensorManager mSensorManager;
	private ArrayList<OnShakeListener> mListeners;
	
	private static final int SHAKE_THRESHOLD = 800;
	
	public ShakeDetector(Context context){
		mContext = context;
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mListeners = new ArrayList<OnShakeListener>();
	}
	
	public interface OnShakeListener{
		
		void onShake(float speed);
		void onStable();
	}
	
	public void registerOnShakeListener(OnShakeListener listener){
		if(mListeners.contains(listener))
			return;
		mListeners.add(listener);
	}
	
	public void unregisterOnShakeListener(OnShakeListener listener){
		mListeners.remove(listener);
	}
	
	public void start(){
		if(null == mSensorManager){
			return;
			//throw new UnsupportedOperationException();
		}
		
		Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		if(null == sensor){
			return;
			//throw new UnsupportedOperationException();
		}
		
		boolean success = mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
		if(!success){
			return;
			//throw new UnsupportedOperationException();
		}
	}
	
	public void stop(){
		if(null != mSensorManager){
			mSensorManager.unregisterListener(this);
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy){
		
	}
	
	@Override
	public void onSensorChanged(SensorEvent event){
		long curTime = System.currentTimeMillis();
		if((curTime - mLastUpdateTime) > 100){
			long diffTime = curTime - mLastUpdateTime;
			mLastUpdateTime = curTime;
			
			float x = event.values[0];
			float y = event.values[1];
			float z = event.values[2];
			
			float speed = Math.abs(x + y + z - mLastX - mLastY - mLastZ) / diffTime * 10000;
			
			if(speed > SHAKE_THRESHOLD){
				WLog.print(TAG, "shake detected w/ speed: " + speed);
				this.notifyListenersShake(speed);
			}else{
				this.notifyListenersNotShake();
			}
			
			mLastX = x;
			mLastY = y;
			mLastZ = z;
		}
		
	}
	
	private void notifyListenersShake(float speed){
		for(OnShakeListener listener : mListeners){
			listener.onShake(speed);
		}
	}
	
	private void notifyListenersNotShake(){
		for(OnShakeListener listener : mListeners){
			listener.onStable();
		}
	}
}