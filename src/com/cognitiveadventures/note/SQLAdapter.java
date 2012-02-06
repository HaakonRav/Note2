/*
 * Practically a copy/paste of Googles NotePad tutorial' NotesDbAdapter.java
 * http://developer.android.com/resources/tutorials/notepad/index.html
 * If you are to use it, know its Google's.
 */

package com.cognitiveadventures.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLAdapter {
	public static final String KEY_TITLE = "ntitle";
	public static final String KEY_BODY = "nbody";
	public static final String KEY_ROWID = "_id";
	
	private static final String DATABASE_NAME = "notesdb";
	private static final String DATABASE_TABLE = "notestable";
	private static final int DATABASE_VERSION = 2;

	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;
	
	private static final String DATABASE_CREATE =
			"create table " + DATABASE_TABLE + " (" + KEY_ROWID + 
			" integer primary key autoincrement," + KEY_TITLE +
			" text not null, " + KEY_BODY + " text not null);";
	
	private final Context ctx;
	
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		}
	}
	
	public SQLAdapter(Context ctx) {
		this.ctx = ctx;
	}
	
	public SQLAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(ctx);
		mDb = mDbHelper.getWritableDatabase();
		return(this);
	}
	
	public void close() {
		mDbHelper.close();
	}
	
	public long createNote(String title, String body) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_BODY, body);
		
		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}
	
	public boolean deleteNote(long rowId) {
		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
	public Cursor fetchAllNotes() {
		return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE, KEY_BODY}, null, null, null, null, null);
	}
	
	public Cursor fetchNote(long rowId) throws SQLException {
		Cursor mCursor =
				mDb.query(true, DATABASE_TABLE, new String[] {KEY_ROWID, KEY_TITLE, KEY_BODY}, KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if(mCursor != null)
			mCursor.moveToFirst();
		return mCursor;
	}
	
	public boolean updateNote(long rowId, String title, String body) {
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		args.put(KEY_BODY, body);
		
		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
	
}
