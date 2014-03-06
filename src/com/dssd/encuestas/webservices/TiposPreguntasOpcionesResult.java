package com.dssd.encuestas.webservices;

import java.util.List;

import org.simpleframework.xml.ElementList;

import android.content.ContentValues;

public class TiposPreguntasOpcionesResult extends ItemsResult<TipoPreguntaOpcionItem> {
	
	@ElementList
	private List<TipoPreguntaOpcionItem> items;
	
	@Override
	public List<TipoPreguntaOpcionItem> getItems() {
		return items;
	}

	@Override
	public String getTableName() {
		return "tipospreguntas_opciones";
	}
	
	@Override
	public ContentValues getContentValues(TipoPreguntaOpcionItem item) {
		ContentValues cv = new ContentValues();
		cv.put("_id", item.idTipoOpcion);
		cv.put("tipoPregunta_id", item.idTipoPregunta);
		cv.put("valor", item.valor);
		cv.put("imagen", item.imagen);
		return cv;
	}
}
