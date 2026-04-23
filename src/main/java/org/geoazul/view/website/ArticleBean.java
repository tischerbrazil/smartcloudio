package org.geoazul.view.website;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.geoazul.ecommerce.view.shopping.Photo;
import org.geoazul.model.website.UrlPostItem;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.ResponsiveOption;
import org.primefaces.refact.ArticleJpaLazyDataModel;
import org.primefaces.util.LangUtils;


@Named
@ViewScoped
public class ArticleBean implements Serializable {

	private List<FilterMeta> filterBy;
	
	
	

	private ArticleJpaLazyDataModel<UrlPostItem> model;

	@Inject
	EntityManager entityManager;
	
	 private List<ResponsiveOption> responsiveOptions;
	 
	 private List<UrlPostItem> urlPostItems;
	
	 public List<UrlPostItem> getLayerByUuid(Long layerid) {
			
		 List<UrlPostItem> dd = findByUuid(layerid);
			
			return dd;
			
			
		}
	
	 public List<UrlPostItem> getLayerById(Long layerid) {
			
		 List<UrlPostItem> dd =findById(layerid);
			
			return dd;
			
			
		}
	 
	 
	 public List<UrlPostItem> findById(Long id) {

			try {
				Query queryModFilter = entityManager.createNamedQuery(UrlPostItem.FIND_POSTS);
				queryModFilter.setParameter("layerId", id);
				
				
		        urlPostItems = queryModFilter.getResultList();
				
				return urlPostItems;
			} catch (Exception e) {
				return null;	
			}
		}
	 
	 public List<UrlPostItem> findByUuid(Long id) {

			try {
				Query queryModFilter = entityManager.createNamedQuery(UrlPostItem.FIND_POSTS_ID);
				queryModFilter.setParameter("layerId", id);
				
				
		        urlPostItems = queryModFilter.getResultList();
				
				return urlPostItems;
			} catch (Exception e) {
				return null;	
			}
		}
	 
	 
	 
	 @PostConstruct
	    public void init() {
	    	
	    	//try{
	    		
				//catalog show
	    		//UrlPostItem prod = entityManager.find(UrlPostItem.class, 1);
	    		// List<Media> listImages = prod.getMedias();
 			 			
 			//photosBanner = new ArrayList<>();
 			//for (Media pm : listImages) {
 			//	photosBanner.add(new Photo(pm.getFilename(), pm.getFilename(),
 	   	    //             pm.getTitle(), pm.getTitle()));
 			//	
 			//
			//}
	    	//	}catch (Exception ign){
	    	//		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "ITEM INEXISTENTE!"));
	    	//	}
	    		
	    	
	    			
	    	
	    	 
	         
	         

	        responsiveOptions1 = new ArrayList<>();
	        responsiveOptions1.add(new ResponsiveOption("1024px", 5));
	        responsiveOptions1.add(new ResponsiveOption("768px", 3));
	        responsiveOptions1.add(new ResponsiveOption("560px", 1));

	        responsiveOptions2 = new ArrayList<>();
	        responsiveOptions2.add(new ResponsiveOption("1024px", 5));
	        responsiveOptions2.add(new ResponsiveOption("960px", 4));
	        responsiveOptions2.add(new ResponsiveOption("768px", 3));
	        responsiveOptions2.add(new ResponsiveOption("560px", 1));

	        responsiveOptions3 = new ArrayList<>();
	        responsiveOptions3.add(new ResponsiveOption("1500px", 5));
	        responsiveOptions3.add(new ResponsiveOption("1024px", 3));
	        responsiveOptions3.add(new ResponsiveOption("768px", 2));
	        responsiveOptions3.add(new ResponsiveOption("560px", 1));
	        
	        
	        responsiveOptions = new ArrayList<>();
	        responsiveOptions.add(new ResponsiveOption("1024px", 3, 3));
	        responsiveOptions.add(new ResponsiveOption("768px", 2, 2));
	        responsiveOptions.add(new ResponsiveOption("560px", 1, 1));
	    	
	        
	        

	  
	       
	    	        
	    }
	 
	 
	 
	

	public ArticleBean() {
		setModel(new ArticleJpaLazyDataModel<>(UrlPostItem.class, () -> entityManager));
	}

	
	

	 private int getInteger(String string) {
	        try {
	            return Integer.parseInt(string);
	        }
	        catch (NumberFormatException ex) {
	            return 0;
	        }
	    }
	 
	 private List<UrlPostItem> filteredUrlPostItems1;
	 
	 public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
	        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
	        if (LangUtils.isBlank(filterText)) {
	            return true;
	        }
	        int filterInt = getInteger(filterText);

	    
	       
	       UrlPostItem urlPostItem = (UrlPostItem) value;
	        return urlPostItem.getNome().toLowerCase().contains(filterText)
	               ;
	       
		
	                
	    }
	
	@Transactional
	public String update() {
		return "/index?gcmid=17&faces-redirect=true&checkin=" + this.checkin.getDate() + "&checkout=" + this.checkout.getDate();
	}
	//@Inject
	//private UserData userData;
	
	

	
	private Date checkin;
	private Date checkout;
	
	
	public Date getCheckin() {
		return checkin;
	}

	public void setCheckin(Date checkin) {
		this.checkin = checkin;
	}

	public Date getCheckout() {
		return checkout;
	}

	public void setCheckout(Date checkout) {
		this.checkout = checkout;
	}

	public List<UrlPostItem> getFilteredUrlPostItems1() {
		return filteredUrlPostItems1;
	}

	public void setFilteredUrlPostItems1(List<UrlPostItem> filteredUrlPostItems1) {
		this.filteredUrlPostItems1 = filteredUrlPostItems1;
	}

	public List<FilterMeta> getFilterBy() {
		return filterBy;
	}

	public void setFilterBy(List<FilterMeta> filterBy) {
		this.filterBy = filterBy;
	}
	
	

	 private List<Photo> photosBanner;
	 



	 private List<ResponsiveOption> responsiveOptions1;

	    private List<ResponsiveOption> responsiveOptions2;

	    private List<ResponsiveOption> responsiveOptions3;

	    private int activeIndex = 0;
	    
	    public void changeActiveIndex() {
	        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	        this.activeIndex = Integer.valueOf(params.get("index"));
	    }

	  public List<ResponsiveOption> getResponsiveOptions() {
        return responsiveOptions;
    }

    public void setResponsiveOptions(List<ResponsiveOption> responsiveOptions) {
        this.responsiveOptions = responsiveOptions;
    }

	    public List<ResponsiveOption> getResponsiveOptions1() {
	        return responsiveOptions1;
	    }

	    public List<ResponsiveOption> getResponsiveOptions2() {
	        return responsiveOptions2;
	    }

	    public List<ResponsiveOption> getResponsiveOptions3() {
	        return responsiveOptions3;
	    }

	    public int getActiveIndex() {
	        return activeIndex;
	    }

	    public void setActiveIndex(int activeIndex) {
	        this.activeIndex = activeIndex;
	    }

		public List<Photo> getPhotosBanner() {
			return photosBanner;
		}

		public void setPhotosBanner(List<Photo> photosBanner) {
			this.photosBanner = photosBanner;
		}


		public List<UrlPostItem> getUrlPostItems() {
			return urlPostItems;
		}


		public void setUrlPostItems(List<UrlPostItem> urlPostItems) {
			this.urlPostItems = urlPostItems;
		}

		public ArticleJpaLazyDataModel<UrlPostItem> getModel() {
			return model;
		}

		public void setModel(ArticleJpaLazyDataModel<UrlPostItem> model) {
			this.model = model;
		}

	

}