package org.geoazul.view.website;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.geoazul.model.app.ApplicationIdentityEntity;
import org.geoazul.model.basic.AbstractGeometry;
import org.geoazul.model.basic.Comp;
import org.geoazul.model.basic.properties.Field;
import org.geoazul.model.security.RealmEntity;
import org.geoazul.model.website.ModuleComponentMap;
import org.geoazul.model.website.ModuleMenuMap;
import org.geoazul.model.website.Modulo;
import org.geoazul.model.website.UrlMenu;
import org.geoazul.model.website.UrlMenuItem;
import org.geoazul.model.website.media.Media;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.model.LazyDataModel;
import org.primefaces.refact.ApplicationJpaLazyDataModel;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * 
 */

@Named
@ViewScoped
public class ApplicationIdentityBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PreDestroy
	public void preDestroy() {
	}
	
	private LazyDataModel<ApplicationIdentityEntity> model;
	
	@PostConstruct
    public void init() {
	 model = new ApplicationJpaLazyDataModel<>(ApplicationIdentityEntity.class, () -> entityManager);
	 
 }


	@Inject
	EntityManager entityManager;
	
	public String create() {
		return "create?faces-redirect=true";
	}
	
	@Inject
	@Param(pathIndex = 0)
	private Long cgmid;

	@Inject
	@Param(pathIndex = 1)
	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getCgmid() {
		return cgmid;
	}

	public void setCgmid(Long cgmid) {
		this.cgmid = cgmid;
	}

	private ApplicationIdentityEntity applicationEntity;

	public ApplicationIdentityEntity getApplicationIdentityEntity() {
		return this.applicationEntity;
	}

	public void setApplicationIdentityEntity(ApplicationIdentityEntity applicationEntity) {
		this.applicationEntity = applicationEntity;
	}
		
	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		
		if (this.id == null) {
			this.applicationEntity = new ApplicationIdentityEntity();
			
		} else {
			this.applicationEntity =  this.findById(id);

			
			
			
		}
	
	}


	
	public ApplicationIdentityEntity findById(Long id) {
		try {
			TypedQuery<ApplicationIdentityEntity> queryApp = entityManager.createNamedQuery(ApplicationIdentityEntity.APP_ID, 
					ApplicationIdentityEntity.class);
			queryApp.setParameter("id",id);
			
			ApplicationIdentityEntity aaa = queryApp.getSingleResult();
			return queryApp.getSingleResult();
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	/*
	 * Support updating and deleting ApplicationEntity entities
	 */

	@Transactional
	public String update() {
		try {
			if (this.id == null) {
				entityManager.persist(this.applicationEntity);
			} else {
				entityManager.merge(this.applicationEntity);
			}
			entityManager.flush();
			return "application_search.xhtml?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
				return "application_search.xhtml?faces-redirect=true";
		}
	}
	
	@Transactional
	public String delete() {
		try {
		
			
			
			ApplicationIdentityEntity deletableEntity = 
					entityManager.find(ApplicationIdentityEntity.class, this.applicationEntity.getId());
			
			for ( Modulo moduleExclude : deletableEntity.getModulos()) {
				
				
				
				
				
				//-------------------------
				
				
				for (  ModuleComponentMap moduleComponentMap : moduleExclude.getComponents()) {
					
					
					Query query1 = entityManager.createNamedQuery(ModuleMenuMap.DELETE_BY_MOD);
					query1.setParameter("module", moduleComponentMap.getModule());
					query1.executeUpdate();
					entityManager.flush();
					
					
					Query query = entityManager.createNamedQuery(ModuleComponentMap.DELETE);
					query.setParameter("moduleComponentMap", moduleComponentMap);
					query.executeUpdate();
					entityManager.flush();
					
					
					
					
					
				}
				
				entityManager.flush();
				
				
				//---------------------------
				
				
				
				
				
				
				
				
				
				
				entityManager.remove(moduleExclude);
				
			
				
			}
			
			entityManager.flush();
			
			
			
			//--------------comp
			
			//-------------------------
			
			
			for (   Comp component : deletableEntity.getComponents()) {
				
				if (component instanceof UrlMenu){
					
					UrlMenu teste = (UrlMenu) component;
					for (  UrlMenuItem item : teste.getMenuItens()) {
						entityManager.remove(item);
					}
					entityManager.flush();
				}
				
				
				
				//--------------------GEOMETRY
				for (    AbstractGeometry geom : component.getGeometrias()) {
					
					entityManager.remove(geom);
				}
				entityManager.flush();
				
				for (     Field field : component.getFields()) {
					
					entityManager.remove(field);
				}
				entityManager.flush();
				
					//--------------------GEOMETRY
				
				
				
				
				entityManager.remove(component);
							

				
			}
			
			entityManager.flush();
			
			
			//---------------------------
			
			//--------------comp
			
			
			
		
			
			
			
			
			
			entityManager.remove(deletableEntity);
		
			entityManager.flush();
			return "application_search.xhtml?faces-redirect=true";
		} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
				return "search?faces-redirect=true";
		}

	}
				
				
				
				//==============================================================
						
				public List<ApplicationIdentityEntity> getAll() {
					try {
					CriteriaQuery<ApplicationIdentityEntity> criteria =
							entityManager.getCriteriaBuilder().createQuery(ApplicationIdentityEntity.class);
					return entityManager.createQuery(
							criteria.select(criteria.from(ApplicationIdentityEntity.class)))
					.getResultList();
					} catch (Exception e) {
							return null;
					}
				}
				
		
				
				/*
				 * Support searching ApplicationIdentityEntity entities with pagination
				 */

				private int page;
				private long count;
				private List<ApplicationIdentityEntity> pageItems;
				
				public int getPage() {
					return this.page;
				}

				public void setPage(int page) {
					this.page = page;
				}

				public int getPageSize() {
					return 50;
				}

				
				public String search() {
					this.page = 0;
					return null;
				}

				public void paginate() {
					
					
					try {
					CriteriaBuilder builder = entityManager.getCriteriaBuilder();
					CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
					Root<ApplicationIdentityEntity> root = countCriteria.from(ApplicationIdentityEntity.class);
					countCriteria = countCriteria.select(builder.count(root)).where(
							getSearchPredicates(root, entityManager));
					this.count = entityManager.createQuery(countCriteria)
							.getSingleResult();
					CriteriaQuery<ApplicationIdentityEntity> criteria = builder.createQuery(ApplicationIdentityEntity.class);
					root = criteria.from(ApplicationIdentityEntity.class);
					TypedQuery<ApplicationIdentityEntity> query = entityManager.createQuery(criteria
							.select(root).where(getSearchPredicates(root, entityManager)));
					query.setFirstResult(this.page * getPageSize()).setMaxResults(
							getPageSize());
					this.pageItems = query.getResultList();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				private Predicate[] getSearchPredicates(Root<ApplicationIdentityEntity> root,
						EntityManager session) {

					CriteriaBuilder builder = session.getCriteriaBuilder();
					List<Predicate> predicatesList = new ArrayList<Predicate>();

					return predicatesList.toArray(new Predicate[predicatesList.size()]);
				}

				public List<ApplicationIdentityEntity> getPageItems() {
					return this.pageItems;
				}

				public long getCount() {
					return this.count;
				}

				public LazyDataModel<ApplicationIdentityEntity> getModel() {
					return model;
				}

				public void setModel(LazyDataModel<ApplicationIdentityEntity> model) {
					this.model = model;
				}
				    
				 
				 
				 
				 
				 
}