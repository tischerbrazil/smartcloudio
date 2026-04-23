package org.geoazul.ecommerce.model.order;

import org.geoazul.ecommerce.model.facade.InventoryService;
import org.geoazul.ecommerce.model.facade.PaymentService;
import org.geoazul.ecommerce.model.facade.ShippingService;

import com.erp.modules.inventory.entities.Product;

public class OrderServiceFacadeImpl implements OrderServiceFacade
{
	@Override
    public boolean placeOrder(final Long pId) {
        boolean orderFulfilled = false;
        final Product product = new Product();
        product.setId(pId);
        if (InventoryService.isAvailable(product)) {
            final boolean paymentConfirmed = PaymentService.makePayment();
            if (paymentConfirmed) {
                ShippingService.shipProduct(product);
                orderFulfilled = true;
            }
        }
        return orderFulfilled;
    }
}