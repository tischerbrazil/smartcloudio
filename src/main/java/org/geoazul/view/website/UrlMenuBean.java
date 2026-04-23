package org.geoazul.view.website;

import static org.omnifaces.util.Faces.redirect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.geoazul.model.app.ApplicationIdentityEntity;
import org.geoazul.model.website.UrlMenu;
import org.geoazul.model.website.UrlMenuItem;
import org.hibernate.Hibernate;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@Named
@ViewScoped
public class UrlMenuBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@PreDestroy
	public void preDestroy() {
	}

	@Inject
	private EntityManager entityManager;

	@Inject
	private HttpServletRequest request;
	
	@Inject
	@Param(pathIndex = 0)
	private Long gcmid;

	@Inject
	@Param(pathIndex = 1)
	private Long id;

	public Long getGcmid() {
		return gcmid;
	}

	public void setGcmid(Long gcmid) {
		this.gcmid = gcmid;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*
	 * Support creating and retrieving UrlMenu entities
	 */

	// #{urlMenuBean.getMenuItems6(true, id)}
	
	  public void onRowReorder(ReorderEvent event) {
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Row Moved", "From: " + event.getFromIndex() + ", To:" + event.getToIndex());
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	    }
	
	 private List<UrlMenuItem> selectedMenuId;
	 
	
	 public void onRowSelect(SelectEvent<UrlMenuItem> event) {
	        FacesMessage msg = new FacesMessage("Product Selected", String.valueOf(event.getObject().getId()));
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	    }

	public List<UrlMenuItem> getMenuProfile(Long menuId) {
		try {
			
			
			UrlMenu urlMenuProfile = findById(menuId);
			return  urlMenuProfile.getMenuItens();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		

	
	}
	
	public List<UrlMenuItem> getMenu(Long menuId) {
		try {
			//FIND_ALL_ON_MENU
			TypedQuery<UrlMenuItem> queryApp = entityManager.createNamedQuery(UrlMenuItem.FIND_ALL_ON_MENU,
					UrlMenuItem.class);
			queryApp.setParameter("urlMenuId", menuId);
			return  queryApp.getResultList();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public String getMenuItems5(boolean showicons, Long menuid) {
		try {

			String class_li_0 = " class=\"nav-item\"";
			String class_li_1 = " class=\"nav-item dropdown dropdown-animate\"  data-toggle=\"hover\"    ";
			String class_li_2 = "";

			String class_href_0 = "class=\"nav-link\""; // " class=\"btn btn-lg waves-effect\" ";
			String class_href_1 = " class=\"nav-link dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\"  aria-haspopup=\"true\" aria-expanded=\"false\" ";
			String class_href_2 = "class=\"dropdown-item\"";

			String class_li_end_0 = "</li>";
			String class_li_end_1 = "<ul aria-labelledby=\"dropdown01\" class=\"dropdown-menu dropdown-menu-single\">"; 
																														
			String class_li_end_2 = "</li>";
			String class_li_end_3 = "</ul></li>";

			String classLiString;
			String classHrefString;
			String classLiStringEnd;

			String menuString = "";

			Query q = entityManager.createNativeQuery("select * from app_menu_item_view where menu_id = " + menuid);

			List<Object[]> menuItens = q.getResultList();

			for (Object[] menuItem : menuItens) {
				
				Optional<Object> existPATH = Optional.ofNullable(menuItem[9]);
							
				Long menu_item_id =  (Long) menuItem[0];
				
				int startline = (int) menuItem[4];
				
				int endline = (int) menuItem[8];
			
				
				String pathStr = "";
			
				if (existPATH.isPresent()){
					pathStr = existPATH.get().toString();
				}

				
				

				if (startline == 0) {
					classLiString = "<li" + class_li_0 + ">";
					classHrefString = "<a   href=\"" + request.getContextPath() + "/" + menuItem[6] 
							+ menu_item_id + pathStr + "\" " + class_href_0 + ">";
				} else if (startline == 1) {
					classLiString = "<li " + class_li_1 + ">";
					classHrefString = "<a   id=\"dropdown01\"   href=\"#\" " + class_href_1 + ">";
				} else {
					classLiString = ""; // "<li " + class_li_2 + ">";
					classHrefString = "<a  href=\"" + request.getContextPath() + "/" + menuItem[6] 
							+ menu_item_id + menuItem[9].toString() + "\" " + class_href_2 + ">";
				}

				if (showicons == false && menuItem[7].equals(null)) {

					classHrefString = classHrefString + menuItem[5] + "</a>";
				} else {
					classHrefString = classHrefString + "<i class=\"" + menuItem[7] + " \"></i> " + menuItem[5]
							+ "</a>";
				}

				if (endline == 0) {
					classLiStringEnd = class_li_end_0;
				} else if (endline == 1) {
					classLiStringEnd = class_li_end_1 + "";
				} else if (endline == 2) {
					classLiStringEnd = ""; // class_li_end_2;
				} else {
					classLiStringEnd = class_li_end_3;
				}

				menuString = menuString + classLiString + classHrefString + classLiStringEnd;

			}

			return menuString;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

	}

	public String getMenuItems6(boolean showicons, Long menuid) {

	
		try {

			String class_li_0 = " class=\"nav-item col-6 col-lg-auto nav-item-line\"";
			String class_li_1 = " class=\"nav-item  dropdown dropdown-animate\"  data-toggle=\"hover\"    ";
			String class_li_2 = "";


			String class_href_0 = "class=\"nav-link py-2 px-0 px-lg-2\""; // " class=\"btn btn-lg waves-effect\" ";
			String class_href_1 = " class=\"nav-link dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\"  aria-haspopup=\"true\" aria-expanded=\"false\" ";
			String class_href_2 = "class=\"dropdown-item\"";

			String class_li_end_0 = "</li>";
			String class_li_end_1 = "<ul aria-labelledby=\"dropdown01\" class=\"dropdown-menu dropdown-menu-single\">"; // "<ul
																														// class=\"dropdown-menu\"
																														// role=\"menu\">
																														// ";
			String class_li_end_2 = "</li>";
			String class_li_end_3 = "</ul></li>";

			String classLiString;
			String classHrefString;
			String classLiStringEnd;

			String menuString = "";

			Query q = entityManager.createNativeQuery("select * from app_menu_item_view where menu_id = " + menuid);

			List<Object[]> menuItens = q.getResultList();

			for (Object[] menuItem : menuItens) {

				
				Long  menu_item_id =  (Long) menuItem[0];
				int startline = (int) menuItem[4];
				int endline = (int) menuItem[8];

				String path_string = (String) menuItem[9];

				String gcmidText = "";
				if (!path_string.contains("/")) {
					gcmidText = "/" + menu_item_id;
				} else {
					gcmidText =  menuItem[9].toString();
				}

				
				if (startline == 0) {
					classLiString = "<li" + class_li_0 + ">";
					classHrefString = "<a   href=\""  + "/" + menuItem[6] + gcmidText + "\" "
							+ class_href_0 + ">";
					
					
				} else if (startline == 1) {
					
					classLiString = "<li " + class_li_1 + ">";
					classHrefString = "<a   id=\"dropdown01\"   href=\"#\" " + class_href_1 + ">";
				} else {
					classLiString = ""; // "<li " + class_li_2 + ">";
					classHrefString = "<a  href=\"" + "/" + menuItem[6] + gcmidText + "\" "
							+ class_href_2 + ">";
				}

				if (showicons == false && menuItem[7].equals(null)) {

					classHrefString = classHrefString + menuItem[5] + "</a>";
				} else {
					classHrefString = classHrefString + "<i class=\"" + menuItem[7] + " \"></i> " + menuItem[5]
							+ "</a>";
				}

				if (endline == 0) {
					classLiStringEnd = class_li_end_0;
				} else if (endline == 1) {
					classLiStringEnd = class_li_end_1 + "";
				} else if (endline == 2) {
					classLiStringEnd = ""; // class_li_end_2;
				} else {
					classLiStringEnd = class_li_end_3;
				}

				menuString = menuString + classLiString + classHrefString + classLiStringEnd;

			}

			return menuString;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

	}
	
	
	public String getMenuItems7(boolean showicons, Long menuid) {


		try {

			String class_li_0 = " class=\"nav-item nav-item-line\"";
			String class_li_1 = " class=\"nav-item  dropdown dropdown-animate\"  data-toggle=\"hover\"    ";
			String class_li_2 = "";

			// class_href_2 BOOT 4 class="nav-link dropdown-toggle" data-toggle="dropdown"
			// aria-haspopup="true" Aria-expanded="false"
			// <li class="nav-item active">
			// <li class="nav-item dropdown">

			String class_href_0 = "class=\"nav-link\""; // " class=\"btn btn-lg waves-effect\" ";
			String class_href_1 = " class=\"nav-link dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\"  aria-haspopup=\"true\" aria-expanded=\"false\" ";
			String class_href_2 = "class=\"dropdown-item\"";

			String class_li_end_0 = "</li>";
			String class_li_end_1 = "<ul aria-labelledby=\"dropdown01\" class=\"dropdown-menu dropdown-menu-single\">"; // "<ul
																														// class=\"dropdown-menu\"
																														// role=\"menu\">
																														// ";
			String class_li_end_2 = "</li>";
			String class_li_end_3 = "</ul></li>";

			String classLiString;
			String classHrefString;
			String classLiStringEnd;

			String menuString = "";

			Query q = entityManager.createNativeQuery("select * from app_menu_item_view where menu_id = " + menuid);

			List<Object[]> menuItens = q.getResultList();

			for (Object[] menuItem : menuItens) {

				
				Long  menu_item_id =  (Long) menuItem[0];
				int startline = (int) menuItem[4];
				int endline = (int) menuItem[8];

				String path_string = (String) menuItem[9];

				String gcmidText = "";
				if (!path_string.contains("gcmid")) {
					gcmidText = "?gcmid=" + menu_item_id;
				} else {
					gcmidText = "?gcmidf=" + menu_item_id + menuItem[9].toString();
				}

				if (startline == 0) {
					classLiString = "<li" + class_li_0 + ">";
					classHrefString = "<a   href=\"" + request.getContextPath() + "/" + menuItem[6] + gcmidText + "\" "
							+ class_href_0 + ">";
				} else if (startline == 1) {
					classLiString = "<li " + class_li_1 + ">";
					classHrefString = "<a   id=\"dropdown01\"   href=\"#\" " + class_href_1 + ">";
				} else {
					classLiString = ""; // "<li " + class_li_2 + ">";
					classHrefString = "<a  href=\"" + request.getContextPath() + "/" + menuItem[6] + gcmidText + "\" "
							+ class_href_2 + ">";
				}

				if (showicons == false && menuItem[7].equals(null)) {

					classHrefString = classHrefString + menuItem[5] + "</a>";
				} else {
					classHrefString = classHrefString + "<i class=\"" + menuItem[7] + " \"></i> " + menuItem[5]
							+ "</a>";
				}

				if (endline == 0) {
					classLiStringEnd = class_li_end_0;
				} else if (endline == 1) {
					classLiStringEnd = class_li_end_1 + "";
				} else if (endline == 2) {
					classLiStringEnd = ""; // class_li_end_2;
				} else {
					classLiStringEnd = class_li_end_3;
				}

				menuString = menuString + classLiString + classHrefString + classLiStringEnd;

			}

			return menuString;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

	}

	public String getMenuItems33(boolean showicons, Long menuid) {


		try {

			String class_li_0 = " class=\"nav-item\"";
			String class_li_1 = " class=\"nav-item dropdown dropdown-animate\"  data-toggle=\"hover\"    ";
			String class_li_2 = "";

			// class_href_2 BOOT 4 class="nav-link dropdown-toggle" data-toggle="dropdown"
			// aria-haspopup="true" Aria-expanded="false"
			// <li class="nav-item active">
			// <li class="nav-item dropdown">

			String class_href_0 = "class=\"nav-link\""; // " class=\"btn btn-lg waves-effect\" ";
			String class_href_1 = " class=\"nav-link dropdown-toggle\" data-toggle=\"dropdown\" role=\"button\"  aria-haspopup=\"true\" aria-expanded=\"false\" ";
			String class_href_2 = "class=\"dropdown-item\"";

			String class_li_end_0 = "</li>";
			String class_li_end_1 = "<ul aria-labelledby=\"dropdown01\" class=\"dropdown-menu dropdown-menu-single\">"; // "<ul
																														// class=\"dropdown-menu\"
																														// role=\"menu\">
																														// ";
			String class_li_end_2 = "</li>";
			String class_li_end_3 = "</ul></li>";

			String classLiString;
			String classHrefString;
			String classLiStringEnd;

			String menuString = "";

			Query q = entityManager.createNativeQuery("select * from app_menu_item_view where menu_id = " + menuid);

			List<Object[]> menuItens = q.getResultList();

			for (Object[] menuItem : menuItens) {

			
				Long menu_item_id = (Long) menuItem[0];
				int startline = (int) menuItem[4];
				int endline = (int) menuItem[8];

				if (startline == 0) {
					classLiString = "<li" + class_li_0 + ">";
					classHrefString = "<a   href=\"" + request.getContextPath() + "/" + menuItem[6] + "?gcmid="
							+ menu_item_id + menuItem[9].toString() + "\" " + class_href_0 + ">";
				} else if (startline == 1) {
					classLiString = "<li " + class_li_1 + ">";
					classHrefString = "<a   id=\"dropdown01\"   href=\"#\" " + class_href_1 + ">";
				} else {
					classLiString = ""; // "<li " + class_li_2 + ">";
					classHrefString = "<a  href=\"" + request.getContextPath() + "/" + menuItem[6] + "?gcmid="
							+ menu_item_id + menuItem[9].toString() + "\" " + class_href_2 + ">";
				}

				if (showicons == false && menuItem[7].equals(null)) {

					classHrefString = classHrefString + menuItem[5] + "</a>";
				} else {
					classHrefString = classHrefString + "<i class=\"" + menuItem[7] + " \"></i> " + menuItem[5]
							+ "</a>";
				}

				if (endline == 0) {
					classLiStringEnd = class_li_end_0;
				} else if (endline == 1) {
					classLiStringEnd = class_li_end_1 + "";
				} else if (endline == 2) {
					classLiStringEnd = ""; // class_li_end_2;
				} else {
					classLiStringEnd = class_li_end_3;
				}

				menuString = menuString + classLiString + classHrefString + classLiStringEnd;

			}

			return menuString;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

	}

	public String getMenuItemsHover(boolean showicons, Long menuid) {

		try {

			String class_li_0 = "";
			String class_li_1 = " class=\"has-children\"";
			String class_li_2 = "";

			String class_href_0 = "class=\"nav-link\"";
			String class_href_1 = " class=\"nav-link\"";
			String class_href_2 = "class=\"dropdown-item\"";

			String class_li_end_0 = "</li>";
			String class_li_end_1 = "<ul class=\"dropdown arrow-top\">";
			String class_li_end_2 = "</li>";
			String class_li_end_3 = "</ul></li>";

			String classLiString;
			String classHrefString;
			String classLiStringEnd;

			String menuString = "";

			Query q = entityManager.createNativeQuery("select * from app_menu_item_view where menu_id = " + menuid);

			List<Object[]> menuItens = q.getResultList();

			for (Object[] menuItem : menuItens) {

			
				Long menu_item_id = (Long) menuItem[0];
				int startline = (int) menuItem[4];
				int endline = (int) menuItem[8];

				if (startline == 0) {
					classLiString = "<li" + class_li_0 + ">";
					classHrefString = "<a   href=\"" + request.getContextPath() + "/" + menuItem[6] + "?gcmid="
							+ menu_item_id + menuItem[9].toString() + "\" " + class_href_0 + ">";
				} else if (startline == 1) {
					classLiString = "<li " + class_li_1 + ">";
					classHrefString = "<a   id=\"dropdown01\"   href=\"#\" " + class_href_1 + ">";
				} else {
					classLiString = ""; // "<li " + class_li_2 + ">";
					classHrefString = "<a  href=\"" + request.getContextPath() + "/" + menuItem[6] + "?gcmid="
							+ menu_item_id + menuItem[9].toString() + "\" " + class_href_2 + ">";
				}

				if (showicons == false && menuItem[7].equals(null)) {

					classHrefString = classHrefString + menuItem[5] + "</a>";
				} else {
					classHrefString = classHrefString + "<i class=\"" + menuItem[7] + " \"></i> " + menuItem[5]
							+ "</a>";
				}

				if (endline == 0) {
					classLiStringEnd = class_li_end_0;
				} else if (endline == 1) {
					classLiStringEnd = class_li_end_1 + "";
				} else if (endline == 2) {
					classLiStringEnd = ""; // class_li_end_2;
				} else {
					classLiStringEnd = class_li_end_3;
				}

				menuString = menuString + classLiString + classHrefString + classLiStringEnd;

			}

			return menuString;

		} catch (Exception e) {
			e.printStackTrace();
			return null;

		}

	}



	private UrlMenu urlMenu;

	public UrlMenu getUrlMenu() {
		return this.urlMenu;
	}

	public void setUrlMenu(UrlMenu urlMenu) {
		this.urlMenu = urlMenu;
	}

	@PostConstruct
	public void init() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}


		if (this.id == null) {


			this.urlMenu = this.example;


		} else {
			this.urlMenu = findById(getId());
		}
	}

	public UrlMenu findByUuid(Long uuid) {

		try {
			
			TypedQuery<UrlMenu> queryApp = entityManager.createNamedQuery(UrlMenu.MENU_ID,
					UrlMenu.class);
			queryApp.setParameter("id", id);
										
			this.urlMenu = queryApp.getSingleResult();

			if (!Hibernate.isInitialized(this.urlMenu.getMenuItens())) {
				Hibernate.initialize(this.urlMenu.getMenuItens());

				for (UrlMenuItem h : this.urlMenu.getMenuItens()) {
					Hibernate.initialize(h.getUrlMenu());
					Hibernate.initialize(h.getParentId());
					Hibernate.initialize(h.getChildrens());
				}

			}

			if (!Hibernate.isInitialized(this.urlMenu.getApplicationEntity())) {
				Hibernate.initialize(this.urlMenu.getApplicationEntity());
			}

			return this.urlMenu;

		} catch (Throwable e) {
			return null;
		}

	}

	public UrlMenu findById(Long id) {

		try {

			return entityManager.find(UrlMenu.class, id);
		} catch (Throwable e) {
			return null;
		}
	}

	/*
	 * Support updating and deleting UrlMenu entities
	 */
	@Transactional
	public void update() {

			if (this.id == null) {
				entityManager.persist(this.urlMenu);
				entityManager.flush();
				FacesMessage msg = new FacesMessage("SUCESSO", "Menu Alterado!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				
				redirect("/administrator/application_view/0/" + this.urlMenu.getApplicationEntity().getId());

			} else {
				entityManager.merge(this.urlMenu);
				entityManager.flush();
				FacesMessage msg = new FacesMessage("SUCESSO", "Menu Criado!");
				FacesContext.getCurrentInstance().addMessage(null, msg);
				
				redirect("/administrator/application_view/0/" + this.urlMenu.getApplicationEntity().getId());
			}
	}

	@Transactional
	public String delete() {

		try {

			UrlMenu deletableEntity = findByUuid(getId());

			entityManager.remove(deletableEntity);
			entityManager.flush();

		} catch (Exception e) {
			return "menu_search?faces-redirect=true";
		}

		return "menu_search?faces-redirect=true";

	}

	/*
	 * Support searching UrlMenu entities with pagination
	 */

	private int page;
	private long count;
	private List<UrlMenu> pageItems;

	private UrlMenu example = new UrlMenu();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public UrlMenu getExample() {
		return this.example;
	}

	public void setExample(UrlMenu example) {
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
			Root<UrlMenu> root = countCriteria.from(UrlMenu.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();

			// Populate this.pageItems

			CriteriaQuery<UrlMenu> criteria = builder.createQuery(UrlMenu.class);
			root = criteria.from(UrlMenu.class);
			TypedQuery<UrlMenu> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicates(root, entityManager)));
			query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
			this.pageItems = query.getResultList();

		} catch (Throwable e) {

		}
	}

	private Predicate[] getSearchPredicates(Root<UrlMenu> root, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String name = this.example.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<UrlMenu> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back UrlMenu entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<UrlMenu> getAll() {

		try {
			CriteriaQuery<UrlMenu> criteria = entityManager.getCriteriaBuilder().createQuery(UrlMenu.class);
			return entityManager.createQuery(criteria.select(criteria.from(UrlMenu.class))).getResultList();

		} catch (Throwable e) {
			return null;
		}

	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private UrlMenu add = new UrlMenu();

	public UrlMenu getAdd() {
		return this.add;
	}

	public UrlMenu getAdded() {
		UrlMenu added = this.add;
		this.add = new UrlMenu();
		return added;
	}

	// ------------------------new menu control order and select

	private UrlMenuItem selectedMenuItem;
	private List<UrlMenuItem> selectedMenuItems;

	public UrlMenuItem getSelectedMenuItem() {
		return selectedMenuItem;
	}

	public void setSelectedMenuItem(UrlMenuItem selectedMenuItem) {
		this.selectedMenuItem = selectedMenuItem;
	}

	public List<UrlMenuItem> getSelectedMenuItems() {
		return selectedMenuItems;
	}

	public void setSelectedMenuItems(List<UrlMenuItem> selectedMenuItems) {
		this.selectedMenuItems = selectedMenuItems;
	}

	private TreeNode root;
	private TreeNode selectedNode;

	@Inject
	private UrlMenuBarFacade facade;

	public void buildTree() {
		try {
			root = new DefaultTreeNode("Root", null);

			List<UrlMenuItem> menusRoot = facade.getRootUrlMenuItems(this.urlMenu);
			for (UrlMenuItem menu : menusRoot) {

				Long menuId = menu.getId();

				DefaultTreeNode node;

				if (menu.getChildrens().size() > 0) {
					node = new DefaultTreeNode(menu, root);
				} else {
					node = new DefaultTreeNode(menu.getType(), menu, root);
				}
				node.setExpanded(true);
				buildSubTree(menu, node);
			}

		} catch (Exception ex) {
		}
	}

	public void buildSubTree(UrlMenuItem menu, DefaultTreeNode parent) {

		List<UrlMenuItem> subMenus = facade.getSubMenus(menu.getId());
		for (UrlMenuItem subMenu : subMenus) {

			Long subMenuId = subMenu.getId();

			Boolean hasSubMenu = facade.hasSubMenu(subMenu);
			DefaultTreeNode node;
			if (!hasSubMenu) {
				node = new DefaultTreeNode(subMenu.getType(), subMenu, parent);
			} else {
				node = new DefaultTreeNode(subMenu, parent);
				List<UrlMenuItem> getSubMenus = facade.getSubMenus(subMenu.getId());

				for (UrlMenuItem m : getSubMenus) {
					Long subMId = m.getId();
					DefaultTreeNode sub = new DefaultTreeNode(m.getType(), m, node);

					sub.setExpanded(true);
				}
			}

			node.setExpanded(true);

		}
	}

	public TreeNode getRoot() {
		buildTree();
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode getSelectedNode() {
		if (selectedNode != null) {
			this.selectedMenuItem = (UrlMenuItem) selectedNode.getData();
		}

		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public List<UrlMenuItem> getMenusRoot() {
		if (selectedNode != null) {
			UrlMenuItem urlMenu = (UrlMenuItem) selectedNode.getData();
			if (urlMenu.getParentId() == null) {
				return facade.getRootUrlMenuItems(this.urlMenu);
			} else {
				return facade.getSubMenus(urlMenu.getParentId().getId());
			}
		} else {
			return null;
		}
	}

	public List<ApplicationIdentityEntity> getAllIdentityApps() {

		try {

			CriteriaQuery<ApplicationIdentityEntity> criteria = entityManager.getCriteriaBuilder()
					.createQuery(ApplicationIdentityEntity.class);

			List<ApplicationIdentityEntity> dd = entityManager
					.createQuery(criteria.select(criteria.from(ApplicationIdentityEntity.class))).getResultList();
			return dd;
		} catch (Exception e) {

		}
		return null;
	}

	public List<UrlMenuItem> getSelectedMenuId() {
		return selectedMenuId;
	}

	public void setSelectedMenuId(List<UrlMenuItem> selectedMenuId) {
		this.selectedMenuId = selectedMenuId;
	}
}
