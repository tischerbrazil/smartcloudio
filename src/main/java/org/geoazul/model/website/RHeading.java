package org.geoazul.model.website;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonTypeName("RHeading")
public class RHeading extends RUIComponentBase {

	private static final long serialVersionUID = 725103170697791019L;

	public static final String COMPONENT_TYPE = "RHeading";
	public static final String COMPONENT_FAMILY = "org.risteos.component";
	// public static final String DEFAULT_RENDERER =
	// "org.risteos.component.HeadingRenderer";
	public static final String DEFAULT_RENDERER = "/includes/widgets/RHeading.xhtml";
	public static final String CONTAINER_CLASS = "ui-heading";

	@Override
	public String getType() {
		return COMPONENT_TYPE;
	}

	public RHeading() {
		setRendererType(DEFAULT_RENDERER);
	}

	@JsonIgnore
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	public enum TypeHeading {
		h1, h2, h3, h4, h5, h6, p
	}

	private TypeHeading typeHeading;
	private String value;
	private String styleClass;
	private int column;
	private Long blockId;
	private boolean bold = false;
	private boolean italic = false;
	private boolean strike = false;
	private boolean underline = false;

	public TypeHeading getTypeHeading() {
		return typeHeading;
	}

	public void setTypeHeading(TypeHeading typeHeading) {
		this.typeHeading = typeHeading;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@JsonIgnore
	public String getValueRendered() {
		String retorno = this.value;
		if (this.isBold()) {
			retorno = "<b>" + retorno + "</b>";
		}
		if (this.isItalic()) {
			retorno = "<i>" + retorno + "</i>";
		}
		if (this.isStrike()) {
			retorno = "<strike>" + retorno + "</strike>";
		}
		if (this.isUnderline()) {
			retorno = "<u>" + retorno + "</u>";
		}
		
		return "<" + this.typeHeading + " class=\"" + styleClass + "\">" + retorno + "</" + this.typeHeading + ">";
		
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

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public boolean isItalic() {
		return italic;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	public boolean isStrike() {
		return strike;
	}

	public void setStrike(boolean strike) {
		this.strike = strike;
	}

	public boolean isUnderline() {
		return underline;
	}

	public void setUnderline(boolean underline) {
		this.underline = underline;
	}

	

}
