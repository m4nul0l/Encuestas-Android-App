package com.dssd.encuestas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

public class PreguntaSiNoFragment extends PreguntaFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		
		FrameLayout opcionesLayout = (FrameLayout) v.findViewById(R.id.frameLayoutOpciones);
		
		inflater.inflate(R.layout.opciones_si_no, opcionesLayout);
		
		View.OnClickListener l = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PreguntasActivity activity = (PreguntasActivity) getActivity();
				activity.responderPregunta(null);
			}
		};
		
		Button bSi = (Button) opcionesLayout.findViewById(R.id.opcionesButtonSi);
		bSi.setOnClickListener(l);
		Button bNo = (Button) opcionesLayout.findViewById(R.id.opcionesButtonNo);
		bNo.setOnClickListener(l);
		
		return v;
	}
}
