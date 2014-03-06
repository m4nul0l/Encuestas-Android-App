package com.dssd.encuestas.webservices;

import java.util.List;

import org.simpleframework.xml.ElementList;

import android.content.ContentValues;

public class TiposPreguntasResult extends ItemsResult<TipoPreguntaItem> {
	
	@ElementList
	private List<TipoPreguntaItem> items;
	
	@Override
	public List<TipoPreguntaItem> getItems() {
		return items;
	}

	@Override
	public String getTableName() {
		return "tipospreguntas";
	}
	
	@Override
	public ContentValues getContentValues(TipoPreguntaItem item) {
		ContentValues cv = new ContentValues();
		cv.put("_id", item.idTipoPregunta);
		cv.put("nombre", item.nombre);
		return cv;
	}
}
