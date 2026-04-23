package org.geoazul.view.website;

import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
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

import org.example.kickoff.model.Person;
import org.geoazul.model.app.ApplicationIdentityEntity;
import org.geoazul.model.basic.Comp;
import org.geoazul.model.basic.properties.Field;
import org.geoazul.model.basic.properties.FieldControl;

import org.geoazul.model.website.UrlPost;
import org.hibernate.Session;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.FlowEvent;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.primefaces.refact.BookProperty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jsonb.JacksonUtil;

/**
 * 
 */

@Named
@ViewScoped
public class ComponentBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@PreDestroy
	public void preDestroy() {
	}
	
	@Inject 
	EntityManager entityManager;
	
	@Inject
	@Param(pathIndex = 0)
	private Long gcmid;

	@Inject
	@Param(pathIndex = 1)
	private Long id;
	
	public Long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	private Long identity_id;

	public Long getIdentity_id() {
		return identity_id;
	}

	public void setIdentity_id(Long identity_id) {
		this.identity_id = identity_id;
	}
	
	private String component_type;
	
	public String getComponent_type() {
		return component_type;
	}

	public void setComponent_type(String component_type) {
		this.component_type = component_type;
	}

	private Comp component;

	public Comp getComponent() {
		return this.component;
	}

	public void setComponent(Comp component) {
		this.component = component;
	}

	public String create() {
		return "component_edit?faces-redirect=true&module_type=" + component_type; 
	}

	@PostConstruct
    public void init() {
		
	
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (this.id == null && this.component_type != null) {


			
			switch (this.component_type) {
			
		
			
			case "post":
				this.component = new UrlPost();
				break;
				
		
				
			
				
		

			default:
				FacesContext facesContext = FacesContext.getCurrentInstance();
				 facesContext.addMessage(null, new FacesMessage(
			        		FacesMessage.SEVERITY_INFO, "Erro!",
			                "Necessário selecionar um Tipo de Módulo!"));
			        String url = facesContext.getExternalContext().getRequestContextPath();
					            try {
					            	facesContext.getExternalContext().redirect(url + "/administrator/module_search");
								} catch (IOException e) {

								}
			    	
			}

			
			
			
		} else {
			 findById(this.id);
				//beanMethod();
		}

	}

	public Comp findById(Long id) {

		try {
			
			TypedQuery<Comp> queryModule = entityManager.createNamedQuery(Comp.COMP_ID, Comp.class);
			queryModule.setParameter("id", id);
			
			this.component = queryModule.getSingleResult();
		
	    
	    	return this.component;

		} catch (Exception e) {
				return null;
		} 
	}

	public Comp findById(Integer id) {
	
		try {
			return entityManager.find(Comp.class, id);
		} catch (Exception e) {
			
				return null;

		}

	}

	public ApplicationIdentityEntity findApplicationIdentityEntity(Long id) {

		try {
				return entityManager.find(ApplicationIdentityEntity.class, id);
		} catch (Exception e) {
				return null;
		} 
	}

	/*
	 * Support updating and deleting Comp entities
	 */

	@Transactional
	public String update() {
		
	
		try {
			
			if (this.id == null) {
				
				
				
				
				
				try {
				
				
					
					TypedQuery<ApplicationIdentityEntity> queryApp = entityManager.createNamedQuery(ApplicationIdentityEntity.APP_ID,
							ApplicationIdentityEntity.class);
					queryApp.setParameter("uuid", identity_id);
										
					this.component.setApplicationEntity(queryApp.getSingleResult());
					
					
					entityManager.persist(this.component);
					entityManager.flush();
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "Compe Inserido!"));
		
				} catch (Exception e) {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERROR", "Tente Novamente!"));
						return "application_search?faces-redirect=true";
				} 				
				
		
				
						} else {

							entityManager.merge(this.component);
							entityManager.flush();
							
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "Compe Alterado!"));
						}
			return "application_view/0/" + this.component.getApplicationEntity().getId() + "?faces-redirect=true";
		} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
				return "application_search?faces-redirect=true";
		} 
	}

	@Transactional
	public String delete() {

		try {
			
			Comp deletableEntity = findById(getId());

			entityManager.remove(deletableEntity);
			entityManager.flush();
			
			return "menu_edit?faces-redirect=true";

		} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
				return "search?faces-redirect=true";
		} 
	}

	/*
	 * Support searching Comp entities with pagination
	 * 
	 */

	private int page;
	private long count;
	private List<Comp> pageItems;

	

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

			// Populate this.count

			CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
			Root<Comp> root = countCriteria.from(Comp.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();

			// Populate this.pageItems

			CriteriaQuery<Comp> criteria = builder.createQuery(Comp.class);
			root = criteria.from(Comp.class);
			TypedQuery<Comp> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicates(root, entityManager)));
			query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
			this.pageItems = query.getResultList();

		} catch (Exception e) {
			
		} 
	}

	private Predicate[] getSearchPredicates(Root<Comp> root, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		if (this.getIdentity_id() == null) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.html");
			} catch (IOException e) {
			}
		} else {
			try {
				ApplicationIdentityEntity projectWebSiteEntityFind = findApplicationIdentityEntity(getIdentity_id());
				if (projectWebSiteEntityFind != null) {
					predicatesList.add(builder.equal(root.get("projectWebSiteEntity"), projectWebSiteEntityFind));
				} else {
					FacesContext.getCurrentInstance().getExternalContext().redirect("index.html");
				}
			} catch (NullPointerException | IOException npe) {
			}
		}

		//String name = this.example.getName();
		//if (name != null && !"".equals(name)) {
		//	predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
		//}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Comp> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Comp entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Comp> getAll() {

		try {
		
			CriteriaQuery<Comp> criteria = entityManager.getCriteriaBuilder().createQuery(Comp.class);
			return entityManager.createQuery(criteria.select(criteria.from(Comp.class))).getResultList();
		} catch (Exception e) {
			try {
				throw e;
			} catch (Throwable e1) {
				e1.printStackTrace();
				return null;
			}
		} 
	}

	// public List<Comp> getAllRootItens() {
	// if (this.id == null) {
	// TypedQuery<Comp> queryApp32 =
	// entityManager.createNamedQuery(Comp.ITENS_ROOT, Comp.class);
	// queryApp32.setParameter("urlmenuid", identity_id);
	// return queryApp32.getResultList();
	// }else {
	// TypedQuery<Comp> queryApp32 =
	// entityManager.createNamedQuery(Comp.ITENS_ROOT_EXCLUDE_ITEM,
	// Comp.class);
	// queryApp32.setParameter("urlmenuid", identity_id);
	// queryApp32.setParameter("urlmenuitemid", id);
	// return queryApp32.getResultList();
	// }
	// }

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	

	public List<String> completeText(String query) {
		List<String> results = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			results.add(query + i);
		}
		return results;
	}
	
	
	private Field fieldNew;

	public Field getFieldNew() {
		return fieldNew;
	}

	public void setFieldNew(Field fieldNew) {
		this.fieldNew = fieldNew;
	}

	// ----
	
	// --------------------------------------------------------------

		private DataType dataTypeChose;

		public DataType getDataTypeChose() {
			return dataTypeChose;
		}

		public void setDataTypeChose(DataType dataTypeChose) {
			this.dataTypeChose = dataTypeChose;
		}

		private String fieldName;

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}
		
		 private String fieldTypeChose;
		 
			public String getFieldTypeChose() {
				return fieldTypeChose;
			}

			public void setFieldTypeChose(String fieldTypeChose) {
				this.fieldTypeChose = fieldTypeChose;
			}
			
			private Map<String,String> dataTypes;
			
			public Map<String,String> getDataTypes() {
				return dataTypes;
			}

			public void setDataTypes(Map<String,String> dataTypes) {
				this.dataTypes = dataTypes;
			}
			
			private Map<String,String> fieldTypes;
			
			public Map<String,String> getFieldTypes() {
				
				Map<String,String> map = new HashMap<String, String>();
				
				switch (this.dataTypeChose) {
				
				case STRING:
					
					map.put("inputText", "inputText");
					map.put("outputText", "outputText");
					map.put("selectOneMenu", "selectOneMenu");
					map.put("inputTextarea", "inputTextarea");
					map.put("inputMask", "inputMask");
					map.put("password", "password");
					map.put("fileUpload", "fileUpload");
					map.put("datePicker", "datePicker");
					map.put("colorPicker", "colorPicker");
					map.put("textEditor", "textEditor");
					map.put("htmlEditor", "htmlEditor");
			       break;
				case NUMBER:
					map.put("inputNumber", "inputNumber");
					map.put("spinner", "spinner");
					 break;
			        
				case BOOLEAN:
					map.put("selectBooleanButton", "selectBooleanButton");
					map.put("selectBooleanCheckbox", "selectBooleanCheckbox");
					 break;
			        
				case ARRAY:
					map.put("chips", "chips");
					 break;
				}
				
				return map; 
				
				
			}
			
			 //public void onDataTypeChange() {
			//	if(country !=null && !country.equals(""))
			//			cities = data.get(country);
			//		else
			//			cities = new HashMap<String, String>();
			//	}

		/**
		 * Enumeration for the different types of Layers
		 */
		public enum DataType {
			STRING, NUMBER, BOOLEAN, ARRAY
		}
		
		
		
	
	
	public String onFlowProcess(FlowEvent event) {


		


		
		if (event.getOldStep().equals("upload")) {
			
				
				if (fieldTypeChose != null) {
					this.fieldNew.getFieldControl().setType(fieldTypeChose);
					Map<String, Object> params = new HashMap<>();
									
					switch (fieldTypeChose) {
					
					//string
					case "inputText":
						params.put("placeHolder", "");
						break;
					case "inputTextarea":
						params.put("placeHolder", "");
						break;
					case "inputMask":
						params.put("placeHolder", "");
						break;
					case "password":
						params.put("placeHolder", "");
						break;
					case "fileUpload":
						
						break;
					case "datePicker":
						params.put("placeHolder", "");
						break;
					case "textEditor":
						params.put("placeHolder", "");
						break;
					case "htmlEditor":
						params.put("placeHolder", "");
						break;
						
					//number	
					case "inputNumber":
						params.put("placeHolder", "");
						break;
					case "spinner":
						params.put("placeHolder", "");
						break;
						
					//boolean	
					case "selectBooleanButton":
						params.put("initval", false);
						break;
					case "selectBooleanCheckbox":
						params.put("initval", false);
						break;
						
					//chips	
					case "chips":
						params.put("placeHolder", "");
						break;
			
					}
										
					this.fieldNew.getFieldControl().setKeys(params);

				}	
				
			
		}
		
		try {
			
		} catch (Exception e) {
		} 
		
		try {
		} catch (Exception e) {
		} 

		//switch (this.dataTypeChose) {
		//case STRING:

			//	if (event.getOldStep().equals("upload")) {
			//	return "confirm";
			//} else {
			//	return "upload";
			//}

		//default:
			
		return event.getNewStep();
		//}
	}
	
	@Transactional
	public void saveField() {
		
		try {
		

		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		try {

				this.fieldNew.setName(fieldName); 
				this.fieldNew.setTypeData(dataTypeChose.toString().toLowerCase());
				
				entityManager.persist(this.fieldNew);
				entityManager.flush();
				this.component.addFieldItem(this.fieldNew);

		 

		
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
	}


	private Map<String,Map<String,String>> data = 
			new HashMap<String, Map<String,String>>();
	
	
	 
	 
	 public void newField() {
			this.dataTypeChose = null;
			this.fieldName = null;
			this.fieldTypeChose = null;
    		this.fieldNew = new Field(this.getComponent());
    		this.fieldNew.setFieldControl(new FieldControl());
		}
	 

	@Transactional
	public void updateField2() {


		try {
		
		
			boolean idFieldNull = false;
			try {
				if (this.fieldNew.getId() == null) {
					idFieldNull = true;
				}
			} catch (Exception e) {
				idFieldNull = true; // FIXME
			}
			if (idFieldNull) {

				FieldControl fieldControl = new FieldControl();
				String controle = this.fieldNew.getTypeData();
				switch (controle) {
				case "string":
					//OutputText outputText = new OutputText();
					//outputText.setSize(10);
					//outputText.setTypeInput("outputText");
					//fieldControl.setOutputText(outputText);
					fieldControl.setType("outputText");
					this.fieldNew.setFieldControl(fieldControl);
				
					
					//		+ this.fieldNew.getFieldControl().getOutputText());
					break;

				case "boolean":
					//SelectBooleanCheckbox selectBooleanCheckbox = new SelectBooleanCheckbox();
					//selectBooleanCheckbox.setDisabled(false);
					//selectBooleanCheckbox.setTypeInput("selectBooleanCheckbox");
					//fieldControl.setSelectBooleanCheckbox(selectBooleanCheckbox);
					fieldControl.setType("selectBooleanCheckbox");
					this.fieldNew.setFieldControl(fieldControl);
					break;

				case "textImg":
					//OutputText outputTextImage = new OutputText();
					//outputTextImage.setSize(30);
					//outputTextImage.setTypeInput("outputText");
					//fieldControl.setOutputText(outputTextImage);
					fieldControl.setType("textImg");
					this.fieldNew.setFieldControl(fieldControl);
					break;

				case "textEditor":
					//OutputText outputTextType = new OutputText();
					//outputTextType.setSize(10);
					//outputTextType.setTypeInput("outputText");
					this.fieldNew.setFieldControl(fieldControl);
					break;

				case "selectOneMenu":

					//fieldControl.setSelectOneMenu(this.fieldNew.getFieldControl().getSelectItems());
					fieldControl.setType("selectOneMenu");

					this.fieldNew.setFieldControl(fieldControl);
					break;

				default:
				}
				
				
				
			
				entityManager.persist(this.fieldNew);
				entityManager.flush();
				this.component.addFieldItem(this.fieldNew);

			} else {
				entityManager.merge(this.fieldNew);
				entityManager.flush();
				this.component.updateFieldItem(this.fieldNew);
			}

		
		} catch (Exception e) {
			
		} 

	}

	@Transactional
	public void deleteField() {

		try {
			
			Field deletableField = entityManager.find(Field.class, this.getFieldNew().getId());

			this.component.removeFieldItem(deletableField);

			entityManager.remove(deletableField);
			entityManager.flush();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Campo Excluido!"));

		} catch (Exception e) {
			
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro!", "Campo Não Excluido!"));

		} 
	}

	// -----
	
	
	public void beanMethodField() {

		try {
			String fieldIdCapture = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.get("fieldid");
					this.fieldNew = entityManager.find(Field.class, fieldIdCapture);
		

		} catch (Exception e) {
			
		} 
	}
	
	
	private DynaFormModel model;

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
	public void beanMethod() {
		

		try {

			
		
			List<Field> fieldsProperties = this.component.getFields();


			model = new DynaFormModel();
			DynaFormRow row = model.createRegularRow();

			DynaFormLabel label11 = row.addLabel("Nome", true, 6, 1);
			
			Map<String, Object> paramNome = new HashMap<>();
			paramNome.put("placeHolder", "Digite aqui o Nome");
			
			
			DynaFormControl control12 = row.addControl(
					new BookProperty("Nome", "Nome", "Digite aqui o Nome", this.component.getName(), false, null, paramNome),
					"input", 3, 1);
			label11.setForControl(control12);

			

			row = model.createRegularRow();
			DynaFormLabel label33 = row.addLabel("Ativo", true, 6, 1);
			DynaFormControl control333 = row.addControl(
					new BookProperty("Ativo", "Ativo", "Ativo", this.component.getEnabled(), true),
					"booleanchoice", 3, 1);
			label33.setForControl(control333);


		
				row = model.createRegularRow();
				DynaFormLabel label111 = row.addLabel("Icon", true, 6, 1);
				DynaFormControl control121 = row.addControl(new BookProperty("Icon", "Icon", "Digite aqui seu icon",
						this.component.getIcon(), false), "input", 3, 1);
				label11.setForControl(control121);
			


			ObjectNode node = null;
			try {
			
				node = new ObjectMapper().readValue(this.component.getStrings().toString(),
						ObjectNode.class);
			} catch (Exception ignore) {
				//ignore
			}
			// CREATE FIELD DYNAMICS AND POPULATE THIS
			int stringKeyIndex = 0;
			Object[] objectValue = new Object[this.component.getFields().size()];
			List<SelectItem> selectItemsBD = null;
			for (Field columnKey : fieldsProperties) {

				switch (columnKey.getTypeData()) {

				case "string":
					
					
					try {
						objectValue[stringKeyIndex] = node.get(columnKey.getName()).textValue();
					} catch (NullPointerException np) {
						objectValue[stringKeyIndex] = "";
					}
					
					selectItemsBD = columnKey.getFieldControl().getSelectItems();

					
					break;

				case "number":
					try {
						objectValue[stringKeyIndex] = node.get(columnKey.getName()).asDouble();
					} catch (NullPointerException np) {
						objectValue[stringKeyIndex] = false;
					}
					selectItemsBD = columnKey.getFieldControl().getSelectItems();
					break;

				case "boolean":
					try {
						objectValue[stringKeyIndex] = node.get(columnKey.getName()).asBoolean();
					} catch (NullPointerException np) {
						objectValue[stringKeyIndex] = false;
					}
					selectItemsBD = columnKey.getFieldControl().getSelectItems();
					break;

				
					
				case "array":
					try {
						objectValue[stringKeyIndex] = node.get(columnKey.getName()).textValue();
					} catch (NullPointerException np) {
						objectValue[stringKeyIndex] = "";
					}
					selectItemsBD = columnKey.getFieldControl().getSelectItems();
					break;

				}
				row = model.createRegularRow();
				row = rowcreate(row, columnKey.getFieldControl().getType(), columnKey.getLabel(), columnKey.getName(),
						objectValue[stringKeyIndex], columnKey.getPlaceHolder(), columnKey.getIcon(),
						columnKey.getRequired(), selectItemsBD, columnKey.getFieldControl().getKeys());
				stringKeyIndex++;

			

			}


		} catch (Exception e) {
			e.printStackTrace();
			
		}

	}
	
	
	
	private DynaFormRow rowcreate(DynaFormRow row, String typeField, String labelField, String nameField,
			Object valueField, String placeHolder, String icon, Boolean required, List<SelectItem> selectItems, 
			Map<String, Object> keys) {
		DynaFormLabel labelDF;
		DynaFormControl controlDF;

		switch (typeField) {

		case "textEditor":
			labelDF = row.addLabel(labelField, true, 12, 1);
			controlDF = row.addControl(new BookProperty(nameField, labelField, placeHolder, valueField, required),
					"texteditor", 3, 4);
			labelDF.setForControl(controlDF);
			break;

		case "textImg":
			labelDF = row.addLabel(labelField, true, 12, 1);
			controlDF = row.addControl(new BookProperty(nameField, labelField, valueField, required), "img", 3, 4);
			labelDF.setForControl(controlDF);
			break;

		case "fileUpload":
			labelDF = row.addLabel(labelField, true, 12, 1);
			controlDF = row.addControl(new BookProperty(nameField, labelField, valueField, required), "fileupload", 3,
					4);
			labelDF.setForControl(controlDF);
			break;

		case "calendarDate":
			labelDF = row.addLabel(labelField, true, 12, 1);
			controlDF = row.addControl(new BookProperty(nameField, labelField, LocalDate.now(), required), "calendar",
					3, 4);
			labelDF.setForControl(controlDF);
			break;

		case "selectOneMenu":
			labelDF = row.addLabel(labelField, true, 12, 1);
			controlDF = row.addControl(new BookProperty(nameField, labelField, valueField, required , selectItems), "selectOneMenu",
					3, 4);
			labelDF.setForControl(controlDF);
			break;

		case "outputText":
			labelDF = row.addLabel(labelField, true, 12, 1);
			controlDF = row.addControl(new BookProperty(nameField, labelField, placeHolder, valueField, required),
					"input", 3, 4);
			labelDF.setForControl(controlDF);
			break;
			
		case "inputText":
			labelDF = row.addLabel(labelField, true, 12, 1); 
			controlDF = row.addControl(new BookProperty(nameField, labelField, placeHolder, valueField, required, null, keys),
					"input", 3, 4);
			labelDF.setForControl(controlDF);
			break;
			
			
		case "inputNumber":
			
			String jsonb = "{\n"
					+ "    \"type\": \"outputText\",\n"
					+ "    \"outputText\": {\n"
					+ "        \"size\": 10,\n"
					+ "        \"typeInput\": \"outputText\"\n"
					+ "    },\n"
					+ "    \"selectBooleanCheckbox\": null\n"
					+ "}";
			
			JsonNode strings = JacksonUtil.toJsonNode(jsonb);
			
			labelDF = row.addLabel(labelField, true, 12, 1);
			controlDF = row.addControl(new BookProperty(nameField, labelField, placeHolder, valueField, required),
					"inputNumber", 3, 4);
			labelDF.setForControl(controlDF);
			break;

		case "selectBooleanCheckbox":
			labelDF = row.addLabel(labelField, true, 12, 1);
			controlDF = row.addControl(new BookProperty(nameField, labelField, placeHolder, valueField, required),
					"booleanchoice", 3, 4);
			labelDF.setForControl(controlDF);
			break;
		}

		return row;
	};
	
	
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

			
			Integer numberFields = this.component.getFields().size();

			for (BookProperty bookProperty : listBookProperty) {

			
				 if (bookProperty.getName() == "Nome") {
					this.component.setName(bookProperty.getValue().toString());
				} else if (bookProperty.getName() == "Titulo") {
					this.component.setTitle(bookProperty.getValue().toString());
				} else if (bookProperty.getName() == "Ativo") {
					Boolean ativo = Boolean.valueOf(bookProperty.getValue().toString());
					this.component.setEnabled(ativo);
				} else if (bookProperty.getName() == "Icon") {
					this.component.setIcon(bookProperty.getValue().toString());

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
				this.component.setStrings(JacksonUtil.toJsonNode(jsonb));
			} else {
				this.component.setStrings(JacksonUtil.toJsonNode(jsonb));
			}
			


			entityManager.merge(this.component);
			entityManager.flush();

			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Registro Alterado!"));

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public Long getGcmid() {
		return gcmid;
	}

	public void setGcmid(Long gcmid) {
		this.gcmid = gcmid;
	}




	
	
}