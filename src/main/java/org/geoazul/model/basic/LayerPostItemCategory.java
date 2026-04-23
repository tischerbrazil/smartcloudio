package org.geoazul.model.basic;

import jakarta.persistence.Entity;

@Entity
public class LayerPostItemCategory  extends LayerCategory {

	public LayerPostItemCategory(String categoryname, String categorydescri) {
		super(categoryname, categorydescri);
	}

	public LayerPostItemCategory() {
	}

}
