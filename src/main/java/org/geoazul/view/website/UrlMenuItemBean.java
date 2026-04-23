package org.geoazul.view.website;

import static org.omnifaces.util.Faces.redirect;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.geoazul.model.basic.Comp;
import org.geoazul.model.website.ModuleFilter;
import org.geoazul.model.website.Modulo;
import org.geoazul.model.website.UrlMenu;
import org.geoazul.model.website.UrlMenuItem;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;

@Named
@ViewScoped
public class UrlMenuItemBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PreDestroy
	public void preDestroy() {
	}	 
	
	@Inject
	@Param(pathIndex = 0)
	private Long gcmid;

	@Inject
	@Param(pathIndex = 1)
	private Long id;

	@Inject
	private EntityManager entityManager;

	public Long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	private String menu_id;

	public String getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(String menu_id) {
		this.menu_id = menu_id;
	}

	private UrlMenuItem urlMenuItem;

	public UrlMenuItem getUrlMenuItem() {
		return this.urlMenuItem;
	}

	public void setUrlMenuItem(UrlMenuItem urlMenuItem) {
		this.urlMenuItem = urlMenuItem;
	}

	public String create() {
		return "menu_item_edit?faces-redirect=true&menu_id=" + this.getMenu_id();
	}

	@PostConstruct
	public void init() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.id == null) {

			try {
				if (this.menu_id == null) {


					try {
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage("ERRO", "Menu não encontrado!"));
						FacesContext.getCurrentInstance().getExternalContext().redirect("menu_search.xhtml");
					} catch (IOException e) {

					}

				} else {


					this.urlMenuItem = this.example;


					Comp comp = findCompByUuid(this.menu_id);


					UrlMenu urlMen = (UrlMenu) comp;


					this.urlMenuItem.setUrlMenu(urlMen);


					
					
		
					
					
					
					

				}
			} catch (Exception e) {
			}

		} else {
			this.urlMenuItem = findById(getId());
			
			
			Query modules = entityManager.createNamedQuery(Modulo.MENU_ID7);
			modules.setParameter("gcmid", this.urlMenuItem.getId());
			this.modulos = modules.getResultList();
			
		}
	}
	
	private List<Modulo>  modulos;

	
	public UrlMenuItem findById(Long id) {
		try {
			return entityManager.find(UrlMenuItem.class, id);
		} catch (Exception e) {
			return null;
		}
	}

	public Comp findCompByUuid(String uuid) {
		try {
			TypedQuery<Comp> queryApp = entityManager.createNamedQuery(Comp.COMP_ID, Comp.class);
			queryApp.setParameter("id", id);
			return queryApp.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public UrlMenu findMenuById(String id) {
		try {
			return entityManager.find(UrlMenu.class, id);
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * Support updating and deleting UrlMenuItem entities
	 */

	@Transactional
	public void update() {
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		
			if (this.id == null) {
				entityManager.persist(this.urlMenuItem);
				facesContext.addMessage(null,
						new FacesMessage("SUCESSO", "Item de Menu Inserido!"));
			} else {
				entityManager.merge(this.urlMenuItem);
				facesContext.addMessage(null,
						new FacesMessage("SUCESSO", "Item de Menu Alterado!"));
			}
			entityManager.flush();
			
			
			redirect("/administrator/menu_view/0/"  + this.urlMenuItem.getUrlMenu().getId().toString());
		

			
	}

	@Transactional
	public void delete() {
	
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		
			UrlMenuItem deletableEntity = findById(getId());
		
			if (deletableEntity.getParentId() != null) {
				UrlMenuItem urlMenuItemParent = deletableEntity.getParentId();
				urlMenuItemParent.getChildrens().remove(deletableEntity);
				deletableEntity.setParentId(null);
			}
			entityManager.remove(deletableEntity);
			entityManager.flush();
				
			facesContext.addMessage(null,
					new FacesMessage("SUCESSO", "Item de Menu Removido!"));
			
	    	redirect("/administrator/menu_view/0/"  + this.urlMenuItem.getUrlMenu().getId().toString());
	    	
	}

	/*
	 * Support searching UrlMenuItem entities with pagination
	 * 
	 */

	private int page;
	private long count;
	private List<UrlMenuItem> pageItems;

	private UrlMenuItem example = new UrlMenuItem();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 50;
	}

	public UrlMenuItem getExample() {
		return this.example;
	}

	public void setExample(UrlMenuItem example) {
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
			Root<UrlMenuItem> root = countCriteria.from(UrlMenuItem.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();
			CriteriaQuery<UrlMenuItem> criteria = builder.createQuery(UrlMenuItem.class);
			root = criteria.from(UrlMenuItem.class);
			TypedQuery<UrlMenuItem> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicates(root, entityManager)));
			query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
			this.pageItems = query.getResultList();
		} catch (Exception e) {
			
		}
	}

	private Predicate[] getSearchPredicates(Root<UrlMenuItem> root, EntityManager session) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		if (this.getMenu_id() == null) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.html");
			} catch (IOException e) {
			}
		} else {
			try {
				UrlMenu urlMenuFind = findMenuById(getMenu_id());
				if (urlMenuFind != null) {
					predicatesList.add(builder.equal(root.get("urlMenu"), urlMenuFind));
				} else {
					FacesContext.getCurrentInstance().getExternalContext().redirect("index.html");
				}
			} catch (NullPointerException | IOException npe) {
			}
		}
		String name = this.example.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
		}
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<UrlMenuItem> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back UrlMenuItem entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<UrlMenuItem> getAll() {
		try {
			CriteriaQuery<UrlMenuItem> criteria = entityManager.getCriteriaBuilder().createQuery(UrlMenuItem.class);
			return entityManager.createQuery(criteria.select(criteria.from(UrlMenuItem.class))).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public List<UrlMenuItem> getAllRootItens() {
		try {
			if (this.id == null) {
				TypedQuery<UrlMenuItem> queryMenuItens = entityManager.createNamedQuery(UrlMenuItem.ITENS_ROOT,
						UrlMenuItem.class);
				queryMenuItens.setParameter("urlmenuid", this.urlMenuItem.getUrlMenu().getId());
				return queryMenuItens.getResultList();
			} else {
				TypedQuery<UrlMenuItem> queryMenuItens = entityManager.createNamedQuery(UrlMenuItem.ITENS_ROOT_EXCLUDE_ITEM,
						UrlMenuItem.class);
				queryMenuItens.setParameter("urlmenuid", this.urlMenuItem.getUrlMenu().getId());
				queryMenuItens.setParameter("urlmenuitemid", id);
				return queryMenuItens.getResultList();
			}
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private UrlMenuItem add = new UrlMenuItem();

	public UrlMenuItem getAdd() {
		return this.add;
	}

	public UrlMenuItem getAdded() {
		UrlMenuItem added = this.add;
		this.add = new UrlMenuItem();
		return added;
	}

	public List<String> completeText(String query) {
		List<String> results = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			results.add(query + i);
		}
		return results;
	}

	public List<Modulo> getModulos() {
		return modulos;
	}

	public void setModulos(List<Modulo> modulos) {
		this.modulos = modulos;
	}

	public Long getGcmid() {
		return gcmid;
	}

	public void setGcmid(Long gcmid) {
		this.gcmid = gcmid;
	}

}
