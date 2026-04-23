package org.geoazul.model.basic.endpoints;

import java.io.Serializable;
import java.util.List;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import org.geoazul.model.Names;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import java.security.Principal;
import java.io.File;
import jakarta.ws.rs.core.Response.ResponseBuilder;

/** 
 * 
 */

@Path("/oauth2")
public class ImageEndpoint implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	EntityManager entityManager;

	private static final String FILE_PATH = "/home/tischer/aimg/z.png";

	@GET
	@Path("/imagem/get")
	@Produces("image/png")
	public Response getFile() {

		File file = new File(FILE_PATH);

		ResponseBuilder response = Response.ok((Object) file);
		response.header("Content-Disposition", "attachment; filename=image_from_server.png");
		return response.build();

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/imagem/")
	public Response listAll(@Context HttpServletRequest request) {

		try {
			Principal userPrincipal = request.getUserPrincipal();

		} catch (Exception e12) {
			e12.printStackTrace();
		}

		try {
			
			TypedQuery<Names> findAllQuery = entityManager.createQuery("SELECT DISTINCT c FROM Names c ORDER BY c.id",
					Names.class);

			final List<Names> results = findAllQuery.getResultList();

			return Response.ok(results, MediaType.APPLICATION_JSON).build();

			// TRUST ");
			// String json = "{\"id\":\"1\",\"name\":\"GOD_IS_VERY_TRUST\"}";

			// return Response.ok(json, MediaType.APPLICATION_JSON).build();

		} catch (Exception e) {
			return null;
		}

	}

}