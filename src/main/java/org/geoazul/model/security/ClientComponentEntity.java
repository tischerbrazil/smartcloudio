package org.geoazul.model.security;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQueries({
    @NamedQuery(name = ClientComponentEntity.COMPONENT_CLIENT_ID_REALM, query = "select client from ClientComponentEntity client WHERE client.clientId = :clientId "
		+ "AND client.realm.id = :realmId"),
	@NamedQuery(name = ClientComponentEntity.COMPONENT_CLIENT_ID, query = "select client from ClientComponentEntity client where client.clientId = :clientId"),
	
})

@DiscriminatorValue("COMPONENT")
public class ClientComponentEntity extends ClientEntity {


	public static final String COMPONENT_CLIENT_ID = "ClientComponentEntity.findClientId";
	public static final String COMPONENT_CLIENT_ID_REALM = "ClientComponentEntity.findClientIdByRealm";



	public ClientComponentEntity() {
	}


}