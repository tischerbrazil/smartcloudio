package org.geoazul.model.basic.rural;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class GeometryGeometryId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long pointriId;
	private Long polygonriId;

	public GeometryGeometryId() {
	}

	public GeometryGeometryId(Long pointriId, Long polygonriId) {
		this.pointriId = pointriId;
		this.polygonriId = polygonriId;
	}

	@Column(name = "pointri_id", nullable = false)
	public Long getPointriId() {
		return this.pointriId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pointriId == null) ? 0 : pointriId.hashCode());
		result = prime * result + ((polygonriId == null) ? 0 : polygonriId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeometryGeometryId other = (GeometryGeometryId) obj;
		if (pointriId == null) {
			if (other.pointriId != null)
				return false;
		} else if (!pointriId.equals(other.pointriId))
			return false;
		if (polygonriId == null) {
			if (other.polygonriId != null)
				return false;
		} else if (!polygonriId.equals(other.polygonriId))
			return false;
		return true;
	}

	public void setPointriId(Long pointriId) {
		this.pointriId = pointriId;
	}

	@Column(name = "polygonri_id", nullable = false)
	public Long getPolygonriId() {
		return this.polygonriId;
	}

	public void setPolygonriId(Long polygonriId) {
		this.polygonriId = polygonriId;
	}
	
	@Override
	public String toString()  {
		return this.polygonriId + "-" + this.pointriId;
	}

}
