package com.dssd.encuestas.webservices;

public class Result {
	
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
