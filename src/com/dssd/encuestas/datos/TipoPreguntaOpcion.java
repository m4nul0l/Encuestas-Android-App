package com.dssd.encuestas.datos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="tipospreguntas_opciones")
public class TipoPreguntaOpcion {
	@DatabaseField(id=true)
	private long _id;
	
	@DatabaseField(foreign=true)
	private TipoPregunta tipoPregunta;
	
	@DatabaseField
	String valor;
	
	@DatabaseField
	String descripcion;
	
	@DatabaseField
	String imagenDefault;

	@DatabaseField
	String imagenPresionada;

	@DatabaseField
	String imagenSeleccionada;

	public TipoPregunta getTipoPregunta() {
		return tipoPregunta;
	}

	public String getValor() {
		return valor;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getImagenDefault() {
		return imagenDefault;
	}

	public String getImagenPresionada() {
		return imagenPresionada;
	}

	public String getImagenSeleccionada() {
		return imagenSeleccionada;
	}
}
