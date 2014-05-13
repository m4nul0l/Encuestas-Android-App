package com.dssd.encuestas;

import java.util.List;

import com.dssd.encuestas.datos.AppConfig;
import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.EncuestaManager;
import com.dssd.encuestas.sync.EncuestasSyncHelper;
import com.dssd.encuestas.sync.WebSyncHelper;

import android.os.Bundle;
import android.os.Handler;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends CollapsingStatusBarActivity {
	
	ProgressDialog noDataWaitProgressDialog;
	List<Encuesta> encuestas;
	static final int sync_retry_wait = 10000;
	
	View contentView;
	EncuestaManager encuestaManager;
	
	transient boolean informarFinSincro = false;
	
	static final float IMAGEN_EMPRESA_WIDTH = 0.3f;
	static final float IMAGEN_LOYALMAKER_WIDTH = 0.4f;
	
	boolean mostrarLogoLoyalMaker = true;
	
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
		/*TemplateUtils.setWidthPercentage(findViewById(R.id.imageViewEmpresa), 0.50f);*/
		
		if(mostrarLogoLoyalMaker) {
			TemplateUtils.setWidthPercentage(findViewById(R.id.imageViewLogo), IMAGEN_LOYALMAKER_WIDTH);
		} else {
			findViewById(R.id.imageViewLogo).setVisibility(View.GONE);
		}
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
			
			Bitmap bitmap = TemplateUtils.loadImagePercResize(this, imagen, IMAGEN_EMPRESA_WIDTH, -1);
			if(bitmap != null) {
				ImageView iv = (ImageView) findViewById(R.id.imageViewEmpresa);
				
				iv.setImageBitmap(bitmap);
				iv.setBackgroundColor(Color.TRANSPARENT);
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
	
	@Override
	public void onBackPressed() {
		final EditText et = new EditText(this);
		final String device = AppConfig.getInstance(this).getDevice();
		//final String password = new StringBuilder(device).reverse().toString();
		final String password = device;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.contrasena_ingresar)
		       .setTitle(R.string.salir)
		       .setView(et)
		       .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
		    	   public void onClick(DialogInterface dialog, int id) {
		    		   if(et.getText().toString().compareTo(password) == 0) {
			    		   Intent i = new Intent(Intent.ACTION_MAIN);
			    		   i.addCategory(Intent.CATEGORY_HOME);
			    		   i.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
			    		   startActivity(Intent.createChooser(i, getResources().getString(R.string.seleccione_launcher)));
		    		   } else {
		    			   Toast.makeText(MainActivity.this, R.string.contrasena_incorrecta, Toast.LENGTH_SHORT).show();
		    		   }
		    	   }
		       })
		       .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   dialog.dismiss();
		           }
		       });		
		AlertDialog dialog = builder.create();		
		dialog.show();
	}
    
    public void normalSync() {
    	WebSyncHelper.getInstance().configurePeriodicSync(this);
    }
	
	public void manualSync() {
		WebSyncHelper.getInstance().requestSync(this);
        
        Toast.makeText(this, R.string.sincro_manual, Toast.LENGTH_SHORT).show();
	}
}
