package org.geoazul.model.basic;

import jakarta.persistence.Entity;

@Entity
public class LayerPolygonCategory  extends LayerCategory {

	public LayerPolygonCategory(String categoryname, String categorydescri) {
		super(categoryname, categorydescri);
	}

	public LayerPolygonCategory() {
	}

}
