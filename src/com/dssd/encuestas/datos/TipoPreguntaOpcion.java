package com.dssd.encuestas.datos;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="tipospreguntas_opciones")
public class TipoPreguntaOpcion {
	@DatabaseField(id=true)
	private long _id;
	
	@DatabaseField(foreign=true)
	private TipoPregunta tipoPregunta;
	
	@DatabaseField
	String valor;
	
	@DatabaseField
	String imagen;

	public TipoPregunta getTipoPregunta() {
		return tipoPregunta;
	}

	public void setTipoPregunta(TipoPregunta tipoPregunta) {
		this.tipoPregunta = tipoPregunta;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

}
