package org.geoazul.view.security;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
import org.example.kickoff.model.Person;
import org.geoazul.model.security.ClientEntity;
import org.hibernate.Session;
import org.keycloak.example.oauth.UserData;

@Named
@ViewScoped
public class CustomerBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PreDestroy
	public void preDestroy() {
	}
	
	@Inject
	private UserData userData;

	@Inject
	EntityManager entityManager;

	private String attribValue;;

	public String getAttribValue() {
		return attribValue;
	}

	public void setAttribValue(String attribValue) {
		this.attribValue = attribValue;
	}
	
	
	
	
	
	
	
	
	//------------------------
	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	


	private Person person;

	public Person getPerson() {
		return this.person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
	

	public void retrieve() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (this.id == null) {
			
		} else {
			this.person = findById(getId());
		}
	}

	


	public Person findById(String uuid) {
		try {
			
			TypedQuery<Person> personU = 
					entityManager.createNamedQuery(Person.USER_UUID_GET, Person.class);
			personU.setParameter("uuid", UUID.fromString(uuid));
			return personU.getSingleResult();
		
		} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
				return null;
		}
	}
	
	

	/*
	 * Support searching Person entities with pagination
	 */

	private int page;
	private long count;
	private List<Person> pageItems;


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
			Root<Person> root = countCriteria.from(Person.class);
			countCriteria = countCriteria.select(builder.count(root)).
					where(getSearchPredicates(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();
			CriteriaQuery<Person> criteria = builder.createQuery(Person.class);
			root = criteria.from(Person.class);
			TypedQuery<Person> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicates(root, entityManager)));
			query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
			this.pageItems = query.getResultList();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

	private Predicate[] getSearchPredicates(Root<Person> root, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		predicatesList.add(builder.isTrue(root.get("enabled")));
		predicatesList.add(builder.isTrue(root.get("customer")));
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Person> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Person entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Person> getAll() {

		try {
			CriteriaQuery<Person> criteria = entityManager.getCriteriaBuilder().createQuery(Person.class);
			return entityManager.createQuery(criteria.select(criteria.from(Person.class))).getResultList();

		} catch (Exception e) {
			return null;
		}
	}
	
	

	

}
