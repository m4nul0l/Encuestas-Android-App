package com.dssd.encuestas;

import android.app.Application;

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
	}
}
