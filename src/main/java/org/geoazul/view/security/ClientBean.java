package org.geoazul.view.security;

import static java.util.Optional.ofNullable;
import static modules.LoadInitParameter.save_FILE_PATH;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.refact.ClientJpaLazyDataModel;
import org.primefaces.util.LangUtils;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.geoazul.model.security.ClientAttributeEntity;
import org.geoazul.model.security.ClientComponentEntity;
import org.geoazul.model.security.ClientEntity;
import org.geoazul.model.security.ClientIdentityEntity;
import org.geoazul.model.security.ClientMobileEntity;
import org.geoazul.model.security.ClientOAuthEntity;
import org.geoazul.model.security.ClientServiceEntity;
import org.keycloak.example.oauth.UserData;

@Named
@ViewScoped
public class ClientBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@PreDestroy
	public void preDestroy() {
	}

	@Inject
	@Param(pathIndex = 0)
	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private ClientJpaLazyDataModel<ClientEntity> model;

	private List<ClientEntity> clientEntityFilter;

	@PostConstruct
	public void init() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}

	    //globalFilterOnly = false;
	    

		

			if (this.id == null || this.id.isEmpty()) {
				model = new ClientJpaLazyDataModel<>(ClientEntity.class, () -> entityManager);

			} else {
				
				
				switch (this.id) {
				case "oauth":
					clientEntity = new ClientOAuthEntity();
					break;
				case "service":
					clientEntity = new ClientIdentityEntity();
					break;
				case "identity":
					clientEntity = new ClientComponentEntity();
					break;
				case "mobile":
					clientEntity = new ClientServiceEntity();
					break;
				default:
				
				
				this.clientEntity = findById(Long.valueOf(id));
				if (clientEntity == null) {
					// REDIRECT
				}

				break;
			}

		}
	}

	 private boolean globalFilterOnly;
	 
	 public boolean isGlobalFilterOnly() {
	        return globalFilterOnly;
	    }

	    public void setGlobalFilterOnly(boolean globalFilterOnly) {
	        this.globalFilterOnly = globalFilterOnly;
	    }
	
	 public void toggleGlobalFilter() {
	        setGlobalFilterOnly(!isGlobalFilterOnly());
	    }
	 
	public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
		if (LangUtils.isBlank(filterText)) {
			return true;
		}
		
		return true;
	}

	private int getInteger(String string) {
		try {
			return Integer.parseInt(string);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	@Inject
	private UserData userData;

	@Inject
	EntityManager entityManager;

	@SuppressWarnings("hiding")
	public static <ClientEntity> Optional<ClientEntity> getOptionalSingleResult(TypedQuery<ClientEntity> typedQuery) {
		return ofNullable(getSingleResultOrNull(typedQuery));
	}

	public static <T> T getSingleResultOrNull(TypedQuery<T> typedQuery) {
		try {
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public ClientEntity findById(Long id) {
		try {
			this.clientEntity = entityManager.find(ClientEntity.class, Long.valueOf(id));
			TypedQuery<ClientEntity> queryApp = entityManager.createNamedQuery(ClientEntity.CLIENT_GET_BY_ID,
					ClientEntity.class);
			queryApp.setParameter("id", id);
			return queryApp.getSingleResult();
		} catch (Exception ex) {
			return null;
		}
	}

	private String attribValue;;

	public String getAttribValue() {
		return attribValue;
	}

	public void setAttribValue(String attribValue) {
		this.attribValue = attribValue;
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

			TypedQuery<Long> queryModule = entityManager.createNamedQuery(ClientAttributeEntity.FIND_COUNT, Long.class);

			queryModule.setParameter("id", this.clientEntity.getId());
			queryModule.setParameter("name", "picture");


			if (queryModule.getSingleResult() > 0) {

				entityManager.createNamedQuery(ClientAttributeEntity.UPDATE_ATTRIB_NAME)
						.setParameter("id", this.clientEntity.getId()).setParameter("name", "picture")
						.setParameter("value", imageUrl).executeUpdate();

				entityManager.flush();

				TypedQuery<ClientAttributeEntity> queryModuleNew = entityManager
						.createNamedQuery(ClientAttributeEntity.FIND_ATTRIB, ClientAttributeEntity.class);

				queryModuleNew.setParameter("id", this.clientEntity.getId());
				queryModuleNew.setParameter("name", "picture");

				ClientAttributeEntity newat = queryModuleNew.getSingleResult();

				newat.setValue(imageUrl);

				this.clientEntity.updateAttribute(newat);


				// this.clientEntity.getAttribute("picture"));

			} else {

				org.geoazul.model.security.ClientAttributeEntity newAttrib = new org.geoazul.model.security.ClientAttributeEntity();

				// newAttrib.setUuid(UUID.randomUUID());
				newAttrib.setName("picture");
				newAttrib.setValue(attribValue);
				newAttrib.setClient(clientEntity);
				// newAttrib.setUser(null);
				// newAttrib.setMimeType(this.attributeTypeChose.image);
				// newAttrib.setTemp(false);
				entityManager.persist(newAttrib);
				this.clientEntity.addAttribute(newAttrib);
				entityManager.flush();

			}


			FacesMessage msg = new FacesMessage("Sucesso", "Imagem Enviada!");
			FacesContext.getCurrentInstance().addMessage(null, msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ------------------------

	private String type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	private ClientEntity clientEntity;

	public ClientEntity getClientEntity() {
		return this.clientEntity;
	}

	public void setClientEntity(ClientEntity clientEntity) {
		this.clientEntity = clientEntity;
	}

	public ClientEntity getClientCreate(String type) {
		switch (type) {
		case "oauth":
			return new ClientOAuthEntity();
		case "identity":
			return new ClientIdentityEntity();
		case "mobile":
			return new ClientMobileEntity();
		case "component":
			return new ClientComponentEntity();
		case "service":
			return new ClientServiceEntity();
		default:
			return null;
		}
	}

	public void retrieve() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (this.id == null) {
			this.clientEntity = this.getClientCreate(this.type);
			if (this.clientEntity == null) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage("Erro!", "Type not found!"));
				try {
					context.getExternalContext().redirect("search.xhtml");
				} catch (IOException ex) {
				}
			}
		} else {
			this.clientEntity = findById(Long.valueOf(this.id));
		}
	}

	public Map<String, ClientEntity> mapClientes() {
		try {
			Map<String, ClientEntity> accounts = entityManager
					.createQuery("select a from ClientEntity a", ClientEntity.class).getResultList().stream()
					.collect(Collectors.toMap(ClientEntity::getClientId, Function.identity()));
			return accounts;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return null;
		}
	}

	@Transactional
	public String update() {
		try {
			if (this.id == null) {
				entityManager.persist(this.clientEntity);

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "Cliente Inserido!"));
			} else {
				entityManager.merge(this.clientEntity);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO", "Cliente Alterado!"));
			}
			entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return "search?faces-redirect=true";
		}
	}

	@Transactional
	public String delete() {
		try {
			ClientEntity deletableEntity = findById(Long.valueOf(this.id));
			entityManager.remove(deletableEntity);
			entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return "search?faces-redirect=true";
		}
	}

	/*
	 * Support searching ClientEntity entities with pagination
	 */

	private int page;
	private long count;
	private List<ClientEntity> pageItems;

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
			CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
			Root<ClientEntity> root = countCriteria.from(ClientEntity.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();
			CriteriaQuery<ClientEntity> criteria = builder.createQuery(ClientEntity.class);
			root = criteria.from(ClientEntity.class);
			TypedQuery<ClientEntity> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicates(root, entityManager)));
			query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
			this.pageItems = query.getResultList();
		} catch (Exception e) {

		}
	}

	private Predicate[] getSearchPredicates(Root<ClientEntity> root, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		predicatesList.add(builder.isFalse(root.get("system")));
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<ClientEntity> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back ClientEntity entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<ClientEntity> getAll() {

		try {
			CriteriaQuery<ClientEntity> criteria = entityManager.getCriteriaBuilder().createQuery(ClientEntity.class);
			return entityManager.createQuery(criteria.select(criteria.from(ClientEntity.class))).getResultList();

		} catch (Exception e) {
			return null;
		}
	}

	// ======================= ClientComponentEntity //FIXME

	public List<ClientComponentEntity> getAllComps() {
		try {
			TypedQuery<ClientComponentEntity> query = entityManager
					.createNamedQuery(ClientComponentEntity.COMPONENT_CLIENT_ID, ClientComponentEntity.class);
			query.setParameter("clientId", "geoserver");
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private ClientOAuthEntity add = new ClientOAuthEntity();

	public ClientEntity getAdd() {
		return this.add;
	}

	public ClientEntity getAdded() {
		ClientEntity added = this.add;
		this.add = new ClientOAuthEntity();
		return added;
	}


	public List<ClientEntity> getClientEntityFilter() {
		return clientEntityFilter;
	}

	public void setClientEntityFilter(List<ClientEntity> clientEntityFilter) {
		this.clientEntityFilter = clientEntityFilter;
	}

	public ClientJpaLazyDataModel<ClientEntity> getModel() {
		return model;
	}

	public void setModel(ClientJpaLazyDataModel<ClientEntity> model) {
		this.model = model;
	}

}
