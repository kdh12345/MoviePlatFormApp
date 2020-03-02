package org.androidtown.movieproject2.DBClasses;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MovieDBhelper extends SQLiteOpenHelper {
    public MovieDBhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
        super.onOpen(db);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("Database","DBHelper.OnCreate()실행 됨");
        sqLiteDatabase.execSQL(SettingDB.SQL_CREATE_TABLE);
        sqLiteDatabase.execSQL(SettingDB.SQL_CREATE_COMMENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {

    }
    public void InsertOrReplaceItem(int id, String movie, String detail, String simple_comments, String comments){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SettingDB.SQL_INSERT+"("+id+", '"+
                movie+"', " +
                detail+", " +
                simple_comments+", " +
                comments +")");
        Log.d("database",id+"insert complete");
        db.close();
    }

    public void updateDatabase(int id, String where, String content){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SettingDB.SQL_UPDATE+where+"='"+content+"' WHERE id="+id);
        Log.d("database",id+"update complete");
        Log.d("database",content);
        db.close();
    }

    public ArrayList<String> getDataFromDB(String column){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> arr = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT "+column+" FROM "+SettingDB.T_NAME,null);
        int index = cursor.getColumnIndex(column);
        if (cursor.moveToFirst()){
            do {
                // Passing values
                String data = cursor.getString(index);
                arr.add(data);
                Log.d("database",data);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
      //  Log.d("database","데이터 받아고기");
        return arr;
    }

    public String getDataFromDB(String column,int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String data="";
        Cursor cursor = db.rawQuery("SELECT "+column+" FROM "+SettingDB.T_NAME+" WHERE "+SettingDB.COL_ID+"="+id,null);
        int index = cursor.getColumnIndex(column);
        if (cursor.moveToFirst()){
            data = cursor.getString(index);
        }
        cursor.close();
        db.close();
        return data;
    }
}
