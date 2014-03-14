package com.dssd.encuestas;

import com.dssd.encuestas.datos.Pregunta;
import com.dssd.encuestas.datos.Respuesta;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PreguntaFragment extends Fragment {
	Pregunta pregunta;
	TextView textViewPregunta;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_preguntas, container, false);
		textViewPregunta = (TextView) v.findViewById(R.id.textViewPregunta);
		refreshPregunta();
		return v;
	}
	
	public void setPregunta(Pregunta p) {
		this.pregunta = p;
		refreshPregunta();
	}
	
	public Pregunta getPregunta() {
		return pregunta;
	}

	protected void refreshPregunta() {
		if(pregunta != null && textViewPregunta != null) {
			textViewPregunta.setText(pregunta.getPregunta());
		}
	}
	
	public Respuesta getRespuesta() {
		return null;
	}
}
