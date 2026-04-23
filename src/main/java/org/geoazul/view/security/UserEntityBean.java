package org.geoazul.view.security;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
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
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.geoazul.erp.Cities;
import org.geoazul.erp.Countries;
import org.geoazul.erp.States;
import org.example.kickoff.view.ActiveLocale;
import org.example.kickoff.view.ActiveUser;
import org.geoazul.model.Address;
import org.geoazul.model.app.ApplicationIdentityEntity;
import org.geoazul.model.security.ClientOAuthEntity;
import org.geoazul.model.security.UserAttributeEntity;
import org.geoazul.model.security.UserAttributeEntity.MimeType;
import org.example.kickoff.business.service.PersonService;
import org.example.kickoff.model.Person;
import org.keycloak.example.oauth.UserData;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.FlowEvent;

import com.erp.modules.inventory.entities.ErpProductUserMapping;

import static modules.LoadInitParameter.*;
import static org.omnifaces.util.Faces.redirect;

@Named
@ViewScoped
public class UserEntityBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@PreDestroy
	public void preDestroy() {
	}

	@Inject
	private HttpServletRequest request;

	@Inject
	private ActiveLocale activeLocale;

	@Inject
	EntityManager entityManager;

	@Inject
	private UserData userData;

	private String id;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void retrieve() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (this.id == null) {
			this.person = this.example;
			this.address = this.exaddress;
		} else {
			try {

				
				
				TypedQuery<Person> queryApp = entityManager.createNamedQuery(Person.USER_UUID_GET, Person.class);
				queryApp.setParameter("uuid", UUID.fromString(id));
				this.person =  queryApp.getSingleResult();
				
				
				
				if (this.person.getAddress().getId() != null) {
					this.address = this.person.getAddress();
				} else {
					this.address = this.exaddress;
				}

				// TypedQuery<Person> queryApp =
				// session.createNamedQuery(Person.USER_ID_GET, Person.class);
				// queryApp.setParameter("id", getId());
				// queryApp.setParameter("realmId", userData.getRealmEntity());
				// Person ddd = queryApp.getSingleResult();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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

	@PostConstruct
	public void init() {

		if (activeUser.isPresent()) {
			TypedQuery<Person> queryUser = entityManager.createNamedQuery(Person.USER_UUID_GET, Person.class);
			queryUser.setParameter("uuid", activeUser.get().getUuid());
			List<Person> usersListRet = queryUser.getResultList();
			if (usersListRet.size() > 0) {
				this.person = usersListRet.get(0);
			}
		}
	}

	
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

		
		try {
		
			entityManager.merge(this.person);
			entityManager.flush();
		
	
			FacesContext facesContext = FacesContext.getCurrentInstance();
			Flash flash = facesContext.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			facesContext.addMessage(null, new FacesMessage("Sucesso", "Usuário Alterado!"));
			
			redirect("/application");

		} catch (Exception ex) {
			ex.printStackTrace();
		}

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


		

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Referência Removida!");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}

	}

	@Transactional
	public String delete() {
		try {
			entityManager.remove(this.person);
			entityManager.flush();


			String redirDefault = userData.getRealmEntity();


			ApplicationIdentityEntity abstractIdentityEntity = null;

			TypedQuery<ApplicationIdentityEntity> query = entityManager
					.createNamedQuery(ApplicationIdentityEntity.DEFAULT_APP, ApplicationIdentityEntity.class);
			query.setParameter("locale", activeLocale.getValue().toString());
			query.setParameter("dtype", "/");
			List<ApplicationIdentityEntity> abstractIdentityEntity2 = query.getResultList();
			if (abstractIdentityEntity2.size() > 0) {
				abstractIdentityEntity = abstractIdentityEntity2.get(0);
			}

			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return "search?faces-redirect=true";
		}

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