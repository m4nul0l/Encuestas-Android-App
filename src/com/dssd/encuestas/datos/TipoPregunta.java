package com.dssd.encuestas.datos;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="tipospreguntas")
public class TipoPregunta {
	@DatabaseField(id=true)
	private long _id;
	
	@DatabaseField
	String nombre;
	
	@ForeignCollectionField
	ForeignCollection<TipoPreguntaOpcion> opciones;
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public ForeignCollection<TipoPreguntaOpcion> getOpciones() {
		return opciones;
	}
	
	public TipoPreguntaOpcion[] getOpcionesArray() {
		return getOpciones().toArray(new TipoPreguntaOpcion[0]);
	}

	public void setPreguntas(ForeignCollection<TipoPreguntaOpcion> opciones) {
		this.opciones = opciones;
	}	
}
