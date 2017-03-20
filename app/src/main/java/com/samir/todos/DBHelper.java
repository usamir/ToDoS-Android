package com.samir.todos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	private static final String CREATE_TODO_LISTS_STRING = "CREATE TABLE todo_lists (_id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,list_name TEXT  NOT NULL); ";
	private static final String CREATE_TODO_TASK_STRING = "CREATE TABLE todo_task (_id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT,task_name TEXT  NOT NULL, "
			+ " task_description TEXT  NULL,task_creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,task_termination_date TIMESTAMP  NULL,task_priority INTEGER DEFAULT 1, "
			+ "active INTEGER DEFAULT 1, todo_list_id INTEGER  NULL,FOREIGN KEY(todo_list_id) REFERENCES todo_lists(_id) );";
	

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TODO_LISTS_STRING);
		db.execSQL(CREATE_TODO_TASK_STRING);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS todo_lists");
		db.execSQL("DROP TABLE IF EXISTS todo_task");
		onCreate(db);

	}
}
