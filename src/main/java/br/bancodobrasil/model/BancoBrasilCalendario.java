package br.bancodobrasil.model;

import java.io.Serializable;
import java.util.Objects;

public class BancoBrasilCalendario implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public BancoBrasilCalendario() {
	}

	public BancoBrasilCalendario(Integer expiracao) {
		this.expiracao = expiracao;
	}
	
	

	



	private Integer expiracao;

	

	public Integer getExpiracao() {
		return expiracao;
	}

	public void setExpiracao(Integer expiracao) {
		this.expiracao = expiracao;
	}
	
	
	
	@Override
	public int hashCode() {
		return Objects.hash(expiracao);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BancoBrasilCalendario other = (BancoBrasilCalendario) obj;
		return Objects.equals(expiracao, other.expiracao);
	}
}
