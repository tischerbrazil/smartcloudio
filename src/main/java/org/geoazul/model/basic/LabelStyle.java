package org.geoazul.model.basic;

import java.math.BigDecimal;

import jakarta.persistence.Column;

public class LabelStyle implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public LabelStyle(String text, String align, String baseline, Integer rotation, String font, Boolean weight,
			String placement, Integer maxangle, Boolean overflow, Integer size, Integer height, Integer offsetX,
			Integer offsetY, String color, String outline, Integer outlineWidth, BigDecimal maxreso, String field,
			BigDecimal minreso) {
		
		this.text = text;
		this.align = align;
		this.baseline = baseline;
		this.rotation = rotation;
		this.font = font;
		this.weight = weight;
		this.placement = placement;
		this.maxangle = maxangle;
		this.overflow = overflow;
		this.size = size;
		this.height = height;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.color = color;
		this.outline = outline;
		this.outlineWidth = outlineWidth;
		this.maxreso = maxreso;
		this.field = field;
		this.minreso = minreso;
	}

	public LabelStyle() {
		this.text =  "normal"; // format text
		this.align = "start"; // aligmnet
		this.baseline = "alphabetic"; // base aligment
		this.rotation = 0; // 0.78 45°, 2.09 120°, 4.14 180°, 6.28 360° || undefined
		this.font = "Arial"; // font family
		this.weight = true; // true for bold
		this.placement = "point"; // line or point
		this.maxangle = 0; // 0.78 45°, 2.09 120°, 4.14 180°, 6.28 360° || undefined
		this.overflow = false; // true for overflow
		this.size = 16; // font size
		this.height = 1; // bbox label height
		this.offsetX = 10;
		this.offsetY = 10;
		this.color = "#000000";
		this.outline = "#fff700";
		this.outlineWidth = 3;
		this.maxreso =  new BigDecimal(10000) ;
		this.field = "nome";
		this.minreso =  new BigDecimal(0.001);
	}
	
	private String text; 
	private String align; 
	private String baseline; 

	private Integer rotation;
	private String font ;
	private Boolean weight ;

	private String placement;
	private Integer maxangle;
	private Boolean overflow;

	private Integer size ;
	private Integer height;

	private Integer offsetX ;
	private Integer offsetY ;
	private String color = "f51313";

	private String outline = "ecbcf5";
	private Integer outlineWidth;

	@Column(name = "minres", precision = 10, scale = 5)
	private BigDecimal maxreso;

	// --------------------- NOT IMPLEMENTE YET--------
	private String field ;

	@Column(name = "maxres", precision = 10, scale = 5)
	private BigDecimal minreso;
	// ------------------------------------------------------------

	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getBaseline() {
		return baseline;
	}

	public void setBaseline(String baseline) {
		this.baseline = baseline;
	}

	public Integer getRotation() {
		return rotation;
	}

	public void setRotation(Integer rotation) {
		this.rotation = rotation;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public Boolean getWeight() {
		return weight;
	}

	public void setWeight(Boolean weight) {
		this.weight = weight;
	}

	public String getPlacement() {
		return placement;
	}

	public void setPlacement(String placement) {
		this.placement = placement;
	}

	public Integer getMaxangle() {
		return maxangle;
	}

	public void setMaxangle(Integer maxangle) {
		this.maxangle = maxangle;
	}

	public Boolean getOverflow() {
		return overflow;
	}

	public void setOverflow(Boolean overflow) {
		this.overflow = overflow;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(Integer offsetX) {
		this.offsetX = offsetX;
	}

	public Integer getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(Integer offsetY) {
		this.offsetY = offsetY;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getOutline() {
		return outline;
	}

	public void setOutline(String outline) {
		this.outline = outline;
	}

	public Integer getOutlineWidth() {
		return outlineWidth;
	}

	public void setOutlineWidth(Integer outlineWidth) {
		this.outlineWidth = outlineWidth;
	}

	public BigDecimal getMinreso() {
		return minreso;
	}

	public void setMinreso(BigDecimal minreso) {
		this.minreso = minreso;
	}

	public BigDecimal getMaxreso() {
		return maxreso;
	}

	public void setMaxreso(BigDecimal maxreso) {
		this.maxreso = maxreso;
	}

	@Override
	public String toString() {
		return "{text:'" + text + "',align:'" + align + "',baseline:'" + baseline
				+ "',rotation:'" + rotation + "',font:'" + font + "',weight:'" + weight
				+ "',placement:'" + placement + "',maxangle:'" + maxangle + "',overflow:'" + overflow
				+ "',size:'" + size + "',height:'" + height
				+ "',offsetX:'" + offsetX + "',offsetY:'" + offsetY + "',color:'" + color
				+ "',outline:'" + outline + "',outlineWidth:'" + outlineWidth + "',minreso:'" + minreso + "',maxreso:'"
				+ maxreso + "'}";
	}

	// private String format = "wrap";
	// private Double minreso = 1.0;

	// --------------------- NOT IMPLEMENTE YET

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

}