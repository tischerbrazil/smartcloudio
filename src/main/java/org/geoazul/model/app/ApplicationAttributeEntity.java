package org.geoazul.model.app;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;

@NamedQueries({
        @NamedQuery(name="deleteApplicationAttributesByApplication", query="delete from ApplicationAttributeEntity attr where attr.application = :application")
})
@Table(name="APP_IDENTITY_ATTRIBUTE")
@Entity
@IdClass(ApplicationAttributeEntity.Key.class)
public class ApplicationAttributeEntity {

   
	@Id
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "APPLICATION_ID")
    protected AbstractIdentityEntity application;

    @Id
    @Column(name = "NAME")
    protected String name;
    
    @Column(name = "VALUE")
    protected String value;
    
    @Column(name = "DTYPE")
    protected String dtype;

    public AbstractIdentityEntity getApplication() {
        return application;
    }

    public void setApplication(AbstractIdentityEntity application) {
        this.application = application;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDtype() {
		return dtype;
	}

	public void setDtype(String dtype) {
		this.dtype = dtype;
	}

	@SuppressWarnings("serial")
	public static class Key implements Serializable {

        protected AbstractIdentityEntity application;

        protected String name;

        public Key() {
        }

        public Key(AbstractIdentityEntity application, String name) {
            this.application = application;
            this.name = name;
        }

        public AbstractIdentityEntity getApplication() {
            return application;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (name != null ? !name.equals(key.name) : key.name != null) return false;
            if (application != null ? !application.getId().equals(key.application != null ? key.application.getId() : null) : key.application != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = application != null ? application.getId().hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((application == null) ? 0 : application.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ApplicationAttributeEntity other = (ApplicationAttributeEntity) obj;
		if (application == null) {
			if (other.application != null)
				return false;
		} else if (!application.equals(other.application))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

   
	
   


}
