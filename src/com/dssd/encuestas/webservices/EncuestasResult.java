package com.dssd.encuestas.webservices;

import java.util.List;

import org.simpleframework.xml.ElementList;

import android.content.ContentValues;

public class EncuestasResult extends ItemsResult<EncuestaItem> {
	
	@ElementList
	private List<EncuestaItem> items;
	
	@Override
	public List<EncuestaItem> getItems() {
		return items;
	}

	@Override
	public String getTableName() {
		return "encuestas";
	}
	
	@Override
	public ContentValues getContentValues(EncuestaItem item) {
		ContentValues cv = new ContentValues();
		cv.put("_id", item.idEncuesta);
		cv.put("idEmpresa", item.idEmpresa);
		cv.put("nombre", item.nombre);
		cv.put("mensajeBienvenida", item.mensajeBienvenida);
		cv.put("mensajeDespedida", item.mensajeDespedida);
		cv.put("letra", item.letra);
		cv.put("imagenFondo", item.imagenFondo);
		cv.put("colorFuente", item.colorFuente);
		cv.put("logo", item.logo);
		cv.put("tiempoReinicio", item.tiempoReinicio);
		cv.put("datos", (item.datos > 0));
		cv.put("comentarios", (item.comentarios > 0));
		cv.put("validacion", (item.validacion > 0));
		cv.put("semaforo", (item.semaforo > 0));
		cv.put("mensajeInhabilitada", item.mensajeInhabilitada);
		cv.put("cantidadTiempo", item.cantidadTiempo);
		cv.put("cantidadEncuestas", item.cantidadEncuestas);
		return cv;
	}
}
