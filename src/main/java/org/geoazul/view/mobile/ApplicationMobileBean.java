package org.geoazul.view.mobile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

import org.geoazul.model.basic.Layer;
import org.geoazul.model.mobile.ApplicationMobileEntity;
import org.hibernate.Hibernate;
import org.hibernate.Session;

import org.omnifaces.cdi.ViewScoped;
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
public class ApplicationMobileBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PreDestroy
	public void preDestroy() {
	}

	@Inject
	
	private HttpServletRequest request;
	
	@Inject
	EntityManager entityManager;
	
	public String create() {
		return "create?faces-redirect=true";
	}
	
	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	private ApplicationMobileEntity applicationEntity;

	public ApplicationMobileEntity getApplicationMobileEntity() {
		return this.applicationEntity;
	}

	public void setApplicationMobileEntity(ApplicationMobileEntity applicationEntity) {
		this.applicationEntity = applicationEntity;
	}
		
	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		
		if (this.id == null) {
			
			this.applicationEntity = new ApplicationMobileEntity();
			
		} else {
			
			 this.findById(id);

			
			
			
		}
	
	}


	
	public ApplicationMobileEntity findById(String id) {
		try {
			TypedQuery<ApplicationMobileEntity> shoppingCartDraft = 
					entityManager.createNamedQuery(ApplicationMobileEntity.APP_ID, ApplicationMobileEntity.class);
			shoppingCartDraft.setParameter("id", id);
			this.applicationEntity = shoppingCartDraft.getSingleResult();
								
			return this.applicationEntity;
		} catch (Exception ex) {
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
			return "application_mobile_search.xhtml?faces-redirect=true";
		} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
				return "search?faces-redirect=true";
		}
	}
	
	@Transactional
	public String delete() {
		try {
			ApplicationMobileEntity deletableEntity = 
					entityManager.find(ApplicationMobileEntity.class, this.applicationEntity.getId());
			entityManager.remove(deletableEntity);
		
			entityManager.flush();
			return "application_mobile_search.xhtml?faces-redirect=true";
		} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
				return "search?faces-redirect=true";
		}

	}
				
				
				
				//==============================================================
						
				public List<ApplicationMobileEntity> getAll() {
					try {
					CriteriaQuery<ApplicationMobileEntity> criteria =
							entityManager.getCriteriaBuilder().createQuery(ApplicationMobileEntity.class);
					return entityManager.createQuery(
							criteria.select(criteria.from(ApplicationMobileEntity.class)))
					.getResultList();
					} catch (Exception e) {
							return null;
					}
				}
								
				/*
				 * Support searching ApplicationMobileEntity entities with pagination
				 */

				private int page;
				private long count;
				private List<ApplicationMobileEntity> pageItems;
				
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
					Root<ApplicationMobileEntity> root = countCriteria.from(ApplicationMobileEntity.class);
					countCriteria = countCriteria.select(builder.count(root)).where(
							getSearchPredicates(root, entityManager));
					this.count = entityManager.createQuery(countCriteria)
							.getSingleResult();
					CriteriaQuery<ApplicationMobileEntity> criteria = builder.createQuery(ApplicationMobileEntity.class);
					root = criteria.from(ApplicationMobileEntity.class);
					TypedQuery<ApplicationMobileEntity> query = entityManager.createQuery(criteria
							.select(root).where(getSearchPredicates(root, entityManager)));
					query.setFirstResult(this.page * getPageSize()).setMaxResults(
							getPageSize());
					this.pageItems = query.getResultList();
							for (ApplicationMobileEntity appLoad : this.pageItems) {
								if (!Hibernate.isInitialized(appLoad.getClientEntity())) {
									Hibernate.initialize(appLoad.getClientEntity());
								}
							}
					} catch (Exception e) {
						
					}
				}

				private Predicate[] getSearchPredicates(Root<ApplicationMobileEntity> root,
						EntityManager session) {

					CriteriaBuilder builder = session.getCriteriaBuilder();
					List<Predicate> predicatesList = new ArrayList<Predicate>();

					return predicatesList.toArray(new Predicate[predicatesList.size()]);
				}

				public List<ApplicationMobileEntity> getPageItems() {
					return this.pageItems;
				}

				public long getCount() {
					return this.count;
				}
				    
				 
				 
				 
				 
				 
}