package org.geoazul.ecommerce.view.admin;


import jakarta.transaction.Transactional;
import com.erp.modules.inventory.entities.Product;

import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.omnifaces.cdi.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */

@Named
@ViewScoped
public class ItemBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PreDestroy
	public void preDestroy() {
	}

	@Inject
	private EntityManager entityManager;

	private Integer id;
	private Product item;

	private int page;

	private long count;

	private List<Product> pageProducts;

	private Product example = new Product();

	private Product add = new Product();

	public Integer getId() {
		return this.id;
	}

	/*
	 * Support updating and deleting Product entities
	 */

	public void setId(Integer id) {
		this.id = id;
	}

	public Product getProduct() {
		return this.item;
	}

	/*
	 * Support searching Product entities with pagination
	 */

	public void setProduct(Product item) {
		this.item = item;
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.id == null) {
			this.item = this.example;
		} else {
			this.item = findById(getId());
		}
	}

	public Product findById(Integer itemId) {
		try {
			return entityManager.find(Product.class, itemId);
		} catch (Exception ex) {
			return null;
		}
	}

	public String update33() {

		return "search?faces-redirect=true";
	}

	@Transactional
	public String update() {
		try {
			if (this.id == null) {
			
				

				entityManager.persist(this.item);
			} else {
				
				
	
		
				entityManager.merge(this.item);
			}
			entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "ERRO INESPERADO!"));
			return "search?faces-redirect=true";
		}
	}

	public String delete() {
		try {
			Product deletableEntity = findById(getId());
			entityManager.remove(deletableEntity);
			entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "ERRO INESPERADO!"));
			return null;
		}
	}

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Product getExample() {
		return this.example;
	}

	public void setExample(Product example) {
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
			Root<Product> root = countCriteria.from(Product.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();
			CriteriaQuery<Product> criteria = builder.createQuery(Product.class);
			root = criteria.from(Product.class);
			TypedQuery<Product> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicates(root, entityManager)));
			query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
			this.pageProducts = query.getResultList();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "ERRO INESPERADO!"));
		}
	}

	private Predicate[] getSearchPredicates(Root<Product> root, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<>();

		String name = this.example.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
		}
		String description = this.example.getDescription();
		if (description != null && !"".equals(description)) {
			predicatesList.add(builder.like(builder.lower(root.<String>get("description")),
					'%' + description.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	/*
	 * Support listing and POSTing back Product entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Product> getPageProducts() {
		return this.pageProducts;
	}

	public long getCount() {
		return this.count;
	}

	public List<Product> getAll() {
		try {
			CriteriaQuery<Product> criteria = entityManager.getCriteriaBuilder().createQuery(Product.class);
			return entityManager.createQuery(criteria.select(criteria.from(Product.class))).getResultList();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "ERRO INESPERADO!"));
			return null;
		}
	}

	public Product getAdd() {
		return this.add;
	}

	public Product getAdded() {
		Product added = this.add;
		this.add = new Product();
		return added;
	}
}
