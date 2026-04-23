package br.bancodobrasil;

import java.util.Optional;
import java.util.UUID;
import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.push.Push;
import jakarta.faces.push.PushContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.fasterxml.jackson.databind.JsonNode;

import br.bancodobrasil.model.BancoBrasilPixPayment;
import jsonb.JacksonUtil;

@Path("/public/webhook")
@RequestScoped
public class BBWebhook {

	
	@Inject
	EntityManager entityManager;
	
	@Inject
	@Push
	PushContext helloWebhook;
	
	private void sendPushMessage(Object message) {
		helloWebhook.send(message);
	}
	
	public Optional<BancoBrasilPixPayment> findBBPixByTxid(UUID txid_uuid) {
		TypedQuery<BancoBrasilPixPayment> query = entityManager
				.createNamedQuery(BancoBrasilPixPayment.PIXPAYMENT_TXID_GET, BancoBrasilPixPayment.class);
		query.setParameter("txid", txid_uuid);
		return Optional.ofNullable(query.getResultStream().findFirst().orElse(null));
	}
	
	
	@POST
	@PermitAll
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(String entityString) {


		
		JsonNode retornoApiPix = JacksonUtil.toJsonNode(entityString);
		
		
		String txid = retornoApiPix.findValue("txid").asText();
		
		String txid1 = txid.substring(0,8);
		String txid2 = txid.substring(8,12);
		String txid3 = txid.substring(12,16);
		String txid4 = txid.substring(16,20);
		String txid5 = txid.substring(20,32);
		
		
		String txidUUIDString = txid1 + "-" + txid2  +
				"-" + txid3  + "-" + txid4 + "-" + txid5;
		
		UUID txidUUID = UUID.fromString(txidUUIDString);
		
		Optional<BancoBrasilPixPayment> bbPixPay = findBBPixByTxid(txidUUID);
		
		
						
	
		String retorno = "{\"index\":" + "" + ", \"value\":" + "" + "}";
		this.sendPushMessage(retorno);

		return Response.ok(String.valueOf(entityString)).build();
		
		
	}

}
