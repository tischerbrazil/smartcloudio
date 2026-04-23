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

import org.geoazul.model.basic.Comp;

@NamedQueries({
        @NamedQuery(name="deleteComponentAttributesByComponent", query="delete from CompAttributeEntity attr where attr.component = :component")
})
@Table(name="APP_COMPONENT_ATTRIBUTE")
@Entity
@IdClass(CompAttributeEntity.Key.class)
public class CompAttributeEntity {

   
	@Id
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "COMPONENT_ID")
    protected Comp component;

    @Id
    @Column(name = "NAME")
    protected String name;
    
    @Column(name = "VALUE")
    protected String value;
    
    @Column(name = "DTYPE")
    protected String dtype;

    public Comp getComponent() {
        return component;
    }

    public void setComponent(Comp component) {
        this.component = component;
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

        protected Comp component;

        protected String name;

        public Key() {
        }

        public Key(Comp component, String name) {
            this.component = component;
            this.name = name;
        }

        public Comp getComponent() {
            return component;
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
            if (component != null ? !component.getId().equals(key.component != null ? key.component.getId() : null) : key.component != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = component != null ? component.getId().hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((component == null) ? 0 : component.hashCode());
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
		CompAttributeEntity other = (CompAttributeEntity) obj;
		if (component == null) {
			if (other.component != null)
				return false;
		} else if (!component.equals(other.component))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

   
	
   


}
