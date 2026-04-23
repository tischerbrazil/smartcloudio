package org.geoazul.view.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import org.geoazul.model.app.AbstractIdentityEntity;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.cdi.viewscope.ViewScopeManager;
import org.omnifaces.util.Faces;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.faces.application.FacesMessage;
import org.omnifaces.util.Messages;

/**
 * 
 */

@Named
@ViewScoped
public class AbstractIdentityEntityBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@PreDestroy
	public void preDestroy() {
	}

	@Inject
	EntityManager entityManager;

	private int page;
	private long count;
	private List<AbstractIdentityEntity> pageItems;

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
			Root<AbstractIdentityEntity> root = countCriteria.from(AbstractIdentityEntity.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();

			CriteriaQuery<AbstractIdentityEntity> criteria = builder.createQuery(AbstractIdentityEntity.class);
			root = criteria.from(AbstractIdentityEntity.class);
			TypedQuery<AbstractIdentityEntity> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicates(root, entityManager)));
			query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());


			this.pageItems = query.getResultList();

		} catch (Exception e) {

		}
	}

	private Predicate[] getSearchPredicates(Root<AbstractIdentityEntity> root, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		// String name = this.example.getName();
		// if (name != null && !"".equals(name)) {
		// predicatesList.add(builder.like(
		// builder.lower(root.<String> get("name")),
		// '%' + name.toLowerCase() + '%'));
		// }

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<AbstractIdentityEntity> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

}