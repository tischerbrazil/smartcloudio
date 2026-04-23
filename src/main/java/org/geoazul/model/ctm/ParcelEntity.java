package org.geoazul.model.ctm;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.geoazul.model.basic.Polygon;
import com.erp.modules.commonClasses.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CTM_PARCEL")
@NamedQueries({
	@NamedQuery(name = ParcelEntity.PARCEL_ITEMS, query = "SELECT  parcel FROM ParcelEntity parcel WHERE parcel.surface = :surface"),
	@NamedQuery(name = ParcelEntity.PARCEL_ID, query = "SELECT  parcel FROM ParcelEntity parcel WHERE parcel.id = :id"),
})
public class ParcelEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	public static final String PARCEL_ITEMS = "getParcelsItems";
	public static final String PARCEL_ID = "getParcelId";
		
	@Column(name = "parte")
	//@JsonIgnore
	private Integer parte;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "surface_id")
	@JsonIgnore
	private Polygon surface;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parcel")
	private List<ParcelUserMappingEntity> proprietarios = new ArrayList<ParcelUserMappingEntity>();

	public Integer getParte() {
		return parte;
	}

	public void setParte(Integer parte) {
		this.parte = parte;
	}

	public Polygon getSurface() {
		return surface;
	}

	public void setSurface(Polygon surface) {
		this.surface = surface;
	}

	public List<ParcelUserMappingEntity> getProprietarios() {
		return proprietarios;
	}

	public void setProprietarios(List<ParcelUserMappingEntity> proprietarios) {
		this.proprietarios = proprietarios;
	}
	
}
