package br.safrapay.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.hibernate.annotations.Type;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import br.bancodobrasil.model.JsonMapStringString;

@Entity
@Table(name = "gateway_safrapay_charge")
@JsonFilter("safraPayChargeFilter")
public class SafraPayCharge implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "smartid", unique = true, nullable = false)
	private Integer smartid;
	
	private String merchantChargeId;
	
	
	
	//==========
	@JsonProperty("customer")
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "smartid")
	private SafraPayCustomer safraPayCustomer;
	//===========
	
	@OneToMany(mappedBy = "charge", fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JsonProperty("transactions")
	private List<SafraPayTransaction> transactions = new ArrayList<SafraPayTransaction>();
	
	@JsonProperty("metadata")
	@Type(JsonMapStringString.class)
	@Column(name = "METADATA")
	private List<Map<String, String>> metadata = new ArrayList<Map<String, String>>();
	

	
	private Integer source;
	
	
	
	public Integer getSmartid() {
		return smartid;
	}

	public void setSmartid(Integer smartid) {
		this.smartid = smartid;
	}

	public String getMerchantChargeId() {
		return merchantChargeId;
	}

	public void setMerchantChargeId(String merchantChargeId) {
		this.merchantChargeId = merchantChargeId;
	}

	public SafraPayCustomer getSafraPayCustomer() {
		return safraPayCustomer;
	}

	public void setSafraPayCustomer(SafraPayCustomer safraPayCustomer) {
		this.safraPayCustomer = safraPayCustomer;
	}

	public List<SafraPayTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<SafraPayTransaction> transactions) {
		this.transactions = transactions;
	}
	
	

	public List<Map<String, String>> getMetadata() {
		return metadata;
	}

	public void setMetadata(List<Map<String, String>> metadata) {
		this.metadata = metadata;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	@Override
	public int hashCode() {
		return Objects.hash(smartid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SafraPayCharge other = (SafraPayCharge) obj;
		return Objects.equals(smartid, other.smartid);
	}



}
