package com.erp.modules.inventory.entities;

import com.erp.modules.commonClasses.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NaturalId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * 
 */

@Entity
@Table(name = "ERP_MANUFACTURER")
@NamedQueries({
	@NamedQuery(name = "Manufacturer.findAll", query = "SELECT m FROM Manufacturer m"),
	@NamedQuery(name = "Manufacturer.findByUuid", query = "SELECT m FROM Manufacturer m WHERE m.id = :id"),
	@NamedQuery(name = "Manufacturer.findAllActives", query = "SELECT m FROM Manufacturer m where m.enabled = true"),
 })
public class Manufacturer extends BaseEntity {
	
	public Manufacturer() {
	
	}

	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "Manufacturer.findAll";
	public static final String FIND_BY_ID = "Manufacturer.findById";
	public static final String FIND_ALL_ACTIVES = "Manufacturer.findAllActives";

	@Column(name = "name")
	private String name;
	
    @ColumnDefault("true")
	@Column(name = "enabled")
	private Boolean enabled;

	@OneToMany(mappedBy = "manufacturer")
	private List<Product> manufacturer = new ArrayList<Product>();
	
	@Column(name = "image")
	private String image;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<Product> getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(List<Product> manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}





	

}