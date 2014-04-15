package com.dssd.encuestas;

import java.util.List;

import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.datos.EncuestaManager;
import com.dssd.encuestas.datos.TipoPregunta;
import com.dssd.encuestas.datos.TipoPreguntaOpcion;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class PreguntaValoresFragment extends PreguntaFragment {
	
	List<String> valores;
	
	static final float IMAGEN_OPCION_WIDTH_GENERAL = 0.12f;
	static final float IMAGEN_OPCION_HEIGHT_GENERAL = 0.53f;
	static final float IMAGEN_OPCION_WIDTH_2 = 0.2f;
	
	int selectedButton = -1;
	
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
		
		Typeface tf = TemplateUtils.getFontRokkittRegular(getActivity());
		
		TextView tvPregunta = (TextView)v.findViewById(R.id.textViewPregunta);
		tvPregunta.setTypeface(tf);
		TemplateUtils.setFontPercentage(tvPregunta, TemplateUtils.GLOBAL_TEXT_SIZE);
		TemplateUtils.setTextColor(tvPregunta, getEncuesta());
		
		TemplateUtils.setWidthPercentage(v.findViewById(R.id.imageViewLogo), 0.2f);
		TemplateUtils.setLogoEmpresa(getActivity(), getEncuesta(), (ImageView)v.findViewById(R.id.imageViewEmpresa), 0.2f);
		TemplateUtils.setWidthPercentage(v.findViewById(R.id.imageViewSiguiente), 0.09f);
		
		/* progreso encuesta */
		PreguntasActivity activity = (PreguntasActivity) getActivity();
		int actual = activity.preguntaActual+1;
		int total = activity.preguntas.length;
		TextView textViewProgreso = (TextView) v.findViewById(R.id.textViewProgreso);
		textViewProgreso.setTypeface(tf);
		TemplateUtils.setFontPercentage(textViewProgreso, TemplateUtils.GLOBAL_TEXT_SIZE);
		textViewProgreso.setText("" + actual + "/" + total);
		ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
		progressBar.setMax(total);
		progressBar.setProgress(actual);
		
		
		FrameLayout opcionesLayout = (FrameLayout) v.findViewById(R.id.frameLayoutOpciones);
		
		final LinearLayout ll = new LinearLayout(getActivity());
		opcionesLayout.addView(ll);
		
		final TipoPreguntaOpcion[] opciones = getOpciones();
		
		View.OnClickListener selectListener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// al presionarse un boton, se "apagan" los otros, configurado con el estado "checked"
				ImageButton selected = (ImageButton) view;
				for (int i = 0; i < ll.getChildCount(); i++) {
					ImageButton button = (ImageButton) ll.getChildAt(i);
					if(button == selected) {
						selectedButton = i;
					} else {
						button.getDrawable().setState(new int[]{android.R.attr.state_checked});
					}
				}
				selected.getDrawable().setState(new int[]{});
				
				/* muestro descripción al apretar el botón */
				Toast.makeText(getActivity(), opciones[selectedButton].getDescripcion(), Toast.LENGTH_SHORT).show();
			}
		};
		View.OnTouchListener tl = new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// verifico si al tocar un botón, suelto el dedo fuera del mismo, entonces vuelvo a apagar el botón
				if(event.getAction() == MotionEvent.ACTION_UP) {
					if(selectedButton >= 0) {
						ImageButton b = (ImageButton) ll.getChildAt(selectedButton);
						if(b != v) {
							ImageButton ib = (ImageButton) v;
							ib.getDrawable().setState(new int[]{android.R.attr.state_checked});
						}
					}
				}
				return false;
			}
		};
		
		//float newButtonSize = IMAGEN_OPCION_WIDTH_GENERAL;
		float newButtonHSize = IMAGEN_OPCION_HEIGHT_GENERAL;
		View.OnClickListener newButtonListener = selectListener;
		switch(opciones.length) {
		case 2:
			//newButtonSize = IMAGEN_OPCION_WIDTH_2;
			//newButtonHSize = IMAGEN_OPCION_WIDTH_2;
			//v.findViewById(R.id.imageViewSiguiente).setVisibility(View.GONE);
			//newButtonListener = siguienteListener;
			break;
		default:
			v.findViewById(R.id.imageViewSiguiente).setOnClickListener(siguienteListener);
		}
		
		
		for (TipoPreguntaOpcion opcion : opciones) {
			View newView = null;
			
			String imagenDefault = opcion.getImagenDefault();
			Bitmap bitmapDefault = TemplateUtils.loadImageRatings(getActivity(), imagenDefault);
			if(bitmapDefault != null) {
				ImageButton b = new ImageButton(getActivity());
				
				String imagenPresionada = opcion.getImagenPresionada();
				String imagenSeleccionada = opcion.getImagenSeleccionada();
				Bitmap bitmapPresionado = TemplateUtils.loadImageRatings(getActivity(), imagenPresionada);
				Bitmap bitmapSeleccionado = TemplateUtils.loadImageRatings(getActivity(), imagenSeleccionada);
				
				StateListDrawable stateListDrawable = new StateListDrawable();
				
				/* presionado */
				if(bitmapPresionado != null) {
					//bitmapPresionado = TemplateUtils.resizeBitmap(bitmapPresionado, getActivity(), newButtonSize);
					bitmapPresionado = TemplateUtils.resizeBitmapH(bitmapPresionado, getActivity(), newButtonHSize);
					stateListDrawable.addState(new int[]{android.R.attr.state_pressed}, new BitmapDrawable(getResources(), bitmapPresionado));
				}
				
				/* seleccionado - checked */
				if(bitmapSeleccionado != null) {
					//bitmapSeleccionado = TemplateUtils.resizeBitmap(bitmapSeleccionado, getActivity(), newButtonSize);
					bitmapSeleccionado = TemplateUtils.resizeBitmapH(bitmapSeleccionado, getActivity(), newButtonHSize);
					stateListDrawable.addState(new int[]{android.R.attr.state_checked}, new BitmapDrawable(getResources(), bitmapSeleccionado));
				}
				
				/* nivel inicial - imagen default */
				//bitmapDefault = TemplateUtils.resizeBitmap(bitmapDefault, getActivity(), newButtonSize);
				bitmapDefault = TemplateUtils.resizeBitmapH(bitmapDefault, getActivity(), newButtonHSize);
				stateListDrawable.addState(new int[]{}, new BitmapDrawable(getResources(), bitmapDefault));
				
				//b.setImageBitmap(bitmapDefault);
				b.setImageDrawable(stateListDrawable);
				b.setBackgroundColor(Color.TRANSPARENT);
				
				newView = b;
			} else {
				Button b = (Button) inflater.inflate(R.layout.opciones_valores_button, opcionesLayout, false);
				//Button b = new Button(getActivity());
				b.setText(opcion.getValor());
				
				newView = b;
			}
			
			newView.setOnClickListener(newButtonListener);
			newView.setOnTouchListener(tl);
			ll.addView(newView);
		}
		
		return v;
	}
	
	View.OnClickListener siguienteListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			if(selectedButton < 0) {
				Toast.makeText(getActivity(), R.string.nada_seleccionado, Toast.LENGTH_SHORT).show();
			} else {
				PreguntasActivity activity = (PreguntasActivity) getActivity();
				
				String valor = getOpciones()[selectedButton].getValor();
				
				/*Respuesta r = new Respuesta();
				r.setPregunta(pregunta);
				r.setRespuesta(valor);
				r.setFecha(new Date());*/
				
				//Toast.makeText(getActivity(), valor, Toast.LENGTH_SHORT).show();
				
				activity.responderPregunta(valor);
			}
		}
	};
}
