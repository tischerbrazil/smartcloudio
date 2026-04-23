package org.geoazul.model.security;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@NamedQueries({
	    @NamedQuery(name = ClientMobileEntity.MOBILE_CLIENT_ID_REALM, query = "select client from ClientMobileEntity client WHERE client.clientId = :clientId "
			+ "AND client.realm.id = :realmId"),
		@NamedQuery(name = ClientMobileEntity.MOBILE_CLIENT_ID2, query = "select client from ClientMobileEntity client where client.clientId = :clientId"),
		@NamedQuery(name = ClientMobileEntity.MOBILE_ID_BYNAME, query = "select client.id from ClientMobileEntity client where client.clientId = :clientId"),
		@NamedQuery(name = ClientMobileEntity.MOBILE_ENTITY_BYNAME, query = "select client from ClientMobileEntity client where client.clientId = :clientId") })
@DiscriminatorValue("MOBILE")
public class ClientMobileEntity extends ClientEntity { 

	public static final String MOBILE_CLIENT_ID_REALM = "ClientMobileEntity.findClientIdRealm";
	public static final String MOBILE_CLIENT_ID2 = "ClientMobileEntity.findClientId";
	public static final String MOBILE_ID_BYNAME = "ClientMobileEntity.findClientIdByName";
	public static final String MOBILE_ENTITY_BYNAME = "ClientMobileEntity.findClientEntityByName";

	public ClientMobileEntity() {
	}

	public ClientMobileEntity(String clientId, String name, String description,  String serverName,
			RealmEntity realm, String secret, String username, String password,
			boolean enabled) {

		super(clientId, name, description, serverName, realm,  secret, username,
				password,  enabled);

	}

	@OneToMany(fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE }, orphanRemoval = true)
	@JoinTable(name = "SEC_CLIENT_DEFAULT_ROLES", joinColumns = {
			@JoinColumn(name = "CLIENT_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLE_ID") })
	Collection<RoleEntity> defaultRoles = new ArrayList<RoleEntity>();

	public Collection<RoleEntity> getDefaultRoles() {
		return defaultRoles;
	}

	public void setDefaultRoles(Collection<RoleEntity> defaultRoles) {
		this.defaultRoles = defaultRoles;
	}

	

}