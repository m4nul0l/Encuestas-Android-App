package com.dssd.encuestas;

import java.util.List;

import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.EncuestaManager;
import com.dssd.encuestas.datos.Pregunta;
import com.dssd.encuestas.sync.WebSyncHelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.widget.Toast;

public class PreguntasActivity extends CollapsingStatusBarActivity {
	
	Pregunta[] preguntas;
	String[] respuestas;
	int preguntaActual = -1;
	EncuestaManager encuestaManager;
	int tiempoReinicio = 0;
	
	boolean guardarRespuestasParciales = true;
	
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
			
			tiempoReinicio = encuesta.getTiempoReinicioInteger();
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
					cancelarEncuesta();
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
	
	public void cancelarEncuesta() {
		// al cancelar, guardo las respuestas hasta el momento
		if(respuestas[0] != null && guardarRespuestasParciales) {
			// solo guardo si al menos hay una respuesta
			EncuestaManager em = new EncuestaManager(PreguntasActivity.this);
			em.guardarRespuestas("", "", "", "", null, respuestas);
			WebSyncHelper.getInstance().requestSync(this);
		}
		finish();
	}
	
	class TimerReinicio extends AsyncTask<Integer, Void, Void> {
		@Override
		protected Void doInBackground(Integer... params) {
			try {
				Thread.sleep(params[0] * 1000);
			} catch (InterruptedException e) {
				//
			}
			return null;
		}
		
		protected void onCancelled(Void result) {};
		protected void onPostExecute(Void result) {
			Toast.makeText(PreguntasActivity.this, R.string.reiniciar_encuesta, Toast.LENGTH_SHORT).show();
			cancelarEncuesta();
		};
	};
	TimerReinicio timer;
	@SuppressLint("NewApi")
	protected void startTimer() {
		if(tiempoReinicio > 0) {
			timer = new TimerReinicio();
			executeAsyncTask(timer, tiempoReinicio);
		}
	}
	protected void stopTimer() {
		if(tiempoReinicio > 0) {
			if(timer != null) {
				timer.cancel(true);
				timer = null;
			}
		}
	}
	protected void resetTimer() {
		stopTimer();
		startTimer();
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
			resetTimer();
			
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			
			PreguntaFragment fragment;
			fragment = new PreguntaValoresFragment();
			fragment.setPregunta(p);
			
			fragmentTransaction.replace(R.id.preguntasMainLayout, fragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		} else {
			stopTimer();
			
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
