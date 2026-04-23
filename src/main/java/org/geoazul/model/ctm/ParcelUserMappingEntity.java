/*
 * Copyright 2019 GeoAzul, Inc. and/or its affiliates
 */

package org.geoazul.model.ctm;

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
import org.example.kickoff.model.Person;
import java.io.Serializable;
import jakarta.persistence.CascadeType;

@Table(name="CTM_PARCEL_USER_MAPPING")
@Entity
@IdClass(ParcelUserMappingEntity.Key.class) 
@NamedQueries({
	@NamedQuery(name=ParcelUserMappingEntity.DELETE, query="delete from ParcelUserMappingEntity m where m = :parcelUserMappingEntity"),
    @NamedQuery(name=ParcelUserMappingEntity.DELETE_BY_MOD_AND_MID, query="delete from ParcelUserMappingEntity m where m.parcel = :parcel and m.person = :Person"),
    @NamedQuery(name=ParcelUserMappingEntity.INSERT, query="insert into  "
    		+ "ParcelUserMappingEntity (person, parcel, parcelRole )    select person, parcel, :parcelRole from Person person, ParcelEntity parcel where person = :person AND  parcel = :parcel"),
    @NamedQuery(name= ParcelUserMappingEntity.PARCEL_USER_MAP_DEL,   query="delete from ParcelUserMappingEntity m where m.parcel = :parcel"),
    @NamedQuery(name=ParcelUserMappingEntity.SELECT, 
	  query="select parcelsUserMap from ParcelUserMappingEntity parcelsUserMap  where person.uuid = :personUuid"),
    @NamedQuery(name=ParcelUserMappingEntity.SELECT_ALL, 
	  query="select parcelsUserMap from ParcelUserMappingEntity parcelsUserMap")
}) 
public class ParcelUserMappingEntity  {

	 public static final String PARCEL_USER_MAP_DEL = "ParcelUserMappingEntity.deleteParcelEntityUserMappingsByParcel";
	 public static final String DELETE = "ParcelUserMappingEntity.delete";
	 public static final String DELETE_BY_MOD_AND_MID = "ParcelUserMappingEntity.deleteByModAndMId";
	 public static final String INSERT = "ParcelUserMappingEntity.insert";
	 public static final String SELECT = "ParcelUserMappingEntity.select";
	 public static final String SELECT_ALL = "ParcelUserMappingEntity.selectAll";
    
	 @Id
    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name="PARCEL_ID")
    private ParcelEntity parcel;
    
    @Id
    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.DETACH)	
    @JoinColumn(name="USER_ID")
    private Person person;
    
    public enum ParcelRole {
    	OWNER, RENT, RESIDENCE
	}
    // FIXME OWNER, ADMINISTRATOR, DEVELOPER... 7 LEVES HERE
	
	@Enumerated(EnumType.STRING)
	@Column(name = "PARCEL_ROLE")
	private ParcelRole parcelRole = ParcelRole.OWNER;
        
    public ParcelUserMappingEntity() {
    	super();
	}

    public ParcelUserMappingEntity(ParcelEntity parcel, 
    		Person person) {
		super();
		this.parcel = parcel;
		this.person = person;
	}

	public ParcelEntity getApp() {
        return parcel;
    }

    public void setApp(ParcelEntity parcel) {
        this.parcel = parcel;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    
    public ParcelRole getParcelRole() {
		return parcelRole;
	}

	public void setParcelRole(ParcelRole parcelRole) {
		this.parcelRole = parcelRole;
	}
    
    public static class Key implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ParcelEntity parcel;
		private Person person;

        public Key() {
        }

        public Key(ParcelEntity parcel, Person person) {
            this.parcel = parcel;
            this.person = person;
        }

        public ParcelEntity getApp() {
            return parcel;
        }
        
        public void setApp(ParcelEntity parcel) {
            this.parcel = parcel;
        }

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }

    @Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((person == null) ? 0 : person.hashCode());
			result = prime * result + ((parcel == null) ? 0 : parcel.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (person == null) {
				if (other.person != null)
					return false;
			} else if (!person.equals(other.person))
				return false;
			if (parcel == null) {
				if (other.parcel != null)
					return false;
			} else if (!parcel.equals(other.parcel))
				return false;
			return true;
		}
    }
}