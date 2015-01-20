package com.dssd.encuestas.datos;

import java.util.Date;

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
    private static final String TIMEOUT_CANTIDAD_ENCUESTAS = "cantidad_encuestas";
    private static final String TIMEOUT_CANTIDAD_ENCUESTAS_MAX = "cantidad_encuestas_max";
    private static final String TIMEOUT_CANTIDAD_TIEMPO = "cantidad_tiempo";
    private static final String TIMEOUT_FECHA = "fecha";
    
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
    
    public int getTimeoutCantidadEncuestas() {
		SharedPreferences prefs = context.getSharedPreferences("timeout", Context.MODE_PRIVATE);
		return prefs.getInt(TIMEOUT_CANTIDAD_ENCUESTAS, 0);
    }
    
    public int getTimeoutCantidadEncuestasMax() {
		SharedPreferences prefs = context.getSharedPreferences("timeout", Context.MODE_PRIVATE);
		return prefs.getInt(TIMEOUT_CANTIDAD_ENCUESTAS_MAX, 0);
    }
        
    public Date getTimeoutFecha() {
		SharedPreferences prefs = context.getSharedPreferences("timeout", Context.MODE_PRIVATE);
		Date date = new Date();
		long time = prefs.getLong(TIMEOUT_FECHA, 0);
		if (time != 0) {
			date.setTime(time);
			return date;	
		}
		return null;
    }
    
    public int getTimeoutCantidadTiempo() {
		SharedPreferences prefs = context.getSharedPreferences("timeout", Context.MODE_PRIVATE);
		return prefs.getInt(TIMEOUT_CANTIDAD_TIEMPO, 0);
    }
    
    public void setTimeout(int cantidadTiempo, int cantidadEncuestas, Date fechaInicio) {
		SharedPreferences prefs = context.getSharedPreferences("timeout", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt(TIMEOUT_CANTIDAD_ENCUESTAS, cantidadEncuestas);
		editor.putInt(TIMEOUT_CANTIDAD_TIEMPO, cantidadTiempo);
		editor.putLong(TIMEOUT_FECHA, fechaInicio != null ? fechaInicio.getTime() : 0);
		editor.putInt(TIMEOUT_CANTIDAD_ENCUESTAS_MAX, cantidadEncuestas);
		
		editor.commit();
    }
    
    public void setTimeoutCantidadEncuestas(int cantidadEncuestas) {
		SharedPreferences prefs = context.getSharedPreferences("timeout", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putInt(TIMEOUT_CANTIDAD_ENCUESTAS, cantidadEncuestas);
		editor.commit();
    }
    public void setTimeoutFecha(Date fecha) {
		SharedPreferences prefs = context.getSharedPreferences("timeout", Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putLong(TIMEOUT_FECHA, fecha.getTime());
		editor.commit();
    }
}
