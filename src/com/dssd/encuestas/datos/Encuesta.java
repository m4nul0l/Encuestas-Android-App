package com.dssd.encuestas.datos;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="encuestas")
public class Encuesta {
	@DatabaseField(generatedId=true)
	private long _id;
	
	@DatabaseField
	private String nombre;
	
	@DatabaseField(foreign=true)
	private Cliente cliente;
	
	@ForeignCollectionField
	ForeignCollection<Pregunta> preguntas;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ForeignCollection<Pregunta> getPreguntas() {
		return preguntas;
	}
	
	public Pregunta[] getPreguntasArray() {
		return getPreguntas().toArray(new Pregunta[0]);
	}

	public void setPreguntas(ForeignCollection<Pregunta> preguntas) {
		this.preguntas = preguntas;
	}	
}
