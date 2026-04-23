package org.geoazul.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.faces.model.SelectItem;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.utils.reflect.Getter;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import jakarta.transaction.Transactional;
import org.geoazul.model.app.ApplicationEntity;
import org.geoazul.model.basic.AbstractGeometry;
import org.geoazul.model.basic.Comp.IconcategoryId;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.basic.LayerCategory;
import org.geoazul.model.basic.LayerPoint;
import org.geoazul.model.basic.properties.Field;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jsonb.JacksonUtil;


import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.refact.BookProperty;
import org.geoazul.model.basic.Point;
/**
 * 
 */

@Named
@ViewScoped
public class LayerBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	
    private MapModel simpleModel;
    
   

    private Marker marker;
	
		

		
    public MapModel getSimpleModel() {

    	 return simpleModel;
	
}

    public MapModel getSimpleModel2(Long layerid) {
    	
    		
    		
    		Layer dd =findById(layerid);
    		
    		
    		
    		 LatLng coordenadas = null;
    		    for (AbstractGeometry geo : this.layer.getGeometrias() ) {
    		   
    		    
    		    	
    		    	
    		    }
    		
    		
    		 simpleModel = new DefaultMapModel();

    	        //RO 
    	        LatLng coord1 = new LatLng(-12.7341, -60.1446);
    	        LatLng coord2= new LatLng(-10.8808, -61.9419);
    	        LatLng coord3 = new LatLng(-8.76183, -63.9020);
    	        
    	        //AC
    	        LatLng coord4 = new LatLng(-8.76183, -63.9020);
    	        
    	        
    	        
    	        
    	       
    	        simpleModel.addOverlay(new Marker(coord1, "Vilhena-RO", "konyaalti.png", "konyaalti.png", "https://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
    	        simpleModel.addOverlay(new Marker(coord2, "Ji-Paraná-RO", "konyaalti.png", "konyaalti.png", "https://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
    	        simpleModel.addOverlay(new Marker(coord3, "Porto Velho-RO", "konyaalti.png", "konyaalti.png", "https://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
    	        
    	        simpleModel.addOverlay(new Marker(coord4, "Rio Branco-AC", "konyaalti.png", "konyaalti.png", "https://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
    	        
    	       return simpleModel;
    		
    		
    	
       
    }
    
    public void onMarkerSelect(OverlaySelectEvent event) {
    	
        marker = (Marker) event.getOverlay();

        
      
        
    }

    public Marker getMarker() {
        return marker;
    }

	
	
	
	@PreDestroy
	public void preDestroy() {
	}	 
	
	@Inject
	EntityManager entityManager;
	
	public Layer getLayerById(Long layerid) {
		
		
		return findById(layerid);
		
		
	}
	
	public Layer getLayerByUuid(Long layerid) {   //fixme
		return findLayerByUuid(layerid);
	}

	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private String nome;

	public String getNome() {
		return this.nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	private String category_id;

	public String getCategory_id() {
		return this.category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}

	private Layer layer;

	public Layer getLayer() {
		return this.layer;
	}

	public void setLayer(Layer layer) {
		this.layer = layer;
	}

	public String create() {
		return "create?faces-redirect=true";
	}

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.id == null) {
			this.layer = this.example;
		} else {
			findLayerByUuid(getId());
		}
	}

	public Layer findLayerByUuid(Long uuid) {
		try {
			TypedQuery<Layer> queryAppLayer = entityManager.createNamedQuery(Layer.LAYER_ID, Layer.class);
			queryAppLayer.setParameter("id", id);
			this.layer = queryAppLayer.getSingleResult();
			return this.layer;
		} catch (Exception e) {
			return null;
		}
	}

	public Layer findById(Long id) {

		try {
			this.layer = entityManager.find(Layer.class, id);
			return this.layer;
		} catch (Exception e) {
			return null;	
		}
	}

	/*
	 * Support updating and deleting Layer entities
	 */

	@Transactional
	public String update() {
		try {
	
			if (this.id == null) {
				entityManager.persist(this.layer);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "Camada Inserida!"));

			} else {
				entityManager.merge(this.layer);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "Camada Alterada!"));
			}
			entityManager.flush();
			return "view?faces-redirect=true&id=" + this.layer.getId();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return "view?faces-redirect=true&id=" + this.layer.getId();
		}
	}

	@Transactional
	public String delete() {
		try {
			Layer deletableEntity = findLayerByUuid(getId());
			ApplicationEntity applicationEntity = (ApplicationEntity) deletableEntity.getApplicationEntity();

			entityManager.remove(deletableEntity);
			entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
				return "search?faces-redirect=true";
		} 
	}

	/*
	 * Support searching Layer entities with pagination
	 */

	private int page;
	private long count;
	private List<Layer> pageItems;

	private Layer example;

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Layer getExample() {
		return this.example;
	}

	public void setExample(Layer example) {
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
			Root<Layer> root = countCriteria.from(Layer.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();

			// Populate this.pageItems

			CriteriaQuery<Layer> criteria = builder.createQuery(Layer.class);
			root = criteria.from(Layer.class);
			TypedQuery<Layer> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicates(root, entityManager)));
			query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
			this.pageItems = query.getResultList();

		} catch (Exception e) {
			
		}
	}

	private Predicate[] getSearchPredicates(Root<Layer> root, EntityManager session) {

		@SuppressWarnings("unused")
		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Layer> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Layer entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Layer> getAll() {
		try {
			CriteriaQuery<Layer> criteria = entityManager.getCriteriaBuilder().createQuery(Layer.class);
			return entityManager.createQuery(criteria.select(criteria.from(Layer.class))).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	// private Layer add = new Layer();

	// public Layer getAdd() {
	// return this.add;
	// }

	// public Layer getAdded() {
	// Layer added = this.add;
	// this.add = new Layer();
	// return added;
	// }

	public List<SelectItem> getLanguages() {
		List<SelectItem> categorias = new ArrayList<SelectItem>();
		try {
			Layer layerCat = this.getAbstractGeometry().getLayer().getLayerCat();
			for (AbstractGeometry pol : layerCat.getGeometrias()) {
				categorias.add(new SelectItem(pol.getId(), pol.getNome()));
			}
		} catch (NullPointerException np) {
			// FIXME
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

	private String objectId;

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		try {
			this.abstractGeometry = entityManager.find(AbstractGeometry.class, objectId);
			this.objectId = objectId;
		} catch (Exception e) {
			
		}
	}

	/*
	 * Support objectId set on Ajax request method
	 */
	public void beanMethod() {
		String objectidCapture = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("objectid");

		
		try {
			
			this.abstractGeometry = entityManager.find(AbstractGeometry.class, objectidCapture);

		

			model = new DynaFormModel();

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

			try {

				if (this.getAbstractGeometry().getLayer() != null) {

					Long idLayerGeom = this.getAbstractGeometry().getLayer().getId();
					DynaFormLabel label9103 = row.addLabel("Camada");
					DynaFormControl control9203 = row.addControl(new BookProperty("Layer", idLayerGeom, false),
							"selectlayer");
					label9103.setForControl(control9203);

				} else {
					DynaFormLabel label9103 = row.addLabel("Camada");
					DynaFormControl control9203 = row.addControl(new BookProperty("Camada", "", false), "selectlayer");
					label9103.setForControl(control9203);
				}

			} catch (NullPointerException np) {
				DynaFormLabel label9103 = row.addLabel("Camada");
				DynaFormControl control9203 = row.addControl(new BookProperty("Camada", "", false), "selectlayer");
				label9103.setForControl(control9203);

			}


			if (this.getAbstractGeometry().getLayer().getLayerCat() != null) {

				try {
					if (this.getAbstractGeometry().getFather().getId() != null) {
						Long idFather = this.getAbstractGeometry().getFather().getId();
						DynaFormLabel label4103 = row
								.addLabel(this.getAbstractGeometry().getFather().getLayer().getName());
						DynaFormControl control4203 = row.addControl(new BookProperty("Father", idFather, false),
								"selectfather");
						label4103.setForControl(control4203);
					}

				} catch (NullPointerException np) {
					DynaFormLabel label4103 = row.addLabel("Father");
					DynaFormControl control4203 = row.addControl(new BookProperty("Father", "", false), "selectfather");
					label4103.setForControl(control4203);
				}

			}

			row = model.createRegularRow();
			DynaFormLabel label11 = row.addLabel("Nome");
			DynaFormControl control12 = row
					.addControl(new BookProperty("Nome", this.getAbstractGeometry().getNome(), true), "input", 3, 1);
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
				node = new ObjectMapper().readValue(this.getAbstractGeometry().getStrings().toString(),
						ObjectNode.class);
			} catch (IOException e) {
			} catch (NullPointerException np) {
			}
			// CREATE FIELD DYNAMICS AND POPULATE THIS
			int stringKeyIndex = 0;
			Object[] objectValue = new Object[this.getAbstractGeometry().getLayer().getFields().size()];

			List<Field> fieldsProperties = this.getAbstractGeometry().getLayer().getFields();

			for (Field columnKey : fieldsProperties) {
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

						if (objectValue[stringKeyIndex] == null || objectValue[stringKeyIndex] == "") {
							objectValue[stringKeyIndex] = "/resources/images/no_image.png";
						}

					} catch (NullPointerException np) {
						objectValue[stringKeyIndex] = "";
					}
					break;

				case "select":
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

		} catch (Exception ex) {


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

	// =============================================== REFACTORING THIS ANOTHER
	// TIMES

	private String cordinateNew;

	public String getCordinateNew() {
		return this.cordinateNew;
	}

	public void setCordinateNew(String cordinateNew) {
		this.cordinateNew = cordinateNew;
	}

	private String typeGeometry;

	public String getTypeGeometry() {
		return typeGeometry;
	}

	public void setTypeGeometry(String typeGeometry) {
		this.typeGeometry = typeGeometry;
	}

	private String tabela;

	public String getTabela() {
		return tabela;
	}

	public void setTabela(String tabela) {
		this.tabela = tabela;
	}

	private String scale;

	private String paper;

	private String sistcord;

	private String modelo;

	private String colorPopup;

	private String background;

	private String bbox;

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getPaper() {
		return paper;
	}

	public void setPaper(String paper) {
		this.paper = paper;
	}

	public String getSistcord() {
		return sistcord;
	}

	public void setSistcord(String sistcord) {
		this.sistcord = sistcord;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getColorPopup() {
		return colorPopup;
	}

	public void setColorPopup(String colorPopup) {
		this.colorPopup = colorPopup;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

	
	public String getBbox() {
		return bbox;
	}

	public void setBbox(String bbox) {
		this.bbox = bbox;
	}

	// ===========================================

	private List<ColumnModel> columns;

	public List<ColumnModel> getColumns() {
		return columns;
	}

	public void updateColumns() {
		UIComponent table = FacesContext.getCurrentInstance().getViewRoot()
				.findComponent(":SelectedLayerForm:tablayer:cars");
		table.setValueExpression("sortBy", null);
	}

	private List<Map<String, ColumnModel>> tableDataShape;

	private AbstractGeometry abstractGeometry;

	public AbstractGeometry getAbstractGeometry() {
		return abstractGeometry;
	}

	public void setAbstractGeometry(AbstractGeometry abstractGeometry) {
		this.abstractGeometry = abstractGeometry;
	}

	public List<Map<String, ColumnModel>> getTableDataShape() {
		
		if (this.abstractGeometry != null) {

			
			tableDataShape = new ArrayList<Map<String, ColumnModel>>();

			tableHeaderNames = new ArrayList<ColumnModel>();
			tableHeaderNames.add(new ColumnModel("Id", "Id"));

			tableHeaderNames.add(new ColumnModel("Nome", "Nome"));

			tableHeaderNames.add(new ColumnModel("Camada", "Camada"));

			List<Field> fieldsProperties = this.abstractGeometry.getLayer().getFields();
			for (Field columnKey : fieldsProperties) {
				tableHeaderNames.add(new ColumnModel(columnKey.getName(), columnKey.getName()));
			}
			
			ObjectNode node = null;

			
			Map<String, ColumnModel> playlist = new HashMap<String, ColumnModel>();
			

			playlist.put(tableHeaderNames.get(0).key,
					new ColumnModel(tableHeaderNames.get(0).key, this.abstractGeometry.getId().toString()));
			

			playlist.put(tableHeaderNames.get(1).key,
					new ColumnModel(tableHeaderNames.get(1).key, this.abstractGeometry.getNome()));

			playlist.put(tableHeaderNames.get(2).key,
					new ColumnModel(tableHeaderNames.get(2).key, this.abstractGeometry.getLayer().layerString()));

			try {
				node = new ObjectMapper().readValue(this.abstractGeometry.getStrings().toString(), ObjectNode.class);
				int stringKeyIndex = 3;
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

			// Generate table header.


			ObjectNode node = null;

			Integer contador = 1;
			List<Field> fieldsProperties = null;
			String categorias = new String();


			columns = new ArrayList<ColumnModel>();

			// for(String columnKey : columnKeys) {
			// String key = columnKey.trim();

			// if(VALID_COLUMN_KEYS.contains(key)) {
			columns.add(new ColumnModel("Id", "Id"));
			columns.add(new ColumnModel("Camada", "Camada"));
			columns.add(new ColumnModel("Nome", "Nome"));
			columns.add(new ColumnModel("Categorias", "Categorias"));


			this.setTableHeaderNames(columns);


			return tableDataGeometrias;
		} else {
			return null;
		}
	}

	// --------------------------------------------------------------------------------------------------------

	private List<Map<String, ColumnModel>> tableData;
	private List<ColumnModel> tableHeaderNames;

	public List<Map<String, ColumnModel>> getTableData() {
		if (this.layer != null) {

			tableData = new ArrayList<Map<String, ColumnModel>>();

			tableHeaderNames = new ArrayList<ColumnModel>();
			tableHeaderNames.add(new ColumnModel("Id", "Id"));
			tableHeaderNames.add(new ColumnModel("Camada", "Camada"));
			tableHeaderNames.add(new ColumnModel("Nome", "Nome"));

			List<Field> fieldsProperties = this.layer.getFields();
			for (Field columnKey : fieldsProperties) {
				tableHeaderNames.add(new ColumnModel(columnKey.getName(), columnKey.getName()));
			}

			// Generate table header.

			List<AbstractGeometry> listPoints = this.layer.getGeometrias();

			ObjectNode node = null;

			for (AbstractGeometry point : listPoints) {

				Map<String, ColumnModel> playlist = new HashMap<String, ColumnModel>();

				playlist.put(tableHeaderNames.get(0).key, new ColumnModel(tableHeaderNames.get(0).key, point.getId().toString()));

				playlist.put(tableHeaderNames.get(1).key,
						new ColumnModel(tableHeaderNames.get(1).key, point.getLayer().layerString()));

				playlist.put(tableHeaderNames.get(2).key,
						new ColumnModel(tableHeaderNames.get(2).key, point.getNome()));

				// String valueNode = null;
				try {
					node = new ObjectMapper().readValue(point.getStrings().toString(), ObjectNode.class);
					int stringKeyIndex = 3;
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

				// -----------------------------------------------------------------------------------------------------------------------------------------
				// node.get("Título").textValue());
				// node.get("Introdução").textValue());

				// Point changePoint = session.find(Point.class, point.getId());

				// Post post = new Post();
				// post.setId(UUID.randomUUID().toString());

				// post.setTitle(node.get("Título").textValue());
				// post.setIntroduction(node.get("Introdução").textValue());

				// post.setTitleLocale("pt_BR", node.get("Título").textValue());
				// post.setIntroductionLocale("pt_BR", node.get("Introdução").textValue());

				// AbstractGeometry teste = (AbstractGeometry) point;

				// post.setGeometry(teste);

				// session.persist(post);

				// session.flush();
				// -----------------------------------------------------------------------------------------------------------------------------------------

				tableData.add(playlist);

			}

			columns = new ArrayList<ColumnModel>();

			// for(String columnKey : columnKeys) {
			// String key = columnKey.trim();

			// if(VALID_COLUMN_KEYS.contains(key)) {
			columns.add(new ColumnModel("Id", "Id"));
			columns.add(new ColumnModel("Camada", "Camada"));
			columns.add(new ColumnModel("Nome", "Nome"));

			for (Field columnKey : fieldsProperties) {
				columns.add(new ColumnModel(columnKey.getName(), columnKey.getName()));
			}

			this.setTableHeaderNames(columns);

			return tableData;
		} else {
			return null;
		}
	}

	public void setTableHeaderNames(List<ColumnModel> tableHeaderNames) {
		this.tableHeaderNames = tableHeaderNames;
	}

	public List<ColumnModel> getTableHeaderNames() {
		return tableHeaderNames;
	}

	static public class ColumnModel {

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

	public void deleteObject() {
		try {
			Layer layerUpdate = (Layer) this.abstractGeometry.getLayer();
			entityManager.remove(this.abstractGeometry);
			entityManager.flush();
			entityManager.refresh(layerUpdate);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Ponto Excluido!"));
		} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}
	}

	@Transactional
	public String submitForm() {

		
		try {
		
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
				} else if (bookProperty.getName() == "Layer") {
					Long cccc = Long.valueOf(bookProperty.getValue().toString());
					Layer layerEntity = entityManager.find(Layer.class, cccc);
					if (layerEntity != null) {
						this.abstractGeometry.setLayer(layerEntity);
					}

				} else if (bookProperty.getName() == "Nome") {
					this.abstractGeometry.setNome(bookProperty.getValue().toString());

				} else if (bookProperty.getName() == "Father") {
					AbstractGeometry fatherEntity = entityManager.find(AbstractGeometry.class,
							bookProperty.getValue().toString());
					if (fatherEntity != null) {
						this.abstractGeometry.setFather(fatherEntity);
					}
				} else if (bookProperty.getName() == "Ativo") {
					Boolean ativo = Boolean.valueOf(bookProperty.getValue().toString());
					this.abstractGeometry.setEnabled(ativo);
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
				this.abstractGeometry.setStrings(JacksonUtil.toJsonNode(jsonb));
			} else {
				this.abstractGeometry.setStrings(JacksonUtil.toJsonNode(jsonb));
			}

			try {
				if (this.abstractGeometry.getParte() == null) {
					Integer parteCalc = this.abstractGeometry.getFather().getChildrens().size() + 1;
					this.abstractGeometry.setParte(parteCalc);
				}
			} catch (Exception e) {
				Integer parteCalc = this.abstractGeometry.getLayer().getGeometrias().size() + 1;
				this.abstractGeometry.setParte(parteCalc);
			}

			entityManager.merge(this.abstractGeometry);

			PrimeFaces.current().ajax().addCallbackParam("saved", true); // basic parameter
			PrimeFaces.current().ajax().addCallbackParam("nomeobject", this.abstractGeometry.getNome()); // basic
			// parameter
			// PrimeFaces.current().ajax().addCallbackParam("geometry",
			// this.abstractGeometry); //pojo as json

			entityManager.flush();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Registro Alterado!"));

		} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro!", "Registro Não Alterado!"));
		}
		return null;
	}

	public List<SelectItem> getLanguages2() {
		if (LANGUAGES.isEmpty()) {
			LANGUAGES.add(new SelectItem("en_US", "English"));
			LANGUAGES.add(new SelectItem("pt_BR", "Brazil"));
		}

		return LANGUAGES;
	}

	@Transactional
	public void createLaeyerP() {

		try {
			LayerPoint lp = new LayerPoint();
//		lp.setId(UUID.randomUUID().toString());
			lp.setName("TESTE");
			lp.setApplicationEntity(this.example.getApplicationEntity());

			
			entityManager.persist(lp);

			entityManager.flush();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}

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

		@SuppressWarnings("unused")
		List<AbstractGeometry> geometrias1 = new ArrayList<AbstractGeometry>();

		return null;
	}

	@SuppressWarnings("unused")
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
		
		try {
			
			LayerCategory layerCat = entityManager.find(LayerCategory.class, category);
			this.setLayerCategory(layerCat);
			this.category = category;

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}

	}

	public List<AbstractGeometry> getPageItemsAG() {

	
		try {
			
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			// Populate this.count
			CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
			Root<AbstractGeometry> root = countCriteria.from(AbstractGeometry.class);
			jakarta.persistence.criteria.Join<AbstractGeometry, LayerCategory> tagJoin = null;
			countCriteria = countCriteria.select(builder.count(root))
					.where(getSearchPredicatesPost(root, tagJoin, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();
			// Populate this.pageItems
			CriteriaQuery<AbstractGeometry> criteria = builder.createQuery(AbstractGeometry.class);
			root = criteria.from(AbstractGeometry.class);


			if (this.layer.getLayerCategories().size() > 0) {
				tagJoin = root.join("categories");
				root.fetch("categories");
			}
			TypedQuery<AbstractGeometry> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicatesPost(root, tagJoin, entityManager)));
			// query.setFirstResult(this.page * getPageSize()).setMaxResults(
			// getPageSize()); FIXME
			return query.getResultList();

		} catch (Exception e) {
				return null;
		}

	}

	/**
	 * Method Geometry Filter by Category
	 */

	private Predicate[] getSearchPredicatesPost(Root<AbstractGeometry> root,
			Join<AbstractGeometry, LayerCategory> tagJoin, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		if (nome != null && !"".equals(nome)) {
			predicatesList.add(builder.like(builder.lower(root.<String>get("nome")), "%" + nome.toLowerCase() + '%'));
		}

		if (this.getLayer() instanceof Layer) {
			if (layer != null) {
				predicatesList.add(builder.equal(root.get("layer"), this.getLayer()));
			}
		}
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	private int page8;
	private long count8;
	private List<AbstractGeometry> pageItems8;

	private AbstractGeometry example8;

	public int getPage8() {
		return this.page8;
	}

	public void setPage8(int page8) {
		this.page8 = page8;
	}

	public int getPageSize8() {
		return 10;
	}

	public long getCount8() {
		return this.count8;
	}

	public void setCount8(int count8) {
		this.count8 = count8;
	}

	public AbstractGeometry getExample8() {
		return this.example8;
	}

	public void setExample8(AbstractGeometry example8) {
		this.example8 = example8;
	}

	public List<AbstractGeometry> getPageItems8() {
		return this.pageItems8;
	}

	public void previousPage() {
		this.page8 = this.getPage8() - 1;
	}

	public void nextPage() {
		this.page8 = this.getPage8() + 1;
	}

	private String nameSearch;

	public String getNameSearch() {
		return nameSearch;
	}

	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	public void search8() {
		this.page8 = 0;
		paginate8();
	}

	public void paginateStart8() {
		this.page8 = 0;
		this.nameSearch = null;
		paginate8();
	}

	public void paginate8() {

		try {
				CriteriaBuilder builder = entityManager.getCriteriaBuilder();

			// Populate this.count

			CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
			Root<AbstractGeometry> root = countCriteria.from(AbstractGeometry.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates8(root, entityManager));

			this.count8 = entityManager.createQuery(countCriteria).getSingleResult();

			// Populate this.pageItems

			CriteriaQuery<AbstractGeometry> criteria = builder.createQuery(AbstractGeometry.class);
			root = criteria.from(AbstractGeometry.class);

			TypedQuery<AbstractGeometry> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicates8(root, entityManager)));
			query.setFirstResult(this.page8 * getPageSize8()).setMaxResults(getPageSize8());

			this.pageItems8 = query.getResultList();

		} catch (Exception e) {
			
		}
	}

	private Predicate[] getSearchPredicates8(Root<AbstractGeometry> root, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		if (this.layer != null) {
			predicatesList.add(builder.equal(root.get("layer"), this.layer));
		}

		String nome = this.getNameSearch();
		if (nome != null && !"".equals(nome)) {
			predicatesList.add(builder.like(builder.lower(root.<String>get("nome")), '%' + nome.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	// ----------------------

}
