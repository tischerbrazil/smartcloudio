package org.geoazul.model.basic.endpoints;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import jakarta.ws.rs.core.UriBuilder;
import org.geoazul.model.basic.AbstractGeometry;


/**
 * 
 */


@Path("/oauth2")
public class AbstractGeometryEndpoint implements Serializable {

	private static final long serialVersionUID = 1L;
	
	//@Inject
	//private UserData userData;
	
	@Inject
	EntityManager entityManager;
	
	@POST
	@Consumes("application/json")
	@Path("/geometries")
	public Response create(AbstractGeometry entity) {
		//em.persist(entity);
		return Response.created(
				UriBuilder.fromResource(AbstractGeometryEndpoint.class)
						.path(String.valueOf(entity.getId())).build()).build();
	}

	@DELETE
	@Path("/geometries/{id}")
	public Response deleteById(@PathParam("id") String id) {
		
	//	AbstractGeometry entity = em.find(AbstractGeometry.class, id);
	//	if (entity == null) {
	//		return Response.status(Status.NOT_FOUND).build();
	//	}
	//	em.remove(entity);
		return Response.noContent().build();
	}

	@GET
	@Path("/geometries/{id}")
	@Produces("application/json")
	@PermitAll
	public Response findById(@PathParam("id") String id) {
		
		
		
	
		try {

		
		TypedQuery<AbstractGeometry> findByIdQuery = entityManager
				.createQuery(
						"SELECT DISTINCT a FROM AbstractGeometry a "
						+ "WHERE a.uuid = :entityId",
						AbstractGeometry.class);
		findByIdQuery.setParameter("entityId", UUID.fromString(id));
		AbstractGeometry entity;
		try {
			entity = findByIdQuery.getSingleResult();
		} catch (NoResultException nre) {
			entity = null;
		}
		if (entity == null) {
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok(entity).build();
		
		} catch (Exception e) {
			return null;
		}
	}

	@GET
	@Produces("application/json")
	@Path("/geometries")
	public List<AbstractGeometry> listAll(
			@QueryParam("start") Integer startPosition,
			@QueryParam("max") Integer maxResult) {
		

		try {
		  
		 
		
		TypedQuery<AbstractGeometry> findAllQuery = entityManager
				.createQuery(
						"SELECT DISTINCT a FROM AbstractGeometry a "
					
						+ "ORDER BY a.id  ",
						AbstractGeometry.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}else {
			findAllQuery.setMaxResults(10);
		}
		
		final List<AbstractGeometry> results = findAllQuery.getResultList();
		
		
		return results;
		
						
		} catch (Exception e) {
		
				return null;
		
		}
	}

	@PUT
	@Path("/geometries/{id:[0-9][0-9]*}")
	@Consumes("application/json")
	public Response update(@PathParam("id") Long id, AbstractGeometry entity) {
		if (entity == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (id == null) {
			return Response.status(Status.BAD_REQUEST).build();
		}
		if (!id.equals(entity.getId())) {
			return Response.status(Status.CONFLICT).entity(entity).build();
		}
	///	if (em.find(AbstractGeometry.class, id) == null) {
	//		return Response.status(Status.NOT_FOUND).build();
	//	}
		//try {
		//	entity = em.merge(entity);
		//} catch (OptimisticLockException e) {
		//	return Response.status(Response.Status.CONFLICT)
		//			.entity(e.getEntity()).build();
		//}

		return Response.noContent().build();
	}
}
