package org.geoazul.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.example.kickoff.business.exception.EntityNotFoundException;
import org.geoazul.ecommerce.model.Item;
import org.geoazul.model.basic.AbstractGeometry;
import org.geoazul.model.basic.Comp;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.basic.LayerCategory;
import org.geoazul.model.basic.Polygon;
import org.geoazul.model.basic.Comp.IconcategoryId;
import org.geoazul.model.basic.properties.Field;
import org.geoazul.model.website.UrlMenu;
import org.geoazul.model.website.UrlPost;
import org.geoazul.model.website.UrlPostItem;
import org.geoazul.model.website.media.Media;
import org.hibernate.Session;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.utils.reflect.Getter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.primefaces.refact.BookProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jsonb.JacksonUtil;


@Named
@ViewScoped
public class Viewer implements Serializable {

	
	@Inject 
	EntityManager entityManager;
	


	@Inject
	@Param(pathIndex = 0)
	private Long id;

	@PostConstruct
    public void init() {
		
	
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.id == null) {
			this.media = null;
		}else{


			
			
			this.media =  findMediaById(this.id);
				//beanMethod();
		}

	}

	public Media findMediaById(Long id) {
		
			TypedQuery<Media> queryModule = entityManager.createNamedQuery(Media.FIND_ID, Media.class);
			queryModule.setParameter("id", id);
			return queryModule.getResultList().stream().findFirst().orElse(null);
		
	}
	
	
	private Media media;
	
	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}



}
