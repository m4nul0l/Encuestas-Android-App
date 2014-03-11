package com.dssd.encuestas;

import java.io.File;
import java.util.List;

import com.dssd.encuestas.datos.EncuestaManager;
import com.dssd.encuestas.datos.TipoPregunta;
import com.dssd.encuestas.datos.TipoPreguntaOpcion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class PreguntaValoresFragment extends PreguntaFragment {
	
	List<String> valores;
	
	public PreguntaValoresFragment() {
	}
	
	TipoPreguntaOpcion[] getOpciones() {
		EncuestaManager encuestaManager = new EncuestaManager(getActivity());
		
		TipoPregunta tipoPregunta = getPregunta().getTipoPregunta();
		encuestaManager.refreshTipoPregunta(tipoPregunta);
		
		TipoPreguntaOpcion[] opcionesArray = tipoPregunta.getOpcionesArray();
		
		return opcionesArray;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
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
			if(imagen != null) {
				File dir = getActivity().getDir("ratings", Context.MODE_PRIVATE);
				File file = new File(dir, imagen);
				
				Bitmap bitmap = BitmapFactory.decodeFile(file.toString());
				
				ImageButton b = new ImageButton(getActivity());
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
