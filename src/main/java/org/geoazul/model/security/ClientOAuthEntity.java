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
	    @NamedQuery(name = ClientOAuthEntity.OAUTH_CLIENT_ID_REALM, query = "select client from ClientOAuthEntity client WHERE client.realm.name = :realmName "
			+ "AND client.realm.id = :realmId"),
		@NamedQuery(name = ClientOAuthEntity.OAUTH_CLIENT_ID, query = "select client from ClientOAuthEntity client where client.clientId = :clientId"),
		@NamedQuery(name = ClientOAuthEntity.OAUTH_ID_BYNAME, query = "select client.id from ClientOAuthEntity client where client.clientId = :clientId"),
		@NamedQuery(name = ClientOAuthEntity.OAUTH_ENTITY_BYNAME, query = "select client from ClientOAuthEntity client where client.clientId = :clientId") })
@DiscriminatorValue("OAUTH")
public class ClientOAuthEntity extends ClientEntity {

	public static final String OAUTH_CLIENT_ID_REALM = "ClientOAuthEntity.findClientIdRealm";
	public static final String OAUTH_CLIENT_ID = "ClientOAuthEntity.findClientId";
	public static final String OAUTH_ID_BYNAME = "ClientOAuthEntity.findClientIdByName";
	public static final String OAUTH_ENTITY_BYNAME = "ClientOAuthEntity.findClientEntityByName";

	public ClientOAuthEntity() {
	}

	public ClientOAuthEntity(String clientId, String name, String description,  String serverName,
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