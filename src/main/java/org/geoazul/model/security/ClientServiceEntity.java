package org.geoazul.model.security;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQueries({
		@NamedQuery(name = "ClientServiceEntity.SERVICE_CLIENT_ID", query = "select client from ClientServiceEntity client where client.clientId = :clientId"),
		@NamedQuery(name = "ClientServiceEntity.SERVICE_ID_BYNAME", query = "select client.id from ClientServiceEntity client where client.clientId = :clientId"),
		@NamedQuery(name = "ClientServiceEntity.SERVICE_ENTITY_BYNAME", query = "select client from ClientServiceEntity client where client.clientId = :clientId") })
@DiscriminatorValue("SERVICE")
public class ClientServiceEntity extends ClientEntity {

	//public static final String SERVICE_CLIENT_ID = "ClientServiceEntity.findClientId";
	//public static final String SERVICE_ID_BYNAME = "ClientServiceEntity.findClientIdByName";
	//public static final String SERVICE_ENTITY_BYNAME = "ClientServiceEntity.findClientEntityByName";

	public ClientServiceEntity(String clientId) {
		this.clientId = clientId;
	}

	public ClientServiceEntity() {
	}

	

}