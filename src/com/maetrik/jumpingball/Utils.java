package com.maetrik.jumpingball;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {
	
	private static final float MAX_DIST_MOVE = 20;
	
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
	
	
	public static boolean calcula_distancia(float xInit, float yInit, float xAct, float yAct) {
		if (Math.sqrt((xAct - xInit) * (xAct - xInit) + (yAct - yInit) * (yAct - yInit)) >= MAX_DIST_MOVE) return true;
		return false;
	}

}
