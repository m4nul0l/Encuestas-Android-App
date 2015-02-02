package com.dssd.encuestas;

import java.util.List;

import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.EncuestaManager;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.graphics.Typeface;

public class InhabilitarActivity extends CollapsingStatusBarActivity {
	
	boolean guardado = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inhabilitar);
		
		EncuestaManager encuestaManager = new EncuestaManager(this);
		List<Encuesta> list = encuestaManager.getEncuestas();
		if(list.size() > 0) {
			Encuesta encuesta = list.get(0);
			TemplateUtils.setLogoEmpresa(this, encuesta, (ImageView)findViewById(R.id.imageViewEmpresa), 0.2f);
			
			TemplateUtils.setTextColor((TextView)findViewById(R.id.textViewInabilitar), encuesta);
			
		}
		
		Typeface tf = TemplateUtils.getFontRokkittRegular(this);
		((TextView)findViewById(R.id.textViewInabilitar)).setTypeface(tf);
		
		TemplateUtils.setFontPercentage((TextView)findViewById(R.id.textViewInabilitar), TemplateUtils.GLOBAL_TEXT_SIZE);
		
		if(App.isMostrarLogoLoyalMaker()) {
			TemplateUtils.setWidthPercentage(findViewById(R.id.imageViewLogo), 0.2f);
		} else {
			findViewById(R.id.imageViewLogo).setVisibility(View.GONE);
		}
		
		setMensajeInhabilitar();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {
			finish();
			return true;
		}
		
		return super.onTouchEvent(event);
	}
	
	public void setMensajeInhabilitar() {
		EncuestaManager encuestaManager = new EncuestaManager(this);
		List<Encuesta> list = encuestaManager.getEncuestas();
		if(list.size() > 0) {
			Encuesta encuesta = list.get(0);
			String mensajeInhabilitada = encuesta.getMensajeInhabilitada();
			if(mensajeInhabilitada != null && mensajeInhabilitada.compareTo("") != 0) {
				TextView tvb = (TextView) findViewById(R.id.textViewInabilitar);
				tvb.setText(mensajeInhabilitada);
			}
			TemplateUtils.setDefaultBackground(this, encuesta);
		}
	}
	
	public void terminar(View view) {
		finish();
	}
	
	public void habilitarAction() {
		startActivity(new Intent(this, ValidacionActivity.class));
		finish();
	}
	
	public void habilitar(View view) {
		habilitarAction();
	}
	
}
