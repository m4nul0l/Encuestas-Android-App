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
	
	@DatabaseField
	boolean validacion;
	
	@DatabaseField
	boolean semaforo;
	
	@DatabaseField
	String mensajeInhabilitada;
	
	@DatabaseField
	String cantidadTiempo;
	
	@DatabaseField
	String cantidadEncuestas;
	
	
	@ForeignCollectionField
	ForeignCollection<Pregunta> preguntas;

	public Long get_id() {
		return _id;
	}
	
	public boolean isValidacion() {
		return validacion;
	}

	public void setValidacion(boolean validacion) {
		this.validacion = validacion;
	}

	public boolean isSemaforo() {
		return semaforo;
	}

	public void setSemaforo(boolean semaforo) {
		this.semaforo = semaforo;
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
	
	public Integer getTiempoReinicioInteger() {
		Integer res = null;
		try {
			res = Integer.parseInt(tiempoReinicio);
		} catch(NumberFormatException e) {
			return 0;
		}
		return res;
	}
	
	public void setDatos(boolean datos) {
		this.datos = datos;
	}
	
	public boolean isDatos() {
		return datos;
	}
	
	public void setComentarios(boolean comentarios) {
		this.comentarios = comentarios;
	}
	
	public boolean isComentarios() {
		return comentarios;
	}

	public String getMensajeInhabilitada() {
		return mensajeInhabilitada;
	}

	public void setMensajeInhabilitada(String mensajeInhabilitada) {
		this.mensajeInhabilitada = mensajeInhabilitada;
	}

	public String getCantidadTiempo() {
		return cantidadTiempo;
	}
	
	public String getCantidadEncuestas() {
		return cantidadEncuestas;
	}
	
	public Integer getCantidadTiempoInteger() {
		Integer res = null;
		try {
			res = Integer.parseInt(cantidadTiempo);
		} catch(NumberFormatException e) {
			return 0;
		}
		return res;
	}
	
	public Integer getCantidadEncuestasInteger() {
		Integer res = null;
		try {
			res = Integer.parseInt(cantidadEncuestas);
		} catch(NumberFormatException e) {
			return 0;
		}
		return res;
	}

	public void setCantidadTiempo(String cantidadTiempo) {
		this.cantidadTiempo = cantidadTiempo;
	}

	public void setTiempoReinicio(String tiempoReinicio) {
		this.tiempoReinicio = tiempoReinicio;
	}
	
	
}
