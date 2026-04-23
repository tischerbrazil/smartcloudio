package org.geoazul.model.security;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import com.erp.modules.commonClasses.BaseEntity;
import com.erp.modules.commonClasses.BaseLongEntity;

@Table(name="SEC_CLIENT_ATTRIBUTES")
@Entity
//@IdClass(ClientAttributeEntity.Key.class)

@NamedQueries({
		@NamedQuery(name = "deleteClientAttributes", query = "delete from  ClientAttributeEntity attr where attr.client IN (select u from ClientEntity u)"),
		@NamedQuery(name = "deleteClientAttributesByNameAndClient", query = "delete from  ClientAttributeEntity attr where attr.client.id = :clientid and attr.name = :name"),
		@NamedQuery(name = "clientAttributeFind", query = "select attr from ClientAttributeEntity attr where attr.client.id = :id and attr.name = :name"),
		@NamedQuery(name = "clientAttibuteUpdateName", query = "UPDATE ClientAttributeEntity SET value = :value  where client.id = :id and name = :name"),
		@NamedQuery(name = "clientAttributeCount", query = "SELECT COUNT(attr) FROM ClientAttributeEntity attr WHERE attr.client.id = :id and attr.name = :name"),
	
})

public class ClientAttributeEntity extends BaseLongEntity {
	
	public static final String DELETE_ALL = "deleteClientAttributes";
	public static final String UPDATE_ATTRIB_NAME = "clientAttibuteUpdateName";
	public static final String DELETE_BY_NAME_AND_CLIENT = "deleteClientAttributesByNameAndClient";
	public static final String FIND_ATTRIB = "clientAttributeFind";
	public static final String FIND_COUNT = "clientAttributeCount";
    
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name = "CLIENT_ID")
    protected ClientEntity client;

    @Column(name="NAME")
    protected String name;

    @Column(name = "VALUE", length = 4000)
    protected String value;

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
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


    public static class Key implements Serializable {

        protected ClientEntity client;

        protected String name;

        public Key() {
        }

        public Key(ClientEntity client, String name) {
            this.client = client;
            this.name = name;
        }

        public ClientEntity getClient() {
            return client;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ClientAttributeEntity.Key key = (ClientAttributeEntity.Key) o;

            if (client != null ? !client.getId().equals(key.client != null ? key.client.getId() : null) : key.client != null) return false;
            if (name != null ? !name.equals(key.name != null ? key.name : null) : key.name != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = client != null ? client.getId().hashCode() : 0;
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }
    }

}
