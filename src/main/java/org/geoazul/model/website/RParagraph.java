package org.geoazul.model.website;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("RParagraph")
public class RParagraph extends RUIComponentBase {

	

	public static final String COMPONENT_TYPE = "RParagraph";
	public static final String COMPONENT_FAMILY = "org.risteos.component";
	// public static final String DEFAULT_RENDERER =
	// "org.risteos.component.HeadingRenderer";
	public static final String DEFAULT_RENDERER = "/includes/widgets/RHeading.xhtml";
	public static final String CONTAINER_CLASS = "ui-heading";

	@Override
	public String getType() {
		return COMPONENT_TYPE;
	}

	public RParagraph() {
			setRendererType(DEFAULT_RENDERER);
		}

	@JsonIgnore
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	

	
	private String value;
	private String styleClass;
	private int column;
	private Long blockId;

	

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
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
