package com.dssd.encuestas;

import com.dssd.encuestas.info.DeviceInfoHelper;

import android.os.Bundle;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Toast;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.action_force_sync:
			String xml = DeviceInfoHelper.getInstance(this).getDeviceInfoXML();
			System.out.println(xml);
			//testSync();
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		//Toast.makeText(this, "se presiono", Toast.LENGTH_SHORT).show();
		
		if(event.getAction() == MotionEvent.ACTION_UP) {
			startActivity(new Intent(this, PreguntasActivity.class));
			return true;
		}
		
		return super.onTouchEvent(event);
	}
	
	
	
	
	public static final String AUTHORITY = "com.loyalmaker.datasync.provider";
    public static final String ACCOUNT_TYPE = "loyalmaker.com";
    public static final String ACCOUNT = "dummyaccount";
    Account mAccount;	
	
	public void testSync() {
		Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) this.getSystemService(
                        ACCOUNT_SERVICE);
        
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        	Toast.makeText(this, "added", Toast.LENGTH_SHORT).show();
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        	Toast.makeText(this, "NOT added", Toast.LENGTH_SHORT).show();
        }
        
        
        // TODO esto todavia no funciona, probablemente sea porque no es una AUTHORITY existente
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(newAccount, AUTHORITY, settingsBundle);
	}
}
