package com.dssd.encuestas.datos;

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
}
