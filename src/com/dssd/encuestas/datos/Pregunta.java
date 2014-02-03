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
	String tipo;

	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	
}
