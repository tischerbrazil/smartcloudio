package org.geoazul.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.geoazul.model.security.RealmEntity;
import org.locationtech.jts.geom.Geometry;
import org.omnifaces.optimusfaces.test.model.LocalGeneratedIdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

//@NamedQueries({
		// @NamedQuery(name="addvisitor", query="update Contador contador set
		// contador.visitas = contador.visitas + 1")
//})
@Entity
@Table(name = "in_contador")
//@SqlResultSetMapping(
//      name = "ContadorRestMapping",
//      classes = @ConstructorResult(
//           targetClass = ContadorRest.class,
//           columns = { 
//         		    @ColumnResult(name = "id", type = Long.class), 
//                  @ColumnResult(name = "realm"), 
//                  @ColumnResult(name = "ip"), 
//                  @ColumnResult(name = "geometry")}))
public class Contador extends LocalGeneratedIdEntity {

	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REALM_ID")
	private RealmEntity realm;
	
	@Column(name = "SESSION")
	private String session;
	
	@Column(name = "IP")
	private String ipNumber;

	@Column(name = "DATETIME")
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private LocalDateTime datetime;

	@Column(name = "GCMID")
	private Long gcmid;

	@Column(name = "GEOMETRY")
	private Geometry geometry;

	public Contador() {
		this.datetime = LocalDateTime.now();
	}

	public Contador(RealmEntity realm, String session, String ipNumber, Long gcmid, String country, String countryISO, 
			String subdivision,	String subdivisionISO, String city, String postal,
			Geometry geometry) {
		this.realm = realm;
		this.session = session;
		this.datetime = LocalDateTime.now();
		this.ipNumber = ipNumber;
		this.gcmid = gcmid;
		this.country = country; 
		this.countryISO = countryISO;
		this.subdivision = subdivision;
		this.subdivisionISO = subdivisionISO;
		this.city = city;
		this.postal = postal;
		this.geometry = geometry;
	}
	
	public Contador(Long id, RealmEntity realm, String session, String ipNumber, Long gcmid, String country, String countryISO, 
			String subdivision,	String subdivisionISO, String city, String postal,
			Geometry geometry) {
		this.id = id;
		this.realm = realm;
		this.session = session;
		this.datetime = LocalDateTime.now();
		this.ipNumber = ipNumber;
		this.gcmid = gcmid;
		this.country = country; 
		this.countryISO = countryISO;
		this.subdivision = subdivision;
		this.subdivisionISO = subdivisionISO;
		this.city = city;
		this.postal = postal;
		this.geometry = geometry;
	}
	
	public Contador(Long id, Geometry geometry) {
		this.id = id;
		this.geometry = geometry;
	}

	@Column(name = "COUNTRY")
	private String country;

	@Column(name = "COUNTRY_ISO")
	private String countryISO;

	@Column(name = "SUBDIVISION")
	private String subdivision;

	@Column(name = "SUBDIVISION_ISO")
	private String subdivisionISO;

	@Column(name = "CITY")
	private String city;

	@Column(name = "POSTAL")
	private String postal;

	public Contador(String ipNumber) {
		this.ipNumber = ipNumber;
		this.datetime = LocalDateTime.now();
	}
	
	public RealmEntity getRealm() {
		return realm;
	}

	public void setRealm(RealmEntity realm) {
		this.realm = realm;
	}
	
	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getIpNumber() {
		return ipNumber;
	}

	public void setIpNumber(String ipNumber) {
		this.ipNumber = ipNumber;
	}

	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}

	public Long getGcmid() {
		return gcmid;
	}

	public void setGcmid(Long gcmid) {
		this.gcmid = gcmid;
	}

	public Geometry getGeometry() {
		return geometry;
	}

	public void setGeometry(Geometry geometry) {
		this.geometry = geometry;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryISO() {
		return countryISO;
	}

	public void setCountryISO(String countryISO) {
		this.countryISO = countryISO;
	}

	public String getSubdivision() {
		return subdivision;
	}

	public void setSubdivision(String subdivision) {
		this.subdivision = subdivision;
	}

	public String getSubdivisionISO() {
		return subdivisionISO;
	}

	public void setSubdivisionISO(String subdivisionISO) {
		this.subdivisionISO = subdivisionISO;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostal() {
		return postal;
	}

	public void setPostal(String postal) {
		this.postal = postal;
	}

}
