package com.samir.todos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class ToDoSActivity extends Activity {

	private DBAdapter dbAdapter;
	private Cursor cursor;
	ListView listView;
	private final int ABOUT_DIALOG = 1;
	private final int HELP_DIALOG = 2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button btnAddTodoList = (Button) findViewById(R.id.btnAddTodoList);
		listView = (ListView) findViewById(R.id.listViewTaskList);
		loadData();

		registerForContextMenu(listView);

		btnAddTodoList.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(ToDoSActivity.this, CreateTodoList.class);
				startActivityForResult(i, 1);
			}

		});

		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long id) {
				if (pos > -1) {
					Intent i = new Intent(ToDoSActivity.this,
							TodoListActivity.class);
					i.putExtra("todoListId",
							(int) cursor.getLong(cursor.getColumnIndex("_id")));
					startActivity(i);
				}
			}
		});
	}

	public void loadData() {
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		cursor = dbAdapter.getAllTodoLists();
		startManagingCursor(cursor);

		MainCursorAdapter todoListsAdapter = new MainCursorAdapter(this,
				android.R.layout.simple_list_item_1, cursor,
				new String[] { "list_name" }, new int[] { android.R.id.text1 },
				dbAdapter);
		listView.setAdapter(todoListsAdapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.aboutMenuItem:
			showDialog(ABOUT_DIALOG);
			break;
		case R.id.helpMenuItem:
			showDialog(HELP_DIALOG);
			break;
		case R.id.settingsMenuItem:
			Intent intent = new Intent(ToDoSActivity.this,
					MainPreferenceActivity.class);
			startActivity(intent);
			break;

		}
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		AlertDialog.Builder builder = new Builder(this);
		builder = new Builder(this);
		switch (id) {
		case ABOUT_DIALOG:
			return builder
					.setMessage(
							"This is a todo task list application...")
					.setPositiveButton("OK", null).setTitle("About ToDoS")
					.setIcon(R.drawable.icon).create();
		case HELP_DIALOG:
			return builder
					.setMessage(
							"To create list click new list button in main activity."
									+ " Enter list name and click save. Empty lists and lists that contain tasks have different icons."
									+ " To create task click new task in task list activity."
									+ ""
									+ " Enter task name, description, choose priority and set end date/time if you want to."
									+ " Tasks are active by default. Active and inactive tasks have different icons and font style.")
					.setPositiveButton("OK", null).setTitle("ToDoS help")
					.setIcon(R.drawable.icon).create();
		}
		return null;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.list_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.listContextMenuDelete:
			dbAdapter.deleteList(cursor.getLong(cursor.getColumnIndex("_id")));
			Toast.makeText(this, "List deleted.", Toast.LENGTH_SHORT).show();
			break;
		default:
		}
		loadData();
		return super.onContextItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 1) {
			Toast.makeText(this, "Created new list", Toast.LENGTH_SHORT).show();
		}
	}
}