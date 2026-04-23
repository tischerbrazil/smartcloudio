package org.geoazul.model.basic;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.locationtech.jts.geom.Geometry;
import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * POINTS GeoEJB
 */

@Entity
@Table(name = "APP_MULTIPOLYGON")
public class MultiPolygon  extends AbstractGeometry  {

	@Column(columnDefinition = "Geometry", nullable = true, name = "geometry")
	@JsonIgnore
	private Geometry geometry;
	
	public MultiPolygon() {
	}
	
	public Geometry getGeometry() {
		return this.geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}
	
}
