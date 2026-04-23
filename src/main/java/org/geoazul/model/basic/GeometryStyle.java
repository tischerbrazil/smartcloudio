package org.geoazul.model.basic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class GeometryStyle implements java.io.Serializable {

	private static final long serialVersionUID = 8128828569585103759L;

	public GeometryStyle() {
		this.rgbafillcolor = "rgba(245,2,62,0.45)";
		this.rgbastrokecolor =  "#00E600";
		this.strokewidth = 1;
		this.strokedashLenght = 0;
		this.strokedashSpace = 0;
		this.radius = 15;
	}

	public GeometryStyle(String rgbafillcolor, String rgbastrokecolor, 
			Integer strokewidth, 
			Integer strokedashLenght,
			Integer strokedashSpace, Integer radius
			) {
		this.rgbafillcolor = rgbafillcolor;
		this.rgbastrokecolor = rgbastrokecolor;
		this.strokewidth = strokewidth;
		this.strokedashLenght = strokedashLenght;
		this.strokedashSpace = strokedashSpace;
		this.radius = radius;
	}

	@JsonIgnoreProperties
	private String rgbafillcolor; // fill color

	@JsonIgnoreProperties
	private String rgbastrokecolor; // alpha fill color
	
	@JsonIgnoreProperties
	private Integer strokewidth;
	
	@JsonIgnoreProperties
	private Integer strokedashLenght;
	
	@JsonIgnoreProperties
	private Integer strokedashSpace;
	
	@JsonIgnoreProperties
	private Integer radius;

	public String getRgbafillcolor() {
		return rgbafillcolor;
	}

	public void setRgbafillcolor(String rgbafillcolor) {
		this.rgbafillcolor = rgbafillcolor;
	}

	public String getRgbastrokecolor() {
		return rgbastrokecolor;
	}

	public void setRgbastrokecolor(String rgbastrokecolor) {
		this.rgbastrokecolor = rgbastrokecolor;
	}

	public Integer getStrokewidth() {
		return strokewidth;
	}

	public void setStrokewidth(Integer strokewidth) {
		this.strokewidth = strokewidth;
	}

	public Integer getStrokedashLenght() {
		return strokedashLenght;
	}

	public void setStrokedashLenght(Integer strokedashLenght) {
		this.strokedashLenght = strokedashLenght;
	}

	public Integer getStrokedashSpace() {
		return strokedashSpace;
	}

	public void setStrokedashSpace(Integer strokedashSpace) {
		this.strokedashSpace = strokedashSpace;
	}
	
	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	@Override
	public String toString() {
		return "{rgbafillcolor:'" + this.getRgbafillcolor() + "',rgbastrokecolor:'" + this.getRgbastrokecolor()
				+ "',strokewidth:'" + strokewidth 
				+ "',strokedashLenght:'" + strokedashLenght
				+ "',strokedashSpace:'" + strokedashSpace 
				+ "',radius:'" + radius + "'}";
	}

}