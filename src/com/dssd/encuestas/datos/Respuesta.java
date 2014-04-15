package com.dssd.encuestas.datos;

import java.util.Date;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="respuestas")
public class Respuesta {
	@DatabaseField(generatedId=true)
	private long _id;
	
	@DatabaseField(foreign=true)
	private Pregunta pregunta;
	
	@DatabaseField(foreign=true)
	private Encuestado encuestado;
	
	@DatabaseField
	String respuesta;
	
	@DatabaseField
	Date fecha;
	
	public long get_id() {
		return _id;
	}
	
	public Pregunta getPregunta() {
		return pregunta;
	}

	public void setPregunta(Pregunta pregunta) {
		this.pregunta = pregunta;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	
	public Date getFecha() {
		return fecha;
	}
	
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public void setEncuestado(Encuestado encuestado) {
		this.encuestado = encuestado;
	}
}
