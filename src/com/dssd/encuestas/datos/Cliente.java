package com.dssd.encuestas.datos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="clientes")
public class Cliente {
	@DatabaseField(generatedId=true)
	long _id;
}
