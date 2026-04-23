package org.geoazul.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "fin_symbol")
public  class Symbol  {
		
	@Id
	@Column(name = "id", unique = true, nullable = false)
	private String id;
				
	@Column(name = "name")
	private String name;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "symbol", fetch = FetchType.LAZY)
	// @JsonBackReference
	private List<Index> indexes = new ArrayList<Index>();

	public Symbol() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Index> getIndexes() {
		return indexes;
	}

	public void setIndexes(List<Index> indexes) {
		this.indexes = indexes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Symbol other = (Symbol) obj;
		return Objects.equals(id, other.id);
	}
	
	
	
}
