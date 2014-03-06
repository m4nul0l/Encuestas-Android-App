package com.dssd.encuestas.datos;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppConfig {

    private static final AppConfig instance = new AppConfig();
    private static Context context;
    
    public static AppConfig getInstance(Context context) {
    	synchronized (instance) {
			if(AppConfig.context == null)
				AppConfig.context = context.getApplicationContext();
		}
    	return instance;
    }
    
    private static final String DEVICE = "device";
    
    public String getDevice() {
		SharedPreferences prefs = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return prefs.getString(DEVICE, null);
    }
    
    public void setDevice(String device) {
		SharedPreferences prefs = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(DEVICE, device);
		editor.commit();
    }
}
