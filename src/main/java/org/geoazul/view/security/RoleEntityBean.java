package org.geoazul.view.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.geoazul.model.security.RoleEntity;

@Named
@ViewScoped
public class RoleEntityBean implements Serializable {

	
	@PreDestroy
	public void preDestroy() {
	}

	@Inject
	
	private HttpServletRequest request;
	
	@Inject
	EntityManager entityManager;

	@Inject
	@Param(pathIndex = 0)
	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	private RoleEntity roleEntity;

	public RoleEntity getRoleEntity() {
		return this.roleEntity;
	}

	public void setRoleEntity(RoleEntity roleEntity) {
		this.roleEntity = roleEntity;
	}
	
	public String create() {
		return "create?faces-redirect=true";
	}

	public void retrieve() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (this.id == null) {
			this.roleEntity = this.example;
		} else {
			findById(getId());
		}
	}

	public RoleEntity findById(Long id) {
		try {
			return  entityManager.find(RoleEntity.class, id);
		} catch (Exception ex) {
			return null;
		}
		
	}

	public RoleEntity findRoleById(String urlString) {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			Root<RoleEntity> root;
			CriteriaQuery<RoleEntity> criteria = builder.createQuery(RoleEntity.class);
			root = criteria.from(RoleEntity.class);
			TypedQuery<RoleEntity> query = entityManager
					.createQuery(criteria.select(root).where(getSearchRolePredicates(root, urlString, entityManager)));
			List<RoleEntity> saidaRole = query.getResultList();
			try {
				if (saidaRole != null) {
					return saidaRole.get(0);
				} else {
					return null;
				}
			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	private Predicate[] getSearchRolePredicates(Root<RoleEntity> root, 
			String urlString, EntityManager session) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	/*
	 * Support updating and deleting RoleEntity entities
	 */

	@Transactional
	public String update() {
		try {
			if (this.id == null) {
				entityManager.persist(this.roleEntity);
			} else {
				entityManager.merge(this.roleEntity);
			}
			entityManager.flush();
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage("SUCESSO!", "Dados Alterados!"));
			return "index?faces-redirect=true";
		} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
				return "search?faces-redirect=true";
		}
	}

	/*
	 * Support searching RoleEntity entities with pagination
	 */

	private int page;
	private long count;
	private List<RoleEntity> pageItems;

	private RoleEntity example = new RoleEntity();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public RoleEntity getExample() {
		return this.example;
	}

	public void setExample(RoleEntity example) {
		this.example = example;
	}

	public String search() {
		this.page = 0;
		return null;
	}

	public void paginate() {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();

			// Populate this.count

			CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
			Root<RoleEntity> root = countCriteria.from(RoleEntity.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();

			// Populate this.pageItems

			CriteriaQuery<RoleEntity> criteria = builder.createQuery(RoleEntity.class);
			root = criteria.from(RoleEntity.class);
			TypedQuery<RoleEntity> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicates(root, entityManager)));
			query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
			this.pageItems = query.getResultList();

		} catch (Exception e) {
		
		}
	}

	private Predicate[] getSearchPredicates(Root<RoleEntity> root, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String name = this.example.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<RoleEntity> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back RoleEntity entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<RoleEntity> getAllRealmRoles() {
		try {
			 TypedQuery<RoleEntity> queryRoleEntity = 
					 entityManager.createNamedQuery(RoleEntity.REALM_ROLES, RoleEntity.class);
			 queryRoleEntity.setParameter("realm",  request.getServerName());
			 return queryRoleEntity.getResultList();
		
		} catch (Exception e) {
			return null;
		}
	}
	
	
	

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private RoleEntity add = new RoleEntity();

	public RoleEntity getAdd() {
		return this.add;
	}

	public RoleEntity getAdded() {
		RoleEntity added = this.add;
		this.add = new RoleEntity();
		return added;
	}

	

}