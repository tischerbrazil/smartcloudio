package org.geoazul.ecommerce.model.order;

public class OrderFulfillmentControllerTest
{
    public void testOrderProduct() throws Exception {
        final OrderFulfillmentController controller = new OrderFulfillmentController();
        controller.facade = (OrderServiceFacade)new OrderServiceFacadeImpl();
        controller.orderProduct(1L);
        final boolean result = controller.orderFulfilled;
    }
}