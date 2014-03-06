package com.dssd.encuestas.webservices;

import java.util.List;

import org.simpleframework.xml.ElementList;

import android.content.ContentValues;

public class PreguntasResult extends ItemsResult<PreguntaItem> {
	
	@ElementList
	private List<PreguntaItem> items;
	
	@Override
	public List<PreguntaItem> getItems() {
		return items;
	}

	@Override
	public String getTableName() {
		return "preguntas";
	}
	
	@Override
	public ContentValues getContentValues(PreguntaItem item) {
		ContentValues cv = new ContentValues();
		cv.put("_id", item.idPregunta);
		cv.put("encuesta_id", item.idEncuesta);
		cv.put("pregunta", item.pregunta);
		cv.put("tipo", item.tipo);
		cv.put("orden", item.orden);
		cv.put("activo", item.activo);
		cv.put("tipoPregunta_id", item.idTipoPregunta);
		return cv;
	}
}
