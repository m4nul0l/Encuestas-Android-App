package com.dssd.encuestas.datos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="encuestas")
public class Encuesta {
	@DatabaseField(generatedId=true)
	private long _id;
	
	@DatabaseField
	private String nombre;
	
	@DatabaseField(foreign=true)
	private Cliente cliente;
}
