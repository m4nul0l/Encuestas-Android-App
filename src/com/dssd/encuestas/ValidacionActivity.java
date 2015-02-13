package com.dssd.encuestas;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.dssd.encuestas.datos.AppConfig;
import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.EncuestaManager;
import com.dssd.encuestas.InhabilitarActivity;

import android.os.Bundle;
import android.content.Intent;

public class ValidacionActivity extends CollapsingStatusBarActivity {
	
	EncuestaManager encuestaManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		encuestaManager = new EncuestaManager(this);
		boolean validada = validarEncuesta();
		boolean semaforo = showSemaforo();
		if(!validada && !semaforo){
			startActivity(new Intent(this, InhabilitarActivity.class));
		}else{
			Bundle localBundle = new Bundle();
			
			if (semaforo && !validada) {
				// Mostrar semadoro en rojo
				localBundle.putBoolean("semaforoRojo", true);
				Intent semaforoActivity = new Intent(this, SemaforoActivity.class);
				semaforoActivity.putExtras(localBundle);
				startActivity(semaforoActivity);
			} else if (semaforo && validada) {
				// Mostrar semadoro en verde
				localBundle.putBoolean("semaforoVerde", true);
				Intent semaforoActivity = new Intent(this, SemaforoActivity.class);
				semaforoActivity.putExtras(localBundle);
				startActivity(semaforoActivity);
			} else if (!semaforo && validada) {
				startActivity(new Intent(this, PreguntasActivity.class));
			}
		}
		finish();
	}
	private boolean showSemaforo() {
		List<Encuesta> list = encuestaManager.getEncuestas();
		if(list.size() > 0) {
			Encuesta encuesta = list.get(0);
			return encuesta.isSemaforo();
		}
		
		return false;
	}
	
	private boolean validarEncuesta() {
		Date fecha = AppConfig.getInstance(ValidacionActivity.this).getTimeoutFecha();
		boolean activate = false;
		if ( fecha != null){
			
			if(AppConfig.getInstance(ValidacionActivity.this).getTimeoutCantidadEncuestas() == 0){
				activate = false;
			}else{
				
				BigDecimal bdEncuestas = new BigDecimal(AppConfig.getInstance(ValidacionActivity.this).getTimeoutCantidadEncuestasMax());
				BigDecimal bdTiempo = new BigDecimal(AppConfig.getInstance(ValidacionActivity.this).getTimeoutCantidadTiempo());
				
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
					AppConfig.getInstance(ValidacionActivity.this).setTimeout(AppConfig.getInstance(ValidacionActivity.this).getTimeoutCantidadTiempo(), 
							AppConfig.getInstance(ValidacionActivity.this).getTimeoutCantidadEncuestasMax(), null);
					activate = true;
				}

			}
			
		}else{
			activate = true;
		}
		
		return activate;
	}
	
}
