package com.dssd.encuestas;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class SemaforoActivity extends CollapsingStatusBarActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.semaforo);
		
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

		   public void run() {
			  startActivity(new Intent(SemaforoActivity.this, PreguntasActivity.class));
		      finish();

		   }

		}, 5000);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(!App.isMostrarBotonTerminar()) {
			if(event.getAction() == MotionEvent.ACTION_UP) {
				finish();
				return true;
			}
		}
		
		return super.onTouchEvent(event);
	}
	
	public void terminar(View view) {
		finish();
	}
	
}