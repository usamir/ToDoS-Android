package com.samir.todos;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class TimeAlarm extends BroadcastReceiver {

	NotificationManager nm;
	CharSequence taskName;
	long taskId;
	private DBAdapter dbAdapter;
	
	@android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
		//Notification notif = new Notification(R.drawable.icon, "ToDoS",
				//System.currentTimeMillis());
		//notif.setLatestEventInfo(context, from, message, contentIntent);
		//nm.notify(1, notif);

		Notification notification = null;

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			notification = new Notification();
			notification.icon = R.drawable.icon;
			try {
				Method deprecatedMethod = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
				deprecatedMethod.invoke(notification, context, "ToDoS", null, contentIntent);
			} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {

			}
		} else {
			// Use new API
			Notification.Builder builder = new Notification.Builder(context)
					.setContentIntent(contentIntent)
					.setSmallIcon(R.drawable.icon)
					.setContentTitle("ToDoS");
			notification = builder.build();
		}
		nm.notify(1, notification);
	}
}
