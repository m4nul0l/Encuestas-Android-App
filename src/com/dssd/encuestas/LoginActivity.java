package com.dssd.encuestas;

import com.dssd.encuestas.info.DeviceInfoHelper;
import com.dssd.encuestas.sync.EncuestasSyncHelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

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
		String info = DeviceInfoHelper.getInstance(this).getDeviceInfoJSON();
		
		AsyncTask<String, String, String> tarea = new AsyncTask<String, String, String>() {
			
			@Override
			protected String doInBackground(String... params) {
				boolean res = EncuestasSyncHelper.registerUser(params[0]);
				if(res) {
					publishProgress("Se validó el usuario");
					
					res = EncuestasSyncHelper.sendInfoUser(params[0], params[1]);
					if(res) {
						publishProgress("Se invió la información del dispositivo");
					}
					
				} else {
					publishProgress("Usuario inválido");
				}
				
				return null;
			}
			
			@Override
			protected void onProgressUpdate(String... values) {
				Toast.makeText(LoginActivity.this, values[0], Toast.LENGTH_SHORT).show();
			}
		};
		tarea.execute(et.getText().toString(), info);
		
		//startActivity(new Intent(this, MainActivity.class));
		//finish();
	}
}
