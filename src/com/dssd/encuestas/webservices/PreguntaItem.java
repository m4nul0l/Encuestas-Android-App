package com.dssd.encuestas.webservices;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="item", strict=false)
public class PreguntaItem {
	@Element
	long idPregunta;
	
	@Element
	long idEncuesta;
	
	@Element
	String pregunta;
	
	@Element(required=false)
	String tipo;
	
	@Element(required=false)
	long orden;
	
	@Element(required=false)
	boolean activo;
	
	@Element
	String idTipoPregunta;
}
