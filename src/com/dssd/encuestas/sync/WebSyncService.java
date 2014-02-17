package com.dssd.encuestas.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class WebSyncService extends Service {
	
    private static WebSyncAdapter sSyncAdapter = null;
    private static final Object sSyncAdapterLock = new Object();
    
    @Override
    public void onCreate() {
    	synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new WebSyncAdapter(getApplicationContext(), true);
            }
        }
    }
    
	@Override
	public IBinder onBind(Intent intent) {
		return sSyncAdapter.getSyncAdapterBinder();
	}

}
