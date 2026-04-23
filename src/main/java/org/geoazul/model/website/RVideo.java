package org.geoazul.model.website;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("RVideo")
public class RVideo extends RUIMedia {

	public RVideo(String width, String height, String preload, String poster) {
		super();
		this.width = width;
		this.height = height;
		this.preload = preload;
		this.poster = poster;
	}

	public static final String COMPONENT_FAMILY = "org.risteos.component";
	public static final String DEFAULT_RENDERER = "/includes/widgets/RVideo.xhtml";

	public static final String COMPONENT_TYPE = "RVideo";
	public static final String CONTAINER_CLASS = "ui-media ui-video";

	@Override
	public String getType() {
		return COMPONENT_TYPE;
	}

	private String width;
	private String height;
	private String preload;
	private String poster;
	private int column;
	private Long blockId;

	public RVideo() {
		setRendererType(DEFAULT_RENDERER);
	}

	@JsonIgnore
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getPreload() {
		return preload;
	}

	public void setPreload(String preload) {
		this.preload = preload;
	}

	public String getPoster() {
		return poster;
	}

	public void setPoster(String poster) {
		this.poster = poster;
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
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return super.getId();
	}

}
