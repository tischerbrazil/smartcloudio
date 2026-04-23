package org.geoazul.model.website;

import jakarta.faces.component.UIOutcomeTarget;

public class RHtmlGraphicImage extends RUIGraphic {

	/**
	 * <p>
	 * The standard component type for this component.
	 * </p>
	 */
	public static final String COMPONENT_TYPE = "org.risteos.OutcomeTarget";

	/**
	 * <p>
	 * The standard component family for this component.
	 * </p>
	 */
	public static final String COMPONENT_FAMILY = "org.risteos.OutcomeTarget";

	

	@Override
	public String getType() {
		return COMPONENT_TYPE;
	}


	private String alt;
	private String dir;
	private String height;
	
	private String ismap;
	private String lang;
	private String longdesc;
	
	private String onclick;
	private String ondblclick;
	private String onkeydown;
	
	private String onkeypress;
	private String onkeyup;
	private String onmousedown;
	
	private String onmousemove;
	private String onmouseout;
	private String onmouseover;
	
	private String onmouseup;
	private String style;
	private String role;
	
	private String styleClass;
	private String title;
	private String usemap;
	
	private String width;
	
	
	private int column;
	private Long blockId;
	

	/**
	 * <p>
	 * Create a new {@link UIOutcomeTarget} instance with default property values.
	 * </p>
	 */
	public RHtmlGraphicImage() {
		super();
		setRendererType("jakarta.faces.Link");
	}

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}
	
	@Override
	public int getColumn() {
		return column;
	}

	@Override
	public void setColumn(int column) {
		this.column = column;
	}
	
	
	@Override
	public Long getBlockId() {
		return blockId;
	}

	@Override
	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}

	public String getAlt() {
		return alt;
	}

	public void setAlt(String alt) {
		this.alt = alt;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getIsmap() {
		return ismap;
	}

	public void setIsmap(String ismap) {
		this.ismap = ismap;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getLongdesc() {
		return longdesc;
	}

	public void setLongdesc(String longdesc) {
		this.longdesc = longdesc;
	}

	public String getOnclick() {
		return onclick;
	}

	public void setOnclick(String onclick) {
		this.onclick = onclick;
	}

	public String getOndblclick() {
		return ondblclick;
	}

	public void setOndblclick(String ondblclick) {
		this.ondblclick = ondblclick;
	}

	public String getOnkeydown() {
		return onkeydown;
	}

	public void setOnkeydown(String onkeydown) {
		this.onkeydown = onkeydown;
	}

	public String getOnkeypress() {
		return onkeypress;
	}

	public void setOnkeypress(String onkeypress) {
		this.onkeypress = onkeypress;
	}

	public String getOnkeyup() {
		return onkeyup;
	}

	public void setOnkeyup(String onkeyup) {
		this.onkeyup = onkeyup;
	}

	public String getOnmousedown() {
		return onmousedown;
	}

	public void setOnmousedown(String onmousedown) {
		this.onmousedown = onmousedown;
	}

	public String getOnmousemove() {
		return onmousemove;
	}

	public void setOnmousemove(String onmousemove) {
		this.onmousemove = onmousemove;
	}

	public String getOnmouseout() {
		return onmouseout;
	}

	public void setOnmouseout(String onmouseout) {
		this.onmouseout = onmouseout;
	}

	public String getOnmouseover() {
		return onmouseover;
	}

	public void setOnmouseover(String onmouseover) {
		this.onmouseover = onmouseover;
	}

	public String getOnmouseup() {
		return onmouseup;
	}

	public void setOnmouseup(String onmouseup) {
		this.onmouseup = onmouseup;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUsemap() {
		return usemap;
	}

	public void setUsemap(String usemap) {
		this.usemap = usemap;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}
	
	
	
	

}
