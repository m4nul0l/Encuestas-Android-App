package com.dssd.encuestas;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;

import com.dssd.encuestas.datos.Encuesta;
import com.dssd.encuestas.templates.TwoColorDrawable;

public class TemplateUtils {
	
	public static void setDefaultBackground(Activity activity, Encuesta encuesta) {
		setTwoColorBackground(activity, null, encuesta);
	}

	public static void setTwoColorBackground(Activity activity, Encuesta encuesta) {
		setTwoColorBackground(activity, null, encuesta);
	}
	
	@SuppressWarnings("deprecation")
	public static void setTwoColorBackground(Activity activity, View contentView, Encuesta encuesta) {
		int color1 = activity.getResources().getColor(R.color.defaultColorSuperior);
		int color2 = activity.getResources().getColor(R.color.defaultColorSuperior);
		//		Color.parseColor("#DF0000"), Color.parseColor("#0000DF")
		
		if(encuesta.getColorSuperior() != null && encuesta.getColorSuperior().compareTo("") != 0) {
			color1 = Color.parseColor("#"+encuesta.getColorSuperior());
		}
		if(encuesta.getColorInferior() != null && encuesta.getColorInferior().compareTo("") != 0) {
			color2 = Color.parseColor("#"+encuesta.getColorInferior());
		}
		
		TwoColorDrawable colorDrawable = new TwoColorDrawable(color1, color2);
		
		if(contentView == null) {
			contentView = activity.findViewById(android.R.id.content);
			if(contentView instanceof FrameLayout && ((FrameLayout)contentView).getChildCount() > 0) {
				contentView = ((FrameLayout)contentView).getChildAt(0);
			}
		}
		contentView.setBackgroundDrawable(colorDrawable);
	}
}
