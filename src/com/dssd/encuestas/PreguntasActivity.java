package com.dssd.encuestas;

import java.util.List;

import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.EncuestaManager;
import com.dssd.encuestas.datos.Pregunta;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;

public class PreguntasActivity extends FragmentActivity {
	
	Pregunta[] preguntas;
	String[] respuestas;
	int preguntaActual = -1;
	EncuestaManager encuestaManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preguntas);
		
		encuestaManager = new EncuestaManager(this);
		
		initPreguntas();
		mostrarSiguientePregunta();
	}
	
	public void initPreguntas() {
		List<Encuesta> list = encuestaManager.getEncuestas();
		if(list.size() > 0) {
			Encuesta encuesta = list.get(0);
			TemplateUtils.setDefaultBackground(this, encuesta);
			preguntas = encuesta.getPreguntasArray();
			respuestas = new String[preguntas.length];
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preguntas, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.cerrar_preguntas_esta_seguro)
			.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			})
			.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// no hacer nada
				}
			});
		builder.show();
	}
	
	public Pregunta getSiguientePregunta() {
		Pregunta pregunta = null;
		
		if((preguntaActual+1) < preguntas.length) {
			pregunta = preguntas[++preguntaActual];
		}
		
		return pregunta;
	}
	
	public void mostrarSiguientePregunta() {
		Pregunta p = getSiguientePregunta();
		
		if(p != null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			
			PreguntaFragment fragment;
			fragment = new PreguntaValoresFragment();
			fragment.setPregunta(p);
			
			fragmentTransaction.replace(R.id.preguntasMainLayout, fragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		} else {
			// no hay mas preguntas
			Intent i = new Intent(this, FinActivity.class);
			i.putExtra("respuestas", respuestas);
			startActivity(i);
			finish();
		}
	}
	
	public void responderPregunta(String respuesta) {
		respuestas[preguntaActual] = respuesta;
		mostrarSiguientePregunta();
	}
}
