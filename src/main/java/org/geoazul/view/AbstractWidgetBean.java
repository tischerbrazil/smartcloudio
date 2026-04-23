package org.geoazul.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.context.FacesContext;

import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;
import org.geoazul.model.basic.AbstractWidget;
import org.geoazul.model.basic.AbstractWidget.CompDef;

@Named
@ViewScoped
public class AbstractWidgetBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PreDestroy
	public void preDestroy() {
	}	 

	@Inject
	EntityManager entityManager;

	/*
	 * Support creating and retrieving Bookcategory entities
	 */

	@Inject
	@Param(pathIndex = 0)
	private Long gcmid;
	
	@Inject
	@Param(pathIndex = 1)
	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getGcmid() {
		return gcmid;
	}

	public void setGcmid(Long gcmid) {
		this.gcmid = gcmid;
	}

	private AbstractWidget abstractWidget;

	public AbstractWidget getAbstractWidget() {
		return this.abstractWidget;
	}

	public void setAbstractWidget(AbstractWidget abstractWidget) {
		this.abstractWidget = abstractWidget;
	}
	
	private List<CompDef> enumCompDef;

	public AbstractWidgetBean(){
		enumCompDef = Arrays.asList(CompDef.values());
	}

	public List<CompDef> getEnumCompDef() {
	    return enumCompDef;
	}

	public String create() {
		return "create?faces-redirect=true";
	}

	@PostConstruct
	public void init() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.id == null) {
			this.abstractWidget = this.example;
		} else {
			this.abstractWidget = findById(this.id);
		}
	}

	public AbstractWidget findById(Long id) {
		try {
			return entityManager.find(AbstractWidget.class, id);
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * Support updating and deleting Bookcategory entities
	 */

	@Transactional
	public String update() {
		
		
		try {
			if (this.id == null) {
				entityManager.persist(this.abstractWidget);
				entityManager.flush();
				return "widget_search?faces-redirect=true";
			} else {
				entityManager.merge(this.abstractWidget);
				entityManager.flush();
				return "widget_search?faces-redirect=true";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	public String delete() {
		try {
			AbstractWidget deletableEntity = findById(getId());
			entityManager.remove(deletableEntity);
			entityManager.flush();
			return "widget_search?faces-redirect=true";
		} catch (Exception e) {
			return null;
		}
	}

	
	private AbstractWidget example = new AbstractWidget();

	public AbstractWidget getExample() {
		return this.example;
	}

	public void setExample(AbstractWidget example) {
		this.example = example;
	}

	/*
	 * Support listing and POSTing back Bookcategory entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<AbstractWidget> getAll() {
		try {
			CriteriaQuery<AbstractWidget> criteria = entityManager.getCriteriaBuilder().createQuery(AbstractWidget.class);
			return entityManager.createQuery(criteria.select(criteria.from(AbstractWidget.class))).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private AbstractWidget add = new AbstractWidget();

	public AbstractWidget getAdd() {
		return this.add;
	}

	public AbstractWidget getAdded() {
		AbstractWidget added = this.add;
		this.add = new AbstractWidget();
		return added;
	}

	public List<String> completeText(String query) {
		List<String> results = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			results.add(query + i);
		}
		return results;
	}

}
