package org.geoazul.model.mobile;

import java.util.UUID;

//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import jakarta.persistence.CollectionTable;
//import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
//import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import org.geoazul.model.app.AbstractIdentityEntity;
import org.geoazul.model.security.ClientEntity;
//import org.hibernate.annotations.BatchSize;
import com.fasterxml.jackson.databind.JsonNode;
//import jakarta.persistence.JoinColumn;

@Entity
@DiscriminatorValue("/mobile/") 
@NamedQueries({
	@NamedQuery(name = ApplicationMobileEntity.DEFAULT_MOBILE_APP, query = "SELECT a FROM ApplicationMobileEntity a WHERE a.locale = :locale and a.clientEntity.clientId = :clientId order by a.defaultApp"),
	@NamedQuery(name = ApplicationMobileEntity.APP_MOBILE_ALL, query = "SELECT a FROM ApplicationMobileEntity a"),
	@NamedQuery(name = ApplicationMobileEntity.APP_ID, query = "SELECT a FROM ApplicationMobileEntity a where a.id = :id")
})
public  class ApplicationMobileEntity extends AbstractIdentityEntity {

	private static final long serialVersionUID = 1L;
	
	public static final String DEFAULT_MOBILE_APP = "ApplicationMobileEntity.defaultMobileAPP";
	public static final String APP_MOBILE_ALL = "ApplicationMobileEntity.appMobileALL";
	public static final String APP_ID = "ApplicationMobileEntity.appUUID";
	
	public ApplicationMobileEntity() {
		super();
	}

	public ApplicationMobileEntity(ClientEntity clientEntity, String name, String title, String description,
			boolean enabled, String image, String template, String locale, Boolean defaultApp,
			Integer epsg, JsonNode strings) {
		super(clientEntity, name, title, description,  enabled, image, template, locale, defaultApp, 
				epsg, strings);
	}
		
	//private String app_name;
	//private String app_logo;
	//private String app_termslink;
	//private String app_privacylink;
	//private Integer app_debug;
	//private Integer app_status;
	//private CurrencyEntity currency_meta;
	//private List<String> banner_images = new ArrayList<String>();
	//private Boolean stripe_live_mode;
	//private String stripe_account;
	//private Integer wp_login_enabled;
	//private String wp_login_base_url;
	//private String wp_login_forgot_password_url;
	//private String wp_login_wp_api_path;
	//private Integer product_prices_include_tax;
	//private Integer disable_shipping;
	//private String theme;
	//private String locale;
	//private String paypal_locale;
	//private String paypal_email;
	//private Boolean stripe_enabled;
	//private Boolean cod_enabled;
	//private Boolean paypal_enabled;
	//private Boolean paypal_live_mode;
	//private String stripe_country_code;
	//private String theme_font;
	//private String social_links;
	//private String theme_colors;
	
	//@ElementCollection(fetch = FetchType.LAZY)
	//  @CollectionTable(name = "raw_events_custom", 
	//		  joinColumns=@JoinColumn(name="example_id"))
	//  @MapKeyColumn(name = "field_key", length = 50)
	//  @Column(name = "field_val", length = 100)
	//  @BatchSize(size = 20)
	//  private Map<String, String> customValues = new HashMap<String, String>();

}
