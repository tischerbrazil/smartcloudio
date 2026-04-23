package org.geoazul.erp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.geoazul.model.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "erp_countries")

@NamedQuery(name = Countries.FIND_ALL, query = "SELECT c FROM Countries c WHERE c.enabled = true ORDER BY c.name ")
public class Countries implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public static final String FIND_ALL = "Country.findAll";
	 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private int id;
		
	@Column(columnDefinition="BOOLEAN DEFAULT false", name = "enabled")
    private boolean enabled;
	
	@Column(name = "name", nullable = false, length = 100)
	private String name;
	
	@Column(name = "iso3", length = 3)
	private String iso3;
	
	@Column(name = "iso2", length = 2)
	private String iso2;
	
	@Column(name = "lang", length = 5)
	private String lang;
	
	@Column(name = "phonecode")
	private String phonecode;
	
	@Column(name = "capital")
	private String capital;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "currency_symbol")
	private String currencySymbol;
	
	@Column(name = "tld")
	private String tld;
	
	@Column(name = "native")
	private String native_;
	
	@Column(name = "region")
	private String region;
	
	@Column(name = "subregion")
	private String subregion;
	
	@Column(name = "timezones", columnDefinition="TEXT")
	private String timezones;
	
	@Column(name = "translations", columnDefinition="TEXT")
	private String translations;
	
	@Column(name = "latitude", precision = 10)
	private BigDecimal latitude;
	
	@Column(name = "longitude", precision = 11)
	private BigDecimal longitude;
	
	@Column(name = "emoji", length = 191)
	private String emoji;
	
	@Column(name = "emojiu", length = 191)
	private String emojiu;
	

	@Column(name = "created_at")
	private LocalDateTime createdAt;
	

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
	
	@Column(name = "flag", nullable = false)
	private boolean flag;
	
	@Column(name = "wikidataid")
	private String wikidataid;
	
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "countries")
	@JsonIgnore
	private Set<States> stateses = new HashSet<States>(0);
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "countries")
	@JsonIgnore
	private Set<Cities> citieses = new HashSet<Cities>(0);
		
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "country")
	@JsonIgnore
	private Set<Address> address = new HashSet<Address>(0);
	
	public Countries() {
	}

	public Countries(int id, String name, boolean flag) {
		this.id = id;
		this.name = name;
		this.flag = flag;
	}
	public Countries(int id, String name, String iso3, String iso2,
			String phonecode, String capital, String currency,
			String currencySymbol, String tld, String native_, String region,
			String subregion, String timezones, String translations,
			BigDecimal latitude, BigDecimal longitude, String emoji,
			String emojiu, LocalDateTime createdAt, LocalDateTime updatedAt, boolean flag,
			String wikidataid, Set<States> stateses, Set<Cities> citieses) {
		this.id = id;
		this.name = name;
		this.iso3 = iso3;
		this.iso2 = iso2;
		this.phonecode = phonecode;
		this.capital = capital;
		this.currency = currency;
		this.currencySymbol = currencySymbol;
		this.tld = tld;
		this.native_ = native_;
		this.region = region;
		this.subregion = subregion;
		this.timezones = timezones;
		this.translations = translations;
		this.latitude = latitude;
		this.longitude = longitude;
		this.emoji = emoji;
		this.emojiu = emojiu;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.flag = flag;
		this.wikidataid = wikidataid;
		this.stateses = stateses;
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

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIso3() {
		return this.iso3;
	}

	public void setIso3(String iso3) {
		this.iso3 = iso3;
	}

	public String getIso2() {
		return this.iso2;
	}

	public void setIso2(String iso2) {
		this.iso2 = iso2;
	}
	
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getPhonecode() {
		return this.phonecode;
	}

	public void setPhonecode(String phonecode) {
		this.phonecode = phonecode;
	}

	public String getCapital() {
		return this.capital;
	}

	public void setCapital(String capital) {
		this.capital = capital;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getCurrencySymbol() {
		return this.currencySymbol;
	}

	public void setCurrencySymbol(String currencySymbol) {
		this.currencySymbol = currencySymbol;
	}

	public String getTld() {
		return this.tld;
	}

	public void setTld(String tld) {
		this.tld = tld;
	}

	public String getNative_() {
		return this.native_;
	}

	public void setNative_(String native_) {
		this.native_ = native_;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSubregion() {
		return this.subregion;
	}

	public void setSubregion(String subregion) {
		this.subregion = subregion;
	}

	public String getTimezones() {
		return this.timezones;
	}

	public void setTimezones(String timezones) {
		this.timezones = timezones;
	}

	public String getTranslations() {
		return this.translations;
	}

	public void setTranslations(String translations) {
		this.translations = translations;
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

	public String getEmoji() {
		return this.emoji;
	}

	public void setEmoji(String emoji) {
		this.emoji = emoji;
	}

	public String getEmojiu() {
		return this.emojiu;
	}

	public void setEmojiu(String emojiu) {
		this.emojiu = emojiu;
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

	public Set<States> getStateses() {
		return this.stateses;
	}

	public void setStateses(Set<States> stateses) {
		this.stateses = stateses;
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
	public String toString() {
		return "Countries [id=" + id + "]";
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
		Countries other = (Countries) obj;
		if (id != other.id)
			return false;
		return true;
	}

	

}
