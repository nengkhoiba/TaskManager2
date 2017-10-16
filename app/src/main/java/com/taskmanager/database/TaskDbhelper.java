package com.taskmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDbhelper extends SQLiteOpenHelper {


	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "TaskDB";
	Context context;

	public TaskDbhelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		//table for storing msg
		db.execSQL("CREATE TABLE TASKS (id INTEGER PRIMARY KEY,task CHAR,details CHAR,summary CHAR);");
		
		
	}
	
	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS TASKS");
		
		onCreate(db);
	}
	public Cursor getTasks()

	{

		Cursor cursor = getReadableDatabase().rawQuery(
				"SELECT * FROM TASKS ORDER BY id DESC", null);
		
		return cursor;
	}

	public void AddTasks( String task, String details, String summary) {
		// TODO Auto-generated method stub
		ContentValues values = new ContentValues(2);

		values.put("task", task);
		values.put("details", details);
		values.put("summary", summary);
		getWritableDatabase().insert("TASKS",null, values);
		
		//Toast.makeText(context, "Data inserted", Toast.LENGTH_LONG).show();

	}

	public void UpdateTask(String ID, String task, String details, String summary) {
		// TODO Auto-generated method stub
		getWritableDatabase().execSQL("UPDATE TASKS SET task='"+task+"', details='"+details+"', summary='"+summary+"' WHERE id='"+ID+"'");

		//Toast.makeText(context, "Data inserted", Toast.LENGTH_LONG).show();

	}

	
	public void deleteAll() {
		// TODO Auto-generated method stub
		try {
			getWritableDatabase().delete("TASKS", null, null);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public void deleteTaskByID(String id) {
		// TODO Auto-generated method stub
		try {
			getWritableDatabase().delete("TASKS","id=?", new String[] {id});


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
