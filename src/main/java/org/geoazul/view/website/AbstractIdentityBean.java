package org.geoazul.view.website;

import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.lifecycle.ClientWindow;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import org.example.kickoff.view.ActiveLocale;
import org.geoazul.model.app.AbstractIdentityEntity;
import org.geoazul.model.app.ApplicationIdentityEntity;
import org.geoazul.model.website.ModuleFilter;
import org.geoazul.model.website.Modulo;
import org.geoazul.model.website.RBlock;
import org.geoazul.model.website.RButton;
import org.geoazul.model.website.RDivBreak;
import org.geoazul.model.website.RFlexDiv;
import org.geoazul.model.website.RGraphicImage;
import org.geoazul.model.website.RHeading;
import org.geoazul.model.website.RParagraph;
import org.geoazul.model.website.RVideo;
import org.geoazul.model.website.UrlMenuItem;
import org.keycloak.example.oauth.UserData;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import org.omnifaces.cdi.Cookie;
import org.omnifaces.cdi.Param;
import org.omnifaces.util.Faces;
import org.primefaces.event.RateEvent;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

@Named
@ViewScoped
public class AbstractIdentityBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(AbstractIdentityBean.class.getName());
	private static final String RESOURCES_PREFFIX = "";
	private static final String WEBAPP_PREFFIX = "/resources";
	private static final String APP_TEMPLATE_PATH = "/themes";

	@Inject
	@Cookie
	private String AcceptCookie;
	
	@Inject
	@Param(pathIndex = 0)
	private Long gcmid;

	public static String indentXmlString(String unformattedXml) {
        try {
            // Parse the XML string into a DOM Document
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            // Optional: set this to true to ignore whitespace between elements in the original document,
            // which can help with clean formatting.
            // dbf.setIgnoringElementContentWhitespace(true); 
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new InputSource(new StringReader(unformattedXml)));

            // Use a Transformer to output the Document with indentation
            TransformerFactory tf = TransformerFactory.newInstance();
            // Optional: try setting "indent-number" attribute for specific indentation levels (e.g., 4 spaces)
            // tf.setAttribute("indent-number", Integer.valueOf(4)); 
            Transformer t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            // Optional: specify encoding
            // t.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); 
            // Optional: Omit XML declaration if not needed (e.g., <?xml version="1.0" ...?>)
            // t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");

            // Transform the DOM source to a String result
            StringWriter sw = new StringWriter();
            t.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();

        } catch (Exception e) {
            // Handle exceptions related to parsing or transformation
            e.printStackTrace();
            return unformattedXml; // Return original string in case of an error
        }
    }
	
	
	public Optional<String> readCookie(String key) {
		try {
			return Arrays.stream(request.getCookies()).filter(c -> key.equals(c.getName())).map(jakarta.servlet.http.Cookie::getValue)
					.findAny();
		} catch (Exception ignore) {
		}
		return Optional.empty();
	}

	public String getCookies() {
		Optional<String> cookie_def = readCookie(request.getServerName().toString());
		try {
			if (cookie_def.isPresent()) {
				return null;
			} else {
				return "PF('sidebar4').show();";
			}
		} catch (Exception ignore) {
		}
		return null;
	}

	private static boolean isFacesEvent(HttpServletRequest request) {
			
		return request.getParameter("jakarta.faces.behavior.event") != null
				|| request.getParameter("omnifaces.event") != null;
	}

	private String my_cookie_def;

	public String getMy_cookie_def() {
		return my_cookie_def;
	}

	public void setMy_cookie_def(String my_cookie_def) {
		this.my_cookie_def = my_cookie_def;
	}

	public static final String COOKIE_NAME_DEF = "cookie_def";
	public static final int COOKIE_MAX_AGE_IN_DAYS = 30;

	public void cookiesSet() {
		Optional<String> cookie_def = readCookie(request.getServerName().toString());

		if (cookie_def.isEmpty() && !isFacesEvent(request)) {
			my_cookie_def = UUID.randomUUID().toString();
			// Never set locale cookie on ajax or unload events.
				
			 Faces.addResponseCookie(request.getServerName().toString(), UUID.randomUUID().toString(), -1);
	        Faces.refresh();
			
			
		} else {
			my_cookie_def = cookie_def.get();
		}

	}

	private Integer rating;

	public void onrate(RateEvent<Integer> rateEvent) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Rate Event",
				"You rated:" + rateEvent.getRating());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void oncancel() {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cancel Event", "Rate Reset");
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Integer getRating() {
		return rating;
	}

	public void setRating(Integer rating) {
		this.rating = rating;
	}

	private int number;

	public int getNumber() {
		return number;
	}

	public void increment() {
		number++;
	}

	public String create() {
		return "create?faces-redirect=true";
	}

	@Inject
	private ActiveLocale activeLocale;

	@Inject
	private HttpServletRequest request;

	@Inject
	private EntityManager entityManager;


	@Inject
	private UserData userData;

	

	

	public void logout() {
		try {
			FacesContext context = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
			request.logout();

			context.getExternalContext().redirect(request.getContextPath() + "/");
		} catch (Exception ignore) {
		}
	}

	public void login() {
		
	}

	public void loginPath() {
		
	}

	public void register() {
		
	}

	public String Avatart() {
		return null;
		
	}
	
	

	//@Inject
	//private ActiveUser activeUser;
	
	@PostConstruct
	public void init() {

		
		
		
		Optional<String> cookie_uuid = readCookie("cart_uuid");

		if (!(!cookie_uuid.isEmpty() && !isFacesEvent(request))) {
			
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
			String name = "cart_uuid";
			
			Map<String, Object> properties = new HashMap<>();
		
			properties.put("maxAge", 31536000);
			properties.put("path", "/");
			try {
				externalContext.addResponseCookie(name, URLEncoder.encode(UUID.randomUUID().toString(), "UTF-8"), properties);
			} catch (UnsupportedEncodingException e) {
			}
	
		}
		
		// Here the user session must reload when user is authenticated and
		// userAccount is null in session because a have not yet a Observer Keycloak

		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		ClientWindow clientWindow = externalContext.getClientWindow();

		//if (clientWindow != null) {
		//} else {
		//}

		try {
			init_modules();
		}catch (Exception ex){
		}
			
	

	}

	private List<ActiveLocale> others;

	public List<ActiveLocale> getOthers() {
		return others;
	}

	public void setOthers(List<ActiveLocale> others) {
		this.others = others;
	}

	private boolean templateExists(String templateName) {
		try {
			return Faces.getExternalContext().getResourceAsStream(templateName) != null;
		} catch (Exception e) {
			LOG.warning(String.format("Could not find file defined in path '%s' " + "Error: %s. Falling!"
					+ "See documentation for more details: ", APP_TEMPLATE_PATH, e.getMessage()));
			return false;
		}
	}

	private String findTemplate(String appTemplatePath, String bundledPath) {
		String result;
		if (templateExists(WEBAPP_PREFFIX + appTemplatePath)) {
			result = WEBAPP_PREFFIX + appTemplatePath;
		} else if (templateExists(RESOURCES_PREFFIX + appTemplatePath)) {
			result = RESOURCES_PREFFIX + appTemplatePath;
		} else {
			result = bundledPath;
		}
		return result;
	}

	// -----------------------------------------------------------------------------------------

	
	

	private String uuid;

	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}



	public Long getGcmid() {
		return this.gcmid;
	}

	public void setGcmid(Long gcmid) {
		this.gcmid = gcmid;
	}

	private AbstractIdentityEntity abstractIdentityEntity;

	public AbstractIdentityEntity getAbstractIdentityEntity() {

		return this.abstractIdentityEntity;
	}

	public void setAbstractIdentityEntity(AbstractIdentityEntity abstractIdentityEntity) {
		this.abstractIdentityEntity = abstractIdentityEntity;
	}

	

	private String tenant;

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	static final String TEXT_NODE = "TextNode";
	static final String DOUBLE_NODE = "DoubleNode";
	static final String BOOL_NODE = "BooleanNode";
	static final String DEC_NODE = "DecimalNode";
	static final String BIGINT_NODE = "BigIntegerNode";
	static final String FLOAT_NODE = "FloatNode";
	static final String INT_NODE = "IntNode";
	static final String LONG_NODE = "LongNode";
	static final String NULL_NODE = "NullNode";
	static final String SHORT_NODE = "ShortNode";
	static final String NUMERIC_NODE = "NumericNode";

	static final String ADMINISTRATOR = "/administrator/";
	
	public void init_modules() {

		String clientName = "/";
		String path33 = request.getServletPath();
		boolean raiz = true;
		String tentativa = "";
		try {
			tentativa = path33.split("/")[2];
			raiz = false;
		} catch (Exception e) {
		}
		if (!raiz) {
			clientName = path33.split("/")[1];
			clientName = "/" + clientName + "/";
		}

	
		
		



		
		TypedQuery<AbstractIdentityEntity> query = entityManager
				.createNamedQuery(AbstractIdentityEntity.DEFAULT_APP_UNION, AbstractIdentityEntity.class);
		query.setParameter("locale", activeLocale.getValue().toString());
		query.setParameter("dtype", clientName);
		query.setMaxResults(1);


		
		List<AbstractIdentityEntity> abstractIdentityEntity_ = query.getResultList();


		if (abstractIdentityEntity_.size() > 0) {
			this.abstractIdentityEntity = abstractIdentityEntity_.get(0);
		} else {
			TypedQuery<AbstractIdentityEntity> query2 = entityManager
					.createNamedQuery(AbstractIdentityEntity.DEFAULT_APP_UNION, AbstractIdentityEntity.class);
			query2.setParameter("locale", "pt");
			query2.setParameter("dtype", clientName);
			List<AbstractIdentityEntity> abstractIdentityEntity2_ = query2.getResultList();
			if (abstractIdentityEntity2_.size() > 0) {
				this.abstractIdentityEntity = abstractIdentityEntity2_.get(0);
			} else {
				this.abstractIdentityEntity = new ApplicationIdentityEntity();
				this.abstractIdentityEntity.setTemplate("/themes/bs53/pageTemplate.xhtml");
			}
		}
		
		
		if (gcmid == null) {
			try {
				TypedQuery<Long> queryRootId = entityManager
						.createNamedQuery(UrlMenuItem.FIND_APP_MENU_ROOT_ID, Long.class).setMaxResults(1);
				queryRootId.setParameter("abstractIdentityEntity", abstractIdentityEntity);
				Long val = (Long) queryRootId.getSingleResult();
				gcmid = val;
			} catch (Exception tt) {
				gcmid = 1L; //FIXME
			}
		}	
		

		
		
		
		InitModuleFilters(this.abstractIdentityEntity.getClientEntity().getId(), gcmid, this.abstractIdentityEntity);
		
		
	}

	private Properties prop;

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	private List<ModuleFilter> moduleFiltersAll;

	public List<ModuleFilter> getModuleFiltersAll() {
		return moduleFiltersAll;
	}

	public void setModuleFiltersAll(List<ModuleFilter> moduleFiltersAll) {
		this.moduleFiltersAll = moduleFiltersAll;
	}

	public void InitModuleFilters(Long long1, Long gcmid, AbstractIdentityEntity aEntity) {

		
		
		
		List<ModuleFilter> moduleFilters = new ArrayList<ModuleFilter>();
		ModuleFilter moduleFilter;

		
		
		Query queryModFilter = entityManager.createNamedQuery(Modulo.MENU_ID6);
		queryModFilter.setParameter("clientid", long1);
		queryModFilter.setParameter("gcmid", gcmid);
		queryModFilter.setParameter("abstractIdentityEntity", aEntity);

		List<Object[]> modulos = (List<Object[]>) queryModFilter.getResultList();
	
	
		
		for (Object[] modulo : modulos) {
			
			
			Long moduloid = (Long) modulo[0];
			String folder = (String) modulo[1];
			String path = (String) modulo[2];
			String include = (String) modulo[3];
			JsonNode fields = (JsonNode) modulo[4];
			Long urlMenuItem = (Long) modulo[5];
			JsonNode components = (JsonNode) modulo[6];
			Integer ordering1 = (Integer) modulo[7];
			Long type = (Long) modulo[8];
			Integer ordering2 = (Integer) modulo[9];
			String widget_id = (String) modulo[10];
			JsonNode block = (JsonNode) modulo[11];
			StringBuilder builder = new StringBuilder();
			builder.append("/includes/");
			builder.append(components.at("/type").asText() + "_" + widget_id);
			builder.append(".xhtml");
			
			
			moduleFilter = new ModuleFilter(UUID.randomUUID().toString(), moduloid, folder, path, include, fields,
					urlMenuItem, components, ordering1, type, ordering2, builder.toString(), widget_id, getBlock(block, moduloid, widget_id));
			
			moduleFilters.add(moduleFilter);
		}

		moduleFiltersAll = moduleFilters;
	}
	
	
	public RBlock getBlock(JsonNode jsonCode, Long id, String widgetId) {

		try {

			
			
			ObjectMapper map33 = new ObjectMapper();

			map33.registerSubtypes(new NamedType(RVideo.class, "RVideo"));
			map33.registerSubtypes(new NamedType(RButton.class, "RButton"));
			map33.registerSubtypes(new NamedType(RHeading.class, "RHeading"));
			map33.registerSubtypes(new NamedType(RFlexDiv.class, "RFlexDiv"));
			map33.registerSubtypes(new NamedType(RDivBreak.class, "RDivBreak"));
			map33.registerSubtypes(new NamedType(RParagraph.class, "RParagraph"));
			map33.registerSubtypes(new NamedType(RGraphicImage.class, "RGraphicImage"));

			RBlock newRBlock = null;
					
			newRBlock = map33.readValue(jsonCode.toString(), RBlock.class);
				
		
			newRBlock.setId(id);
			newRBlock.setAbstractWidget(widgetId);
			return newRBlock;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	
	

	public List<ModuleFilter> getModuleFilters(String position) {
		List<ModuleFilter> moduleFiltersAll_Retorno = new ArrayList<ModuleFilter>();
		for (ModuleFilter modf : this.moduleFiltersAll) {
			if (modf.getHtmltag().equals(position)) {
				moduleFiltersAll_Retorno.add(modf);
			}
		}
		return moduleFiltersAll_Retorno; // FIXME AQUI TA LENTO
	}

	public List<AbstractIdentityEntity> getAll() {
		try {
			CriteriaQuery<AbstractIdentityEntity> criteria = entityManager.getCriteriaBuilder()
					.createQuery(AbstractIdentityEntity.class);
			return entityManager.createQuery(criteria.select(criteria.from(AbstractIdentityEntity.class)))
					.getResultList();
		} catch (Exception e) {
			return null;
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

	


	

}