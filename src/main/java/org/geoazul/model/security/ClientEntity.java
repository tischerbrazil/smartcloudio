package org.geoazul.model.security;


import org.hibernate.annotations.NamedQuery;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.Nationalized;
import com.erp.modules.commonClasses.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "SEC_CLIENT", uniqueConstraints = { @UniqueConstraint(columnNames = { "REALM_ID", "CLIENT_ID" }) })
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@NamedQueries({
		@NamedQuery(name = ClientEntity.CLIENT_GET_BY_CLIENTID, query = "select client from ClientEntity client WHERE client.clientId = :clientId"),
		@NamedQuery(name = ClientEntity.CLIENT_GET_BY_REALM, query = "select client from ClientEntity client WHERE client.clientId = :clientId "
				+ "AND client.realm.id = :realmId"),
		@NamedQuery(name = ClientEntity.CLIENT_GET_BY_ID, query = "select client from ClientEntity client where client.id = :id") ,
		@NamedQuery(name = "ClientAll", query = "select client from ClientEntity client") 		
})
public abstract class ClientEntity  extends BaseEntity  {

	public static final String CLIENT_GET_BY_CLIENTID = "ClientEntity.CLIENT_GET_BY_CLIENTID";
	public static final String CLIENT_GET_BY_REALM = "ClientEntity.CLIENT_GET_BY_REALM";
	public static final String CLIENT_GET_BY_ID = "ClientEntity.CLIENT_GET_BY_ID";
	public static final String CLIENT_ALL = "ClientAll";

	
	@ColumnDefault("false")
	@Column(name = "system")
	private Boolean system;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "dtype", insertable = false, updatable = false)
	private String dtype;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "REALM_ID")
	protected RealmEntity realm;

	@Column(name = "INTRODUCTION")
	private String introduction;

	@Nationalized
	@Column(name = "DESCRIPTION")
	private String description;

	@Column(name = "CLIENT_ID")
	protected String clientId;

	@Column(name = "ENABLED")
	private Boolean enabled;

	@Column(name = "SECRET")
	private String secret;

	@Column(name = "USERNAME")
	private String username;

	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "SERVERNAME")
	private String serverName;

	@OneToMany(cascade = { CascadeType.REMOVE }, orphanRemoval = true, mappedBy = "client")
	protected Collection<ClientAttributeEntity> attributes = new ArrayList<>();

	// IDENTITY SUPER
	public ClientEntity(String clientId, String name, String description, String serverName, RealmEntity realm,
			Boolean enabled) {
		this.clientId = clientId;
		this.clientId = clientId;
		this.description = description;
		this.serverName = serverName;
		this.realm = realm;
		this.setEnabled(enabled);
	}

	// OAUTH SUPER
	public ClientEntity(String clientId, String name, String description, String serverName, RealmEntity realm,
			String secret, String username, String password, boolean enabled) {
		this.clientId = clientId;
		this.description = description;
		this.serverName = serverName;
		this.realm = realm;
		this.secret = secret;
		this.username = username;
		this.password = password;
		this.setEnabled(enabled);
	}

	public ClientEntity() {
		//this.id = 
	}

	public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

	public RealmEntity getRealm() {
		return realm;
	}

	public void setRealm(RealmEntity realm) {
		this.realm = realm;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Collection<ClientAttributeEntity> getAttributes() {
		return attributes;
	}

	public void setAttributes(Collection<ClientAttributeEntity> attributes) {
		this.attributes = attributes;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}



	// -------------

	// FIXME Acredito nao ser preciso isto
	// @ManyToMany(fetch = FetchType.LAZY)
	// @JoinTable(name = "LIB_MIDIAITEM_STUDENT", joinColumns = {
	// @JoinColumn(name = "STUDENTID", nullable = false, updatable = false)},
	// inverseJoinColumns = {
	// @JoinColumn(name = "MIDIAITEMID", nullable = false, updatable = false)})
	// @JsonIgnore
	// private List<AbstractItem> midiaItems;

	public void addAttribute(ClientAttributeEntity fieldItem) {
		this.attributes.add(fieldItem);
	}

	public void updateAttribute(ClientAttributeEntity attributeItem) {
		List<ClientAttributeEntity> atributesNew = new ArrayList<ClientAttributeEntity>();
		this.attributes.retainAll(this.getAttributes());
		for (ClientAttributeEntity attr : this.getAttributes()) {
			if (attr.getClient().getId().equals(attributeItem.getClient().getId())) {
				atributesNew.add(attributeItem);
			} else {
				atributesNew.add(attr);
			}
		}
		this.attributes = atributesNew;
	}

	public void removeAttribute(ClientAttributeEntity attributeItem) {
		List<ClientAttributeEntity> attributesNew = new ArrayList<ClientAttributeEntity>();
		this.attributes.retainAll(this.getAttributes());
		for (ClientAttributeEntity attr : this.getAttributes()) {
			if (!attr.getClient().getId().equals(attributeItem.getClient().getId())) {
				attributesNew.add(attr);
			}
		}
		this.attributes = attributesNew;
	}

	public Boolean getSystem() {
		return system;
	}

	public void setSystem(Boolean system) {
		this.system = system;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

}