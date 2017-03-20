package com.samir.todos;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateTodoList extends Activity {
	private DBAdapter dbAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_todo_list);

		dbAdapter = new DBAdapter(this);
		dbAdapter.open();

		Button btnCreateTodoList = (Button) findViewById(R.id.btnCreateTodoList);
		Button btnCancel = (Button) findViewById(R.id.btnCancelCreateTodoList);
		final EditText editListName = (EditText) findViewById(R.id.editListName);

		btnCreateTodoList.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				int prefListsCount = MainPreferenceActivity
						.getMaxListsNum(getApplicationContext());
				if (dbAdapter.getTodoListsCount() < prefListsCount
						|| prefListsCount == 0) {
					dbAdapter.insertTodoList(editListName.getText().toString());
					setResult(RESULT_OK);
				} else {
					Toast.makeText(getApplicationContext(),
							"Maximum number of lists is " + prefListsCount,
							Toast.LENGTH_SHORT).show();
					setResult(RESULT_CANCELED);
				}
				finish();
			}

		});

		btnCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
}
