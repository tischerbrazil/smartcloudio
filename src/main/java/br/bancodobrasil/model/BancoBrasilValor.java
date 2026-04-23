package br.bancodobrasil.model;

import java.io.Serializable;
import java.util.Objects;

public class BancoBrasilValor implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public BancoBrasilValor(String original	) {
		this.original = original;
	}
	
	public BancoBrasilValor() {
	}

	private String original;

	public String getOriginal() {
		
		return original; 
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	@Override
	public int hashCode() {
		return Objects.hash(original);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BancoBrasilValor other = (BancoBrasilValor) obj;
		return Objects.equals(original, other.original);
	}
	
	
	
	
	
}
