package org.geoazul.model.basic;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POINTS GeoEJB
 */

@Entity
@Table(name = "APP_LINESTRING")
public class Linestring extends AbstractGeometry  {
	
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;


	@Column(columnDefinition = "Geometry", nullable = true, name = "geometry")
	@JsonIgnore

	private Geometry geometry;
	
	@Column(name = "valoroffset")
	private Integer valoroffset;

	public Linestring() {
	}
	
	public Geometry getGeometry() {
		return this.geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public Linestring(Integer valoroffset) {
		this.valoroffset = valoroffset;
	}

	public Integer getValoroffset() {
		return this.valoroffset;
	}

	public void setValoroffset(Integer valoroffset) {
		this.valoroffset = valoroffset;
	}
	
	@JsonIgnore

	public String getGeometrywkt() {
		return geometry.toString();
	}

	public void setGeometrywkt(String geometrywkt) {
		WKTReader wktReader = new WKTReader();
		Geometry geometryGeom = null;
		try {
			geometryGeom = wktReader.read(geometrywkt);
			geometryGeom.setSRID(3857);
		} catch (ParseException e) {
		}
		this.geometry = geometryGeom;
	}

}
