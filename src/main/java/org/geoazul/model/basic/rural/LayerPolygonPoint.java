package org.geoazul.model.basic.rural;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import org.geoazul.model.basic.LayerEntity;

@Entity 
@DiscriminatorValue("polygonpoint")
public class LayerPolygonPoint extends LayerEntity {

	public LayerPolygonPoint() {
	}

}