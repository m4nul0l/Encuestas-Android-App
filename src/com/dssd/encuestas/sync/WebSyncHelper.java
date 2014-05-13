package com.dssd.encuestas.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

public class WebSyncHelper {

	private WebSyncHelper() {
	}

	private static class WebSyncHelperHolder {
		public static final WebSyncHelper INSTANCE = new WebSyncHelper();
	}
	
	public static WebSyncHelper getInstance() {
		return WebSyncHelperHolder.INSTANCE;
	}
	
	public static final String AUTHORITY = "com.loyalmaker.datasync.provider";
    public static final String ACCOUNT_TYPE = "loyalmaker.com";
    public static final String ACCOUNT = "LoyalMaker";
    Account mAccount;
    
    //public static final long MILLISECONDS_PER_SECOND = 1000L;
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 15L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
            SECONDS_PER_MINUTE;
            //* MILLISECONDS_PER_SECOND;
    
    public Account getAccount(Context context) {
		Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        
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
        	//Toast.makeText(this, "added", Toast.LENGTH_SHORT).show();
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        	//Toast.makeText(this, "NOT added", Toast.LENGTH_SHORT).show();
        }
        return newAccount;
    }
    
    public void configurePeriodicSync(Context context) {
    	Account account = getAccount(context);
    	
    	ContentResolver.setSyncAutomatically(account, AUTHORITY, true);
    	
        Bundle settingsBundle = new Bundle();
        ContentResolver.addPeriodicSync(account, AUTHORITY, settingsBundle, SYNC_INTERVAL);
    }
	
	public void requestSync(Context context) {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(getAccount(context), AUTHORITY, settingsBundle);
	}
}
