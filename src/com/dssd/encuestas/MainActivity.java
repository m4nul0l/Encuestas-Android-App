package com.dssd.encuestas;

import java.util.List;

import com.dssd.encuestas.datos.AppConfig;
import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.EncuestaManager;
import com.dssd.encuestas.sync.EncuestasSyncHelper;

import android.os.Bundle;
import android.os.Handler;
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
import android.graphics.Typeface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	ProgressDialog noDataWaitProgressDialog;
	List<Encuesta> encuestas;
	static final int sync_retry_wait = 10000;
	
	View contentView;
	EncuestaManager encuestaManager;
	
	transient boolean informarFinSincro = false;
	
	static final float IMAGEN_EMPRESA_WIDTH = 0.3f;
	static final float IMAGEN_LOYALMAKER_WIDTH = 0.4f;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		contentView = getLayoutInflater().inflate(R.layout.activity_main, null);
		setContentView(contentView);
		
		setTemplate();
		
		String device = AppConfig.getInstance(this).getDevice();
		if(device == null) {
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		}
		
		encuestaManager = new EncuestaManager(this);
	}
	
	public void setTemplate() {
		Typeface tf = TemplateUtils.getFontRokkittRegular(this);
		((TextView)findViewById(R.id.textViewBienvenida)).setTypeface(tf);
		
		//TemplateUtils.setSizePercentage(findViewById(R.id.textViewBienvenida), 0.8f, 0.5f);
		TemplateUtils.setFontPercentage((TextView)findViewById(R.id.textViewBienvenida), TemplateUtils.GLOBAL_TEXT_SIZE);
		TemplateUtils.setWidthPercentage(findViewById(R.id.imageViewComenzar), 0.25f);
		TemplateUtils.setWidthPercentage(findViewById(R.id.imageViewLogo), IMAGEN_LOYALMAKER_WIDTH);
		/*TemplateUtils.setWidthPercentage(findViewById(R.id.imageViewEmpresa), 0.50f);*/
	}
	
	BroadcastReceiver syncReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(informarFinSincro) {
				Toast.makeText(MainActivity.this, R.string.sincro_manual_fin, Toast.LENGTH_SHORT).show();
				informarFinSincro = false;
			}
			
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
				if(noDataWaitProgressDialog != null) {
					noDataWaitProgressDialog.dismiss();
					noDataWaitProgressDialog = null;
				}
				normalSync();
				initBienvenida();
			}
		}
	};
	
	@Override
	protected void onStart() {
		super.onStart();
		
		//LocalBroadcastManager.getInstance(this).registerReceiver(syncReceiver,
		//		new IntentFilter(EncuestasSyncHelper.action_sync_end));
		registerReceiver(syncReceiver,
				new IntentFilter(EncuestasSyncHelper.action_sync_end));
		
		encuestas = encuestaManager.getEncuestas();
		if(encuestas.size() == 0) {
			String sincronizando = getResources().getString(R.string.sincronizando);
			String porFavorEspere = getResources().getString(R.string.por_favor_espere);
			
			noDataWaitProgressDialog = ProgressDialog.show(this, sincronizando, porFavorEspere, true, true, new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			});
			manualSync();
		} else {
			normalSync();
			initBienvenida();
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		//LocalBroadcastManager.getInstance(this).unregisterReceiver(syncReceiver);
		unregisterReceiver(syncReceiver);
		if(noDataWaitProgressDialog != null) {
			noDataWaitProgressDialog.dismiss();
			noDataWaitProgressDialog = null;
		}
	}
	
	public void initBienvenida() {
		List<Encuesta> list = encuestaManager.getEncuestas();
		if(list.size() > 0) {
			Encuesta encuesta = list.get(0);
			String mensajeBienvenida = encuesta.getMensajeBienvenida();
			if(mensajeBienvenida != null && mensajeBienvenida.compareTo("") != 0) {
				TextView tvb = (TextView) findViewById(R.id.textViewBienvenida);
				tvb.setText(mensajeBienvenida);
				TemplateUtils.setTextColor(tvb, encuesta);
			}
			
			TemplateUtils.setDefaultBackground(this, encuesta);
			
			String imagen = encuesta.getLogo();
			Bitmap bitmap = TemplateUtils.loadImage(this, imagen);
			if(bitmap != null) {
				ImageView iv = (ImageView) findViewById(R.id.imageViewEmpresa);
				
				bitmap = TemplateUtils.resizeBitmap(bitmap, this, IMAGEN_EMPRESA_WIDTH);
				
				/*Point size = TemplateUtils.getScreenPercentage(this, IMAGEN_EMPRESA_WIDTH, -1);
				int newHeight = size.x * bitmap.getHeight() / bitmap.getWidth();
				bitmap = Bitmap.createScaledBitmap(bitmap, size.x, newHeight, false);*/
				
				iv.setImageBitmap(bitmap);
				iv.setBackgroundColor(Color.TRANSPARENT);
				//TemplateUtils.setWidthPercentage(iv, 0.40f);
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_force_sync:
			informarFinSincro = true;
			manualSync();
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
    
    public static final long MILLISECONDS_PER_SECOND = 1000L;
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 15L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
            SECONDS_PER_MINUTE *
            MILLISECONDS_PER_SECOND;
    
    public Account getAccount() {
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
    
    public void normalSync() {
        Bundle settingsBundle = new Bundle();
        ContentResolver.addPeriodicSync(getAccount(), AUTHORITY, settingsBundle, SYNC_INTERVAL);
        
        manualSync();
    }
	
	public void manualSync() {
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(getAccount(), AUTHORITY, settingsBundle);
        
        Toast.makeText(this, R.string.sincro_manual, Toast.LENGTH_SHORT).show();
	}
	
}
