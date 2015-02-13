package com.dssd.encuestas;

import java.util.List;

import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.EncuestaManager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SemaforoActivity extends CollapsingStatusBarActivity {
	
	EncuestaManager encuestaManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semaforo);
		encuestaManager = new EncuestaManager(this);
		
		List<Encuesta> list = encuestaManager.getEncuestas();
		if(list.size() > 0) {
			Encuesta encuesta = list.get(0);
			TemplateUtils.setLogoEmpresa(this, encuesta, (ImageView)findViewById(R.id.imageViewEmpresa), 0.2f);
		}
		
		if(App.isMostrarLogoLoyalMaker()) {
			TemplateUtils.setWidthPercentage(findViewById(R.id.imageViewLogo), 0.2f);
		} else {
			findViewById(R.id.imageViewLogo).setVisibility(View.GONE);
		}
		
		boolean semaforoRojo = getIntent().getBooleanExtra("semaforoRojo", false);
		boolean semaforoVerde = getIntent().getBooleanExtra("semaforoVerde", false);
		
		if (semaforoVerde && !semaforoRojo) {
			//modificar imagen a semaforo verde
			
			ImageView img = (ImageView) findViewById(R.id.imageViewSemaforoVerde);
			img.setImageResource(R.drawable.verde_activo);
			
			TextView textView = (TextView) findViewById(R.id.semaforoDescripcion);
			textView.setText("");
		}else if (!semaforoVerde && semaforoRojo) {
			//modificar imagen a semaforo rojo
			ImageView img = (ImageView) findViewById(R.id.imageViewSemaforoRojo);
			img.setImageResource(R.drawable.rojo_activo);
			
			TextView textView = (TextView) findViewById(R.id.semaforoDescripcion);
			textView.setText(getTextInhabilitada());
		}
	}

	private CharSequence getTextInhabilitada() {
		List<Encuesta> list = encuestaManager.getEncuestas();
		if(list.size() > 0) {
			Encuesta encuesta = list.get(0);
			return encuesta.getMensajeInhabilitada();
		}
		
		return "";
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {
			boolean semaforoRojo = getIntent().getBooleanExtra("semaforoRojo", false);
			boolean semaforoVerde = getIntent().getBooleanExtra("semaforoVerde", false);
			
			if (semaforoVerde && !semaforoRojo) {
				startActivity(new Intent(this, PreguntasActivity.class));
			}else if (!semaforoVerde && semaforoRojo) {
				startActivity(new Intent(this, ValidacionActivity.class));
				
			}
			finish();
			return true;
		}
		
		return super.onTouchEvent(event);
	}
	
	public void terminar(View view) {
		finish();
	}
	
}