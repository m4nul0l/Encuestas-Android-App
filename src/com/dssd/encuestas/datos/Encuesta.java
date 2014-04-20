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
	private String imagenFondo;
	
	@DatabaseField
	private String colorFuente;
	
	@DatabaseField
	private String logo;
	
	@DatabaseField
	String tiempoReinicio;
	
	@DatabaseField
	boolean datos;
	
	@DatabaseField
	boolean comentarios;
	
	
	@ForeignCollectionField
	ForeignCollection<Pregunta> preguntas;

	public Long get_id() {
		return _id;
	}
	
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
	
	public String getImagenFondo() {
		return imagenFondo;
	}
	
	public String getColorFuente() {
		return colorFuente;
	}
	
	public String getLogo() {
		return logo;
	}
	
	public String getTiempoReinicio() {
		return tiempoReinicio;
	}
	
	public boolean isDatos() {
		return datos;
	}
	
	public boolean isComentarios() {
		return comentarios;
	}
}
