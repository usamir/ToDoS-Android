package com.todos;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.todos.R;

public class MainPreferenceActivity extends PreferenceActivity {
	
	@Override 
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.main_preference);
	}
	
	public static int tryParse(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	public static int getMaxListsNum(Context context) {
		return tryParse(PreferenceManager.getDefaultSharedPreferences(context)
				.getString("maxListsNum", "0"));
	}
	
	public static int getMaxTasksNum(Context context) {
		return tryParse(PreferenceManager.getDefaultSharedPreferences(context)
				.getString("maxTasksNum", "0"));
	}
	
	public static int getListsSort(Context context) {
		return tryParse(PreferenceManager.getDefaultSharedPreferences(context)
				.getString("listsSort", "0"));
	}
	
	public static int getTasksSort(Context context) {
		return tryParse(PreferenceManager.getDefaultSharedPreferences(context)
				.getString("tasksSort", "0"));
	}

}
