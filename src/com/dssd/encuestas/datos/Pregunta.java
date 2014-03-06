package com.dssd.encuestas.datos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="preguntas")
public class Pregunta {
	@DatabaseField(id=true)
	private long _id;
	
	@DatabaseField(foreign=true)
	private Encuesta encuesta;
	
	@DatabaseField
	String pregunta;
	
	@DatabaseField
	long tipo;

	@DatabaseField
	long orden;
	
	@DatabaseField
	boolean activo;
	
	@DatabaseField(foreign=true)
	private TipoPregunta tipoPregunta;
	
	public Encuesta getEncuesta() {
		return encuesta;
	}

	public void setEncuesta(Encuesta encuesta) {
		this.encuesta = encuesta;
	}

	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
}
