package trongtuyen.txalarm.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import trongtuyen.txalarm.Alarm;

/**
 * Created by trong on 07-Dec-16.
 */

public class AlarmDatabase extends SQLiteOpenHelper{
    public static final String DB_NAME ="txalarm.database";
    public static final String TABLE_ALARM ="tb_alarm";
    public static final String ID_ALARM = "id";
    public static final String HOUR_ALARM = "hour";
    public static final String MINUTE_ALARM = "minute";
    public static final String STATUS_ALARM = "status";
    public static final String SOUND_ALARM = "sound";
    public static final String GAME_ALARM = "game";
    public static final String PREVOL_ALARM = "prevol";
    public static final String VOL_ALARM = "volume";
    public static final String FLAG_NEED_AFD = "flag";



    public static final int DB_VERSION = 1;

    public static final String CREATE_TODO =
            "CREATE TABLE " + TABLE_ALARM+ "(" +
                    ID_ALARM + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL" +
                    ", " + HOUR_ALARM + " TEXT NOT NULL" +
                    "," + MINUTE_ALARM + " TEXT NOT NULL" +
                    "," + STATUS_ALARM + " TEXT NOT NULL" +
                    "," + GAME_ALARM + " TEXT NOT NULL" +
                    "," + SOUND_ALARM + " TEXT NOT NULL" +
                    "," + PREVOL_ALARM + " TEXT NOT NULL" +
                    "," + VOL_ALARM + " TEXT NOT NULL" +
                    "," + FLAG_NEED_AFD + " TEXT NOT NULL" +
                    ")";
    //SQLite
    private SQLiteDatabase db;
    public AlarmDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            db.execSQL(CREATE_TODO);
        }catch(SQLException error){
            error.printStackTrace();
        }

    }

    //Dùng để update phiên bản
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Mở DB
    public void open(){
        try{
            db = getWritableDatabase();
        }catch (SQLException error) {
            error.printStackTrace();
        }
    }

    //Đóng DB
    public void close(){
        if(db!=null && db.isOpen()){
            try {
                db.close();
            } catch (SQLException error) {
                error.printStackTrace();
            }
        }
    }

    //Các hàm truy vấn DB
    //Lấy tất cả các hàng rồi di chuyển về hàng đầu tiên
    public Cursor loadAll(String sql)
    {
        open();
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        close();
        return cursor;
    }

    //Thêm contentvalue
    public long insert(String table, ContentValues values){
        open();
        long index = db.insert(table, null, values);
        close();
        return index;
    }

    //Xóa contentvalue
    public boolean delete(String table, String where){
        open();
        long index = db.delete(table, where, null);
        close();
        return index > 0;
    }

    //update contentvalue
    public boolean update(String table, ContentValues values, String where){
        open();
        long index = db.update(table, values, where, null);
        close();
        return index > 0;
    }


    //Các hàm xử lý Alarm
    //Convert cursor thành 1 alarm
    //#Tham số: Cursor
    //#Trả về: Alarm
    private Alarm cursorToAlarm(Cursor cursor){
        Log.e("fe","public Alarm getAlarm(String sql){");
        Alarm alarm = new Alarm();
        alarm.setId(cursor.getInt(cursor.getColumnIndex(ID_ALARM)))
                .setHour(cursor.getInt(cursor.getColumnIndex(HOUR_ALARM)))
                .setMin(cursor.getInt(cursor.getColumnIndex(MINUTE_ALARM)))
                .setStatus(cursor.getInt(cursor.getColumnIndex(STATUS_ALARM)))
                .setGameType(cursor.getInt(cursor.getColumnIndex(GAME_ALARM)))
                .setSound(cursor.getString(cursor.getColumnIndex(SOUND_ALARM)))
                .setPreVol(cursor.getInt(cursor.getColumnIndex(PREVOL_ALARM)))
                .setVolume(cursor.getFloat(cursor.getColumnIndex(VOL_ALARM)))
                .setFlag(cursor.getInt(cursor.getColumnIndex(FLAG_NEED_AFD)));
        return alarm;
    }

    //Convert alarm thành content value
    //#Tham số: Alarm
    //#Trả về: ContentValues
    private ContentValues  alarmToValues(Alarm alarm){
        ContentValues values = new ContentValues();
        values.put(HOUR_ALARM, alarm.getHour());
        values.put(MINUTE_ALARM, alarm.getMin());
        values.put(STATUS_ALARM, alarm.getStatus());
        values.put(GAME_ALARM,alarm.getGametype());
        values.put(SOUND_ALARM,alarm.getSound());
        values.put(PREVOL_ALARM,alarm.getPreVol());
        values.put(VOL_ALARM,alarm.getVol());
        values.put(FLAG_NEED_AFD,alarm.getFlag());
        return values;
    }


    //Lấy Alarm
    //#Tham số: lệnh SQL
    //#Trả về: Alarm
    public Alarm getAlarm(String sql){
        Log.e("fe","public Alarm getAlarm(String sql){");
        Alarm alarm = null;
        Cursor cursor = loadAll(sql);

        if(cursor.getCount()>0){
            alarm = cursorToAlarm(cursor);
            cursor.close();
        }
        return alarm;
    }

    //Lấy nguyên list Alarm
    //#Tham số: lệnh SQL
    //#Trả về: list Alarm
    public ArrayList<Alarm> getAlarmList(String sql){
        ArrayList<Alarm> taskList = new ArrayList<Alarm>();
        Cursor cursor = loadAll(sql);

        while(!cursor.isAfterLast()){
            taskList.add(cursorToAlarm(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        return taskList;
    }

    public int TotalLine(String sql)
    {
        Cursor cursor = loadAll(sql);
        int Rows= cursor.getCount();
        cursor.close();
        return Rows;
    }

    //Thêm Alarm vào table
    //#Tham số: Alarm
    //#Trả về: id của Alarm
    public long insertAlarm(Alarm alarm){
        return insert(TABLE_ALARM, alarmToValues(alarm));
    }

    //update  Alarm
    //#Tham số: Alarm
    //#Trả về: id của Alarm
    public boolean updateAlarm(Alarm alarm){
        return update(TABLE_ALARM, alarmToValues(alarm),ID_ALARM + " = " + alarm.getId());
    }

    //delete Alarm
    //#Tham số: alarm
    //#Trả về: id của alarm
    public boolean deleteAlarm(String where){
        return delete(TABLE_ALARM, where);
    }
}
