package org.geoazul.view.website;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import jakarta.transaction.Transactional;

import org.geoazul.ecommerce.view.shopping.ShoppingCart;
import org.geoazul.model.website.UrlPost;
import org.geoazul.model.website.UrlPostItem;
import org.hibernate.Session;
import org.omnifaces.cdi.ViewScoped;

@Named
@ViewScoped
public class UrlPostBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PreDestroy
	public void preDestroy() {
	}	 

	@Inject
	private EntityManager entityManager;

	@PostConstruct
	public void init() {
		page = 0;
	}
	
	public void toggleSrc(final ActionEvent evt) {
		page = page + 1;
	}
		
		
	public List<UrlPostItem> getGeometries(String mediaIdString, int page) {
		Integer mediaId = Integer.valueOf(mediaIdString);
		//mediaId = 21288L;
		try {
			TypedQuery<UrlPostItem> queryApp = 
					entityManager.createNamedQuery(UrlPostItem.FIND_POSTS, 
		        				UrlPostItem.class).setFirstResult(page * 5).setMaxResults(5);
		        queryApp.setParameter("layerId", mediaId);
		        return queryApp.getResultList();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}
	
	
	
	public UrlPost getUrlPost(Long postId) {
		try {
			this.urlPost = entityManager.find(UrlPost.class, postId);
			return entityManager.find(UrlPost.class, postId);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private UrlPost urlPost;

	public UrlPost getUrlPost() {
		return this.urlPost;
	}

	public void setUrlPost(UrlPost urlPost) {
		this.urlPost = urlPost;
	}

	public String create() {
		return "post_edit?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}


		if (this.id == null) {


			this.urlPost = this.example;


		} else {
			this.urlPost = findById(getId());
		}
	}
	
	public void editAction(Long id) {
		urlPost = findById(id);
	}

	public UrlPost findById(String id) {
		try {
			
			TypedQuery<UrlPost> UrlPostQ = entityManager
					.createNamedQuery(UrlPost.COMP_ID, UrlPost.class);
			UrlPostQ.setParameter("id", id);
			this.urlPost = UrlPostQ.getSingleResult();
			return this.urlPost;
		} catch (Exception ex) {
			return null;
		}

	}

	public UrlPost findById(Long id) {
		try {
			return entityManager.find(UrlPost.class, id);
		} catch (Exception ex) {
			return null;
		}
	}

	/*
	 * Support updating and deleting UrlPost entities
	 */

	@Transactional
	public String update() {
		try {
			if (this.id == null) {
				entityManager.persist(this.urlPost);
			} else {
				entityManager.merge(this.urlPost);
			}
			entityManager.flush();
			return "post_search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return "search?faces-redirect=true";
		}
	}

	@Transactional
	public String delete() {
		try {
			UrlPost deletableEntity = findById(getId());
			entityManager.remove(deletableEntity);
			entityManager.flush();
			return "post_search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return "post_search?faces-redirect=true";
		}
	}

	/*
	 * Support searching UrlPost entities with pagination
	 */

	private int page;
	private long count;
	private List<UrlPost> pageItems;

	private UrlPost example = new UrlPost();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public UrlPost getExample() {
		return this.example;
	}

	public void setExample(UrlPost example) {
		this.example = example;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
			Root<UrlPost> root = countCriteria.from(UrlPost.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();
			CriteriaQuery<UrlPost> criteria = builder.createQuery(UrlPost.class);
			root = criteria.from(UrlPost.class);
			TypedQuery<UrlPost> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicates(root, entityManager)));
			query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
			this.pageItems = query.getResultList();
		} catch (Exception e) {
		}
	}

	private Predicate[] getSearchPredicates(Root<UrlPost> root, EntityManager session) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		String name = this.example.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
		}
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<UrlPost> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back UrlPost entities (e.g. from inside an
	 * HtmlSelectOnePost)
	 */

	public List<UrlPost> getAll() {
		try {
			CriteriaQuery<UrlPost> criteria = entityManager.getCriteriaBuilder().createQuery(UrlPost.class);
			return entityManager.createQuery(criteria.select(criteria.from(UrlPost.class))).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private UrlPost add = new UrlPost();

	public UrlPost getAdd() {
		return this.add;
	}

	public UrlPost getAdded() {
		UrlPost added = this.add;
		this.add = new UrlPost();
		return added;
	}

}
