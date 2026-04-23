package org.geoazul.view.security;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.*;
import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import modules.LoadInitParameter;

import org.geoazul.erp.Cities;
import org.geoazul.erp.Countries;
import org.geoazul.erp.States;
import org.geoazul.geoserver.CreateProjectWorkspace;
import org.example.kickoff.view.ActiveUser;
import org.geoazul.model.Address;
import org.geoazul.model.Names;
import org.geoazul.model.app.AppUserMappingEntity;
import org.geoazul.model.app.AppUserMappingEntity.AppRole;
import org.geoazul.model.app.ApplicationEntity;
import org.geoazul.model.app.CompAttributeEntity;
import org.geoazul.model.app.ProjectBasicEntity;
import org.geoazul.model.basic.AbstractGeometry;
import org.geoazul.model.basic.LabelStyle;
import org.geoazul.model.basic.Layer;
import org.geoazul.model.basic.LayerGroup;
import org.geoazul.model.basic.LayerLinestring;
import org.geoazul.model.basic.LayerMultiPolygon;
import org.geoazul.model.basic.LayerPoint;
import org.geoazul.model.basic.LayerPolygon;
import org.geoazul.model.basic.LayerRasterGeoserver;
import org.geoazul.model.basic.LayerTile;
import org.geoazul.model.basic.LayerWMSGeoserver;
import org.geoazul.model.basic.Linestring;
import org.geoazul.model.basic.MultiPolygon;
import org.geoazul.model.basic.Point;
import org.geoazul.model.basic.Polygon;
import org.geoazul.model.basic.properties.Field;
import org.geoazul.model.security.ClientComponentEntity;
import org.geoazul.model.security.ClientOAuthEntity;
import org.geoazul.model.security.UserAttributeEntity;
import org.geoazul.model.security.UserAttributeEntity.MimeType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.example.kickoff.business.service.PersonService;
import org.example.kickoff.model.Person;
import org.keycloak.example.oauth.UserData;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DialogFrameworkOptions;
import org.primefaces.model.LazyDataModel;
import org.primefaces.refact.PartnerJpaLazyDataModel;
import com.erp.modules.inventory.entities.ErpProductUserMapping;
import static modules.LoadInitParameter.*;
import static org.omnifaces.util.Faces.redirect;

@Named
@ViewScoped
public class PersonEntityBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@PreDestroy
	public void preDestroy() {
	}

	@Inject
	EntityManager entityManager;

	@Inject
	private UserData userData;

	@Inject
	@Param(pathIndex = 0)
	private Long gcmid;

	public Long getGcmid() {
		return gcmid;
	}

	public void setGcmid(Long gcmid) {
		this.gcmid = gcmid;
	}

	@Inject
	@Param(pathIndex = 1)
	private Long id;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// @DeclareRoles("BusinessAdmin")
	// @RolesAllowed("Administrator")
	// @PermitAll

	public void chooseProject() {

		DialogFrameworkOptions options = DialogFrameworkOptions.builder().resizable(false).draggable(true).modal(true)
				.build();

		Map<String, List<String>> params = new HashMap<>();
		params.put("personId", Collections.singletonList(this.person.getId().toString())); // Single value

		PrimeFaces.current().dialog().openDynamic("selectProject", options, params);

	}

	public void onProjectChosen(SelectEvent event) {
		ApplicationEntity app = (ApplicationEntity) event.getObject();
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Projeto Selecionado",
				"Name:" + app.getName());

		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	private Person activePerson;

	public Person getActivePerson() {
		return activePerson;
	}

	public void setActivePerson(Person activePerson) {
		this.activePerson = activePerson;
	}

	private List<AppUserMappingEntity> selectedProjects;

	public List<AppUserMappingEntity> getSelectedProjects() {
		return selectedProjects;
	}

	public void setSelectedProjects(List<AppUserMappingEntity> selectedProjects) {
		this.selectedProjects = selectedProjects;
	}

	@Transactional
	public void cloneApps() {
		try {

			for (AppUserMappingEntity selectedProject : this.getSelectedProjects()) {

				ApplicationEntity cloneableAPP = entityManager.find(ApplicationEntity.class,
						selectedProject.getApp().getId());

				ProjectBasicEntity projectBasicNew = new ProjectBasicEntity();

				projectBasicNew.setOrigin(cloneableAPP);
				projectBasicNew.setCentroidwkt(cloneableAPP.getCentroidwkt());
				projectBasicNew.setClientEntity(cloneableAPP.getClientEntity());
				projectBasicNew.setDefaultApp(cloneableAPP.getDefaultApp());
				projectBasicNew.setDescription(cloneableAPP.getDescription());
				projectBasicNew.setDtype(cloneableAPP.getDtype());
				projectBasicNew.setEnabled(cloneableAPP.getEnabled());
				projectBasicNew.setEpsg(cloneableAPP.getEpsg());
				projectBasicNew.setImage(cloneableAPP.getImage());
				projectBasicNew.setLikes(0);
				projectBasicNew.setLocale(cloneableAPP.getLocale());
				projectBasicNew.setMaxres(cloneableAPP.getMaxres());
				projectBasicNew.setMaxZoom(cloneableAPP.getMaxZoom());
				projectBasicNew.setMinres(cloneableAPP.getMinres());
				projectBasicNew.setMinZoom(cloneableAPP.getMinZoom());
				projectBasicNew.setName(cloneableAPP.getName());
				projectBasicNew.setShared(cloneableAPP.getShared());
				projectBasicNew.setStrings(cloneableAPP.getStrings());
				projectBasicNew.setTemplate(cloneableAPP.getTemplate());
				projectBasicNew.setTitle(cloneableAPP.getTitle());

				entityManager.persist(projectBasicNew);
				entityManager.flush();

				String origName = cloneableAPP.getImage();
				String dirOrig = cloneableAPP.getId().toString();
				String dirDest = projectBasicNew.getId().toString();

				copy(origName, dirOrig, dirDest);

				for (Layer selectedLayer : cloneableAPP.getLayers().stream().filter(l -> l.getFather() == null)
						.toList()) {

					Layer layerCloneToSave = getLayerClone(selectedLayer);
					layerCloneToSave.setApplicationEntity(projectBasicNew);
					layerCloneToSave.setOrigin(selectedLayer);
					entityManager.persist(layerCloneToSave);
					entityManager.flush();

					cloneAttibutes(selectedLayer, layerCloneToSave);
					cloneFields(selectedLayer, layerCloneToSave);


					cloneGeometries1(selectedLayer, layerCloneToSave);
					cloneGeometries2(selectedLayer, layerCloneToSave);

					if (selectedLayer.getAttributes().stream().filter(f -> f.getName().equals("DATASET")).findAny()
							.isPresent()) {

						String dataSet = selectedLayer.getAttributes().stream()
								.filter(f -> f.getName().equals("DATASET")).findAny().get().getValue();
						getLayerCloneGeoserver(layerCloneToSave, dataSet);
					}

					for (Layer selectedLayerChild : selectedLayer.getChildrens()) {
						Layer layerCloneToSaveChild = getLayerClone(selectedLayerChild);
						layerCloneToSaveChild.setApplicationEntity(projectBasicNew);
						layerCloneToSaveChild.setFather(layerCloneToSave);
						layerCloneToSaveChild.setOrigin(selectedLayerChild);
						entityManager.persist(layerCloneToSaveChild);
						entityManager.flush();

						cloneAttibutes(selectedLayerChild, layerCloneToSaveChild);
						cloneFields(selectedLayerChild, layerCloneToSaveChild);

						cloneGeometries1(selectedLayerChild, layerCloneToSaveChild);
						cloneGeometries2(selectedLayerChild, layerCloneToSaveChild);

						if (selectedLayerChild.getAttributes().stream().filter(f -> f.getName().equals("DATASET"))
								.findAny().isPresent()) {

							String dataSet = selectedLayerChild.getAttributes().stream()
									.filter(f -> f.getName().equals("DATASET")).findAny().get().getValue();
							getLayerCloneGeoserver(layerCloneToSaveChild, dataSet);
						}
					}

				}

				AppUserMappingEntity appUserMapping = new AppUserMappingEntity();
				appUserMapping.setApp(projectBasicNew);

				Person testeP = findUserId(this.personId);

				appUserMapping.setPerson(testeP);
				appUserMapping.setAppRole(AppRole.OWNER);
				entityManager.persist(appUserMapping);
				entityManager.flush();

			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso!", "Apps Clonados!"));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro!", "Erro ao Clonar!"));
			e.printStackTrace();
		}
	}

	@Transactional
	public void cloneGeometries1(Layer selectedLayer, Layer layerCloneToSave) {

		for (AbstractGeometry geometryIn : selectedLayer.getGeometrias().stream().filter(f -> f.getFather() == null)
				.toList()) {


			switch (geometryIn.getClass().getSimpleName()) {

			case "Point":

				Point geomPoi = new Point();
				Point geometryPoi = (Point) geometryIn;

				geomPoi.setGeometry(geometryPoi.getGeometry());
				geomPoi.setGeometrywkt(geometryPoi.getGeometrywkt());

				geomPoi.setMetodoLevantamento(geometryPoi.getMetodoLevantamento());
				geomPoi.setSigmaE(geometryPoi.getSigmaE());
				geomPoi.setSigmaN(geometryPoi.getSigmaN());
				geomPoi.setSigmaH(geometryPoi.getSigmaH());
				geomPoi.setAltura(geometryPoi.getAltura());

				// geom.setCategories(geometry.getCategories());
				// geom.setChildrens(geometry.getChildrens());

				
				geomPoi.setEnabled(geometryPoi.getEnabled());

				geomPoi.setFather(null);

				geomPoi.setIconflag(geometryPoi.getIconflag());
				geomPoi.setLayer(layerCloneToSave);
				// geom.setMedias(geometry.getMedias());
				geomPoi.setNome(geometryPoi.getNome());
				geomPoi.setParte(geometryPoi.getParte());
				geomPoi.setSituacao(geometryPoi.getSituacao());
				geomPoi.setStrings(geometryPoi.getStrings());

				geomPoi.setOrigin(geometryPoi);

				entityManager.persist(geomPoi);
				entityManager.flush();

				break;

			case "Polygon":

				Polygon geomPol = new Polygon();
				Polygon geometryPol = (Polygon) geometryIn;

				geomPol.setGeometry(geometryPol.getGeometry());
				geomPol.setGeometrywkt(geometryPol.getGeometrywkt());

				// geom.setCategories(geometry.getCategories());
				// geom.setChildrens(geometry.getChildrens());

			
				geomPol.setEnabled(geometryPol.getEnabled());

				geomPol.setFather(null);

				geomPol.setIconflag(geometryPol.getIconflag());
				geomPol.setLayer(layerCloneToSave);
				// geom.setMedias(geometry.getMedias());
				geomPol.setNome(geometryPol.getNome());
				geomPol.setParte(geometryPol.getParte());
				geomPol.setSituacao(geometryPol.getSituacao());
				geomPol.setStrings(geometryPol.getStrings());

				geomPol.setOrigin(geometryPol);

				entityManager.persist(geomPol);
				entityManager.flush();
				break;
			case "Linestring":

				Linestring geomLin = new Linestring();
				Linestring geometryLin = (Linestring) geometryIn;

				geomLin.setGeometry(geometryLin.getGeometry());
				geomLin.setGeometrywkt(geometryLin.getGeometrywkt());

				geomLin.setValoroffset(geometryLin.getValoroffset());

				// geom.setCategories(geometry.getCategories());
				// geom.setChildrens(geometry.getChildrens());

			
				geomLin.setEnabled(geometryLin.getEnabled());

				geomLin.setFather(null);

				geomLin.setIconflag(geometryLin.getIconflag());
				geomLin.setLayer(layerCloneToSave);
				// geom.setMedias(geometry.getMedias());
				geomLin.setNome(geometryLin.getNome());
				geomLin.setParte(geometryLin.getParte());
				geomLin.setSituacao(geometryLin.getSituacao());
				geomLin.setStrings(geometryLin.getStrings());

				geomLin.setOrigin(geometryLin);

				entityManager.persist(geomLin);
				entityManager.flush();

				break;
			case "MultiPolygon":

				MultiPolygon geomMPol = new MultiPolygon();
				MultiPolygon geometryMPol = (MultiPolygon) geometryIn;

				geomMPol.setGeometry(geometryMPol.getGeometry());

				// geom.setCategories(geometry.getCategories());
				// geom.setChildrens(geometry.getChildrens());

				
				geomMPol.setEnabled(geometryMPol.getEnabled());

				geomMPol.setFather(null);

				geomMPol.setIconflag(geometryMPol.getIconflag());
				geomMPol.setLayer(layerCloneToSave);
				// geom.setMedias(geometry.getMedias());
				geomMPol.setNome(geometryMPol.getNome());
				geomMPol.setParte(geometryMPol.getParte());
				geomMPol.setSituacao(geometryMPol.getSituacao());
				geomMPol.setStrings(geometryMPol.getStrings());

				geomMPol.setOrigin(geometryMPol);

				entityManager.persist(geomMPol);
				entityManager.flush();

				break;
			}

		}

	}

	@Transactional
	public void cloneGeometries2(Layer selectedLayer, Layer layerCloneToSave) {

		for (AbstractGeometry geometryIn : selectedLayer.getGeometrias().stream().filter(f -> f.getFather() != null)
				.toList()) {

	
			switch (geometryIn.getClass().getSimpleName()) {

			case "Point":

				Point geomPoi = new Point();
				Point geometryPoi = (Point) geometryIn;

				geomPoi.setGeometry(geometryPoi.getGeometry());
				geomPoi.setGeometrywkt(geometryPoi.getGeometrywkt());

				geomPoi.setMetodoLevantamento(geometryPoi.getMetodoLevantamento());
				geomPoi.setSigmaE(geometryPoi.getSigmaE());
				geomPoi.setSigmaN(geometryPoi.getSigmaN());
				geomPoi.setSigmaH(geometryPoi.getSigmaH());
				geomPoi.setAltura(geometryPoi.getAltura());

				// geom.setCategories(geometry.getCategories());
				// geom.setChildrens(geometry.getChildrens());

				
				geomPoi.setEnabled(geometryPoi.getEnabled());

				AbstractGeometry kk112 = findGeomById(geometryPoi.getFather().getId());
				if (kk112 != null) {
					AbstractGeometry kk212 = findGeomByOriginId(kk112.getId());
					geomPoi.setFather(kk212);
				}

				geomPoi.setIconflag(geometryPoi.getIconflag());
				geomPoi.setLayer(layerCloneToSave);
				// geom.setMedias(geometry.getMedias());
				geomPoi.setNome(geometryPoi.getNome());
				geomPoi.setParte(geometryPoi.getParte());
				geomPoi.setSituacao(geometryPoi.getSituacao());
				geomPoi.setStrings(geometryPoi.getStrings());

				geomPoi.setOrigin(geometryPoi);

				entityManager.persist(geomPoi);
				entityManager.flush();

				break;

			case "Polygon":

				Polygon geomPol = new Polygon();
				Polygon geometryPol = (Polygon) geometryIn;

				geomPol.setGeometry(geometryPol.getGeometry());
				geomPol.setGeometrywkt(geometryPol.getGeometrywkt());

				// geom.setCategories(geometry.getCategories());
				// geom.setChildrens(geometry.getChildrens());

				
				geomPol.setEnabled(geometryPol.getEnabled());

				AbstractGeometry kk1 = findGeomById(geometryPol.getFather().getId());
				if (kk1 != null) {
					AbstractGeometry kk2 = findGeomByOriginId(kk1.getId());
					geomPol.setFather(kk2);
				}

				geomPol.setIconflag(geometryPol.getIconflag());
				geomPol.setLayer(layerCloneToSave);
				// geom.setMedias(geometry.getMedias());
				geomPol.setNome(geometryPol.getNome());
				geomPol.setParte(geometryPol.getParte());
				geomPol.setSituacao(geometryPol.getSituacao());
				geomPol.setStrings(geometryPol.getStrings());

				geomPol.setOrigin(geometryPol);

				entityManager.persist(geomPol);
				entityManager.flush();
				break;
			case "Linestring":

				Linestring geomLin = new Linestring();
				Linestring geometryLin = (Linestring) geometryIn;

				geomLin.setGeometry(geometryLin.getGeometry());
				geomLin.setGeometrywkt(geometryLin.getGeometrywkt());

				geomLin.setValoroffset(geometryLin.getValoroffset());

				// geom.setCategories(geometry.getCategories());
				// geom.setChildrens(geometry.getChildrens());

				
				geomLin.setEnabled(geometryLin.getEnabled());

				AbstractGeometry kk11 = findGeomById(geometryLin.getFather().getId());
				if (kk11 != null) {
					AbstractGeometry kk21 = findGeomByOriginId(kk11.getId());
					geomLin.setFather(kk21);
				}

				geomLin.setIconflag(geometryLin.getIconflag());
				geomLin.setLayer(layerCloneToSave);
				// geom.setMedias(geometry.getMedias());
				geomLin.setNome(geometryLin.getNome());
				geomLin.setParte(geometryLin.getParte());
				geomLin.setSituacao(geometryLin.getSituacao());
				geomLin.setStrings(geometryLin.getStrings());

				geomLin.setOrigin(geometryLin);

				entityManager.persist(geomLin);
				entityManager.flush();

				break;
			case "MultiPolygon":

				MultiPolygon geomMPol = new MultiPolygon();
				MultiPolygon geometryMPol = (MultiPolygon) geometryIn;

				geomMPol.setGeometry(geometryMPol.getGeometry());

				// geom.setCategories(geometry.getCategories());
				// geom.setChildrens(geometry.getChildrens());

			
				geomMPol.setEnabled(geometryMPol.getEnabled());

				AbstractGeometry kk112A = findGeomById(geometryMPol.getFather().getId());
				if (kk112A != null) {
					AbstractGeometry kk212A = findGeomByOriginId(kk112A.getId());
					geomMPol.setFather(kk212A);
				}

				geomMPol.setIconflag(geometryMPol.getIconflag());
				geomMPol.setLayer(layerCloneToSave);
				// geom.setMedias(geometry.getMedias());
				geomMPol.setNome(geometryMPol.getNome());
				geomMPol.setParte(geometryMPol.getParte());
				geomMPol.setSituacao(geometryMPol.getSituacao());
				geomMPol.setStrings(geometryMPol.getStrings());

				geomMPol.setOrigin(geometryMPol);

				entityManager.persist(geomMPol);
				entityManager.flush();

				break;
			}

		}

	}

	public AbstractGeometry findGeomById(Long id) {
		try {
			TypedQuery<AbstractGeometry> queryAbs = entityManager.createNamedQuery(AbstractGeometry.SURFACE_ID,
					AbstractGeometry.class);
			queryAbs.setParameter("id", id);
			return queryAbs.getSingleResult();
		} catch (Exception ex) {
			return null;
		}
	}

	public AbstractGeometry findGeomByFhaterId(Long id) {
		try {
			TypedQuery<AbstractGeometry> queryAbs = entityManager.createNamedQuery(AbstractGeometry.FATHER_ID,
					AbstractGeometry.class);
			queryAbs.setParameter("fatherId", id);
			return queryAbs.getSingleResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public AbstractGeometry findGeomByOriginId(Long id) {
		try {
			TypedQuery<AbstractGeometry> queryAbs = entityManager.createNamedQuery(AbstractGeometry.ORIGIN_ID,
					AbstractGeometry.class);
			queryAbs.setParameter("originId", id);
			return queryAbs.getSingleResult();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public AbstractGeometry findAbstractGeometryById(Long id) {
		try {
			return entityManager.find(AbstractGeometry.class, id);
		} catch (Exception e) {
			return null;
		}
	}

	@Transactional
	public void cloneAttibutes(Layer selectedLayer, Layer layerCloneToSave) {
		for (CompAttributeEntity attribute : selectedLayer.getAttributes()) {
			CompAttributeEntity compAtribNew = new CompAttributeEntity();
			compAtribNew.setComponent(layerCloneToSave);
			compAtribNew.setDtype(attribute.getDtype());
			compAtribNew.setName(attribute.getName());
			compAtribNew.setValue(attribute.getValue());
			entityManager.persist(compAtribNew);
			entityManager.flush();
		}
	}

	@Transactional
	public void cloneFields(Layer selectedLayer, Layer layerCloneToSave) {
		Integer seq = -1;
		for (Field field : selectedLayer.getFields()) {
			seq++;
			Field fieldNew = new Field();
			fieldNew.setSequence(seq);
			fieldNew.setLayer(layerCloneToSave);
			fieldNew.setName(field.getName());
			fieldNew.setLabel(field.getLabel());
			fieldNew.setFieldControl(field.getFieldControl());
			fieldNew.setPlaceHolder(field.getPlaceHolder());
			fieldNew.setRequired(field.getRequired());
			fieldNew.setTypeData(field.getTypeData());
			entityManager.persist(fieldNew);
			entityManager.flush();
		}

	}

	private String tenant;

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	// copy(origName, dirOrig, dirDest);
	public void copy(String origName, String dirOrig, String dirDest) {

		String origenFile = save_FILE_PATH + "/files/" + userData.getRealmEntity() + "/" + dirOrig + "/media/"
				+ origName;

		String destinationDirectory = save_FILE_PATH + "/files/" + userData.getRealmEntity() + "/" + dirDest + "/media";

		String destinationFile = save_FILE_PATH + "/files/" + userData.getRealmEntity() + "/" + dirDest + "/media/"
				+ origName;


		Path destinationDir = Paths.get(destinationDirectory);

		Path sourceImage = Paths.get(origenFile);
		Path destinationImage = Paths.get(destinationFile);

		try {
			Files.createDirectories(destinationDir);
			// Copy the file, replacing the target if it already exists
			Files.copy(sourceImage, destinationImage, REPLACE_EXISTING);
		} catch (IOException e) {
			System.err.format("Unable to copy file: %s%n", e.getMessage());
			e.printStackTrace();
		}
	}

	public Layer getLayerClone(Layer layerLoad) {


		
		Layer lt = null;

		switch (layerLoad.getDtype()) {

		case "tile":
			lt = new LayerTile();
			break;
		case "rastergeoserver":
			lt = new LayerRasterGeoserver();
			break;
		case "wmsgeoserver":
			lt = new LayerWMSGeoserver();
			break;
		case "point":
			lt = new LayerPoint();
			break;
		case "polygon":
			lt = new LayerPolygon();
			break;
		case "group":
			lt = new LayerGroup();
			break;
		case "linestring":
			lt = new LayerLinestring();
			break;
		case "multipolygon":
			lt = new LayerMultiPolygon();
			break;
		}

		lt.setCentroidwkt(layerLoad.getCentroidwkt());
		lt.setClientComponentEntity(layerLoad.getClientComponentEntity());
		lt.setDescription(layerLoad.getDescription());
		lt.setDtype(layerLoad.getDtype());
		lt.setEditable(layerLoad.isEditable());
		lt.setEnabled(layerLoad.getEnabled());
		lt.setEpsg(layerLoad.getEpsg());

		lt.setGeometryStyle(layerLoad.getGeometryStyle());
		lt.setIcon(layerLoad.getIcon());
		lt.setIconcategory(layerLoad.getIconcategory());
		lt.setImage(layerLoad.getImage());
		lt.setLabelStyle(layerLoad.getLabelStyle());
		lt.setLayerCat(layerLoad.getLayerCat());
		lt.setMaxres(layerLoad.getMaxres());
		lt.setMaxZoom(layerLoad.getMaxZoom());
		lt.setMinres(layerLoad.getMinres());
		lt.setMinZoom(layerLoad.getMinZoom());
		lt.setName(layerLoad.getName());
		lt.setOpacity(layerLoad.getOpacity());
		lt.setOrderlayer(layerLoad.getOrderlayer());
		lt.setScriptStyle(layerLoad.getScriptStyle());
		lt.setSelected(layerLoad.getSelected());
		lt.setStrings(layerLoad.getStrings());
		lt.setTitle(layerLoad.getTitle());
		lt.setTotalRecords(layerLoad.getTotalRecords());
		lt.setZoom(layerLoad.getZoom());
		return lt;

	}

	public Layer getLayerCloneGeoserver(Layer layerNew, String dataSet) {

		Layer lt = null;

		switch (layerNew.getDtype()) {

		case "tile":
			lt = new LayerTile();
			break;
		case "rastergeoserver":
			lt = new LayerRasterGeoserver();
			break;
		case "wmsgeoserver":
			lt = new LayerWMSGeoserver();


			TypedQuery<ClientComponentEntity> queryApp = entityManager
					.createNamedQuery(ClientComponentEntity.COMPONENT_CLIENT_ID, ClientComponentEntity.class);
			queryApp.setParameter("clientId", "geoserver");
			List<ClientComponentEntity> clientCompEntity = queryApp.getResultList();

			ClientComponentEntity clientGeoserver = clientCompEntity.get(0);



			// ---------------------------------------------------------------------------------------

			String layerName = "l" + layerNew.getId().toString() + "-" + layerNew.getDtype().toString().toLowerCase();
			String storeName = "s" + layerNew.getOrigin().getId().toString() + "-"
					+ layerNew.getDtype().toString().toLowerCase();


			try {
				CreateProjectWorkspace.createLayerSHP(storeName, dataSet, layerName, tenant, clientGeoserver);
			} catch (Exception e2) {
				// e2.printStackTrace();
			}


			String sldFileName = layerName + ".sld";


			try {
				CreateProjectWorkspace.createStyleSLD(layerName, sldFileName, tenant, clientGeoserver);
			} catch (Exception e2) {
				e2.printStackTrace();
			}

			testeFile(sldFileName, tenant, layerNew);

			try {
				CreateProjectWorkspace.updateLayerSLD(layerName, layerName, tenant, clientGeoserver);
			} catch (Exception e2) {
				// e2.printStackTrace();
			}

			// ---------------------------------------------------------------------------------------
			break;

		}

		return lt;

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
				e.printStackTrace();
			}
		} 
		return null;
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

	public void testeFile(String fileNameSLD, String workspaceSLD, Layer layerNew) {

		Color color = extractRGBA(layerNew.getGeometryStyle().getRgbafillcolor());

		String hexColor = rgbToHex(color.getRed(), color.getGreen(), color.getBlue());

		LabelStyle labelstylejson = layerNew.getLabelStyle();

		String labelName = labelstylejson.getField();

		String contentXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<StyledLayerDescriptor\n"
				+ "	xmlns:sld=\"http://www.opengis.net/sld\"\n" + "	xmlns=\"http://www.opengis.net/sld\"\n"
				+ "	xmlns:gml=\"http://www.opengis.net/gml\"\n"
				+ "	xmlns:ogc=\"http://www.opengis.net/ogc\" version=\"1.0.0\">\n" + "	<Name>" + layerNew.getName()
				+ "</Name>\n" + "	<Title>" + layerNew.getName() + "</Title>\n" + "	<Abstract>" + layerNew.getName()
				+ "</Abstract>\n" + "	<UserLayer>\n" + "		<Name>layer</Name>\n"
				+ "		<LayerFeatureConstraints>\n" + "			<FeatureTypeConstraint/>\n"
				+ "		</LayerFeatureConstraints>\n" + "		<UserStyle>\n"
				+ "			<Name>Default Styler</Name>\n" + "			<FeatureTypeStyle>\n"
				+ "				<Name>name</Name>\n" + "				<Rule>\n"

				+ "<TextSymbolizer>\n" + " <Label>\n" + "   <ogc:PropertyName>" + labelName + "</ogc:PropertyName>\n"
				+ " </Label>\n" + "</TextSymbolizer>\n"

				+ "					<PolygonSymbolizer>\n" + "						<Fill>\n"
				+ "							<CssParameter name=\"fill\">" + hexColor + "</CssParameter>\n"
				+ "							<CssParameter name=\"fill-opacity\">" + (color.getAlpha() / 255.0f)
				+ "</CssParameter>\n" + "						</Fill>\n" + "						<Stroke>\n"
				+ "							<CssParameter name=\"stroke\">"
				+ layerNew.getGeometryStyle().getRgbastrokecolor() + "</CssParameter>\n"
				+ "							<CssParameter name=\"stroke-width\">"
				+ layerNew.getGeometryStyle().getStrokewidth() + "</CssParameter>\n"
				+ "							<CssParameter name=\"stroke-dasharray\">"
				+ layerNew.getGeometryStyle().getStrokedashLenght() + " "
				+ layerNew.getGeometryStyle().getStrokedashSpace() + "</CssParameter>\n"
				+ "						</Stroke>\n" + "					</PolygonSymbolizer>\n"
				+ "				</Rule>\n" + "			</FeatureTypeStyle>\n" + "		</UserStyle>\n"
				+ "	</UserLayer>\n" + "</StyledLayerDescriptor>";

		String fileNameComp =  LoadInitParameter.GEOSERVER_DATA_DIR  + "/workspaces/" + workspaceSLD + "/styles/" + fileNameSLD;

		try {
			// Write the string to the file
			Files.writeString(Paths.get(fileNameComp), contentXML);
		} catch (IOException e) {
			System.err.println("Error writing file: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Inject
	@Param(name = "personId")
	private Long personId;

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	@Inject
	private HttpServletRequest request;

	@PostConstruct
	public void init() {
		
		
		
	
		String servername = request.getServerName().replace("-", "_").replace(".", "_");
		tenant = "geoazul_" + servername;



		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (activeUser.isPresent()) {
			TypedQuery<Person> queryUser = entityManager.createNamedQuery(Person.USER_UUID_GET, Person.class);
			queryUser.setParameter("uuid", activeUser.get().getUuid());
			List<Person> usersListRet = queryUser.getResultList();
			if (usersListRet.size() > 0) {
				this.activePerson = usersListRet.get(0);
			}
		}

		if (this.id == null) {
			model = new PartnerJpaLazyDataModel<>(Person.class, () -> entityManager);
		} else {
			try {

				TypedQuery<Person> queryApp = entityManager.createNamedQuery(Person.USER_ID_GET, Person.class);
				queryApp.setParameter("id", id);

				this.person = queryApp.getSingleResult();

				if (this.person.getAddress().getId() != null) {
					this.address = this.person.getAddress();
				} else {
					this.address = this.exaddress;
				}


			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public Person findById(Long id) {
		try {
			return entityManager.find(Person.class, id);
		} catch (Exception e) {
			return null;
		}
	}

	private LazyDataModel<Person> model;

	public LazyDataModel<Person> getModel() {
		return model;
	}

	public void setModel(LazyDataModel<Person> model) {
		this.model = model;
	}
	
	

   
	
	public Person persistActiveUser2() {
		StringBuilder fullName = new StringBuilder("LT");
		String lastName = fullName.delete(0, "TISCHER".length()).toString();
		Person personNew = new Person(UUID.fromString("LAERCIO TISCHER"), "LAERCIO", "TISCHER",
				"webmaster@risteos.com");

		personNew.setName(fullName.toString());
		personNew.setDebit(0D);
		personNew.setCredit(0D);
		personNew.setUsername("RISTEOS");

		entityManager.persist(personNew);
		entityManager.flush();
		return personNew;
	}

	@Inject
	private ActiveUser activeUser;

	@Inject
	private PersonService personService;
	
	public Person findUserId(Long personId) {
		return entityManager.find(Person.class, personId);
	}
	
	@Transactional
	public void retrieveActiveUser() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

		if (activeUser.isPresent()) {

			this.person = findUserId(activeUser.getId());

			if (this.person.getAddress().getId() != null) {
				this.address = this.person.getAddress();
			} else {
				this.address = this.exaddress;
			}

		}
	}

	@Transactional
	public int deleteErpProductUserMapping(ErpProductUserMapping erpProductUserMapping) {
		try {

			Query erpProductUserMappingQuery = entityManager.createNamedQuery(ErpProductUserMapping.DELETE);
			erpProductUserMappingQuery.setParameter("erpProductUserMapping", erpProductUserMapping);
			erpProductUserMappingQuery.executeUpdate();
			entityManager.flush();
			return erpProductUserMappingQuery.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return 0;

	}

	@Transactional
	public void removeWishListItem(ErpProductUserMapping erpProductUserMapping) {
		deleteErpProductUserMapping(erpProductUserMapping);
		this.person.getWishlist().remove(erpProductUserMapping);
	}

	private Person person;

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	private UserAttributeEntity userAttributeEntity;

	public UserAttributeEntity getUserAttributeEntity() {
		return userAttributeEntity;
	}

	public void setUserAttributeEntity(UserAttributeEntity userAttributeEntity) {
		this.userAttributeEntity = userAttributeEntity;
	}

	private Address address;

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	private Address businessAddress;

	public Address getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(Address businessAddress) {
		this.businessAddress = businessAddress;
	}

//==========================================================================

	private boolean value1;
	private boolean value2;

	public boolean isValue1() {
		return value1;
	}

	public void setValue1(boolean value1) {
		this.value1 = value1;
	}

	public boolean isValue2() {
		return value2;
	}

	public void setValue2(boolean value2) {
		this.value2 = value2;
	}

	public void addMessage() {
		String summary = value2 ? "Checked" : "Unchecked";
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(summary));
	}

	public String getRestwork() {
		return userData.getBaseHostName();
	}

	public String getGeoserverURL() {
		return this.getRestwork() + "/geoserver";
	}

	public String getGeoazulURL() {
		return this.getRestwork() + "/app";
	}

	public String getKeyCloakURL() {
		return this.getRestwork() + "/auth";
	}

	// ===========================================================================

	public Person findUserById(String urlString) {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();

			Root<Person> root;
			CriteriaQuery<Person> criteria = builder.createQuery(Person.class);
			root = criteria.from(Person.class);
			TypedQuery<Person> query = entityManager
					.createQuery(criteria.select(root).where(getSearchUserPredicates(root, urlString, entityManager)));

			List<Person> saidaUser = query.getResultList();

			try {

				if (saidaUser != null) {
					return saidaUser.get(0);
				} else {
					return null;
				}

			} catch (Exception e) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
				return null;
			}

		} catch (Exception e) {
			return null;
		}
	}

	private Predicate[] getSearchUserPredicates(Root<Person> root, String urlString, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

//		if (urlString != null) {
//			predicatesList.add(builder.equal(
//					root.get("uriRedirectName"),
//					urlString));
//		}
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	/*
	 * Support updating and deleting Person entities
	 */

	@Transactional
	public String updateTest() {
		try {




			Address address = new Address();
			address.setId(1);
			// address.setAddres1("TESTE");
			// this.person.addAddress(address);

			entityManager.merge(this.person);
			entityManager.flush();
			return "search?faces-redirect=true";

		} catch (Exception ex) {
			return "search?faces-redirect=true";
		}

	}

	@Transactional
	public void updateAccountRet() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);

		entityManager.merge(this.person);
		entityManager.flush();

		facesContext.addMessage(null, new FacesMessage("SUCESSO", "Perfil Alterado!"));

		redirect("/security/user/search");

	}

	@Transactional
	public String updateAccount() {
		try {

			entityManager.merge(this.person);
			entityManager.flush();
		
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Sucesso", "Perfil Alterado!"));
		return "profile_ep?faces-redirect=true";

	}

	@Transactional
	public String update() {
		try {

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro", "Um erro ocorreu!"));

			return "register_error?faces-redirect=true";
		}
		return attribName;

	}

	@Transactional
	public void deleteAttrib(ActionEvent actionEvent) {
		try {




			// RealmAttributeEntity realmAttributeEntity = (RealmAttributeEntity)
			// actionEvent.getComponent().getAttributes()
			// .get("attribid");

			// Query query = entityManager.createNamedQuery(RealmAttributeEntity.DELETE);
			// query.setParameter("realmAttributeEntity", realmAttributeEntity);
			// query.executeUpdate();

			// TRUST-----DELETED---------------------");

			// entityManager.flush();

			// this.realmEntity.getAttributes().remove(realmAttributeEntity);

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Referência Removida!");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}

	}

	@Transactional
	public void delete() {

		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);

		Person deletableEntity = findById(getId());

		entityManager.remove(deletableEntity);
		entityManager.flush();

		facesContext.addMessage(null, new FacesMessage("SUCESSO", "Usuário Excluido!"));

		redirect("/security/user/search");

	}

	/*
	 * Support searching Person entities with pagination
	 */

	private int page;
	private long count;
	private List<Person> pageItems;

	private Person example = new Person();
	private Address exaddress = new Address();
	private Address exbusinessaddress = new Address();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public Person getExample() {
		return this.example;
	}

	public void setExample(Person example) {
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
			Root<Person> root = countCriteria.from(Person.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();

			// Populate this.pageItems

			CriteriaQuery<Person> criteria = builder.createQuery(Person.class);
			root = criteria.from(Person.class);
			TypedQuery<Person> query = entityManager.createQuery(criteria.select(root)
					.where(getSearchPredicates(root, entityManager)).orderBy(builder.asc(root.get("id"))));
			query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
			this.pageItems = query.getResultList();

		} catch (Exception e) {

		}

	}

	private Predicate[] getSearchPredicates(Root<Person> root, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String username = this.example.getUsername();

		if (username != null && !"".equals(username)) {

			predicatesList
					.add(builder.like(builder.lower(root.<String>get("username")), '%' + username.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Person> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back Person entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Person> getAll() {
		try {
			CriteriaQuery<Person> criteria = entityManager.getCriteriaBuilder().createQuery(Person.class);
			return entityManager.createQuery(criteria.select(criteria.from(Person.class))).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private Person add = new Person();

	public Person getAdd() {
		return this.add;
	}

	public Person getAdded() {
		Person added = this.add;
		this.add = new Person();
		return added;
	}

	@Transactional
	public void importIMAGE(FileUploadEvent event) throws Exception {


		try {

			String imageUrl = null;

			try {
				imageUrl = copyFileImage(event.getFile().getFileName(), event.getFile().getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			TypedQuery<Long> queryModule = entityManager.createNamedQuery(UserAttributeEntity.FIND_COUNT, Long.class);

			queryModule.setParameter("userid", this.person.getId());
			queryModule.setParameter("name", "picture");


			if (queryModule.getSingleResult() > 0) {

				entityManager.createNamedQuery(UserAttributeEntity.UPDATE_ATTRIB_NAME)
						.setParameter("userid", this.person.getId()).setParameter("name", "picture")
						.setParameter("value", imageUrl).executeUpdate();

				entityManager.flush();

				TypedQuery<UserAttributeEntity> queryModuleNew = entityManager
						.createNamedQuery(UserAttributeEntity.FIND_ATTRIB, UserAttributeEntity.class);

				queryModuleNew.setParameter("userid", this.person.getId());
				queryModuleNew.setParameter("name", "picture");

				UserAttributeEntity newat = queryModuleNew.getSingleResult();

				newat.setValue(imageUrl);

				this.person.updateAttribute(newat);

			} else {

				UserAttributeEntity newAttrib = new UserAttributeEntity();
				newAttrib.setName("picture");
				newAttrib.setValue(attribValue);
				newAttrib.setUser(this.person);
				newAttrib.setMimeType(this.attributeTypeChose.image);
				newAttrib.setTemp(false);
				entityManager.persist(newAttrib);
				this.person.addAttribute(newAttrib);
				entityManager.flush();

			}

			FacesMessage msg = new FacesMessage("Sucesso", "Imagem Enviada!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ClientOAuthEntity findClientOAuthEntity(String realmID) {
		try {
			TypedQuery<ClientOAuthEntity> query = entityManager
					.createNamedQuery(ClientOAuthEntity.OAUTH_CLIENT_ID_REALM, ClientOAuthEntity.class);
			query.setParameter("clientId", "admin-cli");
			query.setParameter("realmId", realmID);
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Enumeration for the different types of attributes
	 */

	private MimeType attributeTypeChose = MimeType.text;

	public MimeType getAttributeTypeChose() {
		return attributeTypeChose;
	}

	public void setAttributeTypeChose(MimeType attributeTypeChose) {
		this.attributeTypeChose = attributeTypeChose;
	}

	private String attribName;

	public String getAttribName() {
		return attribName;
	}

	public void setAttribName(String attribName) {
		this.attribName = attribName;
	}

	private String attribValue;;

	public String getAttribValue() {
		return attribValue;
	}

	public void setAttribValue(String attribValue) {
		this.attribValue = attribValue;
	}

	private boolean skip;

	public void newAttribute() {
		this.attributeTypeChose = null;
		this.attribName = null;
		this.attribValue = null;
	}

	public boolean isSkip() {
		return skip;
	}

	public void setSkip(boolean skip) {
		this.skip = skip;
	}

	public void validateName() {
		try {
			TypedQuery<Long> queryModule = entityManager.createNamedQuery(UserAttributeEntity.FIND_COUNT_NTEMP,
					Long.class);
			queryModule.setParameter("userid", this.person.getId());
			queryModule.setParameter("name", this.attribName);
			if (queryModule.getSingleResult() > 0) {
				FacesContext.getCurrentInstance().validationFailed();
				FacesMessage msg = new FacesMessage("Erro", "Attributo  já existe: ");
				FacesContext.getCurrentInstance().addMessage(null, msg);
			}
		} catch (Exception e) {
			FacesMessage msg = new FacesMessage("Erro 0743", "Tente Novamente!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}
	}

	@Transactional
	public void removeAttribute(Long id) {
		try {
			UserAttributeEntity deletableAttrib = entityManager.find(UserAttributeEntity.class, id);
			this.person.removeAttribute(deletableAttrib);
			entityManager.flush();
			FacesMessage msg = new FacesMessage("Lembrete", "Salvar para Aplicar!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			// txn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage msg = new FacesMessage("Erro", "Erro Inesperado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
		}

	}

	public void saveAttrib() {
		saveAttribute(true);
	}

	public void confirmAttrib() {
		this.person.addAttribute(this.userAttributeEntity);
		this.attributeTypeChose = MimeType.text;
	}

	@Transactional
	public UserAttributeEntity saveAttribute(boolean personUpdate) {
		try {
			try {
				TypedQuery<UserAttributeEntity> queryModule = entityManager
						.createNamedQuery(UserAttributeEntity.FIND_ATTRIB_TEMP, UserAttributeEntity.class);
				queryModule.setParameter("userid", this.person.getId());
				queryModule.setParameter("name", this.attribName);

				UserAttributeEntity updateAttrib = queryModule.getSingleResult();
				updateAttrib.setValue(attribValue);

				FacesMessage msg = new FacesMessage("Sucesso", "Attributo  Adicionado: " + this.attribName);
				FacesContext.getCurrentInstance().addMessage(null, msg);


				entityManager.merge(updateAttrib);
				entityManager.flush();

				updateAttrib.setTemp(false);

				if (personUpdate) {
					this.person.updateAttribute(updateAttrib);
				}
				this.attributeTypeChose = MimeType.text;
				return updateAttrib;

			} catch (Exception e) {

				UserAttributeEntity newAttrib = new UserAttributeEntity();
				newAttrib.setName(attribName);
				newAttrib.setValue(attribValue);
				newAttrib.setUser(this.person);
				newAttrib.setMimeType(this.attributeTypeChose);
				newAttrib.setTemp(true);
				FacesMessage msg = new FacesMessage("Sucesso", "Attributo  Adicionado: " + this.attribName);
				FacesContext.getCurrentInstance().addMessage(null, msg);


				entityManager.persist(newAttrib);
				entityManager.flush();

				newAttrib.setTemp(false);
				if (personUpdate) {
					this.person.addAttribute(newAttrib);
				}

				return newAttrib;
			}

			// KeycloakAdminClient keycloakAdminClientExample = new KeycloakAdminClient();

			// ApplicationIdentityEntity abstractIdentityEntity = null;

			// TypedQuery<ApplicationIdentityEntity> query = session
			// .createNamedQuery(ApplicationIdentityEntity.DEFAULT_APP,
			// ApplicationIdentityEntity.class);
			// query.setParameter("locale", activeLocale.getValue().toString());
			// query.setParameter("dtype", "/");
			// List<ApplicationIdentityEntity> abstractIdentityEntity2 =
			// query.getResultList();
			// if (abstractIdentityEntity2.size() > 0) {
			// abstractIdentityEntity = abstractIdentityEntity2.get(0);

			// String updateUserRepresentation =
			// keycloakAdminClientExample.updateUser(
			// this.person.getUuid().toString(),
			// newAttrib,
			// userData.getRealmEntity(),
			// abstractIdentityEntity.getClientEntity().getSecret(),
			// abstractIdentityEntity.getClientEntity().getPassword(),
			// abstractIdentityEntity.getClientEntity().getServerName());
			// }

			// Return on default in wizard

		} catch (Exception e) {
			e.printStackTrace();
			FacesMessage msg = new FacesMessage("Erro", "Erro Inesperado!");
			FacesContext.getCurrentInstance().addMessage(null, msg);
			return null;
		}

	}

	public String onFlowProcess(FlowEvent event) {






		switch (this.attributeTypeChose) {
		case image:

			if (event.getOldStep().equals("upload")) {
				return "confirm";
			} else {
				skip = true;
				return "upload";
			}
		default:
			skip = false;
			return event.getNewStep();
		}
	}

	public void importImgAttrib(FileUploadEvent event) {
		FacesMessage msg = new FacesMessage("Sucesso", event.getFile().getFileName() + " Enviado!");
		FacesContext.getCurrentInstance().addMessage(null, msg);
		try {
			copyFileImage(event.getFile().getFileName(), event.getFile().getInputStream());

			userAttributeEntity = saveAttribute(false);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	String copyFileImage(String fileName, InputStream in) throws Exception {

		try {
			Random random = new Random();
			int randomInt = random.nextInt(1000000000);
			LocalDateTime data = LocalDateTime.now();

			String fileNameOnly = data.toString() + "-" + String.valueOf(randomInt) + "_" + fileName;
			String appDirectory = save_FILE_PATH + "/files/" + userData.getRealmEntity() + "/security/";
			File appDir = new File(appDirectory);
			if (!appDir.exists()) {
				appDir.mkdir();
			}
			String fileNameNew = appDirectory + "/" + fileNameOnly;
			String fileURI = userData.getGeoazulURL() + "/files/" + userData.getRealmEntity() + "/security/"
					+ fileNameOnly;

			OutputStream out = new FileOutputStream(new File(fileNameNew));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = in.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}

			in.close();
			out.flush();
			out.close();

			this.setAttribValue(fileURI);
			return fileURI;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private Address companyAddress = new Address();
	private String country = new String();

	public Address getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(Address companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	private States state;

	public States getState() {
		return state;
	}

	public void setState(States state) {
		this.state = state;
	}

	private Cities city;

	public Cities getCity() {
		return city;
	}

	public void setCity(Cities city) {
		this.city = city;
	}

	@Transactional
	public List<Countries> getCountries() {
		try {
			TypedQuery<Countries> query = entityManager.createNamedQuery(Countries.FIND_ALL, Countries.class);
			List<Countries> countries = query.getResultList();
			return countries;
		} catch (Exception ignore) {
		}
		return null;
	}

	private List<States> states;

	public List<States> getStates() {
		try {
			Optional<Countries> hasCountry = Optional.ofNullable(this.person.getCountry());
			if (hasCountry.isPresent()) {
				TypedQuery<States> query5 = entityManager.createNamedQuery(States.FIND_BY_COUNTRY, States.class);
				query5.setParameter("country", hasCountry.get().getId());
				return query5.getResultList();
			} else {
				TypedQuery<States> query5 = entityManager.createNamedQuery(States.FIND_BY_COUNTRY, States.class);
				query5.setParameter("country", this.getCountries().get(0).getId());
				states = query5.getResultList();
				return states;
			}
		} catch (Exception ex) {
			return new ArrayList<States>();
		}

	}

	public void setStates(List<States> states) {
		this.states = states;
	}

	public void onCountryChange() {
		try {
			TypedQuery<States> query5 = entityManager.createNamedQuery(States.FIND_BY_COUNTRY, States.class);
			query5.setParameter("country", this.person.getCountry().getId());
			states = query5.getResultList();
			this.person.setState(null);
			this.person.setCity(null);
		} catch (Exception ignore) {
		}
	}

	public void onStateChange() {
		try {
			TypedQuery<Cities> query = entityManager.createNamedQuery(Cities.FIND_BY_STATE, Cities.class);
			query.setParameter("state", this.person.getState().getId());
			cities = query.getResultList();
			this.setCity(null);
		} catch (Exception ignore) {
		}
	}

	private List<Cities> cities;

	public List<Cities> getCities() {
		Optional<States> hasState = Optional.ofNullable(this.person.getState());
		if (hasState.isPresent()) {
			TypedQuery<Cities> query = entityManager.createNamedQuery(Cities.FIND_BY_STATE, Cities.class);
			query.setParameter("state", this.person.getState().getId());
			return query.getResultList();
		}
		return cities;
	}

	public void setCities(List<Cities> cities) {
		this.cities = cities;
	}

}