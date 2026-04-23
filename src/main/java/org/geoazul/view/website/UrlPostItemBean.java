package org.geoazul.view.website;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
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
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;
import org.geoazul.model.app.ControlType;
import org.geoazul.model.app.DataType;
import org.geoazul.model.basic.AbstractGeometry;
import org.geoazul.model.basic.Comp;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.basic.LayerCategory;
import org.geoazul.model.basic.Comp.IconcategoryId;
import org.geoazul.model.basic.properties.Field;
import org.geoazul.model.website.UrlPost;
import org.geoazul.model.website.UrlPostItem;
import org.geoazul.model.website.media.Media;
import org.geoazul.view.utils.ConvertToASCII2;
import org.hibernate.Session;
import org.keycloak.example.oauth.UserData;
import org.omnifaces.cdi.ViewScoped;
import org.owasp.html.AttributePolicy;
import org.owasp.html.Handler;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamRenderer;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.primefaces.refact.BookProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import br.bancodobrasil.model.BancoBrasilPixPayment;
import jsonb.JacksonUtil;
import jakarta.annotation.Nullable;
import static modules.LoadInitParameter.*;

/**
 * I am need a ViewScoped here to manage the media for exclude
 */

@Named
@ViewScoped
public class UrlPostItemBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@PreDestroy
	public void preDestroy() {
	}

	@Inject
	private EntityManager entityManager;

	@Inject
	
	private HttpServletRequest request;

	@Inject
	private UserData userData;

	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String post_id;

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	private UrlPostItem urlPostItem;

	public UrlPostItem getUrlPostItem() {
		return this.urlPostItem;
	}

	public void setUrlPostItem(UrlPostItem urlPostItem) {
		this.urlPostItem = urlPostItem;
	}

	public String create() {
		return "post_item_edit?faces-redirect=true&post_id=" + this.getPost_id();
	}

	public void retrieve() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (this.id == null) {
			try {
				if (this.post_id == null) {

					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage("ERRO", "Post não encontrado!"));
					FacesContext.getCurrentInstance().getExternalContext().redirect("post_search.xhtml");

				} else {

					this.urlPostItem = new UrlPostItem();


					Comp urlMen = findCompByUuid(this.post_id);


					this.urlPostItem.setLayer(urlMen);
					String jsonb = "{}";

					this.urlPostItem.setStrings(JacksonUtil.toJsonNode(jsonb));
				
					beanMethod();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			this.urlPostItem = findByUuid(getId());
			beanMethod();
//			  model2 = PagedDataModel.lazy(service).build();
		}
	}

	public List<Media> getAllMedias() {

		this.urlPostItem = findByUuid(getId());

		Query queryMedFilter = entityManager.createNamedQuery(Media.ALL);
		queryMedFilter.setParameter("id", this.urlPostItem.getId());

		return queryMedFilter.getResultList();
		// return this.urlPostItem.getMedias();
	}

	public UrlPostItem findByUuid(String uuid) {
		try {
			
			TypedQuery<UrlPostItem> UrlPostItemQ = entityManager
					.createNamedQuery(UrlPostItem.FIND_ID, UrlPostItem.class);
			UrlPostItemQ.setParameter("id", id);
			this.urlPostItem = UrlPostItemQ.getSingleResult();

			return this.urlPostItem;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public UrlPostItem findById(String id) {
		try {
			return entityManager.find(UrlPostItem.class, id);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public Comp findCompByUuid(String uuid) {
		try {

			return entityManager.unwrap(Session.class).bySimpleNaturalId(Comp.class).load(UUID.fromString(uuid));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return null;
		}

	}

	public UrlPost findPostById(String id) {
		try {
			return entityManager.find(UrlPost.class, id);
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return null;
		}
	}

	/*
	 * Support updating and deleting UrlPostItem entities
	 */

	@Transactional
	public String update() {
		try {
			if (this.id == null) {
				entityManager.persist(this.urlPostItem);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "Item Inserido!"));

			} else {
				entityManager.merge(this.urlPostItem);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "Item Alterador!"));
			}
			entityManager.flush();
			return "post_view?faces-redirect=true&id=" + post_id;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return "post_view?faces-redirect=true&id=" + post_id;
		}
	}

	@Transactional
	public String delete() {

		try {

			UrlPostItem deletableEntity = findByUuid(getId());
			String compID = deletableEntity.getLayer().getId().toString();
			entityManager.remove(deletableEntity);
			entityManager.flush();

			return "component_view?faces-redirect=true&id=" + compID;

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return "application_search?faces-redirect=true";
		}
	}

	/*
	 * Support searching UrlPostItem entities with pagination
	 * 
	 */

	private int page;
	private long count;
	private List<UrlPostItem> pageItems;

	private UrlPostItem example = new UrlPostItem();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 50;
	}

	public UrlPostItem getExample() {
		return this.example;
	}

	public void setExample(UrlPostItem example) {
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
			Root<UrlPostItem> root = countCriteria.from(UrlPostItem.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();
			CriteriaQuery<UrlPostItem> criteria = builder.createQuery(UrlPostItem.class);
			root = criteria.from(UrlPostItem.class);
			TypedQuery<UrlPostItem> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicates(root, entityManager)));
			query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
			this.pageItems = query.getResultList();

		} catch (Exception e) {

		}
	}

	private Predicate[] getSearchPredicates(Root<UrlPostItem> root, EntityManager session) {
		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		if (this.getPost_id() == null) {
			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.html");
			} catch (IOException e) {
			}
		} else {
			try {
				UrlPost urlPostFind = findPostById(getPost_id());
				if (urlPostFind != null) {
					predicatesList.add(builder.equal(root.get("urlPost"), urlPostFind));
				} else {
					FacesContext.getCurrentInstance().getExternalContext().redirect("index.html");
				}
			} catch (NullPointerException | IOException npe) {
			}
		}

		String name = this.example.getNome();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<UrlPostItem> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private UrlPostItem add = new UrlPostItem();

	public UrlPostItem getAdd() {
		return this.add;
	}

	public UrlPostItem getAdded() {
		UrlPostItem added = this.add;
		this.add = new UrlPostItem();
		return added;
	}

	public List<String> completeText(String query) {
		List<String> results = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			results.add(query + i);
		}
		return results;
	}

	// ---------

	/*
	 * Support objectId set on Ajax request method CREATE FIELD DYNAMICS AND
	 * POPULATE THIS
	 */
	public void beanMethod() {
		
		List<Field> fieldsProperties;
		Integer fieldSize = 0;
		try {
			fieldSize = this.urlPostItem.getLayer().getFields().size();
		} catch (Exception ex) {
			// ignore
		}
		model = new DynaFormModel();
		DynaFormRow row = new DynaFormRow();
		
		
		row = model.createRegularRow();
		DynaFormLabel label33 = row.addLabel("Ativo");
		DynaFormControl control333 = row.addControl(
				new BookProperty("Ativo", "Ativo", "Ativo", 
						this.urlPostItem.getEnabled(), false), "booleanchoice", 1, 1);
		label33.setForControl(control333);

		

		List<LayerCategory> layerCat = this.urlPostItem.getCategories();

		Long idCategory = null;
		for (LayerCategory layerC : layerCat) {
			idCategory = layerC.getId();
		}
		try {
			Integer numberCat = this.urlPostItem.getLayer().getLayerCategories().size();
			if (numberCat > 0) {
				
				row = model.createRegularRow();
				DynaFormLabel label410 = row.addLabel("Categoria");
				DynaFormControl control420 = row.addControl(
						new BookProperty("Categoria", "Categoria", "Digite aqui a Categoria", idCategory, false),
						"selectcat");
				label410.setForControl(control420);
			}
		} catch (NullPointerException np) {

		}

		// try {
		// if (this.urlPostItem.getLayer() != null) {
		// Long idLayerGeom = this.urlPostItem.getLayer().getId();
		// DynaFormLabel label9103 = row.addLabel("Camada", true, 6, 1,
		// "mail_outline");
		// DynaFormControl control9203 = row.addControl(
		// new BookProperty("Layer", "Camada", "Digite aqui a Camada", idLayerGeom,
		// false), "selectlayer");
		// label9103.setForControl(control9203);
		// } else {
		// DynaFormLabel label9103 = row.addLabel("Camada", true, 6, 1,
		// "mail_outline");
		// DynaFormControl control9203 = row.addControl(
		// new BookProperty("Camada", "Camada", "Digite aqui a Camada", "", false),
		// "selectlayer");
		// label9103.setForControl(control9203);
		// }

		// } catch (NullPointerException np) {
		// DynaFormLabel label9103 = row.addLabel("Camada", true, 6, 1,
		// "mail_outline");
		// DynaFormControl control9203 = row
		// .addControl(new BookProperty("Camada", "Camada", "Digite aqui a Camada", "",
		// false), "selectlayer");
		// label9103.setForControl(control9203);
		// }

		boolean hasIdFather = false;
		Long idFather = 0L;
		try {
			if (this.urlPostItem.getLayer().getLayerCat() != null) {

				try {
					idFather = this.urlPostItem.getFather().getId();
				} catch (NullPointerException np) {
					np.printStackTrace();
				}
				if (idFather > 0) {


					DynaFormLabel label4103 = row.addLabel(this.urlPostItem.getFather().getLayer().getName());
					DynaFormControl control4203 = row.addControl(
							new BookProperty("Father", "Pai", "Digite aqui o Pai", idFather, false), "selectfather");
					label4103.setForControl(control4203);

				} else {

					DynaFormLabel label41038 = row.addLabel(this.urlPostItem.getLayer().getLayerCat().getName());
					DynaFormControl control42038 = row.addControl(
							new BookProperty("Father", "Pai", "Digite aqui o Pai", "", 
									false), "selectfather");
					label41038.setForControl(control42038);
				}

			}

		} catch (NullPointerException np) {
			np.printStackTrace();
		}

		row = model.createRegularRow();
		DynaFormLabel label11 = row.addLabel("Nome");

		Map<String, Object> paramNome = new HashMap<>();
		paramNome.put("placeHolder", "Digite aqui o Nome");
	
		
		DynaFormControl control12 = row.addControl(new BookProperty("Nome", "Nome", "Digite aqui o Nome",
				this.urlPostItem.getNome(), true, JacksonUtil.toJsonNode(
						"{ \"ART\": \"33\", " + "\"ITENS\": {\"teste\": \"Teste\",\"outro\": \"Outro\"}   " + "   }"

				), paramNome), "input", 1, 1);
		label11.setForControl(control12);


		

		if (this.urlPostItem.getLayer().getIconcategory() == IconcategoryId.ENTITY) {
			row = model.createRegularRow();
			DynaFormLabel label111 = row.addLabel("Icon");
			DynaFormControl control121 = row.addControl(
					new BookProperty("Icon", "Icon", "Digite aqui seu icon", this.urlPostItem.getIconflag(), true),
					"input", 1, 1);
			label11.setForControl(control121);
		}


		ObjectNode node = null;
		
		
		try {
			if (this.urlPostItem.getStrings() == null) {
				node = new ObjectMapper().readValue("{}", ObjectNode.class);
			}else {
				node = new ObjectMapper().readValue(this.urlPostItem.getStrings().toString(), ObjectNode.class);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		// CREATE FIELD DYNAMICS AND POPULATE THIS
		int stringKeyIndex = 0;

		Object[] objectValue = null;

		objectValue = new Object[fieldSize];


		List<SelectItem> selectItemsBD = null;


		if (fieldSize > 0) {
			fieldsProperties = this.urlPostItem.getLayer().getFields();

			for (Field columnKey : fieldsProperties) {

				switch (columnKey.getTypeData()) {

				case "string":

					// columnKey.getFieldControl().toString());

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
		}

	}

	private Map<DataType, Map<DataType, ControlType>> data = new HashMap<DataType, Map<DataType, ControlType>>();
	private DataType dataType;
	private ControlType controlType;
	private Map<DataType, DataType> dataTypes;
	private Map<ControlType, ControlType> controlTypes;

	@PostConstruct
	public void init() {

	}

	private DynaFormRow rowcreate(DynaFormRow row, String typeField, String labelField, String nameField,
			Object valueField, String placeHolder, String icon, Boolean required, List<SelectItem> selectItems,
			Map<String, Object> keys) {
		DynaFormLabel labelDF;
		DynaFormControl controlDF;

		switch (typeField) {

		case "textEditor":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, labelField, placeHolder, valueField, required),
					"texteditor", 1, 1);
			labelDF.setForControl(controlDF);
			break;

		case "textImg":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, labelField, valueField, required), "img", 1, 1);
			labelDF.setForControl(controlDF);
			break;

		case "fileUpload":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, labelField, valueField, required), 
					"fileupload", 1, 1);
			labelDF.setForControl(controlDF);
			break;

		case "calendarDate":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, labelField, LocalDate.now(), required), 
					"calendar", 1, 1);
			labelDF.setForControl(controlDF);
			break;

		case "selectOneMenu":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, labelField, valueField, required, selectItems),
					"selectOneMenu", 1, 1);
			labelDF.setForControl(controlDF);
			break;

		case "outputText":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, labelField, placeHolder, valueField, required),
					"input", 1, 1);
			labelDF.setForControl(controlDF);
			break;

		case "inputText":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(
					new BookProperty(nameField, labelField, placeHolder, valueField, required, null, keys), "input", 1,
					1);
			labelDF.setForControl(controlDF);
			break;
			
			
		case "colorPicker":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(
					new BookProperty(nameField, labelField, placeHolder, valueField, required, 
							null, keys), "colorPicker", 1,
					1);
			labelDF.setForControl(controlDF);
			break;

		case "inputNumber":


			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, labelField, placeHolder, valueField, required),
					"inputNumber", 1, 1);
			labelDF.setForControl(controlDF);
			break;

		case "selectBooleanCheckbox":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, labelField, placeHolder, valueField, required),
					"booleanchoice", 1, 1);
			labelDF.setForControl(controlDF);
			break;
		}

		return row;
	};

	// ---------

	// ---------------------------------------------

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

	// ---------------------------------
	private String sanitize(@Nullable String html) {
		StringBuilder sb = new StringBuilder();

		HtmlStreamRenderer renderer = HtmlStreamRenderer.create(sb,
				// Receives notifications on a failure to write to the output.
				new Handler<IOException>() {
					public void handle(IOException ex) {
						// System.out suppresses IOExceptions
						throw new AssertionError(null, ex);
					}
				},
				// Our HTML parser is very lenient, but this receives notifications on
				// truly bizarre inputs.
				new Handler<String>() {
					public void handle(String x) {
						throw new AssertionError(x);
					}
				});
		 
		// Use the policy defined above to sanitize the HTML.
	//	HtmlSanitizer.sanitize(html, br.bancodobrasil.EbayPolicyExample.POLICY_DEFINITION.apply(renderer));
		
		
		
		
		return sb.toString();
	}
	
	private static final AttributePolicy INTEGER = new AttributePolicy() {
	    public String apply(
	        String elementName, String attributeName, String value) {
	      int n = value.length();
	      if (n == 0) { return null; }
	      for (int i = 0; i < n; ++i) {
	        char ch = value.charAt(i);
	        if (ch == '.') {
	          if (i == 0) { return null; }
	          return value.substring(0, i);  // truncate to integer.
	        } else if (!('0' <= ch && ch <= '9')) {
	          return null;
	        }
	      }
	      return value;
	    }
	  };
	  
	  
	 public static final PolicyFactory IMAGES = new HtmlPolicyBuilder()
			 .allowAttributes("class", "style").globally() 
		      .allowUrlProtocols("http", "https").allowElements("img")
		      .allowAttributes("float", "style", "alt", "src").onElements("img")
		      .allowAttributes("border", "height", "width").matching(INTEGER)
		          .onElements("img")
		      .toFactory();

	 
	@Transactional
	public String submitForm() {
		try {
			Layer layerEntity = null;
			AbstractGeometry fatherEntity = null;
			String valuex;
			List<BookProperty> listBookProperty = this.getBookProperties();

			String jsonb = "{";
			int fieldIndex = 1;
			int fieldSize = listBookProperty.size();
			String nomeCatLayer = null;

		

			try {
				nomeCatLayer = this.urlPostItem.getLayer().getLayerCat().getName();
			} catch (NullPointerException np) {
				np.printStackTrace();
			}
		

			Integer numberFields = 0;
			try {
				numberFields = this.urlPostItem.getLayer().getFields().size();
			} catch (Exception ex) {
				// ignore
			}

		
			for (BookProperty bookProperty : listBookProperty) {
				Optional<Object> existString = Optional.ofNullable(bookProperty.getValue());
				StringBuilder sb = new StringBuilder();

				if (existString.isPresent()) {
					// SANITIZE START TO PREVENT INJECT SCRIPT
					String html = bookProperty.getValue().toString();
					
								
					PolicyFactory policy = Sanitizers.FORMATTING.and(Sanitizers.BLOCKS)
							.and(Sanitizers.LINKS).and(Sanitizers.TABLES).and(IMAGES);
					html = policy.sanitize(html);
					
					
								
					
			
					
					html = html.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"").replace("\r", "\\r")
			        .replace("\t", "\\t").replace("\b", "\\b").replace("\f", "\\f");
					
					bookProperty.setValue(html);
}
				// ---------------------------------------------
				if (bookProperty.getName() == "Categoria") {
					LayerCategory layerCat = entityManager.find(LayerCategory.class, bookProperty.getValue());
					List<LayerCategory> layerCatList = this.urlPostItem.getCategories();
					if (layerCatList.size() == 0) {
						this.urlPostItem.getCategories().add(layerCat);
						if (this.urlPostItem.getLayer().getIconcategory() == IconcategoryId.CATEGORY) {
							this.urlPostItem.setIconflag(layerCat.getIconflag());
						}
					} else {
						for (LayerCategory ageom : layerCatList) {
							if (!ageom.equals(layerCat)) {
								this.urlPostItem.getCategories().remove(ageom);
								this.urlPostItem.getCategories().add(layerCat);
								if (this.urlPostItem.getLayer().getIconcategory() == IconcategoryId.CATEGORY) {
									this.urlPostItem.setIconflag(layerCat.getIconflag());
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
					layerEntity = entityManager.find(Layer.class, cccc);
					if (layerEntity != null) {
						this.urlPostItem.setLayer(layerEntity);
					}

				} else if (bookProperty.getName() == "Nome") {
					this.urlPostItem.setNome(bookProperty.getValue().toString());
				} else if (bookProperty.getName() == "Father") {

				

					try {
						Long fatherId = Long.valueOf(bookProperty.getValue().toString());
						fatherEntity = entityManager.find(AbstractGeometry.class, fatherId);


				
							this.urlPostItem.setFather(fatherEntity);
					

					} catch (Exception e) {
						this.urlPostItem.setFather(null);
					}

				} else if (bookProperty.getName() == "Ativo") {
					Boolean ativo = Boolean.valueOf(bookProperty.getValue().toString());
					this.urlPostItem.setEnabled(ativo);
				} else if (bookProperty.getName() == "Icon") {
					this.urlPostItem.setIconflag(bookProperty.getValue().toString());

				} else {

					

					if (numberFields > 0) {
						if (bookProperty.getValue() == null) {
							valuex = "";
						} else {
							valuex = bookProperty.getValue().toString();
							

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
			
			//JacksonUtil.OBJECT_MAPPER.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

			if (numberFields > 0) {
				this.urlPostItem.setStrings(JacksonUtil.toJsonNode(jsonb));
			} else {
				this.urlPostItem.setStrings(JacksonUtil.toJsonNode("{}"));
			}

			try {
				if (this.urlPostItem.getParte() == null) {
					Integer parteCalc = fatherEntity.getChildrens().size() + 1;
					this.urlPostItem.setParte(parteCalc);
				}
			} catch (Exception e) {
				if (layerEntity != null) {
					Integer parteCalc = layerEntity.getGeometrias().size() + 1;
					this.urlPostItem.setParte(parteCalc);
				}
			}

			
			
			
		
			
			ObjectMapper mapper = new ObjectMapper();

			JsonNode ddd = mapper.readTree(jsonb);
			mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
			
		
			
			//this.urlPostItem.setStrings(ddd);

			//if (this.id == null) {
			//	entityManager.persist(this.urlPostItem);
			//	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Registro Inserido!"));
			//} else {
				entityManager.merge(this.urlPostItem);
			//	FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Registro Alterado!"));
			//}

			

			
			
			
			
			try {
				//Query query = entityManager.createNamedQuery(UrlPostItem.UPDATE_STRINGS);
				
				//query.setParameter("newStrings", ddd);
				//query.setParameter("id", this.urlPostItem.getId());
				
				//query.executeUpdate();
				
				
				Query query = entityManager.createNamedQuery(UrlPostItem.UPDATE_ALL);
				
				
				query.setParameter("enabled",  this.urlPostItem.getEnabled());
				query.setParameter("father",  this.urlPostItem.getFather());
				query.setParameter("iconflag",  this.urlPostItem.getIconflag());
				query.setParameter("layer",  this.urlPostItem.getLayer());
				query.setParameter("nome",  this.urlPostItem.getNome());
				query.setParameter("parte",  this.urlPostItem.getParte());
				query.setParameter("situacao",  this.urlPostItem.getSituacao());
		
				
					query.setParameter("strings", this.urlPostItem.getStrings());
					query.setParameter("id", this.urlPostItem.getId());
					
				//	query.executeUpdate();
			
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			entityManager.flush();
			
			
			BancoBrasilPixPayment bb = new BancoBrasilPixPayment();
			bb.setCalendario(null);
			
			

			return "component_view?faces-redirect=true&id=" + this.urlPostItem.getLayer().getId();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// ---------------------------------------------------------------

	public List<SelectItem> getLanguages() {
		List<SelectItem> categorias = new ArrayList<SelectItem>();
		try {
			Layer layerCat = this.urlPostItem.getLayer().getLayerCat();
			for (AbstractGeometry layer : layerCat.getGeometrias()) {
				categorias.add(new SelectItem(layer.getId(), layer.getNome()));
			}
		} catch (NullPointerException np) {
			// PROPERTIES IS NULL null FIXME
		}

		return categorias;
	}

	public List<SelectItem> getLanguagesCat() {
		List<SelectItem> categoriasCat = new ArrayList<SelectItem>();
		for (LayerCategory layerCategory : this.urlPostItem.getLayer().getLayerCategories()) {
			categoriasCat.add(new SelectItem(layerCategory.getId(), layerCategory.getCategoryname()));
		}
		return categoriasCat;
	}

	// ----------------------

	public List<SelectItem> getLanguagesFat(String teste) {
		try {
			if (this.urlPostItem.getLayer().getLayerCat() != null) {
				List<SelectItem> categoriasFat = new ArrayList<SelectItem>();
				categoriasFat.add(new SelectItem("", "Selecione"));
				try {
					for (AbstractGeometry abstractGeometryFather : this.urlPostItem.getLayer().getLayerCat()
							.getGeometrias()) {
						categoriasFat
								.add(new SelectItem(abstractGeometryFather.getId(), abstractGeometryFather.getNome()));
					}
				} catch (Exception e) {
				}
				return categoriasFat;
			}
		} catch (Exception e) {
		}
		return new ArrayList<SelectItem>();
	}

	// ----------------------

	public List<SelectItem> getLanguagesLay(String teste) {
		List<SelectItem> categoriasLay = new ArrayList<SelectItem>();
		try {
			categoriasLay.add(new SelectItem("", "Selecione"));
			String a = null;
			String b = null;
			for (Comp layerLay : this.urlPostItem.getLayer().getApplicationEntity().getComponents()) {
				a = layerLay.getDtype().toLowerCase();
				b = this.urlPostItem.getClass().getSimpleName().toLowerCase();
				if (a.equals(b)) {
					// FIXME
					// categoriasLay
					// .add(new SelectItem(layerLay.getId(), layerLay.getName(), layerLay.getName(),
					// false, true));
				}
				categoriasLay
						.add(new SelectItem(layerLay.getId(), layerLay.getName(), layerLay.getName(), false, true));
			}
			return categoriasLay;
		} catch (Exception e) {
			return categoriasLay;
		}
	}

	// ----------------------

	// private PagedDataModel<Media> model2;

	// @Inject
	// private MediaService service;

	// public PagedDataModel<Media> getModel2() {
	// return model2;
	// }

	// ----------------------------------------------- UPLOAD IMAGE AVATAR
	@Transactional
	public void importIMAGE(FileUploadEvent event) throws Exception {


		try {

			FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			String fileNameOnly = "";
			String fileNameNew = null;
			try {

				Random random = new Random();
				int randomInt = random.nextInt(100);

				LocalDateTime data = LocalDateTime.now();
				// SimpleDateFormat formatador = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
				String fileName = ConvertToASCII2.convert(event.getFile().getFileName()).toLowerCase();
				fileNameOnly = data.toString() + "-" + String.valueOf(randomInt) + "_" + fileName;
				String appDirectory = save_FILE_PATH + "/files/" + userData.getRealmEntity() + "/media";
				File appDirImg = new File(appDirectory);
				if (!appDirImg.exists()) {
					appDirImg.mkdir();
				}
				fileNameNew = appDirectory + "/" + fileNameOnly;

				OutputStream out = new FileOutputStream(new File(fileNameNew));
				int read = 0;
				byte[] bytes = new byte[1024];
				InputStream in = event.getFile().getInputStream();
				while ((read = in.read(bytes)) != -1) {
					out.write(bytes, 0, read);
				}
				in.close();
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Media mediaNew = new Media();
			mediaNew.setFilename("/files/" + userData.getRealmEntity() + "/media/" + fileNameOnly);

			TikaConfig tikaConfig = TikaConfig.getDefaultConfig();

			Metadata metadata = new Metadata();
			String text = null;
			try {
				text = parseUsingComponents(fileNameNew, tikaConfig, metadata);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mediaNew.setMimeType(text);

			mediaNew.setReleaseDate(LocalDateTime.now());

			

			mediaNew.setAbstractGeometry(this.urlPostItem);
			
			entityManager.persist(mediaNew);

			this.urlPostItem.getMedias().add(mediaNew);

			entityManager.merge(this.urlPostItem);
			entityManager.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// FacesContext.getCurrentInstance().addMessage(null, new
			// FacesMessage(e.getMessage()));
			// return "complete?faces-redirect=true&id=" + this.person.getId();
		}

	}

	public String parseUsingComponents(String filename, TikaConfig tikaConfig, Metadata metadata) throws Exception {
		MimeTypes mimeRegistry = tikaConfig.getMimeRepository();


		Path path = Paths.get(filename);
		TikaInputStream tikaStream = TikaInputStream.get(path);
		Detector detector = tikaConfig.getDetector();
	
		return detector.detect(tikaStream, metadata).toString();

	}
	// ------------------------

	private List<Media> selectedMedias;

	public List<Media> getSelectedMedias() {
		return selectedMedias;
	}

	public void setSelectedMedias(List<Media> selectedMedias) {
		this.selectedMedias = selectedMedias;
	}

	@Transactional
	public void deleteMedias() {
		try {

			for (Media media : this.getSelectedMedias()) {
				Media deletedMedia = entityManager.find(Media.class, media.getId());
				this.urlPostItem.removeMedia(deletedMedia);
				entityManager.remove(deletedMedia);
			}

				entityManager.flush();

			FacesMessage msg = new FacesMessage("SUCESSO", "Arquivos Excluidos!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} catch (Exception e) {

		}
	}

	public Map<DataType, Map<DataType, ControlType>> getData() {
		return data;
	}

	public void setData(Map<DataType, Map<DataType, ControlType>> data) {
		this.data = data;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public ControlType getControlType() {
		return controlType;
	}

	public void setControlType(ControlType controlType) {
		this.controlType = controlType;
	}

	public Map<DataType, DataType> getDataTypes() {
		return dataTypes;
	}

	public void setDataTypes(Map<DataType, DataType> dataTypes) {
		this.dataTypes = dataTypes;
	}

	public Map<ControlType, ControlType> getControlTypes() {
		return controlTypes;
	}

	public void setControlTypes(Map<ControlType, ControlType> controlTypes) {
		this.controlTypes = controlTypes;
	}

}