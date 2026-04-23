package br.safrapay.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.omnifaces.optimusfaces.test.model.LocalGeneratedIdEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * 
 * 
  * 
 */


public class SafraPayCustomerPhone extends LocalGeneratedIdEntity {

	private static final long serialVersionUID = 1L;

	
	private String countryCode; //": "55",
	
	
	private String areaCode; //": "21",
	
	
	private String number; //": "999999999",
	
	
	//1	Residencial	Telefone Residencial.
	//2	Commercial	Telefone Comercial.
	//3	Voicemail	Correio de Voz.
	//4	Temporary	Telefone Temporário.
	//5	Mobile	Celular.
	
	
	
	private Integer type; 
	
	
	@JsonIgnore
	private SafraPayCustomer customer;
	
	public String getCountryCode() {
		return countryCode;
	}
	
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public SafraPayCustomer getCustomer() {
		return customer;
	}

	public void setCustomer(SafraPayCustomer customer) {
		this.customer = customer;
	}
	      
	
	
		

}
