package org.geoazul.model.security;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.CollectionAttribute;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

/**
 * Static metamodel for {@link org.geoazul.model.security.ClientEntity}
 **/
@StaticMetamodel(ClientEntity.class)
@Generated("org.hibernate.processor.HibernateProcessor")
public abstract class ClientEntity_ { 

	
	/**
	 * @see #id
	 **/
	public static final String ID = "id";
	
	/**
	 * @see #system
	 **/
	public static final String SYSTEM = "system";
	
	/**
	 * @see #imageUrl
	 **/
	public static final String IMAGE_URL = "imageUrl";
	
	/**
	 * @see #dtype
	 **/
	public static final String DTYPE = "dtype";
	
	/**
	 * @see #realm
	 **/
	public static final String REALM = "realm";
	
	/**
	 * @see #introduction
	 **/
	public static final String INTRODUCTION = "introduction";
	
	/**
	 * @see #description
	 **/
	public static final String DESCRIPTION = "description";
	
	/**
	 * @see #clientId
	 **/
	public static final String CLIENT_ID = "clientId";
	
	/**
	 * @see #enabled
	 **/
	public static final String ENABLED = "enabled";
	
	/**
	 * @see #secret
	 **/
	public static final String SECRET = "secret";
	
	/**
	 * @see #username
	 **/
	public static final String USERNAME = "username";
	
	/**
	 * @see #password
	 **/
	public static final String PASSWORD = "password";
	
	/**
	 * @see #serverName
	 **/
	public static final String SERVER_NAME = "serverName";
	
	/**
	 * @see #attributes
	 **/
	public static final String ATTRIBUTES = "attributes";
	
	/**
	 * @see #_ClientEntity_CLIENT_GET_BY_CLIENTID_
	 **/
	public static final String QUERY_CLIENT_ENTITY__CLIENT__GET__BY__CLIENTID = "ClientEntity.CLIENT_GET_BY_CLIENTID";
	
	/**
	 * @see #_ClientEntity_CLIENT_GET_BY_REALM_
	 **/
	public static final String QUERY_CLIENT_ENTITY__CLIENT__GET__BY__REALM = "ClientEntity.CLIENT_GET_BY_REALM";
	
	/**
	 * @see #_ClientEntity_CLIENT_GET_BY_ID_
	 **/
	public static final String QUERY_CLIENT_ENTITY__CLIENT__GET__BY__ID = "ClientEntity.CLIENT_GET_BY_ID";

	
	/**
	 * Static metamodel type for {@link org.geoazul.model.security.ClientEntity}
	 **/
	public static volatile EntityType<ClientEntity> class_;
	
	/**
	 * Static metamodel for attribute {@link org.geoazul.model.security.ClientEntity#id}
	 **/
	public static volatile SingularAttribute<ClientEntity, Long> id;
	
	/**
	 * Static metamodel for attribute {@link org.geoazul.model.security.ClientEntity#system}
	 **/
	public static volatile SingularAttribute<ClientEntity, Boolean> system;
	
	/**
	 * Static metamodel for attribute {@link org.geoazul.model.security.ClientEntity#imageUrl}
	 **/
	public static volatile SingularAttribute<ClientEntity, String> imageUrl;
	
	/**
	 * Static metamodel for attribute {@link org.geoazul.model.security.ClientEntity#dtype}
	 **/
	public static volatile SingularAttribute<ClientEntity, String> dtype;
	
	/**
	 * Static metamodel for attribute {@link org.geoazul.model.security.ClientEntity#realm}
	 **/
	public static volatile SingularAttribute<ClientEntity, RealmEntity> realm;
	
	/**
	 * Static metamodel for attribute {@link org.geoazul.model.security.ClientEntity#introduction}
	 **/
	public static volatile SingularAttribute<ClientEntity, String> introduction;
	
	/**
	 * Static metamodel for attribute {@link org.geoazul.model.security.ClientEntity#description}
	 **/
	public static volatile SingularAttribute<ClientEntity, String> description;
	
	/**
	 * Static metamodel for attribute {@link org.geoazul.model.security.ClientEntity#clientId}
	 **/
	public static volatile SingularAttribute<ClientEntity, String> clientId;
	
	/**
	 * Static metamodel for attribute {@link org.geoazul.model.security.ClientEntity#enabled}
	 **/
	public static volatile SingularAttribute<ClientEntity, Boolean> enabled;
	
	/**
	 * Static metamodel for attribute {@link org.geoazul.model.security.ClientEntity#secret}
	 **/
	public static volatile SingularAttribute<ClientEntity, String> secret;
	
	/**
	 * Static metamodel for attribute {@link org.geoazul.model.security.ClientEntity#username}
	 **/
	public static volatile SingularAttribute<ClientEntity, String> username;
	
	/**
	 * Static metamodel for attribute {@link org.geoazul.model.security.ClientEntity#password}
	 **/
	public static volatile SingularAttribute<ClientEntity, String> password;
	
	/**
	 * Static metamodel for attribute {@link org.geoazul.model.security.ClientEntity#serverName}
	 **/
	public static volatile SingularAttribute<ClientEntity, String> serverName;
	
	/**
	 * Static metamodel for attribute {@link org.geoazul.model.security.ClientEntity#attributes}
	 **/
	public static volatile CollectionAttribute<ClientEntity, ClientAttributeEntity> attributes;
	


}
