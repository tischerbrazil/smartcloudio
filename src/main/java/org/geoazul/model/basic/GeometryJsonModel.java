package org.geoazul.model.basic;

public class GeometryJsonModel {
	
	
	
	public GeometryJsonModel(String nome, String geometrySelected, 
			String geometrywkt, String layerid, String srs) {
		super();
		this.nome = nome;
		this.geometrySelected = geometrySelected;
		this.geometrywkt = geometrywkt;
		this.layerid = layerid;
		this.srs = srs;
	}
	
	public GeometryJsonModel() {
		
	}

	private String nome;
	private String geometrySelected;
	private String geometrywkt;
	private String layerid;
	private String srs;
	
	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getGeometrySelected() {
		return geometrySelected;
	}
	
	public void setGeometrySelected(String geometrySelected) {
		this.geometrySelected = geometrySelected;
	}
	
	public String getGeometrywkt() {
		return geometrywkt;
	}
	
	public void setGeometrywkt(String geometrywkt) {
		this.geometrywkt = geometrywkt;
	}
	
	public String getLayerid() {
		return layerid;
	}
	
	public void setLayerid(String layerid) {
		this.layerid = layerid;
	}
	
	public String getSrs() {
		return srs;
	}
	
	public void setSrs(String srs) {
		this.srs = srs;
	}
	
}
