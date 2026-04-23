package org.geoazul.model.basic;

import java.math.BigDecimal;


public class LayerView  implements java.io.Serializable  {

	private static final long serialVersionUID = -8018091411821998241L;

	private Boolean visible;
	
	private BigDecimal opacity;

	private String layerid;
	
	private Integer zIndex;
		
	private String name;
	
	private String layerhash;
	
	private Boolean father;
	
	
	public Boolean getVisible() {
		return visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	
	public BigDecimal getOpacity() {
		return opacity;
	}

	public void setOpacity(BigDecimal opacity) {
		this.opacity = opacity;
	}
	
	
	
	public String opacityString() {
		return opacity.toString(); 
	}

	

	public String getLayerid() {
		return layerid;
	}

	public void setLayerid(String layerid) {
		this.layerid = layerid;
	}
	
	
	
	public Integer getZIndex() {
		return zIndex;
	}

	public void setZIndex(Integer zIndex) {
		this.zIndex = zIndex;
	}

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	
	public String getLayerhash() {
		return layerhash;
	}

	public void setLayerhash(String layerhash) {
		this.layerhash = layerhash;
	}

	
	
	public Boolean getFather() {
		return father;
	}

	public void setFather(Boolean father) {
		this.father = father;
	}
	
	
	
	

}