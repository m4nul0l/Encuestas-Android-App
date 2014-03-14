package com.dssd.encuestas;

import java.util.List;

import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.EncuestaManager;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;

public class FinActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fin);
		
		EncuestaManager encuestaManager = new EncuestaManager(this);
		List<Encuesta> list = encuestaManager.getEncuestas();
		if(list.size() > 0) {
			Encuesta encuesta = list.get(0);
			TemplateUtils.setLogoEmpresa(this, encuesta, (ImageView)findViewById(R.id.imageViewEmpresa), 0.2f);
		}
		
		TemplateUtils.setFontPercentage((TextView)findViewById(R.id.textViewDespedida), TemplateUtils.GLOBAL_TEXT_SIZE);
		TemplateUtils.setWidthPercentage(findViewById(R.id.imageViewLogo), 0.2f);
		
		setMensajeDespedida();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		/*if(event.getAction() == MotionEvent.ACTION_UP) {
			finish();
			return true;
		}*/
		
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
}
