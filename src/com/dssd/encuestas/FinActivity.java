package com.dssd.encuestas;

import java.util.List;

import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.EncuestaManager;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Typeface;

public class FinActivity extends CollapsingStatusBarActivity {
	
	boolean botonTerminar = true;
	boolean guardado = false;
	boolean mostrarLogoLoyalMaker = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fin);
		
		EncuestaManager encuestaManager = new EncuestaManager(this);
		List<Encuesta> list = encuestaManager.getEncuestas();
		if(list.size() > 0) {
			Encuesta encuesta = list.get(0);
			TemplateUtils.setLogoEmpresa(this, encuesta, (ImageView)findViewById(R.id.imageViewEmpresa), 0.2f);
			
			TemplateUtils.setTextColor((TextView)findViewById(R.id.textViewDespedida), encuesta);
			
			if(!botonTerminar) {
				encuesta.setDatos(false);
				encuesta.setComentarios(false);
			}
			
			if(encuesta.isDatos()) {
				findViewById(R.id.entrevistadoComentarios).setVisibility(View.GONE);
			} else if(encuesta.isComentarios()) {
				findViewById(R.id.entrevistadoDatos).setVisibility(View.GONE);
			} else {
				findViewById(R.id.entrevistadoDatos).setVisibility(View.GONE);
				findViewById(R.id.entrevistadoComentarios).setVisibility(View.GONE);
				if(!botonTerminar) {
					findViewById(R.id.ImageButtonTerminar).setVisibility(View.GONE);
				}
				guardar();
			}
		}
		
		Typeface tf = TemplateUtils.getFontRokkittRegular(this);
		((TextView)findViewById(R.id.textViewDespedida)).setTypeface(tf);
		
		TemplateUtils.setFontPercentage((TextView)findViewById(R.id.textViewDespedida), TemplateUtils.GLOBAL_TEXT_SIZE);
		
		if(mostrarLogoLoyalMaker) {
			TemplateUtils.setWidthPercentage(findViewById(R.id.imageViewLogo), 0.2f);
		} else {
			findViewById(R.id.imageViewLogo).setVisibility(View.GONE);
		}
		
		setMensajeDespedida();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!botonTerminar) {
			if(event.getAction() == MotionEvent.ACTION_UP) {
				finish();
				return true;
			}
		}
		
		return super.onTouchEvent(event);
	}
	
	public void setMensajeDespedida() {
		EncuestaManager encuestaManager = new EncuestaManager(this);
		List<Encuesta> list = encuestaManager.getEncuestas();
		if(list.size() > 0) {
			Encuesta encuesta = list.get(0);
			String mensajeDespedida = encuesta.getMensajeDespedida();
			if(mensajeDespedida != null && mensajeDespedida.compareTo("") != 0) {
				TextView tvb = (TextView) findViewById(R.id.textViewDespedida);
				tvb.setText(mensajeDespedida);
			}
			TemplateUtils.setDefaultBackground(this, encuesta);
		}
	}
	
	public void terminar(View view) {
		finish();
	}
	
	@Override
	protected void onDestroy() {
		if(isFinishing()) {
			guardar();
		}
		super.onDestroy();
	}
	
	public void guardar() {
		Bundle extras = getIntent().getExtras();
		if(!guardado && extras != null && extras.containsKey("respuestas")) {
			String[] respuestas = extras.getStringArray("respuestas");
			
			String nombre = ((EditText)findViewById(R.id.editTextNombre)).getText().toString();
			String email = ((EditText)findViewById(R.id.editTextEmail)).getText().toString();
			String telefono = ((EditText)findViewById(R.id.editTextTelefono)).getText().toString();
			String comentarios = ((EditText)findViewById(R.id.entrevistadoComentarios)).getText().toString();
			
			EncuestaManager em = new EncuestaManager(this);
			em.guardarRespuestas(nombre, email, telefono, comentarios, null, respuestas);
		}
		guardado = true;
	}
}
