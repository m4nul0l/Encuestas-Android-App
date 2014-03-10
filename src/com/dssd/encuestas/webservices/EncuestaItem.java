package com.dssd.encuestas.webservices;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="item")
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
	String colorSuperior;
	
	@Element(required=false)
	String colorInferior;
	
	@Element(required=false)
	String logo;
}
