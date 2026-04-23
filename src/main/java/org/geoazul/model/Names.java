package org.geoazul.model;

import java.util.UUID;

import org.geoazul.model.basic.Polygon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

@Entity
@Table(name = "api_names")
@NamedQueries({
    @NamedQuery(name = Names.ALL_NAMES, query = "SELECT n FROM Names n")
})
public  class Names  {
	
	public static final String ALL_NAMES = "getAllNames";
	
	public Names(String id, String name) {
		super();
		this.id = id;
		this.name = name;	
	}

	@Id
	@Column(name = "id", unique = true, nullable = false)
	private String id;
	
	@Column(name = "name")
	private String name;

	public Names() {
		 this.id = UUID.randomUUID().toString();
	}
	
	public Names(String name) {
		this.name = name;
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

	

	


}
