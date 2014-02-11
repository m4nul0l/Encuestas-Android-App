package com.dssd.encuestas;

import java.util.Arrays;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class PreguntaValoresFragment extends PreguntaFragment {
	
	List<String> valores;
	
	public PreguntaValoresFragment() {
	}
	
	@Override
	public void setArguments(Bundle bundle) {
		super.setArguments(bundle);
		
		if(bundle.containsKey("valores")) {
			this.valores = bundle.getStringArrayList("valores");
			
			if(this.valores == null) {
				String[] stringArray = bundle.getStringArray("valores");
				if(stringArray != null) {
					this.valores = Arrays.asList(stringArray);
				}
			}
		}
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
				activity.responderPregunta(null);
			}
		};
		
		if(valores != null) {
			for (String valor : valores) {
				Button b = (Button) inflater.inflate(R.layout.opciones_valores_button, opcionesLayout, false);
				//Button b = new Button(getActivity());
				b.setText(valor);
				
				b.setOnClickListener(l);
				ll.addView(b);
			}
		}
		
		
		//inflater.inflate(R.layout.opciones_si_no, opcionesLayout);
		
		//Button bSi = (Button) opcionesLayout.findViewById(R.id.opcionesButtonSi);
		//bSi.setOnClickListener(l);
		//Button bNo = (Button) opcionesLayout.findViewById(R.id.opcionesButtonNo);
		//bNo.setOnClickListener(l);
		
		return v;
	}
}
