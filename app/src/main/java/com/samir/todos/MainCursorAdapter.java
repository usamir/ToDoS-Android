package com.samir.todos;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


public class MainCursorAdapter extends SimpleCursorAdapter {
	private DBAdapter dbAdapter;
	private Cursor c;
	private Context context;

	public MainCursorAdapter(Context context, int layout, Cursor c,
			String[] from, int[] to,DBAdapter dbAdapter) {
		super(context, layout, c, from, to);
		this.c = c;
		this.context = context;
		this.dbAdapter = dbAdapter;

	}

	public View getView(int pos, View inView, ViewGroup parent) {
		View v = inView;
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.main_list_item_lay, null);
		}
		this.c.moveToPosition(pos);
		String list_name = this.c.getString(this.c.getColumnIndex("list_name"));

		ImageView iv = (ImageView) v.findViewById(R.id.main_item_image);
		long count = dbAdapter.getTodoListItemCount(this.c.getInt(this.c
				.getColumnIndex("_id")));
		if (count > 0)
			iv.setImageResource(android.R.drawable.radiobutton_on_background);
		else
			iv.setImageResource(android.R.drawable.radiobutton_off_background);

		TextView listName = (TextView) v.findViewById(R.id.main_item_name);
		listName.setText(list_name);
		return (v);
	}

}
