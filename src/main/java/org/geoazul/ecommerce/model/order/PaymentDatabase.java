package org.geoazul.ecommerce.model.order;



import java.util.Hashtable;

import org.geoazul.ecommerce.model.facade.PaymentService.PaymentRequest; 

 

public class PaymentDatabase extends Database<PaymentRequest> {

	
	  private Hashtable<String, PaymentRequest> data;

	  public PaymentDatabase() {
	    this.data = new Hashtable<String, PaymentRequest>();
	    //0-fail, 1-error, 2-success
	  }

	  @Override
	  public PaymentRequest add(PaymentRequest r) throws DatabaseUnavailableException {
	    return data.put(r.transactionId, r);
	  }

	  @Override
	  public PaymentRequest get(String tId) throws DatabaseUnavailableException {
	    return data.get(tId);
	  }
	
	
}
