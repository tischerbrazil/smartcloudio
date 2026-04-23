package org.geoazul.view.website;

import static org.omnifaces.util.Faces.redirect;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.transaction.Transactional;
import org.geoazul.model.app.AbstractIdentityEntity;
import org.geoazul.model.app.ApplicationEntity;
import org.geoazul.model.app.ApplicationIdentityEntity;
import org.geoazul.model.basic.AbstractWidget;
import org.geoazul.model.basic.Comp;
import org.geoazul.model.basic.AbstractWidget.CompDef;
import org.geoazul.model.website.DivCode;
import org.geoazul.model.website.ModuleComponentMap;
import org.geoazul.model.website.ModuleMenuMap;
import org.geoazul.model.website.Modulo;
import org.geoazul.model.website.RBlock;
import org.geoazul.model.website.RButton;
import org.geoazul.model.website.RComponent;
import org.geoazul.model.website.RDivBreak;
import org.geoazul.model.website.RFlexDiv;
import org.geoazul.model.website.RGraphicImage;
import org.geoazul.model.website.RFlexDiv.JustifyContent;
import org.geoazul.model.website.RHeading;
import org.geoazul.model.website.RParagraph;
import org.geoazul.model.website.RVideo;
import org.geoazul.model.website.UrlMenu;
import org.geoazul.model.website.UrlMenuItem;
import org.geoazul.model.website.RHeading.TypeHeading;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.component.graphicimage.GraphicImage;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.TreeNode;
import org.primefaces.refact.ModuloJpaLazyDataModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import jsonb.JacksonUtil;

@Named
@ViewScoped
public class ModuloBean implements Serializable {

	private static final long serialVersionUID = 1L;

	GraphicImage graphicImage;

	private RBlock rBlock;

	@PreDestroy
	public void preDestroy() {
	}

	@Inject
	@Param(pathIndex = 0)
	private Long cgmid;

	@Inject
	@Param(pathIndex = 1)
	private Long id;

	private ModuloJpaLazyDataModel<Modulo> model;

	
	public void ini2t() {
		model = new ModuloJpaLazyDataModel<>(Modulo.class, () -> entityManager);
	}
	
	@PostConstruct
	public void init() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		
		
		try {
			if (this.id == null) {
			
				if (modcomp_uuid != null) {
				
					ModuleComponentMap componentMap = findModuleCompById(this.id);

					this.modulo = findById(componentMap.getModule().getId());

					try {
						this.getModulo().getMenuItens().size();
					} catch (Exception e) {
						this.modulo.setStrings(JacksonUtil.toJsonNode("{}"));
					}
				
					this.loadWidgets();
					
					// FIXME GAMBIARRA
					try {
						if (this.modulo.getStrings().equals(null)) {
							this.modulo.setStrings(JacksonUtil.toJsonNode("{}"));
						}
					} catch (Exception e) {
						this.modulo.setStrings(JacksonUtil.toJsonNode("{}"));
					}
					// END GAMBIARRA
					
				
					
				//	PrimeFaces current = PrimeFaces.current();
				//	current.executeScript("PF('changeOrder').show()");
				}else {
					this.modulo = new Modulo();
				}
			} else {
				this.modulo = findById(this.id);

				try {
					this.getModulo().getMenuItens().size();
				} catch (Exception e) {
					this.modulo.setStrings(JacksonUtil.toJsonNode("{}"));
				}

				this.loadWidgets();

				// FIXME GAMBIARRA
				try {
					if (this.modulo.getStrings().equals(null)) {
						this.modulo.setStrings(JacksonUtil.toJsonNode("{}"));
					}
				} catch (Exception e) {
					this.modulo.setStrings(JacksonUtil.toJsonNode("{}"));
				}
				// END GAMBIARRA

				
			}

		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	private AbstractIdentityEntity abstractIdentity;

	public AbstractIdentityEntity getAbstractIdentity() {
		return abstractIdentity;
	}

	public void setAbstractIdentity(AbstractIdentityEntity abstractIdentity) {
		this.abstractIdentity = abstractIdentity;
	}

	private Comp component;

	public Comp getComponent() {
		return component;
	}

	public void setComponent(Comp component) {
		this.component = component;
	}

	private AbstractWidget widget;

	public AbstractWidget getWidget() {
		return widget;
	}

	public void setWidget(AbstractWidget widget) {
		this.widget = widget;
	}

	// LIST------------------------------

	private List<Comp> components;

	public List<Comp> getComponents() {
		return components;
	}

	public void setComponents(List<Comp> components) {
		this.components = components;
	}

	private List<AbstractWidget> widgets;

	public List<AbstractWidget> getWidgets() {
		return widgets;
	}

	public void setWidgets(List<AbstractWidget> widgets) {
		this.widgets = widgets;
	}

	// ---------------------------------------------------

	@Transactional
	public void displayLocation() {
		FacesMessage msg;
		if (widget != null) {

			if (component != null) {

				// vamos anexar novo componente selecionado

				AbstractIdentityEntity abstractIdentityEntity = entityManager.find(AbstractIdentityEntity.class,
						this.modulo.getAbstractIdentityEntity().getId());

				Modulo moduloy = entityManager.find(Modulo.class, this.modulo.getId());

				Comp compy = entityManager.find(Comp.class, this.component.getId());

				AbstractWidget widgety = entityManager.find(AbstractWidget.class, this.widget.getId());

				ModuleComponentMap modCompMapWidgety = new ModuleComponentMap(moduloy,
						JacksonUtil
								.toJsonNode("{\"type\":\"" + compy.getDtype() + "\",   \"id\":" + compy.getId() + "}"),
						moduloy.getComponents().size() + 1, widgety, widgety.getStrings(),
						JacksonUtil.toJsonNode("{}"));
				modCompMapWidgety.setEnabled(true);
				entityManager.persist(modCompMapWidgety);

				entityManager.flush();



				this.modulo.getComponents().add(modCompMapWidgety);


				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Widget Anexado!");

			} else {

				// vamos criar novo componente custom

				AbstractIdentityEntity abstractIdentityEntity = entityManager.find(AbstractIdentityEntity.class,
						this.modulo.getAbstractIdentityEntity().getId());



				Modulo modulox = entityManager.find(Modulo.class, this.modulo.getId());


				AbstractWidget widgetx = entityManager.find(AbstractWidget.class, this.widget.getId());


				ModuleComponentMap modCompMapWidget = new ModuleComponentMap(modulox,
						JacksonUtil.toJsonNode("{\"type\":\"custom\"}"), this.modulo.getComponents().size() + 1,
						widgetx, widgetx.getStrings(), JacksonUtil.toJsonNode("{}"));
				modCompMapWidget.setEnabled(true);
				entityManager.persist(modCompMapWidget);

				entityManager.flush();



				this.modulo.getComponents().add(modCompMapWidget);


				msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Widget Inserido");

			}

		} else {


			msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Inválido", "Widget não selecionado.");
		}

		

		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onComponentChange() {
		if (component != null) {

			// point, polygon, linestring, multipolygon,polygonpoint,
			// tile, rastergeoserver,
			// custom, control, menu, post, register
			try {
				CompDef compDef;
				switch (component.getDtype()) {
				case "point":
					compDef = CompDef.point;
					break;
				case "polygon":
					compDef = CompDef.polygon;
					break;
				case "linestring":
					compDef = CompDef.linestring;
					break;
				case "multipolygon":
					compDef = CompDef.multipolygon;
					break;
				case "polygonpoint":
					compDef = CompDef.polygonpoint;
					break;
				case "tile":
					compDef = CompDef.tile;
					break;
				case "rastergeoserver":
					compDef = CompDef.rastergeoserver;
					break;
				case "control":
					compDef = CompDef.control;
					break;
				case "menu":
					compDef = CompDef.menu;
					break;
				case "post":
					compDef = CompDef.post;
					break;
				case "register":
					compDef = CompDef.register;
					break;
				default:
					compDef = CompDef.custom;
					break;
				}

				String absWid = "select awid from AbstractWidget awid where awid.compDef = :compDef";
				TypedQuery<AbstractWidget> findAllQuery = entityManager.createQuery(absWid, AbstractWidget.class)
						.setParameter("compDef", compDef);
				widgets = findAllQuery.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public void onIdentityChange() {
		if (abstractIdentity != null) {
			try {

				String comps = "select comp from Comp comp where comp.applicationEntity.id = :appId";
				TypedQuery<Comp> findAllQuery = entityManager.createQuery(comps, Comp.class).setParameter("appId",
						abstractIdentity.getId());
				components = findAllQuery.getResultList();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} 
	}

	public List<Comp> getAllComp() {
		try {
			// String comps = "select comp from Comp comp where comp.applicationEntity.id =
			// :appId";
			// TypedQuery<Comp> findAllQuery = entityManager.createQuery(comps,
			// Comp.class).setParameter("appId",
			// this.modulo.getAbstractIdentityEntity().getId());
			// List<Comp> hhh = findAllQuery.getResultList();

			// return hhh;

			try {
				CriteriaQuery<Comp> criteria = entityManager.getCriteriaBuilder().createQuery(Comp.class);
				return entityManager.createQuery(criteria.select(criteria.from(Comp.class))).getResultList();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Comp>();
		}

	}

	public List<AbstractIdentityEntity> getAllAbstractdentity() {
		try {

			try {
				CriteriaQuery<AbstractIdentityEntity> criteria = entityManager.getCriteriaBuilder()
						.createQuery(AbstractIdentityEntity.class);
				return entityManager.createQuery(criteria.select(criteria.from(AbstractIdentityEntity.class)))
						.getResultList();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<AbstractIdentityEntity>();
		}

	}

	@Inject
	private EntityManager entityManager;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCgmid() {
		return cgmid;
	}

	public void setCgmid(Long cgmid) {
		this.cgmid = cgmid;
	}

	private String modcomp_uuid;

	public String getModcomp_uuid() {
		return modcomp_uuid;
	}

	public void setModcomp_uuid(String modcomp_uuid) {
		this.modcomp_uuid = modcomp_uuid;
	}

	private Modulo modulo;

	public Modulo getModulo() {
		return this.modulo;
	}

	public void setModulo(Modulo modulo) {
		this.modulo = modulo;
	}

	

	public void loadWidgets() {
		try {
			String absWid = "select awid from AbstractWidget awid";
			TypedQuery<AbstractWidget> findAllWidgets = entityManager.createQuery(absWid, AbstractWidget.class);
			this.widgets = findAllWidgets.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ModuleComponentMap findModuleCompById(Long id) {
		try {
			TypedQuery<ModuleComponentMap> queryModule = entityManager.createNamedQuery(ModuleComponentMap.FIND_BY_ID,
					ModuleComponentMap.class);
			queryModule.setParameter("id", id);
			return queryModule.getResultStream().findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Modulo findById(Long id) {
		try {
			TypedQuery<Modulo> queryModule = entityManager.createNamedQuery(Modulo.MODULE_ID, Modulo.class);
			queryModule.setParameter("id", id);
			this.modulo = queryModule.getSingleResult();
			return this.modulo;
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * Support updating and deleting Modulo entities
	 */

	@Transactional
	public void update() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		if (this.id == null) {
			entityManager.persist(this.modulo);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "Módulo Inserido!"));
			facesContext.addMessage(null, new FacesMessage("SUCESSO!", "REALM ALTERADO!"));
			redirect("/administrator/module_view/" + this.modulo.getId().toString());
		} else {
			entityManager.merge(this.modulo);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "Módulo Alterado!"));
			redirect("/administrator/application_view/" + this.modulo.getAbstractIdentityEntity().getId().toString());
		}
	}

	@Transactional
	public void delete() {
		Long identityId = null;
		try {
			Modulo deletableEntity = findById(this.id);
			identityId = deletableEntity.getAbstractIdentityEntity().getId();
			entityManager.remove(deletableEntity);
			entityManager.flush();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "Container Excluido!"));
			redirect("/administrator/application_view/" + identityId);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "Erro ao Excluir Container!"));
			redirect("/administrator/application_search");
		}
	}

	/*
	 * Support searching Modulo entities with pagination
	 * 
	 */

	/*
	 * Support listing and POSTing back Modulo entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Modulo> getAll() {
		try {
			CriteriaQuery<Modulo> criteria = entityManager.getCriteriaBuilder().createQuery(Modulo.class);
			return entityManager.createQuery(criteria.select(criteria.from(Modulo.class))).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	// public List<Modulo> getAllRootItens() {
	// if (this.id == null) {
	// TypedQuery<Modulo> queryApp32 =
	// session.createNamedQuery(Modulo.ITENS_ROOT, Modulo.class);
	// queryApp32.setParameter("urlmenuid", application_id);
	// return queryApp32.getResultList();
	// }else {
	// TypedQuery<Modulo> queryApp32 =
	// session.createNamedQuery(Modulo.ITENS_ROOT_EXCLUDE_ITEM, Modulo.class);
	// queryApp32.setParameter("urlmenuid", application_id);
	// queryApp32.setParameter("urlmenuitemid", id);
	// return queryApp32.getResultList();
	// }
	// }

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Modulo add = new Modulo();

	public Modulo getAdd() {
		return this.add;
	}

	public Modulo getAdded() {
		Modulo added = this.add;
		this.add = new Modulo();
		return added;
	}

	public List<String> completeText(String query) {
		List<String> results = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			results.add(query + i);
		}
		return results;
	}

	@Transactional
	public void getAllowedAll() {
		try {
			Query query = entityManager.createNamedQuery(ModuleMenuMap.ALLOW_ALL);
			query.setParameter("urlModule", this.modulo);
			int rrr = query.executeUpdate();
			entityManager.flush();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}
	}

	@Transactional
	public void displaySelected() {

		try {
			// session.merge(this.modulo);

			if (this.modulo.getShow() == 0) {

				entityManager.createNamedQuery("deleteByModId", ModuleMenuMap.class)
						.setParameter(1, this.modulo.getId()).executeUpdate();

				this.modulo = entityManager.find(Modulo.class, this.modulo.getId());

				this.modulo.setShow(0);
				entityManager.merge(this.modulo);
				entityManager.flush();
				entityManager.refresh(this.modulo);

			} else if (this.modulo.getShow() == 1) {

				entityManager.createNamedQuery("deleteByModId", ModuleMenuMap.class)
						.setParameter(1, this.modulo.getId()).executeUpdate();

				this.modulo = entityManager.find(Modulo.class, this.modulo.getId());

				// this.modulo.setShow(1);
				// entityManager.merge(this.modulo);
				// entityManager.flush();
				// entityManager.refresh(this.modulo);

				// getInsertMenuItemId(0);
				this.getAllowedAll();
			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}
	}

	public List<AbstractWidget> getAllWidgets() {
		try {
			CriteriaQuery<AbstractWidget> criteria = entityManager.getCriteriaBuilder()
					.createQuery(AbstractWidget.class);
			return entityManager.createQuery(criteria.select(criteria.from(AbstractWidget.class))).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	private TreeNode root;
	private TreeNode[] selectedNode;

	@Inject
	private UrlMenuBarFacade facade;

	public void buildTree() {

		try {

			root = new DefaultTreeNode("Root", null);

			boolean isSelected;


			ApplicationIdentityEntity abstractIdentityEntity = entityManager.find(ApplicationIdentityEntity.class,
					this.modulo.getAbstractIdentityEntity().getId());

			List<UrlMenu> menusFatherRoot = facade.getRootUrlMenus(abstractIdentityEntity);

			for (UrlMenu menuFather : menusFatherRoot) {
				DefaultTreeNode node1 = new DefaultTreeNode(menuFather, root);


				Long menuItemId = 0L;
				isSelected = this.getModulo().getMenuItens().stream()
						.anyMatch(user -> menuItemId.equals(user.getUrlMenuItem()));

				node1.setSelected(isSelected);
				node1.setExpanded(true);
				List<UrlMenuItem> menusRoot = facade.getRootUrlMenuItems(menuFather);


				for (UrlMenuItem menu : menusRoot) {
					Long menuId = menu.getId();
					boolean isSelectedMenuId = this.getModulo().getMenuItens().stream()
							.anyMatch(user -> menuId.equals(user.getUrlMenuItem()));
					DefaultTreeNode node;


					if (menu.getChildrens().size() > 0) {
						node = new DefaultTreeNode(menu, node1);
					} else {


						node = new DefaultTreeNode(menu.getType(), menu, node1);
					}
					if (isSelected) {
						node.setSelected(isSelected);
					} else {
						node.setSelected(isSelectedMenuId);
					}
					node.setExpanded(true);
					buildSubTree(menu, node, isSelected);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}
	}

	public void buildSubTree(UrlMenuItem menu, DefaultTreeNode parent, boolean isSelected) {

		List<UrlMenuItem> subMenus = facade.getSubMenus(menu.getId());
		for (UrlMenuItem subMenu : subMenus) {

			Long subMenuId = subMenu.getId();
			boolean isSubMenuId = this.getModulo().getMenuItens().stream()
					.anyMatch(user -> subMenuId.equals(user.getUrlMenuItem()));

			Boolean hasSubMenu = facade.hasSubMenu(subMenu);
			DefaultTreeNode node;
			if (!hasSubMenu) {
				node = new DefaultTreeNode(subMenu.getType(), subMenu, parent);
			} else {
				node = new DefaultTreeNode(subMenu, parent);
				List<UrlMenuItem> getSubMenus = facade.getSubMenus(subMenu.getId());

				for (UrlMenuItem m : getSubMenus) {
					Long subMId = m.getId();
					boolean isSubMId = this.getModulo().getMenuItens().stream()
							.anyMatch(user -> subMId.equals(user.getUrlMenuItem()));
					DefaultTreeNode sub = new DefaultTreeNode(m.getType(), m, node);

					if (isSelected) {
						sub.setSelected(isSelected);
					} else {
						sub.setSelected(isSubMId);
					}
					sub.setExpanded(true);
				}
			}

			if (isSelected) {
				node.setSelected(isSelected);
			} else {
				node.setSelected(isSubMenuId);
			}
			node.setExpanded(true);

		}
	}

	public TreeNode getRoot() {
		if (root == null) {
			buildTree();
			// checkTree(root);
		}
		return root;
	}

	public void setRoot(TreeNode root) {
		this.root = root;
	}

	public TreeNode[] getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode[] selectedNode) {
		this.selectedNode = selectedNode;
	}

	@Transactional
	public void getInsertMenuItemId(Long menuItem) {
		try {
			Query query = entityManager.createNamedQuery(ModuleMenuMap.INSERT);
			query.setParameter("urlMenuItemId", menuItem);
			query.setParameter("urlModule", this.modulo);
			int rrr = query.executeUpdate();
			entityManager.flush();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}
	}

	@Transactional
	public void displaySelectedMultiple(TreeNode[] nodes) {
		try {
			if (nodes != null && nodes.length > 0) {


				entityManager.createNamedQuery("deleteByModId", ModuleMenuMap.class)
						.setParameter(1, this.modulo.getId()).executeUpdate();

				// Execute the delete query

				for (TreeNode node : nodes) {
					if (node.getData() instanceof UrlMenuItem) {
						UrlMenuItem subMenuIdItem = (UrlMenuItem) node.getData();
						getInsertMenuItemId(subMenuIdItem.getId());
					}
				}

				this.modulo = entityManager.find(Modulo.class, this.modulo.getId());

				this.modulo.setShow(2);
				entityManager.merge(this.modulo);
				entityManager.flush();
				entityManager.refresh(this.modulo);
				// -------------
			} else {
				entityManager.createNamedQuery("deleteByModId", ModuleMenuMap.class)
						.setParameter(1, this.modulo.getId()).executeUpdate();

				this.modulo = entityManager.find(Modulo.class, this.modulo.getId());

				this.modulo.setShow(0);
				entityManager.merge(this.modulo);
				entityManager.flush();
				entityManager.refresh(this.modulo);
			}
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Referência Alterada");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}
	}

	// ===============================================================

	public void editAction(ModuleComponentMap newCompApp) {


	
				
				String absWid = "select awid from AbstractWidget awid where awid.compDef = :compDef";
		
				TypedQuery<AbstractWidget> findAllQuery = entityManager.createQuery(absWid, AbstractWidget.class)
						.setParameter("compDef", newCompApp.getAbstractWidget().getCompDef());
				
				widgets = findAllQuery.getResultList();
				
			

		
		this.moduleComponentMap = newCompApp;

	}

	public void editActionView(ModuleComponentMap newXCompApp) {

		this.moduleComponentMap = newXCompApp;



		ObjectMapper map33 = new ObjectMapper();
		map33.registerSubtypes(new NamedType(RVideo.class, "RVideo"));
		map33.registerSubtypes(new NamedType(RButton.class, "RButton"));
		map33.registerSubtypes(new NamedType(RHeading.class, "RHeading"));
		map33.registerSubtypes(new NamedType(RFlexDiv.class, "RFlexDiv"));
		map33.registerSubtypes(new NamedType(RDivBreak.class, "RDivBreak"));
		map33.registerSubtypes(new NamedType(RParagraph.class, "RParagraph"));
		map33.registerSubtypes(new NamedType(RGraphicImage.class, "RGraphicImage"));

		try {
			rBlock = map33.readValue(this.moduleComponentMap.getBlockCode().toString(), RBlock.class);

		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}

		rBlock.setId(this.moduleComponentMap.getId());
		rBlock.setAbstractWidget(this.moduleComponentMap.getAbstractWidget().toString());


	}

	List<DivCode> divs;

	private List<RComponent> rComp_col_0 = new ArrayList<RComponent>();
	private List<RComponent> rComp_col_1 = new ArrayList<RComponent>();
	private List<RComponent> rComp_col_2 = new ArrayList<RComponent>();

	public RBlock getRBlockInit() {

		this.rComp_col_0.clear();
		this.rComp_col_1.clear();
		this.rComp_col_2.clear();


		rBlock = new RBlock();

		rBlock.setSection_id("section_id");
		rBlock.setSection_class("pt-15 pb-10 py-5");

		rBlock.setContainer_id("container_id");
		rBlock.setContainer_class("container px-lg-7 px-xxl-3");

		rBlock.setRow_div_id("row_id");
		rBlock.setRow_div_class(
				"row mt-2 align-items-center justify-content-between text-center text-lg-start mb-6 mb-lg-0");

		divs = new ArrayList<>();

		// -----------------------------START COLUMNS 1
		// ------------------------------------------------------

		DivCode divCode0 = new DivCode();

		divCode0.setDiv_id("div_code_id_0");
		divCode0.setDiv_class("col-lg-6 order-0 order-lg-1 card-banner");

		// RVideo rVideo = new RVideo();
		// rVideo.setId("1");
		// rVideo.setOrder(0);
		// rVideo.setColumn(0);

		// rVideo.setStyleClass("feature-image img-fluid mb-9 mb-lg-0");
		// rVideo.setPoster("themes/bs5-ecommerce/img/trat.jpg");
		// rVideo.setValue("/themes/bs5-ecommerce/img/tratoron.mp4");
		// rVideo.setWidth("906");
		// rVideo.setHeight("540");
		// rVideo.setCache(false);

		// rVideo.setPlayer(null);
		// rVideo.setParent(null);
		// rVideo.setPreload(null);
		// rVideo.setStyle(null);

		// rComp_col_0.add(rVideo);

		RGraphicImage rGraphicImage = new RGraphicImage();
		rGraphicImage.setId("1");
		rGraphicImage.setOrder(0);
		rGraphicImage.setColumn(0);

		rGraphicImage.setStyleClass("feature-image img-fluid mb-9 mb-lg-0");

		rGraphicImage.setValue("themes/bs5-ecommerce/img/trat.jpg");
		rGraphicImage.setWidth("906");
		rGraphicImage.setHeight("540");

		rGraphicImage.setParent(null);

		rGraphicImage.setStyle(null);

		rComp_col_0.add(rGraphicImage);

		divCode0.setrComponents(rComp_col_0);

		divs.add(divCode0); // vou adiciona o div coluna

		// ---------------------------END COLUMN 2
		// --------------------------------------------------------

		// ---------------------------START COLUMN 1
		// --------------------------------------------------------

		DivCode divCode1 = new DivCode();

		divCode1.setDiv_id("div_code_id_2");
		divCode1.setDiv_class("col-lg-6");

		// ------------------------- START
		// H3---------------------------------------------------------
		RHeading rHeading1 = new RHeading();
		rHeading1.setId("2");
		rHeading1.setOrder(0);
		rHeading1.setColumn(1);
		rHeading1.setTypeHeading(TypeHeading.h3);
		rHeading1.setStyleClass("text-primary mb-2 ls-2");
		rHeading1.setValue("<strong>Tratoron New-Holland</strong>");
		rComp_col_1.add(rHeading1);
		// ------------------------- END H3
		// ----------------------------------------------------------

		// ------------------------- START
		// p---------------------------------------------------------
		RHeading rHeading2 = new RHeading();
		rHeading2.setId("3");
		rHeading2.setOrder(1);

		rHeading2.setColumn(1);
		rHeading2.setTypeHeading(TypeHeading.h4);
		rHeading2.setStyleClass("fw-bolder mb-3");
		rHeading2.setValue("Nova Loja em Rio Branco/AC");
		rComp_col_1.add(rHeading2);
		// ------------------------- END p
		// ----------------------------------------------------------

		// ------------------------- START
		// p---------------------------------------------------------
		RHeading rHeading3 = new RHeading();
		rHeading3.setId("4");
		rHeading3.setOrder(2);
		rHeading3.setColumn(1);
		rHeading3.setTypeHeading(TypeHeading.p);
		rHeading3.setStyleClass("mb-4 px-md-7 px-lg-0");
		rHeading3.setValue("O SmartCloudIO fortalece cada vez mais\n"
				+ "			nosso vínculo com o agro, contribuindo com o crescimento dos\n"
				+ "			nosso clientesue.");
		rComp_col_1.add(rHeading3);
		// ------------------------- END p
		// ----------------------------------------------------------

		// ------------------------- START
		// p---------------------------------------------------------
		RHeading rHeading4 = new RHeading();
		rHeading4.setId("5");
		rHeading4.setOrder(3);

		rHeading4.setColumn(1);
		rHeading4.setTypeHeading(TypeHeading.p);
		rHeading4.setStyleClass("");
		rHeading4.setValue("<strong>Siga-nos em nossas redes sociais</strong>");
		rComp_col_1.add(rHeading4);
		// ------------------------- END p
		// ----------------------------------------------------------

		RFlexDiv rFlexDiv = new RFlexDiv();
		rFlexDiv.setId("6");
		rFlexDiv.setOrder(4);

		rFlexDiv.setColumn(1);
		rFlexDiv.setStyleClass("");
		rFlexDiv.setJustifyContent(JustifyContent.center);

		// ------------------------- START BUTTON 1
		// ----------------------------------------------------------
		RButton rButton1 = new RButton();
		rButton1.setId("7");
		rButton1.setOrder(4);
		rButton1.setColumn(1);
		rButton1.setParentId(0);
		rButton1.setValue("Facebook");
		rButton1.setTitle("Facebook");
		rButton1.setHref("https://www.facebook.com/tratoronnewholland");
		rButton1.setTarget("_blank");
		rButton1.setStyleClass("rounded-button ui-button-primary p-2 m-2");
		rButton1.setIcon("fa fa-facebook");

		// --------------------------- END BUTTON 1
		// -------------------------------------------------------------

		// ------------------------- START BUTTON 1
		// ----------------------------------------------------------
		RButton rButton2 = new RButton();
		rButton2.setId("8");
		rButton2.setOrder(4);
		rButton2.setColumn(1);

		rButton2.setParentId(1);
		rButton2.setValue("Instagram");
		rButton2.setTitle("Instagram");
		rButton2.setHref("https://www.instagram.com/tratoronnewholland");
		rButton2.setTarget("_blank");
		rButton2.setStyleClass("rounded-button ui-button-primary p-2 m-2");
		rButton2.setIcon("fa fa-instagram");

		// --------------------------- END BUTTON 1
		// -------------------------------------------------------------

		List<RComponent> rComponentsChildrens = new ArrayList<RComponent>();
		rComponentsChildrens.add(rButton1);
		rComponentsChildrens.add(rButton2);

		rFlexDiv.setChildren(rComponentsChildrens);

		rComp_col_1.add(rFlexDiv);

		divCode1.setrComponents(rComp_col_1);

		divs.add(divCode1); // vou adiciona o div coluna

		// ========================== END COLUMN 2
		// ==========================================================");


		rBlock.setDivs(divs);


		ObjectMapper objectMapper = new ObjectMapper();

		String blockAsString = "{}";
		try {
			blockAsString = objectMapper.writeValueAsString(rBlock);



		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		ObjectMapper map33 = new ObjectMapper();

		map33.registerSubtypes(new NamedType(RVideo.class, "RVideo"));
		map33.registerSubtypes(new NamedType(RButton.class, "RButton"));
		map33.registerSubtypes(new NamedType(RHeading.class, "RHeading"));
		map33.registerSubtypes(new NamedType(RFlexDiv.class, "RFlexDiv"));
		map33.registerSubtypes(new NamedType(RDivBreak.class, "RDivBreak"));
		map33.registerSubtypes(new NamedType(RParagraph.class, "RParagraph"));
		map33.registerSubtypes(new NamedType(RGraphicImage.class, "RGraphicImage"));


		String json = blockAsString;
		RBlock newRBlock = null;
		;
		try {
			newRBlock = map33.readValue(json, RBlock.class);
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}




		String blockAsString2 = "{}";
		ObjectMapper objectMapper2 = new ObjectMapper();
		try {
			blockAsString2 = objectMapper2.writeValueAsString(newRBlock);


		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		rBlock = newRBlock;

		return rBlock;

	}

	@Transactional
	public void saveAction() {


		try {
			Query query = entityManager.createNamedQuery(ModuleComponentMap.UPDATE_ORDERING);
			query.setParameter("moduleComponentMap", this.moduleComponentMap);
			query.setParameter("ordering", this.moduleComponentMap.getOrdering());
			query.setParameter("newStrings", this.moduleComponentMap.getStrings());
			query.setParameter("newComp", this.moduleComponentMap.getComp());

			query.setParameter("abstractWidget", this.moduleComponentMap.getAbstractWidget());
			query.setParameter("enabled", this.moduleComponentMap.isEnabled());
			query.executeUpdate();

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Widget Alterado!");
			FacesContext.getCurrentInstance().addMessage(null, message);
			entityManager.flush();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}

	}

	private ModuleComponentMap moduleComponentMap;

	public ModuleComponentMap getModuleComponentMap() {
		return moduleComponentMap;
	}

	public void setModuleComponentMap(ModuleComponentMap moduleComponentMap) {
		this.moduleComponentMap = moduleComponentMap;
	}

	@Transactional
	public void deleteModCompMap(ActionEvent actionEvent) {
		try {
			ModuleComponentMap moduleComponentMap = (ModuleComponentMap) actionEvent.getComponent().getAttributes()
					.get("compid");

			Query query = entityManager.createNamedQuery(ModuleComponentMap.DELETE);
			query.setParameter("moduleComponentMap", moduleComponentMap);
			query.executeUpdate();


			entityManager.flush();

			this.modulo.getComponents().remove(moduleComponentMap);

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Referência Removida!");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}

	}

	public List<ApplicationIdentityEntity> getAllIdentityApps() {
		try {
			CriteriaQuery<ApplicationIdentityEntity> criteria = entityManager.getCriteriaBuilder()
					.createQuery(ApplicationIdentityEntity.class);
			return entityManager.createQuery(criteria.select(criteria.from(ApplicationIdentityEntity.class)))
					.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public List<ApplicationEntity> getAllApps() {
		try {
			CriteriaQuery<ApplicationEntity> criteria = entityManager.getCriteriaBuilder()
					.createQuery(ApplicationEntity.class);
			return entityManager.createQuery(criteria.select(criteria.from(ApplicationEntity.class))).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public LazyDataModel<Modulo> getModel() {
		return model;
	}

	public void setModel(ModuloJpaLazyDataModel<Modulo> model) {
		this.model = model;
	}

	public RBlock getrBlock() {
		return rBlock;
	}

	public void setrBlock(RBlock rBlock) {
		this.rBlock = rBlock;
	}

	private String position = "left";

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Transactional
	public void changeBlockAction2() {

		try {

			if (selectedRComponent.getParentId() != null) {

				
				rBlock.getDivs().get(selectedRComponent.getColumn()).getrComponents().get(selectedRComponent.getOrder())
						.getChildren().set(0, selectedRComponent);
			

				rBlock.getDivs().get(selectedRComponent.getColumn()).getrComponents().get(selectedRComponent.getOrder())
						.getChildren().set(selectedRComponent.getParentId(), selectedRComponent);
			
			} else {

			
				rBlock.getDivs().get(selectedRComponent.getColumn()).getrComponents().set(selectedRComponent.getOrder(),
						selectedRComponent);
			}

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Bloco Alterado!");
			FacesContext.getCurrentInstance().addMessage(null, message);

		
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}

	}

	@Transactional
	public void saveBlockAction() {

		try {

			ObjectMapper objectMapper = new ObjectMapper();

			JsonNode retornoBlockJsonNode = JacksonUtil.toJsonNode("{}");

			retornoBlockJsonNode = JacksonUtil.toJsonNode(objectMapper.writeValueAsString(rBlock));
			this.moduleComponentMap.setBlockCode(retornoBlockJsonNode);

			Query query = entityManager.createNamedQuery(ModuleComponentMap.UPDATE_RBLOCK);
			query.setParameter("newBlockCode", retornoBlockJsonNode);
			query.setParameter("moduleComponentMap", this.moduleComponentMap);
			query.executeUpdate();

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Bloco Salvado!");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}

	}

	@Transactional
	public void deleteRComponent() {

		try {

			String uuidLoad = selectedRComponent.getId();

			Integer columnLoad = 0;
			Integer orderIndex = 0;
			Integer parentIndex = 0;

			Integer columnLoadExat = -1;
			Integer orderIndexExat = -1;
			Integer parentIndexExat = -1;

			for (DivCode divCodeLoad : this.rBlock.getDivs()) {

				for (RComponent rComponentLoad : divCodeLoad.getrComponents()) {
					if (uuidLoad.equals(rComponentLoad.getId())) {
						columnLoadExat = columnLoad;
						orderIndexExat = orderIndex;
						parentIndexExat = -1;
					}

					Optional<List<RComponent>> temChildrem = Optional.ofNullable(rComponentLoad.getChildren());

					if (temChildrem.isPresent()) {
						for (RComponent rComponentLoadChildren : rComponentLoad.getChildren()) {
							if (uuidLoad.equals(rComponentLoadChildren.getId())) {


								columnLoadExat = columnLoad;
								orderIndexExat = orderIndex;
								parentIndexExat = parentIndex;
							}
							parentIndex++;
						}
					}

					orderIndex++;
				}
				orderIndex = 0;
				parentIndex = 0;
				columnLoad++;
			}


			if (parentIndexExat == -1) {
				this.rBlock.getDivs().get(columnLoadExat).getrComponents().remove(orderIndexExat.intValue());

			} else {
				this.rBlock.getDivs().get(columnLoadExat).getrComponents().get(orderIndexExat).getChildren()
						.remove(parentIndexExat.intValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}

	}

	public void executeSelect2() {

		String uuidLoad = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("rCompId");


		for (DivCode divCodeLoad : this.rBlock.getDivs()) {

			for (RComponent rComponentLoad : divCodeLoad.getrComponents()) {
				if (uuidLoad.equals(rComponentLoad.getId())) {

					selectedRComponent = rComponentLoad;
				}

				Optional<List<RComponent>> temChildrem = Optional.ofNullable(rComponentLoad.getChildren());

				if (temChildrem.isPresent()) {
					for (RComponent rComponentLoadChildren : rComponentLoad.getChildren()) {
						if (uuidLoad.equals(rComponentLoadChildren.getId())) {

							selectedRComponent = rComponentLoadChildren;
						}

					}
				}

			}

		}

	}

	public void executeSelect() {

		String blockId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("blockid");
		String columnId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("columnid");
		String orderId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("orderid");
		String parentId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("parentid");


		Integer orderInt = Integer.valueOf(orderId);
		Integer columnInt = Integer.valueOf(columnId);
		Integer parentIdInt = null;
		if (parentId != null) {
			parentIdInt = Integer.parseInt(parentId);
		}

		if (moduleComponentMap != null || columnId != null || orderId != null) {
			if (columnId.equals("0")) {
				selectedRComponent = rBlock.getDivs().get(0).getrComponents().stream()
						.filter(rcomp -> rcomp.getOrder() == orderInt).findAny().orElse(null);
				if (parentIdInt != null) {
					selectedRComponent = selectedRComponent.getChildren().get(parentIdInt);
				}
				selectedRComponent.setBlockId(Long.valueOf(blockId));
				position = "left";
			} else if (columnId.equals("1")) {
				selectedRComponent = rBlock.getDivs().get(1).getrComponents().stream()
						.filter(rcomp -> rcomp.getOrder() == orderInt).findAny().orElse(null);
				if (parentIdInt != null) {
					selectedRComponent = selectedRComponent.getChildren().get(parentIdInt);
				}
				selectedRComponent.setBlockId(Long.valueOf(blockId));
				position = "right";
			} else if (columnId.equals("2")) {
				selectedRComponent = rBlock.getDivs().get(2).getrComponents().stream()
						.filter(rcomp -> rcomp.getOrder() == orderInt).findAny().orElse(null);
				if (parentIdInt != null) {
					selectedRComponent = selectedRComponent.getChildren().get(parentIdInt);
				}
				selectedRComponent.setBlockId(Long.valueOf(blockId));
				position = "right";
			} else {
				return;
			}

			selectedRComponent.setColumn(columnInt);
			selectedRComponent.setOrder(orderInt);
			selectedRComponent.setParentId(parentIdInt);
		}
	}

	private RComponent selectedRComponent;

	public RComponent getSelectedRComponent() {
		return selectedRComponent;
	}

	public void setSelectedRComponent(RComponent selectedRComponent) {
		this.selectedRComponent = selectedRComponent;
	}

	public List<RComponent> getrComp_col_1() {
		return rComp_col_1;
	}

	public void setrComp_col_1(List<RComponent> rComp_col_1) {
		this.rComp_col_1 = rComp_col_1;
	}

	public List<RComponent> getrComp_col_2() {
		return rComp_col_2;
	}

	public void setrComp_col_2(List<RComponent> rComp_col_2) {
		this.rComp_col_2 = rComp_col_2;
	}

	public List<RComponent> getrComp_col_0() {
		return rComp_col_0;
	}

	public void setrComp_col_0(List<RComponent> rComp_col_0) {
		this.rComp_col_0 = rComp_col_0;
	}

	// =========================================================

}