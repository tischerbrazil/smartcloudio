package org.geoazul.view.website;

import static modules.LoadInitParameter.save_FILE_PATH;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.geoazul.view.utils.ConvertToASCII2;
import org.keycloak.example.oauth.UserData;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.MatchMode;
import org.primefaces.model.filter.GlobalFilterConstraint;
import org.primefaces.refact.ManufacturerJpaLazyDataModel;
import com.erp.modules.inventory.entities.Manufacturer;

@Named
@ViewScoped
public class ManufacturerBean implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	private List<FilterMeta> filterBy;
	
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
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Inject
	EntityManager entityManager;

	private ManufacturerJpaLazyDataModel<Manufacturer> model;

	private List<Manufacturer> filteredManufacturers;

	private String globalSearch;

	public String getGlobalSearch() {
		return globalSearch;
	}

	public void setGlobalSearch(String globalSearch) {
		this.globalSearch = globalSearch;
	}

	static final String GLOBAL_FILTER_KEY = "globalFilter";

	

	public void processSearch() {

		GlobalFilterConstraint dd = new org.primefaces.model.filter.GlobalFilterConstraint();

		FilterMeta filter = FilterMeta.builder().constraint(dd).field(GLOBAL_FILTER_KEY).filterBy(null)
				.filterValue(globalSearch).matchMode(MatchMode.GLOBAL).build();







		model = new ManufacturerJpaLazyDataModel<Manufacturer>(Manufacturer.class, () -> entityManager, filter);
	}

	@PostConstruct
	public void init() {
		model = new ManufacturerJpaLazyDataModel<>(Manufacturer.class, () -> entityManager);
	}

	public List<Manufacturer> getFilteredManufacturers() {
		return filteredManufacturers;
	}

	public void setFilteredManufacturers(List<Manufacturer> filteredManufacturers) {
		this.filteredManufacturers = filteredManufacturers;
	}

	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(Manufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

	public void setModel(ManufacturerJpaLazyDataModel<Manufacturer> model) {
		this.model = model;
	}

	public ManufacturerBean() {
		model = new ManufacturerJpaLazyDataModel<>(Manufacturer.class, () -> entityManager);

	}

	public LazyDataModel getModel() {
		return model;
	}

	@Inject
	private HttpServletRequest request;

	@Inject
	private UserData userData;

	public Manufacturer findById(Long idCapture) {
		
		try {
			TypedQuery<Manufacturer> queryAppLayer = entityManager.createNamedQuery(Manufacturer.FIND_BY_ID,
					Manufacturer.class);
			queryAppLayer.setParameter("id", idCapture);
			Manufacturer Manufacturer = queryAppLayer.getSingleResult();
			return Manufacturer;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	private Manufacturer manufacturer;

	public void editAction(Long id) {
		this.manufacturer = this.findById(id);
	}

	public void newAction() {
		this.manufacturer = new Manufacturer();
	}

	protected Integer getParamId(String param) {
		FacesContext context = FacesContext.getCurrentInstance();
		Map<String, String> map = context.getExternalContext().getRequestParameterMap();
		return Integer.valueOf(map.get(param));
	}

	@Transactional
	public void removeImage() {
		try {

			Manufacturer manufacturerDelete = entityManager.find(Manufacturer.class, getParamId("itemId"));
			manufacturerDelete.setImage(null);
			entityManager.merge(manufacturerDelete);
			entityManager.flush();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "IMAGEM REMOVIDA!"));
		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO", "ERRO INESPERADO 5789!"));
		}
	}

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
			}

			manufacturer.setImage("/files/" + userData.getRealmEntity() + "/media/" + fileNameOnly);

			entityManager.merge(manufacturer);

			entityManager.flush();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	

	@Transactional
	public void update() {
	
		FacesMessage message = null;
		try {
			if (this.id == null) {

				entityManager.persist(manufacturer);
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Fabricante Adicionado!");

			} else {
				entityManager.merge(manufacturer);
				message = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Fabricante Alterado!");
			}
			entityManager.flush();
			
			
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}
	}

	public List<FilterMeta> getFilterBy() {
		return filterBy;
	}

	public void setFilterBy(List<FilterMeta> filterBy) {
		this.filterBy = filterBy;
	}

}