package com.cfm.waker.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.cfm.waker.entity.Alarm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WakerDatabaseHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "WakerDatabaseHelper";
	
	private static WakerDatabaseHelper instance;
	
	//private Context context;
	
	private static final String DATABASE_NAME = "waker.db";
	private static final String    TABLE_NAME = "alarm";
	private static final int DATABASE_VERSION = 2;
	
	public WakerDatabaseHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//this.context = context;
	}
	
	public static WakerDatabaseHelper getInstance(Context context){
		if(null == instance){
			instance = new WakerDatabaseHelper(context);
		}
		
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db){
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
	                Alarm.Columns.ID + " INTEGER PRIMARY KEY," + 
				    Alarm.Columns.HOUR + " INTEGER, " +
	                Alarm.Columns.MINUTE + " INTEGER, " +
	                Alarm.Columns.SNOOZE_TIME + " INTEGER, " +
	                Alarm.Columns.ENABLED + " INTEGER, " +
	                Alarm.Columns.VIBRATE + " INTEGER, " +
	                Alarm.Columns.RINGTONE + " TEXT, " +
	                Alarm.Columns.DAYS_OF_WEEK + " INTEGER, " +
	                Alarm.Columns.MESSAGE + " TEXT);");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion){
		Log.d(TAG, "Upgrade waker database from" + oldVersion + "to" + "currentVerion");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
		
	}
	
	public List<Alarm> getAlarms(boolean is24Format){
		SQLiteDatabase db = getReadableDatabase();
		String[] columns = {Alarm.Columns.ID,
				            Alarm.Columns.HOUR,
				            Alarm.Columns.MINUTE,
				            Alarm.Columns.SNOOZE_TIME,
				            Alarm.Columns.ENABLED,
				            Alarm.Columns.VIBRATE,
				            Alarm.Columns.RINGTONE,
				            Alarm.Columns.DAYS_OF_WEEK,
				            Alarm.Columns.MESSAGE};
		
		Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, Alarm.Columns.ID + " ASC");
		
		List<Alarm> alarms = null;
		
		if(cursor.moveToFirst()){
			alarms = new ArrayList<Alarm>();
			do{
				alarms.add(getAlarmByCursor(cursor, is24Format));
			}while(cursor.moveToNext());
			
		}
		
		
		cursor.close();
		return alarms;
	}
	
	private Alarm getAlarmByCursor(Cursor cursor, boolean is24Format){
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(cursor.getLong(0));
		Alarm alarm = new Alarm(calendar, is24Format);
		alarm.setHour(cursor.getInt(1));
		alarm.setMinute(cursor.getInt(2));
		alarm.setSnoozeTime(cursor.getInt(3));
		alarm.setEnabled(cursor.getInt(4) == 1 ? true : false);
		alarm.setVibrate(cursor.getInt(5) == 1 ? true : false);
		alarm.setRingtone(cursor.getString(6));
		alarm.setWeek(cursor.getInt(7));
		alarm.setMessage(cursor.getString(8));
		
		return alarm;
	}
	
	public void insertAlarm(Alarm alarm){
		/*
		ContentValues values = createContentValues(alarm);
		
		SQLiteDatabase db = getWritableDatabase();
		
		return db.insert(TABLE_NAME, null, values);
		 */
		
		ContentValues values = createContentValues(alarm);
		
		SQLiteDatabase db = getWritableDatabase();
		
		int hour = (Integer) values.get(Alarm.Columns.HOUR);
		int minute = (Integer) values.get(Alarm.Columns.MINUTE);
		
		String sql = "SELECT " + Alarm.Columns.ID + " FROM " +
		             TABLE_NAME + " WHERE " + Alarm.Columns.HOUR
		             + "=? AND " + Alarm.Columns.MINUTE + "=?";
		
		String[] selectionArgs = {hour + "", minute + ""};
		
		final Cursor cursor = db.rawQuery(sql, selectionArgs);
		if(cursor.moveToFirst()){
			updateAlarm(cursor.getLong(0), alarm);
		}else{
			db.insert(TABLE_NAME, null, values);
		}
		
		cursor.close();
	}
	
	public long updateAlarm(long id, Alarm alarm){
		ContentValues values = createContentValues(alarm);
		
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = Alarm.Columns.ID + "=?";
		String[] whereArgs = {Long.toString(id)};
		
		return db.update(TABLE_NAME, values, whereClause, whereArgs);
	}
	
	public long deleteAlarm(long id){
		SQLiteDatabase db = getWritableDatabase();
		String whereClause = Alarm.Columns.ID + "=?";
		String[] whereArgs = {Long.toString(id)};
		
		return db.delete(TABLE_NAME, whereClause, whereArgs);
	}
	
	public ContentValues createContentValues(Alarm alarm){
		ContentValues cv = new ContentValues();
		cv.put(Alarm.Columns.ID, alarm.getCalendar().getTimeInMillis());
		cv.put(Alarm.Columns.HOUR, alarm.getHour());
		cv.put(Alarm.Columns.MINUTE, alarm.getMinute());
		cv.put(Alarm.Columns.SNOOZE_TIME, alarm.getSnoozeTime());
		cv.put(Alarm.Columns.ENABLED, alarm.isEnabled());
		cv.put(Alarm.Columns.VIBRATE, alarm.isVibrate());
		cv.put(Alarm.Columns.RINGTONE, alarm.getRingtone());
		cv.put(Alarm.Columns.DAYS_OF_WEEK, alarm.getWeek());
		cv.put(Alarm.Columns.MESSAGE, alarm.getMessage());
		
		return cv;
		
	}
	
}
