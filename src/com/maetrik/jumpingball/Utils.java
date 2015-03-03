package com.maetrik.jumpingball;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {
	
	public static void loadRecord() {
		SharedPreferences sharedPref = ResourcesManager.getInstance().actividad.getPreferences(Context.MODE_PRIVATE);
		Constants.RECORD = sharedPref.getLong("record", 0);
	}
	
	
	public static void saveRecord() {
		SharedPreferences sharedPref = ResourcesManager.getInstance().actividad.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putLong("record", Constants.RECORD);
		editor.commit();
	}

}
