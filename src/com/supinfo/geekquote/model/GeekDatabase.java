package com.supinfo.geekquote.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GeekDatabase extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "supinfo.db";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_QUOTE = "quote";
	private static final String TABLE_CREATE =
		       "CREATE TABLE " + TABLE_QUOTE + " (" +
		       "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
		       "textQuote TEXT NOT NULL, " +
		       "dateCreation TEXT, " +
		       "rating INTEGER);";

	
	public GeekDatabase(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
