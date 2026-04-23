package org.geoazul.model.website;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SqlResultSetMapping;
//import jakarta.persistence.NamedQueries;
//import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.NamedQueries;
//import org.hibernate.annotations.NamedQuery;
//import org.hibernate.annotations.NamedNativeQueries;
//import org.hibernate.annotations.NamedNativeQuery;
//import org.hibernate.annotations.NamedQueries;
import java.io.Serializable;

@Table(name="APP_CONTAINER_MENUITEM")
@Entity
@IdClass(ModuleMenuMap.Key.class)
@NamedQueries({
	@NamedQuery(name=ModuleMenuMap.DELETE_BY_MOD, query="delete  from ModuleMenuMap m where m.urlModule = :module"),
	@NamedQuery(name=ModuleMenuMap.DELETE_BY_MODULE, query="delete  from ModuleMenuMap m where m.urlModule.id = :moduleId and m.urlMenuItem < -1"),
    @NamedQuery(name=ModuleMenuMap.DELETE_BY_MOD_AND_MID, query="delete  from ModuleMenuMap m where m.urlModule = :module and m.urlMenuItem = :menuItem"), 
    @NamedQuery(name=ModuleMenuMap.INSERT, query="insert into  ModuleMenuMap (urlMenuItem, urlModule )    select urlMenuItem.id, modulo from UrlMenuItem urlMenuItem, Modulo modulo where urlMenuItem.id = :urlMenuItemId AND  modulo = :urlModule"),
    @NamedQuery(name=ModuleMenuMap.ALLOW_ALL, query="insert into  ModuleMenuMap (urlMenuItem, urlModule )    select 0 as urlMenuItem, urlModule from Modulo urlModule where  urlModule = :urlModule")
}) 
@SqlResultSetMapping(name="deleteResult", columns = { @ColumnResult(name = "count")})
@NamedNativeQueries({
    @NamedNativeQuery(
            name    =   "deleteByModId",
            query   =   "DELETE FROM APP_CONTAINER_MENUITEM WHERE MODULE_ID = ?"
            ,resultSetMapping = "deleteResult"
   )
})
public class ModuleMenuMap  {
	 public static final String DELETE_BY_MOD = "ModuleMenuMap.deleteByMod";
	 public static final String DELETE_BY_MODULE = "ModuleMenuMap.deleteByModule";
	 public static final String DELETE_BY_MOD_AND_MID = "ModuleMenuMap.deleteByModAndMId";
	 public static final String INSERT = "ModuleMenuMap.insert";
	 public static final String ALLOW_ALL = "ModuleMenuMap.allowAll";
	 
    @Id
    @ManyToOne(fetch= FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name="MODULE_ID")
    private Modulo urlModule;

    @Id
    @Column(name = "MENU_ITEM_ID")
    private Long urlMenuItem;
       	

	public Modulo getUrlModule() {
        return urlModule;
    }

    public void setUrlModule(Modulo urlModule) {
        this.urlModule = urlModule;
    }

    public Long getUrlMenuItem() {
        return urlMenuItem;
    }

    public void setUrlMenuItem(Long urlMenuItem) {
        this.urlMenuItem = urlMenuItem;
    }

    @SuppressWarnings("serial")
    public static class Key implements Serializable {

        protected Modulo urlModule;
        protected Long urlMenuItem;

        public Key() {
        }

        public Key(Modulo urlModule, Long urlMenuItem) {
            this.urlModule = urlModule;
            this.urlMenuItem = urlMenuItem;
        }

        public Modulo getUrlModule() {
            return urlModule;
        }

        public Long getUrlMenuItem() {
            return urlMenuItem;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            
            if (urlMenuItem != null ? !urlMenuItem.equals(key.urlMenuItem) : key.urlMenuItem != null)
				return false;
			if (urlModule != null
					? !urlModule.getId().equals(
							key.urlModule != null ? key.urlModule.getId() : null)
					: key.urlModule != null)
				return false;

			return true;
            
            
         
        }

        @Override
        public int hashCode() {
        	
        	int result = urlModule != null ? urlModule.getId().hashCode() : 0;
			result = 31 * result + (urlMenuItem != null ? urlMenuItem.hashCode() : 0);
			return result;
        	
        }
    }

    @Override
    public boolean equals(Object o) {
    	if (this == o)
			return true;
		if (o == null)
			return false;
		if (!(o instanceof ModuleMenuMap))
			return false;

		ModuleMenuMap key = (ModuleMenuMap) o;

		if (urlMenuItem != null ? !urlMenuItem.equals(key.urlMenuItem) : key.urlMenuItem != null)
			return false;
		if (urlModule != null
				? !urlModule.getId().equals(
						key.urlModule != null ? key.urlModule.getId() : null)
				: key.urlModule != null)
			return false;

		return true;
    }

    @Override
    public int hashCode() {
    	
    	int result = urlModule != null ? urlModule.getId().hashCode() : 0;
		result = 31 * result + (urlMenuItem != null ? urlMenuItem.hashCode() : 0);
		return result;

    }

}
