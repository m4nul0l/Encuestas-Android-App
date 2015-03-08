package com.dssd.encuestas;

import java.util.List;

import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.EncuestaManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class SemaforoActivity extends CollapsingStatusBarActivity {
	
	EncuestaManager encuestaManager;
	
	static final float IMAGEN_EMPRESA_WIDTH = 0.2f;
	static final float IMAGEN_LOYALMAKER_WIDTH = 0.4f;
	
	static final float IMAGEN_OPCION_HEIGHT_GENERAL = 0.53f;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_semaforo);
		encuestaManager = new EncuestaManager(this);
		
		List<Encuesta> list = encuestaManager.getEncuestas();
		if(list.size() > 0) {
			Encuesta encuesta = list.get(0);
			TemplateUtils.setDefaultBackground(this, encuesta);
			TemplateUtils.setLogoEmpresa(this, encuesta, (ImageView)findViewById(R.id.imageViewEmpresa), IMAGEN_EMPRESA_WIDTH);
		}
		
		if(App.isMostrarLogoLoyalMaker()) {
			TemplateUtils.setWidthPercentage(findViewById(R.id.imageViewLogo), IMAGEN_LOYALMAKER_WIDTH);
		} else {
			findViewById(R.id.imageViewLogo).setVisibility(View.GONE);
		}
		
		initBotonesSemaforo();
		
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
	
	private void initBotonesSemaforo() {
		TemplateUtils.setHeightPercentage(findViewById(R.id.imageViewSemaforoVerde), IMAGEN_OPCION_HEIGHT_GENERAL);
		TemplateUtils.setHeightPercentage(findViewById(R.id.imageViewSemaforoAmarillo), IMAGEN_OPCION_HEIGHT_GENERAL);
		TemplateUtils.setHeightPercentage(findViewById(R.id.imageViewSemaforoRojo), IMAGEN_OPCION_HEIGHT_GENERAL);
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
				// nada, o sea vuelve a la pantalla anterior
				//startActivity(new Intent(this, ValidacionActivity.class));
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