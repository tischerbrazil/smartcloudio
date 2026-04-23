package org.geoazul.model.app;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import org.geoazul.ecommerce.model.Item;
import org.geoazul.model.security.ClientEntity;
import org.geoazul.model.website.Modulo;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * @author Laercio Tischer
 */

@Entity
@NamedQueries({
		@NamedQuery(name = ApplicationIdentityEntity.DEFAULT_APP, query = "SELECT a FROM ApplicationIdentityEntity a WHERE a.locale = :locale and a.clientEntity.clientId = :dtype order by a.defaultApp"),
		@NamedQuery(name = ApplicationIdentityEntity.APP_ID, query = "SELECT a FROM ApplicationIdentityEntity a where a.id = :id"),
		@NamedQuery(name = ApplicationIdentityEntity.ALL_APP, 
					query = "SELECT a.name FROM " + "ApplicationIdentityEntity a WHERE a.locale = :locale" ),
		@NamedQuery(name = ApplicationIdentityEntity.DEFAULT_ALL, query = "SELECT a FROM ApplicationIdentityEntity a") })
@DiscriminatorValue("/")

public class ApplicationIdentityEntity extends AbstractIdentityEntity {

	private static final long serialVersionUID = 1L;

	public static final String ALL_APP = "ApplicationEntity.allAPP";
	public static final String DEFAULT_APP = "ApplicationIdentityEntity.defaultAPP";
	public static final String DEFAULT_ALL = "ApplicationIdentityEntity.defaultALL";
	public static final String APP_ID = "ApplicationIdentityEntity.APP_ID";
	
	public ApplicationIdentityEntity() {
		super();
	}

	public ApplicationIdentityEntity(ClientEntity clientEntity, String name, String title, String description,
			boolean enabled, String image, String template, String locale, Boolean defaultApp,
			Integer epsg, JsonNode strings) {
		super(clientEntity, name, title, description,  enabled, image, template, locale, defaultApp, 
				epsg, strings);
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "ERP_SERVICE_APPIDENTITY", joinColumns = {
			@JoinColumn(name = "APP_ID", nullable = false) }, inverseJoinColumns = {
					@JoinColumn(name = "SERVICE_ID", nullable = false) })
	private List<Item> services;

	public List<Item> getServices() {
		return services;
	}

	public void setServices(List<Item> services) {
		this.services = services;
	}

	@OneToMany(fetch = FetchType.LAZY, cascade = {
			CascadeType.ALL }, orphanRemoval = true, mappedBy = "abstractIdentityEntity")

	private List<Modulo> modulos = new ArrayList<Modulo>();

	public List<Modulo> getModulos() {
		return modulos;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}
	
	

}