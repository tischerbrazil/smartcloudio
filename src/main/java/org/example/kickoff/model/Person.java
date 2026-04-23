package org.example.kickoff.model;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.EAGER;
import static jakarta.persistence.FetchType.LAZY;
import static java.util.stream.Collectors.toSet;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.kickoff.model.validator.Email;
import org.geoazul.ecommerce.model.Item;
import org.geoazul.ecommerce.view.shopping.ShoppingCart;
import org.geoazul.erp.Cities;
import org.geoazul.erp.Countries;
import org.geoazul.erp.States;
import org.geoazul.model.Address;
import org.geoazul.model.app.AppUserMappingEntity;
import org.geoazul.model.ctm.ParcelUserMappingEntity;
import org.geoazul.model.security.UserAttributeEntity;
import org.geoazul.model.website.ModuleComponentMap;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import com.erp.modules.inventory.entities.ErpProductUserMapping;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import atest.TimestampedEntity;
import br.bancodobrasil.model.JsonJsonNode;
import br.safrapay.api.model.PersonCard;

@Entity
@Table(name = "SEC_USER", uniqueConstraints = { @UniqueConstraint(columnNames = { "email" }),
		@UniqueConstraint(columnNames = { "username" }) })
@NamedQueries({
	    @NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p"),
		@NamedQuery(name = "Person.findById", query = "SELECT p FROM Person p WHERE p.id = :id"),
		@NamedQuery(name = "Person.findByName", query = "SELECT p FROM Person p WHERE p.name = :name"),
		@NamedQuery(name = "Person.findBySupplier", query = "SELECT p FROM Person p WHERE p.supplier = true"),
		@NamedQuery(name = "Person.findByActiveSupplier", query = "SELECT p FROM Person p WHERE p.supplier = true AND p.enabled = true"),
		@NamedQuery(name = "Person.findByCustomer", query = "SELECT p FROM Person p WHERE p.customer = true"),
		@NamedQuery(name = "Person.findByCustomerById", query = "SELECT p FROM Person p WHERE p.customer = true and p.id = :id"),
		@NamedQuery(name = "Person.findByActiveCustomer", query = "SELECT p FROM Person p WHERE p.customer = true AND p.enabled = true"),
		@NamedQuery(name = "Person.findByEmail", query = "SELECT p FROM Person p WHERE p.email = :email"),
		@NamedQuery(name = "Person.findByPhone", query = "SELECT p FROM Person p WHERE p.phone = :phone"),
		@NamedQuery(name = "Person.findByCredit", query = "SELECT p FROM Person p WHERE p.credit = :credit"),
		@NamedQuery(name = "Person.findByDebit", query = "SELECT p FROM Person p WHERE p.debit = :debit"),
		@NamedQuery(name = "Person.findByMobile", query = "SELECT p FROM Person p WHERE p.mobile = :mobile"),
		@NamedQuery(name = "Person.findByIsCompany", query = "SELECT p FROM Person p WHERE p.isCompany = :isCompany"),
		@NamedQuery(name = "Person.findByPurchaseDeals", query = "SELECT p FROM Person p WHERE p.purchaseDeals = :purchaseDeals"),
		@NamedQuery(name = "Person.findBySaleDeals", query = "SELECT p FROM Person p WHERE p.saleDeals = :saleDeals"),
		@NamedQuery(name = "Person.findByActive", query = "SELECT p FROM Person p WHERE p.enabled = :active"),
		@NamedQuery(name = "setIsCompanyTrue", query = "UPDATE Person SET isCompany = true  where id = :userid"),
		@NamedQuery(name = "setIsCompanyFalse", query = "UPDATE Person SET isCompany = false  where id = :userid"),
		@NamedQuery(name = "getAllUsers", query = "select u from Person u order by u.username"),
		@NamedQuery(name = "getUserById", query = "select u from Person u where u.id = :id"),
		@NamedQuery(name = "getUserByUuid", query = "select u from Person u where u.uuid = :uuid"),
		@NamedQuery(name = "getUserByUsername", query = "select u from Person u where u.username = :username"),
		@NamedQuery(name = "getUserByEmail", query = "select u from Person u where u.email = :email"),
		@NamedQuery(name = "getUserByUsername_", query = "select u from Person u where u.username = :username AND u.id <> :id"),
		@NamedQuery(name = "getUserByEmail_", query = "select u from Person u where u.email = :email AND u.id <> :id"),
		@NamedQuery(name = "getUserCount", query = "select count(u) from Person u"),
		@NamedQuery(name = "getUsersByAttributeNameAndValue", query = "select u from Person u join u.attributes attr "
				+ "where attr.name = :name and attr.value = :value"), 
		@NamedQuery(name = "getByLoginToken2", query = "select person from Person person join person.loginTokens loginToken"
				+ " where loginToken.tokenHash = :tokenHash AND"
				+ " loginToken.type = :tokenType AND" 
				+ " loginToken.expiration > CURRENT_TIMESTAMP"), 
		
		
		
		  @NamedQuery(name="updateMailVerified", query="update Person person "
		    		+ "set  person.emailVerified = true where person = :person"),
		  
		})


public class Person extends TimestampedEntity  {
	
	public static final String FIND_ALL = "Person.findAll";
	private static final long serialVersionUID = 1L;
	public static final String USER_ID_GET = "getUserById";
	public static final String USER_UUID_GET = "getUserByUuid";
	public static final String USER_USERNAME_GET = "getUserByUsername";
	public static final String USER_EMAIL_GET = "getUserByEmail";
	public static final String USER_USERNAME_GET_DIF = "getUserByUsername_";
	public static final String USER_EMAIL_GET_DIF = "getUserByEmail_";
	public static final String USER_GET_ALL = "getAllUsers";
	public static final String USER_COMPANY_TRUE = "setIsCompanyTrue";
	public static final String USER_COMPANY_FALSE = "setIsCompanyFalse";
	
	public static final String UPDATE_MAIL_VERIFIED = "updateMailVerified";
	
	public static final String GET_BY_LOGIN_TOKEN = "getByLoginToken2";

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, mappedBy = "person")
	@JsonIgnore
	private List<ErpProductUserMapping> wishlist = new ArrayList<ErpProductUserMapping>();

	public static final int EMAIL_MAXLENGTH = 254;
	public static final int NAME_MAXLENGTH = 32;

	@Column(length = EMAIL_MAXLENGTH, nullable = false, unique = true)
	private @NotNull @Size(max = EMAIL_MAXLENGTH) @Email String email;

	@Column(length = NAME_MAXLENGTH, nullable = true)
	private @Size(max = NAME_MAXLENGTH) String firstName;

	@Column(name = "last_name", length = NAME_MAXLENGTH, nullable = true)
	private @Size(max = NAME_MAXLENGTH) String lastName;

	@Column(name = "full_name", length = NAME_MAXLENGTH, nullable = true)
	private @NotNull @Size(max = NAME_MAXLENGTH) String fullName;

	@Column(name = "emailverified" , nullable = false)
	private boolean emailVerified; // For now.

	@OneToOne(mappedBy = "person", fetch = LAZY, cascade = ALL)
	private Credentials credentials;

	@OneToMany(mappedBy = "person", fetch = LAZY, cascade = ALL, orphanRemoval = true)
	private List<LoginToken> loginTokens = new ArrayList<>();

	@ElementCollection(fetch = EAGER)
	@CollectionTable(name = "SEC_USER_GROUPS")
	private @Enumerated(STRING) Set<Group> groups = new HashSet<>();

	@Column(name = "last_login")
	private Instant lastLogin;
	
	public Person() {
		this.uuid = UUID.randomUUID();
		this.enabled = true;
		this.isSameAddress = true;
		this.createDate = LocalDateTime.now();
		this.isCompany = false;
		this.debit = 0D;
		this.credit = 0D;
		this.supplier = false;
		this.customer = false;
		this.isCompany = false;
	}

	public Person(UUID uuid, String name, String username, String email) {
		this.uuid = uuid;
		this.enabled = true;
		this.isSameAddress = true;
		this.name = name;
		this.username = username;
		this.email = email;
		this.debit = 0D;
		this.credit = 0D;
		this.supplier = false;
		this.customer = false;
		this.isCompany = false;
	}

	@NaturalId
	@Column(name = "UUID", columnDefinition = "uuid", updatable = false, nullable = true)
	protected UUID uuid;

	@Column(name = "username", length = 50, nullable = false)
	private String username;
	
	@Column(name = "name", nullable = false)
	@Size(min = 1, max = 128, message = "{LongString}")
	private String name;

	@Basic(optional = false)
	@Column(name = "ENABLED")
	private boolean enabled;

	@Type(JsonJsonNode.class)
	@Column(name = "field")
	@JsonIgnore
	@ColumnDefault("'{}'") 
	private JsonNode strings;

	@OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy = "user", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SELECT)
	@BatchSize(size = 20)
	@JsonIgnore
	private Collection<UserAttributeEntity> attributes = new ArrayList<UserAttributeEntity>();

	@Column(name = "phone", length = 20)
	private String phone;

	@Column(name = "cpf", length = 14)
	private String cpf;

	@Column(name = "image")
	private String image;

    @ColumnDefault("true")
	@Column(name = "supplier")
	private Boolean supplier;

    @ColumnDefault("true")
	@Column(name = "customer")
	private Boolean customer;

	@Size(max = 64, message = "{LongString}")
	@Column(name = "razao_social")
	private String razao_social;

	@Size(max = 64, message = "{LongString}")
	@Column(name = "inscricao_estadual")
	private String inscricao_estadual;

	@Size(max = 18, message = "{LongString}")
	@Column(name = "cnpj")
	private String cnpj;

	@Column(name = "create_date")
	private LocalDateTime createDate;

	@ColumnDefault("0.00")
	@Column(name = "credit")
	private Double credit;

	@ColumnDefault("0.00")
	@Column(name = "debit")
	private Double debit;

	@Size(max = 64, message = "{LongString}")
	@Column(name = "mobile")
	private String mobile;

    @ColumnDefault("false")
	@Column(name = "is_company")
	private Boolean isCompany;

	@Column(name = "purchase_deals")
	private Integer purchaseDeals;

	@Column(name = "sale_deals")
	private Integer saleDeals;
	
	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH }, 
			orphanRemoval = false, mappedBy = "partner")
	@JsonIgnore

	@OrderBy("id DESC")
	private List<PersonCard> cardList = new ArrayList<PersonCard>();

	@OneToMany(mappedBy = "partner", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Item> product;

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH }, orphanRemoval = true, mappedBy = "user")
	@JsonIgnore
	@OrderBy("id DESC")
	private List<ShoppingCart> shoppingCarts = new ArrayList<ShoppingCart>();

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

	@Column(name = "BIRTHDATE")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	private LocalDate birthdate;

	@Transient
	@JsonIgnore
	private String password;

	@Transient
	@JsonIgnore
	private String confirmPassword;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "person", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Address> addresses = new ArrayList<Address>();

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
	
	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
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

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, mappedBy = "person")
	@JsonIgnore
	private List<AppUserMappingEntity> projects = new ArrayList<AppUserMappingEntity>();

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, mappedBy = "person")
	@JsonIgnore
	private List<ParcelUserMappingEntity> parcels = new ArrayList<ParcelUserMappingEntity>();

    @ColumnDefault("true")
	@Column(name = "same_address")
	@JsonIgnore
	private Boolean isSameAddress;

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public JsonNode getStrings() {
		return strings;
	}

	public void setStrings(JsonNode strings) {
		this.strings = strings;
	}

	public void addAttribute(UserAttributeEntity fieldItem) {
		this.attributes.add(fieldItem);
	}

	public void updateAttribute(UserAttributeEntity attributeItem) {
		List<UserAttributeEntity> atributesNew = new ArrayList<UserAttributeEntity>();
		this.attributes.retainAll(this.getAttributes());
		for (UserAttributeEntity attr : this.getAttributes()) {
			if (attr.getId().equals(attributeItem.getId())) {
				atributesNew.add(attributeItem);
			} else {
				atributesNew.add(attr);
			}
		}
		this.attributes = atributesNew;
	}

	public void removeAttribute(UserAttributeEntity attributeItem) {
		List<UserAttributeEntity> attributesNew = new ArrayList<UserAttributeEntity>();
		this.attributes.retainAll(this.getAttributes());
		for (UserAttributeEntity attr : this.getAttributes()) {
			if (!attr.getId().equals(attributeItem.getId())) {
				attributesNew.add(attr);
			}
		}
		this.attributes = attributesNew;
	}

	public Collection<UserAttributeEntity> getAttributes() {
		return attributes;
	}

	public void setAttributes(Collection<UserAttributeEntity> attributes) {
		this.attributes = attributes;
	}

	public String getAttribute(String name) {
		if (this.getAttributes() != null) {
			for (UserAttributeEntity attr : this.getAttributes()) {
				if (attr.getName().equals(name)) {
					return attr.getValue();
				}
			}
		}
		return null;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@JsonIgnore
	public Address getAddress() {
		Address retornoAdd = new Address();
		for (Address myAddress : this.getAddresses()) {  
				retornoAdd = myAddress;
		}
		return retornoAdd;
	}

	public void addAddress(Address address) {
		addresses.add(address);
		address.setPerson(this);
	}

	public void removeAddress(Address address) {
		addresses.remove(address);
		address.setPerson(this);
	}

	public List<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<Address> addresses) {
		this.addresses = addresses;
	}

	

	public List<AppUserMappingEntity> getProjects() {
		return projects;
	}

	public void setProjects(List<AppUserMappingEntity> projects) {
		this.projects = projects;
	}

	public List<ParcelUserMappingEntity> getParcels() {
		return parcels;
	}

	public void setParcels(List<ParcelUserMappingEntity> parcels) {
		this.parcels = parcels;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getSupplier() {
		return supplier;
	}

	public void setSupplier(Boolean supplier) {
		this.supplier = supplier;
	}

	public Boolean getCustomer() {
		return customer;
	}

	public void setCustomer(Boolean customer) {
		this.customer = customer;
	}

	public Double getCredit() {
		if (credit == null) {
			credit = 0d;
		}
		return credit;
	}

	public void setCredit(Double credit) {
		this.credit = credit;
	}

	public Double getDebit() {
		if (debit == null) {
			debit = 0d;
		}
		return debit;
	}

	public void setDebit(Double debit) {
		this.debit = debit;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Boolean getIsCompany() {
		return isCompany;
	}

	public void setIsCompany(Boolean isCompany) {
		this.isCompany = isCompany;
	}

	public Integer getPurchaseDeals() {
		return purchaseDeals;
	}

	public void setPurchaseDeals(Integer purchaseDeals) {
		this.purchaseDeals = purchaseDeals;
	}

	public Integer getSaleDeals() {
		return saleDeals;
	}

	public void setSaleDeals(Integer saleDeals) {
		this.saleDeals = saleDeals;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public String getRazao_social() {
		return razao_social;
	}

	public void setRazao_social(String razao_social) {
		this.razao_social = razao_social;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getInscricao_estadual() {
		return inscricao_estadual;
	}

	public void setInscricao_estadual(String inscricao_estadual) {
		this.inscricao_estadual = inscricao_estadual;
	}

	public Boolean getIsSameAddress() {
		return isSameAddress;
	}

	public void setIsSameAddress(Boolean isSameAddress) {
		this.isSameAddress = isSameAddress;
	}

	public List<ShoppingCart> getShoppingCarts() {
		return shoppingCarts;
	}

	public void setShoppingCarts(List<ShoppingCart> shoppingCarts) {
		this.shoppingCarts = shoppingCarts;
	}

	public List<Item> getProduct() {
		return product;
	}

	public void setProduct(List<Item> product) {
		this.product = product;
	}
	
	
	public int getGenderValue() {
		return genderValue;
	}

	public void setGenderValue(int genderValue) {
		this.genderValue = genderValue;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<PersonCard> getCardList() {
		return cardList;
	}
	
	public void setCardList(List<PersonCard> cardList) {
		this.cardList = cardList;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public Credentials getCredentials() {
		return credentials;
	}

	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}

	public List<LoginToken> getLoginTokens() {
		return loginTokens;
	}

	public Set<Group> getGroups() {
		return groups;
	}

	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public Instant getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Instant lastLogin) {
		this.lastLogin = lastLogin;
	}

	@Transient
	public Set<Role> getRoles() {
		return groups.stream().flatMap(g -> g.getRoles().stream()).collect(toSet());
	}

	@Transient
	public Set<String> getRolesAsStrings() {
		return getRoles().stream().map(Role::name).collect(toSet());
	}

	public List<ErpProductUserMapping> getWishlist() {
		return wishlist;
	}

	public void setWishlist(List<ErpProductUserMapping> wishlist) {
		this.wishlist = wishlist;
	}

	@Override
	public Object getSample() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getTimestamp() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}