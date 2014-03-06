package com.dssd.encuestas;

import java.util.List;

import com.dssd.encuestas.datos.AppConfig;
import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.EncuestaManager;
import com.dssd.encuestas.info.DeviceInfoHelper;
import com.dssd.encuestas.sync.EncuestasSyncHelper;
import com.dssd.encuestas.templates.TwoColorDrawable;

import android.os.Bundle;
import android.os.Handler;
import android.R.color;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	ProgressDialog noDataWaitProgressDialog;
	List<Encuesta> encuestas;
	static final int sync_retry_wait = 10000;
	
	View contentView;
	EncuestaManager encuestaManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		contentView = getLayoutInflater().inflate(R.layout.activity_main, null);
		setContentView(contentView);
		
		String device = AppConfig.getInstance(this).getDevice();
		if(device == null) {
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		}
		
		encuestaManager = new EncuestaManager(this);
	}
	
	BroadcastReceiver syncReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			encuestas = encuestaManager.getEncuestas();
			if(encuestas.size() == 0) {
				Handler mainHandler = new Handler(context.getMainLooper());
				Runnable myRunnable = new Runnable() {
					@Override
					public void run() {
						manualSync();
					}
				};
				mainHandler.postDelayed(myRunnable, sync_retry_wait);
			} else {
				noDataWaitProgressDialog.dismiss();
				initBienvenida();
			}
		}
	};
	
	@Override
	protected void onStart() {
		super.onStart();
		
		encuestas = encuestaManager.getEncuestas();
		if(encuestas.size() == 0) {
			noDataWaitProgressDialog = ProgressDialog.show(this, "Sincronizando", "Por favor espere...", true, true, new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			});
			LocalBroadcastManager.getInstance(this).registerReceiver(syncReceiver,
					new IntentFilter(EncuestasSyncHelper.action_sync_end));
			
			manualSync();
		} else {
			initBienvenida();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		LocalBroadcastManager.getInstance(this).unregisterReceiver(syncReceiver);
		if(noDataWaitProgressDialog != null) {
			noDataWaitProgressDialog.dismiss();
			noDataWaitProgressDialog = null;
		}
	}
	
	public void initBienvenida() {
		EncuestaManager em = new EncuestaManager(this);
		List<Encuesta> list = em.getEncuestas();
		if(list.size() > 0) {
			Encuesta encuesta = list.get(0);
			String mensajeBienvenida = encuesta.getMensajeBienvenida();
			if(mensajeBienvenida != null && mensajeBienvenida.compareTo("") != 0) {
				TextView tvb = (TextView) findViewById(R.id.textViewBienvenida);
				tvb.setText(mensajeBienvenida);
			}
			
			//setBackground(encuesta);
		}
	}
	
	public void setBackground(Encuesta encuesta) {
		int color1 = getResources().getColor(R.color.defaultColorSuperior);
		int color2 = getResources().getColor(R.color.defaultColorSuperior);
		//		Color.parseColor("#DF0000"), Color.parseColor("#0000DF")
		
		if(encuesta.getColorSuperior() != null && encuesta.getColorSuperior().compareTo("") != 0) {
			color1 = Color.parseColor("#"+encuesta.getColorSuperior());
		}
		if(encuesta.getColorInferior() != null && encuesta.getColorInferior().compareTo("") != 0) {
			color2 = Color.parseColor("#"+encuesta.getColorInferior());
		}
		
		TwoColorDrawable colorDrawable = new TwoColorDrawable(color1, color2);
		contentView.setBackgroundDrawable(colorDrawable);
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
	
	public void manualSync() {
		Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) this.getSystemService(ACCOUNT_SERVICE);
        
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
        
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(newAccount, AUTHORITY, settingsBundle);
	}
	
}
