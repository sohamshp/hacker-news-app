package com.example.soham.hacker_news_app.activity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Soham on 20-04-2017.
 */

public class SavedStoryDB {

    public static final String DATABASE_NAME = "saved_stories";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "stories";
    public static final String KEY_ID = "_id";
    public static final String STORY_ID = "story_string";
    public static final int COL_INDEX = 1;

    private static final String DB_CREATE="create table "+TABLE_NAME+" ("+KEY_ID+" integer primary key "+
            "autoincrement , "+STORY_ID+" text not null );";


    private SQLiteDatabase db;
    private final Context context;
    private MyDBAdapter helper;

    public SavedStoryDB(Context context)
    {
        this.context = context;
        helper = new MyDBAdapter(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    public SavedStoryDB open()
    {
        db = helper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        db.close();
    }

    public long insertEntry(String jsonString) {

        ContentValues contentValues=new ContentValues();
        contentValues.put(STORY_ID, jsonString);
        return db.insert(TABLE_NAME, null, contentValues);
    }
    public void removeEntry(long rowIndex) {
        System.out.print(rowIndex);
        db.delete(TABLE_NAME, KEY_ID+" =?", new String[] {String.valueOf(rowIndex)});
    }

    public void removeEntry(String row) {
        System.out.print(row);
        db.delete(TABLE_NAME, STORY_ID+" =?", new String[] {row});
    }

    public String search(String story_id)
    {
        Cursor cursor = db.query(TABLE_NAME, new String[] {STORY_ID},
                STORY_ID + "=?",new String[] {story_id}, null, null, null);

        if(cursor!=null)
            cursor.moveToFirst();

        String jsonString = cursor.getString(0);
        return jsonString;
    }

    public List<String> getAllEntries() {
        List<String> storylist = new ArrayList<String>();
        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID,STORY_ID}, null, null, null, null, null);
        if(cursor.moveToFirst())
        {
            do {

                String jsonString = cursor.getString(1);
                storylist.add(jsonString);
            }while(cursor.moveToNext());
        }
        return storylist;
    }

    public int updateEntry(long rowIndex,String playerName) {

        ContentValues updateValues=new ContentValues();
        //updateValues.put(COL_NAME, playerName);
        return db.update(TABLE_NAME, updateValues, KEY_ID+" = "+rowIndex, null);
    }

    private static class MyDBAdapter extends SQLiteOpenHelper {
        public MyDBAdapter(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }

}
