package org.geoazul.view.security;

import static java.util.Optional.ofNullable;
import static org.omnifaces.util.Faces.redirect;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.event.ActionEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.geoazul.model.security.ClientOAuthEntity;
import org.geoazul.model.security.RealmAttributeEntity;
import org.geoazul.model.security.RealmEntity;
import org.geoazul.model.security.RoleEntity;
import org.omnifaces.cdi.Param;
import org.omnifaces.cdi.ViewScoped;
import org.primefaces.refact.RealmJpaLazyDataModel;

@Named
@ViewScoped
public class RealmEntityBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@PreDestroy
	public void preDestroy() {
	}

	@Inject
	private EntityManager entityManager;

	private RealmAttributeEntity realmAttributeEntity;

	public RealmAttributeEntity getRealmAttributeEntity() {
		return realmAttributeEntity;
	}
	
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

	public void setRealmAttributeEntity(RealmAttributeEntity realmAttributeEntity) {
		this.realmAttributeEntity = realmAttributeEntity;
	}

	public void editAction(RealmAttributeEntity realmAttributeEntity) {
		this.realmAttributeEntity = realmAttributeEntity;
	}

	@Transactional
	public void deleteAttrib(ActionEvent actionEvent) {
		try {

			RealmAttributeEntity realmAttributeEntity = (RealmAttributeEntity) actionEvent.getComponent()
					.getAttributes().get("attribid");

			Query query = entityManager.createNamedQuery(RealmAttributeEntity.DELETE);
			query.setParameter("realmAttributeEntity", realmAttributeEntity);
			query.executeUpdate();


			entityManager.flush();

			this.realmEntity.getAttributes().remove(realmAttributeEntity);

			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "SUCESSO", "Referência Removida!");
			FacesContext.getCurrentInstance().addMessage(null, message);

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
		}

	}

	
	private RealmEntity realmEntity;

	public RealmEntity getRealmEntity() {
		return this.realmEntity;
	}

	public void setRealmEntity(RealmEntity realmEntity) {
		this.realmEntity = realmEntity;
	}

	private List<RoleEntity> roles = new ArrayList<>();

	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}

	public String create() {
		return "create?faces-redirect=true";
	}

	private RealmJpaLazyDataModel<RealmEntity> model;
	
	@PostConstruct
	public void init() {
		
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}


		if (this.id == null) {
			this.realmEntity = new RealmEntity();
			model = new RealmJpaLazyDataModel<>(RealmEntity.class, () -> entityManager);
		} else {
			this.realmEntity = findById(Long.valueOf(id));
			if (realmEntity == null) {
				throw new EntityNotFoundException("Realm not found.");
			}
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

	public Optional<RealmEntity> findById(String id) {
		TypedQuery<RealmEntity> query = entityManager.createNamedQuery(RealmEntity.REALM_ID_GET, RealmEntity.class);
		query.setParameter("id", Long.valueOf(id));
		return getOptionalSingleResult(query);
	}

	@SuppressWarnings("hiding")
	public static <RealmEntity> Optional<RealmEntity> getOptionalSingleResult(TypedQuery<RealmEntity> typedQuery) {
		return ofNullable(getSingleResultOrNull(typedQuery));
	}

	/**
	 * Returns single result of given typed query, or <code>null</code> if there is
	 * none.
	 * 
	 * @param <T>        The generic result type.
	 * @param typedQuery The involved typed query.
	 * @return Single result of given typed query, or <code>null</code> if there is
	 *         none.
	 * @throws NonUniqueResultException When there is no unique result.
	 */
	public static <T> T getSingleResultOrNull(TypedQuery<T> typedQuery) {
		try {
			return typedQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public RealmEntity findById(Long id) {
		try {
			this.realmEntity = entityManager.find(RealmEntity.class, Long.valueOf(id));
			TypedQuery<RealmEntity> queryApp = entityManager.createNamedQuery(RealmEntity.REALM_ID_GET,
					RealmEntity.class);
			queryApp.setParameter("id", id);
			return queryApp.getSingleResult();
		} catch (Exception ex) {
			return null;
		}
	}

	public RealmEntity findRealmDefault() {
		TypedQuery<RealmEntity> queryApps = entityManager.createNamedQuery(RealmEntity.REALM_DEFAULT,
				RealmEntity.class);
		return queryApps.getSingleResult();
	}

	public RealmEntity findRealmById(String urlString) {
		try {
			CriteriaBuilder builder = entityManager.getCriteriaBuilder();

			Root<RealmEntity> root;
			CriteriaQuery<RealmEntity> criteria = builder.createQuery(RealmEntity.class);
			root = criteria.from(RealmEntity.class);
			TypedQuery<RealmEntity> query = entityManager
					.createQuery(criteria.select(root).where(getSearchRealmPredicates(root, urlString, entityManager)));

			List<RealmEntity> saidaRealm = query.getResultList();

			try {

				if (saidaRealm != null) {
					return saidaRealm.get(0);
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

	private Predicate[] getSearchRealmPredicates(Root<RealmEntity> root, String urlString, EntityManager session) {
		List<Predicate> predicatesList = new ArrayList<Predicate>();
		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	@Transactional
	public String delete() {
		try {
			RealmEntity deletableEntity = findById(Long.valueOf(id));
			entityManager.remove(deletableEntity);
			entityManager.flush();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("SUCESSO!", "Realm Excluído!"));
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("ERRO!", "Realm não Excluído!"));
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(e.getMessage()));
			return "search?faces-redirect=true";
		}
	}

	/*
	 * Support updating and deleting RealmEntity entities
	 */

	@Transactional
	public void update() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		try {
			entityManager.merge(this.realmEntity);
			facesContext.addMessage(null, new FacesMessage("SUCESSO!", "REALM ALTERADO!"));
			redirect("/security/realm/search");
		} catch (Exception e) {
			facesContext.addMessage(null, new FacesMessage("SUCESSO!", "REALM NÃO ALTERADO!"));
			redirect("/security/realm/search");
		}
	}

	/*
	 * Support searching RealmEntity entities with pagination
	 */

	private int page;
	private long count;
	private List<RealmEntity> pageItems;

	private RealmEntity example = new RealmEntity();

	public int getPage() {
		return this.page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public RealmEntity getExample() {
		return this.example;
	}

	public void setExample(RealmEntity example) {
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
			Root<RealmEntity> root = countCriteria.from(RealmEntity.class);
			countCriteria = countCriteria.select(builder.count(root)).where(getSearchPredicates(root, entityManager));
			this.count = entityManager.createQuery(countCriteria).getSingleResult();

			// Populate this.pageItems

			CriteriaQuery<RealmEntity> criteria = builder.createQuery(RealmEntity.class);
			root = criteria.from(RealmEntity.class);
			TypedQuery<RealmEntity> query = entityManager
					.createQuery(criteria.select(root).where(getSearchPredicates(root, entityManager)));
			query.setFirstResult(this.page * getPageSize()).setMaxResults(getPageSize());
			this.pageItems = query.getResultList();

		} catch (Throwable e) {

		}
	}

	private Predicate[] getSearchPredicates(Root<RealmEntity> root, EntityManager session) {

		CriteriaBuilder builder = session.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String name = this.example.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(builder.lower(root.<String>get("name")), '%' + name.toLowerCase() + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<RealmEntity> getPageItems() {
		return this.pageItems;
	}

	public long getCount() {
		return this.count;
	}

	/*
	 * Support listing and POSTing back RealmEntity entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<RealmEntity> getAll() {

		try {
			CriteriaQuery<RealmEntity> criteria = entityManager.getCriteriaBuilder().createQuery(RealmEntity.class);
			return entityManager.createQuery(criteria.select(criteria.from(RealmEntity.class))).getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private RealmEntity add = new RealmEntity();

	public RealmEntity getAdd() {
		return this.add;
	}

	public RealmEntity getAdded() {
		RealmEntity added = this.add;
		this.add = new RealmEntity();
		return added;
	}

	public RealmJpaLazyDataModel<RealmEntity> getModel() {
		return model;
	}

	public void setModel(RealmJpaLazyDataModel<RealmEntity> model) {
		this.model = model;
	}

}