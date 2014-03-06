package com.dssd.encuestas.webservices;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="xml")
public class Result {
	
	@Element
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public boolean isOk() {
		return (result != null && result.compareTo("ok") == 0);
	}
}
