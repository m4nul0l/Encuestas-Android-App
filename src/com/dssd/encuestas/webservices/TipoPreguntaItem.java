package com.dssd.encuestas.webservices;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="item")
public class TipoPreguntaItem {
	@Element
	long idTipoPregunta;
	
	@Element
	String nombre;
}
