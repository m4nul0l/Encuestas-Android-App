package com.dssd.encuestas.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WebAuthenticatorService extends Service {
	
    private WebAuthenticator mAuthenticator;
    
    @Override
    public void onCreate() {
    	mAuthenticator = new WebAuthenticator(this);
    }
    
	@Override
	public IBinder onBind(Intent arg0) {
		return mAuthenticator.getIBinder();
	}

}
