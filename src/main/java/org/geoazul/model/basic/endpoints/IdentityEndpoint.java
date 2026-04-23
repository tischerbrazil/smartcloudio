package org.geoazul.model.basic.endpoints;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import org.geoazul.model.app.AbstractIdentityEntity;
import org.geoazul.model.app.AppUserMappingEntity;
import org.keycloak.example.oauth.UserData;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

/** 
 * 
 */

@Path("/oauth2")
public class IdentityEndpoint implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	EntityManager entityManager;

	@Inject
	private UserData userData;

	private Predicate[] getSearchPredicates33(Root<AppUserMappingEntity> root33, EntityManager session) {

		CriteriaBuilder builder33 = session.getCriteriaBuilder();
		List<Predicate> predicatesList33 = new ArrayList<Predicate>();

		Long userFilter33 = userData.getPersonId();

		if (userFilter33 != null) {
			predicatesList33.add(builder33.equal(root33.get("userId"), userFilter33));
		}

		return predicatesList33.toArray(new Predicate[predicatesList33.size()]);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/identity/")
	public Response listAll() {

		try {

			CriteriaBuilder builder33 = entityManager.getCriteriaBuilder();

			// Populate this.count

			CriteriaQuery<Long> countCriteria33 = builder33.createQuery(Long.class);
			Root<AppUserMappingEntity> root33 = countCriteria33.from(AppUserMappingEntity.class);
			countCriteria33 = countCriteria33.select(builder33.count(root33))
					.where(getSearchPredicates33(root33, entityManager));
			Long count33 = entityManager.createQuery(countCriteria33).getSingleResult();
			// Populate this.pageItems

			CriteriaQuery<AppUserMappingEntity> criteria33 = builder33.createQuery(AppUserMappingEntity.class);
			root33 = criteria33.from(AppUserMappingEntity.class);
			TypedQuery<AppUserMappingEntity> query33 = entityManager
					.createQuery(criteria33.select(root33).where(getSearchPredicates33(root33, entityManager)));
			// int page = 1;
			// int pageSize = 10;
			// query33.setFirstResult(page * pageSize).setMaxResults(pageSize);

			List<AppUserMappingEntity> results = query33.getResultList();

			List<AbstractIdentityEntity> apps = new ArrayList<AbstractIdentityEntity>();

			for (AppUserMappingEntity app : results) {
				apps.add(app.getApp());
			}



			return Response.ok(apps, MediaType.APPLICATION_JSON).build();
		} catch (Exception e) {
			return null;
		}

	}

}