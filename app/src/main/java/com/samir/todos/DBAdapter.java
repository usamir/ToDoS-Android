package com.samir.todos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class DBAdapter {

	private DBHelper dbHelper;
	private Context context;
	private SQLiteDatabase db;

	public DBAdapter(Context context) {
		this.context = context;
	}

	public DBAdapter open() throws SQLException {
		dbHelper = new DBHelper(context, "todosdata", null, 2);
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public long insertTodoList(String list_name) {
		ContentValues initialValues = createTodoListValues(list_name);

		return db.insert("todo_lists", null, initialValues);
	}

	public Cursor getAllTodoLists() {
		if(MainPreferenceActivity.getListsSort(context)==1)
			return db.query("todo_lists", new String[] { "_id", "list_name" },
					null, null, null, null, "list_name");
		
		return db.query("todo_lists", new String[] { "_id", "list_name" },
				null, null, null, null, null);
	}

	public long getTodoListItemCount(int list_id) {
		SQLiteStatement s = db
				.compileStatement("select count(*) from todo_task where todo_list_id="
						+ list_id);
		return s.simpleQueryForLong();
	}
	
	public long getTodoListsCount() {
		SQLiteStatement s = db
				.compileStatement("select count(*) from todo_lists");
		return s.simpleQueryForLong();
	}

	public Cursor getAllTodoTasksByListID(int todo_list_id) {
		String str = null;
		if(MainPreferenceActivity.getTasksSort(context)==1)
			str = "task_name";
		else if(MainPreferenceActivity.getTasksSort(context)==2)
			str = "task_priority DESC";
		else if(MainPreferenceActivity.getTasksSort(context)==3)
			str = "active DESC";
		return db.query("todo_task",
				new String[] { "_id", "task_name", "task_priority", "active",
						"task_description", "task_creation_date",
						"task_termination_date", "todo_list_id" },
				"todo_list_id = " + todo_list_id, null, null, null, str);
	}
	
	public boolean deactivateTask(long task_id) {
		ContentValues values = new ContentValues();
		values.put("active", 0);

		return db.update("todo_task", values,"_id ="+task_id, null) > 0;
	}
	
	public boolean activateTask(long task_id) {
		ContentValues values = new ContentValues();
		values.put("active", 1);

		return db.update("todo_task", values,"_id ="+task_id, null) > 0;
	}
	
	public boolean deleteTask(long task_id) {
		return db.delete("todo_task","_id ="+task_id, null) > 0;
	}
	
	public boolean deleteList(long list_id) {
		db.delete("todo_task","todo_list_id ="+list_id, null);
		return db.delete("todo_lists", "_id ="+list_id, null)>0;
	}

	public long insertTodoListItem(int todo_list_id, String task_name,
			String task_description, int task_priority, String termination_date) {
		ContentValues initialValues = createTodoListItemValues(todo_list_id,
				task_name, task_description, task_priority, termination_date);

		return db.insert("todo_task", null, initialValues);
	}

	private ContentValues createTodoListValues(String list_name) {
		ContentValues values = new ContentValues();
		values.put("list_name", list_name);
		return values;
	}

	private ContentValues createTodoListItemValues(int todo_list_id,
			String task_name, String task_description,int task_priority, String termination_date) {
		ContentValues values = new ContentValues();
		values.put("task_name", task_name);
		values.put("todo_list_id", todo_list_id);
		values.put("task_description", task_description);
		values.put("task_priority", task_priority);
		values.put("task_termination_date", termination_date);
		return values;
	}
}
