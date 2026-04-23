package org.geoazul.model.website;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import com.erp.modules.commonClasses.BaseEntity;

/**
 *  
 */

@Entity
@Table(name = "app_menu_item", uniqueConstraints = @UniqueConstraint(columnNames = { "id", "parent_id" }))
@NamedQueries({
		
		@NamedQuery(name = UrlMenuItem.ITENS_ROOT_EXCLUDE_ITEM, query = "select urlMenuItem from UrlMenuItem urlMenuItem"
				+ " where urlMenuItem.urlMenu.id = :urlmenuid" + " and urlMenuItem.parentId is null "
				+ " and urlMenuItem.id <> :urlmenuitemid "),
		
		@NamedQuery(name = UrlMenuItem.ITENS_ROOT, query = "select urlMenuItem from UrlMenuItem urlMenuItem"
				+ " where urlMenuItem.urlMenu.id = :urlmenuid" + " and urlMenuItem.parentId is null "),
		
		@NamedQuery(name = UrlMenuItem.FIND_ID, query = "select urlMenuItem from UrlMenuItem urlMenuItem where urlMenuItem.id = :id"),

		@NamedQuery(name = UrlMenuItem.FIND_APP_MENU_ROOT, query = "SELECT m FROM UrlMenuItem m WHERE m.parentId IS NULL and m.urlMenu = :urlMenu "
				+ "ORDER BY m.ordering "),
		
		@NamedQuery(name = UrlMenuItem.FIND_APP_MENU_PARENT_ID, query = "SELECT m FROM UrlMenuItem m WHERE m.parentId.id = :parentId "
				+ "ORDER BY m.ordering "),

		@NamedQuery(name = UrlMenuItem.FIND_APP_MENUS, query = "SELECT DISTINCT urlMenuItem.urlMenu FROM UrlMenuItem urlMenuItem "
				+ "LEFT JOIN UrlMenu urlMenu  ON urlMenu = urlMenuItem.urlMenu "
				+ "WHERE urlMenu.applicationEntity = :abstractIdentityEntity"),

		@NamedQuery(name = UrlMenuItem.FIND_APP_MENUS_SIZE, query = "SELECT DISTINCT COUNT(urlMenuItem.urlMenu) FROM UrlMenuItem urlMenuItem "
				+ "LEFT JOIN UrlMenu urlMenu  " + "ON urlMenu = urlMenuItem.urlMenu "
				+ "WHERE urlMenu.applicationEntity = :abstractIdentityEntity"),
		
		
		 @NamedQuery(
		    		name= UrlMenuItem.FIND_ALL_ON_MENU,
		    		query="select urlMenuItem from UrlMenuItem urlMenuItem where urlMenuItem.urlMenu.id = :urlMenuId AND urlMenuItem.enabled = true ORDER BY urlMenuItem.ordering"),
		
		 
		 @NamedQuery(
		    		name= UrlMenuItem.FIND_ALL_ON_MENU_ROOT,
		    		query="select urlMenuItem from UrlMenuItem urlMenuItem where "
		    				+ "urlMenuItem.urlMenu.id = :urlMenuId "
		    				+ "AND urlMenuItem.enabled = true "
		    				+ "AND urlMenuItem.parentId is null "
		    				+ "ORDER BY urlMenuItem.ordering"),
	
		 
		 
		@NamedQuery(name = UrlMenuItem.FIND_APP_MENU_ROOT_ID, query = "SELECT DISTINCT urlMenuItem.id FROM UrlMenuItem urlMenuItem "
				+ "LEFT JOIN UrlMenu urlMenu  " + "ON urlMenu = urlMenuItem.urlMenu "
				+ "WHERE urlMenu.applicationEntity = :abstractIdentityEntity and urlMenuItem.ordering = 0"),
		
	})

public class UrlMenuItem  extends BaseEntity  {

	private static final long serialVersionUID = 1L;

	public static final String FIND_APP_MENU_ROOT_ID= "UrlMenuItem.findMenuRootId";
	
	public static final String FIND_ALL_ON_MENU = "UrlMenuItem.findAllOnMenu";
	
	public static final String FIND_ALL_ON_MENU_ROOT = "UrlMenuItem.findAllOnMenuRoot";
	
	public static final String FIND_APP_MENU_ROOT = "UrlMenuItem.findMenuRoot";
	public static final String FIND_APP_MENU_PARENT_ID = "UrlMenuItem.findParentId";

	public static final String FIND_APP_MENUS = "UrlMenuItem.findMenus";
	public static final String FIND_APP_MENUS_SIZE = "UrlMenuItem.findMenusSize";

	public static final String ITENS_ROOT_EXCLUDE_ITEM = "UrlMenuItem.itensRootExcludeItem";
	public static final String ITENS_ROOT = "UrlMenuItem.itensRoot";
	public static final String FIND_ID = "UrlMenuItem.id";
	
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MENU_ID")
	private UrlMenu urlMenu;

	@Basic(optional = false) //FIXME
	@Column(name = "name", nullable = false, length = 255)
	@NotNull
	private String name;

	@Column(name = "path", length = 1024)
	private String path;

	@Column(name = "link", length = 1024)
	private String link;

	@Column(name = "icon", length = 50)
	private String icon;
	
	@Column(name = "LINK_HREF", length = 50)
	private String link_href;
	
	@Column(name = "SYMBOL_SVG", length = 255)
	private String symbol_svg;

	@Column(name = "type", nullable = false, length = 16)
	@NotNull
	private String type;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id", referencedColumnName = "id", updatable = true, nullable = true)
	private UrlMenuItem parentId;

	@Column(name = "language_id", length = 5)
	private String languageId;

	@Column(name = "ordering")
	private int ordering;

	@OneToMany(mappedBy = "parentId", fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
	@OrderBy("ordering ASC")
	private List<UrlMenuItem> childrens = new ArrayList<UrlMenuItem>();

	@Column(name = "enabled")
	private boolean enabled;
		
    //Generators.timeBasedGenerator().generate()
	
	
	public UrlMenuItem() {
	
	}

	public UrlMenuItem(UrlMenu urlMenu, String name, String path,
			String link, String icon, String type,	UrlMenuItem parentId, 
			String languageId, int ordering,boolean enabled) {
		this.urlMenu = urlMenu;
		this.name = name;
		this.path = path;
		this.link = link;
		this.icon = icon;
		this.type = type;
		this.parentId = parentId;
		this.languageId = languageId;
		this.ordering = ordering;
		this.enabled = enabled;
	}
	
	public UrlMenu getUrlMenu() {
		return urlMenu;
	}

	public void setUrlMenu(UrlMenu urlMenu) {
		this.urlMenu = urlMenu;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLink() {
		return this.link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public UrlMenuItem getParentId() {
		return this.parentId;
	}

	public void setParentId(UrlMenuItem parentId) {
		this.parentId = parentId;
	}

	public String getLanguageId() {
		return this.languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public List<UrlMenuItem> getChildrens() {
		return childrens;
	}

	public void setChildrens(List<UrlMenuItem> childrens) {
		this.childrens = childrens;
	}

	public int getOrdering() {
		return ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}




	public String getLink_href() {
		return link_href;
	}




	public void setLink_href(String link_href) {
		this.link_href = link_href;
	}




	public String getSymbol_svg() {
		return symbol_svg;
	}




	public void setSymbol_svg(String symbol_svg) {
		this.symbol_svg = symbol_svg;
	}



}
