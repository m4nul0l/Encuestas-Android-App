package com.dssd.encuestas.webservices;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="item", strict=false)
public class TipoPreguntaOpcionItem {
	@Element
	long idTipoOpcion;
	
	@Element
	long idTipoPregunta;
	
	@Element
	String valor;
	
	@Element(required=false)
	String descripcion;
	
	@Element(required=false)
	String imagen;
}
