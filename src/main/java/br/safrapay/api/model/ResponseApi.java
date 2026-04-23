package br.safrapay.api.model;

public class ResponseApi {

	
	private String traceKey;
	private ResponseApiErrors erros;
	private Boolean sucess;
	
	public String getTraceKey() {
		return traceKey;
	}
	public void setTraceKey(String traceKey) {
		this.traceKey = traceKey;
	}
	public ResponseApiErrors getErros() {
		return erros;
	}
	public void setErros(ResponseApiErrors erros) {
		this.erros = erros;
	}
	public Boolean getSucess() {
		return sucess;
	}
	public void setSucess(Boolean sucess) {
		this.sucess = sucess;
	}
	
	
	
}
