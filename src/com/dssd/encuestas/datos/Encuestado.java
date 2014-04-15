package com.dssd.encuestas.datos;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="encuestados")
public class Encuestado {
	@DatabaseField(generatedId=true)
	private long _id;
	
	@DatabaseField(foreign=true)
	private Encuesta encuesta;
	
	@DatabaseField
	private String nombre;
	
	@DatabaseField
	private String email;
	
	@DatabaseField
	private String telefono;
	
	@DatabaseField
	private String comentario;
	
	@ForeignCollectionField
	ForeignCollection<Respuesta> respuestas;
	
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public ForeignCollection<Respuesta> getRespuestas() {
		return respuestas;
	}
}
