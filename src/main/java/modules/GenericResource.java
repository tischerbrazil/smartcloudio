package modules;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/public/concurrency/")
@RequestScoped
public class GenericResource {

  // @Context
  // private UriInfo uriInfo;

   @Resource
   private ManagedExecutorService managedExecutor;
      
    @GET
   	@PermitAll
   	@Produces("application/json")
   	@Path("test")
   	public Response listTest()  {
       	String dd = "{\"campo\":\"Olá\"}";

   		return Response.ok(dd, MediaType.APPLICATION_JSON).build();
      
      }

   @GET
   @Path("simpleJob")
   @Produces(MediaType.TEXT_PLAIN)
	@PermitAll
   public String getText() {
      managedExecutor.submit(() -> {
      });
      return "Job Submitted";
   }
   
   @GET
   @Path("parallelJob")
   @Produces(MediaType.TEXT_PLAIN)
	@PermitAll
   public String getParallelJob() throws ExecutionException, InterruptedException {
      Future future1 = managedExecutor.submit(() -> {
            // This takes some while
         });
      Future future2 = managedExecutor.submit(() -> {
            // This takes some while
      });
      future1.get(); // Wait for job to finish and get result (optionally)
      future2.get();
      return "Jobs completed";
   }
}
