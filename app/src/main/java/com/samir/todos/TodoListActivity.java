package com.samir.todos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class TodoListActivity extends Activity {
	private DBAdapter dbAdapter;
	private Cursor cursor;
	private int todoListId;
	static final int TASK_DETAILS_DIALOG_ID = 1;
	ListView listView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.todolist);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			todoListId = extras.getInt("todoListId");
		}

		Button btnAddTodoListItem = (Button) findViewById(R.id.btnAddTodoListItem);
		listView = (ListView) findViewById(R.id.listViewTaskListItem);
		loadData();

		registerForContextMenu(listView);

		btnAddTodoListItem.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent i = new Intent(TodoListActivity.this,
						CreateTodoListItem.class);
				i.putExtra("todoListId", todoListId);
				startActivityForResult(i, 1);
			}

		});

	}

	public void loadData() {
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();

		cursor = dbAdapter.getAllTodoTasksByListID(todoListId);
		startManagingCursor(cursor);

		TaskCursorAdapter todoListItemsAdapter = new TaskCursorAdapter(this,
				android.R.layout.simple_list_item_1, cursor, new String[] {
						"task_name", "task_priority", "active" },
				new int[] { android.R.id.text1 });
		listView.setAdapter(todoListItemsAdapter);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.task_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.taskContextMenuDeactivate:
			dbAdapter.deactivateTask(cursor.getLong(cursor
					.getColumnIndex("_id")));
			Toast.makeText(this, "Task deactivated.", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.taskContextMenuActivate:
			dbAdapter
					.activateTask(cursor.getLong(cursor.getColumnIndex("_id")));
			Toast.makeText(this, "Task activated.", Toast.LENGTH_SHORT).show();
			break;
		case R.id.taskContextMenuDelete:
			dbAdapter.deleteTask(cursor.getLong(cursor.getColumnIndex("_id")));
			Toast.makeText(this, "Task deleted.", Toast.LENGTH_SHORT).show();
			break;
		case R.id.taskContextMenuTaskDetails:
			removeDialog(TASK_DETAILS_DIALOG_ID);

			Bundle b = new Bundle();
			b.putString("task_name",
					cursor.getString(cursor.getColumnIndex("task_name")));
			b.putString("task_description",
					cursor.getString(cursor.getColumnIndex("task_description")));
			b.putInt("active", cursor.getInt(cursor.getColumnIndex("active")));
			b.putString("task_creation_date", cursor.getString(cursor
					.getColumnIndex("task_creation_date")));
			b.putString("task_termination_date", cursor.getString(cursor
					.getColumnIndex("task_termination_date")));
			b.putInt("task_priority",
					cursor.getInt(this.cursor.getColumnIndex("task_priority")));
			showDialog(TASK_DETAILS_DIALOG_ID, b);

			break;
		default:
		}
		loadData();
		return super.onContextItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle b) {
		Dialog dialog = null;
		switch (id) {
		case TASK_DETAILS_DIALOG_ID:
			dialog = new Dialog(this);
			dialog.setContentView(R.layout.task_details);
			dialog.setTitle("Task details");
			TextView name = (TextView) dialog
					.findViewById(R.id.taskDetailsTaskName);
			name.setText("Task name: " + b.getString("task_name"));
			TextView details = (TextView) dialog
					.findViewById(R.id.taskDetailsTaskDetails);
			details.setText("Task details: " + b.getString("task_description"));
			TextView isActive = (TextView) dialog
					.findViewById(R.id.taskDetailsIsActive);
			if (b.getInt("active") > 0)
				isActive.setText("Task is active");
			else
				isActive.setText("Task deactivated");

			TextView creationDate = (TextView) dialog
					.findViewById(R.id.taskDetailsCreationDate);
			creationDate.setText("Task creation date: "
					+ b.getString("task_creation_date"));
			TextView endDate = (TextView) dialog
					.findViewById(R.id.taskDetailsEndDate);
			endDate.setText("Task end date: "
					+ b.getString("task_termination_date"));
			int priority = b.getInt("task_priority");
			ImageView iv1 = (ImageView) dialog
					.findViewById(R.id.taskDetailsPriority);
			if (priority == 1)
				iv1.setImageResource(android.R.drawable.presence_online);
			else if (priority == 2)
				iv1.setImageResource(android.R.drawable.presence_away);
			else
				iv1.setImageResource(android.R.drawable.presence_busy);

			break;
		default:
			dialog = null;
		}

		return dialog;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 1) {
			Toast.makeText(this, "New task created.",
					Toast.LENGTH_SHORT).show();
		}
	}
}
