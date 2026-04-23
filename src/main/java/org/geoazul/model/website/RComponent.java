package org.geoazul.model.website;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
@JsonSubTypes({ @Type(value = RButton.class, name = "RButton") })
@JsonTypeName("RComponent")
public abstract class RComponent {

	public abstract String getId();

	public abstract void setId(String id);
	
	public abstract int getColumn();

	public abstract void setColumn(int column);

	public abstract int getOrder();

	public abstract void setOrder(int order);

	public abstract RComponent getParent();

	public abstract void setParent(RComponent parent);

	
	
	
	public abstract Long getBlockId();
	public abstract void setBlockId(Long blockId);
	
	public abstract Integer getParentId();
	public abstract void setParentId(Integer parentId);

	@JsonIgnore
	public abstract String getRendererType();

	@JsonIgnore
	public abstract void setRendererType(String rendererType);

	 private List<RComponent> children = new ArrayList<RComponent>();
	 
	public abstract List<RComponent> getChildren();

	public abstract void setChildren(List<RComponent> children);
	
	@JsonIgnore
	public Stream<RComponent> flattened() {
        return Stream.concat(
                Stream.of(this),
                children.stream().flatMap(RComponent::flattened));
    }

	@JsonIgnore
	public abstract int getChildCount();

	@JsonIgnore
	public abstract String getFamily();

	@JsonIgnore
	public abstract String getType();

}
