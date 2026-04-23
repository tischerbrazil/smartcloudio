package org.geoazul.model.website;

import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class RUIComponentBase extends RComponent {

	private String id;
	private RComponent parent;
	private List<RComponent> children;
	private int order;
	private String rendererType;
	private Integer parentId;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public RComponent getParent() {
		return parent;
	}

	@Override
	public void setParent(RComponent parent) {
		this.parent = parent;
	}

	@Override
	public List<RComponent> getChildren() {
		return children;
	}

	@Override
	public void setChildren(List<RComponent> children) {
		this.children = children;
	}

	@Override
	public int getOrder() {
		return order;
	}

	@Override
	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getChildCount() {
		if (children != null) {
			return children.size();
		}
		return 0;
	}

	@JsonIgnore
	@Override
	public String getRendererType() {
		return rendererType;
	}

	@JsonIgnore
	@Override
	public void setRendererType(String rendererType) {
		this.rendererType = rendererType;
	}

	@Override
	public Integer getParentId() {
		return parentId;
	}

	@Override
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RUIComponentBase other = (RUIComponentBase) obj;
		return Objects.equals(id, other.id);
	}

}
