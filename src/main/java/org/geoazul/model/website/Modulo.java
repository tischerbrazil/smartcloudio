package org.geoazul.model.website;

import java.util.ArrayList;
import java.util.List;
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
import org.geoazul.model.app.AbstractIdentityEntity;
import org.geoazul.model.app.ApplicationIdentityEntity;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import com.erp.modules.commonClasses.BaseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import br.bancodobrasil.model.JsonJsonNode;

@Entity
@Table(name = "APP_CONTAINER")
//@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({
	    @NamedQuery(name = Modulo.ALL_MODULES, query = "select modu from Modulo modu"),
		@NamedQuery(name = Modulo.MENU_ID2, query = "select modu from Modulo modu where modu.include = :includepos"),
		@NamedQuery(name = Modulo.MENU_ID6, query = "select distinct "
				+ "urlModule.id as col0," 
				+ "app.dtype as col1," 
				+ "urlModule.path as col2,"
				+ "urlModule.include as col3," 
			    + "com.strings  as col4,"
				+ "map33.urlMenuItem as col6," 
				+ "com.comp	as col7,"
				+ "urlModule.ordering as col8,"
				+ "com.id as col9,"
				+ "com.ordering as col10,"
				+ "com.abstractWidget.arquivo as col11,"
				+ "com.blockCode as col12 "
				+ "from Modulo urlModule  "
				+ "LEFT JOIN ModuleMenuMap map33  " + "ON urlModule = map33.urlModule "
				+ "LEFT JOIN ModuleComponentMap com  " + "ON urlModule = com.module "
				+ "AND urlModule.abstractIdentityEntity = :abstractIdentityEntity "
				+ "JOIN AbstractIdentityEntity AS app " + "ON app = urlModule.abstractIdentityEntity "
				+ "AND app = :abstractIdentityEntity "
				+ "WHERE urlModule.enabled = true "
				+ "AND com.enabled = true "
				+ "AND app.clientEntity.id = :clientid  "
				+ "AND (map33.urlMenuItem = :gcmid OR map33.urlMenuItem <= 0) "
				+ " ORDER BY  urlModule.ordering, com.ordering "),
		
		
		@NamedQuery(name = Modulo.MENU_ID7, query = "select distinct "
				+ "urlModule " 
				
				+ "from Modulo urlModule  "
				+ "LEFT JOIN ModuleMenuMap map33  " + "ON urlModule = map33.urlModule "

				+ "WHERE map33.urlMenuItem = :gcmid "
				),
		
		
		@NamedQuery(name = Modulo.MODULE_ID, query = "select modu from Modulo modu where modu.id = :id"),
		@NamedQuery(name = Modulo.MODULE_BY_APP_ID, query = "select modu from Modulo modu where modu.abstractIdentityEntity.id = :id") })
	
	public class Modulo extends BaseEntity  {

	private static final long serialVersionUID = 1L;
	public static final String MENU_ID6 = "Modulo.menuId6";
	public static final String MENU_ID7 = "Modulo.menuId7";
	public static final String ALL_MODULES = "Modulo.ALL_MODULES";
	public static final String MENU_ID2 = "Modulo.menuId2";
	public static final String MODULE_ID = "Modulo.modId";
	public static final String MODULE_BY_APP_ID = "Modulo.modByAppId";

	@Column(name = "name", nullable = false, length = 100)
	private String name;

	@Column(name = "introdution")
	private String introdution;

	@Column(name = "ordering", nullable = false)
	private int ordering;

	@Column(name = "include", nullable = false, length = 50)
	private String include;

	@Column(name = "language_id", nullable = false, length = 7)
	private String languageId;

	@ColumnDefault("false")
	@Column(name = "enabled")
	private Boolean enabled;

	@Type(JsonJsonNode.class)
	@Column(name = "strings")
	@ColumnDefault("'{}'") 
	private JsonNode strings;

	@Column(name = "path")
	private String path;
	
	

	@Column(name = "access")
	private int access;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "urlModule")
	private List<ModuleMenuMap> menuItens = new ArrayList<ModuleMenuMap>();

	@Column(name = "show")
	private int show;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL,  mappedBy = "module")
	@OrderBy("ordering ASC")
	private List<ModuleComponentMap> components = new ArrayList<ModuleComponentMap>();

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "application_id")
	private ApplicationIdentityEntity abstractIdentityEntity;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "identityentity")
	private AbstractIdentityEntity identityEntity;
	
	public Modulo(String name, String introdution, int ordering, 
			String include, String languageId, Boolean enabled, String path, 
			int access,  int show, 
			ApplicationIdentityEntity abstractIdentityEntity ,
			AbstractIdentityEntity identityEntity
				) {
		this.name = name;
		this.introdution = introdution;
		this.ordering = ordering;
		this.include = include;
		this.languageId = languageId;
		this.enabled = enabled;
		this.path = path;
		this.access = access;
		this.show = show;
		this.abstractIdentityEntity = abstractIdentityEntity;
		this.identityEntity = identityEntity;
	}

	public Modulo() {
		this.show = 0;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIntrodution() {
		return this.introdution;
	}

	public void setIntrodution(String introdution) {
		this.introdution = introdution;
	}

	public int getOrdering() {
		return this.ordering;
	}

	public void setOrdering(int ordering) {
		this.ordering = ordering;
	}

	public String getInclude() {
		return this.include;
	}

	public void setInclude(String include) {
		this.include = include;
	}

	public String getLanguageId() {
		return this.languageId;
	}

	public void setLanguageId(String languageId) {
		this.languageId = languageId;
	}

	public Boolean getEnabled() {
		return this.enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public JsonNode getStrings() {
		return this.strings;
	}

	public void setStrings(JsonNode strings) {
		this.strings = strings;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<ModuleMenuMap> getMenuItens() {
		return menuItens;
	}

	public void setMenuItens(List<ModuleMenuMap> menuItens) {
		this.menuItens = menuItens;
	}

	public int getAccess() {
		return access;
	}

	public void setAccess(int access) {
		this.access = access;
	}

	public void removeAllMenuMap() {
		this.getMenuItens().removeAll(this.menuItens);
	}

	public void addMenuItemId(Integer menuItem) {
		// ModuleMenuMap moduleMenuMap33 = new ModuleMenuMap(this, menuItem);
		// menuItens.add(moduleMenuMap33); //FIXME
	}

	public void addModuleMenuMap(ModuleMenuMap moduleMenuMap) {
		menuItens.add(moduleMenuMap);
	}

	public void removeVerticeObra(ModuleMenuMap moduleMenuMap) {
		menuItens.remove(moduleMenuMap);
	}

	public List<ModuleComponentMap> getComponents() {
		return components;
	}

	public void setComponents(List<ModuleComponentMap> components) {
		this.components = components;
	}

	public int getShow() {
		return show;
	}

	public void setShow(int show) {
		this.show = show;
	}

	public ApplicationIdentityEntity getAbstractIdentityEntity() {
		return abstractIdentityEntity;
	}

	public void setAbstractIdentityEntity(ApplicationIdentityEntity abstractIdentityEntity) {
		this.abstractIdentityEntity = abstractIdentityEntity;
	}

	public AbstractIdentityEntity getIdentityEntity() {
		return identityEntity;
	}

	public void setIdentityEntity(AbstractIdentityEntity identityEntity) {
		this.identityEntity = identityEntity;
	}

}