package org.geoazul.model;

import org.geoazul.model.security.RealmEntity;
import org.locationtech.jts.geom.Geometry;
import org.omnifaces.optimusfaces.test.model.LocalGeneratedIdEntity;


public class ContadorRest extends LocalGeneratedIdEntity {

	private static final long serialVersionUID = 1L;

	
	private RealmEntity realm;
	
	private String city;
	
	private Geometry geometry;
		
	
	public RealmEntity getRealm() {
		return realm;
	}

	public void setRealm(RealmEntity realm) {
		this.realm = realm;
	}
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	


}
