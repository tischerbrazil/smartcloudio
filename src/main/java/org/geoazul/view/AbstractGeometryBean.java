package org.geoazul.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import org.example.kickoff.business.exception.EntityNotFoundException;
import org.geoazul.ecommerce.model.Item;
import org.geoazul.model.basic.AbstractGeometry;
import org.geoazul.model.basic.Comp;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.basic.LayerCategory;
import org.geoazul.model.basic.Polygon;
import org.geoazul.model.basic.Comp.IconcategoryId;
import org.geoazul.model.basic.properties.Field;
import org.geoazul.model.website.UrlMenu;
import org.geoazul.model.website.UrlPost;
import org.geoazul.model.website.UrlPostItem;
import org.hibernate.Session;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.utils.reflect.Getter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.primefaces.refact.BookProperty;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jsonb.JacksonUtil;

/**
 * CDI AbstractGeometry
 * <p/>
 * This class provides CRUD functionality for all AbstractGeometry entities. It
 * focuses purely on Java EE 8 standards (e.g. <tt>&#64;RequestScoped</tt> for non
 * state management, <tt>Hibernate</tt> for persistence,
 * <tt>CriteriaBuilder</tt> for searches) rather than introducing a CRUD
 * framework or custom base class.
 */

@Named
@ViewScoped
public class AbstractGeometryBean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	@Inject
	@Param(pathIndex = 0)
	private Long gcmid;

	@Inject
	@Param(pathIndex = 1)
	private Long id;

		
	
	 public AbstractGeometry getAbstractGeometry(Long id) {
			try {
				this.abstractGeometry = findAbstractGeometryByID(id);
				beanMethod();
			} catch (Exception ex) {
			}
			return this.abstractGeometry;
		}
	 
	 
	 
	 
	@PostConstruct
	public void init() {
	   
    
        if (id != null ) {
       
        
        	try {
			
				this.abstractGeometry = findAbstractGeometryByID(id);
				beanMethod();
			} catch (Exception ex) {
				ex.printStackTrace();
				FacesContext context = FacesContext.getCurrentInstance();
				String url = context.getExternalContext().getRequestContextPath();
				try {
					context.getExternalContext().redirect(url + "/errorpages/404.xhtml");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
        }
    }
	
	
	
	
	private Map<Getter<AbstractGeometry>, Object> mapSelectedCriteria2() {
		
		Map<Getter<AbstractGeometry>, Object> moduleCriteria = new HashMap<>();
		//moduleCriteria.put(Layer::getId, "1");	
		
		moduleCriteria.put(AbstractGeometry::getLayer, findLayerById() );

		return moduleCriteria;
	}
	
	public Layer findLayerById() {
		try {
			return entityManager.find(Layer.class, layerId);
		} catch (Exception ex) {
			return null;
		}
	}
	
	private Integer layerId;
		
	public Integer getLayerId() {
		return layerId;
	}

	public void setLayerId(Integer layerId) {
		this.layerId = layerId;
	}
	
	@Inject
	EntityManager entityManager;

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





	private Comp layer;

	public Comp getLayer() {
		return this.layer;
	}

	public void setLayer(Comp layer) {
		this.layer = layer;
	}

	private String layer_id;

	public String getLayer_id() {
		return layer_id;
	}

	public void setLayer_id(String layer_id) {
		this.layer_id = layer_id;
	}

	private AbstractGeometry abstractGeometry;

	public AbstractGeometry getAbstractGeometry() {
		return this.abstractGeometry;
	}

	public void setAbstractGeometry(AbstractGeometry abstractGeometry) {
		this.abstractGeometry = abstractGeometry;
	}

	public void editAction(Long id) {
		this.abstractGeometry = findAbstractGeometryByID(id);
	}
	
	
	public Layer getUrlLayer(Integer layerId) {
		try {
			this.layer = entityManager.find(Layer.class, layerId);
			return entityManager.find(Layer.class, layerId);
		} catch (Exception ex) {
			return null;
		}
	}
	
	
	
	
	
	public void retrieve44() {
		if (this.id == null) {
			
			this.abstractGeometry = this.example;
			// this.layer = findLayerByUuid(getLayer_id());
			// beanMethodNew();
		} else {
			try {
				this.abstractGeometry = findAbstractGeometryByID(this.id);
				beanMethod();
			} catch (Exception ex) {
				FacesContext context = FacesContext.getCurrentInstance();
				String url = context.getExternalContext().getRequestContextPath();
				try {
					context.getExternalContext().redirect(url + "/errorpages/404.xhtml");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	public Layer findLayerByUuid(Long uuid) {
		try {
			TypedQuery<Layer> queryApp = entityManager.createNamedQuery(Layer.LAYER_ID, Layer.class);
			queryApp.setParameter("id", id);
			return queryApp.getSingleResult();
		} catch (Exception ex) {
			return null;
		}
	}

	public AbstractGeometry findAbstractGeometryByID(Long objectId) {
		try {
			
			TypedQuery<AbstractGeometry> queryAbs = entityManager.createNamedQuery(AbstractGeometry.SURFACE_ID,
					AbstractGeometry.class);
			queryAbs.setParameter("id", objectId);
			this.abstractGeometry =  queryAbs.getSingleResult();
		



			return this.abstractGeometry;
		} catch (Exception ex) {
			return null;
		}
	}

	public AbstractGeometry findById(Long id) {
		try {
			return entityManager.find(AbstractGeometry.class, id);
		} catch (Exception ex) {
			return null;
		}
	}

	public Layer findLayerById(Integer id) {
		try {
			return entityManager.find(Layer.class, id);
		} catch (Exception ex) {
			return null;
		}
	}

	public LayerCategory findLayerCategoryById(String id) {
		try {
			return entityManager.find(LayerCategory.class, id);
		} catch (Exception ex) {
			return null;
		}
	}

	/*
	 * Support updating and deleting AbstractGeometry entities
	 */
	@Transactional
	public String update() {
		try {
			if (this.id == null) {
				try {
					entityManager.persist(this.abstractGeometry);
					entityManager.flush();
				} catch (Exception e) {

				}

				return "search?faces-redirect=true";
			} else {
				try {
					entityManager.merge(this.abstractGeometry);
					entityManager.flush();
				} catch (Exception e) {

				}

				return "view?faces-redirect=true&id=" + this.abstractGeometry.getId();

			}

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return null;
		}
	}

	@Transactional
	public String delete() {
		try {
			AbstractGeometry deletableEntity = findAbstractGeometryByID(this.getId());
			entityManager.remove(deletableEntity);
			entityManager.flush();
			return "geometries?faces-redirect=true&id=" + this.getLayer().getId();
		} catch (Exception e) {
			return null;
		}

	}

	/*
	 * Support searching AbstractGeometry entities with pagination
	 */

	private int page;
	private long count;
	private List<AbstractGeometry> pageItems;

	private AbstractGeometry example;

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public AbstractGeometry getExample() {
		return this.example;
	}

	public void setExample(AbstractGeometry example) {
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
			Root<AbstractGeometry> root = countCriteria.from(AbstractGeometry.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();
			// Populate this.pageItems
			CriteriaQuery<AbstractGeometry> criteria = builder.createQuery(AbstractGeometry.class);
			root = criteria.from(AbstractGeometry.class);
			TypedQuery<AbstractGeometry> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicates(root, entityManager)));
			query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
			this.pageItems = query.getResultList();
		} catch (Exception ex) {
		}
	}

	private Predicate[] getSearchPredicates(Root<AbstractGeometry> root, EntityManager session) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		String nome = this.example.getNome();
		if (nome != null && !"".equals(nome)) {
			predicatesList.add(builder.like(builder.lower(root.<String>get("nome")), '%' + nome.toLowerCase() + '%'));
		}
		Short situacao = this.example.getSituacao();
		if (situacao != null && situacao.intValue() != 0) {
			predicatesList.add(builder.equal(root.get("situacao"), situacao));
		}
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	private Layer selectedLayer;

	public Layer getSelectedLayer() {
		return selectedLayer;
	}

	public void setSelectedLayer(Layer selectedLayer) {
		this.selectedLayer = selectedLayer;
	}

	public List<AbstractGeometry> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back AbstractGeometry entities (e.g. from inside
	 * an HtmlSelectOneMenu)
	 */

	public List<AbstractGeometry> getAll() {
		try {
			CriteriaQuery<AbstractGeometry> criteria = entityManager.getCriteriaBuilder()
					.createQuery(AbstractGeometry.class);
			return entityManager.createQuery(criteria.select(criteria.from(AbstractGeometry.class))).getResultList();
		} catch (Exception ex) {
			return null;
		}
	}

	private Long objectId;

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) { // FIXME
		this.abstractGeometry = findAbstractGeometryByID(objectId);
		this.objectId = objectId;
	}

	// ==========================

	public void beanMethod() {
		
		
		
		
		
		

		model = new DynaFormModel();

		DynaFormRow row = model.createRegularRow();

		Long idCategory = null;

		for (LayerCategory layerC : this.getAbstractGeometry().getCategories()) {
			idCategory = layerC.getId(); // FIXME
		}

		try {
			if (this.getAbstractGeometry().getLayer().getLayerCategories().size() > 0) {
				DynaFormLabel label410 = row.addLabel("Categoria");
				DynaFormControl control420 = row.addControl(new BookProperty("Categoria", idCategory, false),
						"selectcat");
				label410.setForControl(control420);
			}
		} catch (NullPointerException np) {
		}

		row = model.createRegularRow();
		DynaFormLabel label11 = row.addLabel("Nome");
		DynaFormControl control12 = row.addControl(new BookProperty("Nome", this.getAbstractGeometry().getNome(), true),
				"input", 3, 1);
		label11.setForControl(control12);

		row = model.createRegularRow();
		DynaFormLabel label33 = row.addLabel("Ativo");
		DynaFormControl control333 = row.addControl(
				new BookProperty("Ativo", this.getAbstractGeometry().getEnabled(), true), "booleanchoice", 3, 1);
		label33.setForControl(control333);

		if (this.getAbstractGeometry().getLayer().getIconcategory() == IconcategoryId.ENTITY) {
			row = model.createRegularRow();
			DynaFormLabel label111 = row.addLabel("Icon");
			DynaFormControl control121 = row.addControl(
					new BookProperty("Icon", this.getAbstractGeometry().getIconflag(), true), "input", 3, 1);
			label11.setForControl(control121);
		}
		ObjectNode node = null;

		try {
			node = new ObjectMapper().readValue(this.getAbstractGeometry().getStrings().toString(), ObjectNode.class);
		} catch (IOException e) {
		} catch (NullPointerException np) {
		}
		
		
		// CREATE FIELD DYNAMICS AND POPULATE THIS
		int stringKeyIndex = 0;
		Object[] objectValue = new Object[this.getAbstractGeometry().getLayer().getFields().size()];
		for (Field columnKey : this.getAbstractGeometry().getLayer().getFields()) {
			switch (columnKey.getTypeData()) {

			case "string":
				try {
					objectValue[stringKeyIndex] = node.get(columnKey.getName()).textValue();
				} catch (NullPointerException np) {
					objectValue[stringKeyIndex] = "";
				}
				break;

			case "double":
				try {
					objectValue[stringKeyIndex] = node.get(columnKey.getName()).asDouble();

					System.out
							.println("-----------------------------DOUBLE" + node.get(columnKey.getName()).asDouble());

				} catch (NullPointerException np) {
					objectValue[stringKeyIndex] = false;
				}
				break;

			case "boolean":
				try {
					objectValue[stringKeyIndex] = node.get(columnKey.getName()).asBoolean();
				} catch (NullPointerException np) {
					objectValue[stringKeyIndex] = false;
				}
				break;
			case "text":
				try {
					objectValue[stringKeyIndex] = node.get(columnKey.getName()).textValue();
				} catch (NullPointerException np) {
					objectValue[stringKeyIndex] = "";
				}
				break;
			case "image":
				try {
					objectValue[stringKeyIndex] = node.get(columnKey.getName()).textValue();
				} catch (NullPointerException np) {
					objectValue[stringKeyIndex] = "";
				}
				break;
			}
			row = model.createRegularRow();
			row = rowcreate(row, columnKey.getFieldControl().getType(), columnKey.getName(), columnKey.getName(),
					objectValue[stringKeyIndex]);
			stringKeyIndex++;
		}
	}

	/*
	 * Support objectId set on Ajax request method
	 */
	public void beanMethodAbs() {

		Boolean hasGeometry = false;

		org.geoazul.model.basic.Polygon polygonRet = (Polygon) this.abstractGeometry;

		try {
			polygonRet.getGeometry().toString();
			hasGeometry = true;
		} catch (NullPointerException np) {
		}
		if (hasGeometry) {

			List<Field> fieldsProperties = this.getAbstractGeometry().getLayer().getFields();

			model = new DynaFormModel();

			// 1. row
			// DynaFormRow row = model.createRegularRow();

			// row.addControl(new BookProperty("Hidden33", true), "hidden");

			// 2. row

			DynaFormRow row = model.createRegularRow();

			List<LayerCategory> layerCat = this.getAbstractGeometry().getCategories();
			Long idCategory = null;
			for (LayerCategory layerC : layerCat) {
				idCategory = layerC.getId();
			}

			try {
				Integer numberCat = this.getAbstractGeometry().getLayer().getLayerCategories().size();
				if (numberCat > 0) {
					DynaFormLabel label410 = row.addLabel("Categoria");
					DynaFormControl control420 = row.addControl(new BookProperty("Categoria", idCategory, false),
							"selectcat");
					label410.setForControl(control420);
				}
			} catch (NullPointerException np) {
			}

			row = model.createRegularRow();
			DynaFormLabel label11 = row.addLabel("Nome");
			DynaFormControl control12 = row
					.addControl(new BookProperty("Nome", this.getAbstractGeometry().getNome(), true), "input", 3, 1);
			label11.setForControl(control12);


			if (this.getAbstractGeometry().getLayer().getIconcategory() == IconcategoryId.ENTITY) {

				row = model.createRegularRow();
				DynaFormLabel label111 = row.addLabel("Icon");
				DynaFormControl control121 = row.addControl(
						new BookProperty("Icon", this.getAbstractGeometry().getIconflag(), true), "input", 3, 1);
				label11.setForControl(control121);
			}

			ObjectNode node = null;
			try {
				node = new ObjectMapper().readValue(this.getAbstractGeometry().getStrings().toString(),
						ObjectNode.class);
			} catch (IOException e) {
			} catch (NullPointerException np) {
			}

			// CREATE FIELD DYNAMICS AND POPULATE THIS
			int stringKeyIndex = 0;
			Object[] objectValue = new Object[this.getAbstractGeometry().getLayer().getFields().size()];
			for (Field columnKey : fieldsProperties) {

				switch (columnKey.getTypeData()) {

				case "string":
					try {
						objectValue[stringKeyIndex] = node.get(columnKey.getName()).textValue();
					} catch (NullPointerException np) {
						objectValue[stringKeyIndex] = "";
					}
					break;

				case "boolean":
					try {
						objectValue[stringKeyIndex] = node.get(columnKey.getName()).asBoolean();
					} catch (NullPointerException np) {
						objectValue[stringKeyIndex] = false;
					}
					break;
				}
				row = model.createRegularRow();
				row = rowcreate(row, columnKey.getFieldControl().getType(), columnKey.getName(), columnKey.getName(),
						objectValue[stringKeyIndex]);
				stringKeyIndex++;
			}

		}

		this.setLayer(this.getAbstractGeometry().getLayer());

	}

	/*
	 * Support objectId set on Ajax request method
	 */
	public void beanMethodNew() {

		List<Field> fieldsProperties = this.getLayer().getFields();

		model = new DynaFormModel();

		// 1. row
		DynaFormRow row = model.createRegularRow();

		// row.addControl(new BookProperty("Hidden33", true), "hidden");

		// 2. row

		// row = model.createRegularRow();

		try {
			Long laycatid = this.getLayer().getLayerCat().getId();
			if (laycatid != null) {
				String nameLayer = this.getAbstractGeometry().getLayer().getLayerCat().getName();
				DynaFormLabel label41 = row.addLabel(nameLayer);
				DynaFormControl control42 = row.addControl(new BookProperty(nameLayer, null, false), "select");
				label41.setForControl(control42);
			}
		} catch (NullPointerException np) {
		}

		try {
			Integer numberCat = this.getLayer().getLayerCategories().size();
			if (numberCat > 0) {
				DynaFormLabel label410 = row.addLabel("Categoria");
				DynaFormControl control420 = row.addControl(new BookProperty("Categoria", null, false), "selectcat");
				label410.setForControl(control420);
			}
		} catch (NullPointerException np) {
		}

		row = model.createRegularRow();
		DynaFormLabel label11 = row.addLabel("Nome");
		DynaFormControl control12 = row.addControl(new BookProperty("Nome", "", true), "input", 3, 1);
		label11.setForControl(control12);

		// CREATE FIELD DYNAMICS AND POPULATE THIS
		int stringKeyIndex = 0;
		Object[] objectValue = new Object[this.getLayer().getFields().size()];
		for (Field columnKey : fieldsProperties) {

			switch (columnKey.getTypeData()) {

			case "string":
				try {
					objectValue[stringKeyIndex] = "";
				} catch (NullPointerException np) {
					objectValue[stringKeyIndex] = "";
				}
				break;

			case "boolean":
				try {
					objectValue[stringKeyIndex] = false;
				} catch (NullPointerException np) {
					objectValue[stringKeyIndex] = false;
				}
				break;
			}
			row = model.createRegularRow();
			row = rowcreate(row, columnKey.getFieldControl().getType(), columnKey.getName(), columnKey.getName(),
					objectValue[stringKeyIndex]);
			stringKeyIndex++;
		}
		this.setLayer(this.getLayer());
	}

	public static void main(String args[]) {

		// String array
		String[] strArray = new String[] { "Java", "String", "Array", "To", "String", "Example" };

		/*
		 * There are several in which we can convert String array to String.
		 */

		/*
		 * 1. Using Arrays.toString method This method returns string like [element1,
		 * element2..]
		 */

		String str1 = Arrays.toString(strArray);

		// replace starting "[" and ending "]" and ","
		str1 = str1.substring(1, str1.length() - 1).replaceAll(",", "");

		/*
		 * 2. Using Arrays.asList method followed by toString. This method also returns
		 * string like [element1, element2..]
		 */

		String str2 = Arrays.asList(strArray).toString();
		// replace starting "[" and ending "]" and ","
		str2 = str2.substring(1, str2.length() - 1).replaceAll(",", "");

		/*
		 * PLEASE NOTE that if the any of the array elements contain ",", it will be
		 * replaced too. So above both methods does not work 100%.
		 */

		// 3. Using StringBuffer.append method
		StringBuffer sbf = new StringBuffer();

		if (strArray.length > 0) {

			sbf.append(strArray[0]);
			for (int i = 1; i < strArray.length; i++) {
				sbf.append(" ").append(strArray[i]);
			}

		}

	}

	public void deleteMethod() {
	}

	private DynaFormRow rowcreate(DynaFormRow row, String typeField, String labelField, String nameField,
			Object valueField) {
		DynaFormLabel labelDF;
		DynaFormControl controlDF;

		switch (typeField) {

		case "textEditor":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, valueField, false), "texteditor", 3, 1);
			labelDF.setForControl(controlDF);
			break;

		case "textImg":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, valueField, false), "img", 3, 1);
			labelDF.setForControl(controlDF);
			break;

		case "fileUpload":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, valueField, false), "fileupload", 3, 1);
			labelDF.setForControl(controlDF);
			break;

		case "calendarDate":

			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, LocalDate.now(), false), "calendar", 3, 1);
			labelDF.setForControl(controlDF);
			break;

		case "outputText":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, valueField, false), "input", 3, 1);
			labelDF.setForControl(controlDF);
			break;

		case "selectBooleanCheckbox":

			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, valueField, false), "booleanchoice", 1, 1);
			labelDF.setForControl(controlDF);
			break;

		}

		return row;
	};

	// +++++++++++++++++++++++++++++++++++ DINA FORM

	private DynaFormModel model;

	private static List<SelectItem> LANGUAGES = new ArrayList<SelectItem>();

	public DynaFormModel getModel() {
		return model;
	}

	public List<BookProperty> getBookProperties() {
		if (model == null) {
			return null;
		}

		List<BookProperty> bookProperties = new ArrayList<BookProperty>();
		for (DynaFormControl dynaFormControl : model.getControls()) {
			bookProperties.add((BookProperty) dynaFormControl.getData());
		}

		return bookProperties;
	}

	@Transactional
	public String deleteObject() {

		try {
		

			AbstractGeometry deleteObj = entityManager.find(AbstractGeometry.class, this.id);
			Long appId = deleteObj.getLayer().getApplicationEntity().getId();
			Long layerId = deleteObj.getLayer().getId();
			entityManager.remove(deleteObj);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Ponto Excluido!"));
			return "layer_view?faces-redirect=true&id=" + appId + "&layer_id=" + layerId;

		} catch (Exception e) {
			
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Erro!", "Ocorreu um erro inesperado ao excluir o registro!"));
			return null;
		}

	}

	
	public String submitForm2() {
		FacesMessage.Severity sev = FacesContext.getCurrentInstance().getMaximumSeverity();
		boolean hasErrors = (sev != null && (FacesMessage.SEVERITY_ERROR.compareTo(sev) >= 0));

		if (!FacesContext.getCurrentInstance().isPostback()) {
			PrimeFaces.current().executeScript("alert('This onload script is added from backing bean.')");
		}

		return null;
	}

	@Transactional
	public String submitForm() {

		
		try {
			

			FacesMessage.Severity sev = FacesContext.getCurrentInstance().getMaximumSeverity();
			boolean hasErrors = (sev != null && (FacesMessage.SEVERITY_ERROR.compareTo(sev) >= 0));

			if (!FacesContext.getCurrentInstance().isPostback()) {
				PrimeFaces.current().executeScript("alert('This onload script is added from backing bean.')");
			}


			// ------------------------------------------------------------------------

			Long catId = null;
			try {
				catId = this.abstractGeometry.getLayer().getLayerCat().getId();

			} catch (NullPointerException np) {
				// Not record found - Out of area from polygons in category. Nothing here
			}

			String valuex;
			List<BookProperty> listBookProperty = this.getBookProperties();

			String jsonb = "{";
			int fieldIndex = 1;
			int fieldSize = listBookProperty.size();
			String nomeCatLayer = null;

			/**
			 * Find when Layer have category manager from another polygon layer
			 */

			try {
				nomeCatLayer = this.getAbstractGeometry().getLayer().getLayerCat().getName();
			} catch (NullPointerException np) {
				// not have category manager
			}

			Integer numberFields = this.abstractGeometry.getLayer().getFields().size();


			for (BookProperty bookProperty : listBookProperty) {


				if (bookProperty.getName() == "Categoria") {


					LayerCategory layerCat = entityManager.find(LayerCategory.class, bookProperty.getValue());
					List<LayerCategory> layerCatList = this.abstractGeometry.getCategories();

					if (layerCatList.size() == 0) {
						this.abstractGeometry.getCategories().add(layerCat);
						if (this.getAbstractGeometry().getLayer().getIconcategory() == IconcategoryId.CATEGORY) {
							this.abstractGeometry.setIconflag(layerCat.getIconflag());
						}
					} else {

						for (LayerCategory ageom : layerCatList) {
							if (!ageom.equals(layerCat)) {
								this.abstractGeometry.getCategories().remove(ageom);
								this.abstractGeometry.getCategories().add(layerCat);

								if (this.getAbstractGeometry().getLayer()
										.getIconcategory() == IconcategoryId.CATEGORY) {
									this.abstractGeometry.setIconflag(layerCat.getIconflag());
								}
							}
						}

					}

					/**
					 * Process here when Layer have category manager from another polygon layer on
					 * bookProperty result values
					 */

				} else if (bookProperty.getName() == "Nome") {
					this.abstractGeometry.setNome(bookProperty.getValue().toString());
					// postNew.setTitleLocale("pt_BR", bookProperty.getValue().toString());
				} else if (bookProperty.getName() == "Ativo") {

					Boolean ativo = Boolean.valueOf(bookProperty.getValue().toString());
					this.abstractGeometry.setEnabled(ativo);
					// postNew.setTitleLocale("pt_BR", bookProperty.getValue().toString());
				} else if (bookProperty.getName() == "Icon") {
					this.abstractGeometry.setIconflag(bookProperty.getValue().toString());

				} else {

					if (numberFields > 0) {

						if (bookProperty.getValue() == null) {
							valuex = "";
						} else {
							valuex = bookProperty.getValue().toString();
							valuex = valuex.replace("\"", "\\\"");
						}

						if (fieldIndex < fieldSize) {
							jsonb = jsonb + "\"" + bookProperty.getName() + "\":\"" + valuex + "\",";
						} else {
							jsonb = jsonb + "\"" + bookProperty.getName() + "\":\"" + valuex + "\"";
						}

					}
				}

				fieldIndex++;
			}
			jsonb = jsonb + "}";
			if (numberFields > 0) {

				String jsonb2 = removeControlCharFull(jsonb);
				this.abstractGeometry.setStrings(JacksonUtil.toJsonNode(jsonb2));
			} else {
				this.abstractGeometry.setStrings(JacksonUtil.toJsonNode("{}"));
			}
			entityManager.merge(this.abstractGeometry);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Registro Alterado!"));
			entityManager.flush();
			return "content_view?faces-redirect=true&objectId=" + this.id;

		} catch (Exception e) {
			return null;
		}

	}

	public static String removeNonAscii(String str) {
		return str.replaceAll("[^\\x00-\\x7F]", "");
	}

	public static String removeNonPrintable(String str) // All Control Char
	{
		return str.replaceAll("[\\p{C}]", "");
	}

	public static String removeSomeControlChar(String str) // Some Control Char
	{
		return str.replaceAll("[\\p{Cntrl}\\p{Cc}\\p{Cf}\\p{Co}\\p{Cn}]", "");
	}

	public static String removeControlCharFull(String str) {
		return removeNonPrintable(str).replaceAll("[\\r\\n\\t]", "");
	}

	public List<SelectItem> getLanguages() {
		List<SelectItem> categorias = new ArrayList<SelectItem>();
		try {
			Layer layerCat = this.getAbstractGeometry().getLayer().getLayerCat();
			for (AbstractGeometry pol : layerCat.getGeometrias()) {
				categorias.add(new SelectItem(pol.getId(), pol.getNome()));
			}
		} catch (NullPointerException np) {
			// PROPERTIES IS BULL null FIXME
		}

		return categorias;
	}

	public List<SelectItem> getLanguagesCat() {
		List<SelectItem> categoriasCat = new ArrayList<SelectItem>();
		for (LayerCategory layerCategory : this.abstractGeometry.getLayer().getLayerCategories()) {
			categoriasCat.add(new SelectItem(layerCategory.getId(), layerCategory.getCategoryname()));
		}
		return categoriasCat;
	}

	public List<SelectItem> getLanguages2() {
		if (LANGUAGES.isEmpty()) {
			LANGUAGES.add(new SelectItem("en_US", "English"));
			LANGUAGES.add(new SelectItem("pt_BR", "Brazil"));
		}

		return LANGUAGES;
	}

	private String locale = "pt_BR";

	private static Map<String, Object> countries;

	static {
		countries = new LinkedHashMap<String, Object>();
		countries.put("Português", new Locale("pt", "BR"));
		countries.put("English", new Locale("en", "US"));
		countries.put("Spanish", new Locale("es", "ES"));
	}

	public Map<String, Object> getCountries() {
		return countries;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
		// refresh(); //REFRESH LANGUAGE
	}

	public void onSelect2(SelectEvent event) {
		String selectedLayer2 = event.getObject().toString();

	}

	// value change event listener
	public void localeChanged(ValueChangeEvent e) {
		String newLocaleValue = e.getNewValue().toString();
		for (Map.Entry<String, Object> entry : countries.entrySet()) {
			if (entry.getValue().toString().equals(newLocaleValue)) {
				FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());
			}
		}
	}

	public List<AbstractGeometry> getGeometrias() {

		List<AbstractGeometry> geometrias1 = new ArrayList<AbstractGeometry>();

		return null;
	}

	private List<AbstractGeometry> pageItemsAG;

	private LayerCategory layerCategory;
	private String category;

	public LayerCategory getLayerCategory() {
		return layerCategory;
	}

	public void setLayerCategory(LayerCategory layerCategory) {
		this.layerCategory = layerCategory;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		LayerCategory layerCat = findLayerCategoryById(category);
		this.setLayerCategory(layerCat);
		this.category = category;
	}

	private List<Map<String, ColumnModel>> tableDataShape;
	private List<ColumnModel> columns;

	public List<Map<String, ColumnModel>> getTableDataShape() {
	
		if (this.abstractGeometry != null) {


			tableDataShape = new ArrayList<Map<String, ColumnModel>>();

			tableHeaderNames = new ArrayList<ColumnModel>();
			
			tableHeaderNames.add(new ColumnModel("Id", "Id"));
			tableHeaderNames.add(new ColumnModel("Nome", "Nome"));
			tableHeaderNames.add(new ColumnModel("Camada", "Camada"));
			tableHeaderNames.add(new ColumnModel("Icon", "Icon"));
			tableHeaderNames.add(new ColumnModel("Ativo", "Ativo"));

			List<Field> fieldsProperties = this.abstractGeometry.getLayer().getFields();
			
			for (Field columnKey : fieldsProperties) {
				tableHeaderNames.add(new ColumnModel(columnKey.getName(), columnKey.getName()));
			}
			// Generate table header.


			ObjectNode node = null;

			Map<String, ColumnModel> playlist = new HashMap<String, ColumnModel>();

			playlist.put(tableHeaderNames.get(0).key,
					new ColumnModel(tableHeaderNames.get(0).key, this.abstractGeometry.getId().toString()));

			playlist.put(tableHeaderNames.get(1).key,
					new ColumnModel(tableHeaderNames.get(1).key, this.abstractGeometry.getNome()));

			playlist.put(tableHeaderNames.get(2).key,
					new ColumnModel(tableHeaderNames.get(2).key, this.abstractGeometry.getLayer().layerString()));

			playlist.put(tableHeaderNames.get(3).key,
					new ColumnModel(tableHeaderNames.get(3).key, this.abstractGeometry.getIconflag()));

			try {
				node = new ObjectMapper().readValue(this.abstractGeometry.getStrings().toString(), ObjectNode.class);
				int stringKeyIndex = 5;
				for (Field columnKey : fieldsProperties) {

					playlist.put(tableHeaderNames.get(stringKeyIndex).key, new ColumnModel(
							tableHeaderNames.get(stringKeyIndex).key, node.get(columnKey.getName()).textValue()));

					stringKeyIndex++;
				}

			} catch (IOException e) {
				// JSON WRAPPER FAILDED FIXME
			} catch (NullPointerException np) {
				// PROPERTIES IS BULL null FIXME
			}


			tableDataShape.add(playlist);


			columns = new ArrayList<ColumnModel>();

			// for(String columnKey : columnKeys) {
			// String key = columnKey.trim();

			// if(VALID_COLUMN_KEYS.contains(key)) {
			columns.add(new ColumnModel("Id", "Id"));

			columns.add(new ColumnModel("Nome", "Nome"));
			columns.add(new ColumnModel("Camada", "Camada"));
			columns.add(new ColumnModel("Icon", "Icon"));

			for (Field columnKey : fieldsProperties) {
				columns.add(new ColumnModel(columnKey.getName(), columnKey.getName()));
			}

			this.setTableHeaderNames(columns);

			return tableDataShape;
			// }else{
			// return null;
			// }

		} else {
			return null;
		}
	}

	// ----------------------------------------------------------------------------------------

	private List<Map<String, ColumnModel>> tableDataGeometrias;

	public List<Map<String, ColumnModel>> getTableDataGeometrias() {

		if (this.abstractGeometry != null) {

			tableDataGeometrias = new ArrayList<Map<String, ColumnModel>>();

			tableHeaderNames = new ArrayList<ColumnModel>();
			tableHeaderNames.add(new ColumnModel("Id", "Id"));
			tableHeaderNames.add(new ColumnModel("Camada", "Camada"));
			tableHeaderNames.add(new ColumnModel("Nome", "Nome"));
			tableHeaderNames.add(new ColumnModel("Categorias", "Categorias"));
			tableHeaderNames.add(new ColumnModel("Icon", "Icon"));
			tableHeaderNames.add(new ColumnModel("Ativo", "Ativo"));
			// Generate table header.


			

			columns = new ArrayList<ColumnModel>();

			// for(String columnKey : columnKeys) {
			// String key = columnKey.trim();

			// if(VALID_COLUMN_KEYS.contains(key)) {
			columns.add(new ColumnModel("Id", "Id"));
			columns.add(new ColumnModel("Camada", "Camada"));
			columns.add(new ColumnModel("Nome", "Nome"));
			columns.add(new ColumnModel("Categorias", "Categorias"));
			columns.add(new ColumnModel("Icon", "Icon"));


			

			this.setTableHeaderNames(columns);


			return tableDataGeometrias;
		} else {
			return null;
		}
	}

	private List<ColumnModel> tableHeaderNames;

	public void setTableHeaderNames(List<ColumnModel> tableHeaderNames) {
		this.tableHeaderNames = tableHeaderNames;
	}

	public List<ColumnModel> getTableHeaderNames() {
		return tableHeaderNames;
	}

	static public class ColumnModel implements Serializable {

		private String key;
		private String value;

		public ColumnModel(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public String getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	}

	// --------------------------------------------------------------------------------------------------------

}
