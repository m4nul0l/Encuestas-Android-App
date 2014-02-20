package com.dssd.encuestas.sync;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

public class WebSyncAdapter extends AbstractThreadedSyncAdapter {

	public WebSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		// TODO Auto-generated constructor stub
	}

	@TargetApi(11)
	public WebSyncAdapter(Context context, boolean autoInitialize,
			boolean allowParallelSyncs) {
		super(context, autoInitialize, allowParallelSyncs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		
		
		// TODO PONER proceso :sync en manifiesto!
		System.out.println("encuestas: onPerformSync");
		deviceAuth();
		//deviceAuth();
	}

	private void deviceAuth() {
		try {
			URL u = new URL("http://www.loyalmaker.com/services/device/index");
			InputStream openStream = u.openStream();
			//InputStreamReader r = new InputStreamReader(openStream);
			//BufferedReader br = new BufferedReader(r);
			
			java.util.Scanner s = new java.util.Scanner(openStream);
			s.useDelimiter("\\A");
		    String str = s.hasNext() ? s.next() : "";
		    s.close();
		    
		    Log.v("loyalmaker", str);
		    
        	//Toast.makeText(getContext(), str, Toast.LENGTH_LONG).show();
		    
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
