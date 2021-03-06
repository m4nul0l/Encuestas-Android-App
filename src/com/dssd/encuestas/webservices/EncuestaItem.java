package com.dssd.encuestas.webservices;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="item", strict=false)
public class EncuestaItem {
	@Element
	long idEncuesta;
	
	@Element
	long idEmpresa;
	
	@Element
	long idSucursal;
	
	@Element(required=false)
	String nombre;
	
	@Element(required=false)
	String mensajeBienvenida;
	
	@Element(required=false)
	String mensajeDespedida;
	
	@Element(required=false)
	String letra;
	
	@Element(required=false)
	String imagenFondo;
	
	@Element(required=false)
	String colorFuente;
	
	@Element(required=false)
	String logo;
	
	@Element(required=false)
	String tiempoReinicio;
	
	@Element(required=false)
	int datos;
	
	@Element(required=false)
	int comentarios;
	
	@Element(required=false)
	int validacion;
	
	@Element(required=false)
	int semaforo;
	
	@Element(required=false)
	String mensajeInhabilitada;
	
	@Element(required=false)
	String cantidadTiempo;
	
	@Element(required=false)
	String cantidadEncuestas;
}
