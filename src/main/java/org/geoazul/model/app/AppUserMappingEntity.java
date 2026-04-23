/*
 * Copyright 2019 GeoAzul, Inc. and/or its affiliates
 */

package org.geoazul.model.app;

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

@Table(name="APP_USER_MAPPING")
@Entity
@IdClass(AppUserMappingEntity.Key.class) 
@NamedQueries({
	@NamedQuery(name=AppUserMappingEntity.DELETE, query="delete from AppUserMappingEntity m where m = :appUserMappingEntity"),
    @NamedQuery(name=AppUserMappingEntity.DELETE_BY_MOD_AND_MID, query="delete from AppUserMappingEntity m where m.app = :app and m.person = :PersonItem"),
    @NamedQuery(name=AppUserMappingEntity.INSERT, query="insert into  "
    		+ "AppUserMappingEntity (person, app, appRole )    select person, app, :appRole from Person person, AbstractIdentityEntity app where person = :urlPerson AND  app = :app"),
    @NamedQuery(name= AppUserMappingEntity.APP_USER_MAP_DEL,   query="delete from AppUserMappingEntity m where m.app = :app"),
    @NamedQuery(name=AppUserMappingEntity.SELECT, 
	  query="select appsUserMap from AppUserMappingEntity appsUserMap  where person.uuid = :personUuid"),
    @NamedQuery(name=AppUserMappingEntity.SELECT_ALL, 
	  query="select appsUserMap from AppUserMappingEntity appsUserMap")
}) 
public class AppUserMappingEntity  {

	public static final String APP_USER_MAP_DEL = "AppUserMappingEntity.deleteAppUserMappingsByApp";
	 public static final String DELETE = "AppUserMappingEntity.delete";
	 public static final String DELETE_BY_MOD_AND_MID = "AppUserMappingEntity.deleteByModAndMId";
	 public static final String INSERT = "AppUserMappingEntity.insert";
	 public static final String SELECT = "AppUserMappingEntity.select";
	 public static final String SELECT_ALL = "AppUserMappingEntity.selectAll";
    
	 @Id
    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name="APP_ID")
    private AbstractIdentityEntity app;
    
    @Id
    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.DETACH)	
    @JoinColumn(name="USER_ID")
    private Person person;
    
    public enum AppRole {
    	OWNER, ADMINISTRATOR, DEVELOPER, EDITOR, AUTHOR, COLABOLATOR, USER
	}
    // FIXME OWNER, ADMINISTRATOR, DEVELOPER... 7 LEVES HERE
	
	@Enumerated(EnumType.STRING)
	@Column(name = "APP_ROLE")
	private AppRole appRole = AppRole.USER;
        
    public AppUserMappingEntity() {
    	super();
	}

    public AppUserMappingEntity(AbstractIdentityEntity app, 
    		Person person) {
		super();
		this.app = app;
		this.person = person;
	}

	public AbstractIdentityEntity getApp() {
        return app;
    }

    public void setApp(AbstractIdentityEntity app) {
        this.app = app;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    
    public AppRole getAppRole() {
		return appRole;
	}

	public void setAppRole(AppRole appRole) {
		this.appRole = appRole;
	}
    
    public static class Key implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private AbstractIdentityEntity app;
		private Person person;

        public Key() {
        }

        public Key(AbstractIdentityEntity app, Person person) {
            this.app = app;
            this.person = person;
        }

        public AbstractIdentityEntity getApp() {
            return app;
        }
        
        public void setApp(AbstractIdentityEntity app) {
            this.app = app;
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
			result = prime * result + ((app == null) ? 0 : app.hashCode());
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
			if (app == null) {
				if (other.app != null)
					return false;
			} else if (!app.equals(other.app))
				return false;
			return true;
		}
    }
}