package org.geoazul.model.app;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import java.math.BigDecimal;
import org.geoazul.model.security.ClientEntity;
import com.fasterxml.jackson.databind.JsonNode;


@Entity
@DiscriminatorValue("/gis/") 
@NamedQueries({
@NamedQuery(name = ProjectBasicEntity.ALL, query = "SELECT a FROM ProjectBasicEntity a")
})
public class ProjectBasicEntity extends ApplicationEntity {
 	 
	public static final String ALL = "ProjectBasicEntity.defaultALL";
	
	public ProjectBasicEntity(ClientEntity clientEntity, String name, String title, String description,
			boolean enabled, String image, String template, String locale, Boolean defaultApp,
			Integer epsg, JsonNode strings,
			String centroidwkt, Integer zoom, Integer maxZoom, Integer minZoom,
			BigDecimal minres, BigDecimal maxres
			) {
		super(clientEntity, name, title, description,  enabled, image, template, locale, defaultApp, 
				epsg, strings, centroidwkt, zoom, maxZoom, minZoom, minres, maxres);
		
	}
	
	public ProjectBasicEntity() 
	{
		super();
		this.setZoom(2);
		this.setMinZoom(0);
		this.setMaxZoom(20);
		this.setCentroidwkt("-6881878.392, -1251814.652 ");
		this.setEpsg(3857);
		this.setDtype("/gis/");
		this.setMinres(new BigDecimal(0.000001));
		this.setMaxres(new BigDecimal(10000));
	}


   
}