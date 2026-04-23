package org.geoazul.model.app;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import com.erp.modules.inventory.entities.Product;
import com.erp.modules.inventory.entities.ProductCategoryOne;
import java.io.Serializable;
import jakarta.persistence.CascadeType;

@Table(name="ERP_PRODUCT_CATEGORY_MAP")
@Entity
@IdClass(ProductCategoryMapping.Key.class) 
@NamedQueries({
	@NamedQuery(name=ProductCategoryMapping.DELETE,  
			query="delete from ProductCategoryMapping m where m.product = :productEntity and m.productCategoryOne = :categoryEntity"),
    //@NamedQuery(name=ProductCategoryMapping.DELETE_BY_MOD_AND_MID, query="delete from AppUserMproductingEntity m where m.product = :product and m.productCategory = :ProductCategoryItem"),
    //@NamedQuery(name=ProductCategoryMapping.INSERT, query="insert into  "
    //		+ "AppUserMproductingEntity (productCategory, product, productRole )    select productCategory, product, :productRole from ProductCategory productCategory, Product product where productCategory = :urlProductCategory AND  product = :product"),
    //@NamedQuery(name= ProductCategoryMapping.APP_USER_MAP_DEL,   query="delete from AppUserMproductingEntity m where m.product = :product"),
    //@NamedQuery(name=ProductCategoryMapping.SELECT, 
	//  query="select productsUserMap from AppUserMproductingEntity productsUserMap  where productCategory.uuid = :productCategoryUuid"),
    //@NamedQuery(name=ProductCategoryMapping.SELECT_ALL, 
	 // query="select productsUserMap from AppUserMproductingEntity productsUserMap")
}) 
//@Cacheable
//@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class ProductCategoryMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// public static final String APP_USER_MAP_DEL = "AppUserMproductingEntity.deleteAppUserMproductingsByApp";
	 public static final String DELETE = "ProductCategoryMapping.delete";
	// public static final String DELETE_BY_MOD_AND_MID = "AppUserMproductingEntity.deleteByModAndMId";
	// public static final String INSERT = "AppUserMproductingEntity.insert";
	// public static final String SELECT = "AppUserMproductingEntity.select";
	// public static final String SELECT_ALL = "AppUserMproductingEntity.selectAll";
    
	@Id
    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name="product")
    private Product product;
    
    @Id
    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.DETACH)	
    @JoinColumn(name="category")
    private ProductCategoryOne productCategoryOne;
    
       
    public ProductCategoryMapping() {
    	super();
	}

    public ProductCategoryMapping(Product product, 
    		ProductCategoryOne productCategoryOne) {
		super();
		this.product = product;
		this.productCategoryOne = productCategoryOne;
	}

	public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public ProductCategoryOne getProductCategoryOne() {
        return productCategoryOne;
    }

    public void setProductCategoryOne(ProductCategoryOne productCategoryOne) {
        this.productCategoryOne = productCategoryOne;
    }
    
    
    
    public static class Key implements Serializable {
        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Product product;
		private ProductCategoryOne productCategoryOne;

        public Key() {
        }

        public Key(Product product, ProductCategoryOne productCategoryOne) {
            this.product = product;
            this.productCategoryOne = productCategoryOne;
        }

        public Product getProduct() {
            return product;
        }
        
        public void setProduct(Product product) {
            this.product = product;
        }

        public ProductCategoryOne getProductCategoryOne() {
            return productCategoryOne;
        }

        public void setProductCategoryOne(ProductCategoryOne productCategoryOne) {
            this.productCategoryOne = productCategoryOne;
        }

    @Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((productCategoryOne == null) ? 0 : productCategoryOne.hashCode());
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
			if (productCategoryOne == null) {
				if (other.productCategoryOne != null)
					return false;
			} else if (!productCategoryOne.equals(other.productCategoryOne))
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