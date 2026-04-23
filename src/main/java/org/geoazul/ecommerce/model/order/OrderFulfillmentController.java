package org.geoazul.ecommerce.model.order;

public class OrderFulfillmentController
{
    OrderServiceFacade facade;
    boolean orderFulfilled;
    
    public OrderFulfillmentController() {
        this.orderFulfilled = false;
    }
    
    public void orderProduct(final Long productId) {
        this.orderFulfilled = this.facade.placeOrder(productId);
    }
}