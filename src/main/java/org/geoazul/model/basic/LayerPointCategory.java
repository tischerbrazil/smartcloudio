package org.geoazul.model.basic;

import jakarta.persistence.Entity;

@Entity
public class LayerPointCategory  extends LayerCategory {

	public LayerPointCategory(String categoryname, String categorydescri) {
		super(categoryname, categorydescri);
	}

	public LayerPointCategory() {
	}

}
