package org.geoazul.model.security;

import java.util.UUID;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQueries({
		@NamedQuery(name = ClientIdentityEntity.IDENTITY_CLIENT_ID, query = "select client from ClientIdentityEntity client where client.clientId = :clientId"),
		@NamedQuery(name = ClientIdentityEntity.IDENTITY_ID_BYNAME, query = "select client.id from ClientIdentityEntity client where client.clientId = :clientId"),
		@NamedQuery(name = ClientIdentityEntity.IDENTITY_ENTITY_BYNAME, query = "select client from ClientIdentityEntity client where client.clientId = :clientId") })
@DiscriminatorValue("IDENTITY")
public class ClientIdentityEntity extends ClientEntity {

	public static final String IDENTITY_CLIENT_ID = "ClientIdentityEntity.findClientId";
	public static final String IDENTITY_ID_BYNAME = "ClientIdentityEntity.findClientIdByName";
	public static final String IDENTITY_ENTITY_BYNAME = "ClientIdentityEntity.findClientEntityByName";

	public ClientIdentityEntity(String clientId, String name, String description, 
			 String serverName, RealmEntity realm, boolean enabled) {
		super(clientId, name, description, serverName, realm, enabled);
	}

	public ClientIdentityEntity() {
		super();
	}

	

}