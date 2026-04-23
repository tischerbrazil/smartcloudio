package org.geoazul.ecommerce.model.facade;

import org.geoazul.ecommerce.model.order.DatabaseUnavailableException;
import org.geoazul.ecommerce.model.order.PaymentDatabase;
import org.geoazul.ecommerce.model.order.Service;


/**
 * The PaymentService class receives request from the {@link Commander} and adds
 * to the {@link PaymentDatabase}.
 */

public class PaymentService extends Service {
	
	 public static boolean makePayment() {
	        return true;
	    }

  public class PaymentRequest {
    public String transactionId;
    float payment;
    boolean paid;

    PaymentRequest(String transactionId, float payment) {
      this.transactionId = transactionId;
      this.payment = payment;
      this.paid = false;
    }
  }

  public PaymentService(PaymentDatabase db, Exception...exc) {
    super(db, exc);
  }

  /**
   * Public method which will receive request from {@link Commander}.
   */
  
  public String receiveRequest(Object...parameters) throws DatabaseUnavailableException {
    //it could also be sending a userid, payment details here or something, not added here
    String tId = generateId();
    PaymentRequest req = new PaymentRequest(tId, (float)parameters[0]);
    return updateDb(req);
  }

  protected String updateDb(Object...parameters) throws DatabaseUnavailableException {
    PaymentRequest req = (PaymentRequest) parameters[0];
    if (database.get(req.transactionId) == null || !req.paid) {
      database.add(req);
      req.paid = true;
      return req.transactionId;
    }
    return null;
  }
}
