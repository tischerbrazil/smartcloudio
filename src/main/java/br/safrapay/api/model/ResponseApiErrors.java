package br.safrapay.api.model;

public class ResponseApiErrors { 
	
	private int erroCode;
	private String message;
	private String field;
	
	public int getErroCode() {
		return erroCode;
	}
	public void setErroCode(int erroCode) {
		this.erroCode = erroCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	
	 
}
