package org.geoazul.view.website;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.omnifaces.cdi.ViewScoped;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.geoazul.model.basic.Comp;
import org.geoazul.model.website.UrlMenu;

@Named
@ViewScoped
public class CompBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@PreDestroy
	public void preDestroy() {
	}
	
	@Inject
	private EntityManager entityManager;
	
	private int page;
	private long count;
	private List<Comp> pageItems;

	private Comp example = new UrlMenu();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 50;
	}

	public Comp getExample() {
		return this.example;
	}

	public void setExample(Comp example) {
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
		Root<Comp> root = countCriteria.from(Comp.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root, entityManager));
		this.count = entityManager.createQuery(countCriteria)
				.getSingleResult();
		CriteriaQuery<Comp> criteria = builder.createQuery(Comp.class);
		root = criteria.from(Comp.class);
		TypedQuery<Comp> query = entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root, entityManager)));
		query.setFirstResult(this.page * getPageSize()).setMaxResults(
				getPageSize());
		this.pageItems = query.getResultList();
		} catch (Exception e) {
			
		}
	}

	private Predicate[] getSearchPredicates(Root<Comp> root, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		  
		

		String name = this.example.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(
					builder.lower(root.<String> get("name")),
					'%' + name.toLowerCase() + '%'));
		}
	
		
		

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Comp> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Component entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Comp> getAll() {
		try {
			String comps = "select comp from Comp comp";
	    	 TypedQuery<Comp> findAllQuery = entityManager.createQuery(comps, Comp.class);
	    	  List<Comp> hhh = findAllQuery.getResultList();
	    	return hhh;
	} catch (Exception e) {
			return null;
	}
	}
	
	


		
	
}
