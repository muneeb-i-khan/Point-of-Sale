package com.increff.pos.flow;

import com.increff.pos.db.pojo.InventoryPojo;
import com.increff.pos.db.pojo.OrderPojo;
import com.increff.pos.model.SalesForm;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderFlowService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private InventoryService inventoryService;

    @Transactional
    public void createOrderAndUpdateInventory(SalesForm salesForm) throws ApiException {
        OrderPojo order = orderService.createOrder(salesForm);
        updateInventoryForSales(salesForm.getItems());
    }

    private void updateInventoryForSales(List<SalesForm.SaleItem> saleItems) throws ApiException {
        for (SalesForm.SaleItem saleItem : saleItems) {
            String barcode = saleItem.getBarcode();
            int quantitySold = saleItem.getQuantity();
            reduceInventory(barcode, quantitySold);
        }
    }

    private void reduceInventory(String barcode, int quantitySold) throws ApiException {
        InventoryPojo inventory = inventoryService.getInventoryByBarcode(barcode);
        if (inventory.getQuantity() < quantitySold) {
            throw new ApiException("Not enough inventory for product with barcode: " + barcode);
        }
        inventory.setQuantity(inventory.getQuantity() - quantitySold);
        inventoryService.updateInventory(inventory.getId(), inventory);
    }
}
