package com.georgiasouthern.simonsays;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "Scores", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table scorestable (id text primary key, val text, val2 text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists scorestable");
        onCreate(db);
    }

    public Integer deleteData(String id) {
        SQLiteDatabase dbWrite = this.getWritableDatabase();
        return dbWrite.delete("scorestable", "ID = ?", new String[] {id});
    }
}