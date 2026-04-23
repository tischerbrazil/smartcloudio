package org.geoazul.view.website;

import java.io.Serializable;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import org.geoazul.model.website.media.Media;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.MatchMode;
import org.primefaces.model.filter.GlobalFilterConstraint;
import org.primefaces.refact.GodJpaLazyDataModel;

@Named
@ViewScoped
public class MediaBean   implements Serializable{

	 //private LazyDataModel model;
	 
	  private LazyDataModel<Media> model;
	 
	  private List<Media> filteredMedias;
	 
	
	

	 private String globalSearch;

	    static final String GLOBAL_FILTER_KEY = "globalFilter";
	    
	 public void processSearch() {
		 
		
		 
		GlobalFilterConstraint dd = new org.primefaces.model.filter.GlobalFilterConstraint();
	
	
		FilterMeta filter = FilterMeta.builder()
				 	.constraint(dd)
				   .field(GLOBAL_FILTER_KEY)
				   .filterBy(null)
			        .filterValue(globalSearch)
			        .matchMode(MatchMode.GLOBAL)
			        .build();
		 
		
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 
		 model = new GodJpaLazyDataModel<Media>(Media.class, () -> entityManager, filter);
	}
	 
	 
	 
	 
	 @Inject
	 EntityManager entityManager;
	 
	 
	 @PostConstruct
	    public void init() {
		 model = new GodJpaLazyDataModel<>(Media.class, () -> entityManager);
		 
	 }

	
	public List<Media> getFilteredMedias() {
		return filteredMedias;
	}
	public void setFilteredMedias(List<Media> filteredMedias) {
		this.filteredMedias = filteredMedias;
	}




	public String getGlobalSearch() {
		return globalSearch;
	}


	public void setGlobalSearch(String globalSearch) {
		this.globalSearch = globalSearch;
	}


	public LazyDataModel<Media> getModel() {
		return model;
	}


	public void setModel(LazyDataModel<Media> model) {
		this.model = model;
	}
	
	
	 


}