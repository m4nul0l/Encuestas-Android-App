package com.dssd.encuestas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dssd.encuestas.datos.AppConfig;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		encuestaManager = new EncuestaManager(this);
		
		if(!validarEncuesta()){
			System.out.println("encuesta inhabilitada");
		}
		setContentView(R.layout.activity_preguntas);
		
		initPreguntas();
		mostrarSiguientePregunta();	
	}
	
	private boolean validarEncuesta() {
		Date fecha = AppConfig.getInstance(PreguntasActivity.this).getTimeoutFecha();
		boolean activate = false;
		if ( fecha != null){
			
			if(AppConfig.getInstance(PreguntasActivity.this).getTimeoutCantidadEncuestas() == 0){
				activate = false;
			}else{
				
				BigDecimal bdEncuestas = new BigDecimal(AppConfig.getInstance(PreguntasActivity.this).getTimeoutCantidadEncuestas());
				BigDecimal bdTiempo = new BigDecimal(AppConfig.getInstance(PreguntasActivity.this).getTimeoutCantidadTiempo());
				
				BigDecimal timebd = bdEncuestas.divide(bdTiempo, 2, RoundingMode.FLOOR);
				
				Calendar cBefore = Calendar.getInstance();
				cBefore.setTime(fecha);
				int minutesBefore = cBefore.get(Calendar.MINUTE);
				int secondsBefore = cBefore.get(Calendar.SECOND);
				int hourBefore = cBefore.get(Calendar.HOUR_OF_DAY);
				int dayBefore = cBefore.get(Calendar.DAY_OF_MONTH);
				
				BigDecimal bdBefore = new BigDecimal(minutesBefore + "." + secondsBefore);
				
				Calendar cNow = Calendar.getInstance();
				cNow.setTime(new Date());
				
				int minutesNow = cNow.get(Calendar.MINUTE);
				int secondsNow = cNow.get(Calendar.SECOND);
				int hourNow = cNow.get(Calendar.HOUR_OF_DAY);
				int dayNow = cNow.get(Calendar.DAY_OF_MONTH);
				
				BigDecimal bdNow = new BigDecimal(minutesNow + "." + secondsNow);
				
				if(dayBefore == dayNow && hourBefore == hourNow){
					BigDecimal rest = bdNow.subtract(bdBefore);
					if (rest.compareTo(timebd) >= 0) {
						activate = true;
					}
				}else if(hourBefore != hourNow && dayBefore == dayNow){
					int restMinutes = 59 - minutesBefore;
					int diffMinutes = restMinutes + minutesNow;
					
					int restSeconds = secondsBefore;
					int diffSeconds = restSeconds + secondsNow;
					
					if (diffSeconds > 59) {
						diffMinutes = diffMinutes + 1; 
						restSeconds = 59 - secondsBefore;
						diffSeconds = restSeconds + secondsNow;
						if (diffSeconds > 59) {
							diffSeconds = restSeconds;
						}
					}
					
					BigDecimal diff = new BigDecimal(diffMinutes + "." + diffSeconds);
					if (diff.compareTo(timebd) >= 0) {
						activate = true;
					}
					
				}else if(dayBefore != dayNow){
					AppConfig.getInstance(PreguntasActivity.this).setTimeout(AppConfig.getInstance(PreguntasActivity.this).getTimeoutCantidadTiempo(), 
							AppConfig.getInstance(PreguntasActivity.this).getTimeoutCantidadEncuestasMax(), null);
					activate = true;
				}

			}
			
		}else{
			activate = true;
			//AppConfig.getInstance(PreguntasActivity.this).setTimeoutFecha(new Date());
		}
		
		return activate;
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
		if(respuestas[0] != null && App.isGuardarRespuestasParciales()) {
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
