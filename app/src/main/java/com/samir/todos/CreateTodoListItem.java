package com.samir.todos;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class CreateTodoListItem extends Activity {
	AlarmManager am;
	final Calendar dateTime = Calendar.getInstance();
	private boolean endDate = false;
	private TextView mDateDisplay;
	private TextView mTimeDisplay;
	private DBAdapter dbAdapter;
	private int todoListId;
	int task_priority = 1;
	static final int DATE_DIALOG_ID = 0;
	static final int TIME_DIALOG_ID = 1;
	private int mYear;
	private int mMonth;
	private int mDay;
	private int mHour;
	private int mMinute;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_todo_list_item);
		getApplicationContext();
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			todoListId = extras.getInt("todoListId");
		}
		Button btnCreateTodoListItem = (Button) findViewById(R.id.btnCreateTodoListItem);
		Button btnCancel = (Button) findViewById(R.id.btnCancelCreateTodoListItem);
		Button btnSetAlarmDate = (Button) findViewById(R.id.btnSetAlarmDate);
		Button btnSetAlarmTime = (Button) findViewById(R.id.btnSetAlarmTime);
		mDateDisplay = (TextView) findViewById(R.id.dateDisplay);
		mDateDisplay.setText("Not set.");
		mTimeDisplay = (TextView) findViewById(R.id.timeDisplay);
		final EditText editListItemName = (EditText) findViewById(R.id.editListItemName);
		final EditText editListItemDescription = (EditText) findViewById(R.id.editListItemDescription);
		final RadioButton rbPriority1 = (RadioButton) findViewById(R.id.rbPriority1);
		final RadioButton rbPriority2 = (RadioButton) findViewById(R.id.rbPriority2);
		final RadioButton rbPriority3 = (RadioButton) findViewById(R.id.rbPriority3);

		rbPriority1.setChecked(true);

		btnSetAlarmDate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
		});

		btnSetAlarmTime.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showDialog(TIME_DIALOG_ID);
			}
		});

		mYear = dateTime.get(Calendar.YEAR);
		mMonth = dateTime.get(Calendar.MONTH);
		mDay = dateTime.get(Calendar.DAY_OF_MONTH);
		mHour = dateTime.get(Calendar.HOUR_OF_DAY);
		mMinute = dateTime.get(Calendar.MINUTE);

		btnCreateTodoListItem.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if (rbPriority2.isChecked())
					task_priority = 2;
				if (rbPriority3.isChecked())
					task_priority = 3;
				long taskId;
				int prefTasksCount = MainPreferenceActivity
						.getMaxTasksNum(getApplicationContext());
				if (dbAdapter.getTodoListItemCount(todoListId) < prefTasksCount
						|| prefTasksCount == 0) {
					if (endDate)
						taskId = dbAdapter.insertTodoListItem(todoListId,
								editListItemName.getText().toString(),
								editListItemDescription.getText().toString(),
								task_priority, mYear + "-" + (mMonth + 1) + "-"
										+ mDay + " " + mHour + ":" + mMinute);
					else
						taskId = dbAdapter.insertTodoListItem(todoListId,
								editListItemName.getText().toString(),
								editListItemDescription.getText().toString(),
								task_priority, "Not set");
					am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
					setOneTimeAlarm(editListItemName.getText().toString(),
							taskId);
					setResult(RESULT_OK);
				} else {
					Toast.makeText(getApplicationContext(),
							"Maximum number of tasks per list is " + prefTasksCount,
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

	private void updateDisplay() {
		endDate = true;
		mDateDisplay.setText(new StringBuilder().append(mMonth + 1).append("-")
				.append(mDay).append("-").append(mYear).append(" "));
		mTimeDisplay.setText(new StringBuilder().append(mHour).append(":")
				.append(mMinute));
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			dateTime.set(year, monthOfYear, dayOfMonth);
			updateDisplay();
		}
	};
	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		public void onTimeSet(TimePicker view, int hour, int minute) {
			mHour = hour;
			mMinute = minute;
			dateTime.set(mYear, mMonth, mDay, mHour, mMinute);
			updateDisplay();
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					true);
		}
		return null;
	}

	public void setOneTimeAlarm(String taskName, long taskId) {
		if (endDate) {
			Intent intent = new Intent(this, TimeAlarm.class);
			intent.putExtra("taskId", taskId);
			intent.putExtra("taskName", taskName);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(this,
					(int) System.currentTimeMillis(), intent,
					PendingIntent.FLAG_ONE_SHOT);
			am.set(AlarmManager.RTC_WAKEUP, dateTime.getTimeInMillis(),
					pendingIntent);
		}
	}
}
