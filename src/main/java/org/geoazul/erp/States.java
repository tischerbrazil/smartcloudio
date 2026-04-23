package org.geoazul.erp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.geoazul.model.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 */

@Entity
@Table(name = "erp_states")

@NamedQueries({
	@NamedQuery(name = "States.findByCountry", query = "SELECT s FROM States s where s.countries.id = :country ORDER BY s.name"),
	@NamedQuery(name = "States.findByCountryActives", query = "SELECT s FROM States s where s.countries.id = :country AND  s.enabled = true ORDER BY s.name desc"),
    @NamedQuery(name = "States.findByCountryActivesByName", query = "SELECT s FROM States s where s.countries.id = :country AND s.iso2 = :iso2 AND  s.enabled = true ORDER BY s.name desc")
})
@DiscriminatorValue("2")
public class States implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_COUNTRY_ACTIVES_BY_NAME = "States.findByCountryActivesByName";
	public static final String FIND_BY_COUNTRY_ACTIVES = "States.findByCountryActives";
	public static final String FIND_BY_COUNTRY = "States.findByCountry";

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private int id;
	
	@Column(name = "enabled")
	private boolean enabled;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id", nullable = false)
	@JsonIgnore
	private Countries countries;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "country_code", nullable = false, length = 2)
	private String countryCode;
	
	@Column(name = "fips_code")
	private String fipsCode;
	
	@Column(name = "iso2")
	private String iso2;
	
	@Column(name = "latitude", precision = 10)
	private BigDecimal latitude;
	
	@Column(name = "longitude", precision = 11)
	private BigDecimal longitude;
	

	@Column(name = "created_at")
	private LocalDateTime createdAt;
		

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@Column(name = "flag", nullable = false)
	private boolean flag;
	
	@Column(name = "wikidataid")
	private String wikidataid;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "states")
	@JsonIgnore
	private Set<Cities> citieses = new HashSet<Cities>(0);
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "state")
	@JsonIgnore
	private Set<Address> address = new HashSet<Address>(0);
	
	public States() {
	}

	public States(int id, Countries countries, String name, String countryCode,
			boolean flag) {
		this.id = id;
		this.countries = countries;
		this.name = name;
		this.countryCode = countryCode;
		this.flag = flag;
	}
	public States(int id, Countries countries, String name, String countryCode,
			String fipsCode, String iso2, BigDecimal latitude,
			BigDecimal longitude, LocalDateTime createdAt, LocalDateTime updatedAt, boolean flag,
			String wikidataid, Set<Cities> citieses) {
		this.id = id;
		this.countries = countries;
		this.name = name;
		this.countryCode = countryCode;
		this.fipsCode = fipsCode;
		this.iso2 = iso2;
		this.latitude = latitude;
		this.longitude = longitude;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.flag = flag;
		this.wikidataid = wikidataid;
		this.citieses = citieses;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Countries getCountries() {
		return this.countries;
	}

	public void setCountries(Countries countries) {
		this.countries = countries;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getFipsCode() {
		return this.fipsCode;
	}

	public void setFipsCode(String fipsCode) {
		this.fipsCode = fipsCode;
	}

	public String getIso2() {
		return this.iso2;
	}

	public void setIso2(String iso2) {
		this.iso2 = iso2;
	}

	public BigDecimal getLatitude() {
		return this.latitude;
	}

	public void setLatitude(BigDecimal latitude) {
		this.latitude = latitude;
	}

	public BigDecimal getLongitude() {
		return this.longitude;
	}

	public void setLongitude(BigDecimal longitude) {
		this.longitude = longitude;
	}

	public LocalDateTime getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return this.updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public boolean isFlag() {
		return this.flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getWikidataid() {
		return this.wikidataid;
	}

	public void setWikidataid(String wikidataid) {
		this.wikidataid = wikidataid;
	}

	public Set<Cities> getCitieses() {
		return this.citieses;
	}

	public void setCitieses(Set<Cities> citieses) {
		this.citieses = citieses;
	}

	public Set<Address> getAddress() {
		return address;
	}

	public void setAddress(Set<Address> address) {
		this.address = address;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		States other = (States) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "States [id=" + id + "]";
	}
}
