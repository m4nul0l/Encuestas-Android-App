package com.dssd.encuestas;

import com.dssd.encuestas.datos.AppConfig;
import com.dssd.encuestas.info.DeviceInfoHelper;
import com.dssd.encuestas.sync.EncuestasSyncHelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	
	ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void validar(View view) {
		
		EditText et = (EditText) findViewById(R.id.editTextUsuario);
		if(et == null || et.getText().toString().length() == 0 ) {
			Toast.makeText(this, R.string.login_edit_error, Toast.LENGTH_SHORT).show();
			return;
		}
		
		progressDialog = ProgressDialog.show(this, "Sincronizando", "Por favor espere...", true);
		
		String info = DeviceInfoHelper.getInstance(this).getDeviceInfoJSON();
		
		AsyncTask<String, String, String> tarea = new AsyncTask<String, String, String>() {
			
			@Override
			protected String doInBackground(String... params) {
				boolean res = EncuestasSyncHelper.registerUser(params[0]);
				if(res) {
					publishProgress("Dispositivo registrado");
					
					res = EncuestasSyncHelper.sendInfoUser(params[0], params[1]);
					if(res) {
						publishProgress("Se invió la información del dispositivo");
						
						AppConfig.getInstance(LoginActivity.this).setDevice(params[0]);
						
						return "ok";
					} else {
						return "Error al enviar información del dispositivo a LoyalMaker.com";
					}
				}
				
				return getResources().getString(R.string.login_invalid_device);
			}
			
			@Override
			protected void onProgressUpdate(String... values) {
				//Toast.makeText(LoginActivity.this, values[0], Toast.LENGTH_SHORT).show();
				progressDialog.setMessage(values[0]);
			}
			
			@Override
			protected void onPostExecute(String result) {
				progressDialog.dismiss();
				if(result != null) {
					if(result.equalsIgnoreCase("ok")) {
						startActivity(new Intent(LoginActivity.this, MainActivity.class));
						finish();
					} else {
						Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
					}
				}
			}
		};
		tarea.execute(et.getText().toString(), info);
		
		//startActivity(new Intent(this, MainActivity.class));
		//finish();
	}
}
