package com.dssd.encuestas;

import android.app.Application;
import android.view.Gravity;

import com.bugsnag.android.Bugsnag;
import com.dssd.encuestas.datos.AppConfig;

public class App extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		
		Bugsnag.register(this, "e84eff567386718b6b5b890363518763");
		
		String device = AppConfig.getInstance(this).getDevice();
		if(device != null) {
			Bugsnag.addToTab("Device", "id", device);
		}
		
		/* Aramark */
		/*
		mostrarLogoLoyalMaker = false;
		mostrarBotonSiguiente = false;
		mostrarBotonTerminar = false;
		guardarRespuestasParciales = false;
		*/
		
		/* Gasco */
		/*
		invertirOrdenRespuestas = true;
		posicionMensajes = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
		*/
	}
	
    static boolean mostrarLogoLoyalMaker = true;
    static boolean invertirOrdenRespuestas = true;
    static int posicionMensajes = Gravity.CENTER;
    static boolean mostrarBotonSiguiente = false;
    static boolean mostrarBotonTerminar = true;
    static boolean guardarRespuestasParciales = true;
    
    public static boolean isMostrarLogoLoyalMaker() {
		return mostrarLogoLoyalMaker;
	}
    public static boolean isInvertirOrdenRespuestas() {
		return invertirOrdenRespuestas;
	}
    public static boolean isMostrarBotonSiguiente() {
		return mostrarBotonSiguiente;
	}
    public static boolean isMostrarBotonTerminar() {
		return mostrarBotonTerminar;
	}
    public static boolean isGuardarRespuestasParciales() {
		return guardarRespuestasParciales;
	}
}
