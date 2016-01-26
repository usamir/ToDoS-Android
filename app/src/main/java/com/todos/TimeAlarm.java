package com.todos;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


public class TimeAlarm extends BroadcastReceiver {

	NotificationManager nm;
	CharSequence taskName;
	long taskId;
	private DBAdapter dbAdapter;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle extras = intent.getExtras();
		if (extras != null) {
			taskId = extras.getLong("taskId");
			taskName = extras.getString("taskName");
		}
		dbAdapter = new DBAdapter(context);
		dbAdapter.open();
		dbAdapter.deactivateTask(taskId);
		dbAdapter.close();
		
		nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		CharSequence from = "ToDoS";
		CharSequence message = "Task "+ taskName+" has beed deactivated.";
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				new Intent(), 0);
		Notification notif = new Notification(R.drawable.icon, "ToDoS",
				System.currentTimeMillis());
		notif.setLatestEventInfo(context, from, message, contentIntent);
		nm.notify(1, notif);
	}
}
