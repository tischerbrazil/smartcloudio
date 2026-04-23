package org.geoazul.model;

import org.locationtech.jts.geom.Geometry;

public  class Visitor  {
	private String country;
	private String countryISO;
	private String subdivision;
	private String subdivisionISO;
	private String city;
	private String postal;
	private Geometry geometry;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryISO() {
		return countryISO;
	}

	public void setCountryISO(String countryISO) {
		this.countryISO = countryISO;
	}

	public String getSubdivision() {
		return subdivision;
	}

	public void setSubdivision(String subdivision) {
		this.subdivision = subdivision;
	}

	public String getSubdivisionISO() {
		return subdivisionISO;
	}

	public void setSubdivisionISO(String subdivisionISO) {
		this.subdivisionISO = subdivisionISO;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

}
