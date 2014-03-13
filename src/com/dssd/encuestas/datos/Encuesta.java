package com.dssd.encuestas.datos;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="encuestas")
public class Encuesta {
	@DatabaseField(id=true)
	private long _id;
	
	@DatabaseField
	private long idEmpresa;
	
	@DatabaseField
	private String nombre;
	
	@DatabaseField
	private String mensajeBienvenida;
	
	@DatabaseField
	private String mensajeDespedida;
	
	@DatabaseField
	private String letra;
	
	@DatabaseField
	private String colorSuperior;
	
	@DatabaseField
	private String colorInferior;
	
	@DatabaseField
	private String logo;
	
	@ForeignCollectionField
	ForeignCollection<Pregunta> preguntas;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	
	public String getMensajeBienvenida() {
		return mensajeBienvenida;
	}
	
	public String getMensajeDespedida() {
		return mensajeDespedida;
	}
	
	public String getColorSuperior() {
		return colorSuperior;
	}

	public void setColorSuperior(String colorSuperior) {
		this.colorSuperior = colorSuperior;
	}

	public String getColorInferior() {
		return colorInferior;
	}

	public void setColorInferior(String colorInferior) {
		this.colorInferior = colorInferior;
	}

	public String getLogo() {
		return logo;
	}
}
