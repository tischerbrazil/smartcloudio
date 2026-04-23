package br.safrapay.api.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import org.geoazul.model.Address;
import org.example.kickoff.model.Person;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * 
 * 
  * 
 */

@Entity
@Table(name = "gateway_safrapay_customer")
@JsonFilter("safraPayCustomerFilter")
@NamedQueries({
		@NamedQuery(name = "SafraPayCustomer.getCustomerById", query = "select c from SafraPayCustomer c where c.id = :id"),
		@NamedQuery(name = "SafraPayCustomer.getCustomerByUuid", query = "select c from SafraPayCustomer c where c.customer_id = :customer_uuid"),
		@NamedQuery(name = "SafraPayCustomer.getCustomerByPartner", query = "select c from SafraPayCustomer c where c.partner.id = :partner_id"), })
public class SafraPayCustomer implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String CUSTOMER_ID_GET = "SafraPayCustomer.getCustomerById";
	public static final String CUSTOMER_UUID_GET = "SafraPayCustomer.getCustomerByUuid";
	public static final String CUSTOMER_PARTNER_ID = "SafraPayCustomer.getCustomerByPartner";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "smartid", unique = true, nullable = false)
	private Integer smartid;

	@Column(name = "CUSTOMER_ID", nullable = true)
	@JsonProperty("id")
	private UUID customer_id; // ": "ffede3ee-37ab-47bb-9981-d4d14697d67a",

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PARTNER_ID", referencedColumnName = "id")
	@JsonIgnore
	private Person partner;
	// ---------------------------
		
	
	//===============
	@OneToOne(mappedBy = "safraPayCustomer")
	@JsonIgnore
	private SafraPayCharge charge;
	//=================
	
	
	@Column(name = "NAME", nullable = true)
	private String name; // ": "Yago Sebastião Lima",

	@Column(name = "EMAIL", nullable = true)
	private String email; // ": "yagosebastiaolima@archosolutions.com.br",

	@Column(name = "ENTITY_TYPE", nullable = true)
	private Integer entityType; // ": 1 Pessoa Física 2 Company

	@Column(name = "DOCUMENT_TYPE", nullable = true)
	private Integer documentType; // ": 1 Cpf 2 Cnpj 3 passport

	@Column(name = "DOCUMENT", nullable = true)
	private String document; // ": "78112482802",
	
	

	@Basic 
	@Column(name = "gender_value")
	private int genderValue;

	@Transient
	private Gender gender;
	
	

	@PostLoad
	void fillTransient() {

		if (genderValue > -1) {
			this.gender = Gender.of(genderValue);
		}
	}

	@PrePersist
	void fillPersistent() {
		if (gender != null) {
			this.genderValue = gender.getGender();
		}
	}

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "PHONE_ID", referencedColumnName = "id")
	@Transient
	private SafraPayCustomerPhone phone;

	//@OneToOne(cascade = CascadeType.ALL)
	//@JoinColumn(name = "ADDRESS_ID", referencedColumnName = "id")
	//@Transient
	//private Address address;

	
	

	
	
	
	public enum Gender {
		Undefined(0), Female(1), Male(2), Other(3);

		private int gender;

		private Gender(int gender) {
			this.gender = gender;
		}

		public int getGender() {
			return gender;
		}

		public static Gender of(int gender) {
			return Stream.of(Gender.values()).filter(p -> p.getGender() == gender).findFirst()
					.orElseThrow(IllegalArgumentException::new);
		}
	}
	
	public Integer getSmartid() {
		return smartid;
	}

	public void setSmartid(Integer smartid) {
		this.smartid = smartid;
	}

	public UUID getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(UUID customer_id) {
		this.customer_id = customer_id;
	}

	public Person getPartner() {
		return partner;
	}
		
	public SafraPayCharge getCharge() {
		return charge;
	}

	public void setCharge(SafraPayCharge charge) {
		this.charge = charge;
	}

	public void setPartner(Person partner) {
		this.partner = partner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getEntityType() {
		return entityType;
	}

	public void setEntityType(Integer entityType) {
		this.entityType = entityType;
	}

	public Integer getDocumentType() {
		return documentType;
	}

	public void setDocumentType(Integer documentType) {
		this.documentType = documentType;
	}

	public String getDocument() {
		return document;
	}

	public void setDocument(String document) {
		this.document = document;
	}

	public SafraPayCustomerPhone getPhone() {
		return phone;
	}

	public void setPhone(SafraPayCustomerPhone phone) {
		this.phone = phone;
	}

	//public Address getAddress() {
	//	return address;
	//}

	//public void setAddress(Address address) {
	//	this.address = address;
	//}



	
}
