package com.dssd.encuestas;

import java.util.List;

import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.EncuestaManager;

import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import android.app.Activity;

public class FinActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fin);
		
		setMensajeDespedida();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP) {
			finish();
			return true;
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
}
