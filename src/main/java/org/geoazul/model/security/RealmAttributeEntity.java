package org.geoazul.model.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;



import java.io.Serializable;

/**
 * @author Laercio Tischer
 * @version $Revision: 1 $
 */
@NamedQueries({
	@NamedQuery(name = "deleteRealmAttributesByRealm", query = "delete from RealmAttributeEntity attr where attr.realm = :realm"),
	@NamedQuery(name=RealmAttributeEntity.DELETE, query="delete from RealmAttributeEntity m where m = :realmAttributeEntity")	
})

@Table(name = "SEC_REALM_DNS")
@Entity
@IdClass(RealmAttributeEntity.Key.class)
public class RealmAttributeEntity implements Serializable {

	public static final String DELETE = "RealmAttributeEntity.delete";
	

	public enum Type {
		A, AAAA, CAA, CNAME,  MX, SRV, TXT
	}
	
	@Id
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "REALM_ID")	
	protected RealmEntity realm;

	@Id
	@Column(name = "NAME")
	protected String name;
	
	@Column(name = "TTl")
	protected String ttl;

	@Id
    @Enumerated(EnumType.ORDINAL)
	@Column(name = "TYPE")
    protected Type type ;
	
	@Column(name = "PRIORITY")
	private Integer priority;
	
	@Column(name = "DATA")
	protected String data;
	
	
	
	public RealmEntity getRealm() {
		return realm;
	}

	public void setRealm(RealmEntity realm) {
		this.realm = realm;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getTtl() {
		return ttl;
	}

	public void setTtl(String ttl) {
		this.ttl = ttl;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	public static class Key implements Serializable {



		protected RealmEntity realm;

		protected String name;
		
		protected Type type ;

		public Key() {
		}

		public Key(RealmEntity user, String name, Type type) {
			this.realm = user;
			this.name = name;
			this.type = type;
		}

		public RealmEntity getRealm() {
			return realm;
		}

		public String getName() {
			return name;
		}
		
		public Type getType() {
			return type;
		}
	

		@Override
		public boolean equals(Object o) {
			if (this == o)
				return true;
			if (o == null || getClass() != o.getClass())
				return false;

			Key key = (Key) o;

			if (name != null ? !name.equals(key.name) : key.name != null)
				return false;
			if (realm != null
					? !realm.getId().equals(
							key.realm != null ? key.realm.getId() : null)
					: key.realm != null)
				return false;

			return true;
		}

		@Override
		public int hashCode() {
			int result = realm != null ? realm.getId().hashCode() : 0;
			result = 31 * result + (name != null ? name.hashCode() : 0);
			return result;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof RealmAttributeEntity))
			return false;

		RealmAttributeEntity key = (RealmAttributeEntity) o;

		if (name != null ? !name.equals(key.name) : key.name != null)
			return false;
		if (realm != null
				? !realm.getId().equals(
						key.realm != null ? key.realm.getId() : null)
				: key.realm != null)
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = realm != null ? realm.getId().hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		return result;
	}

	

}
