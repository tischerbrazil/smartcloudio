package com.erp.modules.inventory.entities;

import jakarta.persistence.Entity;
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

@Table(name="ERP_PRODUCT_USER_MAPPING")
@Entity
@IdClass(ErpProductUserMapping.Key.class) 
@NamedQueries({
	@NamedQuery(name=ErpProductUserMapping.DELETE, query="delete from ErpProductUserMapping m where m = :erpProductUserMapping"),
    @NamedQuery(name=ErpProductUserMapping.DELETE_BY_MOD_AND_MID, query="delete from ErpProductUserMapping m where m.product = :product and m.person = :person"),
    @NamedQuery(name=ErpProductUserMapping.INSERT, query="insert into  "
    		+ "ErpProductUserMapping (person, product)    select person, product from Person person, Product product where person = :person AND  product = :product"),
    @NamedQuery(name= ErpProductUserMapping.PRODUCT_USER_MAP_DEL,   query="delete from ErpProductUserMapping m where m.product = :product"),
    @NamedQuery(name=ErpProductUserMapping.SELECT, 
	  query="select productsUserMap from ErpProductUserMapping productsUserMap  where person.uuid = :personUuid"),
    
    @NamedQuery(name=ErpProductUserMapping.FIND, 
	  query="select productsUserMap from ErpProductUserMapping productsUserMap  where productsUserMap.person.id = :personId AND productsUserMap.product.id = :productId"),
    
    
    @NamedQuery(name=ErpProductUserMapping.SELECT_ALL, query="select productsUserMap from ErpProductUserMapping productsUserMap")
}) 
public class ErpProductUserMapping  {

	public static final String  PRODUCT_USER_MAP_DEL = "ErpProductUserMapping.deleteAppUserMproductingsByApp";
	 public static final String DELETE = "ErpProductUserMapping.delete";
	 public static final String DELETE_BY_MOD_AND_MID = "ErpProductUserMapping.deleteByModAndMId";
	 public static final String INSERT = "ErpProductUserMapping.insert";
	 public static final String SELECT = "ErpProductUserMapping.select";
	 public static final String FIND = "ErpProductUserMapping.FIND";
	 public static final String SELECT_ALL = "ErpProductUserMapping.selectAll";
    
	 @Id
    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name="PRODUCT_ID")
    private Product product;
    
    @Id
    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.DETACH)	
    @JoinColumn(name="USER_ID")
    private Person person;
    
   	
	
    public ErpProductUserMapping() {
    	super();
	}

    public ErpProductUserMapping(Product product, 
    		Person person) {
		super();
		this.product = product;
		this.person = person;
	}

	public Product getApp() {
        return product;
    }

    public void setApp(Product product) {
        this.product = product;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
    
  
    
    public static class Key implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Product product;
		private Person person;

        public Key() {
        }

        public Key(Product product, Person person) {
            this.product = product;
            this.person = person;
        }

        public Product getApp() {
            return product;
        }
        
        public void setApp(Product product) {
            this.product = product;
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
			result = prime * result + ((product == null) ? 0 : product.hashCode());
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
			if (product == null) {
				if (other.product != null)
					return false;
			} else if (!product.equals(other.product))
				return false;
			return true;
		}
    }
}