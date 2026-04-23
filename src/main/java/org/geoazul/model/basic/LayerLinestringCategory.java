package org.geoazul.model.basic;

import jakarta.persistence.Entity;

@Entity
public class LayerLinestringCategory  extends LayerCategory {

	public LayerLinestringCategory(String categoryname, String categorydescri) {
		super(categoryname, categorydescri);
	}

	public LayerLinestringCategory() {
	}

}
