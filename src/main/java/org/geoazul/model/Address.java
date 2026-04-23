package org.geoazul.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.geoazul.erp.Cities;
import org.geoazul.erp.Countries;
import org.geoazul.erp.States;
import org.example.kickoff.model.Person;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import br.safrapay.api.model.PersonCard;
import br.safrapay.api.model.SafraPayCustomer;
import br.safrapay.api.model.SafraPayCustomerPhone;

import jakarta.persistence.Id;

@Entity
@Table(name = "SEC_ADDRESS")
@NamedQueries({
		@NamedQuery(name = "Address.getAddressUserHome", query = "SELECT a FROM Address a WHERE a.person.id = :userid"),
		 })
@JsonIgnoreProperties({ "id", "person" })
public class Address {

	public static final String ADDRESS_USERID_HOME = "Address.getAddressUserHome";
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	@JsonIgnore
	private Integer id;

	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private Person person;

	@Column(name = "name")
	@JsonIgnore
	private String name;

	
	@Column(name = "street")
	private String street;

	@Column(name = "number_code")
	private String number;

	@Column(name = "neighborhood")
	private String neighborhood;

	@JsonProperty("country")
	public String getCountryJson() {
		return country.getNative_();
	}

	@JsonProperty("state")
	public String getStateJson() {
		return state.getName();
	}

	@JsonProperty("city")
	public String getCityJson() {
		return city.getName();
	}

	@Column(name = "complement")
	private String complement;

	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "country_id")
	@JsonIgnore
	private Countries country;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "state_id")
	@JsonIgnore
	private States state;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "city_id")
	@JsonIgnore
	private Cities city;


	@JsonProperty("zipCode")
	private String zipcode;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNeighborhood() {
		return neighborhood;
	}

	public void setNeighborhood(String neighborhood) {
		this.neighborhood = neighborhood;
	}

	public String getComplement() {
		return complement;
	}

	public void setComplement(String complement) {
		this.complement = complement;
	}

	public Countries getCountry() {
		return country;
	}

	public void setCountry(Countries country) {
		this.country = country;
	}

	public States getState() {
		return state;
	}

	public void setState(States state) {
		this.state = state;
	}

	public Cities getCity() {
		return city;
	}

	public void setCity(Cities city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Address other = (Address) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}



}