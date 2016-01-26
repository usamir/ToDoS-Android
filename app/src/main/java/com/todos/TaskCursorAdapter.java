package com.todos;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class TaskCursorAdapter extends SimpleCursorAdapter {
	private Cursor c;
	private Context context;

	public TaskCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to) {
		super(context, layout, c, from, to);
		this.c = c;
		this.context = context;

	}

	public View getView(int pos, View inView, ViewGroup parent) {
		View v = inView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.task_lay, null);
		}
		this.c.moveToPosition(pos);
		String task_name = this.c.getString(this.c.getColumnIndex("task_name"));
		TextView taskName = (TextView) v.findViewById(R.id.task_name);
		taskName.setText(task_name);
		
		ImageView iv = (ImageView) v.findViewById(R.id.task_active_image);
		int active = this.c.getInt(this.c.getColumnIndex("active"));
		if (active == 1){
			iv.setImageResource(android.R.drawable.star_big_on);
			taskName.setPaintFlags(taskName.getPaintFlags() | Paint.ANTI_ALIAS_FLAG);
		}
		else{
			iv.setImageResource(android.R.drawable.star_big_off);
			taskName.setPaintFlags(taskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		}

		int priority = this.c.getInt(this.c.getColumnIndex("task_priority"));
		ImageView iv1 = (ImageView) v.findViewById(R.id.task_priority_image);
		if (priority == 1)
			iv1.setImageResource(android.R.drawable.presence_online);
		else if (priority == 2)
			iv1.setImageResource(android.R.drawable.presence_away);
		else
			iv1.setImageResource(android.R.drawable.presence_busy);

		
		
		
		return (v);
	}

}
