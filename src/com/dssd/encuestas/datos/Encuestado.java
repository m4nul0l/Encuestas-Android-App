package com.dssd.encuestas.datos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="encuestados")
public class Encuestado {
	@DatabaseField(id=true)
	private long _id;

	@DatabaseField(foreign=true)
	private Encuesta encuesta;
}
