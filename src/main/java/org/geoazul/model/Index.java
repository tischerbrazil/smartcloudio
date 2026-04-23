package org.geoazul.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import com.fasterxml.jackson.databind.JsonNode;


@Entity
@Table(name = "fin_index")
@NamedQueries({
	@NamedQuery(name = "Index.findById", query = "SELECT i FROM Index i WHERE i.id = :id"),
	@NamedQuery(name = "Index.findBySymbolDate", query = "SELECT i FROM Index i WHERE i.symbol.id = :symbolId AND i.date = :dateRef")
	 })
public  class Index implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public static final String INDEX_BY_ID = "Index.findById";
	public static final String INDEX_BY_SYMBOL_DATE = "Index.findBySymbolDate";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	//@JsonManagedReference 
	private Symbol symbol; 
	
	@Column(name = "date")
	private Date date;
	
			
	@Column(name = "base")
	private String base;
	
	
	@Column(name = "value")
	private Double value;
	
	public Index() {
	
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Symbol getSymbol() {
		return symbol;
	}

	public void setSymbol(Symbol symbol) {
		this.symbol = symbol;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	
	

	


}
