package com.dssd.encuestas;

import java.util.List;

import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.EncuestaManager;
import com.dssd.encuestas.datos.TipoPregunta;
import com.dssd.encuestas.datos.TipoPreguntaOpcion;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PreguntaValoresFragment extends PreguntaFragment {
	
	List<String> valores;
	
	static final float IMAGEN_OPCION_WIDTH_GENERAL = 0.12f;
	static final float IMAGEN_OPCION_WIDTH_2 = 0.2f;
	
	public PreguntaValoresFragment() {
	}
	
	TipoPreguntaOpcion[] getOpciones() {
		EncuestaManager encuestaManager = new EncuestaManager(getActivity());
		
		TipoPregunta tipoPregunta = getPregunta().getTipoPregunta();
		encuestaManager.refreshTipoPregunta(tipoPregunta);
		
		TipoPreguntaOpcion[] opcionesArray = tipoPregunta.getOpcionesArray();
		
		return opcionesArray;
	}
	
	Encuesta getEncuesta() {
		EncuestaManager encuestaManager = new EncuestaManager(getActivity());
		
		Encuesta encuesta = getPregunta().getEncuesta();
		encuestaManager.refreshEncuesta(encuesta);
		
		return encuesta;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		TemplateUtils.setFontPercentage((TextView)v.findViewById(R.id.textViewPregunta), TemplateUtils.GLOBAL_TEXT_SIZE);
		TemplateUtils.setWidthPercentage(v.findViewById(R.id.imageViewLogo), 0.2f);
		TemplateUtils.setLogoEmpresa(getActivity(), getEncuesta(), (ImageView)v.findViewById(R.id.imageViewEmpresa), 0.2f);
		
		FrameLayout opcionesLayout = (FrameLayout) v.findViewById(R.id.frameLayoutOpciones);
		
		LinearLayout ll = new LinearLayout(getActivity());
		opcionesLayout.addView(ll);
		
		View.OnClickListener l = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PreguntasActivity activity = (PreguntasActivity) getActivity();
				
				//Respuesta r = new Respuesta();
				//r.setPregunta(pregunta);
				//r.setRespuesta();
				
				activity.responderPregunta(null);
			}
		};
		
		TipoPreguntaOpcion[] opciones = getOpciones();
		
		for (TipoPreguntaOpcion opcion : opciones) {
			View newView = null;
			String imagen = opcion.getImagen();
			
			Bitmap bitmap = TemplateUtils.loadImageRatings(getActivity(), imagen);
			if(bitmap != null) {
				ImageButton b = new ImageButton(getActivity());
				
				float newSize = IMAGEN_OPCION_WIDTH_GENERAL;
				switch(opciones.length) {
				case 2:
					newSize = IMAGEN_OPCION_WIDTH_2;
					break;
				}
				
				bitmap = TemplateUtils.resizeBitmap(bitmap, getActivity(), newSize);
				
				b.setImageBitmap(bitmap);
				b.setBackgroundColor(Color.TRANSPARENT);
				
				newView = b;
			} else {
				Button b = (Button) inflater.inflate(R.layout.opciones_valores_button, opcionesLayout, false);
				//Button b = new Button(getActivity());
				b.setText(opcion.getValor());
				
				newView = b;
			}
			
			newView.setOnClickListener(l);
			ll.addView(newView);
		}
		
		return v;
	}
}
