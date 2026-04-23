package org.geoazul.model.basic;

import jakarta.persistence.Entity;

@Entity
public class LayerMultiPolygonCategory  extends LayerCategory {

	public LayerMultiPolygonCategory(String categoryname, String categorydescri) {
		super(categoryname, categorydescri);
	}

	public LayerMultiPolygonCategory() {
	}

}
