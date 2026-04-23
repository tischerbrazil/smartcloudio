package org.geoazul.model.website;

public abstract class RUIMedia extends RUIComponentBase {
				
	public RUIMedia(String value, String player, String style, String styleClass, boolean cache) {
		super();
		this.value = value;
		this.player = player;
		this.style = style;
		this.styleClass = styleClass;
		this.cache = cache;
	}

	public RUIMedia() {
		super();
	}

	private String value;
	private String player;
	private String style;
	private String styleClass;
	private boolean cache;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getPlayer() {
		return player;
	}

	public void setPlayer(String player) {
		this.player = player;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

	public boolean isCache() {
		return cache;
	}

	public void setCache(boolean cache) {
		this.cache = cache;
	}

}
