package org.geoazul.view;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import jakarta.annotation.PostConstruct;
import jakarta.faces.FacesException;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.application.ViewExpiredException;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.ValueChangeEvent;
import jakarta.faces.model.SelectItem;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.apache.http.client.ClientProtocolException;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.detect.Detector;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.metadata.TikaCoreProperties;
import org.apache.tika.mime.MediaType;
import org.apache.tika.mime.MimeTypes;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.example.kickoff.view.ActiveLocale;
import org.example.kickoff.view.ActiveUser;
import org.geoazul.geoserver.CreateProjectWorkspace;
import org.geoazul.model.Contador;
import org.geoazul.model.app.AbstractIdentityEntity;
import org.geoazul.model.app.AppUserMappingEntity;
import org.geoazul.model.app.AppUserMappingEntity.AppRole;
import org.geoazul.model.app.ApplicationAttributeEntity;
import org.geoazul.model.app.ApplicationEntity;
import org.geoazul.model.app.CompAttributeEntity;
import org.geoazul.model.app.ProjectBasicEntity;
import org.geoazul.model.basic.AbstractGeometry;
import org.geoazul.model.basic.Comp;
import org.geoazul.model.basic.GeometryStyle;
import org.geoazul.model.basic.LabelStyle;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.basic.LayerCategory;
import org.geoazul.model.basic.LayerEntity;
import org.geoazul.model.basic.LayerGroup;
import org.geoazul.model.basic.LayerLinestring;
import org.geoazul.model.basic.LayerLinestringCategory;
import org.geoazul.model.basic.LayerMultiPolygon;
import org.geoazul.model.basic.LayerMultiPolygonCategory;
import org.geoazul.model.basic.LayerPoint;
import org.geoazul.model.basic.LayerPointCategory;
import org.geoazul.model.basic.LayerPolygon;
import org.geoazul.model.basic.LayerPolygonCategory;
import org.geoazul.model.basic.LayerRasterGeoserver;
import org.geoazul.model.basic.LayerTile;
import org.geoazul.model.basic.LayerView;
import org.geoazul.model.basic.LayerWMSGeoserver;
import org.geoazul.model.basic.Linestring;
import org.geoazul.model.basic.MultiPolygon;
import org.geoazul.model.basic.Point;
import org.geoazul.model.basic.Polygon;
import org.geoazul.model.basic.Comp.IconcategoryId;
import org.geoazul.model.basic.properties.Field;
import org.geoazul.model.basic.properties.FieldControl;
import org.geoazul.model.basic.rural.GeometryGeometryId;
import org.geoazul.model.basic.rural.PolygonPoint;
import org.geoazul.model.security.ClientComponentEntity;
import org.geoazul.model.security.ClientEntity;
import org.geoazul.model.security.ClientIdentityEntity;
import org.geoazul.model.security.ClientMobileEntity;
import org.geoazul.model.security.ClientOAuthEntity;
import org.geoazul.model.security.ClientServiceEntity;
import org.geoazul.model.security.RealmEntity;
import org.example.kickoff.business.service.PersonService;
import org.example.kickoff.model.Credentials;
import org.example.kickoff.model.Person;
import org.geoazul.model.website.LayerItem;
import org.geoazul.model.website.ModuleFilter;
import org.geoazul.model.website.Modulo;
import org.geoazul.model.website.media.Media;
import org.geoazul.view.print.PrintPDF;
import org.geoazul.view.security.UserEntityBean;
import org.geoazul.view.utils.ConvertToASCII2;
import org.geoazul.view.utils.ProcessMemorial2;
import org.geoazul.view.utils.ProcessODS;
import org.geoazul.view.utils.ProcessProjection;
import org.geoazul.view.website.LayerBarFacade;
import org.geoazul.view.website.LayerGroupBarFacade;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.tool.schema.Action;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import jsonb.JacksonUtil;
import modules.LoadInitParameter;

import static java.time.Instant.now;
import static modules.LoadInitParameter.*;
import static org.example.kickoff.model.Group.USER;
import static org.example.kickoff.model.Group.ADMINISTRATOR;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.keycloak.example.oauth.UserData;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.ReorderEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.extensions.model.dynaform.DynaFormControl;
import org.primefaces.extensions.model.dynaform.DynaFormLabel;
import org.primefaces.extensions.model.dynaform.DynaFormModel;
import org.primefaces.extensions.model.dynaform.DynaFormRow;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.TreeNode;
import org.primefaces.refact.BookProperty;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.geoazul.view.upload.UploadProcessODSWGS84;
import org.geoazul.view.upload.UploadVertices;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.operation.distance.DistanceOp;
import org.geoazul.view.beta.ProcessAreaCartesian;
import org.geoazul.view.beta.ProcessAreaCartesian2;
import org.geoazul.view.beta.ProcessAzimuth;
import org.geoazul.model.app.ProjectRuralEntity;
import org.geoazul.model.app.ProjectUrbanEntity;
import org.w3c.dom.Document;
import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.kml.v22.KMLConfiguration;

@Named
@ViewScoped
public class ApplicationEntityBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
public void teste() {
		
		
		Properties properties = new Properties();
	        properties.put(Environment.JAKARTA_JDBC_URL, "jdbc:postgresql://" + pg_HOST + ":" + pg_PORT + "/" + pg_DAT);
	        properties.put(Environment.JAKARTA_JDBC_PASSWORD, pg_PASS);
	        properties.put(Environment.JAKARTA_JDBC_USER, pg_USER);
	        properties.put(Environment.HBM2DDL_AUTO, "none");
	        properties.put(Environment.C3P0_MIN_SIZE, "5");
	        properties.put(Environment.C3P0_MAX_SIZE, "30");
	        properties.put(Environment.C3P0_ACQUIRE_INCREMENT, "5");
	        properties.put(Environment.C3P0_TIMEOUT, "1800");
	        properties.put(Environment.C3P0_MAX_STATEMENTS, "0");
	        properties.put(Environment.C3P0_IDLE_TEST_PERIOD, "500"); 
	        properties.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");

	        Boolean TRUE = true;
	        
		SessionFactory sessionFactory = new Configuration()
		           
				    .addAnnotatedClass(org.example.kickoff.model.Person.class)
		    		.addAnnotatedClass(org.example.kickoff.model.Credentials.class)
		    		.addAnnotatedClass(org.example.kickoff.model.LoginToken.class)

		    		.addAnnotatedClass(com.erp.modules.inventory.entities.ErpProductUserMapping.class)
		    		.addAnnotatedClass(org.example.kickoff.model.Newsletter.class)

		    		.addAnnotatedClass(br.bancodobrasil.model.BancoBrasilApiToken.class)
		    		.addAnnotatedClass(br.bancodobrasil.model.BancoBrasilPixPayment.class)
		    		.addAnnotatedClass(org.geoazul.ecommerce.model.InstallmentMethod.class)

		    		.addAnnotatedClass(br.safrapay.api.model.SafraPayCharge.class)
		    		.addAnnotatedClass(br.safrapay.api.model.SafraPayTransaction.class)
		    		.addAnnotatedClass(br.safrapay.api.model.SafraPayApiToken.class)
		    		.addAnnotatedClass(br.safrapay.api.model.PersonCard.class)

		    		.addAnnotatedClass(br.safrapay.api.model.SafraPayCustomer.class)
		    		.addAnnotatedClass(br.safrapay.api.model.SafraPayCustomerPhone.class)

		    		.addAnnotatedClass(org.geoazul.ecommerce.model.Message.class)
		    		.addAnnotatedClass(com.erp.modules.shippingmethods.entities.ShippingMethod.class)
		    		.addAnnotatedClass(br.com.correios.CorreiosApiToken.class)

		    		.addAnnotatedClass(com.erp.modules.promocode.entities.PromoCode.class)
		    	

		    		.addAnnotatedClass(org.geoazul.ecommerce.view.shopping.ShoppingCartItem.class)
		    		.addAnnotatedClass(org.geoazul.ecommerce.view.shopping.ShoppingCart.class)

		    		.addAnnotatedClass(org.geoazul.erp.Countries.class)
		    		.addAnnotatedClass(org.geoazul.erp.Cities.class)
		    		.addAnnotatedClass(org.geoazul.erp.States.class)

		    	
		    		
		    		.addAnnotatedClass(org.geoazul.ecommerce.model.Item.class)
		    		.addAnnotatedClass(com.erp.modules.inventory.entities.Product.class)
		    		.addAnnotatedClass(com.erp.modules.inventory.entities.Manufacturer.class)

		    		.addAnnotatedClass(com.erp.modules.inventory.entities.ProductUomCategory.class)
		    		.addAnnotatedClass(com.erp.modules.inventory.entities.ProductUom.class)
		    		

		    		.addAnnotatedClass(com.erp.modules.inventory.entities.ProductAttribute.class)
		    		.addAnnotatedClass(com.erp.modules.inventory.entities.ProductMedia.class)
		    		.addAnnotatedClass(com.erp.modules.inventory.entities.Block.class)

		    		.addAnnotatedClass(com.erp.modules.inventory.entities.Inventory.class)

		    		.addAnnotatedClass(org.geoazul.model.Names.class)
		    		.addAnnotatedClass(org.geoazul.model.Contador.class)

		    		.addAnnotatedClass(org.geoazul.model.app.ApplicationEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.mobile.ApplicationMobileEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.app.ApplicationTemplateEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.app.ProjectRuralEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.app.ProjectUrbanEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.app.ProjectBasicEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.app.ApplicationAttributeEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.app.CompAttributeEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.app.AppUserMappingEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.app.AbstractIdentityEntity.class)

		    	

		    		.addAnnotatedClass(org.geoazul.model.basic.AbstractGeometry.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.Point.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.Polygon.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.Linestring.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.MultiPolygon.class)
		    		
		    		.addAnnotatedClass(org.geoazul.model.ctm.ParcelEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.ctm.ParcelUserMappingEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.AbstractWidget.class)

		    		.addAnnotatedClass(org.geoazul.model.website.LayerItem.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.Layer.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerLinestring.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerMultiPolygon.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerPoint.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerPolygon.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.LayerTile.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.LayerGeoserver.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerRasterGeoserver.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerWMSGeoserver.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.LayerGroup.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.LayerCategory.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerLinestringCategory.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerMultiPolygonCategory.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerPointCategory.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerPolygonCategory.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerPostItemCategory.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.properties.Field.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.rural.LayerPolygonPoint.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.rural.PolygonPoint.class)

		    		.addAnnotatedClass(org.geoazul.model.website.media.Media.class)
		    		.addAnnotatedClass(org.geoazul.model.app.ApplicationIdentityEntity.class)
		    	

		    		.addAnnotatedClass(org.geoazul.model.website.Document.class)

		    		.addAnnotatedClass(org.geoazul.model.website.ModuleComponentMap.class)

		    		.addAnnotatedClass(org.geoazul.model.website.ModuleMenuMap.class)
		    		.addAnnotatedClass(org.geoazul.model.website.Modulo.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.Comp.class)
		    		.addAnnotatedClass(org.geoazul.model.website.Component.class)

		    		.addAnnotatedClass(org.geoazul.model.website.UrlRegister.class)

		    		.addAnnotatedClass(org.geoazul.model.website.UrlMenu.class)
		    		.addAnnotatedClass(org.geoazul.model.website.UrlMenuItem.class)

		    		.addAnnotatedClass(org.geoazul.model.website.UrlPost.class)
		    		.addAnnotatedClass(org.geoazul.model.website.UrlPostItem.class)

		    		.addAnnotatedClass(org.geoazul.model.Address.class)
		    		.addAnnotatedClass(org.geoazul.model.security.ClientAttributeEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.ClientEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.security.ClientComponentEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.ClientIdentityEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.ClientOAuthEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.ClientMobileEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.ClientServiceEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.security.GroupEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.GroupRoleMappingEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.IdentityProviderEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.IdentityProviderMapperEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.KeycloakModelUtils.class)
		    		.addAnnotatedClass(org.geoazul.model.security.RealmAttributeEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.RealmEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.security.RequiredActionProviderEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.RoleAttributeEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.RoleEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.UserAttributeEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.UserRoleMappingEntity.class)
			            
			            
		    		
		    		.addAnnotatedClass(com.erp.modules.inventory.entities.ProductCategoryOne.class)
		    		.addAnnotatedClass(org.geoazul.model.app.ProductCategoryMapping.class)

		            .setProperties(properties)

		            // Automatic schema export
		            .setProperty(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION,
		                         Action.ACTION_CREATE_ONLY)
		            // SQL statement logging
		            
		            .setProperty(AvailableSettings.DEFAULT_SCHEMA, "geoazul_localhost")
		
		            .setProperty(AvailableSettings.SHOW_SQL, TRUE.toString())
		            .setProperty(AvailableSettings.FORMAT_SQL, TRUE.toString())
		            .setProperty(AvailableSettings.HIGHLIGHT_SQL, TRUE.toString())
		            // Create a new SessionFactory
		            .buildSessionFactory();
		
		sessionFactory.getSchemaManager().truncateMappedObjects();

		//sessionFactory.getSchemaManager().exportMappedObjects(false);
		
		// Re-create schema from mappings
		// getSchemaManager().exportMappedObjects(true);

		// Empty tables and reload initial data
		// getSchemaManager().truncateMappedObjects();
		
		
	
	}
	
	
		@Inject
		private PersonService personService;

		public void createFields() {
		  
		  String realmName = "Empresa Modelo";
			
			
			Properties properties = new Properties();
		        properties.put(Environment.JAKARTA_JDBC_URL, "jdbc:postgresql://" + pg_HOST + ":" + pg_PORT + "/" + pg_DAT);
		        properties.put(Environment.JAKARTA_JDBC_PASSWORD, pg_PASS);
		        properties.put(Environment.JAKARTA_JDBC_USER, pg_USER);
		        properties.put(Environment.HBM2DDL_AUTO, "none");
		        properties.put(Environment.C3P0_MIN_SIZE, "5");
		        properties.put(Environment.C3P0_MAX_SIZE, "30");
		        properties.put(Environment.C3P0_ACQUIRE_INCREMENT, "5");
		        properties.put(Environment.C3P0_TIMEOUT, "1800");
		        properties.put(Environment.C3P0_MAX_STATEMENTS, "0");
		        properties.put(Environment.C3P0_IDLE_TEST_PERIOD, "500"); 
		        properties.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
		     
		        Boolean TRUE = true;
			    SessionFactory sessionFactory = new Configuration()
			        
			        
			        .addAnnotatedClass(org.example.kickoff.model.Person.class)
		    		.addAnnotatedClass(org.example.kickoff.model.Credentials.class)
		    		.addAnnotatedClass(org.example.kickoff.model.LoginToken.class)

		    		.addAnnotatedClass(com.erp.modules.inventory.entities.ErpProductUserMapping.class)
		    		.addAnnotatedClass(org.example.kickoff.model.Newsletter.class)

		    		.addAnnotatedClass(br.bancodobrasil.model.BancoBrasilApiToken.class)
		    		.addAnnotatedClass(br.bancodobrasil.model.BancoBrasilPixPayment.class)
		    		.addAnnotatedClass(org.geoazul.ecommerce.model.InstallmentMethod.class)

		    		.addAnnotatedClass(br.safrapay.api.model.SafraPayCharge.class)
		    		.addAnnotatedClass(br.safrapay.api.model.SafraPayTransaction.class)
		    		.addAnnotatedClass(br.safrapay.api.model.SafraPayApiToken.class)
		    		.addAnnotatedClass(br.safrapay.api.model.PersonCard.class)

		    		.addAnnotatedClass(br.safrapay.api.model.SafraPayCustomer.class)
		    		.addAnnotatedClass(br.safrapay.api.model.SafraPayCustomerPhone.class)

		    		.addAnnotatedClass(org.geoazul.ecommerce.model.Message.class)
		    		.addAnnotatedClass(com.erp.modules.shippingmethods.entities.ShippingMethod.class)
		    		.addAnnotatedClass(br.com.correios.CorreiosApiToken.class)

		    		.addAnnotatedClass(com.erp.modules.promocode.entities.PromoCode.class)
		    	

		    		.addAnnotatedClass(org.geoazul.ecommerce.view.shopping.ShoppingCartItem.class)
		    		.addAnnotatedClass(org.geoazul.ecommerce.view.shopping.ShoppingCart.class)

		    		.addAnnotatedClass(org.geoazul.erp.Countries.class)
		    		.addAnnotatedClass(org.geoazul.erp.Cities.class)
		    		.addAnnotatedClass(org.geoazul.erp.States.class)

		    		.addAnnotatedClass(org.geoazul.ecommerce.model.Item.class)
		    		.addAnnotatedClass(com.erp.modules.inventory.entities.Product.class)
		    		.addAnnotatedClass(com.erp.modules.inventory.entities.Manufacturer.class)

		    		.addAnnotatedClass(com.erp.modules.inventory.entities.ProductUomCategory.class)
		    		.addAnnotatedClass(com.erp.modules.inventory.entities.ProductUom.class)

		 
		    		.addAnnotatedClass(com.erp.modules.inventory.entities.ProductAttribute.class)
		    		.addAnnotatedClass(com.erp.modules.inventory.entities.ProductMedia.class)
		    		.addAnnotatedClass(com.erp.modules.inventory.entities.Block.class)

		    		.addAnnotatedClass(com.erp.modules.inventory.entities.Inventory.class)

		    		.addAnnotatedClass(org.geoazul.model.Names.class)
		    		.addAnnotatedClass(org.geoazul.model.Contador.class)

		    		.addAnnotatedClass(org.geoazul.model.app.ApplicationEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.mobile.ApplicationMobileEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.app.ApplicationTemplateEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.app.ProjectRuralEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.app.ProjectUrbanEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.app.ProjectBasicEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.app.ApplicationAttributeEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.app.CompAttributeEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.app.AppUserMappingEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.app.AbstractIdentityEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.AbstractGeometry.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.Point.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.Polygon.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.Linestring.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.MultiPolygon.class)
		    		
		    		.addAnnotatedClass(org.geoazul.model.ctm.ParcelEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.ctm.ParcelUserMappingEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.AbstractWidget.class)

		    		.addAnnotatedClass(org.geoazul.model.website.LayerItem.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.Layer.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerLinestring.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerMultiPolygon.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerPoint.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerPolygon.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.LayerTile.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.LayerGeoserver.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerRasterGeoserver.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerWMSGeoserver.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.LayerGroup.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.LayerCategory.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerLinestringCategory.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerMultiPolygonCategory.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerPointCategory.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerPolygonCategory.class)
		    		.addAnnotatedClass(org.geoazul.model.basic.LayerPostItemCategory.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.properties.Field.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.rural.LayerPolygonPoint.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.rural.PolygonPoint.class)

		    		.addAnnotatedClass(org.geoazul.model.website.media.Media.class)
		    		.addAnnotatedClass(org.geoazul.model.app.ApplicationIdentityEntity.class)
		    	

		    		.addAnnotatedClass(org.geoazul.model.website.Document.class)

		    		.addAnnotatedClass(org.geoazul.model.website.ModuleComponentMap.class)

		    		.addAnnotatedClass(org.geoazul.model.website.ModuleMenuMap.class)
		    		.addAnnotatedClass(org.geoazul.model.website.Modulo.class)

		    		.addAnnotatedClass(org.geoazul.model.basic.Comp.class)
		    		.addAnnotatedClass(org.geoazul.model.website.Component.class)

		    		.addAnnotatedClass(org.geoazul.model.website.UrlRegister.class)

		    		.addAnnotatedClass(org.geoazul.model.website.UrlMenu.class)
		    		.addAnnotatedClass(org.geoazul.model.website.UrlMenuItem.class)

		    		.addAnnotatedClass(org.geoazul.model.website.UrlPost.class)
		    		.addAnnotatedClass(org.geoazul.model.website.UrlPostItem.class)

		    		.addAnnotatedClass(org.geoazul.model.Address.class)
		    		.addAnnotatedClass(org.geoazul.model.security.ClientAttributeEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.ClientEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.security.ClientComponentEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.ClientIdentityEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.ClientOAuthEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.ClientMobileEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.ClientServiceEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.security.GroupEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.GroupRoleMappingEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.IdentityProviderEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.IdentityProviderMapperEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.KeycloakModelUtils.class)
		    		.addAnnotatedClass(org.geoazul.model.security.RealmAttributeEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.RealmEntity.class)

		    		.addAnnotatedClass(org.geoazul.model.security.RequiredActionProviderEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.RoleAttributeEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.RoleEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.UserAttributeEntity.class)
		    		.addAnnotatedClass(org.geoazul.model.security.UserRoleMappingEntity.class)
	
		    		.addAnnotatedClass(com.erp.modules.inventory.entities.ProductCategoryOne.class)
		    		.addAnnotatedClass(org.geoazul.model.app.ProductCategoryMapping.class)
		    		
			        .setProperties(properties)
			            
			            // Automatic schema export
			            .setProperty(AvailableSettings.JAKARTA_HBM2DDL_DATABASE_ACTION, Action.NONE)
			            // SQL statement logging
			            
			            .setProperty(AvailableSettings.DEFAULT_SCHEMA, "geoazul_localhost")
			
			            .setProperty(AvailableSettings.SHOW_SQL, TRUE.toString())
			            .setProperty(AvailableSettings.FORMAT_SQL, TRUE.toString())
			            .setProperty(AvailableSettings.HIGHLIGHT_SQL, TRUE.toString())
			            // Create a new SessionFactory
			            .buildSessionFactory();
			
//---------------------------------------------------------------------------------------------------

			 
			 TypedQuery<ClientEntity> queryCli = entityManager
					 .createNamedQuery(ClientEntity.CLIENT_ALL, ClientEntity.class);
			 List<ClientEntity> clients = queryCli.getResultList();
			 
			
			
			 
			 EntityManager entityManager33 = sessionFactory.createEntityManager();

			 EntityTransaction tx = entityManager33.getTransaction();
			
			 
			    try {
			    	 tx.begin();

			        RealmEntity realmEntityNew = new RealmEntity();

			        	realmEntityNew.setDisplayName(realmName);
			        	realmEntityNew.setDisplayNameHtml(realmName);
			        	realmEntityNew.setEditUsernameAllowed(true);
			        	realmEntityNew.setEnabled(true);
			        	realmEntityNew.setFather(null);
			        	realmEntityNew.setLoginWithEmailAllowed(true);
			        	realmEntityNew.setName(realmName);
			        	realmEntityNew.setRegistrationAllowed(true);
			        	realmEntityNew.setRegistrationEmailAsUsername(true);
			        	realmEntityNew.setRememberMe(true);
			        	realmEntityNew.setResetPasswordAllowed(true);
			     
			        	realmEntityNew.setVerifyEmail(true);
			        	   entityManager33.persist(realmEntityNew);
			        	   
			        	   
			        	
			       		
			       		Person personNew = new Person();

			       		
			       		
			       		personNew.setUuid(UUID.randomUUID());
			       		personNew.setName("LAERCIO");
			       		personNew.setFullName("LAERCIO TISCHER");
			       		personNew.setLastName("TISCHER");
			       		personNew.setUsername("LAERCIO");
			       		personNew.setEmail("admin@admin.com");
			       		personNew.setEnabled(true);
			       		personNew.setEmailVerified(true);
			       		
			       		Instant timestamp = now();
			       		
			       		personNew.setLastModified(timestamp);
			       		personNew.setLastLogin(timestamp);
			       		
	
			       		personNew.setDebit(0D);
			       		personNew.setCredit(0D);
			 

			       	
					
					
			       	personNew.getGroups().add(USER);
			      	personNew.getGroups().add(ADMINISTRATOR);
					
			        entityManager33.persist(personNew);
			        entityManager33.flush();
			        tx.commit();
			        tx.begin();
						Credentials credentials = new Credentials();
						credentials.setPerson(personNew);
					
					String password =  "12345678";
					credentials.setPassword(password);
					
					entityManager33.persist(credentials);
					  entityManager33.flush();
					  tx.commit();
					  
					  tx.begin();
//---------------------------------------------------------------------------------------------------
			        for (ClientEntity cliEntity: queryCli.getResultList()) {
			        	
			        	ClientEntity clientEntityNew = null;
			        	switch (cliEntity.getClass().getSimpleName()) {
			        	case "ClientComponentEntity":
			        		clientEntityNew = new ClientComponentEntity();
			    		break;
			        	case "ClientIdentityEntity":
			        		clientEntityNew = new ClientIdentityEntity();
			    		break;
			        	case "ClientMobileEntity":
			        		clientEntityNew = new ClientMobileEntity();
			    		break;
			        	case "ClientOAuthEntity":
			        		clientEntityNew = new ClientOAuthEntity();
			    		break;
			        	case "ClientServiceEntity":
			        		clientEntityNew = new ClientServiceEntity();
			    		break;
			        	}

			        	clientEntityNew.setClientId(cliEntity.getClientId());
			        	clientEntityNew.setDescription(cliEntity.getDescription());
			        	clientEntityNew.setDtype(cliEntity.getDtype());
			        	clientEntityNew.setEnabled(cliEntity.getEnabled());
			        	clientEntityNew.setImageUrl(cliEntity.getImageUrl());
			        	clientEntityNew.setIntroduction(cliEntity.getIntroduction());
			        	clientEntityNew.setPassword(cliEntity.getPassword());
			        	clientEntityNew.setRealm(realmEntityNew);
			        	clientEntityNew.setSecret(cliEntity.getSecret());
			        	clientEntityNew.setServerName(cliEntity.getServerName());
			        	clientEntityNew.setSystem(cliEntity.getSystem());
			        	clientEntityNew.setUsername(cliEntity.getUsername());

			        	   entityManager33.persist(clientEntityNew);
			        }
//---------------------------------------------------------------------------------------------------			      
			        entityManager33.getTransaction().commit();

			    } catch (Exception ex) {
			        ex.printStackTrace();
			    	entityManager33.getTransaction().rollback();
			        
			    } finally {
			    	
			        entityManager33.close();
			        sessionFactory.close();
			    }
				
//---------------------------------------------------------------------------------------------------
		
		}
		
		
	
	
	

	public ApplicationEntityBean() {
		super();
		layerWizard = new LayerWizard();
	}

	@Inject
	@Push
	PushContext helloChannel;

	@Inject
	@Param(pathIndex = 0)
	private Long gcmid;

	@Inject
	@Param(pathIndex = 1)
	private Long id;

	@Transactional
	public void kmlBacen() throws JAXBException {

		String consultax = "SELECT '<?xml version=\"1.0\" encoding=\"UTF-8\"?>' ||\n" + "xmlelement(\n"
				+ "    NAME kml,\n" + "    XMLATTRIBUTES ('http://www.opengis.net/kml/2.2' AS xmlns),\n"
				+ "    xmlelement(\n" + "        NAME \"Document\",\n"
				+ "        xmlelement(NAME NAME, 'Area_Financiada_SICOR'),\n" + "            xmlagg(\n"
				+ "                xmlelement(\n" + "                    NAME \"Placemark\",\n"
				+ "                    xmlelement(NAME NAME, 'Poligono'),\n"
				+ "                    public.ST_AsKML(public.ST_Transform(public.ST_Force3D(geometry) , 4326)) \\:\\: XML\n"
				+ "                )\n" + "            )\n" + "        )\n" + "    )\n" + "    FROM " + this.tenant
				+ ".app_polygon\n" + "	where id = :id";

		Query query = entityManager.createNativeQuery(consultax, Object.class);
		query.setParameter("id", this.abstractGeometry.getId());
		Object result = query.getSingleResult();
		String title = (String) result;



		String prettyXml = indentXmlString(title);


		String fileNameOnly = "";

		Random random = new Random();
		int randomInt = random.nextInt(1000000000);

		LocalDateTime data = LocalDateTime.now();

		fileNameOnly = "kml_bacen_" + "-" + String.valueOf(randomInt) + ".kml";
		String appDirectory = save_FILE_PATH + "/files/" + userData.getRealmEntity() + "/media/";
		File appDirImg = new File(appDirectory);
		if (!appDirImg.exists()) {
			appDirImg.mkdir();
		}
		String fileNameNew = appDirectory + "/" + fileNameOnly;

		try {
			// Get the Path object for the file
			Path filePath = Paths.get(fileNameNew);

			// Write the string to the file
			Files.writeString(filePath, prettyXml);


		} catch (IOException e) {
			// Handle I/O exceptions (e.g., file permission issues, disk full)
			System.err.println("An error occurred: " + e.getMessage());
			e.printStackTrace();
		}

		Media mediaNew = new Media();
		mediaNew.setFilename("/files/" + userData.getRealmEntity() + "/media/");
		mediaNew.setTitle(fileNameOnly);

		String filename = save_FILE_PATH + mediaNew.getFilename() + fileNameOnly;

		TikaConfig tikaConfig = TikaConfig.getDefaultConfig();

		Metadata metadata = new Metadata();
		String text = null;
		try {
			text = parseUsingComponents(filename, tikaConfig, metadata);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String contentType = metadata.get("Content-Type");
		mediaNew.setMimeType(contentType);

		mediaNew.setReleaseDate(LocalDateTime.now());

		mediaNew.setAbstractGeometry(abstractGeometry);

		entityManager.persist(mediaNew);

		this.abstractGeometry.getMedias().add(mediaNew);
		entityManager.merge(this.abstractGeometry);

		entityManager.flush();

	}

	public static String indentXmlString(String unformattedXml) {
		try {
			// Parse the XML string into a DOM Document
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			// Optional: set this to true to ignore whitespace between elements in the
			// original document,
			// which can help with clean formatting.
			// dbf.setIgnoringElementContentWhitespace(true);
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(new InputSource(new StringReader(unformattedXml)));

			// Use a Transformer to output the Document with indentation
			TransformerFactory tf = TransformerFactory.newInstance();
			// Optional: try setting "indent-number" attribute for specific indentation
			// levels (e.g., 4 spaces)
			// tf.setAttribute("indent-number", Integer.valueOf(4));
			Transformer t = tf.newTransformer();
			t.setOutputProperty(OutputKeys.INDENT, "yes");
			t.setOutputProperty(OutputKeys.METHOD, "xml");
			// Optional: specify encoding
			// t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			// Optional: Omit XML declaration if not needed (e.g., <?xml version="1.0"
			// ...?>)
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

	@Transactional
	public void sendMessage(String messageJ) {
		String[] splitter = messageJ.split(",");
		Integer valX = Integer.valueOf(splitter[1]);
		valX++;
		Query query = entityManager.createNamedQuery(AbstractIdentityEntity.CHANGE_LIKE);
		query.setParameter("likes", valX);
		query.setParameter("appid", Long.valueOf(splitter[2]));
		query.executeUpdate();
		entityManager.flush();

		String retorno = "{\"index\":" + splitter[0] + ", \"value\":" + valX + "}";
		this.sendPushMessage(retorno);
	}

	// valor entre 0 a 100 que sera usado no progressbar
	private Integer progresso;
	// mensagem de notas sendo processadas ou canceladas
	private String mensagem;
	// quantidade de notas que o usuario informou
	private Integer quantidadeNotas;
	// notas processadas
	private List<String> notas;

	private List<String> processos;

	/**
	 * valores padrao de quantidade e notas
	 */
	/**
	 * cria uma mensagem que sera exibida para usuario atraves do componente
	 * p:messages
	 * 
	 * @param texto mensagem informada
	 */
	private void criarMensagem(String texto) {

		FacesMessage msg = new FacesMessage(texto);
		FacesContext.getCurrentInstance().addMessage("", msg);
	}

	// reseta o progresso e a mensagem do progressbar
	private void resetarProgresso() {
		progresso = 0;
		mensagem = "";
	}

	/**
	 * atualiza o progresso
	 * 
	 * @param i posicao da nota na lista
	 */
	private void atualizarProgresso(int i) {
		// calculo para o percentual do processo em relacao a quantidade de notas
		progresso = (i * 100) / quantidadeNotas;
		try {
			Thread.sleep(1500);
		} catch (Exception ex) {
			criarMensagem("erro ");
		}
	}

	/**
	 * processa as notas,podendo ser adicionadas ou canceladas
	 * 
	 * @param acao 1 = notas serao processadas 2 = notas serao canceladas
	 */
	public void processarNotas(String schemaName) {
		try {
			resetarProgresso();

			notas = new ArrayList<>();

			mensagem = "Processo 1 de 4 (Contruindo Tenant)";
			progresso = 0;
			atualizarProgresso(1);

			atualizarProgresso(4);

			criarMensagem("Finalizando Instalação!");
			resetarProgresso();

			FacesContext context = FacesContext.getCurrentInstance();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Projeto UP !"));

			try {

				context.getExternalContext().redirect("search.xhtml");

			} catch (IOException ex) {

				ex.printStackTrace();
			}


		} catch (Exception e) {
			criarMensagem("erro :");
		}
	}

	public Integer getProgresso() {
		if (progresso == null) {
			progresso = 0;
		}
		return progresso;
	}

	public void setProgresso(Integer progresso) {
		this.progresso = progresso;
	}

	public String getMensagem() {
		if (mensagem == null) {
			mensagem = "";
		}
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Integer getQuantidadeNotas() {
		return quantidadeNotas;
	}

	public void setQuantidadeNotas(Integer quantidadeNotas) {
		this.quantidadeNotas = quantidadeNotas;
	}

	public List<String> getNotas() {
		return notas;
	}

	public void setNotas(List<String> notas) {
		this.notas = notas;
	}

	@Inject
	private HttpServletRequest request;

	@Inject
	EntityManager entityManager;

	public AbstractIdentityEntity getAbstractIdentityEntity(Long entityId) {
		try {
			this.applicationEntity = entityManager.find(ApplicationEntity.class, entityId);

			this.applicationEntity.getAttributes().size();
			appLayers = new ArrayList<LayerView>();
			
			for (Layer lnew : this.applicationEntity.getLayers()) {
				LayerView layerView = new LayerView();
				layerView.setLayerid(lnew.getId().toString());
				layerView.setVisible(lnew.getSelected());
				layerView.setOpacity(lnew.getOpacity());
				layerView.setZIndex(lnew.getOrderlayer());
				layerView.setLayerhash(lnew.layerString());
				layerView.setName(lnew.getName());
				appLayers.add(layerView);
			}
			model = new DynaFormModel();
			return this.applicationEntity;

		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Inject
	private UserEntityBean userEntityBean;

	@PostConstruct
	public void init() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (this.id == null) {
			this.applicationEntity = new ProjectBasicEntity();

		} else {

			FacesContext facesContext = FacesContext.getCurrentInstance();

			try {
				String servername = request.getServerName().replace("-", "_").replace(".", "_");
				tenant = "geoazul_" + servername;

				this.applicationEntity = entityManager.find(ApplicationEntity.class, id);

				// this.applicationEntity = entityManager.find(ApplicationEntity.class,
				// Long.valueOf(id));

				this.applicationEntity.getAttributes().size();

				appLayers = new ArrayList<LayerView>();
				for (Layer lnew : this.applicationEntity.getLayers().stream().filter(l -> l.getFather() == null)
						.toList()) {
					LayerView layerView = new LayerView();
					layerView.setLayerid(lnew.getId().toString());
					layerView.setVisible(lnew.getSelected());
					layerView.setOpacity(lnew.getOpacity());
					layerView.setZIndex(lnew.getOrderlayer());
					layerView.setLayerhash(lnew.layerString());
					layerView.setName(lnew.getName());

					boolean result = (lnew instanceof LayerGroup) ? true : false;
					layerView.setFather(result);
					appLayers.add(layerView);
				}

				model = new DynaFormModel();

				isOwner = this.applicationEntity.getShareds().stream()
						.filter(c -> c.getAppRole() == AppRole.OWNER && c.getPerson() == userEntityBean.getPerson())
						.findFirst().isPresent();
			} catch (Exception e1) {
				facesContext.addMessage(null, new FacesMessage("Erro", "ProJeto Inesistente!"));
			}
		}
	}

	private boolean isOwner;

	public boolean isOwner() {
		return isOwner;
	}

	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}

	private String tenant;

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	// ----

	private String idObra;

	public String getIdObra() {
		return idObra;
	}

	public void setIdObra(String idObra) {

		try {

			Polygon obraFind = entityManager.find(Polygon.class, idObra);
			this.obra = obraFind;
			this.idObra = idObra;
		} catch (Exception e) {
			this.idObra = idObra;
		}
	}

	@Inject
	private UserData userData;

	public String create() {
		return "create?faces-redirect=true";
	}

	private ApplicationEntity applicationEntity;

	public ApplicationEntity getApplicationEntity() {
		return this.applicationEntity;
	}

	public void setApplicationEntity(ApplicationEntity applicationEntity) {
		this.applicationEntity = applicationEntity;
	}

	private String objectId;

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.abstractGeometry = findAbstractGeometry(Long.valueOf(objectId));
		this.objectId = objectId;
	}

	private AbstractGeometry abstractGeometry;

	public AbstractGeometry getAbstractGeometry() {
		return abstractGeometry;
	}

	public void setAbstractGeometry(AbstractGeometry abstractGeometry) {
		this.abstractGeometry = abstractGeometry;
	}

	public String getApplicationImg(String name) {
		if (this.getApplicationEntity() != null) {
			for (ApplicationAttributeEntity attr : this.getApplicationEntity().getAttributes()) {
				if (attr.getName().equals(name)) {
					return attr.getValue();
				}
			}
		}
		return null;
	}

	private Predicate[] getSearchPredicatesWithoutTilesAndThis(Root<Layer> layerRoot, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		predicatesList.add(builder.notEqual(layerRoot.type(), builder.literal(LayerTile.class)));

		if (this.applicationEntity != null) {
			predicatesList.add(builder.equal(layerRoot.get("applicationEntity"), this.applicationEntity));
		}

		if (this.getSelectedLayer() instanceof Layer) {
			Layer layer = this.getSelectedLayer();
			if (layer != null) {
				predicatesList.add(builder.notEqual(layerRoot.get("id"), layer.getId()));
			}
		}
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	// ==========================================
	private boolean edited = false;

	public boolean isEdited() {
		return edited;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
	}

	/**
	 * <p/>
	 * This method add a vertex to edited parcel and refresh the "order list"
	 * because new sequence are defined Since: Version 1.1.0
	 */

	@Transactional
	public void updateVO2() {
		if (this.isEdited()) {

			try {

				entityManager.merge(editedVerticeObra);
				entityManager.flush();
				entityManager.refresh(this.obra);

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO!", "VÉRTICE ALTERADO!"));

			} catch (Exception e) {

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("ERRO INESPERADO", "VÉRTICE NÃO INSERIDO!"));

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "ERRO INESPERADO!"));

			}

		} else {
			Long vid = this.getEditedVerticeObra().getPointri().getId();
			String nomevid = this.getEditedVerticeObra().getPointri().getNome();
			GeometryGeometryId testeVOID = new GeometryGeometryId();
			testeVOID.setPointriId(vid);
			testeVOID.setPolygonriId(obra.getId());

			editedVerticeObra.setPolygonri(obra);
			editedVerticeObra.setId(testeVOID);
			editedVerticeObra.setSequencia(0);
			editedVerticeObra.setAzimuth("0");
			editedVerticeObra.setCns(this.getEditedVerticeObra().getCns());
			editedVerticeObra.setDescritivo(this.getEditedVerticeObra().getDescritivo());
			editedVerticeObra.setDistancia("0");
			editedVerticeObra.setMatricula(this.getEditedVerticeObra().getMatricula());
			editedVerticeObra.setTipoLimite(this.getEditedVerticeObra().getTipoLimite());
			obra.addVerticeObra(editedVerticeObra);

			try {

				editedVerticeObra.setMarcoLabel(nomevid);
				entityManager.persist(editedVerticeObra);
				entityManager.flush();
				entityManager.refresh(this.obra);

				List<PolygonPoint> frontListUVO = obra.getVerticeObrasList();
				Iterator iterUVO = frontListUVO.iterator();
				PolygonPoint verticeobra = entityManager.find(PolygonPoint.class, verticeobraId);
				while (iterUVO.hasNext()) {
					PolygonPoint verticeobra_ = (PolygonPoint) iterUVO.next();

					// "-SEQ -> " + verticeobra_.getSequencia());

					if (verticeobra_.getSequencia() == 0) {
						verticeobra_.setSequencia(verticeobra.getSequencia() + 1);
					} else if (verticeobra_.getSequencia() > verticeobra.getSequencia()) {
						verticeobra_.setSequencia(verticeobra_.getSequencia() + 1);
					}

					entityManager.merge(verticeobra_);
					entityManager.flush();
				}

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "VÉRTICE INSERIDO!"));

				entityManager.refresh(this.obra);


			} catch (Exception e) {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "ERRO INESPERADO!"));

			}

		}

	}

	private PolygonPoint editedVerticeObra;

	public PolygonPoint getEditedVerticeObra() {
		return editedVerticeObra;
	}

	public void setEditedVerticeObra(PolygonPoint editedVerticeObra) {
		this.editedVerticeObra = editedVerticeObra;
	}

	private String verticeHashId;

	public String getVerticeHashId() {
		return verticeHashId;
	}

	public void setVerticeHashId(String verticeHashId) {


		String[] splitter = verticeHashId.split("-");



		Long oid = Long.valueOf(splitter[0]);
		Long vid = Long.valueOf(splitter[1]);

		GeometryGeometryId testeVOID = new GeometryGeometryId();

		testeVOID.setPointriId(vid);
		testeVOID.setPolygonriId(oid);

		verticeobraId = testeVOID;

		PolygonPoint verticeobra = findPolygonPoint(verticeobraId);

		this.editedVerticeObra = verticeobra;

		// this.setSelectedParcelaId(verticeobra.getId().getPolygonriId());
		this.verticeHashId = verticeHashId;
	}

	private GeometryGeometryId verticeobraId;

	public GeometryGeometryId getVerticeobraId() {
		return this.verticeobraId;
	}

	public void setVerticeobraId(GeometryGeometryId verticeobraId) {

		Long vid = selectedVO;
		Long oid = selectedParcelaId;

		GeometryGeometryId testeVOID = new GeometryGeometryId();

		testeVOID.setPointriId(vid);
		testeVOID.setPolygonriId(oid);

		verticeobraId = testeVOID;

		PolygonPoint verticeobra = findPolygonPoint(verticeobraId);

		this.editedVerticeObra = verticeobra;

		this.verticeobraId = verticeobraId;

	}

	private Long selectedParcelaId;

	public Long getSelectedParcelaId() {
		return selectedParcelaId;
	}

	public void setSelectedParcelaId(Long selectedParcelaId) {
		this.selectedParcelaId = selectedParcelaId;
	}

	private Long selectedVO;

	public Long getSelectedVO() {
		return selectedVO;
	}

	public void setSelectedVO(Long selectedVO) {
		this.selectedVO = selectedVO;
	}

	public void newVO() {
		editedVerticeObra = new PolygonPoint();
		this.setSelectedVO(null); // FIXME
	}

	public Polygon findPolygonByOriginId(Long id) {
		try {
			TypedQuery<Polygon> queryAbs = entityManager.createNamedQuery(Polygon.SURFACE_ORIGIN_ID, Polygon.class);
			queryAbs.setParameter("originId", id);
			return queryAbs.getSingleResult();
		} catch (Exception ex) {
			return new Polygon();
		}
	}
	
	


	@Transactional
	public void updateObra() {

		if (this.obra == null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Geometria Inexistente!"));
		} else {
			try {

				entityManager.merge(this.obra);
				entityManager.flush();
				
				
				

				if (this.obra.getSituacao() > 1 && this.obra.getOrigin() != null) {

					Polygon obraNew = findPolygonByOriginId(this.obra.getId());

					obraNew.setArea(this.obra.getArea());
					
					obraNew.setEnabled(this.obra.getEnabled());
					obraNew.setFather(null);
					obraNew.setGeometry(this.obra.getGeometry());
					obraNew.setGeometrywkt(this.obra.getGeometrywkt());
					obraNew.setIconflag(this.obra.getIconflag());
					obraNew.setNome(this.obra.getNome());
					obraNew.setOrigin(this.obra);
					obraNew.setParte(this.obra.getParte());
					obraNew.setSituacao(this.obra.getSituacao());
					obraNew.setStrings(this.obra.getStrings());

					obraNew.setLayer(this.obra.getLayer().getOrigin());
					obraNew.setPerimetro(this.obra.getPerimetro());

					entityManager.merge(obraNew);
					entityManager.flush();


					String retorno = "{\"appId\":\"" + obraNew.getLayer().getApplicationEntity().getId()
							+ "\", \"parcelaId\":\"" + obraNew.getId() + "\"}";
					
					this.sendPushMessage(retorno);

				}
			
				
				String classLayer = "LayerEntity";
				if (!(this.obra.getLayer() instanceof LayerEntity)) {
					classLayer = "LayerStyled";
				}

				
				PrimeFaces.current().ajax().addCallbackParam("classLayer", classLayer);
				PrimeFaces.current().ajax().addCallbackParam("namelayer", this.obra.getLayer().getId().toString());
				PrimeFaces.current().ajax().addCallbackParam("typelayer", this.obra.getLayer().getDtype());
				
			

			} catch (Exception e) {
				e.printStackTrace();
			}

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Parcela Alterada!"));
		}
	}

	@Inject
	@Push
	PushContext helloWebsocket;

	private void sendPushMessage(Object message) {
		helloWebsocket.send(message);
	}

	public void sendPushTest() {
		String retorno = "{\"index\":" + "\"JESUS TANKS\"" + ", \"value\":" + "\"GOD IS TRUST\"" + "}";
		this.sendPushMessage(retorno);
	}

	// -------------------------------------------------------------------
	@Transactional
	public void processarObraProj(String finalFilesUrl, Collection<ApplicationAttributeEntity> appatribs,
			Polygon obraCAP, EntityManager session) throws FileNotFoundException, IOException {


		this.runCreate(obraCAP, session);
		String appDirectory = save_FILE_PATH + "/files/" + userData.getRealmEntity() + "/"
				+ this.applicationEntity.getId();
		boolean baseGeometryDir = (new File(appDirectory)).mkdir();

		String appDirectory2 = appDirectory + "/" + obraCAP.getLayer().getId();
		boolean baseGeometryDir2 = (new File(appDirectory2)).mkdir();

		String saveFileProcessFilePath_ = appDirectory2 + "/" + obraCAP.getId();
		boolean baseGeomDir = (new File(saveFileProcessFilePath_)).mkdir();

		String saveFileProcessFilePathUrl_ = "/files/" + userData.getRealmEntity() + "/"
				+ this.applicationEntity.getId() + "/" + obraCAP.getLayer().getId() + "/" + obraCAP.getId();


		if (this.applicationEntity instanceof ApplicationEntity) {

			ProcessODS processODS = new ProcessODS();
			String returnProcesODS = processODS.runProcessODS(obraCAP, entityManager, saveFileProcessFilePath_,
					this.applicationEntity.getEpsg().toString());

		}


		// ProcessMemorial processMemorial = new ProcessMemorial();

		// try {
		// processMemorial.runProcessMemorial(obraCAP, session,
		// saveFileProcessFilePath_);
		// } catch (BirtException e) {
		// e.printStackTrace();
		// }

		ProcessMemorial2 processMemorial2 = new ProcessMemorial2();
		try {


			processMemorial2.createLineString(finalFilesUrl, appatribs, obraCAP, session, "Planta",
					obraCAP.getLayer().getName(), saveFileProcessFilePath_, saveFileProcessFilePathUrl_, tenant);

		} catch (Exception e1) {

			e1.printStackTrace();
		}

		// redundancia na geracao das views //TODO

		// try {
		// printPDFTransaction(obraCAP, saveFileProcessFilePath_,
		// saveFileProcessFilePathUrl_);
		// } catch (FactoryException e) {
		// e.printStackTrace();
		// }

	}

	private Geometry buffer(Geometry geometry, double distance) {
		// Geometry pGeom = JTS.transform(geom, transform)
		Geometry pBuffer = geometry.buffer(distance);
		return pBuffer;
	}

	@Transactional
	public void printPDFTransaction(AbstractGeometry polygonObra, String saveFileProcessFilePath_,
			String saveFileProcessFilePathUrl_)
			throws FileNotFoundException, IOException, NoSuchAuthorityCodeException, FactoryException {


		try {

			String finalLogoUrl = null;
			if (this.getApplicationImg("logoImageMap") == null) {
				finalLogoUrl = userData.getGeoazulURL() + "/resources/Image/g_logoImageMap.png";
			} else {
				finalLogoUrl = userData.getGeoazulURL() + this.getApplicationImg("logoImageMap");
			}

			PrintPDF printPDF = new PrintPDF();

			Polygon polPrint = null;
			try {

				polPrint = entityManager.find(Polygon.class, polygonObra.getId());

				Envelope father_env_buffer = buffer(polPrint.getGeometry(), 10).getEnvelopeInternal();


				Coordinate centro = father_env_buffer.centre();
				// ----------

				double x1 = father_env_buffer.getMinX();
				double y1 = father_env_buffer.getMinY();
				double x2 = father_env_buffer.getMaxX();
				double y2 = father_env_buffer.getMaxY();



				int dimen = 0;
				if ((x2 - x1) / (y2 - y1) < 1.5) {
					dimen = (int) ((y2 - y1) / 4);

					x1 = centro.x - (dimen * 3);
					x2 = centro.x + (dimen * 3);

					y1 = centro.y - dimen * 2;
					y2 = centro.y + dimen * 2;

				} else {
				}





				String x_bbox = x1 + "," + y1 + "," + x2 + "," + y2;


				double escala = ((x2 - x1) / 2) * 10;

				Integer escalaInteger = (int) escala;

				String escalaFormat = "";
				if (escalaInteger < 25) {
					escalaFormat = escalaInteger.toString();
				} else if (escalaInteger >= 25 && escala < 75) {
					escalaFormat = "50";
				} else if (escalaInteger >= 75 && escala < 125) {
					escalaFormat = "100";
				} else if (escalaInteger >= 125 && escala < 175) {
					escalaFormat = "150";
				} else if (escalaInteger >= 175 && escala < 225) {
					escalaFormat = "200";
				} else if (escalaInteger >= 225 && escala < 275) {
					escalaFormat = "250";
				} else if (escalaInteger >= 275 && escala < 325) {
					escalaFormat = "300";
				} else if (escalaInteger >= 325 && escala < 375) {
					escalaFormat = "350";
				} else if (escalaInteger >= 375 && escala < 425) {
					escalaFormat = "400";
				} else if (escalaInteger >= 425 && escala < 475) {
					escalaFormat = "450";
				} else if (escalaInteger >= 475 && escala < 525) {
					escalaFormat = "500";
				} else if (escalaInteger >= 525 && escala < 575) {
					escalaFormat = "550";
				} else if (escalaInteger >= 575 && escala < 625) {
					escalaFormat = "600";
				} else if (escalaInteger >= 625 && escala < 675) {
					escalaFormat = "650";
				} else if (escalaInteger >= 675 && escala < 725) {
					escalaFormat = "700";
				} else if (escalaInteger >= 725 && escala < 775) {
					escalaFormat = "750";
				} else if (escalaInteger >= 775 && escala < 825) {
					escalaFormat = "800";
				} else if (escalaInteger >= 825 && escala < 875) {
					escalaFormat = "850";
				} else if (escalaInteger >= 875 && escala < 925) {
					escalaFormat = "900";
				} else if (escalaInteger >= 925 && escala < 975) {
					escalaFormat = "950";
				} else if (escalaInteger >= 975 && escala < 1500) {
					escalaFormat = "1000";
				} else if (escalaInteger >= 1500 && escala < 2500) {
					escalaFormat = "2000";
				} else if (escalaInteger >= 2500 && escala < 3500) {
					escalaFormat = "3000";
				} else if (escalaInteger >= 3500 && escala < 4500) {
					escalaFormat = "4000";
				} else if (escalaInteger >= 4500 && escala < 5500) {
					escalaFormat = "5000";
				} else if (escalaInteger >= 5500 && escala < 6500) {
					escalaFormat = "6000";
				} else if (escalaInteger >= 6500 && escala < 7500) {
					escalaFormat = "7000";
				} else if (escalaInteger >= 7500 && escala < 8500) {
					escalaFormat = "8000";
				} else if (escalaInteger >= 8500 && escala < 9500) {
					escalaFormat = "9000";
				} else if (escalaInteger >= 9500 && escala <= 10000) {
					escalaFormat = "10000";
				} else if (escalaInteger > 10000) {
					escalaFormat = "1000.";
				}


				String x_sistcord = "2";

				String x_scale = "1:" + escalaFormat;

				String x_modelo = polPrint.getLayer().getName();
				String x_colorpopup = "3";
				String x_paper = "4";
				String x_backgound = "1";




				printPDF.printRun33(userData.getGeoazulURLFiles(), this.getApplicationEntity().getAttributes(),
						finalLogoUrl, entityManager, this.applicationEntity, polPrint, "", x_bbox, x_sistcord, x_scale,
						"Planta", polPrint.getLayer().getName(), x_colorpopup, x_paper, x_backgound, null,
						save_FILE_PATH, false, saveFileProcessFilePath_, saveFileProcessFilePathUrl_, null,
						this.tenant);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			entityManager.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// -------------------------------------------------------------------

	public List<Map<String, ColumnModel>> getTableData() { // TODO

		try {

			if (this.getSelectedLayer() != null) {

				tableData = new ArrayList<Map<String, ColumnModel>>();
				tableHeaderNames = new ArrayList<ColumnModel>();
				tableHeaderNames.add(new ColumnModel("Id", "Id"));
				tableHeaderNames.add(new ColumnModel("Camada", "Camada"));
				tableHeaderNames.add(new ColumnModel("Nome", "Nome"));
				tableHeaderNames.add(new ColumnModel("Icon", "Icon"));

				List<Field> fieldsProperties = this.getSelectedLayer().getFields();

				for (Field columnKey : fieldsProperties) {
					tableHeaderNames.add(new ColumnModel(columnKey.getName(), columnKey.getName()));
				}

				ObjectNode node = null;
				List<AbstractGeometry> layerReloadLoad = this.getPaginateGeometries();
				AbstractGeometry abstractGeometryLoad;

				for (AbstractGeometry geomRegs : layerReloadLoad) {

					abstractGeometryLoad = findAbstractGeometry(geomRegs.getId());

					Map<String, ColumnModel> playlist = new HashMap<String, ColumnModel>();

					playlist.put(tableHeaderNames.get(0).key,
							new ColumnModel(tableHeaderNames.get(0).key, abstractGeometryLoad.getId().toString()));

					playlist.put(tableHeaderNames.get(1).key, new ColumnModel(tableHeaderNames.get(1).key,
							abstractGeometryLoad.getLayer().layerString()));

					playlist.put(tableHeaderNames.get(2).key,
							new ColumnModel(tableHeaderNames.get(2).key, abstractGeometryLoad.getNome()));

					playlist.put(tableHeaderNames.get(3).key,
							new ColumnModel(tableHeaderNames.get(3).key, abstractGeometryLoad.getIconflag()));

					try {
						node = new ObjectMapper().readValue(abstractGeometryLoad.getStrings().toString(),
								ObjectNode.class);
						int stringKeyIndex = 4;
						for (Field columnKey : fieldsProperties) {
							playlist.put(tableHeaderNames.get(stringKeyIndex).key,
									new ColumnModel(tableHeaderNames.get(stringKeyIndex).key,
											node.get(columnKey.getName()).textValue()));
							stringKeyIndex++;
						}

					} catch (IOException e) {
					} catch (NullPointerException np) {

					}

					tableData.add(playlist);

				}

				columns = new ArrayList<ColumnModel>();
				columns.add(new ColumnModel("Id", "Id"));
				columns.add(new ColumnModel("Camada", "Camada"));
				columns.add(new ColumnModel("Nome", "Nome"));
				columns.add(new ColumnModel("Icon", "Icon"));

				for (Field columnKey : fieldsProperties) {
					columns.add(new ColumnModel(columnKey.getName(), columnKey.getName()));
				}
				this.setTableHeaderNames(columns);
				return tableData;
			} else {
				return new ArrayList<Map<String, ColumnModel>>();
			}

		} catch (Throwable e) {
			try {
				throw e;
			} catch (Throwable e1) {

				return new ArrayList<Map<String, ColumnModel>>();
			}
		}

	}

	// --------------------------------------------------------------------------------------------------------
	// FIXME

	private Polygon obra;

	public Polygon getObra() {
		return this.obra;
	}

	public void setObra(Polygon obra) {
		this.obra = obra;
	}

	// ----

	// -------------------

	private List<Polygon> obrasTab;

	public List<Polygon> getObrasTab() {
		return obrasTab;
	}

	public void setObrasTab(List<Polygon> obrasTab) {
		this.obrasTab = obrasTab;
	}
	// -------------------------------------------------------

	private List<Map<String, ColumnModel>> tableData;

	// -------------------------------------------------------------------

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

		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	public List<AbstractGeometry> getPaginateGeometries() {

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

			// this.pageItems8 = query.getResultList();

			return query.getResultList();
		} catch (Exception ex) {
			return new ArrayList<AbstractGeometry>();
		}

	}

	private Predicate[] getSearchPredicates8(Root<AbstractGeometry> root, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		Layer layerS = this.getSelectedLayer();
		if (layerS != null) {
			predicatesList.add(builder.equal(root.get("layer"), layerS));
		}

		String nome = this.getNameSearch();
		if (nome != null && !"".equals(nome)) {
			predicatesList.add(builder.like(builder.lower(root.<String>get("nome")), '%' + nome.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	// ----------------------

	// ----------------------

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

	/*
	 * Support printing map
	 */

	@Transactional
	public void printPDF() throws FileNotFoundException, NoSuchAuthorityCodeException, IOException, FactoryException {
		
		if (this.applicationEntity instanceof ProjectRuralEntity) {
			// printPDF_RURAL();
		} else if (this.applicationEntity instanceof ProjectBasicEntity) {
			printPDF_BASIC();
		} else if (this.applicationEntity instanceof ProjectUrbanEntity) {
			printPDF_URBANO();
		}

	}


	@Transactional
	public void printPDF_BASIC()
			throws FileNotFoundException, IOException, NoSuchAuthorityCodeException, FactoryException {

		if (this.abstractGeometry != null) {


		

				String finalLogoUrl = null;
				if (this.getApplicationImg("logoImageMap") == null) {
					finalLogoUrl = userData.getGeoazulURL() + "/resources/Image/g_logoImageMap.png";
				} else {
					finalLogoUrl = userData.getGeoazulURL() + this.getApplicationImg("logoImageMap");
				}

				String finalFilesUrl = userData.getGeoazulURLFiles();
				Collection<ApplicationAttributeEntity> appatribs = this.getApplicationEntity().getAttributes();



				String bBox = this.getBbox();


				if (bBox.equals(null)) {
				} else {

					String[] parts = bBox.split(",");
					String part1 = parts[0];
					String part2 = parts[1];
					String part3 = parts[2];
					String part4 = parts[3];

					double x1 = Double.valueOf(part1).doubleValue();
					double y1 = Double.valueOf(part2).doubleValue();
					double x2 = Double.valueOf(part3).doubleValue();
					double y2 = Double.valueOf(part4).doubleValue();


				}
				Polygon polPrint = null;
				
			
					polPrint = entityManager.find(Polygon.class, this.abstractGeometry.getId());
					// this.abstractGeometry = (Polygon) this.abstractGeometry;
					// =============================================

					String appDirectory = save_FILE_PATH + "/files/" + userData.getRealmEntity() + "/"
							+ this.applicationEntity.getId();
					boolean baseGeometryDir = (new File(appDirectory)).mkdir();

					String appDirectory2 = appDirectory + "/" + polPrint.getLayer().getId();
					boolean baseGeometryDir2 = (new File(appDirectory2)).mkdir();

					String saveFileProcessFilePath_ = appDirectory2 + "/" + polPrint.getId();
					boolean baseGeomDir = (new File(saveFileProcessFilePath_)).mkdir();


					String saveFileProcessFilePathUrl_ = "/files/" + userData.getRealmEntity() + "/"
							+ this.applicationEntity.getId() + "/" + polPrint.getLayer().getId() + "/"
							+ polPrint.getId();


					// ============================================


					Double scala = Double.valueOf(this.getScale());
					scala = scala * 1.5;
					Integer scaleInt = null;
					if (this.getPaper() == "3" || this.getPaper().equals("3")) {
						
						scala = 6 * (scala / 10);
						scaleInt = scala.intValue();
					} else {
						scaleInt = scala.intValue();
					}
					

			}
		
		
			
	}

	@Transactional
	public void printPDF_URBANO()
			throws FileNotFoundException, IOException, NoSuchAuthorityCodeException, FactoryException {

		if (this.abstractGeometry != null) {


			try {

				String finalLogoUrl = null;
				if (this.getApplicationImg("logoImageMap") == null) {
					finalLogoUrl = userData.getGeoazulURL() + "/resources/Image/g_logoImageMap.png";
				} else {
					finalLogoUrl = userData.getGeoazulURL() + this.getApplicationImg("logoImageMap");
				}

				String finalFilesUrl = userData.getGeoazulURLFiles();
				Collection<ApplicationAttributeEntity> appatribs = this.getApplicationEntity().getAttributes();


				PrintPDF printPDF = new PrintPDF();

				String bBox = this.getBbox();


				if (bBox.equals(null)) {
				} else {

					String[] parts = bBox.split(",");
					String part1 = parts[0];
					String part2 = parts[1];
					String part3 = parts[2];
					String part4 = parts[3];

					double x1 = Double.valueOf(part1).doubleValue();
					double y1 = Double.valueOf(part2).doubleValue();
					double x2 = Double.valueOf(part3).doubleValue();
					double y2 = Double.valueOf(part4).doubleValue();


				}
				Polygon polPrint = null;
				try {
					polPrint = entityManager.find(Polygon.class, this.abstractGeometry.getId());
					// this.abstractGeometry = (Polygon) this.abstractGeometry;
					// =============================================

					String appDirectory = save_FILE_PATH + "/files/" + userData.getRealmEntity() + "/"
							+ this.applicationEntity.getId();
					boolean baseGeometryDir = (new File(appDirectory)).mkdir();

					String appDirectory2 = appDirectory + "/" + polPrint.getLayer().getId();
					boolean baseGeometryDir2 = (new File(appDirectory2)).mkdir();

					String saveFileProcessFilePath_ = appDirectory2 + "/" + polPrint.getId();
					boolean baseGeomDir = (new File(saveFileProcessFilePath_)).mkdir();


					String saveFileProcessFilePathUrl_ = "/files/" + userData.getRealmEntity() + "/"
							+ this.applicationEntity.getId() + "/" + polPrint.getLayer().getId() + "/"
							+ polPrint.getId();


					// ============================================


					Double scala = Double.valueOf(this.getScale());
					scala = scala * 1.5;
					Integer scaleInt = null;
					if (this.getPaper() == "3" || this.getPaper().equals("3")) {
						scala = 6 * (scala / 10);
						scaleInt = scala.intValue();
					} else {
						scaleInt = scala.intValue();
					}

					Comp layergeo = null;
					String geoserverLayer = null;
					try {
						// layergeo = polPrint.getLayer().getApplicationEntity().getLayersTile().get(0);
						// geoserverLayer = "l" + layergeo.getUuid().toString().replace("-", "");
					} catch (Exception e1) {
					}

					printPDF.printRun33(finalFilesUrl, appatribs, finalLogoUrl, entityManager, this.applicationEntity,
							polPrint, this.getBbox(), "", this.getSistcord(), scaleInt.toString(), this.modelo,
							polPrint.getLayer().getName(), this.getColorPopup(), this.getPaper(), this.getBackground(),
							null, save_FILE_PATH, false, saveFileProcessFilePath_, saveFileProcessFilePathUrl_,
							geoserverLayer, this.tenant);

					ProcessMemorial2 processMemorial2 = new ProcessMemorial2();
					try {
						processMemorial2.createLineString(finalFilesUrl, appatribs, polPrint, entityManager,
								this.modelo, polPrint.getLayer().getName(), saveFileProcessFilePath_,
								saveFileProcessFilePathUrl_, tenant);
					} catch (Exception e1) {
					}

				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				entityManager.flush();
			} catch (Throwable e) {
				try {
					throw e;
				} catch (Throwable e1) {
					e1.printStackTrace();
				}
			}
		}


	}

	/*
	 * Support printing map RURAL
	 */

	// ===============================================

	private Layer selectedLayer;

	public Layer getSelectedLayer() {
		return selectedLayer;
	}

	public void setSelectedLayer(Layer selectedLayer) {
		this.selectedLayer = selectedLayer;
	}

	private Layer layerTemp;

	public Layer getLayerTemp() {
		return layerTemp;
	}

	public void setLayerTemp(Layer layerTemp) {
		this.layerTemp = layerTemp;
	}
	// -----

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

	private List<Map<String, ColumnModel>> tableDataShape;

	private List<ColumnModel> columns;

	private List<ColumnModel> tableHeaderNames;

	public List<Map<String, ColumnModel>> getTableDataShape() {
		// start");
		if (this.abstractGeometry != null) {

			// if (this.abstractGeometry.getLayer().getId() !=
			// "POzzy33x-5555-0000-dddd-AA7a23d970PO" ){

			// start2" + this.abstractGeometry.getNome());
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

			ObjectNode node = null;

			Integer contador = 1;
			List<Field> fieldsProperties = null;
			String categorias = new String();

			columns = new ArrayList<ColumnModel>();
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

	// --------------------------------------------------------------------------------------------------------

	public void retrieve_start() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

	}

	private Map<String, ConnectionProvider> connectionProviderMap = new HashMap<>();

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		try {

			if (this.id == null) {

				try {
					FacesContext context = FacesContext.getCurrentInstance();
					context.getExternalContext().redirect("/application/index.xhtml");
				} catch (IOException ignore) {
					ignore.printStackTrace();
				}

			} else {

				this.applicationEntity = entityManager.find(ApplicationEntity.class, id);

				this.applicationEntity.getAttributes().size();

				appLayers = new ArrayList<LayerView>();
				for (Layer lnew : this.applicationEntity.getLayers()) {
					LayerView layerView = new LayerView();
					layerView.setLayerid(lnew.getId().toString());
					layerView.setVisible(lnew.getSelected());
					layerView.setOpacity(lnew.getOpacity());
					layerView.setZIndex(lnew.getOrderlayer());
					layerView.setLayerhash(lnew.layerString());
					layerView.setName(lnew.getName());
					appLayers.add(layerView);
				}

				model = new DynaFormModel();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	public ApplicationEntity findById(Long id) {
		try {
			return entityManager.find(ApplicationEntity.class, id);
		} catch (Exception ex) {
			return null;
		}
	}

	public Modulo findModule(int moduloId) {
		try {
			return entityManager.find(Modulo.class, moduloId);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	public AbstractGeometry findAbstractGeometry(Long objectId) {
		try {
			return entityManager.find(AbstractGeometry.class, objectId);
		} catch (Exception ex) {
			return null;
		}
	}

	@Transactional
	public void mergePolygonPoint(PolygonPoint polygonPoint) {
		try {
			entityManager.merge(polygonPoint);
			entityManager.flush();
			entityManager.refresh(this.obra);
		} catch (Exception e) {
		}
	}

	@Transactional
	public void persistPolygonPoint(PolygonPoint polygonPoint) {
		try {
			entityManager.persist(polygonPoint);
			entityManager.flush();
			entityManager.refresh(this.obra);
		} catch (Exception e) {
		}
	}

	public PolygonPoint findPolygonPoint(GeometryGeometryId verticeobraId) {
		try {
			return entityManager.find(PolygonPoint.class, verticeobraId);
		} catch (Exception ex) {
			return null;
		}
	}

	public List<Layer> getAllWithoutTile2() {
		try {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<Layer> criteria = cb.createQuery(Layer.class);
			Root<Layer> layerRoot = criteria.from(Layer.class);
			TypedQuery<Layer> query = entityManager.createQuery(
					criteria.select(layerRoot).where(getSearchPredicatesWithoutTilesAndThis(layerRoot, entityManager)));
			return query.getResultList();
		} catch (Exception ex) {
			return null;
		}
	}

	public Layer findLayer(Long layerId) {
		try {
			return entityManager.find(Layer.class, layerId);
		} catch (Exception ex) {
			return null;
		}
	}

	@Transactional
	public void processRural() {

		try {
			String finalFilesUrl = userData.getGeoazulURLFiles();
			Collection<ApplicationAttributeEntity> appatribs = this.getApplicationEntity().getAttributes();
			// this.runCreate(obraCAP, session);

			this.processarObraProj(finalFilesUrl, appatribs, this.obra, entityManager);
			entityManager.flush();
			// entityManager.refresh(this.obra);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Support updating and deleting ApplicationEntity entities
	 */
	private Integer epsgNew;

	public ClientEntity findClienId(String clientId) {
		try {

			TypedQuery<ClientEntity> queryApp = entityManager.createNamedQuery(ClientEntity.CLIENT_GET_BY_CLIENTID,
					ClientEntity.class);
			queryApp.setParameter("clientId", clientId);
			return queryApp.getSingleResult();
		} catch (Exception ex) {
			return null;
		}
	}

	public Person findPersonEmail(String email) {
		try {
			TypedQuery<Person> queryApp = entityManager.createNamedQuery(Person.USER_EMAIL_GET, Person.class);
			queryApp.setParameter("email", email);
			return queryApp.getSingleResult();
		} catch (Exception ex) {
			return null;
		}
	}

	@Transactional
	public String update33() {

		try {

			if (this.id == null) {

				LabelStyle styleNew = new LabelStyle();

				ClientEntity clientEntity = findClienId("/gis/");

				ApplicationAttributeEntity applicationAttributeEntity;
				ApplicationAttributeEntity applicationAttributeEntity2;
				AppUserMappingEntity appUserMappingEntity;
				LayerTile layerTile;
				LayerPoint layerPoint;
				LayerPolygon layerPolygon;
				LayerMultiPolygon layerMultiPolygon;

				this.applicationEntity.setClientEntity(clientEntity);
				this.applicationEntity.setCreatedDate(LocalDateTime.now());
				this.applicationEntity.setLocale("pt_BR");
				applicationEntity.setLikes(0);
				applicationEntity.setShared(0);
				applicationEntity.setStrings(JacksonUtil.toJsonNode("{}"));
				this.applicationEntity.setImage("/images/geoazul_basic_thumb.png");
				entityManager.persist(this.applicationEntity);
				entityManager.flush();
				appUserMappingEntity = new AppUserMappingEntity();
				appUserMappingEntity.setApp(this.getApplicationEntity());


				appUserMappingEntity.setPerson(userData.getPerson());

				appUserMappingEntity.setAppRole(AppRole.ADMINISTRATOR);
				entityManager.persist(appUserMappingEntity);
				entityManager.flush();

				switch (this.applicationEntity.getClass().getSimpleName()) {

				case "ProjectBasicEntity":

					applicationAttributeEntity = new ApplicationAttributeEntity();
					applicationAttributeEntity.setName("logoImageMap");
					applicationAttributeEntity.setValue("/images/logoImageMap.png");
					applicationAttributeEntity.setApplication(this.applicationEntity);

					entityManager.persist(applicationAttributeEntity);
					entityManager.flush();

					applicationAttributeEntity2 = new ApplicationAttributeEntity();

					applicationAttributeEntity2.setName("backgroundImage");
					applicationAttributeEntity2.setValue("/images/backgroundImage.png");
					applicationAttributeEntity2.setApplication(this.applicationEntity);

					entityManager.persist(applicationAttributeEntity2);
					entityManager.flush();

					String name = "GOOGLE";
					LayerTile layerNew = new LayerTile(name, this.applicationEntity, "Example Camada Google",
							JacksonUtil.toJsonNode("{}"), this.applicationEntity.getLayers().size() + 1,
							this.applicationEntity.getEpsg(), this.applicationEntity.getZoom(),
							this.applicationEntity.getMinZoom(), this.applicationEntity.getMaxZoom(),
							this.applicationEntity.getMaxres(), this.applicationEntity.getMinres());

					String maptilerXYZ = "{\n"
							+ "  \"type\": \"Maptiler\",\n"
							+ "  \"apiKey\": \"t2zX1AkgvdqLsGQb7ON7\",\n"
							+ "  \"urlXYZ\": \"https://api.maptiler.com/maps/satellite/{z}/{x}/{y}.jpg?key=\",\n"
							+ "  \"imagerySet\": \"satellite\"\n"
							+ "}";

					layerNew.setStrings(JacksonUtil.toJsonNode(maptilerXYZ));

					entityManager.persist(layerNew);
					entityManager.flush();

					LayerPolygon newLayer = new LayerPolygon();
					newLayer.setName("POLYGON");
					newLayer.setApplicationEntity(this.getApplicationEntity());

					newLayer.setDescription("CAMADA POLYGON ");
					newLayer.setSelected(true);
					newLayer.setZoom(5);

					newLayer.setMinZoom(0);
					newLayer.setMaxZoom(20);
					newLayer.setMaxres(new BigDecimal(100000));
					newLayer.setMinres(new BigDecimal(0.00001));
					GeometryStyle geomStylePol = new GeometryStyle("rgba(245,2,62,0.45)", "#00e600", 2, 0, 0, 15);

					newLayer.setGeometryStyle(geomStylePol);
					newLayer.setStrings(JacksonUtil.toJsonNode("{}"));

					newLayer.setEpsg(3857);
					newLayer.setEditable(true);

					newLayer.setTitle("POLYGONS");
					newLayer.setOrderlayer(this.getApplicationEntity().getLayers().size() + 1);
					LabelStyle styleNewPol = new LabelStyle();
					styleNewPol.setMaxreso(new BigDecimal(100000));
					styleNewPol.setMinreso(new BigDecimal(0.00001));

					newLayer.setLabelStyle(styleNewPol);

					entityManager.persist(newLayer);
					entityManager.flush();
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Projeto criado!"));

				case "ProjectRuralEntity":

					applicationAttributeEntity = new ApplicationAttributeEntity();

					applicationAttributeEntity.setName("logoImageMap");
					applicationAttributeEntity.setValue("/images/logoImageMap.png");
					applicationAttributeEntity.setApplication(this.applicationEntity);

					entityManager.persist(applicationAttributeEntity);

					applicationAttributeEntity2 = new ApplicationAttributeEntity();

					applicationAttributeEntity2.setName("backgroundImage");
					applicationAttributeEntity2.setValue("/images/backgroundImage.png");
					applicationAttributeEntity2.setApplication(this.applicationEntity);

					entityManager.persist(applicationAttributeEntity2);

					layerPoint = new LayerPoint();
					layerPoint.setApplicationEntity(this.applicationEntity);
					layerPoint.setDescription("VÉRTICES");
					layerPoint.setName("VÉRTICES");
					layerPoint.setSelected(true);

					layerPoint.setLabelStyle(styleNew);
					entityManager.persist(layerPoint);

					layerMultiPolygon = new LayerMultiPolygon();
					layerMultiPolygon.setApplicationEntity(this.applicationEntity);
					layerMultiPolygon.setDescription("IMÓVEIS");
					layerMultiPolygon.setName("IMÓVEIS");
					layerMultiPolygon.setSelected(true);
					layerMultiPolygon.setLabelStyle(styleNew);
					entityManager.persist(layerMultiPolygon);

					layerPolygon = new LayerPolygon();
					layerPolygon.setApplicationEntity(this.applicationEntity);
					layerPolygon.setDescription("PARCELAS");
					layerPolygon.setName("PARCELAS");
					layerPolygon.setSelected(true);
					layerPolygon.setLabelStyle(styleNew);
					entityManager.persist(layerPolygon);
					break;

				}

			} else {
				entityManager.merge(this.applicationEntity);
			}

			entityManager.flush();

			return "index?faces-redirect=true";
		} catch (Exception e) {
			e.printStackTrace();

		}
		return "index?faces-redirect=true";
	}

	public String update333() {




		return "index?faces-redirect=true";

	}

	public void addMessage(String summary, String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void confirm() {
		addMessage("Confirmed", "You have accepted");
	}

	@Transactional
	public void delete() {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			ApplicationEntity deletableEntity = entityManager.find(ApplicationEntity.class,
					this.getApplicationEntity().getId());
			entityManager.remove(deletableEntity);
			entityManager.flush();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Projeto Excluido!"));
			context.getExternalContext().redirect("/application/index");

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro!", "Projeto não Excluido!"));
			e.printStackTrace();
		}
	}

	/*
	 * Support searching ApplicationEntity entities with pagination
	 */

	private ApplicationEntity example = new ProjectBasicEntity();

	public ApplicationEntity getExample() {
		return this.example;
	}

	public void setExample(ApplicationEntity example) {
		this.example = example;
	}

	/*
	 * Support listing and POSTing back ApplicationEntity entities (e.g. from inside
	 * an HtmlSelectOneMenu)
	 */

	public List<ApplicationEntity> getAll() {

		try {

			CriteriaQuery<ApplicationEntity> criteria = entityManager.getCriteriaBuilder()
					.createQuery(ApplicationEntity.class);
			return entityManager.createQuery(criteria.select(criteria.from(ApplicationEntity.class))).getResultList();
		} catch (Exception ex) {
			return null;
		}
	}

	private ApplicationEntity add = new ProjectBasicEntity();

	public ApplicationEntity getAdd() {
		return this.add;
	}

	public ApplicationEntity getAdded() {
		ApplicationEntity added = this.add;
		this.add = new ProjectBasicEntity();
		return added;
	}

	/*
	 * Support searching ApplicationEntity entities with pagination
	 */

	private int page;
	private long count;
	private List<ApplicationEntity> pageItems;

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
			Root<ApplicationEntity> root = countCriteria.from(ApplicationEntity.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();

			// Populate this.pageItems

			CriteriaQuery<ApplicationEntity> criteria = builder.createQuery(ApplicationEntity.class);
			root = criteria.from(ApplicationEntity.class);
			TypedQuery<ApplicationEntity> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicates(root, entityManager)));
			query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());


			this.pageItems = query.getResultList();

		} catch (Exception ex) {
		}

	}

	private Predicate[] getSearchPredicates(Root<ApplicationEntity> root, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String name = this.example.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<ApplicationEntity> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	@Transactional
	public void deleteMedias() {

		try {


			for (Media media : this.getSelectedMedias()) {
				Media deletedMedia = entityManager.find(Media.class, media.getId());
				entityManager.remove(deletedMedia);
				this.abstractGeometry.removeMedia(deletedMedia);
			}
			entityManager.flush();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "Arquivos Excluidos!"));

		} catch (Exception e) {
		}
	}

	private List<Media> selectedMedias;

	public List<Media> getSelectedMedias() {
		return selectedMedias;
	}

	public void setSelectedMedias(List<Media> selectedMedias) {
		this.selectedMedias = selectedMedias;
	}

	public String getRestwork() {
		return userData.getBaseHostName();
	}
	
	public String getGeoserverURL() {
		return GEOSERVER_URL;
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

	
	//deleteOB
	@Transactional
	public void deleteObject() {

		try {

			AbstractGeometry removeGeometry = entityManager.find(AbstractGeometry.class, this.abstractGeometry.getId());

			entityManager.remove(removeGeometry);
			entityManager.flush();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Geometria Excluida!"));

		} catch (Exception e) {
		}
	}

	@Transactional
	public void deleteLayer() {

		try {
			Layer deletableLayer = entityManager.find(Layer.class, this.getSelectedLayer().getId());

			this.getApplicationEntity().removeLayerItem(deletableLayer);

			entityManager.remove(deletableLayer);
			entityManager.flush();

			appLayers = new ArrayList<LayerView>();
			Integer novaSequencia = 1;
			for (Layer lnew : this.applicationEntity.getLayers().stream().filter(l -> l.getFather() == null)
					.toList()) {
				LayerView layerView = new LayerView();
				layerView.setLayerid(lnew.getId().toString());
				layerView.setVisible(lnew.getSelected());
				layerView.setOpacity(lnew.getOpacity());
				layerView.setZIndex(novaSequencia);
				layerView.setLayerhash(lnew.layerString());
				layerView.setName(lnew.getName());
				boolean result = (lnew instanceof LayerGroup) ? true : false;
				layerView.setFather(result);
				appLayers.add(layerView);
				if (lnew.getOrderlayer() != novaSequencia) {
					lnew.setOrderlayer(novaSequencia);
					entityManager.merge(lnew);  //FIXME CRIAR UPDATE DIRETO NO ENTITY
					entityManager.flush();
				}
				novaSequencia++;
			}
			
			
			
			
			
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Camada Excluida!"));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public String submitCoordinatesWKT() {
		// org.geoazul.model.basic.Polygon poly = (Polygon) poli;


		try {

			entityManager.merge(this.abstractGeometry);
			entityManager.flush();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Coordenadas Alteradas!"));

		} catch (Exception e) {
			return null;
		}
		return null;

	}

	@Transactional
	public String submitForm() {


		try {
			String valuex;
			List<BookProperty> listBookProperty = this.getBookProperties();
			String jsonb = "{";
			int fieldIndex = 1;
			int fieldSize = listBookProperty.size();



			this.abstractGeometry = entityManager.find(AbstractGeometry.class, this.abstractGeometry.getId());

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


					try {

						Long fatherId = Long.valueOf(bookProperty.getValue().toString());
						AbstractGeometry fatherEntity = entityManager.find(AbstractGeometry.class, fatherId);


						this.abstractGeometry.setFather(fatherEntity);

					} catch (Exception e) {
						this.abstractGeometry.setFather(null);
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


			entityManager.flush();


			PrimeFaces.current().ajax().addCallbackParam("layerobject", this.abstractGeometry.getLayer().layerString());
			PrimeFaces.current().ajax().addCallbackParam("idobject", this.abstractGeometry.getId().toString());
			PrimeFaces.current().ajax().addCallbackParam("nomeobject", this.abstractGeometry.getNome());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Registro Alterado!"));

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	public void beanMethodPrint() {

		String objectidCapture = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("objectid");
		String objectlayeridCapture = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("layerid");

		try {
			this.abstractGeometry = findAbstractGeometry(Long.valueOf(objectidCapture));
			this.objectId = objectidCapture;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	public void showMessage() {
		String messageLoad = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("message");
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "MENSAGEM", messageLoad);
        PrimeFaces.current().dialog().showMessageDynamic(message);
    }
	
	

	private String uuid;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
		beanMethod();
	}

	/*
	 * Support objectId set on Ajax request method CREATE FIELD DYNAMICS AND
	 * POPULATE THIS
	 */

	public void beanMethod33() {
	}

	private Contador visitor;

	public Contador getVisitor() {
		return visitor;
	}

	public void setVisitor(Contador visitor) {
		this.visitor = visitor;
	}

	public void beanMethodVisitor() {
		String objectidCapture = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("objectid");
		Long idCL = Long.valueOf(objectidCapture);
		this.visitor = entityManager.find(Contador.class, idCL);

	}

	public void extendsFeature() {
		String objectIdZoom = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("abstractId");

		this.abstractGeometry = findAbstractGeometry(Long.valueOf(objectIdZoom));


		// FIXME Error on this id load

		Geometry dd = null;
		switch (this.abstractGeometry.getClass().getSimpleName()) {
		case "Polygon":
			dd = ((Polygon) abstractGeometry).getGeometry();
			break;
		case "MultiPolygon":
			dd = ((MultiPolygon) abstractGeometry).getGeometry();
			break;
		case "Linestring":
			dd = ((Linestring) abstractGeometry).getGeometry();
			break;
		case "Point":
			dd = ((Point) abstractGeometry).getGeometry();
			break;
		}


		PrimeFaces.current().ajax().addCallbackParam("coordx", dd.getCentroid().getX());
		PrimeFaces.current().ajax().addCallbackParam("coordy", dd.getCentroid().getY());
	}

	@Transactional
	public void beanMethod() {
		String objectidCapture = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("objectid");

		try {

			this.abstractGeometry = findAbstractGeometry(Long.valueOf(objectidCapture));




			// FIXME Error on this id load
			if (abstractGeometry instanceof Polygon) {
				this.obra = entityManager.find(Polygon.class, this.abstractGeometry.getId());

			} else {
				this.obra = null;
			}


			List<Field> fieldsProperties = this.getAbstractGeometry().getLayer().getFields();


			model = new DynaFormModel();

			DynaFormRow row = new DynaFormRow();

			row = model.createRegularRow();
			DynaFormLabel label33 = row.addLabel("Ativo");
			DynaFormControl control333 = row.addControl(
					new BookProperty("Ativo", "Ativo", "Ativo", this.getAbstractGeometry().getEnabled(), true),
					"booleanchoice", 3, 1);
			label33.setForControl(control333);


			// List<LayerCategory> layerCat = this.getAbstractGeometry().getCategories();
			String idCategory = null;
			// for (LayerCategory layerC : layerCat) {
			// idCategory = layerC.getId();
			// }
			try {
				Integer numberCat = this.getAbstractGeometry().getLayer().getLayerCategories().size();
				if (numberCat > 0) {
					DynaFormLabel label410 = row.addLabel("Categoria");

					Map<String, Object> paramCat = new HashMap<>();
					paramCat.put("placeHolder", "Digite aqui o Categoria");

					DynaFormControl control420 = row.addControl(new BookProperty("Categoria", "Categoria",
							"Digite aqui a Categoria", idCategory, false, null, paramCat), "selectcat");
					label410.setForControl(control420);
				}
			} catch (NullPointerException np) {

			}
			// try {
			// if (this.getAbstractGeometry().getLayer() != null) {
			// Long idLayerGeom = this.getAbstractGeometry().getLayer().getId();
			// DynaFormLabel label9103 = row.addLabel("Camada", true, 6, 1,
			// "mail_outline");
			// DynaFormControl control9203 = row.addControl(
			// new BookProperty("Layer", "Camada", "Digite aqui a Camada", idLayerGeom,
			// false),
			// "selectlayer");
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
			// DynaFormControl control9203 = row.addControl(
			// new BookProperty("Camada", "Camada", "Digite aqui a Camada", "", false),
			// "selectlayer");
			// label9103.setForControl(control9203);
			// }


			boolean hasIdFather = false;
			Long idFather = 0L;
			try {
				if (this.getAbstractGeometry().getLayer().getLayerCat() != null) {

					try {
						idFather = this.getAbstractGeometry().getFather().getId();
					} catch (NullPointerException np) {
					}

					Map<String, Object> paramPai = new HashMap<>();
					paramPai.put("placeHolder", "Digite aqui o Pai");

					if (idFather > 0) {


						DynaFormLabel label4103 = row
								.addLabel(this.getAbstractGeometry().getFather().getLayer().getName());

						DynaFormControl control4203 = row.addControl(
								new BookProperty("Father", "Pai", "Digite aqui o Pai", idFather, false, null, paramPai),
								"selectfather");
						label4103.setForControl(control4203);

					} else {

						DynaFormLabel label41038 = row
								.addLabel(this.getAbstractGeometry().getLayer().getLayerCat().getName());
						DynaFormControl control42038 = row.addControl(
								new BookProperty("Father", "Pai", "Digite aqui o Pai", "", false, null, paramPai),
								"selectfather");
						label41038.setForControl(control42038);
					}

				}

			} catch (NullPointerException np) {
			}


			row = model.createRegularRow();
			DynaFormLabel label11 = row.addLabel("Nome");

			Map<String, Object> paramNome = new HashMap<>();
			paramNome.put("placeHolder", "Digite aqui o Nome");

			DynaFormControl control12 = row.addControl(new BookProperty("Nome", "Nome", "Digite aqui o Nome",
					this.getAbstractGeometry().getNome(), false, null, paramNome), "input", 3, 1);
			label11.setForControl(control12);


			if (this.getAbstractGeometry().getLayer().getIconcategory() == IconcategoryId.ENTITY) {
				row = model.createRegularRow();
				DynaFormLabel label111 = row.addLabel("Icon");
				DynaFormControl control121 = row.addControl(new BookProperty("Icon", "Icon", "Digite aqui seu icon",
						this.getAbstractGeometry().getIconflag(), true), "input", 3, 1);
				label11.setForControl(control121);
			}


			ObjectNode node = null;
			try {
				node = new ObjectMapper().readValue(this.getAbstractGeometry().getStrings().toString(),
						ObjectNode.class);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException np) {
				np.printStackTrace();

			}

			// CREATE FIELD DYNAMICS AND POPULATE THIS
			int stringKeyIndex = 0;
			Object[] objectValue = new Object[this.getAbstractGeometry().getLayer().getFields().size()];
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

	private Field fieldNew;

	public Field getFieldNew() {
		return fieldNew;
	}

	public void setFieldNew(Field fieldNew) {
		this.fieldNew = fieldNew;
	}

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
		LayerCategory layerCat = findLayerCategory(category);
		this.setLayerCategory(layerCat);
		this.category = category;
	}

	public void beanMethodCat() {
		String catIdCapture = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("categoryid");

		if (catIdCapture.equals("new")) {

			if (this.selectedLayer instanceof LayerPoint) {
				this.layerCategory = new LayerPointCategory();
			} else if (this.selectedLayer instanceof LayerLinestring) {
				this.layerCategory = new LayerLinestringCategory();
			} else if (this.selectedLayer instanceof LayerPolygon) {
				this.layerCategory = new LayerPolygonCategory();
			} else if (this.selectedLayer instanceof LayerMultiPolygon) {
				this.layerCategory = new LayerMultiPolygonCategory();

			} else if (this.selectedLayer instanceof LayerPoint) {
				this.layerCategory = new LayerPointCategory();
			} else if (this.selectedLayer instanceof LayerPolygon) {
				this.layerCategory = new LayerPolygonCategory();
			} else if (this.selectedLayer instanceof LayerMultiPolygon) {
				this.layerCategory = new LayerMultiPolygonCategory();
			}

		} else {
			this.layerCategory = findLayerCategory(catIdCapture);
		}
	}

	public LayerCategory findLayerCategory(String id) {
		try {
			return entityManager.find(LayerCategory.class, id);
		} catch (Exception ex) {
			return null;
		}
	}

	public void beanMethodField() {
		try {
			String fieldIdCapture = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.get("fieldid");
			this.fieldNew = entityManager.find(Field.class, fieldIdCapture);
		} catch (Exception e) {
		}
	}

	public void newField() {
		this.dataTypeChose = null;
		this.fieldName = null;
		this.fieldTypeChose = null;
		this.fieldNew = new Field(this.getSelectedLayer());
		this.fieldNew.setFieldControl(new FieldControl());
	}

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

	private Map<String, String> dataTypes;

	public Map<String, String> getDataTypes() {
		return dataTypes;
	}

	public void setDataTypes(Map<String, String> dataTypes) {
		this.dataTypes = dataTypes;
	}

	private Map<String, String> fieldTypes;

	public Map<String, String> getFieldTypes() {

		Map<String, String> map = new HashMap<String, String>();

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

		case DATE:
			map.put("datePicker", "datePicker");
			break;
		}

		return map;

	}

	/**
	 * Enumeration for the different types of Layers
	 */
	public enum DataType {
		STRING, NUMBER, BOOLEAN, ARRAY, DATE, DATETIME, TIME, MEDIA
	}

	public String onFlowProcess2(FlowEvent event) {





		if (event.getOldStep().equals("upload2")) {

			if (fieldTypeChose != null) {
				this.fieldNew.getFieldControl().setType(fieldTypeChose);
				Map<String, Object> params = new HashMap<>();

				switch (fieldTypeChose) {

				// string
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

				// number
				case "inputNumber":
					params.put("placeHolder", "");
					break;
				case "spinner":
					params.put("placeHolder", "");
					break;

				// boolean
				case "selectBooleanButton":
					params.put("initval", false);
					break;
				case "selectBooleanCheckbox":
					params.put("initval", false);
					break;

				// chips
				case "chips":
					params.put("placeHolder", "");
					break;

				}

				this.fieldNew.getFieldControl().setKeys(params);

			}

		}

		

		return event.getNewStep();
	
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
			
			this.selectedLayer.addFieldItem(this.fieldNew);
		

			Layer layerLocal  = entityManager.find(Layer.class,this.fieldNew.getLayer().getId());

			
				for (Layer layerChild : layerLocal.getChildrensOrigins()   ) {
					Field fildChild = new Field();
					
					fildChild.setFieldControl(this.fieldNew.getFieldControl());
					fildChild.setIcon(this.fieldNew.getIcon());
					fildChild.setLabel(this.fieldNew.getLabel());
					fildChild.setLayer(layerChild);
					fildChild.setName(this.fieldNew.getName());
					fildChild.setPlaceHolder(this.fieldNew.getPlaceHolder());
					fildChild.setRequired(this.fieldNew.getRequired());
					fildChild.setSequence(this.fieldNew.getSequence());
					fildChild.setTypeData(this.fieldNew.getTypeData());
					entityManager.persist(fildChild);
					entityManager.flush();
				}
			
		} catch (Exception e) {
			 e.printStackTrace();
		}

	}

	private Map<String, Map<String, String>> data = new HashMap<String, Map<String, String>>();

	@Transactional
	public void deleteLayerCategory() {
		Long idCategory = null;
		try {
			idCategory = this.getLayerCategory().getId();
		} finally {

			try {

				if (idCategory != null) {
					layerCategory = entityManager.find(LayerCategory.class, idCategory); // FIXME DETACHED

					this.selectedLayer.removeCategoryItem(this.layerCategory);

					entityManager.remove(layerCategory);
					entityManager.flush();
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage("Sucesso!", "Categoria Excluido!"));

				}

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("Erro!", "Categoria Não Excluida!"));
			}

		}
	}

	public List<SelectItem> getLanguages() {
		List<SelectItem> categorias = new ArrayList<SelectItem>();
		try {
			Layer layerCat = this.getAbstractGeometry().getLayer().getLayerCat();
			for (AbstractGeometry layer : layerCat.getGeometrias()) {
				categorias.add(new SelectItem(layer.getId(), layer.getNome()));
			}
		} catch (NullPointerException np) {
			// PROPERTIES IS NULL null FIXME
		}

		return categorias;
	}

	public List<SelectItem> getLanguagesCat() {
		if (this.abstractGeometry != null) {
			List<SelectItem> categoriasCat = new ArrayList<SelectItem>();
			for (LayerCategory layerCategory : this.abstractGeometry.getLayer().getLayerCategories()) {
				categoriasCat.add(new SelectItem(layerCategory.getId(), layerCategory.getCategoryname()));
			}
			return categoriasCat;
		}
		return null;

	}

	// ----------------------

	public List<SelectItem> getLanguagesFat(String teste) {
		try {
			if (this.getAbstractGeometry().getLayer().getLayerCat() != null) {
				List<SelectItem> categoriasFat = new ArrayList<SelectItem>();
				categoriasFat.add(new SelectItem("", "Selecione"));
				try {
					for (AbstractGeometry abstractGeometryFather : this.abstractGeometry.getLayer().getLayerCat()
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

			for (Comp layerLay : this.applicationEntity.getLayers()) {
				a = layerLay.getDtype().toLowerCase();
				b = this.abstractGeometry.getClass().getSimpleName().toLowerCase();
				if (a.equals(b)) {
					categoriasLay
							.add(new SelectItem(layerLay.getId(), layerLay.getName(), layerLay.getName(), false, true));
				}
			}
			return categoriasLay;
		} catch (Exception e) {
			return null;
		}
	}

	// ----------------------

	// ---------------------
	@Transactional
	public void updateLayerCategory() {
		try {
			Long idCategory = this.getLayerCategory().getId();

			if (idCategory == null) {

				this.layerCategory.setLayer(this.getSelectedLayer());
				entityManager.persist(this.layerCategory);
				this.selectedLayer.addCategoryItem(this.layerCategory);
			} else {
				entityManager.merge(this.layerCategory);
				this.selectedLayer.updateCategoryItem(this.layerCategory);
			}
			entityManager.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Transactional
	public void deleteMethod() {
		// this.deletePoint(objectid); //FIXME
		
	}

	public void editAction(String objectid) {
	}

	@Transactional
	public void updateField2() {

		try {

			boolean idFieldNull = false;
			try {
				if (this.fieldNew.getId() == null) {
					idFieldNull = true;
				} else {
				}
			} catch (Exception e) {
				idFieldNull = true; // FIXME
			}

			if (idFieldNull) {


				FieldControl fieldControl = new FieldControl();

				String controle = this.fieldNew.getTypeData();

				switch (controle) {
				case "string":
					// OutputText outputText = new OutputText();
					// outputText.setSize(10);

					// outputText.setTypeInput("outputText");
					// fieldControl.setOutputText(outputText);
					fieldControl.setType("outputText");

					this.fieldNew.setFieldControl(fieldControl);

					break;

				case "boolean":
					// SelectBooleanCheckbox selectBooleanCheckbox = new SelectBooleanCheckbox();
					// selectBooleanCheckbox.setDisabled(false);
					// selectBooleanCheckbox.setTypeInput("selectBooleanCheckbox");
					// fieldControl.setSelectBooleanCheckbox(selectBooleanCheckbox);
					fieldControl.setType("selectBooleanCheckbox");
					this.fieldNew.setFieldControl(fieldControl);
					break;

				case "textImg":
					// OutputText outputTextImage = new OutputText();
					// outputTextImage.setSize(30);
					// outputTextImage.setTypeInput("outputText");
					// fieldControl.setOutputText(outputTextImage);
					fieldControl.setType("textImg");
					this.fieldNew.setFieldControl(fieldControl);
					break;

				case "textEditor":

					// {
					// "type": null,
					// "outputText": null,
					// "selectOneMenu": [],
					// "selectBooleanCheckbox": null
					// }

					// OutputText outputTextType = new OutputText();
					// outputTextType.setSize(10);
					// outputTextType.setTypeInput("outputText");
					this.fieldNew.setFieldControl(fieldControl);
					break;

				case "selectOneMenu":

					// fieldControl.setSelectOneMenu(this.fieldNew.getFieldControl().getSelectItems());
					fieldControl.setType("selectOneMenu");

					this.fieldNew.setFieldControl(fieldControl);
					break;

				}

				this.fieldNew.setLayer(this.getSelectedLayer());

				entityManager.persist(this.fieldNew);
				this.selectedLayer.addFieldItem(this.fieldNew);

			} else {
				entityManager.merge(this.fieldNew);
				this.selectedLayer.updateFieldItem(this.fieldNew);
			}

			entityManager.flush();
		} catch (Exception e) {
		}

	}

	@Transactional
	public void deleteField() {

		try {

			Field deletableField = entityManager.find(Field.class, this.getFieldNew().getId());

			this.selectedLayer.removeFieldItem(deletableField);

			entityManager.remove(deletableField);
			entityManager.flush();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Campo Excluido!"));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Error!", "Campo não Excluido!"));

		}
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
					"texteditor", 3, 1);
			labelDF.setForControl(controlDF);
			break;

		case "textImg":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, labelField, valueField, required), "img", 3, 1);
			labelDF.setForControl(controlDF);
			break;

		case "fileUpload":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, labelField, valueField, required), "fileupload", 3,
					1);
			labelDF.setForControl(controlDF);
			break;

		case "calendarDate":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, labelField, LocalDate.now(), required), "calendar",
					3, 1);
			labelDF.setForControl(controlDF);
			break;

		case "selectOneMenu":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, labelField, valueField, required, selectItems),
					"selectOneMenu", 3, 1);
			labelDF.setForControl(controlDF);
			break;

		case "outputText":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, labelField, placeHolder, valueField, required),
					"input", 3, 1);
			labelDF.setForControl(controlDF);
			break;
			
		case "datePicker":
			labelDF = row.addLabel(labelField);
			LocalDate dateTest = LocalDate.parse(valueField.toString().toUpperCase());
			controlDF = row.addControl(new BookProperty(nameField, labelField, placeHolder, dateTest, required),
					"datePicker", 3, 1);
			labelDF.setForControl(controlDF);
			break;

		case "inputText":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(
					new BookProperty(nameField, labelField, placeHolder, valueField, required, null, keys), "input", 3,
					1);
			labelDF.setForControl(controlDF);
			break;

		case "inputNumber":

			String jsonb = "{\n" + "    \"type\": \"outputText\",\n" + "    \"outputText\": {\n"
					+ "        \"size\": 10,\n" + "        \"typeInput\": \"outputText\"\n" + "    },\n"
					+ "    \"selectBooleanCheckbox\": null\n" + "}";

			JsonNode strings = JacksonUtil.toJsonNode(jsonb);

			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, labelField, placeHolder, valueField, required),
					"inputNumber", 3, 1);
			labelDF.setForControl(controlDF);
			break;

		case "selectBooleanCheckbox":
			labelDF = row.addLabel(labelField);
			controlDF = row.addControl(new BookProperty(nameField, labelField, placeHolder, valueField, required),
					"booleanchoice", 3, 1);
			labelDF.setForControl(controlDF);
			break;
		}

		return row;
	};

	public List<Layer> getAllLayers() {
		if (this.selectedLayer == null) {
			return new ArrayList<Layer>();
		} else {
			return this.applicationEntity.getLayers().stream().filter(f -> !f.getId().equals(selectedLayer.getId()))
					.toList();
		}
	}

	public List<Layer> getAllLayersGroups() {
		if (this.selectedLayer == null) {
			return new ArrayList<Layer>();
		} else {
			return this.applicationEntity.getLayers().stream()
					.filter(f -> !f.getId().equals(selectedLayer.getId()) && f.getDtype().equals("group")).toList();
		}
	}

	public List<String> getAllFields() {
		if (this.selectedLayer == null) {
			return new ArrayList<String>();
		} else {

			return selectedLayer.getFields().stream().map(Field::getName) // Use map to transform to a stream of names
					.collect(Collectors.toList()); // Collect the results into a List

		}
	}

	@Deprecated
	public void onTest() {
	}

	public void layerGeometries() {

		String layerIdCapture = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("layerid");


		char separator = '-';

		int separatorIndex = layerIdCapture.indexOf(separator);


		String zCompleta = layerIdCapture.substring(1, separatorIndex);

		try {

			TypedQuery<Layer> queryApp = entityManager.createNamedQuery(Layer.LAYER_ID, Layer.class);
			queryApp.setParameter("id", zCompleta);

			Layer layerFind = queryApp.getSingleResult();

			this.selectedLayer = layerFind;
			paginateStart8();
		} catch (Exception e) {
		}

	}

	public void beanMethodLayerEdit() {

		String layerIdCapture = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("layerid");


		char separator = '-';

		int separatorIndex = layerIdCapture.indexOf(separator);


		String zCompleta = layerIdCapture.substring(1, separatorIndex);

		try {
			TypedQuery<Layer> queryApp = entityManager.createNamedQuery(Layer.LAYER_ID, Layer.class);
			queryApp.setParameter("id", zCompleta);
			Layer layerFind = queryApp.getSingleResult();
			// layerFind.getFields());
			// layerFind.getLayerCategories());

			this.selectedLayer = layerFind;

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public void beanMethodLayerGeoEdit() {
		String layerIdCapture = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("layerid");


		char separator = '-';

		int separatorIndex = layerIdCapture.indexOf(separator);


		String zCompleta = layerIdCapture.substring(1, separatorIndex);

		try {

			TypedQuery<Layer> queryApp = entityManager.createNamedQuery(Layer.LAYER_ID, Layer.class);
			queryApp.setParameter("uuid", zCompleta);

			Layer layerFind = queryApp.getSingleResult();

			this.selectedLayer = layerFind;

			model = new DynaFormModel();

		} catch (Exception e) {
		}

	}

	private String inputFile;

	public String getInputFile() {
		return this.inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	String copyFileImage(String fileName, InputStream in, boolean filePath) throws Exception {

		try {

			Random random = new Random();
			int randomInt = random.nextInt(1000000000);

			LocalDateTime data = LocalDateTime.now();
			String fileNameOnly = data.toString() + "-" + String.valueOf(randomInt) + "_" + fileName;

			String appDirectory = save_FILE_PATH + "/files/" + userData.getRealmEntity() + "/"
					+ this.applicationEntity.getId();
			String appDirectoryImage = appDirectory + "/image";
			File appDir = new File(appDirectory);
			if (!appDir.exists()) {
				appDir.mkdir();
			}

			File appDirImg = new File(appDirectoryImage);
			if (!appDirImg.exists()) {
				appDirImg.mkdir();
			}

			String fileNameNew = appDirectoryImage + "/" + fileNameOnly;

			// String fileNameNew = LoadInitParam.saveFilePath() + "/tmp/" + fileNameOnly;

			OutputStream out = new FileOutputStream(new File(fileNameNew));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();

			if (filePath) {
				setInputFile(fileNameNew);
				return fileNameNew;
			} else {
				setInputFile(fileNameOnly);
				return fileNameOnly;
			}

		} catch (IOException e) {

		}
		return fileName;

	}

	private List<LayerView> appLayers = new ArrayList<>();

	public void setAppLayers(List<LayerView> appLayers) {
		this.appLayers = appLayers;
	}

	public List<LayerView> getAppLayers() {
		return appLayers;
	}

	// LAYER GROUPS

	private List<LayerView> appLayersGroup = new ArrayList<>();

	public List<LayerView> getAppLayersGroup() {
		return appLayersGroup;
	}

	public void setAppLayersGroup(List<LayerView> appLayersGroup) {
		this.appLayersGroup = appLayersGroup;
	}

	// =====================
	private LayerView selectedLayerView;

	public LayerView getSelectedLayerView() {
		return selectedLayerView;
	}

	public void setSelectedLayerView(LayerView selectedLayerView) {
		this.selectedLayerView = selectedLayerView;
	}

	public void onRowReorder(ReorderEvent event) {
		FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Row Moved",
				"From: " + event.getFromIndex() + ", To:" + event.getToIndex());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowSelect(SelectEvent<LayerView> event) {
		FacesMessage msg = new FacesMessage("Camada Selecionada", null);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		PrimeFaces.current().ajax().addCallbackParam("namehash", event.getObject().getLayerhash());
		PrimeFaces.current().ajax().addCallbackParam("layerId", event.getObject().getLayerid());
	}

	public void onRowUnselect(UnselectEvent<LayerView> event) {
		FacesMessage msg = new FacesMessage("Layer Unselected", event.getObject().getLayerid());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	// =====================

	@Transactional
	public void onReorder() {
		List<String> layersRoot = new ArrayList<String>();
		try {
			Integer novaSequencia = 1;
			for (LayerView layerView : this.appLayers) {
				Layer layerUpdateSeq = entityManager.find(Layer.class, Long.valueOf(layerView.getLayerid()));
				if (layerUpdateSeq.getOrderlayer() != novaSequencia) {
					layerUpdateSeq.setOrderlayer(novaSequencia);
					entityManager.merge(layerUpdateSeq);  //FIXME CRIAR UPDATE DIRETO NO ENTITY
					entityManager.flush();
				}
				layersRoot.add(layerUpdateSeq.layerString());
				novaSequencia++;
			}
			Integer novaSequencia2 = 1;
			for (LayerView layerView2 : this.appLayers) {
				if (layerView2.getZIndex() != novaSequencia2) {
					layerView2.setZIndex(novaSequencia2);
				}
				novaSequencia2++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso", "Ordem Alterada!"));
		String layersRootjSON = new Gson().toJson(layersRoot);
		PrimeFaces.current().ajax().addCallbackParam("layersRoot", layersRootjSON);
	}

	@Transactional
	public void onReorderGroup() {
		List<String> layersGroup = new ArrayList<String>();
		try {

			Integer novaSequencia = 1;
			for (LayerView layerView : this.appLayersGroup) {
				Layer layerGroupUpdateSeq = entityManager.find(Layer.class, Long.valueOf(layerView.getLayerid()));
				layerGroupUpdateSeq.setOrderlayer(novaSequencia);
				layersGroup.add(layerGroupUpdateSeq.layerString());
				entityManager.merge(layerGroupUpdateSeq);
				novaSequencia++;
			}
			entityManager.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso", "Ordem Alterada!"));

		String layersGroupjSON = new Gson().toJson(layersGroup);

		PrimeFaces.current().ajax().addCallbackParam("layersGroup", layersGroupjSON);

		PrimeFaces.current().ajax().addCallbackParam("layerString", selectedLayerGroup.layerString());

	}

	/**
	 * Update the Layer properties changed in 'LayerEdit.xhtml' This method provides
	 * update and insert functionality for the 'layer' abstract class FIXME The
	 * method to Insert new Layer is not implement yet
	 */

	@Transactional
	public void updateLayerItem(LayerView layerItem) {
		List<LayerView> layersNew = new ArrayList<LayerView>();
		this.appLayers.retainAll(this.getAppLayers());
		for (LayerView attr : this.getAppLayers()) {
			if (attr.getLayerid().equals(layerItem.getLayerid())) {
				layersNew.add(layerItem);
			} else {
				layersNew.add(attr);
			}
		}
		this.setAppLayers(layersNew);
	}

	@Transactional
	public void removeLayerItem(LayerView layerItem) {
		List<LayerView> layersNew = new ArrayList<LayerView>();
		this.appLayers.retainAll(this.getAppLayers());
		for (LayerView attr : this.getAppLayers()) {
			if (!attr.getLayerid().equals(layerItem.getLayerid())) {
				layersNew.add(attr);
			}
		}
		this.appLayers = layersNew;
	}

	public void updateLayerView(LayerView layerItem) {
		List<LayerView> layersNew = new ArrayList<LayerView>();
		this.appLayers.retainAll(this.getAppLayers());
		for (LayerView attr : this.getAppLayers()) {
			if (attr.getLayerid().equals(layerItem.getLayerid())) {
				layersNew.add(layerItem);
			} else {
				layersNew.add(attr);
			}
		}
		this.setAppLayers(layersNew);
	}
	
	
	
	

	@Transactional
	public void updateLayer() {


		entityManager.merge(this.selectedLayer);
		entityManager.flush();


		this.getApplicationEntity().updateLayerItem(this.selectedLayer);


		if (this.selectedLayer.getFather() != null) {
			this.selectedLayer.getFather().getChildrens().add(this.selectedLayer);
			this.getApplicationEntity().updateLayerItem(this.selectedLayer.getFather());
		} else {
			if (this.selectedLayerGroup != null) {
				this.selectedLayerGroup.getChildrens().remove(this.selectedLayer);
				long registros = this.applicationEntity.getLayers().stream()
						.filter(l -> l.getFather() != null && l.getFather().equals(this.selectedLayerGroup)).count();
				if (registros < 1)
					this.getApplicationEntity().clearLayerChildren(this.selectedLayerGroup);
			}
		}

		// AQUI VAI FAZER UPDATE .....................................................
		if (this.selectedLayerGroup != null) {
			appLayersGroup = new ArrayList<>();
			Integer sequence = 1;
			for (Layer lnew2 : this.applicationEntity.getLayers().stream()
					.filter(l -> l.getFather() != null && l.getFather().equals(this.selectedLayerGroup)).toList()) {
				LayerView layerView2 = new LayerView();
				layerView2.setLayerid(lnew2.getId().toString());
				layerView2.setVisible(lnew2.getSelected());
				layerView2.setOpacity(lnew2.getOpacity());
				layerView2.setZIndex(sequence);
				layerView2.setLayerhash(lnew2.layerString());
				layerView2.setName(lnew2.getName());
				layerView2.setFather(lnew2.getChildrens().size() > 0);
				appLayersGroup.add(layerView2);
				sequence++;
			}
		}

		// AQUI VAI FAZER UPDATE TAMBEM................................................

		// AQUI NAO MUDA...................................
		appLayers = new ArrayList<>();
		Integer sequence2 = 1;
		for (Layer lnew : this.applicationEntity.getLayers().stream().filter(l -> l.getFather() == null).toList()) {
			LayerView layerView = new LayerView();
			layerView.setLayerid(lnew.getId().toString());
			layerView.setVisible(lnew.getSelected());
			layerView.setOpacity(lnew.getOpacity());
			layerView.setZIndex(sequence2);
			layerView.setLayerhash(lnew.layerString());
			layerView.setName(lnew.getName());

			layerView.setFather(lnew.getChildrens().size() > 0);
			appLayers.add(layerView);
			sequence2++;
		}
		// AQUI NAO MUDA...................................



		/**
		 * At the point we will refresh the Application class to refresh the frontend in
		 * 'Layer.xhtml'
		 */
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage("Sucesso!", "Camada: " + selectedLayer.getName() + " Alterada!"));

		
		
		
		
		
		
		
		
		String classLayer = "LayerEntity";
		if (!(this.selectedLayer instanceof LayerEntity)) {
			classLayer = "LayerStyled";
		}

		
		PrimeFaces.current().ajax().addCallbackParam("classLayer", classLayer);
		 
		

		PrimeFaces.current().ajax().addCallbackParam("zindex", this.selectedLayer.getOrderlayer());
		
		PrimeFaces.current().ajax().addCallbackParam("labelstylejson", this.selectedLayer.getLabelStyle());
		PrimeFaces.current().ajax().addCallbackParam("geomstylejson", this.selectedLayer.getGeometryStyle());
		
		PrimeFaces.current().ajax().addCallbackParam("lstylelayer", this.selectedLayer.getLabelStyle());
		PrimeFaces.current().ajax().addCallbackParam("gstylelayer", this.selectedLayer.getGeometryStyle());
		
		PrimeFaces.current().ajax().addCallbackParam("stringslayer", this.selectedLayer.getStrings().toPrettyString());
		
		PrimeFaces.current().ajax().addCallbackParam("namelayer", this.selectedLayer.getId().toString());
		PrimeFaces.current().ajax().addCallbackParam("typelayer", this.selectedLayer.getDtype());
		
		
		
		if (this.selectedLayer instanceof LayerWMSGeoserver) {

			LabelStyle labelstylejson = this.selectedLayer.getLabelStyle();
			GeometryStyle geomstylejson = this.selectedLayer.getGeometryStyle();
			
	//		PrimeFaces.current().ajax().addCallbackParam("labelstylejson", labelstylejson);
	//		PrimeFaces.current().ajax().addCallbackParam("geomstylejson", geomstylejson);
	//		PrimeFaces.current().ajax().addCallbackParam("layername", this.selectedLayer.layerString());

			String layerNameSLD = this.selectedLayer.layerString();
			String sldFileName = layerName + ".sld";


			Color color = extractRGBA(geomstylejson.getRgbafillcolor());

			// The getAlpha() method returns an int between 0-255
			// For the CSS format (0.0-1.0)

			String hexColor = rgbToHex(color.getRed(), color.getGreen(), color.getBlue());

			String contentXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<StyledLayerDescriptor\n"
					+ "	xmlns:sld=\"http://www.opengis.net/sld\"\n" + "	xmlns=\"http://www.opengis.net/sld\"\n"
					+ "	xmlns:gml=\"http://www.opengis.net/gml\"\n"
					+ "	xmlns:ogc=\"http://www.opengis.net/ogc\" version=\"1.0.0\">\n" + "	<Name>" + layerName
					+ "</Name>\n" + "	<Title>" + layerName + "</Title>\n" + "	<Abstract>" + layerName
					+ "</Abstract>\n" + "	<UserLayer>\n" + "		<Name>layer</Name>\n"
					+ "		<LayerFeatureConstraints>\n" + "			<FeatureTypeConstraint/>\n"
					+ "		</LayerFeatureConstraints>\n" + "		<UserStyle>\n"
					+ "			<Name>Default Styler</Name>\n" + "			<FeatureTypeStyle>\n"
					+ "				<Name>name</Name>\n" + "				<Rule>\n"

					+ "<TextSymbolizer>\n"

					+ " <Label>\n" + "   <ogc:PropertyName>" + labelstylejson.getField() + "</ogc:PropertyName>\n"
					+ " </Label>\n"

					+ "	 <Font>\n" + "	 <CssParameter name=\"font-family\">" + labelstylejson.getFont()
					+ "</CssParameter>\n" + "	 <CssParameter name=\"font-size\">" + labelstylejson.getSize()
					+ "</CssParameter>\n" + "	 <CssParameter name=\"font-style\">" + labelstylejson.getText()
					+ "</CssParameter>\n" + "	 <CssParameter name=\"font-weight\">"
					+ ((labelstylejson.getWeight()) ? "bold" : "normal") + "</CssParameter>\n" + "	 </Font>\n"

					+ "	 <Halo>\n" + "	 <Radius>" + labelstylejson.getOutlineWidth() + "</Radius>\n" + "	 <Fill>\n"
					+ "	 <CssParameter name=\"fill\">" + labelstylejson.getOutline() + "</CssParameter>\n"
					+ "	 </Fill>\n" + "	 </Halo>\n"

					+ "	<Fill>\n" + "	<CssParameter name=\"fill\">" + labelstylejson.getColor() + "</CssParameter>\n"
					+ "	</Fill>\n"

					+ "</TextSymbolizer>\n"

					+ "					<PolygonSymbolizer>\n" + "						<Fill>\n"
					+ "							<CssParameter name=\"fill\">" + hexColor + "</CssParameter>\n"
					+ "							<CssParameter name=\"fill-opacity\">" + (color.getAlpha() / 255.0f)
					+ "</CssParameter>\n" + "						</Fill>\n" + "						<Stroke>\n"
					+ "							<CssParameter name=\"stroke\">" + geomstylejson.getRgbastrokecolor()
					+ "</CssParameter>\n" + "							<CssParameter name=\"stroke-width\">"
					+ geomstylejson.getStrokewidth() + "</CssParameter>\n"
					+ "							<CssParameter name=\"stroke-dasharray\">"
					+ geomstylejson.getStrokedashLenght() + " " + geomstylejson.getStrokedashSpace() + "</CssParameter>\n"
					+ "						</Stroke>\n" + "					</PolygonSymbolizer>\n"
					+ "				</Rule>\n" + "			</FeatureTypeStyle>\n" + "		</UserStyle>\n"
					+ "	</UserLayer>\n" + "</StyledLayerDescriptor>";

			try {

				Amor4(sldFileName, tenant, contentXML);
			} catch (IOException | JAXBException | TransformerException e) {
				e.printStackTrace();
			}

			TypedQuery<ClientComponentEntity> queryApp = entityManager
					.createNamedQuery(ClientComponentEntity.COMPONENT_CLIENT_ID, ClientComponentEntity.class);
			queryApp.setParameter("clientId", "geoserver");
			List<ClientComponentEntity> clientCompEntity = queryApp.getResultList();

			ClientComponentEntity clientGeoserver = clientCompEntity.get(0);

			try {
				CreateProjectWorkspace.updateLayerSLD(layerName, layerName, tenant, clientGeoserver);
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
		
		
		
		
		

	}
	
	
	

	public static String rgbToHex(int r, int g, int b) {
		// Ensure values are within the 0-255 range
		if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
			throw new IllegalArgumentException("RGB values must be between 0 and 255.");
		}

		// Use String.format to get the padded hexadecimal string
		// %02X means: print an integer as a two-digit uppercase hexadecimal number,
		// padding with zeros if necessary.
		return String.format("#%02X%02X%02X", r, g, b);
	}

	public static Color extractRGBA(String colorString) {
		// Regex to capture the values inside rgb() or rgba()
		Pattern pattern = Pattern.compile("rgba?\\((\\d+),\\s*(\\d+),\\s*(\\d+)(?:,\\s*([0-9.]+))?\\)");
		Matcher matcher = pattern.matcher(colorString);

		if (matcher.matches()) {
			try {
				int r = Integer.parseInt(matcher.group(1));
				int g = Integer.parseInt(matcher.group(2));
				int b = Integer.parseInt(matcher.group(3));
				float a = 1.0f; // Default alpha to 1.0 (opaque)

				// Check if an alpha value was captured
				if (matcher.group(4) != null) {
					a = Float.parseFloat(matcher.group(4));
				}

				// Create a Color object
				Color color = new Color(r, g, b, (int) (a * 255));

				return color;

			} catch (NumberFormatException e) {
			}
		} else {
		}
		return null;
	}

	public void Amor4(String fileNameSLD, String workspaceSLD, String contentXML)
			throws ClientProtocolException, IOException, JAXBException, TransformerException {

		String fileNameComp = LoadInitParameter.GEOSERVER_DATA_DIR  + "/workspaces/" + workspaceSLD + "/styles/" + fileNameSLD;

		try {
			// Write the string to the file
			Files.writeString(Paths.get(fileNameComp), contentXML);
		} catch (IOException e) {
			System.err.println("Error writing file: " + e.getMessage());
			e.printStackTrace();
		}

	}

	/*
	 * Support searching ApplicationEntity entities with pagination
	 */

	@Inject
	private ActiveUser activeUser;

	@Transactional
	public List<AppUserMappingEntity> getAppsUserMap() {

		if (activeUser.isPresent()) {

			TypedQuery<AppUserMappingEntity> queryApps = entityManager.createNamedQuery(AppUserMappingEntity.SELECT,
					AppUserMappingEntity.class);

			queryApps.setParameter("personUuid", activeUser.get().getUuid());

			return queryApps.getResultList();
		} else {
			return null;
		}
	}

	public List<AppUserMappingEntity> getAppsUserMapAll() {
		if (userData.getPerson() != null) {
			TypedQuery<AppUserMappingEntity> queryApps = entityManager.createNamedQuery(AppUserMappingEntity.SELECT_ALL,
					AppUserMappingEntity.class);
			return queryApps.getResultList();
		} else {
			return null;
		}
	}

	private int page33;
	private long count33;
	private List<AppUserMappingEntity> pageItems33;

	public int getPage33() {
		return this.page33;
	}

	public void setPage33(int page33) {
		this.page33 = page33;
	}

	public int getPageSize33() {
		return 10;
	}

	public String search33() {
		this.page33 = 0;
		return null;
	}

	public void paginate33() {

		try {

			CriteriaBuilder builder33 = entityManager.getCriteriaBuilder();

			// Populate this.count

			CriteriaQuery<Long> countCriteria33 = builder33.createQuery(Long.class);
			Root<AppUserMappingEntity> root33 = countCriteria33.from(AppUserMappingEntity.class);
			countCriteria33 = countCriteria33.select(builder33.count(root33))
					.where(getSearchPredicates33(root33, entityManager));
			this.count33 = entityManager.createQuery(countCriteria33).getSingleResult();

			// Populate this.pageItems

			CriteriaQuery<AppUserMappingEntity> criteria33 = builder33.createQuery(AppUserMappingEntity.class);
			root33 = criteria33.from(AppUserMappingEntity.class);
			TypedQuery<AppUserMappingEntity> query33 = entityManager
					.createQuery(criteria33.select(root33).where(getSearchPredicates33(root33, entityManager)));
			query33.setFirstResult(this.page33 * getPageSize33()).setMaxResults(getPageSize33());
			this.pageItems33 = query33.getResultList();

		} catch (Exception e) {
		}

	}

	private Predicate[] getSearchPredicates33(Root<AppUserMappingEntity> root33, EntityManager session) {

		CriteriaBuilder builder33 = session.getCriteriaBuilder();
		List<Predicate> predicatesList33 = new ArrayList<Predicate>();

		Long userFilter33 = userData.getPersonId();

		if (userFilter33 != null) {
			predicatesList33.add(builder33.equal(root33.get("userId"), userFilter33));
		}

		return predicatesList33.toArray(new Predicate[predicatesList33.size()]);
	}

	public List<AppUserMappingEntity> getPageItems33() {
		return this.pageItems33;
	}

	public long getCount33() {
		return this.count33;
	}

	private String locale = "pt_BR";

	private static Map<String, Object> countries;

	static {
		countries = new LinkedHashMap<String, Object>();
		countries.put("Português", new Locale("pt", "BR"));
		countries.put("English", new Locale("en"));
		countries.put("Spanish", new Locale("es"));
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

	private String layer_id;

	public String getLayer_id() {
		return this.layer_id;
	}

	public void setLayer_id(String layer_id) {

		try {

			Layer layerFind = entityManager.find(Layer.class, layer_id);

			this.selectedLayer = layerFind;

			this.layer_id = layer_id;

		} catch (Exception e) {
		}
	}

	// ---------------------------------------

	public List<AbstractGeometry> getPageItemsAG2() {

		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			// Populate this.count
			CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
			Root<AbstractGeometry> root = countCriteria.from(AbstractGeometry.class);

			jakarta.persistence.criteria.Join<AbstractGeometry, LayerCategory> tagJoin = null;

			countCriteria = countCriteria.select(builder.count(root))
					.where(getSearchPredicatesPost2(root, tagJoin, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();
			// Populate this.pageItems
			CriteriaQuery<AbstractGeometry> criteria = builder.createQuery(AbstractGeometry.class);
			root = criteria.from(AbstractGeometry.class);

			if (this.selectedLayer.getLayerCategories().size() > 0) {
				tagJoin = root.join("categories");
				root.fetch("categories");
			}
			TypedQuery<AbstractGeometry> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicatesPost2(root, tagJoin, entityManager)));
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

	private Predicate[] getSearchPredicatesPost2(Root<AbstractGeometry> root,
			Join<AbstractGeometry, LayerCategory> tagJoin, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		if (tagJoin != null && !"".equals(tagJoin.toString())) {
			String idCategory = this.getCategory_id();
			if (idCategory != null && !"".equals(idCategory)) {
				predicatesList.add(builder.equal(tagJoin.get("id"), idCategory));
			}
		}

		// if (nome != null && !"".equals(nome)) {
		// predicatesList.add(builder.like(
		// builder.lower(root.<String> get("nome")),
		// "%" + nome.toLowerCase() + '%'));
		// }

		if (this.getSelectedLayer() instanceof Layer) {
			if (this.getSelectedLayer() != null) {
				predicatesList.add(builder.equal(root.get("layer"), this.getSelectedLayer()));
			}
		}
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	private String category_id;

	public String getCategory_id() {
		return this.category_id;
	}

	public void setCategory_id(String category_id) {
		this.category_id = category_id;
	}
	// ---------------------------------

	

	private List<ModuleFilter> moduleFiltersAll;

	public List<ModuleFilter> getModuleFiltersAll() {
		return moduleFiltersAll;
	}

	public void setModuleFiltersAll(List<ModuleFilter> moduleFiltersAll) {
		this.moduleFiltersAll = moduleFiltersAll;
	}

	@Inject
	private ActiveLocale activeLocale;

	@Transactional
	public void beanMethodUps() { // FIXME

		try {

			String objectidCapture = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.get("objectid");

			String coordinateCapture = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
					.get("coordinate");

			TypedQuery<AbstractGeometry> queryAbs = entityManager.createNamedQuery(AbstractGeometry.SURFACE_ID,
					AbstractGeometry.class);

			queryAbs.setParameter("id", objectidCapture);

			this.abstractGeometry = queryAbs.getSingleResult();

			Geometry geometry = null;
			WKTReader wktReader = new WKTReader();

			Integer appSRID = this.getApplicationEntity().getEpsg();
			Integer layerSRID = this.abstractGeometry.getLayer().getEpsg();

			String consulta = "SELECT  public.st_astext(" + " public.st_transform(public.st_geomfromtext("
					+ "public.st_asewkt(public.st_force2d('" + coordinateCapture + "')" + " 	), " + appSRID + "), "
					+ layerSRID + ")) ";

			Query queryFF = entityManager.createNativeQuery(consulta);

			// List<Object> listFF =
			// Collections.checkedList(queryFF.getResultList(), Object.class);

			// GUAVA
			List<Object> listFF = Lists.newArrayList(Iterables.filter(queryFF.getResultList(), Object.class));

			// List<Object> listGuava2 = ImmutableList.copyOf(
			// Iterables.filter(queryFF.getResultList(),
			// Object.class));


			// List<ProducaoApontamento> aLista = Collections.checkedList( c.list(),
			// ProducaoApontamento.class);
			// clientes = Collections.checkedList(query.list(), Cliente.class);
			Geometry targetGeometry1 = null;

			if (!listFF.isEmpty()) {

				Object geometriaString = listFF.get(0);
				try {
					geometry = wktReader.read(geometriaString.toString());

					// ------------------------------------------------------------------------------

					targetGeometry1 = geometry;
					targetGeometry1.setSRID(layerSRID);

					// targetGeometry1.toString());

					// -----------------------------------------------------------------------------------------------------------

				} catch (ParseException e) {
					e.printStackTrace();

				}
			}

			if (targetGeometry1 != null) {

				switch (this.abstractGeometry.getClass().getSimpleName()) {

				case "Polygon":
					Polygon mergeNew = (Polygon) this.abstractGeometry;
					mergeNew.setGeometry(targetGeometry1);
					entityManager.merge(mergeNew);
					break;

				case "Point":
					Point mergeNew2 = (Point) this.abstractGeometry;
					mergeNew2.setGeometry(targetGeometry1);
					entityManager.merge(mergeNew2);
					break;

				case "Linestring":
					Linestring mergeNew3 = (Linestring) this.abstractGeometry;
					mergeNew3.setGeometry(targetGeometry1);
					entityManager.merge(mergeNew3);
					break;

				case "MultiPolygon":
					// MultiPolygon mergeNew4 = (MultiPolygon) this.abstractGeometry;
					// mergeNew4.setGeometry(targetGeometry1);
					// entityManager.merge(mergeNew4);
					break;
				}
			}

			entityManager.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// JAVASXRIPT ESTRUCTURE GISGOOG

	// --------------------- NEW LAYER

	private int page2;
	private long count2;
	private List<Layer> pageItems2;

	private Layer example2;

	public int getPage2() {
		return this.page2;
	}

	public void setPage2(int page2) {
		this.page2 = page2;
	}

	public int getPageSize2() {
		return 30;
	}

	public Layer getExample2() {
		return this.example2;
	}

	public void setExample2(Layer example2) {
		this.example2 = example2;
	}

	public String search2() {
		this.page2 = 0;
		return null;
	}

	public void paginate2() {

		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();
			// Populate this.count
			CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
			Root<Layer> root = countCriteria.from(Layer.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates2(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();
			// Populate this.pageItems
			CriteriaQuery<Layer> criteria = builder.createQuery(Layer.class);
			root = criteria.from(Layer.class);
			TypedQuery<Layer> query = entityManager
					.createQuery(criteria.select(root).orderBy(builder.asc(root.get("orderlayer"))).select(root)
							.where(getSearchPredicates2(root, entityManager)));
			query.setFirstResult(this.page2 * getPageSize2()).setMaxResults(getPageSize2());
			this.pageItems2 = query.getResultList();

		} catch (Exception e) {
		}

	}

	private Predicate[] getSearchPredicates2(Root<Layer> root, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		// put here your filter list
		try {
			if (this.getApplicationEntity() != null) {
				predicatesList.add(builder.equal(root.get("applicationEntity"), this.getApplicationEntity()));
			}
			// String nameString = "Materias";
			// predicatesList.add(builder.notEqual(root.get("name"), nameString));
			String name = "";
			if (name != null && !"".equals(name)) {
				predicatesList
						.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Layer> getPageItems2() {
		return this.pageItems2;
	}

	public void setPageItems2(List<Layer> pageItems2) {
		this.pageItems2 = pageItems2;
	}

	public long getCount2() {
		return this.count2;
	}

	@Transactional
	public void addNewLayerPolygon() {

		try {

			LayerPolygon newLayer = new LayerPolygon();
			newLayer.setName("POLYGON");
			newLayer.setApplicationEntity(this.getApplicationEntity());

			newLayer.setDescription("CAMADA POLYGON ");
			newLayer.setSelected(true);
			newLayer.setZoom(5);
			newLayer.setMinZoom(0);
			newLayer.setMaxZoom(20);
			newLayer.setEditable(true);

			newLayer.setTitle("POLYGONS");
			newLayer.setOrderlayer(this.getApplicationEntity().getLayers().size() + 1);
			LabelStyle styleNew = new LabelStyle();
			styleNew.setMaxreso(new BigDecimal(0));
			styleNew.setMinreso(new BigDecimal(0));
			newLayer.setLabelStyle(styleNew);

			entityManager.persist(newLayer);

			/**
			 * At the point we will refresh the Application class to refresh the frontend in
			 * 'Layer.xhtml'
			 */

			this.applicationEntity.add(newLayer);
			entityManager.flush();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Camada Adicionada!"));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro!", "Camada Não Adicionada!"));
		}

	}

	@Transactional
	public void addNewLayerMPolygon() {

		try {

			LayerMultiPolygon newLayer = new LayerMultiPolygon();
			newLayer.setName("MÚLTIPLOS");
			newLayer.setApplicationEntity(this.getApplicationEntity());

			newLayer.setDescription("CAMADA MULTI POLYGONS ");
			newLayer.setSelected(true);
			newLayer.setZoom(5);
			newLayer.setMinZoom(0);
			newLayer.setMaxZoom(20);
			newLayer.setEditable(true);

			newLayer.setTitle("MULTI-POLS");
			newLayer.setOrderlayer(this.getApplicationEntity().getLayers().size() + 1);
			LabelStyle styleNew = new LabelStyle();
			styleNew.setMaxreso(new BigDecimal(0));
			styleNew.setMinreso(new BigDecimal(0));
			newLayer.setLabelStyle(styleNew);

			entityManager.persist(newLayer);

			/**
			 * At the point we will refresh the Application class to refresh the frontend in
			 * 'Layer.xhtml'
			 */
			this.applicationEntity.add(newLayer);
			entityManager.flush();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Camada Adicionada!"));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro!", "Camada Não Adicionada!"));

		}

	}

	@Transactional
	public void addNewLayerLinestring() {

		try {

			LayerLinestring newLayer = new LayerLinestring();
			newLayer.setName("LINESTRING");
			newLayer.setApplicationEntity(this.getApplicationEntity());

			newLayer.setDescription("LINESTRING LAYER");
			newLayer.setSelected(true);
			newLayer.setZoom(5);
			newLayer.setMinZoom(0);
			newLayer.setMaxZoom(20);
			newLayer.setEditable(true);

			newLayer.setTitle("LINES");
			newLayer.setOrderlayer(this.getApplicationEntity().getLayers().size() + 1);
			LabelStyle styleNew = new LabelStyle();
			styleNew.setMaxreso(new BigDecimal(0));
			styleNew.setMinreso(new BigDecimal(0));
			newLayer.setLabelStyle(styleNew);

			entityManager.persist(newLayer);

			this.applicationEntity.add(newLayer);
			entityManager.flush();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Camada Adicionada!"));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro!", "Camada Não Adicionada!"));

		}
	}

	@Transactional
	public void addNewLayerMunicipio() {

		try {


			LayerMultiPolygon newLayerMultiPolygon = new LayerMultiPolygon();
			newLayerMultiPolygon.setName("CIDADE");
			newLayerMultiPolygon.setApplicationEntity(this.getApplicationEntity());

			newLayerMultiPolygon.setDescription("CIDADE");
			newLayerMultiPolygon.setSelected(true);
			newLayerMultiPolygon.setZoom(5);
			newLayerMultiPolygon.setMinZoom(0);
			newLayerMultiPolygon.setMaxZoom(20);
			newLayerMultiPolygon.setEditable(true);

			newLayerMultiPolygon.setTitle("CIDADE");
			newLayerMultiPolygon.setOrderlayer(this.getApplicationEntity().getLayers().size() + 1);
			LabelStyle styleNew = new LabelStyle();
			newLayerMultiPolygon.setLabelStyle(styleNew);

			entityManager.persist(newLayerMultiPolygon);

			/**
			 * At the point we will refresh the Application class to refresh the frontend in
			 * 'Layer.xhtml'
			 */
			this.applicationEntity.add(newLayerMultiPolygon);

			entityManager.flush();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Camada Adicionada!"));


		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro!", "Camada Não Adicionada!"));

		}

	}

	@Transactional
	public void parcelar() {

		try {



			Polygon obraFind = entityManager.find(Polygon.class, this.getAbstractGeometry().getId());

			ProcessProjection processProjection = new ProcessProjection();
			processProjection.runProcessProjection(entityManager, this.applicationEntity, obraFind);

			// Integer parteCalc = obraFind.getFather().getChildrens().size() + 1;


			// obraFind.setParte(parteCalc);
			// entityManager.merge(obraFind);


			entityManager.flush();
		} catch (Throwable e) {

		}

	}

	@Transactional
	public void vertificar() {

		try {


			Polygon obraFind = entityManager.find(Polygon.class, this.getAbstractGeometry().getId());

			ProcessProjection processProjection = new ProcessProjection();
			processProjection.runProcessProjectionVertices(entityManager, this.applicationEntity, obraFind);

			entityManager.flush();
		} catch (Exception e) {
		}

	}

	public String convertToASCII2(String text) {
		return text.replaceAll("[ãâàáä]", "a").replaceAll("[êèéë]", "e").replaceAll("[îìíï]", "i")
				.replaceAll("[õôòóö]", "o").replaceAll("[ûúùü]", "u").replaceAll("[ÃÂÀÁÄ]", "A")
				.replaceAll("[ÊÈÉË]", "E").replaceAll("[ÎÌÍÏ]", "I").replaceAll("[ÕÔÒÓÖ]", "O")
				.replaceAll("[ÛÙÚÜ]", "U").replace('ç', 'c').replace('Ç', 'C').replace('ñ', 'n').replace('Ñ', 'N');
	}

	// ===========================================================

	// --------------------

	public void uploadLinhaBase(FileUploadEvent event) {


		FacesMessage msg = new FacesMessage("SUCESSO!",
				"ARQUIVO: " + event.getFile().getFileName() + " IMPORTADO SEM ERROS");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		try {
			UploadVertices uploadVertices = new UploadVertices();


			this.copyFileOds(event.getFile().getFileName(), event.getFile().getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			this.uploadProcessODS(this.inputFile, this.getApplicationEntity(), this.getSelectedLayer());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	GeometryFactory geometryFactory = new GeometryFactory();

	public void uploadProcessODS(String fileNameNew, ApplicationEntity gleba, Layer layerOds)
			throws IOException, ParseException {


	}

	@Transactional
	public void uploadWGS84(FileUploadEvent event) {

		try {

			FacesMessage msg = new FacesMessage("SUCESSO!", event.getFile().getFileName() + " ENVIADO");
			FacesContext.getCurrentInstance().addMessage(null, msg);

			ApplicationEntity projectRuralEntity = (ApplicationEntity) this.getApplicationEntity();
			UploadProcessODSWGS84 uploadProcessODSWGS84 = new UploadProcessODSWGS84();
			uploadProcessODSWGS84.copyFileWGS84(event.getFile().getFileName(), event.getFile().getInputStream(),
					entityManager, projectRuralEntity);

			entityManager.flush();
		} catch (Exception e) {
		}

	}

	public void copyFileOds(String fileName, InputStream in) throws Exception {
		// ░░░░░░░░░░ ★
		// ░░░░░░░░░░██
		// ░░░░░░░░░████
		// ░░░░░░░██▒▒▒▒██
		// ░░░░░██▒▒▒▒▒▒▒▒██
		// ░░░░░░░██▒▒▒▒██
		// ░░░░░░░░██████
		// ░░░░░░░███▓▓███

		try {

			Random random = new Random();
			int randomInt = random.nextInt(1000000000);

			LocalDateTime data = LocalDateTime.now();
			String fileNameOnly = data.toString() + "-" + String.valueOf(randomInt) + "_" + fileName;

			String appDirectory = save_FILE_PATH + "/files/" + userData.getRealmEntity() + "/"
					+ this.applicationEntity.getId();


			String appDirectoryImage = appDirectory + "/ods";
			File appDir = new File(appDirectory);
			if (!appDir.exists()) {
				appDir.mkdir();
			}

			File appDirImg = new File(appDirectoryImage);
			if (!appDirImg.exists()) {
				appDirImg.mkdir();
			}


			String fileNameNew = appDirectoryImage + "/" + fileNameOnly;


			// String fileNameNew = LoadInitParam.saveFilePath() + "/tmp/" + fileNameOnly;

			OutputStream out = new FileOutputStream(new File(fileNameNew));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();

			setInputFile(fileNameNew);

		} catch (IOException e) {
		}

	}

	// -----------------------

	GeometryFactory gf = new GeometryFactory();

	ProcessAreaCartesian processAreaCartesian = new ProcessAreaCartesian();
	ProcessAreaCartesian2 processAreaCartesian2 = new ProcessAreaCartesian2();

	private List<PolygonPoint> verticeobraItems;

	public List<PolygonPoint> getPolygonPointItems() {
		return this.verticeobraItems;
	}

	// ==============================

	public String runCreate(Polygon obraCAP, EntityManager entityManager) {


		if (obraCAP.getVerticeObrasList() == null) {
			return null;
		} else {
			Iterator i = obraCAP.getVerticeObrasList().iterator(); // verticeObraList.iterator();

			// -------------------------------------------------------------------
			if (obraCAP.getVerticeObrasList().size() > 2) {
				Integer srid_ = 31980;
				Integer sequencia = 0;
				@SuppressWarnings("unused")
				double doubleLeste;
				@SuppressWarnings("unused")
				double doubleNorte;
				@SuppressWarnings("unused")
				double doubleAltura;
				org.locationtech.jts.geom.Coordinate[] pointsNew = new Coordinate[obraCAP.getVerticeObrasList().size()
						+ 1];
				org.locationtech.jts.geom.Coordinate[] pointsNew2 = new Coordinate[obraCAP.getVerticeObrasList()
						.size()];

				String X4326 = null;
				String Y4326 = null;
				String utmPoint;
				double mediaAltura = 0;
				while (i.hasNext()) {
					PolygonPoint verticeobra_ = (PolygonPoint) i.next();


					mediaAltura = mediaAltura + verticeobra_.getPointri().getAltura();
					doubleAltura = verticeobra_.getPointri().getAltura();


					// -------------------------------- NEW POSTGIS CONVERTER
					// --------------------------------------------

					utmPoint = "POINT(" + verticeobra_.getPointri().getGeometry().getCoordinate().x + " "
							+ verticeobra_.getPointri().getGeometry().getCoordinate().y + ")";

					String consulta = "SELECT" + " public.ST_X(public.ST_Transform(public.ST_GeomFromText('" + utmPoint
							+ "'," + srid_ + "),4326)) As X4326,"
							+ " public.ST_Y(public.ST_Transform(public.ST_GeomFromText('" + utmPoint + "'," + srid_
							+ "),4326)) As Y4326 ";


					Query queryFF = entityManager.createNativeQuery(consulta);
					List<Object[]> listFF = queryFF.getResultList();
					if (!listFF.isEmpty()) {
						Object stringFF[] = listFF.get(0);
						X4326 = stringFF[0].toString();
						Y4326 = stringFF[1].toString();
					}

					pointsNew[sequencia] = new Coordinate(verticeobra_.getPointri().getGeometry().getCoordinate().x,
							verticeobra_.getPointri().getGeometry().getCoordinate().y);
					pointsNew2[sequencia] = new Coordinate(((-1) * Double.valueOf(X4326).doubleValue()),
							Double.valueOf(Y4326).doubleValue());


					sequencia++;
				}
				mediaAltura = mediaAltura / obraCAP.getVerticeObrasList().size();
				pointsNew[obraCAP.getVerticeObrasList().size()] = pointsNew[0]; // Close de Polygon
				Geometry geomet = gf.createPolygon(pointsNew);
				geomet.setSRID(3857);
				// "------33---------------GEOMET---------------------------------------" +
				// geomet.toString());

				// ------------------------------------ AREA CARTESIAN GET
				// ------------------------------------

				double areaCartesiana[] = processAreaCartesian.testarPTL(pointsNew2, mediaAltura);

				obraCAP.setArea(geomet.getArea());
				obraCAP.setPerimetro(geomet.getLength());

				// obraCAP.setArea(areaCartesiana[0]);
				// obraCAP.setPerimetro(areaCartesiana[1]);

				// ------------------ geomet converter

				obraCAP.setGeometry(geomet);

				obraCAP.getGeometry().setSRID(srid_);

				// FIXME
				// obraCAP.getFather().setGeometry(geomet);

				// obraCAP.getFather().getGeometry().setSRID(obraCAP.getLayer().getApplicationEntity().getEpsg());

				// MUNICIPIO CHOOSER

				// MunicipioCloser municipioCloser = new
				// MunicipioCloser(obraCAP.getGeometry().toString(), entityManager);


				// obraCAP.setMunicipio(municipioCloser.getMunicipio());


				entityManager.merge(obraCAP);

				// session.merge(obraCAP.getFather());



				// AbstractGeometry sss = obraCAP.getFather();


				// sss.setGeometry(geomet);


				// entityManager.merge(sss);



				createLineString2(obraCAP, mediaAltura, entityManager);
				createLineString(obraCAP, mediaAltura, entityManager); // FIXME
				return null;
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage("ERRO", "Polígono necessita no minimo 3 vértices!"));
				return null;
			}
		}

	}

	public void createLineString2(Polygon obraCAP, double mediaAltura, EntityManager session) {
		// LINESTRING START");

		double longitudeFirst1X = 0;
		double latitudeFirst1X = 0;
		double altitudeFirstMeters1X = 0;

		double longitude1X = 0;
		double latitude1X = 0;
		double altitudeMeters1X = 0;

		double longitude2X = 0;
		double latitude2X = 0;
		double altitudeMeters2X = 0;

		double longitudeRadians1 = 0;
		double latitudeRadians1 = 0;

		double longitudeRadians2 = 0;
		double latitudeRadians2 = 0;

		Integer srid_;
		Integer sequencia = 0;
		Coordinate FirstPointGeodetic = null;
		org.locationtech.jts.geom.Coordinate[] pointsNew = new Coordinate[obraCAP.getVerticeObrasList().size() + 1];
		org.locationtech.jts.geom.Coordinate[] pointsSirgasNew = new Coordinate[obraCAP.getVerticeObrasList().size()
				+ 1];

		String X4326 = null;
		String Y4326 = null;
		String utmPoint;

		String X4326Next = null;
		String Y4326Next = null;
		String utmPointNext;

		Iterator<?> i = obraCAP.getVerticeObrasList().iterator();
		Iterator<?> iTo = obraCAP.getVerticeObrasList().iterator();
		Integer numeroVertices = obraCAP.getVerticeObrasList().size();
		Point verticeFirst = null;
		PolygonPoint verticeobraTo = (PolygonPoint) iTo.next();

		while (i.hasNext()) {
			PolygonPoint verticeobra_ = (PolygonPoint) i.next();

			srid_ = 31980;

			// -------------------------------- NEW POSTGIS CONVERTER
			// --------------------------------------------
			utmPoint = "POINT(" + verticeobra_.getPointri().getGeometry().getCoordinate().x + " "
					+ verticeobra_.getPointri().getGeometry().getCoordinate().y + ")";

			String consulta = "SELECT" + " public.ST_X(public.ST_Transform(public.ST_GeomFromText('" + utmPoint + "',"
					+ srid_ + "),4326)) As X4326," + " public.ST_Y(public.ST_Transform(public.ST_GeomFromText('"
					+ utmPoint + "'," + srid_ + "),4326)) As Y4326 ";

			Query queryFF = session.createNativeQuery(consulta);

			List<Object[]> listFF = queryFF.getResultList();
			if (!listFF.isEmpty()) {
				Object stringFF[] = listFF.get(0);
				X4326 = stringFF[0].toString();
				Y4326 = stringFF[1].toString();
			}

			pointsNew[sequencia] = new Coordinate(Double.valueOf(X4326).doubleValue(),
					Double.valueOf(Y4326).doubleValue());

			pointsSirgasNew[sequencia] = new Coordinate(verticeobra_.getPointri().getGeometry().getCoordinate().x,
					verticeobra_.getPointri().getGeometry().getCoordinate().y, verticeobra_.getPointri().getAltura());

			if (sequencia == 0) {
				verticeFirst = verticeobra_.getPointri();
				// =============== FIRST DATA FROM VETEX ===================
				FirstPointGeodetic = new Coordinate(((-1) * Double.valueOf(X4326).doubleValue()),
						Double.valueOf(Y4326).doubleValue());

				longitudeFirst1X = Double.valueOf(X4326).doubleValue();
				latitudeFirst1X = Double.valueOf(Y4326).doubleValue();
				altitudeFirstMeters1X = verticeobra_.getPointri().getAltura();
				// =============== FIRST DATA =============================
			}

			// Vamos determinar o atual
			longitude1X = Double.valueOf(X4326).doubleValue();
			latitude1X = Double.valueOf(Y4326).doubleValue();
			altitudeMeters1X = verticeobra_.getPointri().getAltura();
			// Fim de Atual

			if (sequencia + 1 < numeroVertices) {
				verticeobraTo = (PolygonPoint) iTo.next();
				verticeobra_.setPointTo(verticeobraTo.getPointri());

				// -------------------------------- NEW POSTGIS NEXT CONVERTER
				// --------------------------------------------
				utmPointNext = "POINT(" + verticeobraTo.getPointri().getGeometry().getCoordinate().x + " "
						+ verticeobraTo.getPointri().getGeometry().getCoordinate().y + ")";

				String consultaNext = "SELECT" + " public.ST_X(public.ST_Transform(public.ST_GeomFromText('"
						+ utmPointNext + "'," + srid_ + "),4326)) As X4326,"
						+ " public.ST_Y(public.ST_Transform(public.ST_GeomFromText('" + utmPointNext + "'," + srid_
						+ "),4326)) As Y4326 ";

				Query queryFFNext = session.createNativeQuery(consultaNext);

				List<Object[]> listFFNext = queryFFNext.getResultList();
				if (!listFFNext.isEmpty()) {
					Object stringFFNext[] = listFFNext.get(0);
					X4326Next = stringFFNext[0].toString();
					Y4326Next = stringFFNext[1].toString();
				}

				longitude2X = Double.valueOf(X4326Next).doubleValue();
				latitude2X = Double.valueOf(Y4326Next).doubleValue();
				altitudeMeters2X = verticeobra_.getPointri().getAltura();
				// --------------------------------------------------------------------

				String geH = "LINESTRING(" + pointsSirgasNew[sequencia].x + " " + pointsSirgasNew[sequencia].y + ","
						+ verticeobraTo.getPointri().getGeometry().getCoordinate().x + " "
						+ verticeobraTo.getPointri().getGeometry().getCoordinate().y + ")";
				// String geH= "LINESTRING(" + pointsNew[sequencia].x + " " +
				// pointsNew[sequencia].y + ","
				// + X4326Next + " " + Y4326Next + ")";

				WKTReader fromText = new WKTReader();

				org.locationtech.jts.geom.Geometry geom55 = null;

				try {
					geom55 = fromText.read(geH);
				} catch (org.locationtech.jts.io.ParseException e) {
					e.printStackTrace();
				}

				geom55.setSRID(Integer.valueOf(obraCAP.getLayer().getEpsg()));

				verticeobra_.setGeometry((LineString) geom55);

			} else {
				verticeobra_.setPointTo(verticeFirst);
				longitude2X = longitudeFirst1X;
				latitude2X = latitudeFirst1X;
				altitudeMeters2X = altitudeFirstMeters1X;

				// String geH2= "LINESTRING(" + pointsNew[sequencia].x + " " +
				// pointsNew[sequencia].y + ","
				// + longitude2X + " " + latitude2X + ")";

				String geH2 = "LINESTRING(" + pointsSirgasNew[sequencia].x + " " + pointsSirgasNew[sequencia].y + ","
						+ pointsSirgasNew[0].x + " " + pointsSirgasNew[0].y + ")";

				WKTReader fromText = new WKTReader();

				org.locationtech.jts.geom.Geometry geom56 = null;

				try {
					geom56 = fromText.read(geH2);
				} catch (org.locationtech.jts.io.ParseException e) {
					e.printStackTrace();
				}

				geom56.setSRID(Integer.valueOf(obraCAP.getLayer().getEpsg()));

				verticeobra_.setGeometry((LineString) geom56);

				//
			}

			double semiEixoMaior = 6378137.00000; // Semi-eixo maior do elipsÃ³ide de referÃªncia
			double semiEixoMenor = 6356752.31414; // Semi-eixo menor do elipsÃ³ide de referÃªncia
			double f = 1 / 298.257223563;

			Coordinate pointsNew2 = new Coordinate(((-1) * Double.valueOf(X4326).doubleValue()),
					Double.valueOf(Y4326).doubleValue());

			Coordinate ptl = processAreaCartesian2.PTL(FirstPointGeodetic, pointsNew2, semiEixoMaior, semiEixoMenor, f,
					mediaAltura);


			// " Z-> " + cartesian1.getZ());
			// " Z-> " + cartesian2.getZ());

			double a1Minus2H = Math.pow((altitudeMeters1X - altitudeMeters2X), 2);



			verticeobra_.setLeste(ptl.x);
			verticeobra_.setNorte(ptl.y);

			// ---------------------------------------------------------------------

			String consultaDescritivo = "SELECT DISTINCT  "

					+ " pointri_id, polygonri_id, point_next, sequencia, tipolimite, geometry, distancia, azimuth, jusantemontante, "
					+ " esqDir, leste, norte, descritivo, cns, matricula, "
					+ "anchorpointx, anchorpointy, displacementx, displacementy, rotation, "
					+ "polygon_confrontante_id, point_id_to, status, angle, marco_label, print_conf, print_dist, print_marco"
					+ " FROM app_polygonripointri  vo where " + " vo.polygonri_id in "
					+ "(select distinct geomet.id from app_geometry geomet, "
					+ "app_component  comp  where geomet.situacao > 0 and geomet.layer_id = comp.id "
					+ "AND  comp.editable = true AND comp.application_id = '" + this.applicationEntity.getId() + "') "
					+ " AND vo.pointri_id = '" + verticeobra_.getPointTo().getId() + "'" + " AND vo.point_id_to = '"
					+ verticeobra_.getPointri().getId() + "' limit (1.1)";


			try {

				Query queryNativaDescritivo = session.createNativeQuery(consultaDescritivo, PolygonPoint.class);

				PolygonPoint verticeobradescritivo = ((PolygonPoint) queryNativaDescritivo.getSingleResult());

				ObjectNode node = null;
				String prefixo = null;
				try {
					node = new ObjectMapper().readValue(verticeobradescritivo.getPolygonri().getStrings().toString(),
							ObjectNode.class);
					prefixo = node.get("Tipo").textValue();
				} catch (IOException e) {

				} catch (NullPointerException np) {

				}

				// verticeobradescritivo.getPolygonri().getLayer().getId());
				if (prefixo == null) {

					Layer LayerMaior = session.find(Layer.class,
							verticeobradescritivo.getPolygonri().getLayer().getId());

					prefixo = LayerMaior.getName();

				} else {
				}

				String confrontante = prefixo + " " + verticeobradescritivo.getPolygonri().getNome();
				verticeobra_.setDescritivo(confrontante.toUpperCase());
			} catch (NoResultException e) {

			}

			// ===============================

			// ========================================

			session.merge(verticeobra_);
			// entityManager.flush();

			sequencia++;
		}
	}

	// Inicio criacao das LinesStrings

	public void createLineString(Polygon obraCLS, double mediaAltura, EntityManager session) {

		double doubleLeste;
		double doubleNorte;

		org.postgis.Point FirstPoint;
		org.postgis.Point FirstPointFirst;
		org.postgis.Point NextPoint;

		Coordinate FirstPointGeodetic;
		Coordinate FirstPointFirstGeodetic;
		Coordinate NextPointGeodetic;

		Coordinate FirstdoubleGeodetic;

		String verticeFirstFirst;
		Point verticeToFirstFirst;
		String verticeNext;
		Point verticeTo;
		Point utmPoint;

		List<PolygonPoint> verticeobraEspecial = obraCLS.getVerticeObrasList();

		Iterator i = verticeobraEspecial.iterator();
		Iterator i2 = verticeobraEspecial.iterator();
		PolygonPoint verticeobraNext = (PolygonPoint) i2.next();

		// =============== FIRST POINT COORDINATES UTM AND GEODETIC ======
		doubleLeste = verticeobraNext.getPointri().getGeometry().getCoordinate().x;
		doubleNorte = verticeobraNext.getPointri().getGeometry().getCoordinate().y;
		FirstPointFirstGeodetic = verticeobraNext.getPointri().getGeometry().getCoordinate();

		// =============== FIRST POINT NAMES =============================

		FirstPointFirst = new org.postgis.Point(doubleLeste, doubleNorte);
		verticeFirstFirst = verticeobraNext.getPointri().getNome();
		verticeToFirstFirst = verticeobraNext.getPointri();

		Integer contador = 0;
		while (i.hasNext()) {

			doubleLeste = verticeobraNext.getPointri().getGeometry().getCoordinate().x;

			doubleNorte = verticeobraNext.getPointri().getGeometry().getCoordinate().y;

			PolygonPoint verticeobra_ = (PolygonPoint) i.next();

			if (i2.hasNext()) {
				verticeobraNext = (PolygonPoint) i2.next();
				// ==============================================================================
				doubleLeste = verticeobraNext.getPointri().getGeometry().getCoordinate().x;
				doubleNorte = verticeobraNext.getPointri().getGeometry().getCoordinate().y;
				NextPoint = new org.postgis.Point(doubleLeste, doubleNorte);
				NextPointGeodetic = verticeobraNext.getPointri().getGeometry().getCoordinate();
				verticeNext = verticeobraNext.getPointri().getNome();
				verticeTo = verticeobraNext.getPointri();
			} else {
				NextPoint = FirstPointFirst;
				NextPointGeodetic = FirstPointFirstGeodetic;
				verticeNext = verticeFirstFirst;
				verticeTo = verticeToFirstFirst;
			}

			// Aqui o processamento
			doubleLeste = verticeobra_.getPointri().getGeometry().getCoordinate().x;
			doubleNorte = verticeobra_.getPointri().getGeometry().getCoordinate().y;

			FirstPoint = new org.postgis.Point(doubleLeste, doubleNorte);
			FirstPointGeodetic = verticeobra_.getPointri().getGeometry().getCoordinate();
			Integer srid_ = 31980;

			FirstPoint.setSrid(srid_);
			NextPoint.setSrid(srid_);

			Coordinate primeiroPonto = new Coordinate(verticeobra_.getPointri().getGeometry().getCoordinate().x,
					verticeobra_.getPointri().getGeometry().getCoordinate().y);
			Coordinate segundoPonto = new Coordinate(verticeobraNext.getPointri().getGeometry().getCoordinate().x,
					verticeobraNext.getPointri().getGeometry().getCoordinate().y);
			Geometry firstPointX = gf.createPoint(primeiroPonto);
			Geometry secondPointX = gf.createPoint(segundoPonto);
			DistanceOp distanceOp = new DistanceOp(firstPointX, secondPointX);

			ProcessAzimuth processAzimuth = new ProcessAzimuth();

			double azimuth = processAzimuth.Azimuth(FirstPointGeodetic, NextPointGeodetic);


			Query queryNativa = session.createNativeQuery(
					"SELECT public.st_distance('" + FirstPoint + "" + "','" + NextPoint + "') as distance,"
							+ "degrees(public.st_azimuth1('" + FirstPoint + "','" + NextPoint + "')) as azimute," +

							" public.ST_X(public.ST_Transform(public.ST_GeomFromText('" + FirstPoint + "'," + srid_
							+ "),4326)) As firstX," + " public.ST_Y(public.ST_Transform(public.ST_GeomFromText('"
							+ FirstPoint + "'," + srid_ + "),4326)) As firstY," +

							" public.ST_X(public.ST_Transform(public.ST_GeomFromText('" + NextPoint + "'," + srid_
							+ "),4326)) As nextX," + " public.ST_Y(public.ST_Transform(public.ST_GeomFromText('"
							+ NextPoint + "'," + srid_ + "),4326)) As nextY ");

			Object saida[] = (Object[]) queryNativa.getSingleResult();
			String dist = saida[0].toString();
			double distancia = Double.valueOf(dist).doubleValue();
			double distanciaInt33 = Math.floor(distancia);
			// + distanciaInt33);
			int distanciaInt = (int) distanciaInt33;
			// distanciaInt);
			String inteiro = String.valueOf(distanciaInt);
			String distString = null;
			if (distanciaInt33 == distancia) {
				distString = dist + "0";
			} else {
				distString = dist.substring(0, inteiro.length() + 3);
			}


			String azim = saida[1].toString();
			double azimute = Double.valueOf(azim).doubleValue();
			String firstX_ = saida[2].toString();
			double firstXD = Double.valueOf(firstX_).doubleValue();
			String firstY_ = saida[3].toString();
			double firstYD = Double.valueOf(firstY_).doubleValue();
			String nextX_ = saida[4].toString();
			double nextXD = Double.valueOf(nextX_).doubleValue();
			String nextY_ = saida[5].toString();
			double nextYD = Double.valueOf(nextY_).doubleValue();
			org.postgis.Point point0 = new org.postgis.Point(firstXD, firstYD);
			org.postgis.Point point1 = new org.postgis.Point(nextXD, nextYD);
			List points2 = new ArrayList();
			points2.add(point0);
			points2.add(point1);
			org.postgis.Geometry lineString = new org.postgis.LineString(
					(org.postgis.Point[]) points2.toArray(new org.postgis.Point[points2.size()]));

			lineString.srid = 4326;
			WKTReader fromText = new WKTReader();
			org.locationtech.jts.geom.Geometry geom = null;

			String teste = "LINESTRING" + lineString.getValue();
			try {
				geom = fromText.read(teste);
			} catch (org.locationtech.jts.io.ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			geom.setSRID(4326);
			Long vertice_id_update = verticeobra_.getId().getPointriId();
			String testeRR;

			Integer azimuthInteger = 10; // (int) azimuth;




			verticeobra_.setDistancia(distString);

			verticeobra_.setAzimuth(decToDMS(azimute));

			verticeobra_.setAngle(azimute);

			verticeobra_.setPointNext(verticeNext);

			verticeobra_.setPointTo(verticeTo);


			session.merge(verticeobra_);

		}

	}

	public String decToDMS(double coord) {

		double doubleDegres = Math.floor(coord); // 33

		String degrees = String.valueOf((int) doubleDegres); // 33
		double doubleDecimals = coord - doubleDegres; // 0.3333

		double doubleMinutes = doubleDecimals * 60; // 19.998
		double absDoubleMinutes = Math.floor(doubleMinutes); // 19 double
		String Minutes = String.valueOf((int) absDoubleMinutes); // 19 string
		doubleDecimals = doubleMinutes - absDoubleMinutes; // 0.998

		double doubleSeconds = doubleDecimals * 60; // 59.88
		double absDoubleSeconds = Math.floor(doubleSeconds); // 59 double
		String Seconds = String.valueOf((int) absDoubleSeconds); // 19 string

		return degrees + "º" + Minutes + "'" + Seconds + "\"";
	}

	public Integer getEpsgNew() {
		return epsgNew;
	}

	public void setEpsgNew(Integer epsgNew) {
		this.epsgNew = epsgNew;
	}

	// ==========================================================

	Point marcoEntity;

	public Point getMarcoEntity() {

		try {

			Long verticeId = verticeobraId.getPointriId();
			Point captureMarco = (Point) entityManager
					.createQuery("select vertice from Point vertice where vertice.id = :idVertice")
					.setParameter("idVertice", verticeId).getSingleResult();


			return captureMarco;
		} catch (Exception e) {
		}
		return marcoEntity;

	}

	private Long selectedVerticeId;

	public Long getSelectedVerticeId() {
		return selectedVerticeId;
	}

	public void setSelectedVerticeId(Long selectedVerticeId) {
		this.selectedVerticeId = selectedVerticeId;
	}

	public void setMarcoEntity(Point marcoEntity) {

		

		this.selectedVerticeId = marcoEntity.getId();
		this.marcoEntity = marcoEntity;
	}

	public List autocompleteMarco(Object o) {
		return procurarVertices(o.toString());
	}

	public List<Point> completeThemeV(String query) {

		try {


			Integer tamanhoChave = query.length();
			if (tamanhoChave > 2) {

				String Query2 = "select vertice from Point vertice where lower(vertice.nome) like lower('%" + query
						+ "%') AND vertice.id not in (select verticeobra.pointri.id from PolygonPoint verticeobra where verticeobra.polygonri.id = '"
						+ this.obra.getId() + "') order by vertice.nome";


				// Query front = entityManager
				// .createQuery(Query2);

				List<Point> frontList = entityManager.createQuery(Query2, Point.class).setMaxResults(10)
						.getResultList();


				// List frontList = front.getResultList();


				return frontList;
			} else {
				return null;
			}

		} catch (Exception e) {
		e.printStackTrace();
		}
		return null;

	}

	public List procurarVertices(String nome) {

		try {

			try {
				String Query = "select vertice from Point vertice where lower(vertice.nome) like lower('" + nome
						+ "%')) order by vertice.id";
				ArrayList vertices = new ArrayList<Point>(entityManager.createQuery(Query).getResultList());
				return vertices;
			} catch (Exception e) {
				return null;
			}

		} catch (Exception e) {
		}
		return null;
	}

	public List<Point> getVertices() {

		try {

			String Query = "select vertice from Point vertice order by vertice.id";
			List<Point> vertice = new ArrayList<Point>(entityManager.createQuery(Query).getResultList());
			return vertice;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public void newEditedObra() {
		this.editedVerticeObra = new PolygonPoint();
	}

	/**
	 * <p/>
	 * This method disconnect the vertex from the edited parcel from the verticeobra
	 * and refresh the "order list" because new sequence are defined Since: Version
	 * 1.1.0
	 */
	@Transactional
	public void deleteVO() {

		try {

			PolygonPoint frontLO = entityManager.find(PolygonPoint.class, editedVerticeObra.getId());
			entityManager.remove(frontLO);
			entityManager.flush();

			this.obra.removeVerticeObra(frontLO);
			entityManager.flush();


		} catch (Exception e) {
			e.printStackTrace();
		}

		// -----------------------------------------------------------------


		try {

			Polygon PolygonSeq = entityManager.find(Polygon.class, this.obra.getId());

			Integer novaSequencia = 1;
			List<PolygonPoint> frontListUVO = PolygonSeq.getVerticeObrasList();
			Iterator<PolygonPoint> iterUVO = frontListUVO.iterator();
			while (iterUVO.hasNext()) {

				PolygonPoint verticeobra_ = iterUVO.next();
				verticeobra_.setSequencia(novaSequencia);
	
				entityManager.merge(verticeobra_);

				novaSequencia++;
			}
			entityManager.flush();

			entityManager.refresh(PolygonSeq);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			this.obra = entityManager.find(Polygon.class, this.obra.getId());



			try {
				if (this.obra.getFather() != null) {
					this.setAbstractGeometry(this.obra.getFather());
				}
			} catch (NullPointerException np) {
				this.setAbstractGeometry(this.obra);
			}


			try {
				if (this.obra.getFather() == null) {
					this.setAbstractGeometry(this.obra);
				}
			} catch (NullPointerException np) {

			}


			List<Field> fieldsProperties = null;
			try {
				if (this.getAbstractGeometry().getLayer().getFields() != null) {
					fieldsProperties = this.getAbstractGeometry().getLayer().getFields();
				}
			} catch (NullPointerException np) {
				np.printStackTrace();
			}
			model = new DynaFormModel();

			// this.getAbstractGeometry().getNome());
			DynaFormRow row = model.createRegularRow();

			try {

				List<LayerCategory> layerCat = this.getAbstractGeometry().getCategories();
				Long idCategory = null;
				for (LayerCategory layerC : layerCat) {
					idCategory = layerC.getId();
				}
				Integer numberCat = this.getAbstractGeometry().getLayer().getLayerCategories().size();
				if (numberCat > 0) {
					DynaFormLabel label410 = row.addLabel("Categoria");
					DynaFormControl control420 = row.addControl(new BookProperty("Categoria", idCategory, false),
							"selectcat");
					label410.setForControl(control420);
				}
			} catch (NullPointerException np) {
				np.printStackTrace();
			}

			try {
				if (this.getAbstractGeometry().getLayer().getLayerCat() != null) {

					if (this.getAbstractGeometry().getFather().getId() != null) {

						Long idFather = this.getAbstractGeometry().getFather().getId();
						DynaFormLabel label4103 = row.addLabel("Father");
						DynaFormControl control4203 = row
								.addControl(new BookProperty("Father", idFather, false, null, null), "selectfather");
						label4103.setForControl(control4203);

					}

				}
			} catch (NullPointerException np) {
				DynaFormLabel label4103 = row.addLabel("Father");
				DynaFormControl control4203 = row.addControl(new BookProperty("Father", "", false), "selectfather");
				label4103.setForControl(control4203);

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
			if (fieldsProperties != null) {
				// CREATE FIELD DYNAMICS AND POPULATE THIS
				int stringKeyIndex = 0;
				Object[] objectValue = new Object[fieldsProperties.size()];
				List<SelectItem> selectItemsBD = null;
				for (Field columnKey : fieldsProperties) {
					switch (columnKey.getTypeData()) {

					case "string":


						try {
							objectValue[stringKeyIndex] = node.get(columnKey.getName()).textValue();
						} catch (NullPointerException np) {
							objectValue[stringKeyIndex] = "";
						}
						break;

					case "number":
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
					row = rowcreate(row, columnKey.getFieldControl().getType(), columnKey.getLabel(),
							columnKey.getName(), objectValue[stringKeyIndex], columnKey.getPlaceHolder(),
							columnKey.getIcon(), columnKey.getRequired(), selectItemsBD,
							columnKey.getFieldControl().getKeys());
					stringKeyIndex++;
				}
			}

			List<Polygon> oNewList = new ArrayList<Polygon>();

			try {
				if (this.getAbstractGeometry() != null) {
//FIXME Este estava dando erro na API REST POST
					// Polygon obra5 = new Polygon();

					// obra5.setLayer(this.getObra().getFather().getLayer());
					// obra5.setLayerid(this.getObra().getFather().getLayer().getName());
					// oNewList.add(obra5);

				}

			} catch (NullPointerException npe) {

			}
			// try {
			// List<AbstractGeometry> oOldList = new ArrayList<AbstractGeometry>(
			// this.getObra().getFather().getChildrens());
			// for (int i = 0; i < oOldList.size(); i++) {
			// for (int i2 = 0; i2 < oOldList.size(); i2++) {
			// if (Integer.valueOf(oOldList.get(i2).getParte()) == (i + 1)) {

			// Polygon pol33 = session.find(Polygon.class, oOldList.get(i2).getId());

			// oNewList.add(pol33);
			// }
			// }
			// }

			// } catch (NullPointerException npe) {

			// }

			// Polygon pol33 = session.find(Polygon.class, oOldList.get(i2).getId());

			oNewList.add(this.getObra());

			this.obrasTab = oNewList;

		} catch (Exception ex) {
		}
	}

	// @Inject
	// EmailService emailService;

	public ClientOAuthEntity findClientOAuthManager(String realmName) {
		try {
			TypedQuery<ClientOAuthEntity> query = entityManager
					.createNamedQuery(ClientOAuthEntity.OAUTH_CLIENT_ID_REALM, ClientOAuthEntity.class);
			query.setParameter("clientId", "admin-cli");
			query.setParameter("realmId", realmName);
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// ----------------------------------------------- UPLOAD IMAGE

	@Transactional
	public void importIMAGE(FileUploadEvent event) throws Exception {

		try {

			FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			String fileNameOnly = "";
			try {

				Random random = new Random();
				int randomInt = random.nextInt(1000000000);

				LocalDateTime data = LocalDateTime.now();

				String fileName = ConvertToASCII2.convert(event.getFile().getFileName()).toLowerCase();
				fileNameOnly = data.toString() + "-" + String.valueOf(randomInt) + "_" + fileName;
				String appDirectory = save_FILE_PATH + "/files/" + userData.getRealmEntity() + "/media/";
				File appDirImg = new File(appDirectory);
				if (!appDirImg.exists()) {
					appDirImg.mkdir();
				}
				String fileNameNew = appDirectory + "/" + fileNameOnly;
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
			}

			Media mediaNew = new Media();
			mediaNew.setFilename("/files/" + userData.getRealmEntity() + "/media/");
			mediaNew.setTitle(fileNameOnly);

			String filename = save_FILE_PATH + mediaNew.getFilename() + fileNameOnly;

			TikaConfig tikaConfig = TikaConfig.getDefaultConfig();

			Metadata metadata = new Metadata();
			String text = null;
			try {
				text = parseUsingComponents(filename, tikaConfig, metadata);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String contentType = metadata.get("Content-Type");
			mediaNew.setMimeType(contentType);

			mediaNew.setReleaseDate(LocalDateTime.now());

			mediaNew.setAbstractGeometry(abstractGeometry);

			entityManager.persist(mediaNew);

			this.abstractGeometry.getMedias().add(mediaNew);
			entityManager.merge(this.abstractGeometry);

			entityManager.flush();

		} catch (Exception e) {

		}

	}

	public static String parseUsingAutoDetect(String filename, TikaConfig tikaConfig, Metadata metadata)
			throws Exception {

		AutoDetectParser parser = new AutoDetectParser(tikaConfig);
		ContentHandler handler = new BodyContentHandler();
		TikaInputStream stream = TikaInputStream.get(new File(filename), metadata);
		parser.parse(stream, handler, metadata, new ParseContext());
		return handler.toString();
	}

	public static String parseUsingComponents(String filename, TikaConfig tikaConfig, Metadata metadata)
			throws Exception {
		MimeTypes mimeRegistry = tikaConfig.getMimeRepository();

		metadata.set(TikaCoreProperties.EMBEDDED_RESOURCE_TYPE_KEY, filename);
		InputStream stream = TikaInputStream.get(new File(filename));

		stream = TikaInputStream.get(new File(filename));
		Detector detector = tikaConfig.getDetector();

		Parser parser = tikaConfig.getParser();

		MediaType type = detector.detect(stream, metadata);
		metadata.set(Metadata.CONTENT_TYPE, type.toString());

		ContentHandler handler = new BodyContentHandler();
		parser.parse(stream, handler, metadata, new ParseContext());

		return handler.toString();
	}

	// --------------------------------------------------------------
	// ===============================

	public Layer getLayerNew(LayerType type, String name) {
		int lastOrder = (int) this.applicationEntity.getLayers().stream().
				filter(f-> f.getFather() == null).count() + 1;
		
		switch (type) {
		case POINT:
			
			return new LayerPoint(name, this.applicationEntity, "Example Layer Point", JacksonUtil.toJsonNode("{}"),
					lastOrder, this.applicationEntity.getEpsg(),
					this.applicationEntity.getZoom(), this.applicationEntity.getMaxZoom(),
					this.applicationEntity.getMinZoom(), this.applicationEntity.getMaxres(),
					this.applicationEntity.getMinres());
		case POLYGON:
			GeometryStyle geomStylePol = new GeometryStyle("rgba(245,2,62,0.45)", "#00e600", 2, 0, 0, 15);

			return new LayerPolygon(name, this.applicationEntity, "Example Layer Point", JacksonUtil.toJsonNode("{}"),
					lastOrder, this.applicationEntity.getEpsg(),
					this.applicationEntity.getZoom(), this.applicationEntity.getMaxZoom(),
					this.applicationEntity.getMinZoom(), this.applicationEntity.getMaxres(),
					this.applicationEntity.getMinres(), geomStylePol, new LabelStyle());

		case LINESTRING:

			GeometryStyle geomStyleLin = new GeometryStyle("rgba(245,2,62,0.45)", "#00e600", 2, 0, 0, 15);
			return new LayerLinestring(name, this.applicationEntity, "Example Layer Point",
					JacksonUtil.toJsonNode("{}"), lastOrder,
					this.applicationEntity.getEpsg(), this.applicationEntity.getZoom(),
					this.applicationEntity.getMaxZoom(), this.applicationEntity.getMinZoom(),
					this.applicationEntity.getMaxres(), this.applicationEntity.getMinres(), geomStyleLin,
					new LabelStyle());

		case MULTIPOLYGON:
			return new LayerMultiPolygon(name, this.applicationEntity);

		case TILE:
			return new LayerTile(name, this.applicationEntity, "Example Layer Tile", JacksonUtil.toJsonNode("{}"),
					lastOrder, this.applicationEntity.getEpsg(),
					this.applicationEntity.getZoom(), this.applicationEntity.getMaxZoom(),
					this.applicationEntity.getMinZoom(), this.applicationEntity.getMaxres(),
					this.applicationEntity.getMinres());

		case GROUP:
			return new LayerGroup(name, this.applicationEntity, "Example Layer Tile", JacksonUtil.toJsonNode("{}"),
					lastOrder, this.applicationEntity.getEpsg(),
					this.applicationEntity.getZoom(), this.applicationEntity.getMaxZoom(),
					this.applicationEntity.getMinZoom(), this.applicationEntity.getMaxres(),
					this.applicationEntity.getMinres());

		case RASTERGEOSERVER:
			return new LayerRasterGeoserver(name, this.applicationEntity, lastOrder);

		case WMSGEOSERVER:
			return new LayerWMSGeoserver(name, this.applicationEntity, lastOrder);

		default:
			throw new IllegalArgumentException("LayerType not supported.");
		}
	}

	@Transactional
	public void layerSelectListener() {
		

		try {
			this.layerTemp = this.getLayerNew(this.layerWizard.getLayerTypeChose(), this.layerWizard.getLayerName());
			entityManager.persist(this.layerTemp);
			entityManager.flush();

			this.getApplicationEntity().addLayerItem(this.layerTemp);

			LayerView layerView = new LayerView();
			layerView.setLayerid(this.layerTemp.getId().toString());
			layerView.setVisible(this.layerTemp.getSelected());
			layerView.setOpacity(this.layerTemp.getOpacity());
			layerView.setZIndex(this.layerTemp.getOrderlayer());

			layerView.setLayerhash(this.layerTemp.layerString());

			layerView.setName(this.layerTemp.getName());

			this.appLayers.add(layerView);

			PrimeFaces.current().ajax().addCallbackParam("zindex", this.layerTemp.getOrderlayer());
			
			PrimeFaces.current().ajax().addCallbackParam("labelstylejson", this.layerTemp.getLabelStyle());
			PrimeFaces.current().ajax().addCallbackParam("geomstylejson", this.layerTemp.getGeometryStyle());

			PrimeFaces.current().ajax().addCallbackParam("stringslayer", this.layerTemp.getStrings().toPrettyString());
			
			PrimeFaces.current().ajax().addCallbackParam("namelayer", this.layerTemp.getId().toString());
			PrimeFaces.current().ajax().addCallbackParam("typelayer", this.layerTemp.getDtype());

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Camada Adicionada!"));

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro!", "Camada Não Adicionada!"));
		}

	}

	// ================================
	
	@Transactional
	public void layerSelectListenerXYZ() {


		try {

			this.layerTemp = this.getLayerNew(this.layerWizard.getLayerTypeChose(), this.layerWizard.getLayerName());

		

			String maptilerXYZ = "{\n"
					+ "  \"type\": \"Maptiler\",\n"
					+ "  \"apiKey\": \"t2zX1AkgvdqLsGQb7ON7\",\n"
					+ "  \"urlXYZ\": \"https://api.maptiler.com/maps/satellite/{z}/{x}/{y}.jpg?key=\",\n"
					+ "  \"imagerySet\": \"satellite\"\n"
					+ "}";

			this.layerTemp.setStrings(JacksonUtil.toJsonNode(maptilerXYZ));

			entityManager.persist(this.layerTemp);
			entityManager.flush();
			
		//	Integer orderlay = this.layerTemp.getApplicationEntity().getComponents().size();
			
			this.getApplicationEntity().addLayerItem(this.layerTemp);

			this.layerWizard.setId(this.layerTemp.getId());
			// -------------------------------------------

			LayerView layerView = new LayerView();
			layerView.setLayerid(this.layerWizard.getId().toString());
			layerView.setVisible(this.layerWizard.getVisible());
			layerView.setOpacity(this.layerWizard.getOpacity());
			layerView.setZIndex(this.layerTemp.getOrderlayer());

			layerView.setLayerhash(this.layerWizard.layerWizardString());

			layerView.setName(this.layerWizard.getLayerName());

			this.appLayers.add(layerView);
			
			PrimeFaces.current().ajax().addCallbackParam("zindex", this.layerTemp.getOrderlayer());

			PrimeFaces.current().ajax().addCallbackParam("stringslayer", this.layerTemp.getStrings().toPrettyString());
			PrimeFaces.current().ajax().addCallbackParam("namelayer", this.layerWizard.layerWizardString());
			PrimeFaces.current().ajax().addCallbackParam("typelayer",
					layerWizard.getLayerTypeChose().toString().toLowerCase());

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Camada Adicionada!"));

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro!", "Camada Não Adicionada!"));
		}

	}
	// --------------------------------------------------------------

	// ================================

	@Transactional
	public void layerSelectListenerWMS() {


		try {

			//
			TypedQuery<ClientComponentEntity> queryApp = entityManager
					.createNamedQuery(ClientComponentEntity.COMPONENT_CLIENT_ID, ClientComponentEntity.class);
			queryApp.setParameter("clientId", "geoserver");
			List<ClientComponentEntity> clientCompEntity = queryApp.getResultList();

			ClientComponentEntity clientGeoserver = clientCompEntity.get(0);

			switch (this.layerWizard.getLayerTypeChose()) {

			case RASTERGEOSERVER:

				try {
					CreateProjectWorkspace.createWorkspace(tenant, clientGeoserver);
				} catch (Exception e2) {
					// e2.printStackTrace();
				}

				try {
					CreateProjectWorkspace.createDataStoreGeoTIFF(this.layerWizard.layerWizardString(), tenant,
							this.layerWizard.getUrlLayer(), clientGeoserver);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				try {
					CreateProjectWorkspace.createLayerGeoTIFF(this.layerWizard.layerWizardString(), clientGeoserver,
							tenant);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					// e2.printStackTrace();
				}

				break;

			case WMSGEOSERVER:

				try {
					CreateProjectWorkspace.createWorkspace(tenant, clientGeoserver);
				} catch (Exception e2) {
					// e2.printStackTrace();
				}

				try {
					CreateProjectWorkspace.createDataStoreSHP(this.layerWizard, tenant, this.getInputFile(),
							clientGeoserver);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				try {

					CreateProjectWorkspace.createLayerSHP(this.layerWizard.storeLayerWizardString(),
							this.layerWizard.getDataSet(), this.layerWizard.layerWizardString(), tenant,
							clientGeoserver);

				} catch (Exception e2) {
					e2.printStackTrace();
				}

				String sldName = layerWizard.layerWizardString();
				String sldFileName = layerWizard.layerWizardString() + ".sld";

			
				
				try {
					CreateProjectWorkspace.createStyleSLD(sldName, sldFileName, tenant, clientGeoserver);
				} catch (Exception e2) {
					e2.printStackTrace();
				}

				testeFile(layerWizard.layerWizardString(), tenant);

				try {
					CreateProjectWorkspace.updateLayerSLD(layerWizard.layerWizardString(),
							layerWizard.layerWizardString(), tenant, clientGeoserver);

				} catch (Exception e2) {
					// TODO Auto-generated catch block
					// e2.printStackTrace();
				}

				break;

			default:
				break;

			}

			// -------------------------------------------

			CompAttributeEntity compAttributeEntity = new CompAttributeEntity();
			compAttributeEntity.setName("DATASET");
			compAttributeEntity.setComponent(this.layerTemp);
			compAttributeEntity.setDtype("label");
			compAttributeEntity.setValue(this.layerWizard.getDataSet());
			entityManager.persist(compAttributeEntity);
			entityManager.flush();

			this.getApplicationEntity().addLayerItem(this.layerTemp);

			LayerView layerView = new LayerView();
			layerView.setLayerid(this.layerWizard.getId().toString());
			layerView.setVisible(this.layerWizard.getVisible());
			layerView.setOpacity(this.layerWizard.getOpacity());
			layerView.setZIndex(this.layerTemp.getOrderlayer());

			layerView.setLayerhash(this.layerWizard.layerWizardString());

			layerView.setName(this.layerWizard.getLayerName());

			this.appLayers.add(layerView);

			// --------------------------------------------------

			// SHIPEFILE GET ATTIBUTES

			String fileName = this.layerWizard.getUrlLayer() + "/" + this.layerWizard.getDataSet() + ".shp";
			File file = new File(fileName);

			try {
				// 2. Obtain a FileDataStore to handle the shapefile
				FileDataStore store = FileDataStoreFinder.getDataStore(file);
				SimpleFeatureSource featureSource = store.getFeatureSource();

				// 3. Get the schema (structure) to access field names
				SimpleFeatureType schema = featureSource.getSchema();

				Integer seq = -1;
				for (AttributeDescriptor att : schema.getAttributeDescriptors()) {
					seq++;
					Field fieldNew = new Field();
					fieldNew.setSequence(seq);
					fieldNew.setLayer(this.layerTemp);
					fieldNew.setName(att.getName().toString());
					fieldNew.setLabel(att.getName().toString());
					fieldNew.setFieldControl(null);
					fieldNew.setPlaceHolder(null);
					fieldNew.setTypeData(att.getType().getName().toString());
					entityManager.persist(fieldNew);
				}
				entityManager.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}

			// ---------------------------------------------------

			PrimeFaces.current().ajax().addCallbackParam("stringslayer", this.layerTemp.getStrings().toPrettyString());
			PrimeFaces.current().ajax().addCallbackParam("namelayer", this.layerWizard.layerWizardString());
			
			PrimeFaces.current().ajax().addCallbackParam("zindex", this.layerWizard.getzIndex());
			
			
			PrimeFaces.current().ajax().addCallbackParam("typelayer",
					layerWizard.getLayerTypeChose().toString().toLowerCase());

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso33!", "Camada Adicionada!"));

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro!", "Camada Não Adicionada!"));
		}

	}
	// --------------------------------------------------------------

	public void testeFile(String fileNameSLD, String workspaceSLD) {

		String content = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
				+ "<StyledLayerDescriptor version=\"1.0.0\"\n"
				+ "  xsi:schemaLocation=\"http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd\"\n"
				+ "  xmlns=\"http://www.opengis.net/sld\" xmlns:ogc=\"http://www.opengis.net/ogc\"\n"
				+ "  xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n"
				+ "\n" + "  <NamedLayer>\n" + "    <Name>pol</Name>\n" + "    <UserStyle>\n"
				+ "      <Title>A gold polygon style</Title>\n" + "      <FeatureTypeStyle>\n" + "        <Rule>\n"
				+ "          <Title>gold polygon</Title>\n" + "          <PolygonSymbolizer>\n" + "      \n"
				+ "            <Stroke>\n" + "              <CssParameter name=\"stroke\">#FF0000</CssParameter>\n"
				+ "              <CssParameter name=\"stroke-width\">1.0</CssParameter>\n" + "            </Stroke>\n"
				+ "          </PolygonSymbolizer>\n" + "\n" + "        </Rule>\n" + "\n" + "      </FeatureTypeStyle>\n"
				+ "    </UserStyle>\n" + "  </NamedLayer>\n" + "</StyledLayerDescriptor>";

		String fileNameComp = LoadInitParameter.GEOSERVER_DATA_DIR  + "/workspaces/" + workspaceSLD + "/styles/" + fileNameSLD + ".sld";

		try {
			// Write the string to the file
			Files.writeString(Paths.get(fileNameComp), content);
		} catch (IOException e) {
			System.err.println("Error writing file: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private String layerName = null;

	public String getLayerName() {
		return layerName;
	}

	public void setLayerName(String layerName) {
		this.layerName = layerName;
	}

	/**
	 * Enumeration for the different types of Layers
	 */
	public enum LayerType {
		POINT, POLYGON, LINESTRING, MULTIPOLYGON,

		TILE,

		WMSGEOSERVER, RASTERGEOSERVER,

		GROUP
	}

	private boolean skip;

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public String onFlowProcess(FlowEvent event) {
		skip = false;
		return event.getNewStep();

	}

	@Transactional
	public String onFlowProcessWms(FlowEvent event) {
		return event.getNewStep();
	}

	private String clientLayer;

	public String getClientLayer() {
		return clientLayer;
	}

	public void setClientLayer(String clientLayer) {
		this.clientLayer = clientLayer;
	}

	@Transactional
	public void importGeoTiff(FileUploadEvent event) {


		FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		try {
			copyFileImage(event.getFile().getFileName(), event.getFile().getInputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		defineLayerWizard(this.getInputFile());
	}

	@Transactional
	public void defineLayerWizard(String fileNamePath) {
		Layer layerNew = this.getLayerNew(this.layerWizard.getLayerTypeChose(), this.layerWizard.getLayerName());
		entityManager.persist(layerNew);
		entityManager.flush();
		this.layerWizard.setId(layerNew.getId());
		this.layerWizard.setUrlLayer(fileNamePath);
	}

	@Transactional
	public void importSHP(FileUploadEvent event) {

		try {
			String filN = copyFileImage(event.getFile().getFileName(), event.getFile().getInputStream(), true);
			this.layerWizard.setImageName(filN);
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Erro", "Erro ao enviar o arquivo!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

		extractShpFile(); // Extract the Offset

	}
	
	@Transactional
	public void importKML(FileUploadEvent event) throws Exception {

		try {
			String filNKML = copyFileImage(event.getFile().getFileName(), event.getFile().getInputStream(), true);
			
			File kmlFile = new File("/home/tischer/Documentos/teste2.kml");
		       // KMLConfiguration configuration = new KMLConfiguration();
		        InputStream inputStream3 = new FileInputStream(kmlFile);

		        // Create a parser with the KML configuration
		        org.geotools.xsd.Parser parser3 = new org.geotools.xsd.Parser(new KMLConfiguration());

		        // Parse the KML document (this often returns the top-level Document/Folder as a SimpleFeature)
		        SimpleFeature kmlFeature = (SimpleFeature) parser3.parse(inputStream3);
		        
		        // Extract the list of nested features (Placemarks, Folders, etc.)
		        // KML features are typically found under an attribute named "Feature"

		        List<SimpleFeature> features = (List<SimpleFeature>) kmlFeature.getAttribute("Feature");

		        if (features != null) {

		            for (SimpleFeature feature : features) {
		                // Access the default geometry, which might be a Point, LineString, Polygon, etc.
		                Object defaultGeometry = feature.getDefaultGeometry();
		     
		                Polygon polSave = null;
		                if (defaultGeometry instanceof org.locationtech.jts.geom.Polygon) {
		                	org.locationtech.jts.geom.Polygon polygon33 = (org.locationtech.jts.geom.Polygon) defaultGeometry;
		                	
		               
		                    
		                    if (this.selectedLayer.getEpsg() == 4326) {
		                    	 
		                    	 
		                    	Geometry geometry = null;
		             			WKTReader wktReader = new WKTReader();


		             			String consulta = "SELECT  public.st_astext(" + " public.st_transform(public.st_geomfromtext('"
		             					+ polygon33.toString()
		             					
		             					+ "', 4326), "
		             					+ this.selectedLayer.getEpsg() + ")) ";

		             			Query queryFF = entityManager.createNativeQuery(consulta);

		             			// GUAVA
		             			List<Object> listFF = Lists.newArrayList(Iterables.filter(queryFF.getResultList(), Object.class));



		             			Geometry targetGeometry1 = null;

		             			if (!listFF.isEmpty()) {
		             				Object geometriaString = listFF.get(0);
		             				try {
		             					geometry = wktReader.read(geometriaString.toString());

		             					// ------------------------------------------------------------------------------

		             					polygon33 = (org.locationtech.jts.geom.Polygon) geometry;
		             					polygon33.setSRID(this.selectedLayer.getEpsg());

		             					// targetGeometry1.toString());

		             					// -----------------------------------------------------------------------------------------------------------

		             				} catch (ParseException e) {
		             					e.printStackTrace();

		             				}
		                    	 
		                    	 
		             			}
		                    	 
		                    	 
		                    	 
		                    	 
		                    	 
		                    }else {
		                    }
		                    
		                    polSave = new Polygon();
		                    polSave.setEnabled(true);
		                    polSave.setGeometry(polygon33);
		                    polSave.setIconflag(null);
		                    polSave.setLayer(this.selectedLayer);
		                    polSave.setNome(feature.getAttribute("name").toString());
		                    polSave.setParte(null);
		                    polSave.setPerimetro(polygon33.getLength());
		                    polSave.setArea(polygon33.getArea());
		                    polSave.setSituacao(Short.valueOf("1"));
		                    polSave.setStrings(JacksonUtil.toJsonNode("{}"));
		                 
		                    entityManager.persist(polSave);
		                 
		                    
		                    
		                    
		                }
		                // You can add more checks for other geometry types if needed
		            }
		        }

			
		        entityManager.flush();
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage msg = new FacesMessage("Erro", "Erro ao enviar o arquivo!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
		
		
		
		
		

	}

	@Transactional
	public void extractShpFile() {
		this.layerTemp = this.getLayerNew(this.layerWizard.getLayerTypeChose(), this.layerWizard.getLayerName());
		this.layerTemp.setEnabled(false);
		entityManager.persist(this.layerTemp);
		entityManager.flush();

		try {
			String destinationFolder = LoadInitParameter.GEOSERVER_DATA_DIR + "/" + this.layerTemp.storeLayerString();
			String zipFileName = this.layerWizard.getImageName();
			this.layerWizard.setId(this.layerTemp.getId());
			this.layerWizard.setDataSets(unzip(zipFileName, destinationFolder));
			this.layerWizard.setUrlLayer(destinationFolder);
			this.layerWizard.setzIndex(this.layerTemp.getOrderlayer().toString());
		} catch (IOException e) {
			FacesMessage msg = new FacesMessage("Erro", "Erro ao descompactar o arquivo!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	// Method to zip files
	public static void zipFiles(String[] srcFiles, String zipFile) throws IOException {
		try (FileOutputStream fos = new FileOutputStream(zipFile); ZipOutputStream zos = new ZipOutputStream(fos)) {

			byte[] buffer = new byte[1024];

			for (String srcFile : srcFiles) {
				File fileToZip = new File(srcFile);
				try (FileInputStream fis = new FileInputStream(fileToZip)) {
					zos.putNextEntry(new ZipEntry(fileToZip.getName()));
					int length;
					while ((length = fis.read(buffer)) > 0) {
						zos.write(buffer, 0, length);
					}
					zos.closeEntry();
				}
			}
		}
	}

	// Method to unzip files
	public static List<String> unzip(String zipFile, String destFolder) throws IOException {

		
		
		
		List<String> dataSets = new ArrayList<String>();
		
		
		
		Path zipFile2 =  Path.of(zipFile);
		
		
		Path destDir2 =  Path.of(destFolder);
		
		
		
		try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(zipFile2))) {
	        ZipEntry entry;
	        while ((entry = zis.getNextEntry()) != null) {
	            Path resolvedPath = destDir2.resolve(entry.getName()).normalize();
	            
	            // Security Check: Prevent Zip Slip vulnerability
	            if (!resolvedPath.startsWith(destDir2)) {
	                throw new IOException("Entry is outside of the target dir: " + entry.getName());
	            }

	            if (entry.isDirectory()) {
	                Files.createDirectories(resolvedPath);
	            } else {
	                Files.createDirectories(resolvedPath.getParent());
	                Files.copy(zis, resolvedPath, StandardCopyOption.REPLACE_EXISTING);
	                
	                
	            	Integer lastDotIndex = entry.getName().lastIndexOf('.');
					if (entry.getName().toLowerCase().endsWith(".shp") && lastDotIndex > 0) {
						dataSets.add(entry.getName().substring(0, lastDotIndex));
					}

	                
	                
	            }
	            zis.closeEntry();
	        }
	    }
		
		
		
		
		
		
		
		
		return dataSets;
	}

	public void testeTFlow() {

	}

	public String getECom() {
		return "&";
	}

	public String getEqualCom() {
		return "&&";
	}

	public String getJScriptStart() {
		return "<script type=\"text/javascript\">";
	}

	public String getJScriptEnd() {
		return "</script>";
	}

	private StreamedContent file;

	public StreamedContent getFile() {
		return file;
	}

	public void throwNullPointerException() {
		throw new NullPointerException("A NullPointerException!");
	}

	public void throwWrappedIllegalStateException() {
		Throwable t = new IllegalStateException("A wrapped IllegalStateException!");
		throw new FacesException(t);
	}

	public void throwViewExpiredException() {
		throw new ViewExpiredException("A ViewExpiredException!",
				FacesContext.getCurrentInstance().getViewRoot().getViewId());
	}

	public void newLayer() {
		layerWizard = new LayerWizard();
		layerWizard.setVisible(true);
		layerWizard.setOpacity(new BigDecimal("100"));
	}

	private LayerWizard layerWizard;

	public LayerWizard getLayerWizard() {
		return layerWizard;
	}

	public void setLayerWizard(LayerWizard layerWizard) {
		this.layerWizard = layerWizard;
	}

	public Long getGcmid() {
		return gcmid;
	}

	public void setGcmid(Long gcmid) {
		this.gcmid = gcmid;
	}

	// ========================================================================

	private LayerItem selectedLayerItem;

	public LayerItem getSelectedLayerItem() {
		return selectedLayerItem;
	}

	public void setSelectedLayerItem(LayerItem selectedLayerItem) {
		this.selectedLayerItem = selectedLayerItem;
	}

	private List<LayerItem> selectedLayerItems;

	public List<LayerItem> getSelectedLayerItems() {
		return selectedLayerItems;
	}

	public void setSelectedLayerItems(List<LayerItem> selectedLayerItems) {
		this.selectedLayerItems = selectedLayerItems;
	}

	private Layer layer;

	public Layer getLayer() {
		return this.layer;
	}

	public void setLayer(Layer layer) {
		this.layer = layer;
	}

	private TreeNode root;
	private TreeNode selectedNode;

	@Inject
	private LayerBarFacade facade;

	@Inject
	private LayerGroupBarFacade facadeGroup;

	public void buildTree() {
		try {
			root = new DefaultTreeNode("Root", null);

			List<LayerItem> layersRoot = facade.getRootLayerItems(this.layer);
			for (LayerItem layer : layersRoot) {

				Long layerId = layer.getId();

				DefaultTreeNode node;

				if (layer.getChildrens().size() > 0) {
					node = new DefaultTreeNode(layer, root);
				} else {
					node = new DefaultTreeNode(layer.getType(), layer, root);
				}
				node.setExpanded(true);
				buildSubTree(layer, node);
			}

		} catch (Exception ex) {
		}
	}

	public void buildSubTree(LayerItem layer, DefaultTreeNode parent) {

		List<LayerItem> subLayers = facade.getSubLayers(layer.getId());
		for (LayerItem subLayer : subLayers) {

			Long subLayerId = subLayer.getId();

			Boolean hasSubLayer = facade.hasSubLayer(subLayer);
			DefaultTreeNode node;
			if (!hasSubLayer) {
				node = new DefaultTreeNode(subLayer.getType(), subLayer, parent);
			} else {
				node = new DefaultTreeNode(subLayer, parent);
				List<LayerItem> getSubLayers = facade.getSubLayers(subLayer.getId());

				for (LayerItem m : getSubLayers) {
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
			this.selectedLayerItem = (LayerItem) selectedNode.getData();
		}

		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public List<LayerItem> getLayersRoot() {
		if (selectedNode != null) {
			LayerItem layer = (LayerItem) selectedNode.getData();
			if (layer.getParentId() == null) {
				return facade.getRootLayerItems(this.layer);
			} else {
				return facade.getSubLayers(layer.getParentId().getId());
			}
		} else {
			return null;
		}
	}

	// ==============================================================================

	public void onInputChanged(ValueChangeEvent event) {
		Object teste = event.getNewValue();
		String info = (String) event.getComponent().getAttributes().get("customInfo");
		FacesMessage message = new FacesMessage("Id: " + info, "Value: " + event.getNewValue());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@Transactional
	public void onSlideEnd(SlideEndEvent event) {
		String idLayer = (String) event.getComponent().getAttributes().get("idLayer");
		Double valueOpacity = event.getValue();
		Layer layerChange = entityManager.find(Layer.class, Long.valueOf(idLayer));

		layerChange.setOpacity(BigDecimal.valueOf(valueOpacity));

		entityManager.merge(layerChange);
		entityManager.flush();
		FacesMessage message = new FacesMessage("SUCESSO!", "Opacidade Alterada!");
		FacesContext.getCurrentInstance().addMessage(null, message);
		PrimeFaces.current().ajax().addCallbackParam("layerId", layerChange.layerString());
		PrimeFaces.current().ajax().addCallbackParam("opacity", BigDecimal.valueOf(valueOpacity));
	}

	@Transactional
	public void addMessageNew(AjaxBehaviorEvent event) {
		String idLayer = (String) event.getComponent().getAttributes().get("idLayer");
		UIComponent component = event.getComponent();
		if (component instanceof UIInput) {
			UIInput inputComponent = (UIInput) component;
			Boolean value = (Boolean) inputComponent.getValue();
			Layer layerChange = entityManager.find(Layer.class, Long.valueOf(idLayer));
			layerChange.setSelected(value);
			entityManager.merge(layerChange);
			entityManager.flush();

			FacesMessage message = new FacesMessage("SUCESSO!", "Visibilidade Alterada!");
			FacesContext.getCurrentInstance().addMessage(null, message);
			PrimeFaces.current().ajax().addCallbackParam("layerId", layerChange.layerString());
			PrimeFaces.current().ajax().addCallbackParam("visible", value);

		}
	}

	public void layersGroupLoad() {


		String objectidCapture = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("layerId");

		Long layerId = Long.valueOf(objectidCapture);

		this.appLayersGroup.clear();

		this.selectedLayerGroup = findLayer(layerId);

		for (Layer lnewg : this.selectedLayerGroup.getChildrens()) {
			LayerView layerViewG = new LayerView();
			layerViewG.setLayerid(lnewg.getId().toString());
			layerViewG.setVisible(lnewg.getSelected());
			layerViewG.setOpacity(lnewg.getOpacity());
			layerViewG.setZIndex(lnewg.getOrderlayer());
			layerViewG.setLayerhash(lnewg.layerString());
			layerViewG.setName(lnewg.getName());
			layerViewG.setFather(lnewg.getChildrens().size() > 0);
			appLayersGroup.add(layerViewG);
		}


	}

	// ========================================================================
	// ========================================================================

	private Layer selectedLayerGroup;

	public Layer getSelectedLayerGroup() {
		return selectedLayerGroup;
	}

	public void setSelectedLayerGroup(Layer selectedLayerGroup) {
		this.selectedLayerGroup = selectedLayerGroup;
	}

	private List<Layer> selectedLayerGroups;

	public List<Layer> getSelectedLayerGroups() {
		return selectedLayerGroups;
	}

	public void setSelectedLayerGroups(List<Layer> selectedLayerGroups) {
		this.selectedLayerGroups = selectedLayerGroups;
	}

	// ==============================================================================

}